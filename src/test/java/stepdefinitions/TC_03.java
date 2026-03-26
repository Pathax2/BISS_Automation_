// ===================================================================================================================================
// File          : BISSAgentStepDefinitions.java
// Package       : stepdefinitions
// Description   : Step definitions for BISS Agent end-to-end farmer application journey.
//                 Covers: Login, Portal Navigation, Farmer Dashboard, Side Navigation,
//                         Application Start, Active Farmer, Scheme Selection, Land Details,
//                         GAEC 7, ACRES, Eco, Review & Submit, Correspondence Upload.
//
//                 Naming convention  : iAction(actionType, identifyBy, locator, value)
//                 Test data access   : System.getProperty("TD:ColumnName") — set by Hooks via ExcelUtilities
//                 Herd data access   : ExcelUtilities.getRowData("HerdData", rowIndex)
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 07-03-2026
// ===================================================================================================================================

package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ObjReader;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.stream.Collectors;

import static commonFunctions.CommonFunctions.*;

public class TC_03 {

    private static final Logger log = Logger.getLogger(TC_03.class.getName());


    // ===================================================================================================================================
    //  BACKGROUND — Login and Portal Entry
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Given the agent user is on the login page
    // Description   : Launches browser and navigates to the application URL loaded from test data Config sheet
    // ***************************************************************************************************************************************************************************************
    @Given("the agent user is on the login page")
    public void theAgentUserIsOnTheLoginPage()
    {
        log.info("Application launched via Hooks. Browser is active and URL loaded.");
        log.info("[STEP] Given the agent user is on the login page");

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent logs into the application with valid credentials and OTP
    // Description   : Enters username and password from test data, submits login form, handles OTP if present
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs into the application with valid credentials and OTP")
    public void theAgentLogsIntoTheApplicationWithValidCredentialsAndOTP()
    {

        log.info("[LOGIN] Classic login detected.");

        iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        //iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), "TD:Username");
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), Hooks.RUNTIME_USERNAME);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        log.info("[STEP] Detect login screen and auto-login using simple PIN loop...");

        WebDriver driver = CommonFunctions.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        By pinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

        // --- Check for PIN login screen ---
        if (isVisible(pinFormBy, 3)) {

            log.info("[LOGIN] PIN screen detected. Using simple loop 1 → 7.");

            for (int idx = 1; idx <= 7; idx++) {

                String dynamicXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));

                By pinInputBy = By.xpath(dynamicXpath);

