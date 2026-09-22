// ===================================================================================================================================
// File          : TC_07_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_07_ENTS — Transfer Application E2E (Agent to ETF Partner).
//
//                 Steps in this file (2):
//                   1. the agent logs out and re-logs in as the ETF partner {string}
//                   2. the ETF partner completes the transferee acceptance flow (DataTable)
//
//                 Step 2 is also used by TC_01_ENTS (same agent): when the ETF button is not on the dashboard, the
//                 standard "View Transfer Application" route is taken, which is the agent's own acceptance screen.
//
//                 Playwright edition (22-09-2026):
//                   - Same step texts, same ObjectRepository keys, same flow.
//                   - transfereeHerd may be a runtime token such as {transferee.herd} (utilities.EntsTestData).
//                   - Login / logout use the shared EntsSession helpers.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import utilities.EntsTestData;
import utilities.ObjReader;

import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static stepdefinitions.ENTS.EntsSession.isVisible;
import static stepdefinitions.ENTS.EntsSession.pause;

public class TC_07_ENTS
{
    private static final Logger log = Logger.getLogger(TC_07_ENTS.class.getName());


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the ETF partner {string}
    // Description   : Logs out of the current session and logs in as the ETF partner.
    // Parameters    : pPartnerUsername (String) - Partner login e.g. "agr15594", "agr15678", "agr15512"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright)
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the ETF partner {string}")
    public void theAgentLogsOutAndReLogsInAsTheETFPartner(String pPartnerUsername)
    {
        log.info("[STEP] When the agent logs out and re-logs in as the ETF partner: " + pPartnerUsername);

        EntsSession.logout();
        EntsSession.loginAs(pPartnerUsername, "[ETF-LOGIN]");

        log.info("Re-logged in as ETF partner: " + pPartnerUsername);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the ETF partner completes the transferee acceptance flow (DataTable)
    // Description   : 1. Searches for the transferee herd and clicks View
    //                 2. ETF button shown (ETF partner): ETF -> transfer key -> View Transfer Application
    //                    otherwise (agent)             : View Transfer Application (2) -> transfer key -> View
    //                 3. Enters notes, Submit Application to DAFM, accepts T&C, confirms
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number, or {transferee.herd}
    //                   notes          (String) - transferee notes e.g. "Approved Test"
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
    // ***************************************************************************************************************************************************************************************
    @And("the ETF partner completes the transferee acceptance flow")
    public void theETFPartnerCompletesTheTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] And the ETF partner completes the transferee acceptance flow");

        Map<String, String> iData = EntsTestData.resolveTable(pDataTable.asMap(String.class, String.class));

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();
        String iTransferKey    = TC_01_ENTS.iCapturedTransferKey;

        Assertions.assertFalse(iTransferKey == null || iTransferKey.isEmpty(),
                "Transfer key must have been captured in the Transferor flow before the transferee can accept.");

        // ── Search for the transferee herd ───────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iTransfereeHerd);

        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        pause(2000);
             iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
        log.info("Transferee herd opened: " + iTransfereeHerd);

        if (isVisible(ObjReader.getLocator("iETFBtn"), 3))
        {
            // ETF partner dashboard - dedicated ETF button
            iAction("CLICK", "XPATH", ObjReader.getLocator("iETFBtn"), null);
            log.info("ETF button clicked.");
        }
        else
        {
            // Agent dashboard - standard View Transfer Application button
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn2"), null);
            iAction("CLICK",       "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn2"), null);
        }

        // ── Enter the captured transfer key and open the application ─────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iTransferKey);
        log.info("Transfer key entered: " + iTransferKey);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn"), null);

        // ── Notes, Submit to DAFM, T&C, confirm ─────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferSubmitToDAFMBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTandCCheckbox"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSubmitBtn"), null);

        log.info("Transferee acceptance completed for herd: " + iTransfereeHerd);
    }
}
