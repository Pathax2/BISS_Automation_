// ===================================================================================================================================
// File          : TC_11.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_11 — BISSAGL-21062 Shift Document Retrieval Logic
//                 to Document Service.
//
//                 Covers two scenarios:
//
//                   AT-TC-00 : Agent-side precondition
//                                - Login → My Clients → Submitted filter → pick herd
//
//                   Main scenario : Staff sends 5 correspondence letters then agent verifies
//                                   they appear in Recent Correspondence on the dashboard
//                                - Staff login → herd search → scheme year selection
//                                - Application table → View link → Correspondence tab
//                                - Query Letters → select doc type → enter heading/body
//                                - Preview → Submit → Confirm (repeated x5)
//                                - Agent login → dashboard → Recent Correspondence frame
//                                - My Clients → submitted herd → Correspondence tab
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
//                   "the agent applies the {string} quick filter"                  → TC_07.java
//                   "the agent picks a herd from the list"                         → TC_07.java
//                   "the agent clicks on the View Dashboard button"                → TC_05.java
//                   "the agent navigates to the {string} tab on Side Nav bar"      → TC_06.java
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjReader;

import java.util.List;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_11
{
    private static final Logger log = Logger.getLogger(TC_11.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user is on the staff login page
    // Description   : Verifies the staff (internal) login page has loaded
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Given("the staff user is on the staff login page")
    public void theStaffUserIsOnStaffLoginPage()
    {
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iStaffLoginPageIndicator"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user logs in with username {string}
    // Description   : Enters the given username into the staff login username field
    // Parameters    : pUsername (String) - staff username e.g. "agr4442"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the staff user logs in with username {string}")
    public void theStaffUserLogsInWithUsername(String pUsername)
    {
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernameTxtbox"), pUsername);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user selects the data protection checkbox
    // Description   : Ticks the data protection agreement checkbox on the staff login page
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user selects the data protection checkbox")
    public void theStaffUserSelectsDataProtectionCheckbox()
    {
        iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iDataProtectionCheckbox"), "CHECK");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user opens the {string} application
    // Description   : Clicks the named application tile on the staff portal home screen
    // Parameters    : pApp (String) - application tile label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user opens the {string} application")
    public void theStaffUserOpensApplication(String pApp)
    {
        iAction("CLICK", "XPATH",
                "//*[normalize-space()='" + pApp.trim() + "']"
                        + " | //*[contains(@class,'app-tile') and .//*[normalize-space()='" + pApp.trim() + "']]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user searches for a herd from row {int} with scheme year {string}
    // Description   : Selects the scheme year from the dropdown and clicks Search,
    //                 then uses the herd from the given row number as the search fixture
    // Parameters    : pRow        (int)    - 1-based row index
    //                 pSchemeYear (String) - year e.g. "2025"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user searches for a herd from row {int} with scheme year {string}")
    public void theStaffUserSearchesForHerdWithSchemeYear(int pRow, String pSchemeYear)
    {
        iAction("LIST", "XPATH",
                "//*[@id='schemeYear'] | //*[@formcontrolname='schemeYear']",
                pSchemeYear.trim());
        iAction("CLICK", "XPATH",
                "//button[normalize-space()='Search']", null);
        iAction("WAITVISIBLE", "XPATH",
                "(//table[contains(@id,'application-search-table')]//tr | //mat-row)[1]", null);
        String iHerdXpath = "(//table[contains(@id,'application-search-table')]//tr["
                + pRow + "]//td)[1]";
        String iHerd = iAction("GETTEXT", "XPATH", iHerdXpath, null);
        log.info("[TC_11] Staff using herd from row " + pRow + ": " + iHerd);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user clicks on the {string} link in the application table at position {int}
    // Description   : Clicks the nth occurrence of the named link in the application search results table
    // Parameters    : pLinkText (String) - link text e.g. "View"
    //                 pIndex    (int)    - 1-based position
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user clicks on the {string} link in the application table at position {int}")
    public void theStaffUserClicksOnLinkAtPosition(String pLinkText, int pIndex)
    {
        iAction("CLICK", "XPATH",
                "(//*[contains(@id,'application-search-table')]//a[normalize-space()='"
                        + pLinkText.trim() + "'])[" + pIndex + "]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user navigates to {string} in the side navigation bar
    // Description   : Clicks the named link in the staff-side left navigation bar
    // Parameters    : pLink (String) - link label e.g. "Correspondence"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the staff user navigates to {string} in the side navigation bar")
    public void theStaffUserNavigatesInSideNavBar(String pLink)
    {
        iAction("CLICK", "XPATH",
                "//a[normalize-space()='" + pLink.trim() + "']"
                        + " | //span[normalize-space()='" + pLink.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user switches to the {string} tab in Correspondence
    // Description   : Clicks the named tab in the Correspondence section header
    // Parameters    : pTab (String) - tab label e.g. "Query Letter"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user switches to the {string} tab in Correspondence")
    public void theStaffUserSwitchesToTabInCorrespondence(String pTab)
    {
        iAction("CLICK", "XPATH",
                "//button[normalize-space()='" + pTab.trim() + "']"
                        + " | //a[normalize-space()='" + pTab.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user checks for any inspection error
    // Description   : Checks whether an inspection-related error banner is present and
    //                 logs a warning if found, so the query letter flow can continue regardless
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user checks for any inspection error")
    public void theStaffUserChecksForAnyInspectionError()
    {
        List<org.openqa.selenium.WebElement> iErrors =
                getDriver().findElements(
                        org.openqa.selenium.By.xpath(
                                "//*[contains(@class,'error') and contains(normalize-space(),'inspection')]"));
        if (!iErrors.isEmpty() && iErrors.get(0).isDisplayed())
        {
            log.warning("[TC_11] Inspection error banner detected — continuing with query letter flow.");
        }
        else
        {
            log.info("[TC_11] No inspection error banner present.");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user selects {string} from the {string} query letter dropdown
    // Description   : Selects the given document type from the named dropdown in the Query Letters form
    // Parameters    : pValue      (String) - option to select e.g. "BISS Free Text Letter"
    //                 pDropdownId (String) - dropdown control id / formcontrolname e.g. "docType"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user selects {string} from the {string} query letter dropdown")
    public void theStaffUserSelectsFromQueryLetterDropdown(String pValue, String pDropdownId)
    {
        iAction("LIST", "XPATH",
                "//*[@id='" + pDropdownId.trim() + "']"
                        + " | //*[@formcontrolname='" + pDropdownId.trim() + "']",
                pValue.trim());
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user types {string} in the {string} query letter text area
    // Description   : Types text into the named textarea in the Query Letters form
    // Parameters    : pText      (String) - text to enter e.g. "Test Heading"
    //                 pAreaLabel (String) - visible label of the textarea e.g. "Enter letter heading"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user types {string} in the {string} query letter text area")
    public void theStaffUserTypesInQueryLetterTextArea(String pText, String pAreaLabel)
    {
        iAction("TEXTBOX", "XPATH",
                "//label[normalize-space()='" + pAreaLabel.trim() + "']/following-sibling::textarea"
                        + " | //textarea[@placeholder='" + pAreaLabel.trim() + "']"
                        + " | //textarea[contains(@aria-label,'" + pAreaLabel.trim() + "')]",
                pText);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user clicks the {string} button
    // Description   : Clicks the named button on the staff Query Letters form
    //                 e.g. "Preview", "Submit"
    // Parameters    : pButton (String) - button label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user clicks the {string} button")
    public void theStaffUserClicksButton(String pButton)
    {
        iAction("CLICK", "XPATH",
                "//button[normalize-space()='" + pButton.trim() + "']", null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the staff user confirms by clicking the {string} button
    // Description   : Clicks the confirmation button in the submit dialog
    //                 e.g. "Yes, Confirm"
    // Parameters    : pButton (String) - confirmation button label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the staff user confirms by clicking the {string} button")
    public void theStaffUserConfirmsByClickingButton(String pButton)
    {
        iAction("CLICK", "XPATH",
                "//button[normalize-space()='" + pButton.trim() + "']", null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent can see the {string} frame on the Agent Dashboard
    // Description   : Verifies the named section/frame heading is visible on the Agent Dashboard
    // Parameters    : pFrame (String) - frame heading text e.g. "Recent Correspondence"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent can see the {string} frame on the Agent Dashboard")
    public void theAgentCanSeeFrameOnAgentDashboard(String pFrame)
    {
        iAction("VERIFYELEMENT", "XPATH",
                "//*[normalize-space()='" + pFrame.trim() + "']"
                        + " | //*[contains(normalize-space(),'" + pFrame.trim() + "')]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the latest 5 letters are visible in Recent Correspondence
    // Description   : Asserts that at least 5 letter entries are present in the Recent
    //                 Correspondence frame on the Agent Dashboard
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the latest 5 letters are visible in Recent Correspondence")
    public void theAgentVerifiesLatest5LettersInRecentCorrespondence()
    {
        List<org.openqa.selenium.WebElement> iLetters =
                getDriver().findElements(
                        org.openqa.selenium.By.xpath(
                                "//*[contains(@class,'correspondence')]//tr"
                                        + " | //*[contains(@class,'recent-correspondence')]//li"));

        if (iLetters.size() < 5)
        {
            throw new AssertionError(
                    "Expected at least 5 letters in Recent Correspondence but found: " + iLetters.size());
        }
        log.info("[TC_11] Verified " + iLetters.size() + " letter entries in Recent Correspondence.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects the submitted herd from row {int}
    // Description   : Reads the herd number from the nth row in the My Clients submitted
    //                 list and searches for it
    // Parameters    : pRow (int) - 1-based row index
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent selects the submitted herd from row {int}")
    public void theAgentSelectsSubmittedHerdFromRow(int pRow)
    {
        String iHerd = iAction("GETTEXT", "XPATH",
                "(//mat-row | //tbody/tr)[" + pRow + "]"
                        + "//*[contains(@class,'herd') or @data-column='herd']",
                null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iHerdSearchField"), iHerd.trim());
        iAction("CLICK", "XPATH",
                "//mat-row[.//*[normalize-space()='" + iHerd.trim() + "']]"
                        + " | //tr[.//*[normalize-space()='" + iHerd.trim() + "']]",
                null);
    }
}