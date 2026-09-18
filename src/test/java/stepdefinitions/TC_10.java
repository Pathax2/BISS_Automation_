// ===================================================================================================================================
// File          : TC_10.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_10 - BISSAGL-20695
//                 Verifies an agent can create a new client record from the No Herd Number tab
//                 and that a reference number is generated correctly for the created client.
//
//                 Playwright migration notes:
//                   - The local isVisible(By, seconds) helper used WebDriverWait; it now uses
//                     UiHelpers.isVisible(Locator, seconds) on the FIRST match - same wait, returns false on timeout.
//                   - Selenium imports (By, WebDriverWait, ExpectedConditions) removed.
//                   - The step "the agent switches to the {string} tab on the My Clients page" was REMOVED from this
//                     file: TC_06.java already defines "the agent switches to the {string} tab on the My Client(s)
//                     page", and "Client(s)" is Cucumber optional text, so it matches BOTH "My Client page" and
//                     "My Clients page". Keeping both would make Cucumber fail with a duplicate step definition.
//                     TC_06.java clicks the same Angular Material tab header, so TC_10.feature is unaffected.
//                   - Every other step goes through iAction, which is already Playwright-based - logic unchanged.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// Updated       : 18-09-2026 - Migrated to Playwright; duplicate My Clients tab step removed (now in TC_06.java)
// ===================================================================================================================================

package stepdefinitions;

import com.microsoft.playwright.Locator;
import commonFunctions.UiHelpers;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utilities.ObjReader;

import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;

public class TC_10
{
    private static final Logger log = Logger.getLogger(TC_10.class.getName());


