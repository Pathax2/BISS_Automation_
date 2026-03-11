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
    // Description   : JUnit 5 entry point — triggered by Maven Surefire or IDE
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
    // Parameters    : pArgs (String[]) - unused; all config via system properties
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void main(String[] pArgs)
    {
        runFrameworkExecution();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : runFrameworkExecution
    // Description   : Core execution loop with full reporting pipeline integration.
    //                 Per each Y-flagged row:
    //                   1. Reads TestCase_ID, Description, Environment, Browser, Tags
    //                   2. Validates feature file on disk
    //                   3. Times the full Cucumber run
    //                   4. Records result (status, duration, error, screenshot) in ReportManager
    //                   5. Writes PASS/FAIL back to ExecutionControl.xlsx
    //                 Post-loop: generates HTML + JUnit XML reports
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

        // Suite-level defaults for ReportManager — first Y-row values will dominate,
        // but we prime with JVM props to handle Bamboo plan-level overrides
        String iSuiteBrowser     = System.getProperty("browser",     "CHROME");
        String iSuiteEnvironment = System.getProperty("environment", "");
        ReportManager.startSuite(iSuiteEnvironment, iSuiteBrowser);

        for (int iRowNumber = 1; iRowNumber <= iRowCount; iRowNumber++)
        {
            String iTestCaseID    = "";
            String iEnvironment   = "";
            String iBrowser       = "";
            String iDescription   = "";
            String iTags          = "";
            String iErrorMessage  = "";
            String iScreenshot    = "";
            long   iStartTime     = 0L;

            try
            {
                String iExecFlag = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Execution").trim();
                if (!iExecFlag.equalsIgnoreCase("Y")) { continue; }

                iAnyRun = true;

                iTestCaseID  = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "TestCase_ID").trim();
                iEnvironment = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Environment").trim();
                iDescription = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Description").trim();
                iTags        = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Tags").trim();
                iBrowser     = iExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Browser").trim();

                if (iBrowser.isEmpty())  { iBrowser     = System.getProperty("browser",     "CHROME"); }
                if (iTestCaseID.isEmpty())  { throw new RuntimeException("TestCase_ID blank at row : " + iRowNumber); }
                if (iEnvironment.isEmpty()) { throw new RuntimeException("Environment blank for : " + iTestCaseID); }

                String iFeaturePath = iFeatureDirectoryPath + iTestCaseID + ".feature";
                if (!new File(iFeaturePath).exists())
                {
                    throw new RuntimeException("Feature file not found : " + iFeaturePath);
                }

                System.setProperty("testcase",    iTestCaseID);
                System.setProperty("environment", iEnvironment);
                System.setProperty("browser",     iBrowser);

                printExecutionHeader(iTestCaseID, iEnvironment, iBrowser, iFeaturePath);
                new File(iHtmlReportPath).mkdirs();
                new File(iJsonReportPath).mkdirs();

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

                if (iExitCode == 0)
                {
                    iExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "PASS");
                    log.info("[RUNNER] PASS : " + iTestCaseID);
                    iPassCount++;

                    ReportManager.recordResult(iTestCaseID, iDescription, "PASS",
                            iDuration, "", "", iTags, iEnvironment, iBrowser);
                }
                else
                {
                    // Hooks stores failure reason and screenshot path into system properties
                    // using keys: lastFailureReason.<TestCaseID> and lastScreenshotPath.<TestCaseID>
                    iErrorMessage = System.getProperty("lastFailureReason." + iTestCaseID, "");
                    iScreenshot   = System.getProperty("lastScreenshotPath." + iTestCaseID, "");

                    iExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "FAIL");
                    log.severe("[RUNNER] FAIL : " + iTestCaseID);
                    iFailCount++;

                    ReportManager.recordResult(iTestCaseID, iDescription, "FAIL",
                            iDuration, iErrorMessage, iScreenshot, iTags, iEnvironment, iBrowser);
                }
            }
            catch (Exception iException)
            {
                long iDuration = iStartTime > 0 ? System.currentTimeMillis() - iStartTime : 0L;

                try
                {
                    if (!iTestCaseID.isEmpty())
                    {
                        iExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "FAIL");
                    }
                }
                catch (Exception ignored) {}

                log.severe("[RUNNER] ERROR row=" + iRowNumber + " ID=" + iTestCaseID
                        + " | " + iException.getMessage());
                iFailCount++;

                ReportManager.recordResult(iTestCaseID, iDescription, "ERROR",
                        iDuration, iException.getMessage(), "", iTags, iEnvironment, iBrowser);
            }
        }

        // -------------------------------------------------------------------------------------------------------------------------------
        // REPORTING PIPELINE — always runs, even if all tests failed
        // -------------------------------------------------------------------------------------------------------------------------------
        ReportManager.endSuite();
        printExecutionSummary(iPassCount, iFailCount);

        try   { HtmlReportGenerator.generate(); }
        catch (Exception e) { log.severe("[RUNNER] HTML report failed : " + e.getMessage()); }

        try   { JUnitXmlGenerator.generate(); }
        catch (Exception e) { log.severe("[RUNNER] JUnit XML failed : " + e.getMessage()); }

        // -------------------------------------------------------------------------------------------------------------------------------
        // Suite outcome — fail JUnit test if any test case failed (Bamboo reads JUnit result)
        // -------------------------------------------------------------------------------------------------------------------------------
        if (!iAnyRun)
        {
            throw new RuntimeException("No rows with Execution=Y in ExecutionControl.xlsx.");
        }

        if (iFailCount > 0)
        {
            throw new RuntimeException("Suite completed with " + iFailCount
                    + " failure(s). Check HTML report and Bamboo JUnit results.");
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Console output helpers
    // -------------------------------------------------------------------------------------------------------------------------------
    private static void printExecutionHeader(String pID, String pEnv, String pBrowser, String pPath)
    {
        System.out.println("\n======================================================================");
        System.out.println("  STARTING   : " + pID);
        System.out.println("  Environment: " + pEnv);
        System.out.println("  Browser    : " + pBrowser);
        System.out.println("  Feature    : " + pPath);
        System.out.println("======================================================================");
    }

    private static void printExecutionSummary(int pPass, int pFail)
    {
        System.out.println("\n======================================================================");
        System.out.println("  EXECUTION SUMMARY");
        System.out.println("  Total  : " + (pPass + pFail));
        System.out.println("  PASS   : " + pPass);
        System.out.println("  FAIL   : " + pFail);
        System.out.println("  HTML   : Test_Report/html/");
        System.out.println("  XML    : target/surefire-reports/BISS_Execution_Results.xml");
        System.out.println("======================================================================");
    }
}