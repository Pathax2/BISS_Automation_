// ===================================================================================================================================
// File          : CucumberStepListener.java
// Package       : reporting
// Description   : Cucumber ConcurrentEventListener that automatically captures step-level results
//                 via the Cucumber event bus, eliminating the need to manually instrument each
//                 step-definition method. Registers for TestCaseStarted, TestStepStarted,
//                 TestStepFinished, and TestCaseFinished events.
//
// Usage         : Add to cucumber.properties or @CucumberOptions:
//                   cucumber.plugin = reporting.CucumberStepListener
//
// Integration Points:
//   - ReportManager.java  : calls beginTestCase(), recordStep(), addStepLog()
//   - Cucumber EventBus   : listens on lifecycle events
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CucumberStepListener implements ConcurrentEventListener
{
    private static final Logger log = Logger.getLogger(CucumberStepListener.class.getName());

    // Track step start times per thread to compute duration
    private final Map<Long, Long> iStepStartTimes = new ConcurrentHashMap<>();

    // Track test case start times per thread
    private final Map<Long, Long> iTestCaseStartTimes = new ConcurrentHashMap<>();

    // ===============================================================================================================================
    // EVENT REGISTRATION
    // ===============================================================================================================================

    @Override
    public void setEventPublisher(EventPublisher pPublisher)
    {
        pPublisher.registerHandlerFor(TestCaseStarted.class,  this::onTestCaseStarted);
        pPublisher.registerHandlerFor(TestStepStarted.class,  this::onTestStepStarted);
        pPublisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        pPublisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    // ===============================================================================================================================
    // EVENT HANDLERS
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : onTestCaseStarted
    // Description   : Fires when a Cucumber scenario starts. Initialises the test case in ReportManager
    //                 using the scenario name. Tags are extracted and joined into a space-separated string.
    // Parameters    : pEvent (TestCaseStarted) - Cucumber event
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private void onTestCaseStarted(TestCaseStarted pEvent)
    {
        long iThreadId = Thread.currentThread().getId();
        iTestCaseStartTimes.put(iThreadId, System.currentTimeMillis());

        TestCase iTestCase = pEvent.getTestCase();
        String iScenarioName = iTestCase.getName();
        String iTags = String.join(" ", iTestCase.getTags());

        // Extract test case ID from tags if present (e.g. @TC_01)
        String iTestCaseID = extractTestCaseID(iTags, iScenarioName);

        ReportManager.beginTestCase(iTestCaseID, iScenarioName, iTags);

        log.fine("[CucumberStepListener] TestCase started: " + iScenarioName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : onTestStepStarted
    // Description   : Fires when a single step starts. Records the start timestamp for duration calculation.
    //                 Only processes PickleStepTestStep (actual Gherkin steps), ignoring hooks.
    // Parameters    : pEvent (TestStepStarted) - Cucumber event
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private void onTestStepStarted(TestStepStarted pEvent)
    {
        if (pEvent.getTestStep() instanceof PickleStepTestStep)
        {
            iStepStartTimes.put(Thread.currentThread().getId(), System.currentTimeMillis());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : onTestStepFinished
    // Description   : Fires when a single step completes. Extracts the Gherkin keyword, step text,
    //                 status, duration, and error (if any), then records via ReportManager.recordStep().
    //                 Only processes PickleStepTestStep (actual Gherkin steps), ignoring hooks.
    // Parameters    : pEvent (TestStepFinished) - Cucumber event
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private void onTestStepFinished(TestStepFinished pEvent)
    {
        if (!(pEvent.getTestStep() instanceof PickleStepTestStep))
        {
            return;   // Skip hooks
        }

        PickleStepTestStep iStep = (PickleStepTestStep) pEvent.getTestStep();
        Result iResult = pEvent.getResult();

        // Duration
        long iThreadId  = Thread.currentThread().getId();
        long iStartTime = iStepStartTimes.getOrDefault(iThreadId, System.currentTimeMillis());
        long iDurationMs = System.currentTimeMillis() - iStartTime;
        iStepStartTimes.remove(iThreadId);

        // Keyword & text
        String iKeyword  = iStep.getStep().getKeyword().trim();   // "Given ", "When ", etc.
        String iStepText = iStep.getStep().getText();

        // Status mapping: Cucumber Status → ReportManager status string
        String iStatus;
        switch (iResult.getStatus())
        {
            case PASSED:    iStatus = "PASS"; break;
            case FAILED:    iStatus = "FAIL"; break;
            case SKIPPED:   iStatus = "SKIP"; break;
            case PENDING:   iStatus = "SKIP"; break;
            case UNDEFINED: iStatus = "SKIP"; break;
            default:        iStatus = "ERROR"; break;
        }

        // Error message
        String iErrorMessage = "";
        if (iResult.getError() != null)
        {
            Throwable iErr = iResult.getError();
            iErrorMessage = iErr.getClass().getSimpleName() + ": " + iErr.getMessage();
        }

        // Build a log message with step context
        String iLogMessage = buildStepLogMessage(iKeyword, iStepText, iStatus, iDurationMs, iErrorMessage);

        // Record into ReportManager
        ReportManager.recordStep(
                iKeyword,
                iStepText,
                iStatus,
                iDurationMs,
                iLogMessage,
                iErrorMessage,
                null   // Screenshot path — set separately via Hooks if needed
        );

        log.fine("[CucumberStepListener] Step finished: " + iKeyword + " " + iStepText + " → " + iStatus);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : onTestCaseFinished
    // Description   : Fires when a Cucumber scenario finishes. This is informational only;
    //                 the actual ReportManager.recordResult() call is still made by TestRunner
    //                 so that it can supply the overall status, screenshot path, etc.
    // Parameters    : pEvent (TestCaseFinished) - Cucumber event
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private void onTestCaseFinished(TestCaseFinished pEvent)
    {
        long iThreadId = Thread.currentThread().getId();
        iTestCaseStartTimes.remove(iThreadId);

        log.fine("[CucumberStepListener] TestCase finished: " + pEvent.getTestCase().getName()
                + " → " + pEvent.getResult().getStatus());
    }

    // ===============================================================================================================================
    // HELPER METHODS
    // ===============================================================================================================================

    /**
     * Extracts a test case ID from the tag string.
     * Looks for tags matching @TC_\d+ or @TCID_\d+ pattern.
     * Falls back to a sanitised version of the scenario name.
     */
    private String extractTestCaseID(String pTags, String pScenarioName)
    {
        if (pTags != null)
        {
            for (String tag : pTags.split("\\s+"))
            {
                if (tag.matches("@TC[_-]?\\d+.*"))
                {
                    return tag.replaceFirst("^@", "");
                }
                if (tag.matches("@TCID[_-]?\\d+.*"))
                {
                    return tag.replaceFirst("^@", "");
                }
            }
        }

        // Fallback: derive an ID from the scenario name
        if (pScenarioName != null && !pScenarioName.isEmpty())
        {
            String sanitised = pScenarioName.replaceAll("[^A-Za-z0-9]+", "_");
            if (sanitised.length() > 30) sanitised = sanitised.substring(0, 30);
            return sanitised;
        }
        return "UNKNOWN";
    }

    /**
     * Builds a descriptive log message for a step execution.
     */
    private String buildStepLogMessage(
            String pKeyword, String pStepText, String pStatus, long pDurationMs, String pError)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Executing: ").append(pKeyword).append(" ").append(pStepText).append("\n");
        sb.append("Status: ").append(pStatus).append(" | Duration: ").append(pDurationMs).append("ms");
        if (pError != null && !pError.isEmpty())
        {
            sb.append("\nError: ").append(pError);
        }
        return sb.toString();
    }
}