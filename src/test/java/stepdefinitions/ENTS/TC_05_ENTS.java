// ===================================================================================================================================
// File          : TC_05_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_05_ENTS - Transfer Application E2E (Agent to Individual).
//
//                 Only the steps unique to an Agent-to-Individual transfer live here. The difference from the
//                 agent-to-agent flow: the individual sees the transfer on their own dashboard, so there is no
//                 My Clients herd search before the acceptance.
//
//                 Steps in this file (2):
//                   1. the agent logs out and re-logs in as the individual transferee {string}
//                   2. the individual completes the transferee acceptance flow (DataTable)
//
//                 Every other step of TC_05_ENTS.feature is already defined:
//                   login / portal navigation     -> stepdefinitions.TC_03
//                   tab switching                 -> stepdefinitions.TC_06
//                   create / upload / send / key  -> stepdefinitions.ENTS.TC_01_ENTS
//                   transferor re-login           -> stepdefinitions.ENTS.TC_03_ENTS
//
//                 Playwright edition (22-09-2026):
//                   - Same step texts, same ObjectRepository keys, same flow.
//                   - The copied login block (username, PIN loop, TOTP, T&C, Account Expired) is replaced by the
//                     shared EntsSession.loginAs / EntsSession.logout.
//                   - The acceptance screens are the shared EntsSession.acceptWithTransferKey, the same code the
//                     agent and ETF partner acceptance steps use.
//                   - The key comes from TC_01_ENTS.iCapturedTransferKey and is asserted before it is typed; the
//                     Selenium version read a system property that nothing set.
//                   - The unused private handlePostLoginOTP() helper is gone.
//                   - DataTable values may be runtime tokens such as {transferee.herd} (utilities.EntsTestData).
//
//                 Runtime data: the transferee here is an individual, not an agent's client, so its herd cannot come
//                 from the Agent Login query. Keep the individual's herd and login hard-coded in the feature file, or
//                 ask for the Individual Login query to be wired to a token of its own.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import utilities.EntsTestData;

import java.util.Map;
import java.util.logging.Logger;

public class TC_05_ENTS
{
    private static final Logger log = Logger.getLogger(TC_05_ENTS.class.getName());


    // ===================================================================================================================================
    //  INDIVIDUAL LOGIN
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the individual transferee {string}
    // Description   : Logs out of the agent session and logs in as the individual, who lands on their own dashboard.
    // Parameters    : pUsername (String) - individual login, e.g. "PAUDYFROG", "TERENCE1"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright, shared login)
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the individual transferee {string}")
    public void theAgentLogsOutAndReLogsInAsTheIndividualTransferee(String pUsername)
    {
        String iUsername = EntsTestData.resolve(pUsername);
        log.info("[STEP] When the agent logs out and re-logs in as the individual transferee: " + iUsername);

        EntsSession.logout();
        EntsSession.loginAs(iUsername, "[LOGIN-INDIVIDUAL]");

        log.info("Logged in as the individual transferee: " + iUsername);
    }


    // ===================================================================================================================================
    //  INDIVIDUAL TRANSFEREE ACCEPTANCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the individual completes the transferee acceptance flow (DataTable)
    // Description   : The individual is already on the dashboard that lists their own transfers, so this step goes
    //                 straight to the acceptance screens: ETF button or View Transfer Application, the captured
    //                 transfer key, notes, Submit Application to DAFM, T&C and confirm.
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number, used for the log only (no search on this dashboard)
    //                   notes          (String) - transferee notes, e.g. "Approved Test"
    //
    //                 Depends on the key captured by TC_01_ENTS "the transfer key should be captured".
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright)
    // ***************************************************************************************************************************************************************************************
    @And("the individual completes the transferee acceptance flow")
    public void theIndividualCompletesTheTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] And the individual completes the transferee acceptance flow");

        Map<String, String> iData = EntsTestData.resolveTable(pDataTable.asMap(String.class, String.class));

        String iTransfereeHerd = iData.getOrDefault("transfereeHerd", "").trim();
        String iNotes          = iData.get("notes").trim();
        String iTransferKey    = TC_01_ENTS.iCapturedTransferKey;

        Assertions.assertFalse(iTransferKey == null || iTransferKey.isEmpty(),
                "Transfer key must have been captured in the Transferor flow before the individual can accept.");

        EntsSession.acceptWithTransferKey(iTransferKey, iNotes);

        log.info("Individual transferee acceptance completed for herd: " + iTransfereeHerd);
    }
}
