// ===================================================================================================================================
// File          : TC_12.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_12 - BISSAGL-21680 Login Page Full Coverage.
//
//                 Covers 14 scenarios:
//
//                   AT-TC-01 : Individual user positive login
//                   AT-TC-02 : Individual user negative login + UI element verification
//                   AT-TC-03 : Agent user positive login
//                   AT-TC-04 : Agent user negative login + UI element verification
//                   AT-TC-05 : Agent Dashboard frames and icon verification
//                   AT-TC-06 : Display My Clients screen
//                   AT-TC-07 : Sort My Clients by Name and Herd Number
//                   AT-TC-08 : Items per page pagination verification
//                   AT-TC-09 : Quick filter buttons
//                   AT-TC-10 : Row accordion - Submitted herd
//                   AT-TC-11 : Row accordion - Not Started herd
//                   AT-TC-12 : Search by herd number and clear
//                   AT-TC-13 : Search by invalid characters - no results
//                   AT-TC-14 : Export to Excel
//
//                 Note: No shared Background is used - each scenario manages its own
//                 login because individual and agent login flows differ.
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  - all UI interactions
//                   ObjReader.getLocator("keyName")                  - all locator lookups
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                          -> TC_01_Login.java
//                   "the agent logs into the application..."                       -> TC_01_Login.java
//                   "the agent opens the BISS application"                         -> TC_01_Login.java
//                   "the agent should land on the BISS Home page"                  -> TC_01_Login.java
//                   "the agent navigates to the Home and My Clients..."            -> TC_04.java
//                   "the agent navigates to the {string} tab"                      -> TC_04.java
//                   "the agent applies the {string} quick filter"                  -> TC_07.java
//                   "the agent searches for herd number {string}"                  -> TC_05.java
//                   "the agent can see the {string} frame on the Agent Dashboard"  -> TC_11.java
//                   "the agent clicks on the {string} farmer dashboard button"     -> TC_03.java
//
//                 Playwright migration notes:
//                   - getDriver().navigate().back()  -> UiHelpers.navigateBack().
//                   - getDriver().findElements(...)  -> countOf(...), an immediate count like Selenium findElements.
//                   - Excel export check: Playwright never downloads into the Windows Downloads folder. Every download is
//                     saved by PlaywrightFactory into report.downloads.path (default Test_Report\\downloads) and the step
//                     waits for it there with UiHelpers.waitForDownloadedFile - this also works inside the container.
//                   - Object Repository keys used here that are NOT in the package ObjectRepository.properties:
//                       iIndividualLoginPageIndicator, iUsernameTxtbox, iPasswordTxtbox, iOTPTxtbox, iLoginBtn,
//                       iLoginErrorMessage, iRowsPerPageDropdown
//                     The package file has iUsernametxtbox, iPasswordtxtbox, iOPTtxtbox, iLoginbtn and iRowsPerPageTrigger
//                     (different spelling). Add the keys above, or copy your Selenium repository with
//                     tools/copy-from-selenium.ps1 -WithRepository, before running TC_12.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// Updated       : 18-09-2026 - Migrated to Playwright (navigateBack, countOf, downloads read from report.downloads.path)
// ===================================================================================================================================

package stepdefinitions;

import com.microsoft.playwright.Locator;
import commonFunctions.UiHelpers;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ConfigManager;
import utilities.ObjReader;

import java.nio.file.Path;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;

