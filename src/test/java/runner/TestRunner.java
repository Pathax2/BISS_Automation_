// ===================================================================================================================================
// File          : TestRunner.java
// Package       : runner
// Description   : Entry point for the BISS BDD Cucumber automation framework.
//                 Reads ExecutionControl.xlsx, iterates rows marked Y, sets JVM system properties
//                 per test case, invokes Cucumber CLI for each feature file, writes PASS/FAIL back,
//                 and drives the full reporting pipeline:
//                   ReportManager → HtmlReportGenerator → JUnitXmlGenerator
//
// Bamboo Integration:
//   • JUnit XML written to target/surefire-reports/BISS_Execution_Results.xml
//     → Configure "JUnit Parser" task in Bamboo pointing to target/surefire-reports/*.xml
//   • HTML report written to Test_Report/html/
//     → Add as a Bamboo Artifact definition so it appears as a downloadable link per build
//
// ═══════════════════════════════════════════════════════════════════════════════
//  HOW TO TRIGGER TESTS
// ═══════════════════════════════════════════════════════════════════════════════
//  Step 1 : Open ExecutionControl.xlsx
//           Set Execution = Y  on every row you want to run
//           Set Execution = N  on every row you want to skip
//           Save the file
//
//  Step 2 : Right-click TestRunner.java in IntelliJ → Run 'executeSelectedTestCases'
//           OR: mvn test
//
//  ⚠  NEVER right-click a .feature file and run it directly.
//     The feature file has no way to pass system properties to Hooks.
//     TestRunner is the ONLY correct entry point.
//
// ═══════════════════════════════════════════════════════════════════════════════
//  ExecutionControl.xlsx — REQUIRED COLUMNS
// ═══════════════════════════════════════════════════════════════════════════════
//  Execution   │ Y = run │ N = skip
//  TestCase_ID │ Must match .feature filename  e.g. TC_Login_01 → TC_Login_01.feature
//  Description │ Used in reports
//  Environment │ Must match Env column in TestData.xlsx Config sheet  e.g. QA
//  Status      │ Written back by framework: PASS or FAIL
//
//  OPTIONAL columns (safe to add later — framework defaults gracefully if absent):
//  Browser     │ CHROME / FIREFOX / EDGE — defaults to CHROME if column not present
//  Tags        │ Cucumber tags for filtering — used in HTML report only
//
// ═══════════════════════════════════════════════════════════════════════════════
//  TestData.xlsx — HOW TEST DATA IS LOADED
// ═══════════════════════════════════════════════════════════════════════════════
//  TestRunner sets System.setProperty("testcase", "TC_Login_01")
//  Hooks.beforeAllExecution() reads that property
//  Hooks calls: ExcelUtilities.loadCurrentTestDataRow("Data", "TC_Login_01")
//  ExcelUtilities finds the row where TestCase_ID = TC_Login_01 in sheet Data
//  All columns from that row are loaded into memory for this test case
//  Step definitions call iAction(..., "TD:Username") → resolves to "aga6029"
//  Step definitions call iAction(..., "TD:Password") → resolves to "@c3ntst678!!"
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package runner;

import io.cucumber.core.cli.Main;
import org.junit.jupiter.api.Test;
import reporting.HtmlReportGenerator;
import reporting.JUnitXmlGenerator;
import reporting.ReportManager;
import utilities.ExcelUtilities;

import java.io.File;
import java.util.logging.Logger;

public class TestRunner
{
    private static final Logger log = Logger.getLogger(TestRunner.class.getName());

    // -------------------------------------------------------------------------------------------------------------------------------
    // Framework paths — all overridable via -D JVM system properties for Bamboo plan configuration
    // -------------------------------------------------------------------------------------------------------------------------------
    public static final String iExecutionControlFilePath  = System.getProperty(
            "execution.control.path",  "src/test/resources/Execution_Control_File/ExecutionControl.xlsx");

