package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import reporting.ReportManager;
import utilities.ExcelUtilities;
import utilities.RetryAnalyser;
import utilities.SoftAssertManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Hooks
{
    public static XWPFDocument iDocument    = null;
    public static String       iDocPath     = "";
    public static String       iTestCaseID  = "";
    public static String       iEnvironment = "";
    public static String       iUrl         = "";

    private static final StringBuilder iAccumulatedErrors  = new StringBuilder();
    private static String              iLastScreenshotPath = "";
    private static long                iScenarioStartTime  = 0L;

    private static final String iTestDataFilePath  = "src/test/resources/Test_Data/TestData.xlsx";
    private static final String iTestDataSheetName = "Data";
    private static final String iConfigSheetName   = "Config";
    private static final String iDefaultBrowser    = "CHROME";

    // ── Standard runtime herd / username (used by TC_03 through TC_17) ────────────────────────────
    public static String RUNTIME_HERD     = null;
    public static String RUNTIME_USERNAME = null;

    // ── Preliminary Checks runtime data (used by TC_18, TC_19, TC_20) ────────────────────────────
    public static String OVERCLAIM_HERD         = null;
    public static String OVERCLAIM_USERNAME      = null;
    public static String DUAL_CLAIM_HERD         = null;
    public static String DUAL_CLAIM_USERNAME     = null;
    public static String AGRI_ACTIVITY_HERD      = null;
    public static String AGRI_ACTIVITY_USERNAME  = null;

    // ─── Blacklisted herds — re-fetch if we land on one of these ─────────────────────────────────
    private static final Set<String> BLACKLISTED_HERDS = Set.of(
            "R1230577","C2280025","C1281036","Q1060377","M1391623","M1701886","D1160614",
            "X1120993","P1261051","E209434X","D1174054","H2621144","B1240361","G1930639",
            "G194082X","B1560048","R1050781","D201078X","M1952021","W1040563","B1150150",
            "B1380562","C129112X","G1221786","T209028X","T1790688","G1280928","G1930167",
            "Y1070492","Y1791564","R1040514","C1430554","D2450985"
    );

    private static void rerunIfBlacklisted()
    {
        int retries = 0;

        while (RUNTIME_HERD != null && BLACKLISTED_HERDS.contains(RUNTIME_HERD) && retries < 50)
        {
            retries++;
            CommonFunctions.log.warning("[HOOKS] Blacklisted herd detected: " + RUNTIME_HERD + " — re-fetching (attempt " + retries + ")");

            database.DBRouter.runDB("DATA", "List of herds with no errors at all",
                    String.valueOf(java.time.Year.now().getValue()),
                    String.valueOf(10 + retries)
            );

            List<Map<String, Object>> rows = database.DBRouter.getRows();
            if (rows == null || rows.isEmpty()) {
                throw new RuntimeException("[HOOKS] Re-fetch returned no rows — cannot find replacement herd.");
            }

            boolean replaced = false;
            for (Map<String, Object> r : rows) {
                String h = Objects.toString(r.get("APP_HERD_NO"), "").trim();
                if (!h.isEmpty() && !BLACKLISTED_HERDS.contains(h)) {
                    RUNTIME_HERD = h;
                    CommonFunctions.log.info("[HOOKS] Replacement herd selected: " + RUNTIME_HERD);
                    replaced = true;
                    break;
                }
            }

            if (!replaced) {
                CommonFunctions.log.warning("[HOOKS] No non-blacklisted herd found — retrying...");
            }
        }

        if (BLACKLISTED_HERDS.contains(RUNTIME_HERD)) {
            throw new RuntimeException("[HOOKS] Could not resolve a non-blacklisted herd after "
                    + retries + " retries. Last herd=" + RUNTIME_HERD);
        }
    }

    // =================================================================================================
    // BEFORE ALL — runs once before any scenario in the test run
    // =================================================================================================
    @BeforeAll
    public static void beforeAllExecution()
    {
        try
        {
            killProcessByName("chrome");
            killProcessByName("chromedriver");
            iTestCaseID  = System.getProperty("testcase",    "").trim();
            iEnvironment = System.getProperty("environment", "").trim();

            if (iTestCaseID.isEmpty())  { throw new RuntimeException("TestCase_ID system property missing."); }
            if (iEnvironment.isEmpty()) { throw new RuntimeException("Environment system property missing."); }

            iAccumulatedErrors.setLength(0);
            iLastScreenshotPath = "";
            iDocument           = null;
            iDocPath            = "";

            CommonFunctions.log.info("========== EXECUTION START : " + iTestCaseID + " ==========");
            CommonFunctions.loadDescriptionCache();
            CommonFunctions.clearStepLog();

            ExcelUtilities iTestDataExcel = new ExcelUtilities(iTestDataFilePath);
            iTestDataExcel.loadCurrentTestDataRow(iTestDataSheetName, iTestCaseID);

            int iConfigRow = iTestDataExcel.findRow(iConfigSheetName, "Env", iEnvironment);
            if (iConfigRow == -1) { throw new RuntimeException("Environment not found in Config sheet : " + iEnvironment); }

            iUrl = iTestDataExcel.getCellValue(iConfigSheetName, iConfigRow, "URL").trim();
            if (iUrl.isEmpty()) { throw new RuntimeException("URL blank for environment : " + iEnvironment); }

            // =====================================================================================
            // Bootstrap runtime Herd + Username BEFORE any scenario runs (DATA → INET)
            // =====================================================================================
            String herdYear         = System.getProperty("herd.year",  "2026").trim();
            String herdLimit        = System.getProperty("herd.limit", "25").trim();
            String herdRetry        = System.getProperty("herd.retry", "3").trim();
            String usernameOverride = System.getProperty("usernameOverride", "").trim();

            String herdFromSheet  = "";
            String unameFromSheet = "";
            try { herdFromSheet  = ExcelUtilities.getCurrentTestDataValue("HerdNumber"); } catch (Exception ignored) {}
            try { unameFromSheet = ExcelUtilities.getCurrentTestDataValue("Username");   } catch (Exception ignored) {}

            RUNTIME_HERD     = null;
            RUNTIME_USERNAME = null;

            if (!usernameOverride.isEmpty()) {
                RUNTIME_USERNAME = usernameOverride;
                RUNTIME_HERD     = herdFromSheet;
                CommonFunctions.log.info("[BOOT] Using usernameOverride=" + RUNTIME_USERNAME + ", herd(sheet)=" + RUNTIME_HERD);
                rerunIfBlacklisted();
            } else {
                final int maxAttempts = Integer.parseInt(herdRetry);
                final int limit       = Integer.parseInt(herdLimit);

                boolean found = false;

                for (int attempt = 1; attempt <= maxAttempts && !found; attempt++) {
                    database.DBRouter.runDB("DATA", "List of herds with no errors at all", herdYear, String.valueOf(limit), "");
                    List<Map<String,Object>> rows = database.DBRouter.getRows();
                    if (rows == null || rows.isEmpty()) {
                        throw new RuntimeException("Runtime DB (DATA) returned no herds for year=" + herdYear + ", limit=" + limit);
                    }

                    List<String> candidates = new java.util.ArrayList<>();
                    for (Map<String,Object> r : rows) {
                        String h = Objects.toString(r.get("APP_HERD_NO"), "").trim();
                        if (!h.isEmpty() && !candidates.contains(h)) candidates.add(h);
                    }
                    java.util.Collections.shuffle(candidates, new java.util.Random(System.nanoTime()));

                    CommonFunctions.log.info("[BOOT] Attempt " + attempt + "/" + maxAttempts +
                            " — trying " + candidates.size() + " randomized candidate(s) from DATA (year=" +
                            herdYear + ", limit=" + limit + ")");

                    int idx = 0;
                    for (String candidate : candidates) {
                        idx++;
                        CommonFunctions.log.info("[BOOT] Candidate [" + idx + "/" + candidates.size() + "] : " + candidate);

                        database.DBRouter.runDB("INET", "Get Login Id for herd", candidate);
                        String candidateUser = database.DBRouter.getValue("USERNAME");

                        if (candidateUser != null && !candidateUser.isBlank()) {
                            RUNTIME_HERD     = candidate;
                            RUNTIME_USERNAME = candidateUser.trim();
                            CommonFunctions.log.info("[BOOT] SELECTED Herd=" + RUNTIME_HERD + " | Username=" + RUNTIME_USERNAME);

                            rerunIfBlacklisted();
                            found = true;
                            break;
                        } else {
                            CommonFunctions.log.warning("[BOOT] INET: no USERNAME for herd=" + candidate + " — next candidate...");
                        }
                    }

                    if (!found) {
                        CommonFunctions.log.warning("[BOOT] No mapped USERNAME found in attempt " + attempt +
                                ". Re-running DATA query to get a fresh set of " + limit + " candidates...");
                    }
                }

                if (!found) {
                    if (!unameFromSheet.isBlank()) {
                        RUNTIME_USERNAME = unameFromSheet;
                        RUNTIME_HERD     = (RUNTIME_HERD == null || RUNTIME_HERD.isBlank()) ? herdFromSheet : RUNTIME_HERD;
                        CommonFunctions.log.warning("[BOOT] Falling back to Username from sheet: " + RUNTIME_USERNAME +
                                " | Herd=" + RUNTIME_HERD);
                        rerunIfBlacklisted();
                    } else {
                        throw new RuntimeException("Runtime DB (INET) returned no USERNAME after " + herdRetry +
                                " attempt(s). Try -Dherd.limit=10 or set -DusernameOverride=... for this run.");
                    }
                }
            }

            System.setProperty("TD:HerdNumber", RUNTIME_HERD == null ? "" : RUNTIME_HERD);
            System.setProperty("TD:Username",   RUNTIME_USERNAME == null ? "" : RUNTIME_USERNAME);

            Hooks.RUNTIME_HERD     = RUNTIME_HERD;
            Hooks.RUNTIME_USERNAME = RUNTIME_USERNAME;

            try {
                int dataRowIdx = iTestDataExcel.findRow(iTestDataSheetName, "TestCase_ID", iTestCaseID);
                if (dataRowIdx != -1) {
                    iTestDataExcel.setCellValue(iTestDataSheetName, dataRowIdx, "HerdNumber", RUNTIME_HERD == null ? "" : RUNTIME_HERD);
                    iTestDataExcel.setCellValue(iTestDataSheetName, dataRowIdx, "Username",   RUNTIME_USERNAME == null ? "" : RUNTIME_USERNAME);
                    CommonFunctions.log.info("[BOOT→Excel] Data updated: HerdNumber=" + RUNTIME_HERD + ", Username=" + RUNTIME_USERNAME);
                } else {
                    CommonFunctions.log.warning("[BOOT→Excel] Row not found for TestCase_ID=" + iTestCaseID + " (skipped write-back)");
                }
            } catch (Exception e) {
                CommonFunctions.log.warning("[BOOT→Excel] Persist failed: " + e.getMessage());
            }

            String iBrowserType = System.getProperty("browser", iDefaultBrowser).trim().toUpperCase();
            CommonFunctions.launchBrowser(iBrowserType, iUrl);

            Object[] iReportObjects = CommonFunctions.startWordReport(iTestCaseID);
            iDocument = (XWPFDocument) iReportObjects[0];
            iDocPath  = String.valueOf(iReportObjects[1]);

            CommonFunctions.log.info("BeforeAll complete | URL=" + iUrl + " | Report=" + iDocPath);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("BeforeAll failed for [" + iTestCaseID + "] : " + iException.getMessage(), iException);
        }
    }

    // =================================================================================================
    // BEFORE — @preliminary tag
    // Runs before every scenario tagged @preliminary (TC_18, TC_19, TC_20)
    // Step 1: Query BISS_DATA for one pending herd per check type
    // Step 2: Query BISS_INET for the agent login ID for each herd
    // =================================================================================================
    // ***************************************************************************************************************************************************************************************
    // Hook          : @Before("@preliminary")
    // Description   : Populates six static runtime fields before any @preliminary scenario runs.
    //                 Runs AFTER @BeforeAll so the browser and report are already initialised.
    //                 Step 1 — BISS_DATA: "Preliminary Checks Herds" query returns one row per
    //                          check type (LVC_DESC) where LVS_DESC = 'Pending'.
    //                 Step 2 — BISS_INET: "Get Login Id for herd" called once per herd to resolve
    //                          the agent username.
    //                 Fields populated:
    //                   OVERCLAIM_HERD / OVERCLAIM_USERNAME
    //                   DUAL_CLAIM_HERD / DUAL_CLAIM_USERNAME
    //                   AGRI_ACTIVITY_HERD / AGRI_ACTIVITY_USERNAME
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Before("@preliminary")
    public void beforePreliminaryCheck()
    {
        CommonFunctions.log.info("[HOOKS] @preliminary — fetching preliminary check herds from BISS_DATA...");

        String iHerdYear = System.getProperty("herd.year", "2026").trim();

        // ── Step 1: One herd per check type from BISS_DATA ──────────────────────────────────────
        database.DBRouter.runDB("DATA", "Preliminary Checks Herds", iHerdYear);
        List<Map<String, Object>> iRows = database.DBRouter.getRows();

        if (iRows == null || iRows.isEmpty())
        {
            throw new RuntimeException(
                    "[HOOKS] @preliminary — BISS_DATA query returned no rows. " +
                            "No pending preliminary check herds found for year=" + iHerdYear + ". " +
                            "Ensure LVS_DESC = 'Pending' records exist in vwbs_land_validation.");
        }

        for (Map<String, Object> iRow : iRows)
        {
            String iCheckType = Objects.toString(iRow.get("LVC_DESC"), "").trim();
            String iHerd      = Objects.toString(iRow.get("LVL_HERD_NO"), "").trim();

            switch (iCheckType)
            {
                case "Overclaim":
                    OVERCLAIM_HERD = iHerd;
                    CommonFunctions.log.info("[HOOKS] OVERCLAIM_HERD      = " + OVERCLAIM_HERD);
                    break;
                case "Dual claim":
                    DUAL_CLAIM_HERD = iHerd;
                    CommonFunctions.log.info("[HOOKS] DUAL_CLAIM_HERD     = " + DUAL_CLAIM_HERD);
                    break;
                case "Agricultural Activity":
                    AGRI_ACTIVITY_HERD = iHerd;
                    CommonFunctions.log.info("[HOOKS] AGRI_ACTIVITY_HERD  = " + AGRI_ACTIVITY_HERD);
                    break;
                default:
                    CommonFunctions.log.warning("[HOOKS] Unrecognised LVC_DESC value: '" + iCheckType + "' — skipping.");
                    break;
            }
        }

        // ── Step 2: Resolve agent login IDs from BISS_INET ─────────────────────────────────────
        OVERCLAIM_USERNAME    = fetchPrelimUsername(OVERCLAIM_HERD,    "OVERCLAIM");
        DUAL_CLAIM_USERNAME   = fetchPrelimUsername(DUAL_CLAIM_HERD,   "DUAL_CLAIM");
        AGRI_ACTIVITY_USERNAME = fetchPrelimUsername(AGRI_ACTIVITY_HERD, "AGRI_ACTIVITY");

        CommonFunctions.log.info("[HOOKS] OVERCLAIM_USERNAME      = " + OVERCLAIM_USERNAME);
        CommonFunctions.log.info("[HOOKS] DUAL_CLAIM_USERNAME     = " + DUAL_CLAIM_USERNAME);
        CommonFunctions.log.info("[HOOKS] AGRI_ACTIVITY_USERNAME  = " + AGRI_ACTIVITY_USERNAME);
    }

    // ***************************************************************************************************************************************************************************************
    // Method        : fetchPrelimUsername
    // Description   : Queries BISS_INET for the agent login ID for a given herd number.
    //                 Returns null if the herd is null/blank or no result is found.
    //                 Logs a warning rather than throwing — a missing username for one check type
    //                 should not block the other two TCs from running.
    // Parameters    : pHerd      — herd number string e.g. "A1060204"
    //                 pCheckType — label used only for log messages e.g. "OVERCLAIM"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    private String fetchPrelimUsername(String pHerd, String pCheckType)
    {
        if (pHerd == null || pHerd.isBlank())
        {
            CommonFunctions.log.warning("[HOOKS] fetchPrelimUsername — herd is null/blank for " + pCheckType + " — skipping INET query.");
            return null;
        }

        database.DBRouter.runDB("INET", "Get Login Id for herd", pHerd);
        String iUsername = database.DBRouter.getValue("USERNAME");

        if (iUsername == null || iUsername.isBlank())
        {
            CommonFunctions.log.warning("[HOOKS] fetchPrelimUsername — no login ID found in BISS_INET for herd: " + pHerd + " (" + pCheckType + ")");
            return null;
        }

        return iUsername.trim();
    }

    // =================================================================================================
    // BEFORE — runs before every scenario
    // =================================================================================================
    @Before
    public void beforeScenarioExecution(Scenario pScenario)
    {
        SoftAssertManager.reset();
        RetryAnalyser.clearRetryState();
        iScenarioStartTime = System.currentTimeMillis();
        CommonFunctions.log.info("---------- Scenario START : " + pScenario.getName() + " ----------");

        String iScenarioTags = String.join(" ", pScenario.getSourceTagNames());
        ReportManager.beginTestCase(iTestCaseID, pScenario.getName(), iScenarioTags);
    }

    // =================================================================================================
    // AFTER — runs after every scenario
    // =================================================================================================
    @After
    public void afterScenarioExecution(Scenario pScenario)
    {
        SoftAssertManager.assertAll();

        long iScenarioDuration = System.currentTimeMillis() - iScenarioStartTime;
        utilities.ScreenshotManager.appendScenarioDivider(
                iDocument,
                pScenario.getName(),
                !pScenario.isFailed(),
                iScenarioDuration
        );

        try
        {
            if (pScenario.isFailed())
            {
                CommonFunctions.log.severe("---------- Scenario FAILED : " + pScenario.getName() + " ----------");

                iAccumulatedErrors.append("Scenario [")
                        .append(pScenario.getName())
                        .append("] FAILED. Status=")
                        .append(pScenario.getStatus().name())
                        .append("\n");

                try
                {
                    if (iDocument != null && !iDocPath.isEmpty())
                    {
                        iLastScreenshotPath = CommonFunctions.addScreenshotToReport(
                                iDocument, iDocPath, iTestCaseID);
                    }
                    else
                    {
                        String iSafeName = iTestCaseID + "_" + pScenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                        iLastScreenshotPath = CommonFunctions.takeScreenshot(iSafeName);
                    }
                }
                catch (Exception iShotException)
                {
                    CommonFunctions.log.severe("Screenshot failed : " + iShotException.getMessage());
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

    // =================================================================================================
    // AFTER ALL — runs once after all scenarios complete
    // =================================================================================================
    @AfterAll
    public static void afterAllExecution()
    {
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
            CommonFunctions.log.severe("Failed to publish failure properties : " + iException.getMessage());
        }

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

    // =================================================================================================
    // UTILITY
    // =================================================================================================
    public static void killProcessByName(String processName)
    {
        String os = System.getProperty("os.name").toLowerCase();
        String command;
        try {
            if (os.contains("win")) {
                String processWithExe = processName.endsWith(".exe") ? processName : processName + ".exe";
                command = "taskkill /F /IM " + processWithExe + " /T";
            } else {
                command = "pkill -f " + processName;
            }
            Runtime.getRuntime().exec(command);
            CommonFunctions.log.info("Cleaned up process: " + processName);
        } catch (Exception e) {
            CommonFunctions.log.warning("Could not kill process " + processName + ": " + e.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : captureStep
    // Description   : On-demand screenshot method for step definitions.
    //                 Takes a screenshot of the current browser state and embeds it into the
    //                 running Word report with the supplied label as the caption.
    // Parameters    : pStepLabel (String) - describes what is being captured, shown as caption
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void captureStep(String pStepLabel)
    {
        CommonFunctions.captureStepScreenshot(iDocument, iDocPath, pStepLabel);
    }
}