// ===================================================================================================================================
// File          : TC_03_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_03_ENTS - Transfer Application E2E (cross-agent).
//
//                 Only the steps that are unique to a cross-agent transfer live here. The difference from
//                 TC_01_ENTS / TC_02_ENTS: the transferee belongs to a DIFFERENT agent, so the session logs out and
//                 back in as that agent before the acceptance.
//
//                 Steps in this file (4):
//                   1. the agent logs out and re-logs in as the transferee agent {string}
//                   2. the agent logs out and re-log in as the transferee agent {string}   (no logout - see below)
//                   3. the agent logs out and re-logs in as the transferor agent
//                   4. the agent completes the cross-agent transferee acceptance flow (DataTable)
//
//                 Steps 1 and 2 have almost the same text and were both in the Selenium file; step 2 never logged out,
//                 so it is kept as the "already logged out" variant. Both are here so either feature wording binds.
//
//                 Playwright edition (22-09-2026):
//                   - Same step texts, same ObjectRepository keys, same flow.
//                   - The ~250 lines of copied login code (username, PIN loop, TOTP, T&C, Account Expired) are gone;
//                     all four steps use the shared EntsSession.loginAs / EntsSession.logout.
//                   - Step 3 took the transferor login from the system property TD:Username / Hooks.RUNTIME_USERNAME.
//                     It now remembers the agent who was logged in when step 1 or 2 ran, and falls back to those.
//                   - Step 3 used iExitBISSLink / iLogoutBtn / iWelcomeLoginBtn, which are not in the Playwright
//                     ObjectRepository; it uses the same logout as every other ENTS step (iExitLink / iLogoutbtn).
//                   - Step 4 searches the transferee herd with EntsSession.openTransferOutHerd, which waits for the row
//                     of THAT herd and skips rows marked expired, instead of clicking the first View link on the page.
//                   - Step 4 takes the key from TC_01_ENTS.iCapturedTransferKey and asserts it is there first.
//                   - DataTable values may be runtime tokens such as {transferee.herd} (utilities.EntsTestData).
//
//                 Runtime data for a cross-agent transfer:
//                   The create step (TC_01_ENTS) takes an optional transfereeAgent row in its DataTable. With it, the
//                   transferor herd comes from the logged-in agent's pool and the transferee herd from that other
//                   agent's pool (EntsTestData.nextCrossAgentPair). Use the same login in this file's step 1, or put
//                   {transferee.agent} in the feature file so both always match.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import stepdefinitions.Hooks;
import utilities.EntsTestData;
import utilities.ObjReader;

import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static stepdefinitions.ENTS.EntsSession.isVisible;

public class TC_03_ENTS
{
    private static final Logger log = Logger.getLogger(TC_03_ENTS.class.getName());

    // The agent who created the transfer. Remembered when the session switches to the transferee agent, so the
    // "re-logs in as the transferor agent" step can go back without the feature file naming the login again.
    private static String iTransferorAgent = "";