    public static final String iExecutionControlSheetName = System.getProperty(
            "execution.control.sheet", "Sheet1");

    public static final String iFeatureDirectoryPath = System.getProperty(
            "feature.directory.path",  "src/test/resources/Test_Cases/");

    public static final String iStepDefinitionsGlue = System.getProperty(
            "glue.package", "stepdefinitions");

    private static final String iHtmlReportPath = "target/cucumber-reports/";
    private static final String iJsonReportPath = "target/cucumber-json/";

    // ***************************************************************************************************************************************************************************************
    // Function Name : executeSelectedTestCases
    // Description   : JUnit 5 entry point — triggered by Maven Surefire or right-clicking in IntelliJ
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Test
    public void executeSelectedTestCases()
    {
        runFrameworkExecution();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : main
    // Description   : Direct execution entry point — no JUnit required
    // Parameters    : pArgs (String[]) - unused; all config via system properties or Excel
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void main(String[] pArgs)
    {
        runFrameworkExecution();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : runFrameworkExecution
    // Description   : Core execution loop.
    //
    //                 For each row in ExecutionControl.xlsx where Execution = Y:
    //                   1. Reads TestCase_ID and Environment (required columns)
    //                   2. Reads Browser and Tags safely — defaults if columns not present in Excel
    //                   3. Confirms matching .feature file exists on disk
    //                   4. Sets system properties so Hooks.beforeAllExecution() can read them
    //                   5. Hooks fires → finds matching row in TestData.xlsx by TestCase_ID
    //                                 → loads all test data columns into memory (Username, Password etc.)
    //                                 → reads URL from Config sheet for the given Environment
    //                                 → launches browser and navigates to URL
    //                   6. Cucumber runs all scenarios in the feature file
    //                   7. PASS or FAIL written back to Status column in ExecutionControl.xlsx
    //                   8. Moves to next Y row — does NOT abort the suite on a single test failure
    //
    //                 After the loop:
    //                   - HTML management report generated → Test_Report/html/
    //                   - JUnit XML generated → target/surefire-reports/ (Bamboo dashboard)
    //
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void runFrameworkExecution()
    {
        ExcelUtilities iExcel     = new ExcelUtilities(iExecutionControlFilePath);
        int            iRowCount  = iExcel.getRowCount(iExecutionControlSheetName);
        boolean        iAnyRun    = false;
        int            iPassCount = 0;
        int            iFailCount = 0;

        // Suite-level defaults for ReportManager
        String iSuiteBrowser     = System.getProperty("browser",     "CHROME");
        String iSuiteEnvironment = System.getProperty("environment", "");
        ReportManager.startSuite(iSuiteEnvironment, iSuiteBrowser);

        for (int iRowNumber = 1; iRowNumber <= iRowCount; iRowNumber++)
        {
            String iTestCaseID   = "";
            String iEnvironment  = "";
            String iBrowser      = "";
            String iDescription  = "";
            String iTags         = "";
            String iErrorMessage = "";
            String iScreenshot   = "";
            long   iStartTime    = 0L;

            try
            {
                // ── Check Execution flag ─────────────────────────────────────────────────────────
                String iExecFlag = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Execution").trim();

                if (!iExecFlag.equalsIgnoreCase("Y"))
                {
                    log.info("[RUNNER] Row " + iRowNumber + " skipped (Execution=" + iExecFlag + ")");
                    continue;
                }

                iAnyRun = true;

                // ── Read required columns ────────────────────────────────────────────────────────
                iTestCaseID  = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "TestCase_ID").trim();
                iEnvironment = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Environment").trim();
                iDescription = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Description").trim();

                if (iTestCaseID.isEmpty())
                {
                    throw new RuntimeException("TestCase_ID is blank at row " + iRowNumber);
                }

                if (iEnvironment.isEmpty())
                {
                    throw new RuntimeException("Environment is blank for TestCase_ID : " + iTestCaseID);
                }

                // ── Read optional columns — safe fallback when column does not exist in Excel ────
                //
                //    FIX: The original code used getCellValue() directly for Browser and Tags.
                //    If those columns don't exist in ExecutionControl.xlsx it throws an exception
                //    and the entire suite crashes before a single test runs.
                //    safeGetCell() catches that exception and returns the supplied default instead.
                //
                //    Browser → defaults to CHROME  (or -Dbrowser=FIREFOX if set via Maven/Bamboo)
                //    Tags    → defaults to ""       (tags column is used in HTML report only)
                //
                iBrowser = safeGetCell(iExcel, iExecutionControlSheetName, iRowNumber, "Browser",
                        System.getProperty("browser", "CHROME"));

                iTags = safeGetCell(iExcel, iExecutionControlSheetName, iRowNumber, "Tags", "");

                // ── Locate the feature file ──────────────────────────────────────────────────────
                //    TestCase_ID in Excel MUST exactly match the .feature filename:
                //    TC_Login_01  →  src/test/resources/Test_Cases/TC_Login_01.feature
                String iFeaturePath = iFeatureDirectoryPath + iTestCaseID + ".feature";

                if (!new File(iFeaturePath).exists())
                {
                    throw new RuntimeException(
                            "Feature file not found : " + iFeaturePath + "\n"
                                    + "Ensure a file named '" + iTestCaseID + ".feature' exists in "
                                    + iFeatureDirectoryPath);
                }

                // ── Push values to Hooks via system properties ───────────────────────────────────
                //    Hooks.beforeAllExecution() reads exactly these three properties.
                //    It then uses "testcase" value to load the matching row from TestData.xlsx.
                System.setProperty("testcase",    iTestCaseID);
                System.setProperty("environment", iEnvironment);
                System.setProperty("browser",     iBrowser);

                printExecutionHeader(iTestCaseID, iEnvironment, iBrowser, iFeaturePath);
                new File(iHtmlReportPath).mkdirs();
                new File(iJsonReportPath).mkdirs();

                // ── Run Cucumber for this feature file ───────────────────────────────────────────
                iStartTime = System.currentTimeMillis();

                byte iExitCode = Main.run(
                        new String[]{
                                iFeaturePath,
                                "--glue",   iStepDefinitionsGlue,
                                "--plugin", "pretty",
                                "--plugin", "summary",
                                "--plugin", "html:" + iHtmlReportPath + iTestCaseID + ".html",
                                "--plugin", "json:" + iJsonReportPath + iTestCaseID + ".json"
                        },
                        Thread.currentThread().getContextClassLoader()
                );

                long iDuration = System.currentTimeMillis() - iStartTime;

                // ── Write result back to ExecutionControl.xlsx ───────────────────────────────────
                if (iExitCode == 0)
                {
                    iExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "PASS");
                    log.info("[RUNNER] ✓ PASS : " + iTestCaseID + "  (" + formatDuration(iDuration) + ")");
                    iPassCount++;

                    ReportManager.recordResult(iTestCaseID, iDescription, "PASS",
                            iDuration, "", "", iTags, iEnvironment, iBrowser);
                }
                else
                {
                    // Hooks.afterAllExecution() publishes these via System.setProperty after Cucumber exits
                    iErrorMessage = System.getProperty("lastFailureReason." + iTestCaseID, "");
                    iScreenshot   = System.getProperty("lastScreenshotPath." + iTestCaseID, "");

                    iExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "FAIL");
                    log.severe("[RUNNER] ✗ FAIL : " + iTestCaseID + "  (" + formatDuration(iDuration) + ")");
                    iFailCount++;

                    ReportManager.recordResult(iTestCaseID, iDescription, "FAIL",
                            iDuration, iErrorMessage, iScreenshot, iTags, iEnvironment, iBrowser);
                }
            }
            catch (Exception iException)
            {
                // Row-level error — log it, mark FAIL, continue to next row
                long iDuration = iStartTime > 0 ? System.currentTimeMillis() - iStartTime : 0L;

                try
                {
                    if (!iTestCaseID.isEmpty())
                    {
                        iExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "FAIL");
                    }
                }
                catch (Exception ignored) {}

                log.severe("[RUNNER] ERROR — Row=" + iRowNumber
                        + "  TestCase_ID=" + iTestCaseID
                        + "\n           Reason: " + iException.getMessage());

                iFailCount++;

                ReportManager.recordResult(iTestCaseID, iDescription, "ERROR",
                        iDuration, iException.getMessage(), "", iTags, iEnvironment, iBrowser);
            }
        }

        // ── Reporting pipeline — always runs regardless of pass/fail count ───────────────────────
        ReportManager.endSuite();
        printExecutionSummary(iPassCount, iFailCount);

        try   { HtmlReportGenerator.generate(); }
        catch (Exception e) { log.severe("[RUNNER] HTML report failed : " + e.getMessage()); }

        try   { JUnitXmlGenerator.generate(); }
        catch (Exception e) { log.severe("[RUNNER] JUnit XML failed : " + e.getMessage()); }

        // ── Suite outcome ────────────────────────────────────────────────────────────────────────
        if (!iAnyRun)
        {
            throw new RuntimeException(
                    "No rows with Execution=Y found in ExecutionControl.xlsx.\n"
                            + "Set at least one row to Y, save the file, and re-run.");
        }

        if (iFailCount > 0)
        {
            throw new RuntimeException(
                    "Suite completed with " + iFailCount + " failure(s). "
                            + "Check Status column in ExecutionControl.xlsx and HTML report in Test_Report/html/");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : safeGetCell
    // Description   : Reads a cell by column name. Returns pDefault if the column does not exist
    //                 in the Excel file or if the cell is blank. Never throws.
    //                 Used for optional columns (Browser, Tags) so the framework works correctly
    //                 even when those columns are not present in ExecutionControl.xlsx.
    // Parameters    : pExcel      (ExcelUtilities) - open workbook instance
    //                 pSheet      (String)         - sheet name
    //                 pRow        (int)            - 1-based row number
    //                 pColumnName (String)         - column header to read
    //                 pDefault    (String)         - value returned if column absent or blank
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 11-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String safeGetCell(ExcelUtilities pExcel, String pSheet,
                                      int pRow, String pColumnName, String pDefault)
    {
        try
        {
            String iValue = pExcel.getCellValue(pSheet, pRow, pColumnName).trim();
            return iValue.isEmpty() ? pDefault : iValue;
        }
        catch (Exception iException)
        {
            // Column does not exist in this Excel file — return the default silently
            return pDefault;
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Console output helpers
    // -------------------------------------------------------------------------------------------------------------------------------
    private static void printExecutionHeader(String pID, String pEnv, String pBrowser, String pPath)
    {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  STARTING   : " + pID);
        System.out.println("║  Environment: " + pEnv);
        System.out.println("║  Browser    : " + pBrowser);
        System.out.println("║  Feature    : " + pPath);
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }

    private static void printExecutionSummary(int pPass, int pFail)
    {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  EXECUTION SUMMARY");
        System.out.println("║  Total : " + (pPass + pFail));
        System.out.println("║  PASS  : " + pPass);
        System.out.println("║  FAIL  : " + pFail);
        System.out.println("║  HTML  : Test_Report/html/");
        System.out.println("║  XML   : target/surefire-reports/BISS_Execution_Results.xml");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }

    private static String formatDuration(long pMs)
    {
        long iSecs = (pMs / 1000) % 60;
        long iMins = pMs / (1000 * 60);
        return iMins > 0 ? iMins + "m " + iSecs + "s" : iSecs + "s";
    }
}
