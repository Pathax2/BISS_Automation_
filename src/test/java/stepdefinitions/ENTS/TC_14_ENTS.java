// ===================================================================================================================================
// TC_14_ENTS.java | package stepdefinitions.ENTS
// ETF Authorisation steps (Individual adds ETF auth)
// New steps (2):
//   1. the individual adds ETF authorisation with the following details (DataTable)
//   2. the ETF partner searches for herd {string} and opens it
// Author: Aniket Pathare | Created: 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import utilities.ObjReader;
import java.util.Map;
import java.util.logging.Logger;
import static commonFunctions.CommonFunctions.iAction;

public class TC_14_ENTS
{
    private static final Logger log = Logger.getLogger(TC_14_ENTS.class.getName());

    @And("the individual adds ETF authorisation with the following details")
    public void theIndividualAddsETFAuthorisation(DataTable pDataTable)
    {
        log.info("[STEP] Individual adds ETF authorisation");
        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        iAction("CLICK", "XPATH", ObjReader.getLocator("iAllowEntitlementBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddETFAuthorisationBtn"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iETFAvailableDropdown"), iData.get("etfCode").trim());
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTxorEntsAllowedField"), iData.get("txorEntsAllowed").trim());
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTxeeEntsAllowedField"), iData.get("txeeEntsAllowed").trim());
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddETFAuthorisationConfirmBtn"), null);
        log.info("ETF authorisation added: " + iData.get("etfCode"));
    }

    @And("the ETF partner searches for herd {string} and opens it")
    public void theETFPartnerSearchesForHerdAndOpensIt(String pHerd)
    {
        log.info("[STEP] ETF partner searches for herd: " + pHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), pHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
    }

    @Then("the ETF authorisation should be visible")
    public void theETFAuthorisationShouldBeVisible()
    {
        log.info("[STEP] Verifying ETF authorisation is visible");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iETFAuthorisationRow"), null);
    }
}