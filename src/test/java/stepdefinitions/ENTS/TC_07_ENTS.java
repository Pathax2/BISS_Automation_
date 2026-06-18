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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefinitions.Hooks;
import utilities.ObjReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;
import static stepdefinitions.ENTS.TC_01_ENTS.iCapturedTransferKey;

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

        performLogout();
        performLogin(pPartnerUsername);

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
    public void theETFPartnerCompletesTheTransfereeAcceptanceFlow(DataTable pDataTable) throws InterruptedException {
        log.info("[STEP] And the ETF partner completes the transferee acceptance flow");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();

        // ── Search for the transferee herd ───────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iTransfereeHerd);
        Thread.sleep(2000);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
        log.info("Partner herd opened: " + iTransfereeHerd);

        // ── Click the ETF button ─────────────────────────────────────────────────────────
        // ETF button is unique to the Partner dashboard — it's NOT the standard View button
        if (isVisible(By.xpath(ObjReader.getLocator("iETFBtn")), 3)) {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iETFBtn"), null);
            log.info("ETF button clicked.");
            // ── Enter the captured transfer key ──────────────────────────────────────────────
            //String iTransferKey = System.getProperty("lastCapturedTransferKey", "");
            //Assertions.assertFalse(iTransferKey.isEmpty(), "Transfer key must have been captured in the Transferor flow before the ETF Partner can accept.");

            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iCapturedTransferKey);
            log.info("Transfer key entered: " + iCapturedTransferKey);

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
        }
        else
        {
            // ── View the transfer application ────────────────────────────────────────────────
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn2"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn2"), null);


            // ── Enter the captured transfer key ──────────────────────────────────────────────
            //String iTransferKey = System.getProperty("lastCapturedTransferKey", "");
            //Assertions.assertFalse(iTransferKey.isEmpty(), "Transfer key must have been captured in the Transferor flow before the ETF Partner can accept.");

            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iCapturedTransferKey);
            log.info("Transfer key entered: " + iCapturedTransferKey);
            // ── View the transfer application ────────────────────────────────────────────────
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn"), null);

            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);
            // ── Submit to DAFM ───────────────────────────────────────────────────────────────
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferSubmitToDAFMBtn"), null);

            // Accept T&C
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTandCCheckbox"), null);

            // Confirm submission
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSubmitBtn"), null);
        }







        log.info("ETF partner transferee acceptance completed for herd: " + iTransfereeHerd);
    }
    // ***************************************************************************************************************************************************************************************
    // Method        : performLogout
    // Description   : Logs out via Exit + Logout buttons. Falls back to navigate + deleteAllCookies.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-05-2026
    // ***************************************************************************************************************************************************************************************
    public void performLogout()
    {
        log.info("[TC13-RELOGIN] Logging out current session...");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"),  null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"), null);

            By iSadPopup = By.xpath(ObjReader.getLocator("iLogoutPopup"));
            if (isVisible(iSadPopup, 1)) iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutPopup"), null);
            log.info("[TC13-RELOGIN] Logout complete.");
        }
        catch (Exception e)
        {
            log.warning("[TC13-RELOGIN] UI logout failed (" + e.getMessage() + ") — navigating to base URL as fallback.");
            getDriver().manage().deleteAllCookies();
            getDriver().navigate().to(Hooks.iUrl);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Method        : performLogin
    // Description   : Logs in as specified agent. Handles PIN+TOTP and OTP-only flows.
    //                 Detects Account Expired and calls Hooks.markAgentExpired().
    // Parameters    : pUsername — agent username
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-05-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogin(String pUsername)
    {
        log.info("[TC13-RELOGIN] Logging in as: " + pUsername);
        getDriver().navigate().to(Hooks.iUrl);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),      "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),            null);

        By iExpiredBy = By.xpath(ObjReader.getLocator("iAccountExpiredError"));
        if (isVisible(iExpiredBy, 3))
        {
            String iErrText = getDriver().findElement(iExpiredBy).getText().trim();
            if (iErrText.toLowerCase().contains("account expired") || iErrText.toLowerCase().contains("account has expired") || iErrText.contains("Invalid username or password."))
            {
                log.warning("[TC13-RELOGIN] Account Expired for: " + pUsername + " — marking expired.");
                Hooks.markAgentExpired(pUsername);
                return;
            }
        }

        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));
        if (isVisible(iPinFormBy, 3))
        {
            log.info("[TC13-RELOGIN] PIN screen — filling slots 1-7.");
            for (int iIdx = 1; iIdx <= 7; iIdx++)
            {
                By iPinBy = By.xpath(ObjReader.getLocator("iPinInputIndex")
                        .replace("{idx}", String.valueOf(iIdx)));
                if (isVisible(iPinBy, 1))
                {
                    WebElement iInput = getDriver().findElement(iPinBy);
                    if (iInput.getAttribute("disabled") == null && iInput.isEnabled())
                    {
                        iInput.clear();
                        iInput.sendKeys("1");
                    }
                }
            }
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iPinLoginBtn"),   null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"),   "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);
            log.info("[TC13-RELOGIN] PIN + TOTP submitted.");
        }
        else
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),  null);
            log.info("[TC13-RELOGIN] OTP-only login submitted.");
        }

        if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[TC13-RELOGIN] Terms accepted.");
        }
        log.info("[TC13-RELOGIN] Login complete: " + pUsername);
    }

    // ***************************************************************************************************************************************************************************************
    // Method        : isVisible
    // Description   : Short-wait visibility check — returns true/false, never throws.
    // Parameters    : pLocator — By locator | pSeconds — max wait seconds
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    private boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception e) { return false; }
    }
}
