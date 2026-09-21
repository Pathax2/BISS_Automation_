// ===================================================================================================================================
// TC_08_ENTS.java | package stepdefinitions.ENTS
// Individual-as-TRANSFEROR + Agent-as-TRANSFEREE steps
//
// New steps (4):
//   1. the individual logs in as transferor {string}
//   2. the individual creates a transfer application with the following details (DataTable)
//   3. the agent logs in as transferee agent {string}
//   4. the agent completes the same agent transferee acceptance flow (DataTable)
//
// Reused: TC_01_ENTS (send/capture/submit), TC_05_ENTS (individual login pattern)
// Author: Aniket Pathare | Created: 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import commonFunctions.CommonFunctions;
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

public class TC_08_ENTS
{
    private static final Logger log = Logger.getLogger(TC_08_ENTS.class.getName());

    // ── Individual logs in as TRANSFEROR ──────────────────────────────────────────
    @When("the individual logs in as transferor {string}")
    public void theIndividualLogsInAsTransferor(String pUsername)
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

    // ── Individual creates transfer ──────────────────────────────────────────────
    // Same flow as TC_01_ENTS but: no signature form link, uses CRO doc type, no herd search
    // (Individual is already on their own dashboard — goes straight to Create Transfer)
    @And("the individual creates a transfer application with the following details")
    public void theIndividualCreatesATransferApplication(DataTable pDataTable) throws InterruptedException {
        log.info("[STEP] When the agent creates a transfer application with the following details");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

       // String iTransferorHerd = iData.get("transferorHerd").trim();
        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iTransfereeName = iData.get("transfereeName").trim();
        String iTransferType   = iData.get("transferType").trim();
        String iEntitlements   = iData.get("entitlements").trim();
        String iNotes          = iData.get("notes").trim();
        boolean iHasLeaseYear  = "Yes".equalsIgnoreCase(iData.getOrDefault("leaseYear", "").trim());



        // ── Step 2 : Click "Create Transfer Application" ─────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCreateTransferBtn"), null);

        // ── Step 3 : Search for the transferee ───────────────────────────────────────────
        // Click the Search button inside the transfer type dialog to open the search form
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTypeSearchBtn"), null);

        // Fill in transferee herd and name
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeHerdField"), iTransfereeHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeNameField"), iTransfereeName);

        // Execute the search
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSearchBtn"), null);
        log.info("Transferee searched: " + iTransfereeName + " (" + iTransfereeHerd + ")");

        // ── Step 4 : Select the transfer type ────────────────────────────────────────────
        Thread.sleep(1000);
        // The transfer type is a radio button or selectable row identified by its code
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + iTransferType + "')] | "
                        + "//tr[contains(.,'" + iTransferType + "')]//input | "
                        + "//*[@value='" + iTransferType + "']",
                null);
        Thread.sleep(1000);
        log.info("Transfer type selected: " + iTransferType);

        // Click Next to proceed past transfer type selection
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // ── Step 5 : Add entitlement ─────────────────────────────────────────────────────
        // Click the first "Add" entitlement button for the transferor
        //iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferAddEntitlementBtn"), null);

        // ── Click Add on the row with the highest Available Entitlements value ────────────
        // Reads all entitlement value cells, parses each as double, finds the max,
        // then clicks the Add button at the matching row index.
        // The two NodeLists are parallel — index N in values = index N in buttons.
        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iTransferEntitlementValues"), null);
        List<WebElement> iEntitlementCells = getDriver().findElements(By.xpath(ObjReader.getLocator("iTransferEntitlementValues")));
        List<WebElement> iAddButtons = getDriver().findElements(By.xpath(ObjReader.getLocator("iTransferAddEntitlementBtns")));

        if (iEntitlementCells.isEmpty())
        {
            throw new RuntimeException("[TRANSFER] No entitlement rows found in 'Entitlements available to transfer' table.");
        }

        int    iMaxIndex = 0;
        double iMaxValue = -1.0;

        for (int i = 0; i < iEntitlementCells.size(); i++)
        {
            String iRaw = iEntitlementCells.get(i).getText().trim();
            try
            {
                double iVal = Double.parseDouble(iRaw);
                log.info("[TRANSFER] Row " + (i + 1) + " entitlements: " + iVal);
                if (iVal > iMaxValue)
                {
                    iMaxValue = iVal;
                    iMaxIndex = i;
                }
            }
            catch (NumberFormatException e)
            {
                log.warning("[TRANSFER] Could not parse entitlement value at row "
                        + (i + 1) + ": '" + iRaw + "' — skipping.");
            }
        }

        log.info("[TRANSFER] Highest entitlement: " + iMaxValue + " at row " + (iMaxIndex + 1) + " — clicking Add.");

        WebElement iTargetAddBtn = iAddButtons.get(iMaxIndex);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block:'center'});", iTargetAddBtn);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", iTargetAddBtn);

        log.info("[TRANSFER] Add clicked for entitlement value: " + iMaxValue);


        // Enter the entitlement amount
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferEntitlementAmountField"), iEntitlements);

        // ── Step 5a : Lease year selection (only for type 211) ───────────────────────────
        // The lease requires selecting a lease year from a dropdown before adding
        if (iHasLeaseYear)
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearDropdown"), null);
            // Select the first available lease year option
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearFirstOption"), null);
            log.info("Lease year selected.");
        }

        // Click Add in the entitlement dialog to confirm
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogAddBtn"), null);

        // Click Next to proceed to the summary / notes page
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // ── Step 6 : Enter transfer notes ────────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);

      //  log.info("Transfer application created | Type=" + iTransferType + " | Transferor=" + iTransferorHerd + " → Transferee=" + iTransfereeHerd + " | Entitlements=" + iEntitlements);

    }

    // ── Agent logs in as TRANSFEREE ──────────────────────────────────────────────
    @When("the agent logs in as transferee agent {string}")
    public void theAgentLogsInAsTransfereeAgent(String pAgentUsername) throws InterruptedException {
        log.info("[STEP] When the agent logs out and re-logs in as the individual transferee: " + pAgentUsername);
        performLogout();

        {

            log.info("[LOGIN] Classic login detected.");

            // Hit the initial 'Log In' button on the BISS landing screen to get to the Keycloak form
            //iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

            // Type the agent's username — pulled from Hooks.RUNTIME_USERNAME which is resolved
            // at runtime from BISS_DATA + BISS_INET before any scenario executes.
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pAgentUsername);
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
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pAgentUsername);
                iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),       "TD:Password");
                iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),             null);

                log.info("[LOGIN] Re-attempting login with new agent: " + pAgentUsername);
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
                Thread.sleep(2000);
                log.info("[LOGIN] PIN login submitted.");

                for (int iNext = 1; iNext <= 6; iNext++)
                {
                    if (isVisible(By.xpath(ObjReader.getLocator("iNextBtnNewUser")), 1))
                    {
                        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtnNewUser"), null);
                        log.info("Clicked Next button - Attempt " + iNext);
                    }
                    else
                    {
                        log.info("Next button no longer available after " + (iNext - 1) + " clicks.");
                        break;
                    }
                }
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

    // ── Agent completes same-agent transferee acceptance ─────────────────────────
    // Same as TC_01_ENTS transferee flow but as a standalone step for cross-user scenarios
    @And("the agent completes the same agent transferee acceptance flow")
    public void theAgentCompletesTheSameAgentTransfereeAcceptanceFlow(DataTable pDataTable) throws InterruptedException {
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

        log.info("Individual transferee acceptance completed for herd: " + iTransfereeHerd);

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

    private boolean isVisible(By pLocator, int pSeconds) {
        try { new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(pLocator)); return true; }
        catch (Exception e) { return false; }
    }
}
