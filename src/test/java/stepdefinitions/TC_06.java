// ===================================================================================================================================
// File          : TC_06.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_06 — Regression Suite for Bugs and Features in BISS - 1.
//
//                 Active Scenario:
//
//                   AT-TC-01 : Correspondence upload + View Correspondence verification
//                                - Login → My Clients → herd search → Correspondence tab
//                                - Document type selection from dropdown
//                                - File upload and submit via stepper
//                                - Navigate to Transfers tab → herd search → View → View Correspondence
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                            → TC_03.java
//                   "the agent logs into the application..."                         → TC_03.java
//                   "the agent opens the {string} application"                       → TC_03.java
//                   "the agent should land on the BISS Home page"                    → TC_03.java
//                   "the agent navigates to the {string} and {string} Left Menu Link"→ TC_03.java
//                   "the agent opens a farmer dashboard using herd data"             → TC_03.java
//                   "the farmer dashboard should be displayed"                       → TC_03.java
//                   "the agent navigates through the farmer side navigation tabs"    → TC_03.java
//                   "each requested side navigation tab should open successfully"    → TC_03.java
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


    // ===================================================================================================================================
    //  STEPPER BUTTON — Generic click for any stepper/wizard button by visible text
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the {string} stepper button
    // Description   : Clicks a button in the application wizard stepper bar by its visible text.
    //                 Handles both standard buttons and mat-raised-buttons used in the BISS wizard.
    //                 The text match is normalised to handle leading/trailing whitespace in the
    //                 feature file (e.g. " Upload a document" has a leading space).
    // Parameters    : pButtonText (String) - visible button label e.g. "Upload a document", "Upload"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clicks on the {string} stepper button")
    public void theAgentClicksOnTheStepperButton(String pButtonText)
    {
        log.info("[STEP] Then the agent clicks on the stepper button: " + pButtonText);

        // Normalise the button text — the feature file sometimes has leading/trailing spaces
        // (e.g. " Upload a document") due to formatting in the Gherkin table
        String iCleanText = pButtonText.trim();

        // Build a robust XPath that catches both <button> and <a> elements used as steppers,
        // using normalize-space() to handle any DOM whitespace inconsistencies
        String iXpath = "//button[normalize-space()='" + iCleanText + "']"
                + " | //a[normalize-space()='" + iCleanText + "']"
                + " | //span[normalize-space()='" + iCleanText + "']/ancestor::button[1]";

        iAction("CLICK", "XPATH", iXpath, null);
    }


    // ===================================================================================================================================
    //  CORRESPONDENCE — DOCUMENT TYPE SELECTION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects {string} from the {string} dropdown
    // Description   : Selects the given value from the named dropdown in the Correspondence
    //                 upload form. The dropdown is located by its id or formcontrolname attribute.
    // Parameters    : pValue      (String) - option text e.g. "Commonage Evidence"
    //                 pDropdownId (String) - dropdown control id / formcontrolname e.g. "doc-type"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} from the {string} dropdown")
    public void theAgentSelectsFromDropdown(String pValue, String pDropdownId)
    {
        log.info("[STEP] And the agent selects '" + pValue + "' from the '" + pDropdownId + "' dropdown");

        // Build a combined XPath that matches by either id or formcontrolname —
        // the BISS portal uses both depending on which Angular component renders the dropdown
        iAction("LIST", "XPATH",
                "//*[@id='" + pDropdownId.trim() + "']"
                        + " | //*[@formcontrolname='" + pDropdownId.trim() + "']",
                pValue.trim());
    }


    // ===================================================================================================================================
    //  CORRESPONDENCE — FILE UPLOAD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent uploads a document in Correspondence
    // Description   : Sends a file path to the hidden file upload input on the Correspondence page.
    //                 The file path is resolved relative to the project root — defaults to a sample
    //                 PDF bundled in the test resources. The UPLOADFILE action bypasses the OS file
    //                 picker dialog which Selenium cannot interact with.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent uploads a document in Correspondence")
    public void theAgentUploadsADocumentInCorrespondence()
    {
        log.info("[STEP] And the agent uploads a document in Correspondence");

        // Build the absolute path to the sample document bundled in the test resources.
        // Falls back to this default path — override via system property if needed for CI.
        String iFilePath = System.getProperty("correspondence.upload.path",
                System.getProperty("user.dir")
                        + java.io.File.separator + "src"
                        + java.io.File.separator + "test"
                        + java.io.File.separator + "resources"
                        + java.io.File.separator + "Test_Data"
                        + java.io.File.separator + "sample_upload.pdf");

        // Send the file path directly to the hidden <input type="file"> element —
        // this triggers the same upload handler as the OS file picker without needing
        // to interact with the native dialog
        iAction("UPLOADFILE", "XPATH", ObjReader.getLocator("iCorrespondenceFileUpload"), iFilePath);
        log.info("Document uploaded: " + iFilePath);
    }


    // ===================================================================================================================================
    //  TRANSFERS — HERD SEARCH
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent switches to the {string} tab on the My Clients page
    // Description   : Clicks the named tab header on the My Clients page to switch between
    //                 different client views (e.g. "Transfers", "Applications")
    // Parameters    : pTabName (String) - visible tab label e.g. "Transfers"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent switches to the {string} tab on the My Clients page")
    public void theAgentSwitchesToTheTabOnTheMyClientsPage(String pTabName)
    {
        log.info("[STEP] And the agent switches to the '" + pTabName + "' tab on the My Clients page");

        // The My Clients page uses mat-tab-group — target the tab label by its visible text
        iAction("CLICK", "XPATH",
                "//div[contains(@class,'mat-tab-label')]//span[normalize-space()='" + pTabName.trim() + "']"
                        + " | //a[normalize-space()='" + pTabName.trim() + "']",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent searches for a transfer herd number {string}
    // Description   : Enters the given herd number into the Transfers tab search field.
    //                 This is a separate step from the My Clients herd search (TC_03) because
    //                 the Transfers tab has its own search input with a different locator.
    // Parameters    : pHerd (String) - transfer herd number e.g. "B1410500"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent searches for a transfer herd number {string}")
    public void theAgentSearchesForTransferHerdNumber(String pHerd)
    {
        log.info("[STEP] When the agent searches for a transfer herd number: " + pHerd);

        // Type the herd number into the Transfers-specific search field
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), pHerd);

        // Hit the search button to filter the Transfers table
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
    }


    // ===================================================================================================================================
    //  TRANSFERS — VIEW LINK AND VIEW CORRESPONDENCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the View link for the searched herd
    // Description   : Clicks the View link in the Transfers tab results row for the herd
    //                 returned by the search. Assumes a single matching result row.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clicks on the View link for the searched herd")
    public void theAgentClicksOnViewLinkForSearchedHerd()
    {
        log.info("[STEP] Then the agent clicks on the View link for the searched herd");

        // Click the View hyperlink in the first matching row of the Transfers results table
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the View Correspondence link in the dashboard
    // Description   : Clicks the View Correspondence hyperlink visible on the farmer dashboard
    //                 after navigating from the Transfers tab View action.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the View Correspondence link in the dashboard")
    public void theAgentClicksOnViewCorrespondenceLink()
    {
        log.info("[STEP] And the agent clicks on the View Correspondence link in the dashboard");

        // Click the View Correspondence hyperlink — this opens the correspondence section
        // for the farmer the agent navigated to via the Transfers tab
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewCorrespondenceLink"), null);
    }
}