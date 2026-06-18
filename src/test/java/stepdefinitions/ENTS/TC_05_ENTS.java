// ===================================================================================================================================
// File          : TC_05_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_05_ENTS — Transfer Application E2E (Agent to Individual).
//
//                 This file contains ONLY the steps unique to Agent-to-Individual transfers.
//                 The critical difference from TC_03_ENTS (Agent-to-Agent):
//                   - Individual users log in via a DIFFERENT login page URL
//                   - Individual usernames are non-agent format (e.g. "PAUDYFROG", "TERENCE1")
//                   - A "Close personal details" dialog appears after Individual login
//                   - Individual navigates directly to "Transfers" tab (no My Clients → tab switch)
//                   - Individual sees the transfer on their dashboard without a herd search
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
//                   1. the agent logs out and re-logs in as the individual transferee {string}
//                   2. the individual completes the transferee acceptance flow (DataTable)
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import commonFunctions.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefinitions.Hooks;
import utilities.ObjReader;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;
import static stepdefinitions.ENTS.TC_01_ENTS.iCapturedTransferKey;

public class TC_05_ENTS
{
    private static final Logger log = Logger.getLogger(TC_05_ENTS.class.getName());


    // ===================================================================================================================================
    //  INDIVIDUAL LOGIN — Logout agent session, re-login as Individual
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the individual transferee {string}
    // Description   : Exits the current BISS/agent session, logs out fully, then performs a
    //                 fresh login using the INDIVIDUAL login page with the supplied username.
    //
    //                 Key differences from TC_03_ENTS.theAgentLogsOutAndReLogsInAsTheTransfereeAgent():
    //                   1. Navigates to the Individual login page (different URL from agent login)
    //                   2. Uses the Individual username directly (e.g. "PAUDYFROG") not from TD property
    //                   3. Dismisses the "Close personal details" dialog that appears for Individual users
    //                   4. Navigates to BISS → Transfers directly (no My Clients tab switch)
    //
    // Parameters    : pIndividualUsername (String) - Individual login username e.g. "PAUDYFROG", "TERENCE1"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the individual transferee {string}")
    public void theAgentLogsOutAndReLogsInAsTheIndividualTransferee(String pUsername)
    {
        log.info("[STEP] When the agent logs out and re-logs in as the individual transferee: " + pUsername);
        performLogout();

        {

            log.info("[LOGIN] Classic login detected.");

            // Hit the initial 'Log In' button on the BISS landing screen to get to the Keycloak form
            //iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

            // Type the agent's username — pulled from Hooks.RUNTIME_USERNAME which is resolved
            // at runtime from BISS_DATA + BISS_INET before any scenario executes.
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pUsername);
            // Move past the username screen to get to the password entry form
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);

            // Type the password from the test data sheet (TD:Password column)
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");

