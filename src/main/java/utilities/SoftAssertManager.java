// ===================================================================================================================================
// File          : SoftAssertManager.java
// Package       : utilities
// Description   : Soft assertion manager for the BISS automation framework.
//                 Unlike hard assertions (which halt execution immediately), soft assertions
//                 collect ALL failures throughout a scenario and report them together at the end.
//
//                 This is critical for validation-heavy scenarios where you need to know ALL
//                 broken fields on a form, not just the first one.
//
// Folder        : src/main/java/utilities/SoftAssertManager.java
//
// Usage in Step Definitions:
//   SoftAssertManager.softVerifyText("Page Title", actualTitle, "CONTAINS:Welcome");
//   SoftAssertManager.softVerifyTrue("Submit button visible", submitBtn.isDisplayed());
//   SoftAssertManager.softVerifyEquals("Error count", actualCount, 3);
//   SoftAssertManager.assertAll(); // called in @After — fails scenario with full failure list
//
// Integration:
//   - Hooks.java @After calls SoftAssertManager.assertAll() before screenshot/report steps
//   - SoftAssertManager.reset() is called in @Before to clear state between scenarios
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SoftAssertManager
{
    private static final Logger log = Logger.getLogger(SoftAssertManager.class.getName());

    // ThreadLocal list — each Cucumber thread maintains its own independent failure list
    private static final ThreadLocal<List<String>> iFailures =
            ThreadLocal.withInitial(ArrayList::new);

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor — static utility only
    // -------------------------------------------------------------------------------------------------------------------------------
    private SoftAssertManager() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : softVerifyText
    // Description   : Verifies a text value using the same prefix logic as CommonFunctions VERIFYTEXT.
    //                 Supported prefixes: EXACT: | CONTAINS: (default) | STARTSWITH:
    //                 Records a failure if the verification fails — does NOT throw.
    // Parameters    : pCheckpoint  (String) - human-readable name for this check (shown in report)
    //                 pActual      (String) - actual text value from the UI
    //                 pExpected    (String) - expected value with optional prefix
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void softVerifyText(String pCheckpoint, String pActual, String pExpected)
    {
        try
        {
            String iActual   = pActual   == null ? "" : pActual.trim();
            String iExpected = pExpected == null ? "" : pExpected.trim();
            boolean iPassed;

            if (iExpected.startsWith("EXACT:"))
            {
                String iExpectedValue = iExpected.substring(6).trim();
                iPassed = iActual.equals(iExpectedValue);
                if (!iPassed)
                {
                    record(pCheckpoint, "EXACT match failed | Expected='" + iExpectedValue + "' | Actual='" + iActual + "'");
                }
            }
            else if (iExpected.startsWith("STARTSWITH:"))
            {
                String iExpectedValue = iExpected.substring(11).trim();
                iPassed = iActual.startsWith(iExpectedValue);
                if (!iPassed)
                {
                    record(pCheckpoint, "STARTSWITH failed | Expected prefix='" + iExpectedValue + "' | Actual='" + iActual + "'");
                }
            }
            else
            {
                // Default: CONTAINS
                String iExpectedValue = iExpected.startsWith("CONTAINS:") ? iExpected.substring(9).trim() : iExpected;
                iPassed = iActual.contains(iExpectedValue);
                if (!iPassed)
                {
                    record(pCheckpoint, "CONTAINS failed | Expected to contain='" + iExpectedValue + "' | Actual='" + iActual + "'");
                }
            }

            if (iPassed)
            {
                log.info("[SoftAssert] PASS | " + pCheckpoint);
            }
        }
        catch (Exception iException)
        {
            record(pCheckpoint, "Exception during text verification : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : softVerifyTrue
    // Description   : Verifies a boolean condition. Records failure if condition is false.
    // Parameters    : pCheckpoint (String)  - human-readable name for this check
    //                 pCondition  (boolean) - the condition to assert as true
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void softVerifyTrue(String pCheckpoint, boolean pCondition)
    {
        if (!pCondition)
        {
            record(pCheckpoint, "Expected condition to be TRUE but was FALSE");
        }
        else
        {
            log.info("[SoftAssert] PASS | " + pCheckpoint);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : softVerifyFalse
    // Description   : Verifies a boolean condition is false. Records failure if condition is true.
    // Parameters    : pCheckpoint (String)  - human-readable name for this check
    //                 pCondition  (boolean) - the condition to assert as false
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void softVerifyFalse(String pCheckpoint, boolean pCondition)
    {
        if (pCondition)
        {
            record(pCheckpoint, "Expected condition to be FALSE but was TRUE");
        }
        else
        {
            log.info("[SoftAssert] PASS | " + pCheckpoint);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : softVerifyEquals
    // Description   : Verifies two objects are equal using .equals(). Records failure if not equal.
    // Parameters    : pCheckpoint (String) - human-readable name for this check
    //                 pActual     (Object) - actual value
    //                 pExpected   (Object) - expected value
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void softVerifyEquals(String pCheckpoint, Object pActual, Object pExpected)
    {
        boolean iPassed = (pActual == null && pExpected == null)
                || (pActual != null && pActual.equals(pExpected));

        if (!iPassed)
        {
            record(pCheckpoint, "Equals check failed | Expected='" + pExpected + "' | Actual='" + pActual + "'");
        }
        else
        {
            log.info("[SoftAssert] PASS | " + pCheckpoint);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : softVerifyNotNull
    // Description   : Verifies a value is not null. Records failure if null.
    // Parameters    : pCheckpoint (String) - human-readable name for this check
    //                 pActual     (Object) - value to check
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void softVerifyNotNull(String pCheckpoint, Object pActual)
    {
        if (pActual == null)
        {
            record(pCheckpoint, "Expected non-null value but was NULL");
        }
        else
        {
            log.info("[SoftAssert] PASS | " + pCheckpoint);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : assertAll
    // Description   : Evaluates all collected soft assertion failures.
    //                 If any failures exist, throws a single AssertionError listing ALL failures.
    //                 Call this in Hooks @After before screenshot logic so failures are captured.
    //                 Always clears the failure list after evaluation.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void assertAll()
    {
        List<String> iCurrentFailures = iFailures.get();

        if (iCurrentFailures.isEmpty())
        {
            log.info("[SoftAssert] All soft assertions passed.");
            return;
        }

        StringBuilder iMessage = new StringBuilder();
        iMessage.append("\n========== SOFT ASSERTION FAILURES (")
                .append(iCurrentFailures.size())
                .append(") ==========\n");

        for (int i = 0; i < iCurrentFailures.size(); i++)
        {
            iMessage.append("  [").append(i + 1).append("] ").append(iCurrentFailures.get(i)).append("\n");
        }

        iMessage.append("==================================================");

        // Clear before throwing so the next scenario starts clean
        reset();

        throw new AssertionError(iMessage.toString());
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : reset
    // Description   : Clears all collected failures for the current thread.
    //                 Called in Hooks @Before to guarantee a clean slate for every scenario.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void reset()
    {
        iFailures.get().clear();
        iFailures.remove();
        log.info("[SoftAssert] State reset for current thread.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : hasFailures
    // Description   : Returns true if any soft assertion failures have been recorded this scenario.
    //                 Useful in step definitions to short-circuit further steps if verification failed.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static boolean hasFailures()
    {
        return !iFailures.get().isEmpty();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getFailureCount
    // Description   : Returns the number of soft assertion failures recorded so far in this scenario
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static int getFailureCount()
    {
        return iFailures.get().size();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getFailures
    // Description   : Returns an unmodifiable snapshot of all failures recorded so far
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static List<String> getFailures()
    {
        return Collections.unmodifiableList(new ArrayList<>(iFailures.get()));
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private helper
    // -------------------------------------------------------------------------------------------------------------------------------
    private static void record(String pCheckpoint, String pReason)
    {
        String iEntry = "Checkpoint [" + pCheckpoint + "] — " + pReason;
        iFailures.get().add(iEntry);
        log.severe("[SoftAssert] FAIL | " + iEntry);
    }
}