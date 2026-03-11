// ===================================================================================================================================
// File          : TestStepLogger.java
// Package       : reporting
// Description   : Cucumber EventListener plugin for the BISS framework.
//                 Captures every Gherkin step's execution result (PASSED / FAILED / SKIPPED / PENDING)
//                 during a Cucumber run and makes them available to HtmlReportGenerator for rendering
//                 a collapsible step-by-step breakdown inside each test case row in the HTML report.
//
// Folder        : src/main/java/reporting/TestStepLogger.java
//
// Registration  : Add to Cucumber CLI args in TestRunner.java:
//                   "--plugin", "reporting.TestStepLogger"
//
// Data access   : TestStepLogger.getStepsForScenario(scenarioId)
//                 Returns List<StepRecord> for use in HtmlReportGenerator
//
// Thread safety : ConcurrentHashMap keyed by scenario ID — safe for parallel execution
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 11-03-2026
// ===================================================================================================================================

package reporting;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class TestStepLogger implements EventListener
{
    private static final Logger log = Logger.getLogger(TestStepLogger.class.getName());

    // Scenario ID → ordered list of step records — ConcurrentHashMap for parallel safety
    private static final Map<String, List<StepRecord>> iStepMap = new ConcurrentHashMap<>();

    // Track the currently running scenario per thread for step attribution
    private final ThreadLocal<String> iCurrentScenarioId = new ThreadLocal<>();

    // ***************************************************************************************************************************************************************************************
    // Function Name : setEventPublisher
    // Description   : Called by Cucumber to register all event handlers. Required by EventListener.
    // Parameters    : pPublisher (EventPublisher) - Cucumber's event bus
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Override
    public void setEventPublisher(EventPublisher pPublisher)
    {
        pPublisher.registerHandlerFor(TestCaseStarted.class,  this::onTestCaseStarted);
        pPublisher.registerHandlerFor(TestStepStarted.class,  this::onTestStepStarted);
        pPublisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        pPublisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
        pPublisher.registerHandlerFor(TestRunFinished.class,  this::onTestRunFinished);
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Event handlers
    // -------------------------------------------------------------------------------------------------------------------------------

    private void onTestCaseStarted(TestCaseStarted pEvent)
    {
        String iScenarioId = pEvent.getTestCase().getId().toString();
        iCurrentScenarioId.set(iScenarioId);
        iStepMap.put(iScenarioId, Collections.synchronizedList(new ArrayList<>()));

        log.fine("[StepLogger] Scenario started : " + pEvent.getTestCase().getName() + " | ID=" + iScenarioId);
    }

    private void onTestStepStarted(TestStepStarted pEvent)
    {
        // Nothing to do on step start — we record on finish when we have duration and status
    }

    private void onTestStepFinished(TestStepFinished pEvent)
    {
        // Only log PickleStepTestStep — ignore Before/After hook steps
        if (!(pEvent.getTestStep() instanceof PickleStepTestStep)) { return; }

        String                iScenarioId  = iCurrentScenarioId.get();
        PickleStepTestStep    iStep        = (PickleStepTestStep) pEvent.getTestStep();
        Status                iStatus      = pEvent.getResult().getStatus();
        Duration              iDuration    = pEvent.getResult().getDuration();
        String                iKeyword     = iStep.getStep().getKeyword().trim();
        String                iText        = iStep.getStep().getText().trim();
        String                iErrorMsg    = "";

        if (pEvent.getResult().getError() != null)
        {
            iErrorMsg = pEvent.getResult().getError().getMessage();
            if (iErrorMsg == null) { iErrorMsg = pEvent.getResult().getError().getClass().getSimpleName(); }
        }

        StepRecord iRecord = new StepRecord(iKeyword, iText, iStatus.name(), iDuration, iErrorMsg);

        List<StepRecord> iSteps = iStepMap.get(iScenarioId);
        if (iSteps != null)
        {
            iSteps.add(iRecord);
        }

        log.fine("[StepLogger] Step " + iStatus.name() + " : " + iKeyword + " " + iText);
    }

    private void onTestCaseFinished(TestCaseFinished pEvent)
    {
        String iScenarioId = pEvent.getTestCase().getId().toString();
        List<StepRecord> iSteps = iStepMap.getOrDefault(iScenarioId, Collections.emptyList());

        long iPassCount = iSteps.stream().filter(s -> "PASSED".equals(s.iStatus)).count();
        long iFailCount = iSteps.stream().filter(s -> "FAILED".equals(s.iStatus)).count();

        log.info("[StepLogger] Scenario '" + pEvent.getTestCase().getName()
                + "' finished | Steps: " + iSteps.size()
                + " | Passed: " + iPassCount
                + " | Failed: " + iFailCount);

        iCurrentScenarioId.remove();
    }

    private void onTestRunFinished(TestRunFinished pEvent)
    {
        log.info("[StepLogger] Test run finished. Scenarios logged : " + iStepMap.size());
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // PUBLIC API — called by HtmlReportGenerator to embed step details in the report
    // -------------------------------------------------------------------------------------------------------------------------------

    // ***************************************************************************************************************************************************************************************
    // Function Name : getStepsForScenario
    // Description   : Returns the ordered list of step records for a given scenario ID.
    //                 Returns empty list if scenario ID not found.
    // Parameters    : pScenarioId (String) - Cucumber scenario ID from TestCaseResult
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static List<StepRecord> getStepsForScenario(String pScenarioId)
    {
        return Collections.unmodifiableList(
                iStepMap.getOrDefault(pScenarioId, Collections.emptyList()));
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getAllSteps
    // Description   : Returns the full step map (unmodifiable). Used for report rendering.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static Map<String, List<StepRecord>> getAllSteps()
    {
        return Collections.unmodifiableMap(iStepMap);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : clear
    // Description   : Clears all recorded step data. Called from TestRunner between test cases
    //                 to prevent stale data accumulation across Cucumber runs.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void clear()
    {
        iStepMap.clear();
        log.info("[StepLogger] Step log cleared.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildStepHtml
    // Description   : Builds an HTML fragment showing a collapsible step-by-step breakdown
    //                 for a given scenario. Called by HtmlReportGenerator.
    // Parameters    : pScenarioId (String) - Cucumber scenario ID
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String buildStepHtml(String pScenarioId)
    {
        List<StepRecord> iSteps = getStepsForScenario(pScenarioId);

        if (iSteps.isEmpty())
        {
            return "<div style='font-size:12px;color:#a0aec0;'>No step data captured for this scenario.</div>";
        }

        StringBuilder iHtml = new StringBuilder();
        iHtml.append("<div style='margin-top:12px;'>")
                .append("<table style='width:100%;border-collapse:collapse;font-size:12px;'>")
                .append("<thead><tr style='background:#4a5568;color:white;'>")
                .append("<th style='padding:7px 10px;text-align:left;width:10%;'>Keyword</th>")
                .append("<th style='padding:7px 10px;text-align:left;width:55%;'>Step</th>")
                .append("<th style='padding:7px 10px;text-align:left;width:15%;'>Status</th>")
                .append("<th style='padding:7px 10px;text-align:left;width:20%;'>Duration</th>")
                .append("</tr></thead><tbody>");

        for (StepRecord iStep : iSteps)
        {
            String iRowBg   = "PASSED".equals(iStep.iStatus) ? ""
                    : "FAILED".equals(iStep.iStatus) ? "background:#fff5f5;"
                    : "SKIPPED".equals(iStep.iStatus) ? "background:#fffff0;" : "";

            String iBadge   = "PASSED".equals(iStep.iStatus)
                    ? "<span style='background:#c6f6d5;color:#276749;padding:2px 8px;border-radius:8px;font-size:11px;font-weight:700;'>PASS</span>"
                    : "FAILED".equals(iStep.iStatus)
                    ? "<span style='background:#fed7d7;color:#9b2c2c;padding:2px 8px;border-radius:8px;font-size:11px;font-weight:700;'>FAIL</span>"
                    : "<span style='background:#fefcbf;color:#744210;padding:2px 8px;border-radius:8px;font-size:11px;font-weight:700;'>SKIP</span>";

            iHtml.append("<tr style='border-bottom:1px solid #edf2f7;").append(iRowBg).append("'>")
                    .append("<td style='padding:7px 10px;color:#718096;font-style:italic;'>")
                    .append(escHtml(iStep.iKeyword)).append("</td>")
                    .append("<td style='padding:7px 10px;'>").append(escHtml(iStep.iStepText)).append("</td>")
                    .append("<td style='padding:7px 10px;'>").append(iBadge).append("</td>")
                    .append("<td style='padding:7px 10px;color:#718096;'>")
                    .append(iStep.getDurationFormatted()).append("</td>")
                    .append("</tr>");

            // Error row for failed steps
            if ("FAILED".equals(iStep.iStatus) && iStep.iErrorMessage != null && !iStep.iErrorMessage.isEmpty())
            {
                iHtml.append("<tr style='background:#fff5f5;'>")
                        .append("<td colspan='4' style='padding:6px 10px;'>")
                        .append("<div style='background:#2d3748;color:#fc8181;padding:8px 12px;border-radius:6px;")
                        .append("font-family:monospace;font-size:11px;white-space:pre-wrap;word-break:break-word;'>")
                        .append(escHtml(iStep.iErrorMessage))
                        .append("</div></td></tr>");
            }
        }

        iHtml.append("</tbody></table></div>");
        return iHtml.toString();
    }

    private static String escHtml(String pInput)
    {
        if (pInput == null) { return ""; }
        return pInput.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    // ===============================================================================================================================
    // INNER CLASS : StepRecord — data model for a single step execution result
    // ===============================================================================================================================

    public static class StepRecord
    {
        public final String   iKeyword;
        public final String   iStepText;
        public final String   iStatus;         // PASSED | FAILED | SKIPPED | PENDING | UNDEFINED
        public final Duration iDuration;
        public final String   iErrorMessage;

        public StepRecord(String pKeyword, String pStepText, String pStatus,
                          Duration pDuration, String pErrorMessage)
        {
            iKeyword      = pKeyword;
            iStepText     = pStepText;
            iStatus       = pStatus;
            iDuration     = pDuration;
            iErrorMessage = pErrorMessage;
        }

        public String getDurationFormatted()
        {
            if (iDuration == null) { return "—"; }
            long iMs = iDuration.toMillis();
            if (iMs < 1000) { return iMs + "ms"; }
            return String.format("%.2fs", iMs / 1000.0);
        }

        public boolean isPassed()  { return "PASSED".equals(iStatus); }
        public boolean isFailed()  { return "FAILED".equals(iStatus); }
        public boolean isSkipped() { return "SKIPPED".equals(iStatus) || "PENDING".equals(iStatus); }
    }
}