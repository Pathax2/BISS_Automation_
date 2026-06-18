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
    public void theIndividualCreatesATransferApplication(DataTable pDataTable)
    {
        log.info("[STEP] Individual creates transfer application");
        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iTransfereeName = iData.get("transfereeName").trim();
        String iTransferType   = iData.get("transferType").trim();
        String iEntitlements   = iData.get("entitlements").trim();
        String iNotes          = iData.get("notes").trim();
        boolean iHasLeaseYear  = "Yes".equalsIgnoreCase(iData.getOrDefault("leaseYear", "").trim());

        // Create Transfer Application (Individual is already on Transfers tab)
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCreateTransferBtn"), null);

        // Search transferee
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTypeSearchBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeHerdField"), iTransfereeHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeNameField"), iTransfereeName);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSearchBtn"), null);

        // Select transfer type

        String iTransferType1 = String.format(ObjReader.getLocator("iTransferType"), iTransferType);

        iAction("CLICK", "XPATH", iTransferType1, null);

        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // Add entitlement
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferAddEntitlementBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferEntitlementAmountField"), iEntitlements);
        if (iHasLeaseYear)
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearDropdown"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearFirstOption"), null);
        }
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogAddBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // Notes
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);

        // Upload CRO document (different from agent's signature doc)
        String iFilePath = System.getProperty("transfer.upload.path",
                System.getProperty("user.dir")
                        + java.io.File.separator + "src"
                        + java.io.File.separator + "test"
                        + java.io.File.separator + "resources"
                        + java.io.File.separator + "Test_Data"
                        + java.io.File.separator + "Cover_Letter.pdf");

        // ── Click "Upload Document" on the summary card ───────────────────────────────
        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iTransferSupportingDocUploadBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iTransferSupportingDocUploadBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iTransferSupportingDocUploadBtn"), null);

        // ── Wait for dialog to open ───────────────────────────────────────────────────
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iTransferDocTypeDropdown"), null);

        // ── Select document type ──────────────────────────────────────────────────────
        iAction("LIST", "XPATH", ObjReader.getLocator("iTransferDocTypeDropdown"), "Companies Registrations Office (Company Printout)");
        log.info("[TRANSFER] Document type selected: " + "Companies Registrations Office (Company Printout)");

        // ── Attach PDF ────────────────────────────────────────────────────────────────
        iAction("UPLOADFILE", "XPATH", ObjReader.getLocator("iTransferFileUploadInput"), iFilePath);

        // ── Confirm upload ────────────────────────────────────────────────────────────
        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iTransferDialogUploadDocBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iTransferDialogUploadDocBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iTransferDialogUploadDocBtn"), null);

        // ── Wait for dialog to close ──────────────────────────────────────────────────
        iAction("WAITINVISIBLE", "XPATH", "//app-supporting-doc-upload-transfer-application-popup", null);


        log.info("Individual transfer created: " + iTransferType + " → " + iTransfereeHerd);
    }

    // ── Agent logs in as TRANSFEREE ──────────────────────────────────────────────
    @When("the agent logs in as transferee agent {string}")
    public void theAgentLogsInAsTransfereeAgent(String pAgentUsername)
    {
        log.info("[STEP] Agent logs in as transferee: " + pAgentUsername);
        // Exit and logout from Individual/ETF session
        iAction("CLICK", "XPATH", ObjReader.getLocator("iExitBISSLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutBtn"), null);

        // Agent login
        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pAgentUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // Navigate to BISS → My Clients → Transfers
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
        iAction("CLICK", "XPATH",
                "//div[contains(@class,'mat-tab-label')]//span[normalize-space()='Transfers'] | //a[normalize-space()='Transfers']", null);
        log.info("Agent logged in as transferee: " + pAgentUsername);
    }

    // ── Agent completes same-agent transferee acceptance ─────────────────────────
    // Same as TC_01_ENTS transferee flow but as a standalone step for cross-user scenarios
    @And("the agent completes the same agent transferee acceptance flow")
    public void theAgentCompletesTheSameAgentTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] Agent completes transferee acceptance");
        Map<String, String> iData = pDataTable.asMap(String.class, String.class);
        String iHerd = iData.get("transfereeHerd").trim();
        String iNotes = iData.get("notes").trim();

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
        iAction("CLICK", "XPATH",
                "//tr[contains(.,'" + iHerd + "')]//button[contains(text(),'View')] | //button[contains(text(),'View')]", null);

        String iKey = System.getProperty("lastCapturedTransferKey", "");
        Assertions.assertFalse(iKey.isEmpty(), "Transfer key must be captured first.");
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iKey);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferSubmitToDAFMBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTandCCheckbox"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSubmitBtn"), null);
        log.info("Transferee acceptance completed for: " + iHerd);
    }

    private boolean isVisible(By pLocator, int pSeconds) {
        try { new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(pLocator)); return true; }
        catch (Exception e) { return false; }
    }
}