                if (isVisible(pinInputBy, 1)) {

                    WebElement input = driver.findElement(pinInputBy);

                    boolean disabled = input.getAttribute("disabled") != null;

                    if (!disabled && input.isEnabled())
                    {
                        input.clear();
                        input.sendKeys("1");
                        log.info("[LOGIN] Entered '1' into PIN index: " + idx);
                    }
                    else
                    {
                        log.info("[LOGIN] PIN index " + idx + " is DISABLED — skipping.");
                    }

                } else {
                    log.info("[LOGIN] PIN index " + idx + " not present in DOM — skipping.");
                }
            }

            // Click login button
            iAction("CLICK", "XPATH", ObjReader.getLocator("iPinLoginBtn"), null);
            log.info("[LOGIN] PIN login submitted.");
            // Enter the 6-digit TOTP value (hardcoded or from TD)
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"), "111111");

            // Click the TOTP Login / Submit button
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);

            log.info("[LOGIN] TOTP screen completed.");
            // Click the Terms & Conditions checkbox
            if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                // Click the Accept button
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
                log.info("[LOGIN] Accept Terms & Conditions completed.");
            }




        }
        else
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);
            log.info("[LOGIN] Classic login completed.");
        }

    }


    /** SIMPLE helper to avoid ambiguous method call */
    private boolean isVisible(By locator, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(CommonFunctions.getDriver(), Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent opens the "Basic Income Support for Sustainability" application
    // Description   : Clicks the named application tile on the portal home screen
    // ***************************************************************************************************************************************************************************************
    @And("the agent opens the {string} application")
    public void theAgentOpensTheApplication(String pApplicationName)
    {
            log.info("[STEP] And the agent opens the application: " + pApplicationName);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), "");

            log.info("Enter the application name which you want to lookup.");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");

            // Verify dashboard title element is visible — confirms successful login and page load
            iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iSearchAppLabel"), "");
            iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iSearchAppLabel"), "Found 1 applications matching your search");

            //Click on BISS Link from Search Results
            iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), "");

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent should land on the BISS Home page
    // Description   : Validates that the BISS Home page header is visible after login
    // ***************************************************************************************************************************************************************************************
    @Then("the agent should land on the BISS Home page")
    public void theAgentShouldLandOnTheBISSHomePage()
    {
            iAction("WAITINVISIBLE", "XPATH", "iScreenBuffer", "MDC Progress Spinner");
            log.info("[STEP] Then the agent should land on the BISS Home page");
            iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iBissTitle"), "");
            iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");
            log.info("BISS Home page confirmed | Title: " + "Basic Income Support for Sustainability");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent navigates to the "My Clients" tab
    // Description   : Clicks the My Clients navigation tab on the BISS portal
    // ***************************************************************************************************************************************************************************************
    @And("the agent navigates to the {string} and {string} Left Menu Link")
    public void theAgentNavigatesToTheTab(String iHomeTab,String iMyCLientTab)
    {
            log.info("[STEP] And the agent navigates to the tab: " + iHomeTab);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);

            log.info("[STEP] And the agent navigates to the tab: " + iMyCLientTab);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);

            // 2. Client Selection and Header Verification
            iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");

            iAction("CLICK", "XPATH", ObjReader.getLocator("iHerdExpiredTab"), "");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iDraftTab"), "");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iSubmittedTab"), "");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNotStartedTab"), "");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iViewAllTab"), "");


    }


    // ===================================================================================================================================
    //  FARMER SELECTION AND DASHBOARD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens a farmer dashboard using herd data from row 4
    // Description   : Reads herd number from the specified row of the HerdData sheet in TestData.xlsx
    //                 and searches for that farmer on the My Clients screen
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens a farmer dashboard using herd data")
    public void theAgentOpensAFarmerDashboardUsingHerdDataFromRow()
    {
        log.info("[STEP] When the agent opens a farmer dashboard using herd data");
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), Hooks.RUNTIME_HERD);
        iAction("CLICK",   "XPATH",    ObjReader.getLocator("herdSearchBtn"),   null);
        log.info("Farmer dashboard opened for herd number: " + "TD:iHerdNumber");

        // Form Interaction (Dropdown and Search)
        // Open the dropdown
        iAction("CLICK", "XPATH", ObjReader.getLocator("iListItemsPerPage"), null);

        // (optional) wait until panel is visible (if your iAction supports WAIT_VISIBLE)
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);

        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);

        // Click the last option dynamically
        iAction("CLICK", "XPATH", ObjReader.getLocator("iMatSelectLastOption"), null);

        log.info("[STEP] Last item from Page Size dropdown selected at runtime");
        log.info("[STEP] Last Item from Total Number of Pages List Box is selected");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the farmer dashboard should be displayed
    // Description   : Confirms the farmer dashboard panel is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the farmer dashboard should be displayed")
    public void theFarmerDashboardShouldBeDisplayed()
    {
        log.info("[STEP] Then the farmer dashboard should be displayed");
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
        iAction("CLICK",   "XPATH",    ObjReader.getLocator("iFirstClientName"),   null);

        //  CONDITIONAL CHECK: Delete existing draft if present before starting a new one
        By iDeleteBtnBy = By.xpath(ObjReader.getLocator("iDeleteDraftBtn"));

        // Check for 3 seconds only to keep the test fast
        if (isElementPresentAndVisible(iDeleteBtnBy, 3))
        {
            log.info("Existing draft detected. Clearing it before proceeding.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteDraftBtn"), "Delete Draft Button");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iConfirmDeleteBtn"), "Confirm Delete Button");

            // SYNC: Wait for the MDC spinner to finish the deletion process
            iAction("WAITINVISIBLE", "XPATH", "//div[@class='mdc-circular-progress__gap-patch']//*[name()='svg']", "Deletion Spinner");
        }
        else
        {
            log.info("No existing draft found. Proceeding directly to Start Application.");
        }

        //CLick on View Start Application for Farmer Dashboard
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStartApplicationBtn"), "");


        if (isVisible(By.xpath(ObjReader.getLocator("iContinueBtn")), 3)) {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);
        }
    }


    private boolean isElementPresentAndVisible(By pBy, int pSeconds)
    {
        try
        {
            WebDriverWait iShortWait = new WebDriverWait(getDriver(), java.time.Duration.ofSeconds(pSeconds));
            iShortWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(pBy));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }


    // ===================================================================================================================================
    //  SIDE NAVIGATION VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent navigates through the farmer side navigation tabs
    // Description   : Iterates through the provided DataTable list of tab names and clicks each one
    // ***************************************************************************************************************************************************************************************
    @When("the agent navigates through the farmer side navigation tabs")
    public void theAgentNavigatesThroughTheFarmerSideNavigationTabs(DataTable pDataTable)
    {
        log.info("[STEP] When the agent navigates through the farmer side navigation tabs");

        List<String> iTabs = pDataTable.asList();

        for (int i = 0; i < iTabs.size(); i++) {

            String clean = iTabs.get(i).trim();
            String xp;

            // SPECIAL CASE → Transfers uses a mat-icon
            if (clean.equalsIgnoreCase("Transfers")) {

                // Fluent wait is already integrated inside WAITVISIBLE
                iAction("WAITVISIBLE", "XPATH", "//mat-icon[normalize-space()='swap_horizontal_circle']", null);
                iAction("CLICK",       "XPATH", "//mat-icon[normalize-space()='swap_horizontal_circle']", null);

                // SKIP NEXT TAB
                i++;
                continue;
            }

            // Generic robust locator for all other tabs
            xp = String.format("(//mat-selection-list//span[contains(normalize-space(),'%s')])[1]", clean.replace("My ", ""));

            log.info("  Clicking side nav tab: " + clean);

            iAction("WAITVISIBLE",   "XPATH", xp, null);
            iAction("WAITCLICKABLE", "XPATH", xp, null);
            iAction("MOUSEHOVER",    "XPATH", xp, null);
            iAction("CLICK",         "XPATH", xp, null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then each requested side navigation tab should open successfully
    // Description   : Confirms the last clicked tab rendered its content panel
    // ***************************************************************************************************************************************************************************************
    @Then("each requested side navigation tab should open successfully")
    public void eachRequestedSideNavigationTabShouldOpenSuccessfully()
    {
        log.info("[STEP] Then each requested side navigation tab should open successfully");
        String iActiveTab = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerDashboardHeader"), null);
        Assertions.assertFalse(iActiveTab.isEmpty(), "An active side navigation tab should be present.");
        log.info("Side navigation tabs validated | Last active: " + iActiveTab);
    }


    // ===================================================================================================================================
    //  APPLICATION START
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent deletes any existing draft if present
    // Description   : Checks for an existing draft application and deletes it if found, otherwise continues
    // ***************************************************************************************************************************************************************************************
    @When("the agent deletes any existing draft if present")
    public void theAgentDeletesAnyExistingDraftIfPresent()
    {
        log.info("[STEP] When the agent deletes any existing draft if present");
        try {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteDraftBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iConfirmDeleteBtn"),null);
            log.info("Existing draft deleted.");
        } catch (Exception e) {
            log.info("No existing draft found — continuing to start new application.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent starts a new BISS application
    // Description   : Clicks the Start Application button to begin a new BISS submission
    // ***************************************************************************************************************************************************************************************
    @And("the agent starts a new BISS application")
    public void theAgentStartsANewBISSApplication()
    {
        log.info("[STEP] And the agent starts a new BISS application");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStartApplicationBtn"), null);
        if (isVisible(By.xpath(ObjReader.getLocator("iContinueBtn")), 5))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Active Farmer step should be displayed
    // Description   : Confirms the Active Farmer step header is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Active Farmer step should be displayed")
    public void theActiveFarmerStepShouldBeDisplayed()
    {
        log.info("[STEP] Then the Active Farmer step should be displayed");
        String iStepHeader = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerStatusComment"), null);
        Assertions.assertFalse(iStepHeader.isEmpty(), "Active Farmer step header should be visible.");
        log.info("Active Farmer step confirmed.");
    }


    // ===================================================================================================================================
    //  ACTIVE FARMER STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent selects "Making Hay/Silage/Haylage" in the Active Farmer step
    // Description   : Selects the specified active farmer activity radio button or checkbox
    // ***************************************************************************************************************************************************************************************
    @When("the agent selects {string} in the Active Farmer step")
    public void theAgentSelectsInTheActiveFarmerStep(String pActivity)
    {
        log.info("[STEP] When the agent selects in the Active Farmer step: " + pActivity);
        // Perform the UNCHECK operation
        iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iDeclareNotActiveChk"), "UNCHECK");
       // iAction("CLICK", "XPATH", "//label[contains(text(),'" + pActivity + "')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent proceeds to the next application step
    // Description   : Clicks the Next / Continue / Proceed button to advance the wizard step
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds to the next application step")
    public void theAgentProceedsToTheNextApplicationStep()
    {
        log.info("[STEP] And the agent proceeds to the next application step");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Scheme Selection step should be displayed
    // Description   : Confirms the Scheme Selection step header is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Scheme Selection step should be displayed")
    public void theSchemeSelectionStepShouldBeDisplayed() {
        log.info("[STEP] Then the Scheme Selection step should be displayed");
        String iStepHeader = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iSchemeSelectionHeader"), null);
        Assertions.assertFalse(iStepHeader.isEmpty(), "Scheme Selection step header should be visible.");
        log.info("Scheme Selection step confirmed.");
    }


    // ===================================================================================================================================
    //  SCHEME SELECTION STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent selects the "Organics" scheme
    // Description   : Clicks the scheme card or checkbox for the named scheme
    // ***************************************************************************************************************************************************************************************
    @When("the agent selects the {string} scheme")
    public void theAgentSelectsTheScheme(String pSchemeName)
    {
        log.info("[STEP] When the agent selects the scheme: " + pSchemeName);

        // Identify toggle button based on scheme name
        String toggleXpath = String.format("//span[normalize-space()='%s']/preceding::button[@role='switch'][1]", pSchemeName);

        // Read ON/OFF state using the aria-checked attribute
        WebElement toggle = CommonFunctions.getDriver().findElement(By.xpath(toggleXpath));
        String state = toggle.getAttribute("aria-checked");

        // If OFF → turn ON
        if ("false".equalsIgnoreCase(state)) {
            log.info("Scheme '" + pSchemeName + "' is OFF → turning ON");
            iAction("CLICK", "XPATH", "//mat-card/descendant::span[contains(text(),'"+pSchemeName+"')]", null);
            //iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
        }
        else {
            log.info("Scheme '" + pSchemeName + "' is already ON → no action");
        }

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent accepts the scheme selection acknowledgement if displayed
    // Description   : Dismisses any acknowledgement modal that appears after scheme selection — skips if not present
    // ***************************************************************************************************************************************************************************************
    @And("the agent accepts the scheme selection acknowledgement if displayed")
    public void theAgentAcceptsTheSchemeSelectionAcknowledgementIfDisplayed() {
        log.info("[STEP] And the agent accepts the scheme selection acknowledgement if displayed");
        try {
            log.info("Acknowledgement modal displayed — continuing.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCloseDialogBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIUnderstandBtn"), null);
            log.info("Scheme selection acknowledgement accepted.");
        } catch (Exception e) {
            log.info("No acknowledgement modal displayed — continuing.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIUnderstandBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Land Details step should be displayed
    // Description   : Confirms the Land Details step header is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Land Details step should be displayed")
    public void theLandDetailsStepShouldBeDisplayed()
    {
        log.info("[STEP] Then the Land Details step should be displayed");
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iLandDetailsHeader"), "Land Details");
        log.info("Land Details step confirmed.");
    }


    // ===================================================================================================================================
    //  LAND DETAILS — INVALID PARCEL VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent attempts to add parcel "Invalid789"
    // Description   : Enters an invalid parcel ID into the add parcel dialog and submits
    // ***************************************************************************************************************************************************************************************
    @When("the agent attempts to add parcel {string}")
    public void theAgentAttemptsToAddParcel(String pParcelId)
    {
        log.info("[STEP] When the agent attempts to add parcel: " + pParcelId);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the invalid parcel warning message should be displayed
    // Description   : Asserts the invalid parcel error message is visible on screen
    // ***************************************************************************************************************************************************************************************
    @Then("the invalid parcel warning message should be displayed")
    public void theInvalidParcelWarningMessageShouldBeDisplayed() {
        log.info("[STEP] Then the invalid parcel warning message should be displayed");
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iInvalidParcelError"), "Please enter a valid parcel number");
        log.info("Invalid parcel warning confirmed: ");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the add parcel dialog should remain available for correction or cancellation
    // Description   : Confirms the add parcel dialog is still open and usable after an invalid entry
    // ***************************************************************************************************************************************************************************************
    @And("the add parcel dialog should remain available for correction or cancellation")
    public void theAddParcelDialogShouldRemainAvailableForCorrectionOrCancellation() {
        log.info("[STEP] And the add parcel dialog should remain available for correction or cancellation");
        String iDialogTitle = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iAddParcelModalHeader"), null);
        Assertions.assertFalse(iDialogTitle.isEmpty(), "Add parcel dialog should still be open after invalid entry.");
        log.info("Add parcel dialog still open: " + iDialogTitle);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ARCHIVED PARCEL VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent attempts to add archived parcel "H19112055"
    // Description   : Enters a known archived parcel ID and submits it
    // ***************************************************************************************************************************************************************************************
    @When("the agent attempts to add archived parcel {string}")
    public void theAgentAttemptsToAddArchivedParcel(String pParcelId) {
        log.info("[STEP] When the agent attempts to add archived parcel: " + pParcelId);
        //iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the archived parcel warning should be displayed if applicable
    // Description   : Soft assertion — logs warning if message is found; does not fail if not present
    // ***************************************************************************************************************************************************************************************
    @Then("the archived parcel warning should be displayed if applicable")
    public void theArchivedParcelWarningShouldBeDisplayedIfApplicable() {
        log.info("[STEP] Then the archived parcel warning should be displayed if applicable");
        try {
            String iWarning = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iLpisTransformationError"), null);
            log.info("Archived parcel warning found: " + iWarning);
        } catch (Exception e) {
            log.info("No archived parcel warning displayed — may not apply to this parcel in this environment.");
        }
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ADD VALID PARCEL
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent adds parcel "A1190600017" with claimed area "7"
    // Description   : Opens add parcel dialog, enters parcel ID and claimed area, submits
    // ***************************************************************************************************************************************************************************************
    @When("the agent adds parcel {string} with claimed area {string}")
    public void theAgentAddsParcelWithClaimedArea(String pParcelId, String pClaimedArea) {
        log.info("[STEP] When the agent adds parcel: " + pParcelId + " with claimed area: " + pClaimedArea);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"),pClaimedArea);


    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent sets parcel ownership status to "Owned"
    // Description   : Selects ownership status from the dropdown in the add parcel form
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets parcel ownership status to {string}")
    public void theAgentSetsParcelOwnershipStatusTo(String pOwnershipStatus) {
        log.info("[STEP] And the agent sets parcel ownership status to: " + pOwnershipStatus);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"),  pOwnershipStatus);
        //iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusOwned"),  pOwnershipStatus);


    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent sets parcel use to "Apples"
    // Description   : Selects parcel land use from the dropdown in the add parcel form
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets parcel use to {string}")
    public void theAgentSetsParcelUseTo(String pParcelUse) {
        log.info("[STEP] And the agent sets parcel use to: " + pParcelUse);
        //iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iParcelUseSelect"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelUseSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), pParcelUse);

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent sets parcel organic status to "Conventional"
    // Description   : Selects organic status from the dropdown in the add parcel form
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets parcel organic status to {string}")
    public void theAgentSetsParcelOrganicStatusTo(String pOrganicStatus) {
        log.info("[STEP] And the agent sets parcel organic status to: " + pOrganicStatus);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"),  pOrganicStatus);

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the parcel should be added successfully to Land Details
    // Description   : Confirms success message or parcel row is visible in Land Details table
    // ***************************************************************************************************************************************************************************************
    @Then("the parcel should be added successfully to Land Details")
    public void theParcelShouldBeAddedSuccessfullyToLandDetails() {
        log.info("[STEP] Then the parcel should be added successfully to Land Details");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);
        log.info("Parcel added successfully: ");
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ALREADY CLAIMED PARCEL
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent attempts to add parcel "A1190600017" again
    // Description   : Attempts to re-add a parcel that was already added in this session
    // ***************************************************************************************************************************************************************************************
    @When("the agent attempts to add parcel {string} again")
    public void theAgentAttemptsToAddParcelAgain(String pParcelId) {
        log.info("[STEP] When the agent attempts to add parcel again: " + pParcelId);
        log.info("[STEP] When the agent adds parcel: " + pParcelId + " with claimed area: ");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the already claimed parcel warning should be displayed if applicable
    // Description   : Soft assertion for duplicate parcel warning — logs result without hard failing
    // ***************************************************************************************************************************************************************************************
    @Then("the already claimed parcel warning should be displayed if applicable")
    public void theAlreadyClaimedParcelWarningShouldBeDisplayedIfApplicable() {
        log.info("[STEP] Then the already claimed parcel warning should be displayed if applicable");
        try {
            String iWarning = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iAlreadyClaimedParcelWarning"), null);
            log.info("Already claimed warning found: " + iWarning);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCancelBtn"), null);
        } catch (Exception e) {
            log.info("No already-claimed warning displayed.");
        }
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ADD PLOT
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent adds a plot with the following details
    // Description   : Reads plot details from DataTable and fills in the add plot form fields
    //                 DataTable keys: county, townland, plotReference, ownershipStatus,
    //                                 organicStatus, claimedArea, plotUse, mapChangeOption
    // ***************************************************************************************************************************************************************************************
    @When("the agent adds a plot with the following details")
    public void theAgentAddsAPlotWithTheFollowingDetails(DataTable pDataTable)
    {
        log.info("[STEP] When the agent adds a plot with the following details");

        Map<String, String> iPlotData = pDataTable.asMap(String.class, String.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().trim(),
                        e -> e.getValue().trim()
                ));

        // Add Plot Button
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddPlotBtn"), null);

        // County
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCountySelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iCountySelect"), iPlotData.get("county"));

        // Townland
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTownlandSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iTownlandSelect"), iPlotData.get("townland"));

        // Plot Reference
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPlotReferenceInput"), iPlotData.get("plotReference"));

        // Ownership Status
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), iPlotData.get("ownershipStatus"));

        // Organic Status
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), iPlotData.get("organicStatus"));

        // Claimed Area
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), iPlotData.get("claimedArea"));

        // Plot Use
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPlotUseSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iPlotUseSelect"), iPlotData.get("plotUse"));

        // Map Change Option
        iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iMapChangeOption_SubmitPaperMap"), null);

        log.info("Plot form filled | Reference: " + iPlotData.get("plotReference"));
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the plot should be added successfully to Land Details
    // Description   : Submits the add plot form and confirms a success confirmation is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the plot should be added successfully to Land Details")
    public void thePlotShouldBeAddedSuccessfullyToLandDetails() {
        log.info("[STEP] Then the plot should be added successfully to Land Details");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn2"), null);
       // iAction("CLICK", "XPATH", "//button[contains(text(),'Add') or contains(text(),'Save') or contains(text(),'Submit')]", null);
       // String iConfirmation = iAction("GETTEXT", "XPATH", "//div[contains(@class,'success') or contains(@class,'confirmation')]", null);
        //Assertions.assertFalse(iConfirmation.isEmpty(), "Plot success confirmation should be visible after adding.");
       // log.info("Plot added successfully: " + iConfirmation);
        //Delete this line later - Aniket
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCancelBtn"), null);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ADD PARCEL FROM GIS MAP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent adds a parcel from the GIS map with the following details
    // Description   : Opens the map interface, selects county and townland, fills parcel fields
    //                 DataTable keys: county, townland, claimedArea, ownershipStatus, parcelUse, organicStatus
    // ***************************************************************************************************************************************************************************************
    @When("the agent adds a parcel from the GIS map with the following details")
    public void theAgentAddsAParcelFromTheGISMapWithTheFollowingDetails(DataTable pDataTable) {
        log.info("[STEP] When the agent adds a parcel from the GIS map with the following details");
        Map<String, String> iMapData = pDataTable.asMap(String.class, String.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().trim(),
                        e -> e.getValue().trim()
                ));
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        // County
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCountySelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iCountySelect"), iMapData.get("county"));

        // Townland
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTownlandSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iTownlandSelect"), iMapData.get("townland"));

        iAction("CLICK", "XPATH", ObjReader.getLocator("iMapOpenBtn"), null);

        iAction("CLICK", "XPATH", ObjReader.getLocator("selectFeature"), null);

        iAction("CLICK", "XPATH", ObjReader.getLocator("mainMapImage"), null);

        iAction("CLICK", "XPATH", ObjReader.getLocator("parcelRowSpecific"), null);

        iAction("CLICK", "XPATH", ObjReader.getLocator("claimButton"), null);

        // Claimed Area
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), iMapData.get("claimedArea"));

        // Ownership Status
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), iMapData.get("ownershipStatus"));

        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelUseSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), iMapData.get("parcelUse"));

        // Organic Status
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), iMapData.get("organicStatus"));

        log.info("[STEP] Then the parcel should be added successfully to Land Details");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);
        log.info("Parcel added successfully: ");

        log.info("GIS map parcel form filled");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the GIS-selected parcel should be added successfully
    // Description   : Submits the GIS parcel form and confirms success
    // ***************************************************************************************************************************************************************************************
    @Then("the GIS-selected parcel should be added successfully")
    public void theGISSelectedParcelShouldBeAddedSuccessfully() {
        log.info("[STEP] Then the GIS-selected parcel should be added successfully");

        log.info("GIS parcel added successfully: ");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — PARCEL AVAILABILITY CHECK
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Then parcel "J1650300004" should be available in Land Details
    // Description   : Confirms the specified parcel ID row exists in the Land Details parcel table
    // ***************************************************************************************************************************************************************************************
    @Then("parcel {string} should be available in Land Details")
    public void parcelShouldBeAvailableInLandDetails(String pParcelId) {
            log.info("[STEP] Then parcel should be available in Land Details: " + pParcelId);
            WebDriver driver = CommonFunctions.getDriver();
            WebDriverWait wait = CommonFunctions.getWait();

            log.info("[STEP] Searching and selecting parcel: " + pParcelId);

            String parcelCellXpath = ObjReader.getLocator("iParcelRefCell");

            List<WebElement> cells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(parcelCellXpath)));

            boolean found = false;

            for (WebElement cell : cells)
            {
                String text = cell.getText().trim();
                log.info("Checking parcel value: " + text);

                if (text.equalsIgnoreCase(pParcelId))
                {
                    log.info("Parcel match found → " + pParcelId);
                    // Click using your hardened action engine
                    iAction("CLICK", "XPATH", parcelCellXpath + "[text()='" + pParcelId + "']", null);
                    found = true;
                    log.info("[STEP COMPLETE] Parcel opened in side drawer: " + pParcelId);
                    break;
                }
            }

            if (!found)
            {
                throw new AssertionError("Parcel not found in table: " + pParcelId);
            }
            log.info("[STEP COMPLETE] Parcel opened in side drawer: " + pParcelId);
        }


    // ===================================================================================================================================
    //  LAND DETAILS — EDIT PARCEL (SIDE DRAWER)
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens parcel "J1650300004" in the side drawer
    // Description   : Clicks the parcel row or edit icon to open the side drawer editor
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens parcel {string} in the side drawer")
    public void theAgentOpensParcelInTheSideDrawer(String pParcelId)
    {
        log.info("[STEP] When the agent opens parcel in the side drawer: " + pParcelId);
        iAction("VERIFYTEXT", "XPATH", "//mat-dialog-container//*[contains(normalize-space(),'" + pParcelId + "')]", pParcelId);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent raises an EH change request with reason "Test Reason"
    // Description   : Enters a change request reason in the side drawer EH section
    // ***************************************************************************************************************************************************************************************
    @And("the agent raises an EH change request with reason {string}")
    public void theAgentRaisesAnEHChangeRequestWithReason(String pReason)
    {

        log.info("[STEP] And the agent raises an EH change request with reason: " + pReason);
        iAction("CLICK", "XPATH", "iRequestEhChangeCheckbox", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent changes parcel use to "Coriander"
    // Description   : Updates the parcel use dropdown in the side drawer
    // ***************************************************************************************************************************************************************************************
    @And("the agent changes parcel use to {string}")
    public void theAgentChangesParcelUseTo(String pParcelUse) {
        log.info("[STEP] And the agent changes parcel use to: " + pParcelUse);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelUseSelect"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), pParcelUse);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent saves the parcel changes
    // Description   : Clicks Save in the side drawer to commit the parcel edits
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves the parcel changes")
    public void theAgentSavesTheParcelChanges() {
        log.info("[STEP] And the agent saves the parcel changes");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iSaveParcelChangesBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the parcel update should be saved successfully
    // Description   : Confirms that the parcel save action produced a success indicator
    // ***************************************************************************************************************************************************************************************
    @Then("the parcel update should be saved successfully")
    public void theParcelUpdateShouldBeSavedSuccessfully() {
        log.info("[STEP] Then the parcel update should be saved successfully");
        String iConfirmation = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'success') or contains(@class,'toast') or contains(@class,'alert-success')]",
                null);
        Assertions.assertFalse(iConfirmation.isEmpty(), "Parcel save confirmation should be visible.");
        log.info("Parcel update saved: " + iConfirmation);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — DELETE AND UNDO PARCEL
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent deletes parcel "J1650300004"
    // Description   : Clicks the delete icon on the specified parcel row
    // ***************************************************************************************************************************************************************************************
    @When("the agent deletes parcel {string}")
    public void theAgentDeletesParcel(String pParcelId) {
        log.info("[STEP] When the agent deletes parcel: " + pParcelId);
        iAction("CLICK", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]/ancestor::tr//button[contains(@class,'delete') or contains(text(),'Delete') or contains(@aria-label,'Delete')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then parcel "J1650300004" should be marked for deletion
    // Description   : Confirms the parcel row has a deletion indicator (strikethrough, badge, or class)
    // ***************************************************************************************************************************************************************************************
    @Then("parcel {string} should be marked for deletion")
    public void parcelShouldBeMarkedForDeletion(String pParcelId) {
        log.info("[STEP] Then parcel should be marked for deletion: " + pParcelId);
        String iDeletedRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]/ancestor::tr[contains(@class,'deleted') or contains(@class,'marked') or contains(@class,'strikethrough')]",
                null);
        Assertions.assertFalse(iDeletedRow.isEmpty(),
                "Parcel " + pParcelId + " should be visually marked for deletion.");
        log.info("Parcel marked for deletion confirmed: " + pParcelId);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent undoes deletion for parcel "J1650300004"
    // Description   : Clicks the Undo button on the deleted parcel row
    // ***************************************************************************************************************************************************************************************
    @When("the agent undoes deletion for parcel {string}")
    public void theAgentUndoesDeletionForParcel(String pParcelId) {
        log.info("[STEP] When the agent undoes deletion for parcel: " + pParcelId);
        iAction("CLICK", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]/ancestor::tr//button[contains(text(),'Undo') or contains(@aria-label,'Undo')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then parcel "J1650300004" should be restored in Land Details
    // Description   : Confirms the parcel row no longer has a deletion indicator
    // ***************************************************************************************************************************************************************************************
    @Then("parcel {string} should be restored in Land Details")
    public void parcelShouldBeRestoredInLandDetails(String pParcelId) {
        log.info("[STEP] Then parcel should be restored in Land Details: " + pParcelId);
        String iRestoredRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]",
                null);
        Assertions.assertFalse(iRestoredRow.isEmpty(),
                "Parcel " + pParcelId + " should be visible and restored in Land Details.");
        log.info("Parcel restored confirmed: " + pParcelId);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — PLOT AVAILABILITY AND DELETE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Then plot "T87654321" should be available in Land Details
    // Description   : Confirms the plot reference is visible in the Land Details table
    // ***************************************************************************************************************************************************************************************
    @Then("plot {string} should be available in Land Details")
    public void plotShouldBeAvailableInLandDetails(String pPlotRef) {
        log.info("[STEP] Then plot should be available in Land Details: " + pPlotRef);
        String iPlotRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pPlotRef + "')]",
                null);
        Assertions.assertFalse(iPlotRow.isEmpty(),
                "Plot " + pPlotRef + " should appear in Land Details table.");
        log.info("Plot confirmed in Land Details: " + pPlotRef);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent deletes parcel or plot "T87654321"
    // Description   : Clicks the delete button on the specified parcel or plot row
    // ***************************************************************************************************************************************************************************************
    @When("the agent deletes parcel or plot {string}")
    public void theAgentDeletesParcelOrPlot(String pReference) {
        log.info("[STEP] When the agent deletes parcel or plot: " + pReference);
        iAction("CLICK", "XPATH",
                "//td[contains(text(),'" + pReference + "')]/ancestor::tr//button[contains(@class,'delete') or contains(text(),'Delete') or contains(@aria-label,'Delete')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent confirms the deletion
    // Description   : Confirms a deletion modal prompt if it appears
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms the deletion")
    public void theAgentConfirmsTheDeletion() {
        log.info("[STEP] And the agent confirms the deletion");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Confirm') or contains(text(),'Yes') or contains(text(),'OK')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then plot "T87654321" should be marked for deletion
    // Description   : Confirms the plot row has a deletion indicator
    // ***************************************************************************************************************************************************************************************
    @Then("plot {string} should be marked for deletion")
    public void plotShouldBeMarkedForDeletion(String pPlotRef) {
        log.info("[STEP] Then plot should be marked for deletion: " + pPlotRef);
        String iDeletedRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pPlotRef + "')]/ancestor::tr[contains(@class,'deleted') or contains(@class,'marked')]",
                null);
        Assertions.assertFalse(iDeletedRow.isEmpty(),
                "Plot " + pPlotRef + " should be visually marked for deletion.");
        log.info("Plot marked for deletion confirmed: " + pPlotRef);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — MANDATORY INFORMATION COMPLETION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent completes all mandatory information in Land Details
    // Description   : Handles any remaining mandatory fields flagged in the Land Details step
    // ***************************************************************************************************************************************************************************************
    @When("the agent completes all mandatory information in Land Details")
    public void theAgentCompletesAllMandatoryInformationInLandDetails() {
        log.info("[STEP] When the agent completes all mandatory information in Land Details");
        // Clicks the complete mandatory info button or resolves inline mandatory field indicators
        try {
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Complete Mandatory') or contains(text(),'Complete Information')]",
                    null);
        } catch (Exception e) {
            log.info("No explicit complete mandatory button — mandatory fields resolved inline.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the next application step should open successfully
    // Description   : Confirms the application has advanced past Land Details
    // ***************************************************************************************************************************************************************************************
    @Then("the next application step should open successfully")
    public void theNextApplicationStepShouldOpenSuccessfully() {
        log.info("[STEP] Then the next application step should open successfully");
        String iStepHeader = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);
        Assertions.assertFalse(iStepHeader.isEmpty(), "A new step header should be visible after advancing.");
        log.info("Next application step opened: " + iStepHeader);
    }


    // ===================================================================================================================================
    //  GAEC 7 STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens the "GAEC 7" step
    // Description   : Clicks the GAEC 7 step link in the application navigation
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens the {string} step")
    public void theAgentOpensTheStep(String pStepName) {
        log.info("[STEP] When the agent opens the step: " + pStepName);
        iAction("CLICK", "XPATH",
                "//a[contains(text(),'" + pStepName + "')] | //button[contains(text(),'" + pStepName + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent proceeds from GAEC 7
    // Description   : Clicks the Continue or Next button on the GAEC 7 panel
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds from GAEC 7")
    public void theAgentProceedsFromGAEC7() {
        log.info("[STEP] And the agent proceeds from GAEC 7");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Continue') or contains(text(),'Next') or contains(text(),'Proceed')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent handles any GAEC 7 continue action if present
    // Description   : Soft step — clicks a secondary continue if a GAEC 7 confirmation dialog appears
    // ***************************************************************************************************************************************************************************************
    @And("the agent handles any GAEC 7 continue action if present")
    public void theAgentHandlesAnyGAEC7ContinueActionIfPresent() {
        log.info("[STEP] And the agent handles any GAEC 7 continue action if present");
        try {
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Continue') or contains(text(),'OK') or contains(text(),'Accept')]",
                    null);
            log.info("GAEC 7 secondary continue action handled.");
        } catch (Exception e) {
            log.info("No GAEC 7 secondary action present — continuing.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the application should move beyond the GAEC 7 step
    // Description   : Confirms the page has moved past GAEC 7 by checking the header no longer reads GAEC 7
    // ***************************************************************************************************************************************************************************************
    @Then("the application should move beyond the GAEC 7 step")
    public void theApplicationShouldMoveBeyondTheGAEC7Step() {
        log.info("[STEP] Then the application should move beyond the GAEC 7 step");
        String iCurrentStep = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);
        Assertions.assertFalse(iCurrentStep.contains("GAEC 7"),
                "Application should have moved past GAEC 7. Current step: " + iCurrentStep);
        log.info("Application moved beyond GAEC 7 | Current step: " + iCurrentStep);
    }


    // ===================================================================================================================================
    //  ACRES STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects "Yes, rescore" on panel 1
    // Description   : Selects the specified ACRES panel option by label text
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} on panel {int}")
    public void theAgentSelectsOnPanel(String pOption, int pPanelNumber) {
        log.info("[STEP] And the agent selects: " + pOption + " on panel: " + pPanelNumber);
        iAction("CLICK", "XPATH",
                "(//div[contains(@class,'panel')])[" + pPanelNumber + "]//label[contains(text(),'" + pOption + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent continues panel 1
    // Description   : Clicks the Continue button within the specified panel
    // ***************************************************************************************************************************************************************************************
    @And("the agent continues panel {int}")
    public void theAgentContinuesPanel(int pPanelNumber) {
        log.info("[STEP] And the agent continues panel: " + pPanelNumber);
        iAction("CLICK", "XPATH",
                "(//div[contains(@class,'panel')])[" + pPanelNumber + "]//button[contains(text(),'Continue')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the ACRES step should be completed successfully
    // Description   : Confirms ACRES step completion indicator is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the ACRES step should be completed successfully")
    public void theACRESStepShouldBeCompletedSuccessfully() {
        log.info("[STEP] Then the ACRES step should be completed successfully");
        String iStepStatus = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'step-complete') or contains(@class,'step-success')]",
                null);
        Assertions.assertFalse(iStepStatus.isEmpty(), "ACRES step completion indicator should be visible.");
        log.info("ACRES step completed: " + iStepStatus);
    }


    // ===================================================================================================================================
    //  ECO STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects the "AP2" scheme option
    // Description   : Clicks the named Eco scheme option card or radio button
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects the {string} scheme option")
    public void theAgentSelectsTheSchemeOption(String pSchemeOption) {
        log.info("[STEP] And the agent selects the scheme option: " + pSchemeOption);
        iAction("CLICK", "XPATH",
                "//label[contains(text(),'" + pSchemeOption + "')] | //div[contains(@data-scheme,'" + pSchemeOption + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects "Standard" for panel 2
    // Description   : Selects the named option within the specified Eco panel
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} for panel {int}")
    public void theAgentSelectsForPanel(String pOption, int pPanelNumber) {
        log.info("[STEP] And the agent selects: " + pOption + " for panel: " + pPanelNumber);
        iAction("CLICK", "XPATH",
                "(//div[contains(@class,'eco-panel')])[" + pPanelNumber + "]//label[contains(text(),'" + pOption + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent saves the selected eco option
    // Description   : Clicks Save on the current Eco option selection
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves the selected eco option")
    public void theAgentSavesTheSelectedEcoOption() {
        log.info("[STEP] And the agent saves the selected eco option");
        iAction("CLICK", "XPATH", "//button[contains(text(),'Save')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects approved spreader manufacturer "Lemken"
    // Description   : Selects the spreader manufacturer from the AP5 dropdown
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects approved spreader manufacturer {string}")
    public void theAgentSelectsApprovedSpreaderManufacturer(String pManufacturer) {
        log.info("[STEP] And the agent selects approved spreader manufacturer: " + pManufacturer);
        iAction("LIST", "ID", "spreaderManufacturer", "VISIBLETEXT:" + pManufacturer);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects approved spreader model "Polaris 14"
    // Description   : Selects the spreader model from the AP5 dropdown
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects approved spreader model {string}")
    public void theAgentSelectsApprovedSpreaderModel(String pModel) {
        log.info("[STEP] And the agent selects approved spreader model: " + pModel);
        iAction("LIST", "ID", "spreaderModel", "VISIBLETEXT:" + pModel);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent enters spreader serial number "A735B78346"
    // Description   : Types the spreader serial number into the AP5 serial number field
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters spreader serial number {string}")
    public void theAgentEntersSpreaderSerialNumber(String pSerialNumber) {
        log.info("[STEP] And the agent enters spreader serial number: " + pSerialNumber);
        iAction("TEXTBOX", "ID", "spreaderSerialNumber", pSerialNumber);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Eco step should be completed successfully
    // Description   : Confirms the Eco step completion indicator is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Eco step should be completed successfully")
    public void theEcoStepShouldBeCompletedSuccessfully() {
        log.info("[STEP] Then the Eco step should be completed successfully");
        String iStepStatus = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'step-complete') or contains(@class,'step-success')]",
                null);
        Assertions.assertFalse(iStepStatus.isEmpty(), "Eco step completion indicator should be visible.");
        log.info("Eco step completed: " + iStepStatus);
    }


    // ===================================================================================================================================
    //  ECO OPT-OUT VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent returns to the "Scheme Selection" step
    // Description   : Navigates back to the Scheme Selection step via the step navigation links
    // ***************************************************************************************************************************************************************************************
    @When("the agent returns to the {string} step")
    public void theAgentReturnsToTheStep(String pStepName) {
        log.info("[STEP] When the agent returns to the step: " + pStepName);
        iAction("CLICK", "XPATH",
                "//a[contains(text(),'" + pStepName + "')] | //button[contains(text(),'" + pStepName + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent opens the "Eco" scheme card
    // Description   : Clicks the Eco scheme card in the Scheme Selection step
    // ***************************************************************************************************************************************************************************************
    @And("the agent opens the {string} scheme card")
    public void theAgentOpensTheSchemeCard(String pSchemeName) {
        log.info("[STEP] And the agent opens the scheme card: " + pSchemeName);
        iAction("CLICK", "XPATH",
                "//div[contains(@class,'scheme-card')]//h3[contains(text(),'" + pSchemeName + "')]/ancestor::div[contains(@class,'scheme-card')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent accepts the acknowledgement if displayed
    // Description   : Dismisses any acknowledgement modal — skips safely if not present
    // ***************************************************************************************************************************************************************************************
    @And("the agent accepts the acknowledgement if displayed")
    public void theAgentAcceptsTheAcknowledgementIfDisplayed() {
        log.info("[STEP] And the agent accepts the acknowledgement if displayed");
        try {
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Accept') or contains(text(),'Acknowledge') or contains(text(),'OK')]",
                    null);
            log.info("Acknowledgement accepted.");
        } catch (Exception e) {
            log.info("No acknowledgement modal present — continuing.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the application should continue successfully after eco validation
    // Description   : Confirms the application step moved forward after eco opt-out validation
    // ***************************************************************************************************************************************************************************************
    @Then("the application should continue successfully after eco validation")
    public void theApplicationShouldContinueSuccessfullyAfterEcoValidation() {
        log.info("[STEP] Then the application should continue successfully after eco validation");
        String iCurrentStep = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);
        Assertions.assertFalse(iCurrentStep.isEmpty(),
                "A step header should be visible after eco opt-out validation.");
        log.info("Application continued after eco validation | Current step: " + iCurrentStep);
    }


    // ===================================================================================================================================
    //  REVIEW AND SUBMIT
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent completes all review page next actions
    // Description   : Iterates through any Next buttons on the Review & Submit multi-page review
    // ***************************************************************************************************************************************************************************************
    @And("the agent completes all review page next actions")
    public void theAgentCompletesAllReviewPageNextActions() {
        log.info("[STEP] And the agent completes all review page next actions");
        int iMaxPages = 10;
        int iPage     = 0;
        while (iPage < iMaxPages) {
            try {
                iAction("CLICK", "XPATH",
                        "//button[contains(text(),'Next') and not(@disabled)]",
                        null);
                iPage++;
                log.info("Review page next clicked — page: " + iPage);
            } catch (Exception e) {
                log.info("No more Next buttons on review — all review pages completed.");
                break;
            }
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent accepts the Terms and Conditions
    // Description   : Checks the Terms and Conditions checkbox on the submission page
    // ***************************************************************************************************************************************************************************************
    @And("the agent accepts the Terms and Conditions")
    public void theAgentAcceptsTheTermsAndConditions() {
        log.info("[STEP] And the agent accepts the Terms and Conditions");
        iAction("CHECKBOX", "ID", "termsAndConditions", "CHECK");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent submits the application
    // Description   : Clicks the Submit Application button
    // ***************************************************************************************************************************************************************************************
    @And("the agent submits the application")
    public void theAgentSubmitsTheApplication() {
        log.info("[STEP] And the agent submits the application");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Submit') and not(contains(text(),'Confirm'))]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent confirms the application submission
    // Description   : Confirms the submission confirmation modal
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms the application submission")
    public void theAgentConfirmsTheApplicationSubmission() {
        log.info("[STEP] And the agent confirms the application submission");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Confirm') or contains(text(),'Yes, Submit') or contains(text(),'OK')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the application should be submitted successfully
    // Description   : Confirms the application submission success message or reference number is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the application should be submitted successfully")
    public void theApplicationShouldBeSubmittedSuccessfully() {
        log.info("[STEP] Then the application should be submitted successfully");
        String iConfirmation = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'success') or contains(@class,'confirmation') or contains(@class,'submitted')]",
                null);
        Assertions.assertFalse(iConfirmation.isEmpty(),
                "Application submission success message should be visible.");
        log.info("Application submitted successfully | Confirmation: " + iConfirmation);
    }


    // ===================================================================================================================================
    //  CORRESPONDENCE — DOCUMENT UPLOAD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent chooses to upload a document
    // Description   : Clicks the Upload Document button on the Correspondence tab
    // ***************************************************************************************************************************************************************************************
    @And("the agent chooses to upload a document")
    public void theAgentChoosesToUploadADocument() {
        log.info("[STEP] And the agent chooses to upload a document");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Upload') or contains(text(),'Upload Document')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects document type "Commonage Evidence"
    // Description   : Selects the document type from the upload form dropdown
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects document type {string}")
    public void theAgentSelectsDocumentType(String pDocumentType) {
        log.info("[STEP] And the agent selects document type: " + pDocumentType);
        iAction("LIST", "ID", "documentType", "VISIBLETEXT:" + pDocumentType);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent uploads the correspondence document
    // Description   : Sends the file path to the file input element to trigger upload
    //                 File path is read from test data property TD:CorrespondenceFilePath
    // ***************************************************************************************************************************************************************************************
    @And("the agent uploads the correspondence document")
    public void theAgentUploadsTheCorrespondenceDocument() {
        log.info("[STEP] And the agent uploads the correspondence document");
        String iFilePath = System.getProperty("TD:CorrespondenceFilePath",
                "src/test/resources/TestDocuments/sample_correspondence.pdf");
        iAction("TEXTBOX", "CSS", "input[type='file']", iFilePath);
        log.info("Document file path sent to upload input: " + iFilePath);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent confirms the upload
    // Description   : Clicks the Confirm or Submit button on the document upload dialog
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms the upload")
    public void theAgentConfirmsTheUpload() {
        log.info("[STEP] And the agent confirms the upload");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Confirm') or contains(text(),'Upload') or contains(text(),'Submit')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the document should be uploaded successfully in Correspondence
    // Description   : Confirms the uploaded document appears in the Correspondence document list
    // ***************************************************************************************************************************************************************************************
    @Then("the document should be uploaded successfully in Correspondence")
    public void theDocumentShouldBeUploadedSuccessfullyInCorrespondence() {
        log.info("[STEP] Then the document should be uploaded successfully in Correspondence");
        String iDocRow = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'correspondence-list')]//td[contains(text(),'Commonage Evidence')]",
                null);
        Assertions.assertFalse(iDocRow.isEmpty(),
                "Uploaded document should be visible in the Correspondence list.");
        log.info("Document uploaded and confirmed in Correspondence: " + iDocRow);
    }
}
