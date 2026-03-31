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

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
        log.info("[STEP] Individual logs in as transferor: " + pUsername);
        // Navigate to Individual login page
        try { iAction("CLICK", "XPATH", ObjReader.getLocator("iIndividualLoginLink"), null); }
        catch (Exception e) { log.info("Already on login page."); }

        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // OTP
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // Dismiss personal details dialog if present
        try { if (isVisible(By.xpath(ObjReader.getLocator("iPersonalDetailsCloseBtn")), 5))
            iAction("CLICK", "XPATH", ObjReader.getLocator("iPersonalDetailsCloseBtn"), null); }
        catch (Exception ignored) {}

        // Navigate to BISS → Transfers
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersTabDirect"), null);
        log.info("Individual logged in as transferor: " + pUsername);
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
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + iTransferType + "')] | //*[@value='" + iTransferType + "']", null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // Add entitlement
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferAddEntitlementBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferEntitlementAmountField"), iEntitlements);
        if (iHasLeaseYear) {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearDropdown"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferLeaseYearFirstOption"), null);
        }
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogAddBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // Notes
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);

        // Upload CRO document (different from agent's signature doc)
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferUploadDocBtn"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iTransferDocTypeDropdown"),
                "Companies Registrations Office (Company Printout)");
        String iFilePath = System.getProperty("user.dir") + "/src/test/resources/Test_Data/sample_upload.pdf";
        iAction("UPLOADFILE", "XPATH", ObjReader.getLocator("iTransferFileUploadInput"), iFilePath);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogUploadDocBtn"), null);

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