    // ===================================================================================================================================
    //  CROSS-AGENT SESSION MANAGEMENT - logout and re-login
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the transferee agent {string}
    // Description   : Logs out of the transferor agent session and logs in as the transferee agent. The transferor
    //                 login is remembered for the "re-logs in as the transferor agent" step.
    // Parameters    : pUsername (String) - transferee agent login, e.g. "aga6325", or {transferee.agent}
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright, shared login)
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the transferee agent {string}")
    public void theAgentLogsOutAndReLogsInAsTheTransfereeAgent(String pUsername)
    {
        String iUsername = EntsTestData.resolve(pUsername);
        log.info("[STEP] When the agent logs out and re-logs in as the transferee agent: " + iUsername);

        rememberTransferorAgent();
        EntsSession.logout();
        EntsSession.loginAs(iUsername, "[LOGIN-TRANSFEREE-AGENT]");

        log.info("Logged in as the transferee agent: " + iUsername + " (transferor agent kept: " + iTransferorAgent + ")");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-log in as the transferee agent {string}
    // Description   : Same as the step above but WITHOUT the logout - the Selenium version of this wording assumed the
    //                 session was already logged out by the previous step. Kept so both feature wordings bind.
    // Parameters    : pUsername (String) - transferee agent login, or {transferee.agent}
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright, shared login)
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-log in as the transferee agent {string}")
    public void theAgentLogsOutAndReLogInAsTheTransfereeAgent(String pUsername)
    {
        String iUsername = EntsTestData.resolve(pUsername);
        log.info("[STEP] When the agent re-logs in as the transferee agent (already logged out): " + iUsername);

        rememberTransferorAgent();
        EntsSession.loginAs(iUsername, "[LOGIN-TRANSFEREE-AGENT]");

        log.info("Logged in as the transferee agent: " + iUsername);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the transferor agent
    // Description   : Logs out of the transferee agent session, logs back in as the agent who created the transfer and
    //                 opens BISS -> Home -> My Clients -> Transfers, ready for the next section.
    //                 Login resolution order: the agent remembered when the session switched to the transferee agent,
    //                 the current pair's agent, the system property TD:Username, Hooks.RUNTIME_USERNAME.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright, shared login)
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the transferor agent")
    public void theAgentLogsOutAndReLogsInAsTheTransferorAgent()
    {
        log.info("[STEP] When the agent logs out and re-logs in as the transferor agent");

        String iUsername = transferorAgent();
        Assertions.assertFalse(iUsername.isEmpty(),
                "No transferor agent is known. The transfer must be created by an agent before switching back to it.");

        EntsSession.logout();
        EntsSession.loginAs(iUsername, "[LOGIN-TRANSFEROR-AGENT]");
        EntsSession.openBissMyClientsTransfers();

        log.info("Re-logged in as the transferor agent: " + iUsername);
    }


    // ===================================================================================================================================
    //  CROSS-AGENT TRANSFEREE ACCEPTANCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent completes the cross-agent transferee acceptance flow (DataTable)
    // Description   : 1. Searches the transferee herd and opens the first not-expired row
    //                 2. ETF button shown : ETF -> transfer key -> View Transfer Application
    //                    otherwise        : View Transfer Application (2) -> transfer key -> View
    //                 3. Enters notes, Submit Application to DAFM, accepts T&C, confirms
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number, or {transferee.herd}
    //                   notes          (String) - transferee notes, e.g. "Approved Test"
    //
    //                 Depends on the key captured by TC_01_ENTS "the transfer key should be captured".
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
    // ***************************************************************************************************************************************************************************************
    @And("the agent completes the cross-agent transferee acceptance flow")
    public void theAgentCompletesTheCrossAgentTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] And the agent completes the cross-agent transferee acceptance flow");

        Map<String, String> iData = EntsTestData.resolveTable(pDataTable.asMap(String.class, String.class));

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();
        String iTransferKey    = TC_01_ENTS.iCapturedTransferKey;

        Assertions.assertFalse(iTransferKey == null || iTransferKey.isEmpty(),
                "Transfer key must have been captured in the Transferor flow before the transferee agent can accept.");

        // Search the transferee herd in THIS agent's Transfer Out list and open its row
        EntsSession.openTransferOutHerd(iTransfereeHerd);
        log.info("Transferee herd opened: " + iTransfereeHerd);

        if (isVisible(ObjReader.getLocator("iETFBtn"), 3))
        {
            // Partner dashboard - dedicated "Access an application using Transfer Key" button
            iAction("CLICK", "XPATH", ObjReader.getLocator("iETFBtn"), null);
            log.info("ETF button clicked.");
        }
        else
        {
            // Agent dashboard - standard View Transfer Application button
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn2"), null);
            iAction("CLICK",       "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn2"), null);
        }

        // Transfer key, then open the application
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iTransferKey);
        log.info("Transfer key entered: " + iTransferKey);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn"), null);

        // Notes, Submit to DAFM, T&C, confirm
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferSubmitToDAFMBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTandCCheckbox"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSubmitBtn"), null);

        log.info("Cross-agent transferee acceptance completed for herd: " + iTransfereeHerd);
    }


    // ===================================================================================================================================
    //  HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : rememberTransferorAgent
    // Description   : Stores the agent who is logged in right now, so the session can come back to it later
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-09-2026
    // ***************************************************************************************************************************************************************************************
    private static void rememberTransferorAgent()
    {
        String iCurrent = EntsTestData.loggedInUser();
        if (iCurrent != null && !iCurrent.isBlank())
        {
            iTransferorAgent = iCurrent.trim();
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : transferorAgent
    // Description   : The agent who created the transfer: remembered login, else the current pair's agent, else the
    //                 system property TD:Username, else Hooks.RUNTIME_USERNAME. "" when none of them is set.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-09-2026
    // ***************************************************************************************************************************************************************************************
    private static String transferorAgent()
    {
        if (!iTransferorAgent.isBlank()) return iTransferorAgent;

        EntsTestData.TransferPair iPair = EntsTestData.currentPair();
        if (iPair != null && iPair.agent != null && !iPair.agent.isBlank()) return iPair.agent.trim();

        String iProperty = System.getProperty("TD:Username", "").trim();
        if (!iProperty.isEmpty()) return iProperty;

        return Hooks.RUNTIME_USERNAME == null ? "" : Hooks.RUNTIME_USERNAME.trim();
    }
}
