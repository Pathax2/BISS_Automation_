// ===================================================================================================================================
// File          : TC_07_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_07_ENTS — Transfer Application E2E (Agent to ETF Partner).
//
//                 This file contains ONLY the steps unique to Agent-to-ETF Partner transfers.
//                 The critical difference from Agent-to-Agent (TC_03_ENTS) and Agent-to-Individual (TC_05_ENTS):
//                   - ETF Partner logs in via the PARTNER login page (different URL)
//                   - Partner uses MS Authenticator OTP (not SMS OTP or PIN+TOTP)
//                   - Partner login flow: username → Login → password → Login → MS Auth OTP → Login
//                   - Partner clicks the "ETF" button on dashboard (not View or Transfers tab)
//                   - Partner searches for herd first, then clicks ETF button
//
//                 ┌──────────────────────────────────────────────────────────────────┬────────────────────┐
//                 │ Reused Steps                                                     │ Defined In         │
//                 ├──────────────────────────────────────────────────────────────────┼────────────────────┤
//                 │ the agent user is on the login page                              │ TC_03.java         │
//                 │ the agent logs into the application...                           │ TC_03.java         │
//                 │ the agent opens the {string} application                         │ TC_03.java         │
//                 │ the agent should land on the BISS Home page                      │ TC_03.java         │
//                 │ the agent navigates to ... Left Menu Link                        │ TC_03.java         │
//                 │ the agent switches to the {string} tab...                        │ TC_06.java         │
//                 │ the agent creates a transfer application with the following...   │ TC_01_ENTS.java    │
//                 │ the agent uploads the transferor signature document              │ TC_01_ENTS.java    │
//                 │ the agent sends the transfer for acceptance                      │ TC_01_ENTS.java    │
//                 │ the transfer key should be captured                              │ TC_01_ENTS.java    │
//                 │ the transfer should be submitted successfully                    │ TC_01_ENTS.java    │
//                 │ the agent logs out and re-logs in as the transferor agent        │ TC_03_ENTS.java    │
//                 └──────────────────────────────────────────────────────────────────┴────────────────────┘
//
//                 New steps in this file (2 total):
//                   1. the agent logs out and re-logs in as the ETF partner {string}
//                   2. the ETF partner completes the transferee acceptance flow (DataTable)
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ObjReader;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_07_ENTS
{
    private static final Logger log = Logger.getLogger(TC_07_ENTS.class.getName());


    // ===================================================================================================================================
    //  ETF PARTNER LOGIN — Logout agent session, re-login as Partner
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the ETF partner {string}
    // Description   : Exits the current BISS/agent session, logs out, then performs a fresh
    //                 login using the PARTNER login page with MS Authenticator OTP.
    //
    //                 Key differences from TC_03_ENTS (Agent-to-Agent) and TC_05_ENTS (Individual):
    //                   1. Navigates to the Partner login page (different URL)
    //                   2. Partner login button is different from agent login button
    //                   3. Uses MS Authenticator OTP (not SMS OTP)
    //                   4. Three-click login sequence: username→Login, password→Login, OTP→Login
    //                   5. No personal details dialog (that's Individual-only)
    //                   6. Partner goes directly to BISS home — searches for herd from there
    //
    // Parameters    : pPartnerUsername (String) - Partner login username e.g. "agr15594", "agr15678", "agr15512"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the ETF partner {string}")
    public void theAgentLogsOutAndReLogsInAsTheETFPartner(String pPartnerUsername)
    {
        log.info("[STEP] When the agent logs out and re-logs in as the ETF partner: " + pPartnerUsername);

        // ── Step 1 : Exit BISS and logout ────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iExitBISSLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutBtn"), null);
        log.info("Logged out of agent session.");

        // ── Step 2 : Navigate to Partner login page ──────────────────────────────────────
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginLink"), null);
        }
        catch (Exception e)
        {
            log.info("Already on Partner login page — continuing.");
        }

        // ── Step 3 : Enter Partner username ──────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPartnerUsernameField"), pPartnerUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginBtn"), null);
        log.info("Partner username entered: " + pPartnerUsername);

        // ── Step 4 : Enter password ──────────────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginBtn"), null);

        // ── Step 5 : MS Authenticator OTP ────────────────────────────────────────────────
        // Partner uses Microsoft Authenticator — different from SMS OTP (agent) and PIN+TOTP (cross-agent)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iMSAuthOTPField"), "111111");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginBtn"), null);
        log.info("MS Authenticator OTP entered.");

        // ── Step 6 : Navigate to BISS ────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);

        log.info("Re-logged in as ETF partner: " + pPartnerUsername);
    }


    // ===================================================================================================================================
    //  ETF PARTNER TRANSFEREE ACCEPTANCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the ETF partner completes the transferee acceptance flow (DataTable)
    // Description   : After the ETF partner has logged in, this step:
    //                 1. Searches for the transferee herd
    //                 2. Clicks View on the searched herd
    //                 3. Clicks the ETF button on the transferee dashboard
    //                 4. Enters the captured transfer key
    //                 5. Views the transfer application
    //                 6. Enters transferee notes
    //                 7. Submits to DAFM with T&C acceptance
    //
    //                 Key differences from TC_03_ENTS (Agent acceptance) and TC_05_ENTS (Individual):
    //                   - Partner searches for herd first (like Agent), then clicks ETF button (unique to Partner)
    //                   - ETF button is a dedicated button, not the standard "View" button
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number to search for
    //                   notes          (String) - transferee notes e.g. "Approved Test"
    //
    //                 Depends on: transfer key from System.getProperty("lastCapturedTransferKey")
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the ETF partner completes the transferee acceptance flow")
    public void theETFPartnerCompletesTheTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] And the ETF partner completes the transferee acceptance flow");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();

        // ── Search for the transferee herd ───────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iTransfereeHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
        log.info("Partner herd opened: " + iTransfereeHerd);

        // ── Click the ETF button ─────────────────────────────────────────────────────────
        // ETF button is unique to the Partner dashboard — it's NOT the standard View button
        iAction("CLICK", "XPATH", ObjReader.getLocator("iETFBtn"), null);
        log.info("ETF button clicked.");

        // ── Enter the captured transfer key ──────────────────────────────────────────────
        String iTransferKey = System.getProperty("lastCapturedTransferKey", "");
        Assertions.assertFalse(iTransferKey.isEmpty(),
                "Transfer key must have been captured in the Transferor flow before the ETF Partner can accept.");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iTransferKey);
        log.info("Transfer key entered: " + iTransferKey);

        // ── View the transfer application ────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn"), null);

        // ── Enter transferee notes ───────────────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);

        // ── Submit to DAFM ───────────────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferSubmitToDAFMBtn"), null);

        // Accept T&C
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTandCCheckbox"), null);

        // Confirm submission
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSubmitBtn"), null);

        log.info("ETF partner transferee acceptance completed for herd: " + iTransfereeHerd);
    }
}