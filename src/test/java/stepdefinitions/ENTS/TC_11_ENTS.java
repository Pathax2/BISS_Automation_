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

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import utilities.ObjReader;
import java.util.Map;
import java.util.logging.Logger;
import static commonFunctions.CommonFunctions.iAction;

public class TC_11_ENTS
{
    private static final Logger log = Logger.getLogger(TC_11_ENTS.class.getName());
    private static String iPartnerUsername = "";

    @Given("the ETF partner logs in as transferor {string}")
    public void theETFPartnerLogsInAsTransferor(String pPartnerUsername)
    {
        log.info("[STEP] ETF partner logs in as transferor: " + pPartnerUsername);
        iPartnerUsername = pPartnerUsername;
        try { iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginLink"), null); }
        catch (Exception ignored) {}
        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPartnerUsernameField"), pPartnerUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iMSAuthOTPField"), "111111");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPartnerLoginBtn"), null);
        log.info("ETF partner logged in as transferor: " + pPartnerUsername);
    }

    @When("the ETF partner creates a transfer with manual entitlements")
    public void theETFPartnerCreatesATransferWithManualEntitlements(DataTable pDataTable)
    {
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
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + iTransferType + "')] | //*[@value='" + iTransferType + "']", null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        // MANUAL entitlement — different from standard flow
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferAddManualEntitlementBtn"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iTransferEntitlementTypeDropdown"), iEntType);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferEntitlementAmountField"), iEntitlements);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNetUVField"), iNetUV);
        if (iHasLeaseYear) {
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
    public void theETFPartnerLogsBackInAsTransferor()
    {
        log.info("[STEP] ETF partner logs back in as transferor");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iExitBISSLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutBtn"), null);
        theETFPartnerLogsInAsTransferor(iPartnerUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);
    }
}