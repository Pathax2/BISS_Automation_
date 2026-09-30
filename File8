// ===================================================================================================================================
// TC_11_ENTS.java | package stepdefinitions.ENTS
// ETF Partner-as-TRANSFEROR with MANUAL entitlements
//
// New steps (3):
//   1. the ETF partner logs in as transferor {string} — Background step
//   2. the ETF partner creates a transfer with manual entitlements (DataTable)
//   3. the ETF partner logs back in as transferor — re-login between sections
//
// KEY DIFFERENCE from standard transfers: uses Add Manual Entitlements button,
// Entitlement Type dropdown (BISS), Net UV field, and transfereeAddress field
// Author: Aniket Pathare | Created: 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import commonFunctions.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
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

import static commonFunctions.CommonFunctions.getDriver;
import static commonFunctions.CommonFunctions.iAction;

public class TC_11_ENTS
{
    private static final Logger log = Logger.getLogger(TC_11_ENTS.class.getName());
    private static String iPartnerUsername = "";

    @Given("the ETF partner logs in as transferor {string}")
    public void theETFPartnerLogsInAsTransferor(String pPartnerUsername) throws InterruptedException {

        log.info("[LOGIN] Classic login detected.");

        // Hit the initial 'Log In' button on the BISS landing screen to get to the Keycloak form
        //iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

        // Type the agent's username — pulled from Hooks.RUNTIME_USERNAME which is resolved
        // at runtime from BISS_DATA + BISS_INET before any scenario executes.
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pPartnerUsername);
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
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pPartnerUsername);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),       "TD:Password");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),             null);

            log.info("[LOGIN] Re-attempting login with new agent: " + pPartnerUsername);
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

    @When("the ETF partner creates a transfer with manual entitlements")
    public void theETFPartnerCreatesATransferWithManualEntitlements(DataTable pDataTable) throws InterruptedException {
        log.info("[STEP] ETF partner creates transfer with manual entitlements");
        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransferorHerd = iData.get("transferorHerd").trim();
        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iTransfereeName = iData.get("transfereeName").trim();
        String iTransferType   = iData.get("transferType").trim();
        String iEntitlements   = iData.get("entitlements").trim();
        String iEntType        = iData.getOrDefault("entitlementType", "BISS").trim();
        String iNetUV          = iData.getOrDefault("netUV", "1").trim();
        String iNotes          = iData.get("notes").trim();
        String iAddress        = iData.getOrDefault("transfereeAddress", "").trim();
        boolean iHasLeaseYear  = "Yes".equalsIgnoreCase(iData.getOrDefault("leaseYear", "").trim());

        // Search for transferor herd
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iTransferorHerd);
        Thread.sleep(2000);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);

        // Create Transfer Application
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCreateTransferBtn"), null);

        // Search transferee (with address field — unique to ETF transfers)
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTypeSearchBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeHerdField"), iTransfereeHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeNameField"), iTransfereeName);
        if (!iAddress.isEmpty())
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeAddressField"), iAddress);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSearchBtn"), null);

        // Select type + Next
        // ── Step 4 : Select the transfer type ────────────────────────────────────────────
        Thread.sleep(1000);
        // The transfer type is a radio button or selectable row identified by its code
        iAction("CLICK", "XPATH", "//mat-radio-button[contains(.,'" + iTransferType + "')] | " + "//tr[contains(.,'" + iTransferType + "')]//input | " + "//*[@value='" + iTransferType + "']", null);
        Thread.sleep(1000);
        log.info("Transfer type selected: " + iTransferType);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // MANUAL entitlement — different from standard flow
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferAddManualEntitlementBtn"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iTransferEntitlementTypeDropdown"), iEntType);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferEntitlementAmountField"), iEntitlements);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNetUVField"), iNetUV);

        if (isVisible(By.xpath(ObjReader.getLocator("iTransferLeaseYearDropdown")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearDropdown"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearFirstOption"), null);
        }
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogAddBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // Notes (no document upload for ETF manual transfers)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);
        log.info("ETF manual transfer created: " + iTransferType + " → " + iTransfereeHerd);
    }

    @When("the ETF partner logs back in as transferor")
    public void theETFPartnerLogsBackInAsTransferor() throws InterruptedException {
        log.info("[STEP] ETF partner logs back in as transferor");

        theETFPartnerLogsInAsTransferor(iPartnerUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);
    }
    private boolean isVisible(By pLocator, int pSeconds) {
        try { new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(pLocator)); return true; }
        catch (Exception e) { return false; }
    }
}
