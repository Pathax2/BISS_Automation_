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

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Hooks
{
    public static XWPFDocument iDocument    = null;
    public static String       iDocPath     = "";
    public static String       iTestCaseID  = "";
    public static String       iEnvironment = "";
    public static String       iUrl         = "";

    private static final StringBuilder iAccumulatedErrors  = new StringBuilder();
    private static String              iLastScreenshotPath = "";

    private static final String iTestDataFilePath  = "src/test/resources/Test_Data/TestData.xlsx";
    private static final String iTestDataSheetName = "Data";
    private static final String iConfigSheetName   = "Config";
    private static final String iDefaultBrowser    = "CHROME";
    public static String RUNTIME_HERD = null;
    public static String RUNTIME_USERNAME = null;
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

            ExcelUtilities iTestDataExcel = new ExcelUtilities(iTestDataFilePath);
            iTestDataExcel.loadCurrentTestDataRow(iTestDataSheetName, iTestCaseID);

            int iConfigRow = iTestDataExcel.findRow(iConfigSheetName, "Env", iEnvironment);
            if (iConfigRow == -1) { throw new RuntimeException("Environment not found in Config sheet : " + iEnvironment); }

            iUrl = iTestDataExcel.getCellValue(iConfigSheetName, iConfigRow, "URL").trim();
            if (iUrl.isEmpty()) { throw new RuntimeException("URL blank for environment : " + iEnvironment); }

            // =====================================================================================
            // NEW: Bootstrap runtime Herd + Username BEFORE any scenario runs (DATA → INET)
            // =====================================================================================
            // Runtime knobs (no code edits needed)
            String herdYear         = System.getProperty("herd.year",  "2026").trim();
            String herdLimit        = System.getProperty("herd.limit", "25").trim();    // fetch 25 candidates
            String herdRetry        = System.getProperty("herd.retry", "3").trim();    // attempts to re-fetch fresh candidates
            String usernameOverride = System.getProperty("usernameOverride", "").trim();

            // If your sheet already has values (loaded above), read them (optional)
            String herdFromSheet  = "";
            String unameFromSheet = "";
            try { herdFromSheet  = ExcelUtilities.getCurrentTestDataValue("HerdNumber"); } catch (Exception ignored) {}
            try { unameFromSheet = ExcelUtilities.getCurrentTestDataValue("Username");   } catch (Exception ignored) {}

            String runtimeHerd = null;
            String runtimeUsername = null;

            if (!usernameOverride.isEmpty()) {
                // Use provided username; keep herd from sheet if available
                runtimeUsername = usernameOverride;
                runtimeHerd     = herdFromSheet;
                CommonFunctions.log.info("[BOOT] Using usernameOverride=" + runtimeUsername + ", herd(sheet)=" + runtimeHerd);
            } else {
                final int maxAttempts = Integer.parseInt(herdRetry);
                final int limit       = Integer.parseInt(herdLimit);

                boolean found = false;

                for (int attempt = 1; attempt <= maxAttempts && !found; attempt++) {
                    // --- DATA DB → fetch up to N candidate herds (ordered) ---
                    database.DBRouter.runDB("DATA", "List of herds with no errors at all", herdYear, String.valueOf(limit),"");
                    List<Map<String,Object>> rows = database.DBRouter.getRows();
                    if (rows == null || rows.isEmpty()) {
                        throw new RuntimeException("Runtime DB (DATA) returned no herds for year=" + herdYear + ", limit=" + limit);
                    }

                    // Build candidate list (distinct, non-blank), then shuffle for randomness
                    List<String> candidates = new java.util.ArrayList<>();
                    for (Map<String,Object> r : rows) {
                        String h = Objects.toString(r.get("APP_HERD_NO"), "").trim();
                        if (!h.isEmpty() && !candidates.contains(h)) candidates.add(h);
                    }
                    java.util.Collections.shuffle(candidates, new java.util.Random(System.nanoTime()));

                    CommonFunctions.log.info("[BOOT] Attempt " + attempt + "/" + maxAttempts +
                            " — trying " + candidates.size() + " randomized candidate(s) from DATA (year=" +
                            herdYear + ", limit=" + limit + ")");

                    // Try each candidate until one returns a USERNAME from INET
                    int idx = 0;
                    for (String candidate : candidates) {
                        idx++;
                        CommonFunctions.log.info("[BOOT] Candidate [" + idx + "/" + candidates.size() + "] : " + candidate);

                        database.DBRouter.runDB("INET", "Get Login Id for herd", candidate);
                        String candidateUser = database.DBRouter.getValue("USERNAME");

                        if (candidateUser != null && !candidateUser.isBlank()) {
                            runtimeHerd     = candidate;
                            runtimeUsername = candidateUser.trim();
                            CommonFunctions.log.info("[BOOT] SELECTED Herd=" + runtimeHerd + " | Username=" + runtimeUsername);
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

                // If still not found, consider fallback to sheet’s username (if present) or fail fast
                if (!found) {
                    if (!unameFromSheet.isBlank()) {
                        runtimeUsername = unameFromSheet;
                        runtimeHerd     = (runtimeHerd == null || runtimeHerd.isBlank()) ? herdFromSheet : runtimeHerd;
                        CommonFunctions.log.warning("[BOOT] Falling back to Username from sheet: " + runtimeUsername +
                                " | Herd=" + runtimeHerd);
                    } else {
                        throw new RuntimeException("Runtime DB (INET) returned no USERNAME after " + herdRetry +
                                " attempt(s). Try -Dherd.limit=10 or set -DusernameOverride=... for this run.");
                    }
                }
            }

            // Publish TD:* variables so steps can consume TD:HerdNumber / TD:Username
            System.setProperty("TD:HerdNumber", runtimeHerd == null ? "" : runtimeHerd);
            System.setProperty("TD:Username",   runtimeUsername == null ? "" : runtimeUsername);
            Hooks.RUNTIME_HERD = runtimeHerd;
            Hooks.RUNTIME_USERNAME = runtimeUsername;

            // Persist into Excel (Data sheet) for the current TestCase_ID
            try {
                int dataRowIdx = iTestDataExcel.findRow(iTestDataSheetName, "TestCase_ID", iTestCaseID);
                if (dataRowIdx != -1) {
                    iTestDataExcel.setCellValue(iTestDataSheetName, dataRowIdx, "HerdNumber", runtimeHerd == null ? "" : runtimeHerd);
                    iTestDataExcel.setCellValue(iTestDataSheetName, dataRowIdx, "Username",   runtimeUsername == null ? "" : runtimeUsername);
                    CommonFunctions.log.info("[BOOT→Excel] Data updated: HerdNumber=" + runtimeHerd + ", Username=" + runtimeUsername);
                } else {
                    CommonFunctions.log.warning("[BOOT→Excel] Row not found for TestCase_ID=" + iTestCaseID + " (skipped write-back)");
                }
            } catch (Exception e) {
                CommonFunctions.log.warning("[BOOT→Excel] Persist failed: " + e.getMessage());
            }
            // =====================================================================================

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

    @Before
    public void beforeScenarioExecution(Scenario pScenario)
    {
        SoftAssertManager.reset();
        RetryAnalyser.clearRetryState();
        CommonFunctions.log.info("---------- Scenario START : " + pScenario.getName() + " ----------");
    }

    @After
    public void afterScenarioExecution(Scenario pScenario)
    {
        SoftAssertManager.assertAll();
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

    @AfterAll
    public static void afterAllExecution()
    {
        // Publish failure details as system properties for TestRunner -> ReportManager pickup
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

    public static void killProcessByName(String processName) {
        String os = System.getProperty("os.name").toLowerCase();
        String command;
        try {
            if (os.contains("win")) {
                // Windows requires .exe and specific flags /F (force) /T (tree)
                String processWithExe = processName.endsWith(".exe") ? processName : processName + ".exe";
                command = "taskkill /F /IM " + processWithExe + " /T";
            } else {
                // Mac/Linux
                command = "pkill -f " + processName;
            }
            Runtime.getRuntime().exec(command);
            CommonFunctions.log.info("Cleaned up process: " + processName);
        } catch (Exception e) {
            CommonFunctions.log.warning("Could not kill process " + processName + ": " + e.getMessage());
        }
    }
}
