// ===================================================================================================================================
// File          : ReportManager.java
// Package       : reporting
// Description   : Central execution data collector for the BISS automation framework.
//                 Accumulates per-test-case results (status, duration, error, screenshot path, steps)
//                 in a thread-safe list during the TestRunner execution loop.
//                 At end of run, passes the complete dataset to HtmlReportGenerator and JUnitXmlGenerator.
//
// Integration Points:
//   - TestRunner.java     : calls ReportManager.recordResult() after each test case
//   - Hooks.java          : calls ReportManager.setScenarioError() on scenario failure
//   - HtmlReportGenerator : consumes ReportManager.getResults() to build the HTML dashboard
//   - JUnitXmlGenerator   : consumes ReportManager.getResults() to build Bamboo-compatible XML
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ReportManager
{
    private static final Logger log = Logger.getLogger(ReportManager.class.getName());

    private static final DateTimeFormatter iDisplayFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    // Thread-safe list — safe for concurrent writes if parallel execution is ever enabled
    private static final CopyOnWriteArrayList<TestCaseResult> iResults = new CopyOnWriteArrayList<>();

    // Execution-level metadata
    private static String iSuiteStartTime  = "";
    private static String iSuiteEndTime    = "";
    private static String iEnvironment     = "";
    private static String iBrowser         = "";
    private static String iExecutedBy      = System.getProperty("user.name", "BAMBOO");
    private static String iBuildNumber     = System.getProperty("bamboo.buildNumber",    "LOCAL");
    private static String iBuildPlanKey    = System.getProperty("bamboo.buildPlanKey",   "BISS-AUTO");
    private static String iBambooAgentName = System.getProperty("bamboo.agentName",      "Local Machine");

    // ===============================================================================================================================
    // PUBLIC API
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : startSuite
    // Description   : Records suite-level metadata at the very beginning of a TestRunner execution cycle
    // Parameters    : pEnvironment (String) - target environment (DEV | STAGING | PROD)
    //                 pBrowser     (String) - browser type used in this run
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void startSuite(String pEnvironment, String pBrowser)
    {
        iResults.clear();
        iEnvironment   = pEnvironment == null ? "" : pEnvironment.trim();
        iBrowser       = pBrowser     == null ? "" : pBrowser.trim();
        iSuiteStartTime = LocalDateTime.now().format(iDisplayFormatter);

        log.info("[ReportManager] Suite started | Environment=" + iEnvironment + " | Browser=" + iBrowser);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : endSuite
    // Description   : Records the suite end timestamp
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void endSuite()
    {
        iSuiteEndTime = LocalDateTime.now().format(iDisplayFormatter);
        log.info("[ReportManager] Suite ended. Total results collected : " + iResults.size());
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : recordResult
    // Description   : Records the full result of a single test case execution after it completes.
    //                 Called from TestRunner after each Cucumber Main.run() invocation.
    // Parameters    : pTestCaseID    (String)  - test case identifier
    //                 pDescription   (String)  - test case description from ExecutionControl
    //                 pStatus        (String)  - PASS | FAIL | ERROR
    //                 pDurationMs    (long)    - execution duration in milliseconds
    //                 pErrorMessage  (String)  - failure reason (null or blank if PASS)
    //                 pScreenshotPath(String)  - path to screenshot file (null or blank if none)
    //                 pTags          (String)  - Cucumber tags associated with the test case
    //                 pEnvironment   (String)  - environment this test case ran against
    //                 pBrowser       (String)  - browser used for this test case
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void recordResult(
            String pTestCaseID,
            String pDescription,
            String pStatus,
            long   pDurationMs,
            String pErrorMessage,
            String pScreenshotPath,
            String pTags,
            String pEnvironment,
            String pBrowser)
    {
        TestCaseResult iResult = new TestCaseResult();
        iResult.iTestCaseID     = pTestCaseID     == null ? ""      : pTestCaseID.trim();
        iResult.iDescription    = pDescription    == null ? ""      : pDescription.trim();
        iResult.iStatus         = pStatus         == null ? "ERROR" : pStatus.trim().toUpperCase();
        iResult.iDurationMs     = pDurationMs;
        iResult.iErrorMessage   = pErrorMessage   == null ? ""      : pErrorMessage.trim();
        iResult.iScreenshotPath = pScreenshotPath == null ? ""      : pScreenshotPath.trim();
        iResult.iTags           = pTags           == null ? ""      : pTags.trim();
        iResult.iEnvironment    = pEnvironment    == null ? ""      : pEnvironment.trim();
        iResult.iBrowser        = pBrowser        == null ? ""      : pBrowser.trim();
        iResult.iTimestamp      = LocalDateTime.now().format(iDisplayFormatter);

        iResults.add(iResult);
        log.info("[ReportManager] Recorded | " + iResult.iTestCaseID + " | " + iResult.iStatus
                + " | " + iResult.getDurationFormatted());
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getResults
    // Description   : Returns an unmodifiable view of all collected results
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static List<TestCaseResult> getResults()
    {
        return Collections.unmodifiableList(new ArrayList<>(iResults));
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Suite-level aggregation helpers
    // -------------------------------------------------------------------------------------------------------------------------------
    public static int getTotalCount()   { return iResults.size(); }
    public static int getPassCount()    { return (int) iResults.stream().filter(r -> "PASS".equals(r.iStatus)).count(); }
    public static int getFailCount()    { return (int) iResults.stream().filter(r -> "FAIL".equals(r.iStatus)).count(); }
    public static int getErrorCount()   { return (int) iResults.stream().filter(r -> "ERROR".equals(r.iStatus)).count(); }
    public static long getTotalDurationMs() { return iResults.stream().mapToLong(r -> r.iDurationMs).sum(); }

    public static String getPassPercent()
    {
        int iTotal = getTotalCount();
        return iTotal == 0 ? "0" : String.format("%.1f", (getPassCount() * 100.0 / iTotal));
    }

    public static String getFailPercent()
    {
        int iTotal = getTotalCount();
        return iTotal == 0 ? "0" : String.format("%.1f", (getFailCount() * 100.0 / iTotal));
    }

    public static String getSuiteStartTime()  { return iSuiteStartTime; }
    public static String getSuiteEndTime()    { return iSuiteEndTime; }
    public static String getEnvironment()     { return iEnvironment; }
    public static String getBrowser()         { return iBrowser; }
    public static String getExecutedBy()      { return iExecutedBy; }
    public static String getBuildNumber()     { return iBuildNumber; }
    public static String getBuildPlanKey()    { return iBuildPlanKey; }
    public static String getBambooAgentName() { return iBambooAgentName; }

    public static String getSuiteDurationFormatted()
    {
        long iMs = getTotalDurationMs();
        long iSecs = (iMs / 1000) % 60;
        long iMins = (iMs / (1000 * 60)) % 60;
        long iHrs  = iMs / (1000 * 60 * 60);
        return String.format("%02dh %02dm %02ds", iHrs, iMins, iSecs);
    }


    // ===============================================================================================================================
    // INNER CLASS : TestCaseResult — data model for a single test case execution result
    // ===============================================================================================================================

    public static class TestCaseResult
    {
        public String iTestCaseID      = "";
        public String iDescription     = "";
        public String iStatus          = "";   // PASS | FAIL | ERROR
        public long   iDurationMs      = 0L;
        public String iErrorMessage    = "";
        public String iScreenshotPath  = "";
        public String iTags            = "";
        public String iEnvironment     = "";
        public String iBrowser         = "";
        public String iTimestamp       = "";

        public String getDurationFormatted()
        {
            long iSecs = (iDurationMs / 1000) % 60;
            long iMins = (iDurationMs / (1000 * 60)) % 60;
            return String.format("%dm %02ds", iMins, iSecs);
        }

        public boolean isPassed()  { return "PASS".equalsIgnoreCase(iStatus); }
        public boolean isFailed()  { return "FAIL".equalsIgnoreCase(iStatus); }
        public boolean isError()   { return "ERROR".equalsIgnoreCase(iStatus); }

        public String getStatusBadgeClass()
        {
            switch (iStatus.toUpperCase())
            {
                case "PASS":  return "badge-pass";
                case "FAIL":  return "badge-fail";
                default:      return "badge-error";
            }
        }

        public String getRowClass()
        {
            switch (iStatus.toUpperCase())
            {
                case "PASS":  return "row-pass";
                case "FAIL":  return "row-fail";
                default:      return "row-error";
            }
        }
    }
}