    // ***************************************************************************************************************************************************************************************
    // Step          : Given the agent is on the BISS Agent Home Screen
    // Description   : Confirms the BISS home screen is active after Background navigation
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Given("the agent is on the BISS Agent Home Screen")
    public void theAgentIsOnTheBISSAgentHomeScreen()
    {
        log.info("[STEP] Given the agent is on the BISS Agent Home Screen");

        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"),
                "Basic Income Support for Sustainability");
        log.info("BISS Agent Home Screen confirmed.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent opens the Create Client form
    // Description   : Clicks the Create Client button inside the No Herd Number tab panel.
    //                 Scoped to biss-no-herd-no-client to avoid matching the dialog footer button.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent opens the Create Client form")
    public void theAgentOpensTheCreateClientForm()
    {
        log.info("[STEP] And the agent opens the Create Client form");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iNoHerdCreateClientBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iNoHerdCreateClientBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iNoHerdCreateClientBtn"), null);
        log.info("Create Client form opened.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent fills in the Create Client form with the following details
    // Description   : Reads all form field key-value pairs from a vertical DataTable.
    //                 Each key maps to a dedicated locator in ObjectRepository.
    //                 County is a mat-select - uses LIST action.
    //                 All other fields are plain text inputs - uses TEXTBOX action.
    // Parameters    : pDataTable - two-column DataTable: | fieldName | value |
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent fills in the Create Client form with the following details")
    public void theAgentFillsInTheCreateClientFormWithTheFollowingDetails(DataTable pDataTable)
    {
        log.info("[STEP] And the agent fills in the Create Client form with the following details");

        Map<String, String> iFormData = pDataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> iEntry : iFormData.entrySet())
        {
            String iFieldName  = iEntry.getKey().trim();
            String iFieldValue = iEntry.getValue().trim();

            fillCreateClientField(iFieldName, iFieldValue);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent enters {string} in the {string} form field
    // Description   : Enters a value into a single named Create Client form field.
    //                 pFieldName must match a key handled by fillCreateClientField - same locator map as the
    //                 DataTable step above. Used for standalone single-field interactions.
    // Parameters    : pValue     - text to enter
    //                 pFieldName - form field key matching ObjectRepository locator
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - shared field mapping extracted into fillCreateClientField)
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters {string} in the {string} form field")
    public void theAgentEntersInTheFormField(String pValue, String pFieldName)
    {
        log.info("[STEP] And the agent enters '" + pValue + "' in the '" + pFieldName + "' form field");

        fillCreateClientField(pFieldName.trim(), pValue);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent ticks the "BISS" reference type checkbox
    // Description   : Clicks the named option inside the mat-selection-list referenceTypes.
    //                 Uses %s substitution on iCreateClientRefTypeOption locator.
    //                 Clicks the mdc-list-item__primary-text span - the native checkbox input
    //                 is not directly clickable due to the ripple overlay.
    // Parameters    : pReferenceType - visible label e.g. "BISS", "NRCISYF", "TRANSFERS"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent ticks the {string} reference type checkbox")
    public void theAgentTicksTheReferenceTypeCheckbox(String pReferenceType)
    {
        log.info("[STEP] And the agent ticks the '" + pReferenceType + "' reference type checkbox");

        String iOptionXpath = String.format(ObjReader.getLocator("iCreateClientRefTypeOption"), pReferenceType);

        iAction("WAITVISIBLE",   "XPATH", iOptionXpath, null);
        iAction("WAITCLICKABLE", "XPATH", iOptionXpath, null);
        iAction("CLICK",         "XPATH", iOptionXpath, null);
        log.info("Ticked reference type: " + pReferenceType);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent submits the Create Client form
    // Description   : Clicks the Create Client submit button in the dialog footer.
    //                 Scoped via iCreateClientSubmitBtn to buttons-container - avoids
    //                 matching the tab-level Create Client button with the same label.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent submits the Create Client form")
    public void theAgentSubmitsTheCreateClientForm()
    {
        log.info("[STEP] Then the agent submits the Create Client form");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iCreateClientSubmitBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iCreateClientSubmitBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iCreateClientSubmitBtn"), null);
        log.info("Create Client form submitted.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent completes the post creation dialog flow
    // Description   : Handles the post-submission dialog button sequence:
    //                 "I understand" -> "Edit" -> "Close"
    //                 Each button is soft-checked with isVisible before clicking -
    //                 not all buttons appear in every environment or flow variant.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - UiHelpers.isVisible)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent completes the post creation dialog flow")
    public void theAgentCompletesThePostCreationDialogFlow()
    {
        log.info("[STEP] Then the agent completes the post creation dialog flow");

        clickIfPresent("iCreateClientIUnderstandBtn", "I understand");
        clickIfPresent("iCreateClientEditBtn",        "Edit");
        clickIfPresent("iCreateClientCloseBtn",       "Close");

        log.info("Post creation dialog flow completed.");
    }


    // ===================================================================================================================================
    //  PRIVATE HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : fillCreateClientField
    // Description   : Maps a Create Client form field name to its Object Repository key and fills it.
    //                 County is a mat-select (LIST action); every other field is a plain text input (TEXTBOX).
    //                 An unknown field name is logged as a warning and skipped, exactly as before.
    // Parameters    : pFieldName  (String) - field key from the feature file, e.g. name, add1, county
    //                 pFieldValue (String) - value to enter
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026 (extracted from the two identical Selenium switch blocks)
    // ***************************************************************************************************************************************************************************************
    private static void fillCreateClientField(String pFieldName, String pFieldValue)
    {
        switch (pFieldName)
        {
            case "name":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientName"), pFieldValue);
                break;
            case "add1":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientAdd1"), pFieldValue);
                break;
            case "add2":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientAdd2"), pFieldValue);
                break;
            case "add3":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientAdd3"), pFieldValue);
                break;
            case "county":
                iAction("LIST",    "XPATH", ObjReader.getLocator("iCreateClientCounty"), pFieldValue);
                break;
            case "eircode":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientEircode"), pFieldValue);
                break;
            case "contactNumber":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientContactNumber"), pFieldValue);
                break;
            case "herdNumber":
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iCreateClientHerdNumber"), pFieldValue);
                break;
            default:
                log.warning("Unknown form field key '" + pFieldName + "' - no locator mapped. Skipping.");
                return;
        }

        log.info("Filled field '" + pFieldName + "' with: " + pFieldValue);
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : clickIfPresent
    // Description   : Clicks a dialog button only when it appears within 5 seconds; logs and skips otherwise.
    //                 Replacement for the Selenium isVisible(By, 5) guards in the post creation dialog flow.
    // Parameters    : pLocatorKey (String) - ObjectRepository key of the button
    //                 pLabel      (String) - button label, for the log line
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static void clickIfPresent(String pLocatorKey, String pLabel)
    {
        if (isVisible(ObjReader.getLocator(pLocatorKey), 5))
        {
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator(pLocatorKey), null);
            iAction("CLICK",         "XPATH", ObjReader.getLocator(pLocatorKey), null);
            log.info("Clicked '" + pLabel + "' button.");
        }
        else
        {
            log.info("'" + pLabel + "' button not present - skipping.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : xp
    // Description   : Builds a Playwright Locator from an XPath string (the framework adds the "xpath=" prefix).
    // Parameters    : pXpath (String) - XPath expression
    // Returns       : Locator
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static Locator xp(String pXpath)
    {
        return UiHelpers.byXpath(pXpath);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : isVisible
    // Description   : Replacement for the Selenium WebDriverWait + visibilityOfElementLocated helper.
    //                 Waits up to pSeconds for the FIRST match to become visible; returns false on timeout.
    // Parameters    : pXpath   (String) - XPath expression
    //                 pSeconds (int)    - how long to wait
    // Returns       : boolean - true when visible within the time limit
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static boolean isVisible(String pXpath, int pSeconds)
    {
        return UiHelpers.isVisible(xp(pXpath).first(), pSeconds);
    }
}
