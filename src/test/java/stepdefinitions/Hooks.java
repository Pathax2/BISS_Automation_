// ===================================================================================================================================
// File          : Hooks.java
// Package       : stepdefinitions
// Description   : Cucumber lifecycle hooks for the BISS automation framework.
//                 Manages the full test case execution pipeline in order:
//
//                   @BeforeAll  → killProcessByName()      : clean up stale browser processes
//                               → loadCurrentTestDataRow() : load test data from TestData.xlsx
//                               → bootstrapRuntimeHerd()   : DB-driven herd + username resolution
//                               → launchBrowser()          : open browser and navigate to URL
//                               → startWordReport()        : create Word report document
//
//                   @Before     → SoftAssertManager.reset()   : clear soft assertions per scenario
//                               → RetryAnalyser.clearRetry()  : reset retry state per scenario
//
//                   @After      → SoftAssertManager.assertAll() : flush soft assertions
//                               → takeScreenshot()              : capture on failure only (auto)
//                               → addScreenshotToReport()       : add failure screenshot to Word doc
//
//                   @AfterAll   → publish system properties     : failure reason + screenshot path for TestRunner
//                               → finalizeWordReport()          : write Word doc to disk
//                               → closeBrowser()                : quit browser and clean ThreadLocal
//
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//  SCREENSHOT FROM STEP DEFINITIONS
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//  iDocument and iDocPath are public static — call these two lines anywhere in any step:
//
//    CommonFunctions.addScreenshotToReport(Hooks.iDocument, Hooks.iDocPath, Hooks.iTestCaseID);
//
//  Screenshots are added to the Word report in the order they are called.
//  The report is written to disk once at the end of the test case in @AfterAll.
//
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//  RUNTIME HERD RESOLUTION — System Properties
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//  herd.year         : scheme year to query (default: 2026)
//  herd.limit        : max candidate herds to fetch from DATA DB (default: 25)
//  herd.retry        : number of re-fetch attempts if no USERNAME found (default: 3)
//  usernameOverride  : skip DB resolution and use this username directly
//
//  After resolution, these are published as system properties for TD: resolution in iAction():
//    TD:HerdNumber   → resolved herd number
//    TD:Username     → resolved agent username
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import utilities.ExcelUtilities;
import utilities.RetryAnalyser;
import utilities.SoftAssertManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Hooks
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // Public state — shared with step definitions for screenshot and test identity access
    // iDocumentS and iDocPath are used by step definitions to add screenshots mid-scenario
    // -------------------------------------------------------------------------------------------------------------------------------
    public static XWPFDocument iDocument    = null;
    public static String       iDocPath     = "";
    public static String       iTestCaseID  = "";
    public static String       iEnvironment = "";
    public static String       iUrl         = "";

    // ── Runtime herd and username — resolved from DB before any scenario runs ──────────────────
    // Accessible from step definitions as Hooks.RUNTIME_HERD and Hooks.RUNTIME_USERNAME
    // Also published as system properties TD:HerdNumber and TD:Username for TD: resolution
    public static String RUNTIME_HERD     = null;
    public static String RUNTIME_USERNAME = null;

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private state — internal to Hooks lifecycle only
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final StringBuilder iAccumulatedErrors  = new StringBuilder();
    private static       String        iLastScreenshotPath = "";

    // -------------------------------------------------------------------------------------------------------------------------------
    // Framework file paths and defaults — loaded from resources
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String iTestDataFilePath  = "src/test/resources/Test_Data/TestData.xlsx";
    private static final String iTestDataSheetName = "Data";
    private static final String iConfigSheetName   = "Config";
    private static final String iDefaultBrowser    = "CHROME";


    // ===============================================================================================================================
    // SECTION 1 : @BeforeAll — Pre-Execution Setup
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeAllExecution
    // Description   : Runs once before all scenarios in this test case execution.
    //                 Performs the full setup pipeline in sequence:
    //                   1. Kill stale browser and driver processes
    //                   2. Read testcase and environment system properties
    //                   3. Load test data row from TestData.xlsx for this TestCase_ID
    //                   4. Read URL from Config sheet for the resolved environment
    //                   5. Bootstrap runtime herd + username via DB (DATA → INET chain)
    //                   6. Publish TD:HerdNumber and TD:Username as system properties
    //                   7. Write resolved values back to TestData.xlsx
    //                   8. Launch browser and navigate to application URL
    //                   9. Create Word report document for this test case
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Precondition  : testcase and environment system properties must be set by TestRunner
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @BeforeAll
    public static void beforeAllExecution()
    {
        try
        {
            // ── Step 1 : Clean up stale browser and driver processes ──────────────────────────────
            killProcessByName("chrome");
            killProcessByName("chromedriver");

            // ── Step 2 : Read required system properties set by TestRunner ───────────────────────
            iTestCaseID  = System.getProperty("testcase",    "").trim();
            iEnvironment = System.getProperty("environment", "").trim();

            if (iTestCaseID.isEmpty())  { throw new RuntimeException("TestCase_ID system property is missing."); }
            if (iEnvironment.isEmpty()) { throw new RuntimeException("Environment system property is missing."); }

            // Reset state from any previous execution on this JVM
            iAccumulatedErrors.setLength(0);
            iLastScreenshotPath = "";
            iDocument           = null;
            iDocPath            = "";

            CommonFunctions.log.info("========== EXECUTION START : " + iTestCaseID + " ==========");

            // ── Step 3 : Load test data and description cache ─────────────────────────────────────
            CommonFunctions.loadDescriptionCache();

            ExcelUtilities iTestDataExcel = new ExcelUtilities(iTestDataFilePath);
            iTestDataExcel.loadCurrentTestDataRow(iTestDataSheetName, iTestCaseID);

            // ── Step 4 : Resolve application URL from Config sheet ────────────────────────────────
            int iConfigRow = iTestDataExcel.findRow(iConfigSheetName, "Env", iEnvironment);
            if (iConfigRow == -1)
            {
                throw new RuntimeException("Environment '" + iEnvironment + "' not found in Config sheet.");
            }

            iUrl = iTestDataExcel.getCellValue(iConfigSheetName, iConfigRow, "URL").trim();
            if (iUrl.isEmpty())
            {
                throw new RuntimeException("URL is blank for environment : " + iEnvironment);
            }

            // ── Step 5 : Bootstrap runtime herd + username from DB ────────────────────────────────
            bootstrapRuntimeHerdAndUsername(iTestDataExcel);

            // ── Step 6 : Launch browser ───────────────────────────────────────────────────────────
            String iBrowserType = System.getProperty("browser", iDefaultBrowser).trim().toUpperCase();
            CommonFunctions.launchBrowser(iBrowserType, iUrl);

            // ── Step 7 : Create Word report document ──────────────────────────────────────────────
            Object[] iReportObjects = CommonFunctions.startWordReport(iTestCaseID);
            iDocument = (XWPFDocument) iReportObjects[0];
            iDocPath  = String.valueOf(iReportObjects[1]);

            CommonFunctions.log.info("BeforeAll complete | URL=" + iUrl + " | Browser=" + iBrowserType + " | Report=" + iDocPath);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("BeforeAll failed for [" + iTestCaseID + "] : " + iException.getMessage(), iException);
        }
    }


    // ===============================================================================================================================
    // SECTION 2 : @Before — Pre-Scenario Setup
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeScenarioExecution
    // Description   : Runs before each individual scenario within the test case.
    //                 Resets soft assertion state and retry state so each scenario starts clean.
    //
    // Parameters    : pScenario (Scenario) - Cucumber scenario object for the current scenario
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Before
    public void beforeScenarioExecution(Scenario pScenario)
    {
        SoftAssertManager.reset();
        RetryAnalyser.clearRetryState();
        CommonFunctions.log.info("---------- Scenario START : " + pScenario.getName() + " ----------");
    }


    // ===============================================================================================================================
    // SECTION 3 : @After — Post-Scenario Cleanup
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterScenarioExecution
    // Description   : Runs after each individual scenario within the test case.
    //                 Flushes soft assertions (fails the scenario if any were collected).
    //                 On failure: captures a screenshot, adds it to the Word report,
    //                 and appends the failure message to the accumulated error log.
    //                 On pass: logs a pass confirmation.
    //
    // Parameters    : pScenario (Scenario) - Cucumber scenario object for the current scenario
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @After
    public void afterScenarioExecution(Scenario pScenario)
    {
        // Flush any collected soft assertions — will mark scenario as failed if any exist
        SoftAssertManager.assertAll();

        try
        {
            if (pScenario.isFailed())
            {
                CommonFunctions.log.severe("---------- Scenario FAILED : " + pScenario.getName() + " ----------");

                // Accumulate failure details for TestRunner → ReportManager pickup in @AfterAll
                iAccumulatedErrors
                        .append("Scenario [")
                        .append(pScenario.getName())
                        .append("] FAILED | Status=")
                        .append(pScenario.getStatus().name())
                        .append("\n");

                // ── Automatic failure screenshot ──────────────────────────────────────────────────
                try
                {
                    String iSafeName = iTestCaseID + "_" + pScenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                    String iShotPath = CommonFunctions.takeScreenshot(iSafeName);
                    iLastScreenshotPath = iShotPath;

                    if (iDocument != null && !iDocPath.isEmpty())
                    {
                        CommonFunctions.addScreenshotToReport(iDocument, iDocPath, iTestCaseID);
                    }
                }
                catch (Exception iShotException)
                {
                    CommonFunctions.log.severe("Failure screenshot capture failed : " + iShotException.getMessage());
                }
            }
            else
            {
                CommonFunctions.log.info("---------- Scenario PASSED : " + pScenario.getName() + " ----------");
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("After hook error : " + iException.getMessage());
        }
    }


    // ===============================================================================================================================
    // SECTION 4 : @AfterAll — Post-Execution Teardown
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterAllExecution
    // Description   : Runs once after all scenarios in this test case execution have completed.
    //                 Performs teardown in sequence:
    //                   1. Publish failure reason and screenshot path as system properties
    //                      so TestRunner can read them for ReportManager
    //                   2. Finalize and write the Word report to disk
    //                   3. Close the browser and clean up ThreadLocal driver references
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @AfterAll
    public static void afterAllExecution()
    {
        // ── Step 1 : Publish failure details for TestRunner → ReportManager pickup ─────────────
        try
        {
            if (iAccumulatedErrors.length() > 0)
            {
                System.setProperty("lastFailureReason." + iTestCaseID, iAccumulatedErrors.toString().trim());
            }
            if (!iLastScreenshotPath.isEmpty())
            {
                System.setProperty("lastScreenshotPath." + iTestCaseID, iLastScreenshotPath);
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Failed to publish failure system properties : " + iException.getMessage());
        }

        // ── Step 2 : Finalize Word report — writes all accumulated screenshots to disk ─────────
        try
        {
            if (iDocument != null && !iDocPath.isEmpty())
            {
                CommonFunctions.finalizeWordReport(iDocument, iDocPath);
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Word report finalization failed : " + iException.getMessage());
        }

        // ── Step 3 : Close browser and clean up driver ThreadLocal ────────────────────────────
        try
        {
            CommonFunctions.closeBrowser();
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Browser close failed : " + iException.getMessage());
        }

        CommonFunctions.log.info("========== EXECUTION END : " + iTestCaseID + " ==========");
    }


    // ===============================================================================================================================
    // SECTION 5 : Runtime Herd and Username Bootstrap
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : bootstrapRuntimeHerdAndUsername
    // Description   : Resolves the herd number and agent username to use for this test case execution
    //                 via a two-database lookup chain (DATA → INET).
    //
    //                 Resolution order:
    //                   1. If -DusernameOverride=... is set — use that username directly,
    //                      keeping the herd from the TestData.xlsx sheet if present
    //                   2. Otherwise — query DATA DB for candidate herds (up to herd.limit),
    //                      shuffle them for randomness, then check each one in INET DB
    //                      until a USERNAME is found
    //                   3. If no INET username found after herd.retry attempts — fall back to
    //                      the username from TestData.xlsx if present, or throw to fail fast
    //
    //                 After resolution:
    //                   - Publishes TD:HerdNumber and TD:Username as system properties
    //                   - Sets Hooks.RUNTIME_HERD and Hooks.RUNTIME_USERNAME
    //                   - Writes resolved values back to TestData.xlsx for this TestCase_ID
    //
    //                 Tunable via system properties (no code changes needed):
    //                   -Dherd.year=2026          scheme year for DATA query (default: 2026)
    //                   -Dherd.limit=25           max candidate herds to fetch (default: 25)
    //                   -Dherd.retry=3            re-fetch attempts if no match (default: 3)
    //                   -DusernameOverride=agent1 bypass DB and use this username directly
    //
    // Parameters    : pTestDataExcel (ExcelUtilities) - open workbook instance for write-back
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void bootstrapRuntimeHerdAndUsername(ExcelUtilities pTestDataExcel)
    {
        // ── Read tuning knobs from system properties ──────────────────────────────────────────
        String iHerdYear         = System.getProperty("herd.year",        "2026").trim();
        String iHerdLimit        = System.getProperty("herd.limit",       "25").trim();
        String iHerdRetry        = System.getProperty("herd.retry",       "3").trim();
        String iUsernameOverride = System.getProperty("usernameOverride", "").trim();

        // ── Read existing values from TestData.xlsx sheet (used as fallback) ─────────────────
        String iHerdFromSheet  = "";
        String iUnameFromSheet = "";

        try { iHerdFromSheet  = ExcelUtilities.getCurrentTestDataValue("HerdNumber"); } catch (Exception ignored) {}
        try { iUnameFromSheet = ExcelUtilities.getCurrentTestDataValue("Username");   } catch (Exception ignored) {}

        String iRuntimeHerd     = null;
        String iRuntimeUsername = null;

        if (!iUsernameOverride.isEmpty())
        {
            // ── Override path : use the provided username directly ────────────────────────────
            iRuntimeUsername = iUsernameOverride;
            iRuntimeHerd     = iHerdFromSheet;
            CommonFunctions.log.info("[BOOT] usernameOverride applied | Username=" + iRuntimeUsername + " | Herd(sheet)=" + iRuntimeHerd);
        }
        else
        {
            // ── DB resolution path : DATA → INET chain ────────────────────────────────────────
            final int iMaxAttempts = Integer.parseInt(iHerdRetry);
            final int iLimit       = Integer.parseInt(iHerdLimit);
            boolean   iFound       = false;

            for (int iAttempt = 1; iAttempt <= iMaxAttempts && !iFound; iAttempt++)
            {
                // ── Fetch candidate herds from DATA DB ────────────────────────────────────────
                database.DBRouter.runDB("DATA", "List of herds with no errors at all",
                        iHerdYear, String.valueOf(iLimit), "");

                List<Map<String, Object>> iRows = database.DBRouter.getRows();

                if (iRows == null || iRows.isEmpty())
                {
                    throw new RuntimeException(
                        "DATA DB returned no herds for year=" + iHerdYear + ", limit=" + iLimit);
                }

                // ── Build deduplicated candidate list and shuffle for randomness ──────────────
                List<String> iCandidates = new ArrayList<>();

                for (Map<String, Object> iRow : iRows)
                {
                    String iHerd = Objects.toString(iRow.get("APP_HERD_NO"), "").trim();
                    if (!iHerd.isEmpty() && !iCandidates.contains(iHerd))
                    {
                        iCandidates.add(iHerd);
                    }
                }

                Collections.shuffle(iCandidates, new Random(System.nanoTime()));

                CommonFunctions.log.info("[BOOT] Attempt " + iAttempt + "/" + iMaxAttempts
                        + " | Candidates: " + iCandidates.size()
                        + " | Year=" + iHerdYear + " | Limit=" + iLimit);

                // ── Try each candidate in INET DB until a USERNAME is returned ────────────────
                int iIdx = 0;

                for (String iCandidate : iCandidates)
                {
                    iIdx++;
                    CommonFunctions.log.info("[BOOT] Candidate [" + iIdx + "/" + iCandidates.size() + "] : " + iCandidate);

                    database.DBRouter.runDB("INET", "Get Login Id for herd", iCandidate);
                    String iCandidateUser = database.DBRouter.getValue("USERNAME");

                    if (iCandidateUser != null && !iCandidateUser.isBlank())
                    {
                        iRuntimeHerd     = iCandidate;
                        iRuntimeUsername = iCandidateUser.trim();
                        CommonFunctions.log.info("[BOOT] SELECTED | Herd=" + iRuntimeHerd + " | Username=" + iRuntimeUsername);
                        iFound = true;
                        break;
                    }
                    else
                    {
                        CommonFunctions.log.warning("[BOOT] No USERNAME in INET for herd=" + iCandidate + " — trying next candidate.");
                    }
                }

                if (!iFound)
                {
                    CommonFunctions.log.warning("[BOOT] Attempt " + iAttempt + " exhausted all candidates."
                            + " Re-fetching fresh set of " + iLimit + " candidates from DATA...");
                }
            }

            // ── Fallback : use sheet values if DB resolution failed ───────────────────────────
            if (!iFound)
            {
                if (!iUnameFromSheet.isBlank())
                {
                    iRuntimeUsername = iUnameFromSheet;
                    iRuntimeHerd     = (iRuntimeHerd == null || iRuntimeHerd.isBlank())
                                       ? iHerdFromSheet
                                       : iRuntimeHerd;

                    CommonFunctions.log.warning("[BOOT] Fallback to sheet values | Username=" + iRuntimeUsername + " | Herd=" + iRuntimeHerd);
                }
                else
                {
                    throw new RuntimeException(
                        "INET DB returned no USERNAME after " + iHerdRetry + " attempt(s). "
                        + "Try increasing -Dherd.limit or use -DusernameOverride=<agent> for this run.");
                }
            }
        }

        // ── Publish as system properties for TD: resolution in step definitions ───────────────
        System.setProperty("TD:HerdNumber", iRuntimeHerd     == null ? "" : iRuntimeHerd);
        System.setProperty("TD:Username",   iRuntimeUsername == null ? "" : iRuntimeUsername);

        Hooks.RUNTIME_HERD     = iRuntimeHerd;
        Hooks.RUNTIME_USERNAME = iRuntimeUsername;

        // ── Write resolved values back to TestData.xlsx for this TestCase_ID ─────────────────
        try
        {
            int iDataRowIdx = pTestDataExcel.findRow(iTestDataSheetName, "TestCase_ID", iTestCaseID);

            if (iDataRowIdx != -1)
            {
                pTestDataExcel.setCellValue(iTestDataSheetName, iDataRowIdx, "HerdNumber",
                        iRuntimeHerd == null ? "" : iRuntimeHerd);
                pTestDataExcel.setCellValue(iTestDataSheetName, iDataRowIdx, "Username",
                        iRuntimeUsername == null ? "" : iRuntimeUsername);

                CommonFunctions.log.info("[BOOT→Excel] Write-back complete | HerdNumber=" + iRuntimeHerd
                        + " | Username=" + iRuntimeUsername);
            }
            else
            {
                CommonFunctions.log.warning("[BOOT→Excel] Row not found for TestCase_ID=" + iTestCaseID + " — write-back skipped.");
            }
        }
        catch (Exception iExcelException)
        {
            CommonFunctions.log.warning("[BOOT→Excel] Write-back failed (non-fatal) : " + iExcelException.getMessage());
        }
    }


    // ===============================================================================================================================
    // SECTION 6 : Process Management
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : killProcessByName
    // Description   : Forcefully terminates all running processes matching the given name.
    //                 Called at the start of @BeforeAll to clean up stale chrome and chromedriver
    //                 processes from previous test runs that may not have exited cleanly.
    //
    //                 OS detection:
    //                   Windows — taskkill /F /IM processname.exe /T  (force + kill child tree)
    //                   Mac/Linux — pkill -f processname
    //
    //                 Non-fatal — if the process is not running or the kill fails, a warning is
    //                 logged and execution continues. This should never block the test from starting.
    //
    // Parameters    : pProcessName (String) - process name to kill (e.g. "chrome", "chromedriver")
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void killProcessByName(String pProcessName)
    {
        String iOs      = System.getProperty("os.name").toLowerCase();
        String iCommand;

        try
        {
            if (iOs.contains("win"))
            {
                // Windows: append .exe if not already present; /F = force, /T = kill child processes
                String iProcessWithExe = pProcessName.endsWith(".exe") ? pProcessName : pProcessName + ".exe";
                iCommand = "taskkill /F /IM " + iProcessWithExe + " /T";
            }
            else
            {
                // Mac / Linux
                iCommand = "pkill -f " + pProcessName;
            }

            Runtime.getRuntime().exec(iCommand);
            CommonFunctions.log.info("[BOOT] Process cleaned up : " + pProcessName);
        }
        catch (Exception iException)
        {
            CommonFunctions.log.warning("[BOOT] Could not kill process '" + pProcessName + "' (non-fatal) : " + iException.getMessage());
        }
    }
}
