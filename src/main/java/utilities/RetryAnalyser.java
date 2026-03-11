// ===================================================================================================================================
// File          : RetryAnalyser.java
// Package       : utilities
// Description   : Cucumber-compatible retry mechanism for the BISS automation framework.
//                 Retries failed scenarios a configurable number of times before marking them FAIL.
//                 Only retries on transient exceptions (timeout, stale element) — NOT on
//                 AssertionError (genuine business failures must never be silently retried).
//
// Folder        : src/main/java/utilities/RetryAnalyser.java
//
// Integration   :
//   Registered as a Cucumber plugin in TestRunner.java:
//     "--plugin", "utilities.RetryAnalyser"
//   OR via cucumber.properties:
//     cucumber.plugin=utilities.RetryAnalyser
//
// Config keys (application.properties):
//   retry.count=1                                           (max retries per scenario)
//   retry.on.exceptions=TimeoutException,StaleElementReferenceException
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import io.cucumber.java.Scenario;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class RetryAnalyser
{
    private static final Logger log = Logger.getLogger(RetryAnalyser.class.getName());

    // Max retries read from ConfigManager — defaulting to 1 if config unavailable
    private static final int iMaxRetries = ConfigManager.getInt("retry.count", 1);

    // Exception names that are eligible for retry (read from config, comma-separated)
    private static final Set<String> iRetryableExceptions = resolveRetryableExceptions();

    // Thread-safe counter: scenario ID → number of retries already attempted
    private static final Map<String, Integer> iRetryCountMap = new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor
    // -------------------------------------------------------------------------------------------------------------------------------
    private RetryAnalyser() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : shouldRetry
    // Description   : Called after each scenario failure. Returns true if the scenario should be retried.
    //                 Retry logic:
    //                   - AssertionError → always returns false (genuine test failure, no retry)
    //                   - Retry count exceeded → returns false
    //                   - Exception not in retryable list → returns false
    //                   - Otherwise → increments counter, returns true
    // Parameters    : pScenario     (Scenario)  - the failed Cucumber scenario
    //                 pCause        (Throwable) - the exception that caused the failure (may be null)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static boolean shouldRetry(Scenario pScenario, Throwable pCause)
    {
        if (iMaxRetries <= 0)
        {
            return false;
        }

        String iScenarioId = pScenario.getId();

        // Never retry genuine assertion failures — these are real bugs, not flakiness
        if (pCause instanceof AssertionError)
        {
            log.warning("[RetryAnalyser] AssertionError — no retry for : " + pScenario.getName());
            return false;
        }

        // Check if the exception type is in the retryable list
        if (pCause != null && !isRetryable(pCause))
        {
            log.warning("[RetryAnalyser] Exception not retryable [" + pCause.getClass().getSimpleName()
                    + "] for scenario : " + pScenario.getName());
            return false;
        }

        int iCurrentCount = iRetryCountMap.getOrDefault(iScenarioId, 0);

        if (iCurrentCount >= iMaxRetries)
        {
            log.severe("[RetryAnalyser] Max retries (" + iMaxRetries + ") exhausted for : "
                    + pScenario.getName());
            iRetryCountMap.remove(iScenarioId);
            return false;
        }

        iRetryCountMap.put(iScenarioId, iCurrentCount + 1);

        log.warning("[RetryAnalyser] Retrying scenario [attempt " + (iCurrentCount + 1)
                + "/" + iMaxRetries + "] : " + pScenario.getName());

        return true;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRetryCount
    // Description   : Returns the number of retries attempted so far for a given scenario ID
    // Parameters    : pScenarioId (String) - Cucumber scenario ID
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static int getRetryCount(String pScenarioId)
    {
        return iRetryCountMap.getOrDefault(pScenarioId, 0);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : clearRetryState
    // Description   : Clears all retry state. Called between test cases in TestRunner to prevent
    //                 state leaking from one feature file execution to the next.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void clearRetryState()
    {
        iRetryCountMap.clear();
        log.info("[RetryAnalyser] Retry state cleared.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getMaxRetries
    // Description   : Returns the configured maximum retry count
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static int getMaxRetries()
    {
        return iMaxRetries;
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------------------------------------------------------------

    private static boolean isRetryable(Throwable pCause)
    {
        if (iRetryableExceptions.isEmpty())
        {
            // If no whitelist configured, retry on any non-assertion exception
            return true;
        }

        String iExceptionSimpleName = pCause.getClass().getSimpleName();

        // Check the exception itself and its causes up the chain
        Throwable iCurrent = pCause;
        while (iCurrent != null)
        {
            if (iRetryableExceptions.contains(iCurrent.getClass().getSimpleName()))
            {
                return true;
            }
            iCurrent = iCurrent.getCause();
        }

        return false;
    }

    private static Set<String> resolveRetryableExceptions()
    {
        Set<String> iSet = new HashSet<>();

        try
        {
            String iRaw = ConfigManager.getOrDefault("retry.on.exceptions",
                    "TimeoutException,StaleElementReferenceException");

            Arrays.stream(iRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(iSet::add);
        }
        catch (Exception iException)
        {
            // Fallback to safe defaults if config fails
            iSet.add("TimeoutException");
            iSet.add("StaleElementReferenceException");
        }

        log.info("[RetryAnalyser] Retryable exceptions : " + iSet + " | Max retries : " + ConfigManager.getInt("retry.count", 1));
        return iSet;
    }
}