            // Submit the password — this either takes us to the dashboard or triggers MFA
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);

            // ── Account Expired detection (Option 4 fix — 05-05-2026) ────────────────────
            // Keycloak shows "Account Expired" immediately after Login click when the agent's
            // SSO account has expired. Detect it here before waiting for PIN/OTP screen.
            // If found: mark agent expired → Hooks.markAgentExpired() re-resolves a fresh
            // herd+agent pair → update RUNTIME_USERNAME → re-attempt login with new agent.
            By iExpiredMsgBy = By.xpath("//*[contains(@class,'kc-feedback-text') " + "and contains(normalize-space(),'Account Expired')]");

            if (isVisible(iExpiredMsgBy, 3))
            {
                String iExpiredAgent = Hooks.RUNTIME_USERNAME;
                log.warning("[LOGIN] Account Expired detected for agent: " + iExpiredAgent + " — calling Hooks.markAgentExpired() to re-resolve.");

                // Mark expired + re-resolve new herd+agent into Hooks.RUNTIME_HERD / RUNTIME_USERNAME
                Hooks.markAgentExpired(iExpiredAgent);

                // Cancel the current Keycloak session and restart login with new agent
                iAction("CLICK", "XPATH", "//button[normalize-space()='Cancel'] | //a[normalize-space()='Cancel']", null);

                // Navigate back to base URL for a clean login state
                CommonFunctions.getDriver().navigate().to(Hooks.iUrl);
                // iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

                // Re-attempt login with the newly resolved agent
                // iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),     null);
                getDriver().navigate().to(Hooks.iUrl);
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pUsername);
                iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),       "TD:Password");
                iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),             null);

                log.info("[LOGIN] Re-attempting login with new agent: " + pUsername);
            }
            // ── End Account Expired detection ──────────────────────────────────────────────

            log.info("[STEP] Detect login screen and auto-login using simple PIN loop...");

            WebDriver driver = CommonFunctions.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

            By pinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

            // Check whether the PIN login screen has appeared (give it 3 seconds — no need to wait longer)
            if (isVisible(pinFormBy, 3)) {

                log.info("[LOGIN] PIN screen detected. Using simple loop 1 → 7.");

                // The PIN form has up to 7 digit slots — loop through all of them
                // and enter '1' into each one that is enabled and visible
                for (int idx = 1; idx <= 7; idx++) {

                    // Build the XPath for this particular PIN slot dynamically using the index
                    String dynamicXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));

                    By pinInputBy = By.xpath(dynamicXpath);

                    if (isVisible(pinInputBy, 1)) {

                        WebElement input = driver.findElement(pinInputBy);

                        boolean disabled = input.getAttribute("disabled") != null;

                        if (!disabled && input.isEnabled())
                        {
                            // Slot is active — clear any pre-filled value and type our digit
                            input.clear();
                            input.sendKeys("1");
                            log.info("[LOGIN] Entered '1' into PIN index: " + idx);
                        }
                        else
                        {
                            // Some PIN slots are disabled (grey) depending on the account setup — skip those
                            log.info("[LOGIN] PIN index " + idx + " is DISABLED — skipping.");
                        }

                    } else {
                        // This slot number doesn't exist in the DOM for this account — move on
                        log.info("[LOGIN] PIN index " + idx + " not present in DOM — skipping.");
                    }
                }

                // All available PIN digits are filled — submit the PIN form
                iAction("CLICK", "XPATH", ObjReader.getLocator("iPinLoginBtn"), null);
                log.info("[LOGIN] PIN login submitted.");

                if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 2))
                {
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                    // T&C checkbox is ticked — the Accept button should now be enabled, click it
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
                    log.info("[LOGIN] Accept Terms & Conditions completed.");
                }

                if (isVisible(By.xpath(ObjReader.getLocator("iNextBtnNewUser")), 3)) {
                    for (int iNext = 1; iNext <= 7; iNext++)
                    {
                        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtnNewUser"), null);
                    }
                }
                // After PIN, the system asks for a 6-digit TOTP code from the authenticator app
                // We're using a hardcoded test value here — replace with TD lookup if needed
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"), "111111");

                // Submit the TOTP code to complete the MFA flow
                iAction("CLICK", "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);

                log.info("[LOGIN] TOTP screen completed.");
                // Click the Terms & Conditions checkbox
                // Some accounts require accepting Terms & Conditions after first login or after a reset.
                // Check if the T&C screen is there — if it is, tick the checkbox and hit Accept.
                // If it's not there (most runs), this quietly skips without failing.
                if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
                {
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                    // T&C checkbox is ticked — the Accept button should now be enabled, click it
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
                    log.info("[LOGIN] Accept Terms & Conditions completed.");
                }




            }
            else
            {
                // No PIN screen appeared — this is the simpler OTP-only login path
                // Type the 6-digit OTP and submit directly
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
                iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);
                log.info("[LOGIN] Classic login completed.");
            }

        }
    }


    // ===================================================================================================================================
    //  INDIVIDUAL TRANSFEREE ACCEPTANCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the individual completes the transferee acceptance flow (DataTable)
    // Description   : After the individual user has logged in, this step:
    //                 1. Clicks View on the transfer row matching the herd number on the dashboard
    //                    (Individual doesn't need to search — their transfers are listed directly)
    //                 2. Enters the captured transfer key
    //                 3. Views the transfer application
    //                 4. Enters transferee notes
    //                 5. Submits to DAFM with T&C acceptance
    //
    //                 Key differences from TC_03_ENTS.theAgentCompletesTheCrossAgentTransfereeAcceptanceFlow():
    //                   - No herd search step — Individual sees transfers directly on their dashboard
    //                   - No "View Link for Searched Herd" step — goes straight to the transfer row
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number to match on the dashboard
    //                   notes          (String) - transferee notes e.g. "Approved Test"
    //
    //                 Depends on: transfer key from System.getProperty("lastCapturedTransferKey")
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the individual completes the transferee acceptance flow")
    public void theIndividualCompletesTheTransfereeAcceptanceFlow(DataTable pDataTable) throws InterruptedException {
        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();


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

        log.info("Individual transferee acceptance completed for herd: " + iTransfereeHerd);
    }


    // ===================================================================================================================================
    //  HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : handlePostLoginOTP
    // Description   : Handles OTP / PIN flow after Individual login. Same logic as TC_03_ENTS
    //                 but kept as a private method here to avoid cross-class coupling.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    private void handlePostLoginOTP()
    {
        WebDriver iDriver = getDriver();
        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

        if (isVisible(iPinFormBy, 3))
        {
            log.info("[LOGIN-INDIVIDUAL] PIN screen detected. Entering PIN 1→7.");

            for (int idx = 1; idx <= 7; idx++)
            {
                String iDynamicXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));
                By iPinBy = By.xpath(iDynamicXpath);

                if (isVisible(iPinBy, 1))
                {
                    try
                    {
                        org.openqa.selenium.WebElement iInput = iDriver.findElement(iPinBy);
                        if (iInput.getAttribute("disabled") == null && iInput.isEnabled())
                        {
                            iInput.clear();
                            iInput.sendKeys("1");
                        }
                    }
                    catch (Exception ignored) {}
                }
            }

            iAction("CLICK", "XPATH", ObjReader.getLocator("iPinLoginBtn"), null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"), "111111");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);

            // Handle T&C if present
            if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
            }
        }
        else
        {
            // Simple OTP flow
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);
        }

        log.info("[LOGIN-INDIVIDUAL] Post-login OTP handling complete.");
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
            getDriver().manage().deleteAllCookies();
            getDriver().navigate().to(Hooks.iUrl);
        }
        catch (Exception e)
        {
            log.warning("[TC13-RELOGIN] UI logout failed (" + e.getMessage() + ") — navigating to base URL as fallback.");
            getDriver().manage().deleteAllCookies();
            getDriver().navigate().to(Hooks.iUrl);
        }
    }
    /**
     * Short-wait visibility check — returns true if the element appears within pSeconds.
     */
    private boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            WebDriverWait iWait = new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds));
            iWait.until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
