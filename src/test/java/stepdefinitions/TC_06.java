// ===================================================================================================================================
// File          : TC_06.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_06 — Regression Suite for Bugs and Features in BISS - 1.
//
//                 Covers two active scenarios:
//
//                   AT-TC-01 : Correspondence upload flow
//                                - Login → My Clients → herd search → Correspondence tab
//                                - Document type selection from dropdown
//                                - File upload and submit
//
//                   AT-TC-18 : View Correspondence hyperlink verification
//                                - Login → My Clients → Transfers tab
//                                - Search by transfer herd number
//                                - Click View link → click View Correspondence link
//
//                 Note: TC_06 has no shared Background — each scenario performs its own
//                 full login because the herds and navigation paths differ.
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  — all UI interactions
//                   ObjReader.getLocator("keyName")                   — all locator lookups
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                          → TC_01_Login.java
//                   "the agent logs into the application..."                       → TC_01_Login.java
//                   "the agent opens the BISS application"                         → TC_01_Login.java
//                   "the agent should land on the BISS Home page"                  → TC_01_Login.java
//                   "the agent navigates to the Home and My Clients..."            → TC_04.java
//                   "the agent searches for herd number {string}"                  → TC_05.java
//                   "the agent clicks on the row for client {string}"              → TC_05.java
//                   "the agent clicks on the View Dashboard button"                → TC_05.java
//                   "the agent switches to the {string} tab on the My Clients page"→ TC_05.java
//                   "the agent clicks on the {string} stepper button"              → TC_03.java
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjReader;

import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_06
{
    private static final Logger log = Logger.getLogger(TC_06.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates to the {string} tab on the Side Navigation bar
    // Description   : Clicks the named tab in the left-side application navigation bar
    //                 e.g. "Correspondence", "Land Details", "Applications / Payments"
    // Parameters    : pTab (String) - visible tab label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent navigates to the {string} tab on the Side Navigation bar")
    public void theAgentNavigatesToTabOnSideNavBar(String pTab)
    {
        iAction("CLICK", "XPATH",
                "//a[normalize-space()='" + pTab.trim() + "']"
                        + " | //span[normalize-space()='" + pTab.trim() + "']"
                        + " | //li[normalize-space()='" + pTab.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects {string} from the {string} dropdown
    // Description   : Selects the given document type from the named dropdown in the
    //                 Correspondence upload form
    // Parameters    : pValue      (String) - option text e.g. "Commonage Evidence"
    //                 pDropdownId (String) - dropdown control id / formcontrolname e.g. "doc-type"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} from the {string} dropdown")
    public void theAgentSelectsFromDropdown(String pValue, String pDropdownId)
    {
        iAction("LIST", "XPATH",
                "//*[@id='" + pDropdownId.trim() + "']"
                        + " | //*[@formcontrolname='" + pDropdownId.trim() + "']",
                pValue.trim());
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent uploads a document in Correspondence
    // Description   : Interacts with the file upload control in the Correspondence section
    //                 to attach a document. File path is resolved from test data.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent uploads a document in Correspondence")
    public void theAgentUploadsADocumentInCorrespondence()
    {
        String iFilePath = System.getProperty("user.dir")
                + java.io.File.separator + "src" + java.io.File.separator
                + "test" + java.io.File.separator + "resources"
                + java.io.File.separator + "Test_Data"
                + java.io.File.separator + "sample_upload.pdf";
        iAction("UPLOADFILE", "XPATH", ObjReader.getLocator("iCorrespondenceFileUpload"), iFilePath);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent searches for a transfer herd number {string}
    // Description   : Enters the given herd number into the Transfers tab search field.
    //                 Separate step from the My Clients herd search as the Transfers
    //                 search field may differ in locator.
    // Parameters    : pHerd (String) - transfer herd number e.g. "B1410500"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent searches for a transfer herd number {string}")
    public void theAgentSearchesForTransferHerdNumber(String pHerd)
    {
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), pHerd);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the View link for the searched herd
    // Description   : Clicks the View link in the Transfers tab results row for
    //                 the herd returned by the search
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clicks on the View link for the searched herd")
    public void theAgentClicksOnViewLinkForSearchedHerd()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the View Correspondence link in the dashboard
    // Description   : Clicks the View Correspondence hyperlink visible on the farmer dashboard
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the View Correspondence link in the dashboard")
    public void theAgentClicksOnViewCorrespondenceLink()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewCorrespondenceLink"), null);
    }
}