public class TC_12
{
    private static final Logger log = Logger.getLogger(TC_12.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the individual user is on the individual login page
    // Description   : Verifies the Individual (non-agent) login page has loaded
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Given("the individual user is on the individual login page")
    public void theIndividualUserIsOnIndividualLoginPage()
    {
        log.info("[STEP] Given the individual user is on the individual login page");

        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iIndividualLoginPageIndicator"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the individual user enters {string} as their username
    // Description   : Types the given username into the Individual login username field
    // Parameters    : pUsername (String) - individual username e.g. "MERVSTEP1"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @When("the individual user enters {string} as their username")
    public void theIndividualUserEntersAsTheirUsername(String pUsername)
    {
        log.info("[STEP] When the individual user enters '" + pUsername + "' as their username");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernameTxtbox"), pUsername);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks the Login button
    // Description   : Clicks the primary Login / Continue button on the login page
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks the Login button")
    public void theAgentClicksTheLoginButton()
    {
        log.info("[STEP] And the agent clicks the Login button");

        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters the password
    // Description   : Enters the password from test data into the password field.
    //                 TD:Password is resolved from TestData.xlsx, or from the TD_PASSWORD environment variable in CI.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters the password")
    public void theAgentEntersThePassword()
    {
        log.info("[STEP] And the agent enters the password");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordTxtbox"), "TD:Password");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent completes OTP verification
    // Description   : Enters the OTP from test data and clicks Login to complete authentication
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent completes OTP verification")
    public void theAgentCompletesOTPVerification()
    {
        log.info("[STEP] Then the agent completes OTP verification");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOTPTxtbox"), "TD:OTP");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginBtn"),  null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the login error message is displayed
    // Description   : Verifies an error/validation message is visible after a failed login attempt
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the login error message is displayed")
    public void theLoginErrorMessageIsDisplayed()
    {
        log.info("[STEP] Then the login error message is displayed");

        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iLoginErrorMessage"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the user clicks the Cancel button
    // Description   : Clicks the Cancel button on the login error dialog or login form
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the user clicks the Cancel button")
    public void theUserClicksCancelButton()
    {
        log.info("[STEP] And the user clicks the Cancel button");

        iAction("CLICK", "XPATH", "//button[normalize-space()='Cancel']", null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the user verifies the {string} link is visible
    // Description   : Verifies the named hyperlink is present and visible on the login page
    // Parameters    : pLink (String) - link text e.g. "Privacy Statement", "Need Help", "Forgot Password"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the user verifies the {string} link is visible")
    public void theUserVerifiesLinkIsVisible(String pLink)
    {
        log.info("[STEP] And the user verifies the '" + pLink + "' link is visible");

        iAction("VERIFYELEMENT", "XPATH", "//a[normalize-space()='" + pLink.trim() + "']", null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the password field eye icon is visible
    // Description   : Verifies the password visibility toggle icon is present on the password field
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the password field eye icon is visible")
    public void thePasswordFieldEyeIconIsVisible()
    {
        log.info("[STEP] And the password field eye icon is visible");

        iAction("VERIFYELEMENT", "XPATH",
                "//*[contains(@class,'eye') or contains(@class,'password-toggle') or @data-testid='toggle-password']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the user verifies the {string} element is visible
    // Description   : Verifies a named UI element (button, link, or text) is visible on the page
    // Parameters    : pElement (String) - visible text e.g. "Forgot Password", "Cancel", "Back to Login"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the user verifies the {string} element is visible")
    public void theUserVerifiesElementIsVisible(String pElement)
    {
        log.info("[STEP] And the user verifies the '" + pElement + "' element is visible");

        iAction("VERIFYELEMENT", "XPATH",
                "//*[normalize-space()='" + pElement.trim() + "']"
                        + " | //button[normalize-space()='" + pElement.trim() + "']"
                        + " | //a[normalize-space()='" + pElement.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the user verifies the {string} element with label {string} is visible
    // Description   : Verifies a button or link element located by its data-testid or id attribute
    // Parameters    : pTestId (String) - element test ID e.g. "terms-link", "helpdesk-link"
    //                 pLabel  (String) - descriptive label for logging e.g. "Terms", "Helpdesk"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the user verifies the {string} element with label {string} is visible")
    public void theUserVerifiesElementWithLabelIsVisible(String pTestId, String pLabel)
    {
        iAction("VERIFYELEMENT", "XPATH",
                "//*[@data-testid='" + pTestId.trim() + "']"
                        + " | //*[@id='" + pTestId.trim() + "']"
                        + " | //a[contains(@class,'" + pTestId.trim() + "')]",
                null);
        log.info("[TC_12] Verified element visible: " + pLabel + " [testid=" + pTestId + "]");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters an incorrect username
    // Description   : Types a deliberately invalid username into the login username field
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @When("the agent enters an incorrect username")
    public void theAgentEntersAnIncorrectUsername()
    {
        log.info("[STEP] When the agent enters an incorrect username");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernameTxtbox"), "INVALID_USER_TC12");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters the valid username
    // Description   : Types the runtime-resolved agent username into the login username field
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @When("the agent enters the valid username")
    public void theAgentEntersTheValidUsername()
    {
        log.info("[STEP] When the agent enters the valid username");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernameTxtbox"), "TD:Username");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the {string} icon is present on the Dashboard
    // Description   : Verifies the named icon button is visible on the Agent Dashboard header bar.
    //                 e.g. "contact-us", "help", "profile"
    // Parameters    : pIconId (String) - icon element identifier (data-testid, id, or class)
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the {string} icon is present on the Dashboard")
    public void theAgentVerifiesIconIsPresent(String pIconId)
    {
        log.info("[STEP] And the agent verifies the '" + pIconId + "' icon is present on the Dashboard");

        iAction("VERIFYELEMENT", "XPATH",
                "//*[@data-testid='" + pIconId.trim() + "']"
                        + " | //*[@id='" + pIconId.trim() + "']"
                        + " | //*[contains(@class,'" + pIconId.trim() + "')]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent sorts the {string} column in ascending and descending order
    // Description   : Clicks the named column header twice - first click for ascending,
    //                 second click for descending sort
    // Parameters    : pColumn (String) - column header label e.g. "Name", "Herd Number"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent sorts the {string} column in ascending and descending order")
    public void theAgentSortsColumnInAscendingAndDescendingOrder(String pColumn)
    {
        log.info("[STEP] Then the agent sorts the '" + pColumn + "' column in ascending and descending order");

        String iHeaderXpath = "//th[normalize-space()='" + pColumn.trim() + "']"
                + " | //mat-header-cell[normalize-space()='" + pColumn.trim() + "']";

        iAction("CLICK", "XPATH", iHeaderXpath, null);
        iAction("CLICK", "XPATH", iHeaderXpath, null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies each page size option from the items per page dropdown
    // Description   : Iterates through available page size options (10, 20, 50) in the
    //                 pagination dropdown and verifies the table updates for each selection
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies each page size option from the items per page dropdown")
    public void theAgentVerifiesEachPageSizeOption()
    {
        log.info("[STEP] And the agent verifies each page size option from the items per page dropdown");

        String[] iPageSizes = { "10", "20", "50" };
        for (String iSize : iPageSizes)
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iRowsPerPageDropdown"), iSize);
            log.info("[TC_12] Page size set to: " + iSize);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent expands the accordion for any row using the {string} icon
    // Description   : Clicks the accordion expand icon on the first available My Clients row.
    //                 The icon name matches the Angular Material icon text content.
    // Parameters    : pIconName (String) - icon name e.g. "keyboard_arrow_down"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @When("the agent expands the accordion for any row using the {string} icon")
    public void theAgentExpandsAccordionForAnyRow(String pIconName)
    {
        log.info("[STEP] When the agent expands the accordion for any row using the '" + pIconName + "' icon");

        iAction("CLICK", "XPATH",
                "(//mat-icon[normalize-space()='" + pIconName.trim() + "'])[1]"
                        + " | (//*[contains(@class,'accordion') and contains(@class,'arrow')])[1]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent can see the Payment Details section
    // Description   : Verifies the Payment Details accordion panel is visible after row expansion
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent can see the Payment Details section")
    public void theAgentCanSeePaymentDetailsSection()
    {
        log.info("[STEP] Then the agent can see the Payment Details section");

        iAction("VERIFYELEMENT", "XPATH",
                "//*[contains(normalize-space(),'Payment Details') or contains(@class,'payment-details')]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates back in the browser
    // Description   : Triggers browser back navigation (Playwright page.goBack through UiHelpers)
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - UiHelpers.navigateBack)
    // ***************************************************************************************************************************************************************************************
    @And("the agent navigates back in the browser")
    public void theAgentNavigatesBackInBrowser()
    {
        log.info("[STEP] And the agent navigates back in the browser");

        UiHelpers.navigateBack();
        log.info("[TC_12] Browser navigated back.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clears the herd search field
    // Description   : Clicks the clear (X) icon to reset the My Clients herd search field
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clears the herd search field")
    public void theAgentClearsTheHerdSearchField()
    {
        log.info("[STEP] Then the agent clears the herd search field");

        iAction("CLICK", "XPATH",
                "//*[contains(@class,'clear') or contains(@aria-label,'clear') or @mat-icon-button]"
                        + "[preceding-sibling::input or ancestor::mat-form-field]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : no herd results are shown in the My Clients list
    // Description   : Verifies the My Clients table is empty (no data rows visible) after
    //                 an invalid or unmatched search term is entered
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - countOf replaces findElements)
    // ***************************************************************************************************************************************************************************************
    @Then("no herd results are shown in the My Clients list")
    public void noHerdResultsAreShownInMyClientsList()
    {
        log.info("[STEP] Then no herd results are shown in the My Clients list");

        int iRows = countOf("//mat-row | //tbody/tr[not(contains(@class,'no-data'))]");

        if (iRows > 0)
        {
            throw new AssertionError("Expected no herd rows but found " + iRows + " visible rows.");
        }
        log.info("[TC_12] Verified - no herd rows shown for invalid search.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the Excel file has been downloaded successfully
    // Description   : Verifies that an Excel file (.xlsx or .xls) arrived after clicking Export to Excel.
    //                 Playwright does NOT use the Windows Downloads folder: the browser keeps a download in a
    //                 temporary folder and deletes it when it closes, so the framework copies every download into
    //                 report.downloads.path (default Test_Report\\downloads) as soon as it finishes. The same code
    //                 therefore works on the laptop and inside the Docker container.
    //                 The file is also kept as evidence for the run.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - download folder replaces the Windows Downloads folder)
    // ***************************************************************************************************************************************************************************************
    @Then("the Excel file has been downloaded successfully")
    public void theExcelFileHasBeenDownloadedSuccessfully()
    {
        log.info("[STEP] Then the Excel file has been downloaded successfully");

        Path iFile = UiHelpers.waitForDownloadedFile(".xlsx,.xls", 30);

        if (iFile == null)
        {
            throw new AssertionError("Expected an Excel file (.xlsx / .xls) after Export to Excel, but none arrived within 30 s. "
                    + "Downloads are saved to: "
                    + ConfigManager.getOrDefault("report.downloads.path", "Test_Report/downloads"));
        }

        log.info("[TC_12] Excel export verified - file saved: " + iFile);
    }


    // ===================================================================================================================================
    //  PRIVATE HELPERS (Playwright replacements for the Selenium calls in this class)
    // ===================================================================================================================================

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
    // Function Name : countOf
    // Description   : How many elements match right now - replacement for driver.findElements(...).size().
    //                 Does not wait, exactly like Selenium findElements.
    // Parameters    : pXpath (String) - XPath expression
    // Returns       : int - number of matches
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static int countOf(String pXpath)
    {
        return xp(pXpath).count();
    }
}
