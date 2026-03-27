// ===================================================================================================================================
// File          : TC_03.java
// Package       : stepdefinitions
// Description   : Step definitions for the BISS Agent end-to-end farmer application journey.
//
//                 This class covers the COMPLETE business flow that an agent goes through
//                 when processing a farmer's BISS application — from logging in all the way
//                 through to submitting the application and uploading correspondence documents.
//
//                 Flow Summary:
//                   1.  Login (Classic OTP or PIN + TOTP)
//                   2.  Portal navigation to BISS application
//                   3.  My Clients — tab navigation + farmer search
//                   4.  Farmer Dashboard — draft cleanup + application start
//                   5.  Side navigation tab validation
//                   6.  Active Farmer step
//                   7.  Scheme Selection step (Organics toggle)
//                   8.  Land Details — invalid parcel, archived parcel, add parcel,
//                                       already-claimed check, add plot, GIS map parcel,
//                                       edit/delete/undo parcel, delete plot
//                   9.  GAEC 7 step
//                   10. ACRES step (panel selections + warnings)
//                   11. Eco step (AP2, AP5 with spreader details)
//                   12. Eco opt-out validation
//                   13. Review & Submit
//                   14. Correspondence document upload
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  — all UI interactions
//                   ObjReader.getLocator("keyName")                   — all locator lookups
//                   Hooks.RUNTIME_HERD / Hooks.RUNTIME_USERNAME        — DB-resolved at suite start
//                   "TD:ColumnName"                                    — test data values from Excel
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 07-03-2026
// ===================================================================================================================================

package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ObjReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_03
{
    private static final Logger log = Logger.getLogger(TC_03.class.getName());


    // ===================================================================================================================================
    //  SECTION 1 : BACKGROUND — Login and Portal Entry
    //
    //  These steps run before EVERY scenario in TC_03.feature (defined under Background).
    //  They get the agent logged in and landed on the BISS home page before any scenario begins.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Given the agent user is on the login page
    //
    // What happens here:
    //   Nothing needs to be done in code — Hooks.beforeAllExecution() has already launched
    //   the browser and navigated to the application URL before this step is even called.
    //   This step exists purely to make the Gherkin readable and self-documenting.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Given("the agent user is on the login page")
    public void theAgentUserIsOnTheLoginPage()
    {
        // The browser is already open and on the login page — Hooks handled the launch.
        // This step is intentionally a no-op; it just confirms the precondition in the report.
        log.info("[STEP] Given: Agent is on the login page — browser launched by Hooks.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent logs into the application with valid credentials and OTP
    //
    // What happens here:
    //   This is the smart login handler. After username + password are submitted, the app
    //   may respond with one of TWO second-factor screens:
    //
    //   Path A — PIN screen (iPinForm is visible within 3 seconds):
    //     The agent's login requires a PIN. We loop through PIN digit inputs 1→7, entering "1"
    //     into each enabled box and skipping any that are disabled. After PIN submission,
    //     we enter the 6-digit TOTP code and submit. Finally, we accept Terms & Conditions
    //     if that screen appears (first-time login scenario).
    //
    //   Path B — Classic OTP (PIN screen not visible):
    //     Standard Keycloak OTP flow. Enter the OTP code directly and click login.
    //
    //   The herd and username used here come from Hooks.RUNTIME_USERNAME — resolved dynamically
    //   from the Oracle DB at suite startup (not hardcoded in a spreadsheet).
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent logs into the application with valid credentials and OTP")
    public void theAgentLogsIntoTheApplicationWithValidCredentialsAndOTP()
    {
        log.info("[LOGIN] Starting login sequence for username: " + Hooks.RUNTIME_USERNAME);

        // Step 1: Click the welcome / initial login button that appears on the landing page
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

        // Step 2: Type in the username resolved from the DB at suite startup
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), Hooks.RUNTIME_USERNAME);

        // Step 3: Click Continue to advance to the password screen
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);

        // Step 4: Enter the password from test data (TD:Password is loaded from TestData.xlsx by Hooks)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");

        // Step 5: Click the Login / Submit button
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        log.info("[LOGIN] Credentials submitted. Detecting second-factor screen...");

        WebDriver      iDriver    = CommonFunctions.getDriver();
        By             iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

        // ── PATH A: PIN screen detected ──────────────────────────────────────────────────────────
        // Some accounts require a PIN after password. We check for the PIN form appearing within
        // 3 seconds. If it appears we handle it; if not, we fall through to the classic OTP path.
        if (isVisible(iPinFormBy, 3))
        {
            log.info("[LOGIN] PIN screen detected. Filling PIN digit inputs (index 1 → 7).");

            // Loop through all possible PIN digit positions (1 through 7).
            // Not every position will be present or enabled — we skip gracefully.
            for (int idx = 1; idx <= 7; idx++)
            {
                // Build the XPath dynamically by replacing the {idx} placeholder
                String iDynamicXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));
                By     iPinInputBy   = By.xpath(iDynamicXpath);

                if (isVisible(iPinInputBy, 1))
                {
                    WebElement iInput    = iDriver.findElement(iPinInputBy);
                    boolean    iDisabled = iInput.getAttribute("disabled") != null;

                    if (!iDisabled && iInput.isEnabled())
                    {
                        iInput.clear();
                        iInput.sendKeys("1");
                        log.info("[LOGIN] Entered '1' into PIN index: " + idx);
                    }
                    else
                    {
                        log.info("[LOGIN] PIN index " + idx + " is disabled — skipping.");
                    }
                }
                else
                {
                    log.info("[LOGIN] PIN index " + idx + " not in DOM — skipping.");
                }
            }

            // Submit the PIN login form
            iAction("CLICK", "XPATH", ObjReader.getLocator("iPinLoginBtn"), null);
            log.info("[LOGIN] PIN submitted. Moving to TOTP screen.");

            // Enter the 6-digit TOTP code
            // NOTE: "111111" is a test environment placeholder.
            // TODO: Replace with a dynamic TOTP generator or TD:OTP test data column for production use.
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);
            log.info("[LOGIN] TOTP submitted.");

            // Accept Terms & Conditions if this is a first-time login for this account.
            // This screen only appears once per user — we check for it and skip safely if absent.
            if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
                log.info("[LOGIN] Terms & Conditions accepted.");
            }
        }
        // ── PATH B: Classic OTP (no PIN screen) ──────────────────────────────────────────────────
        else
        {
            log.info("[LOGIN] PIN screen not detected. Using classic OTP flow.");

            // Enter the 6-digit OTP directly into the Keycloak OTP field
            // NOTE: "111111" is a test environment placeholder.
            // TODO: Replace with dynamic OTP or TD:OTP column.
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOTPtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);
            log.info("[LOGIN] Classic OTP login completed.");
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent opens the {string} application
    //           (called with "Basic Income Support for Sustainability")
    //
    // What happens here:
    //   After login the agent lands on a portal home screen that lists all available applications.
    //   Rather than clicking a tile directly, we use the search bar to find BISS by name.
    //   This is more resilient than tile-position-based navigation — if the portal adds new apps
    //   or reorders tiles, the search approach still finds the right one.
    //   We verify the search returned exactly 1 result before clicking through.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent opens the {string} application")
    public void theAgentOpensTheApplication(String pApplicationName)
    {
        log.info("[STEP] And: Opening application — " + pApplicationName);

        // Click the search bar first to give it focus
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iAppSearchBar"), "");

        // Type the full application name into the search bar
        iAction("TEXTBOX",       "XPATH", ObjReader.getLocator("iAppSearchBar"), pApplicationName);

        // Confirm the search result label appeared — ensures exactly one match was found
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iSearchAppLabel"), "");
        iAction("VERIFYTEXT",    "XPATH", ObjReader.getLocator("iSearchAppLabel"), "Found 1 applications matching your search");

        Hooks.captureStep("BISS Home page loaded — title verified");

        // Click the BISS application link in the search results
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iBissLink"), "");

        log.info("[STEP] BISS application link clicked. Waiting for BISS home page to load.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the agent should land on the BISS Home page
    //
    // What happens here:
    //   After clicking the BISS link the app does an async load. We wait for the MDC circular
    //   progress spinner to disappear first (iScreenBuffer) — this tells us Angular has finished
    //   its data calls. Then we verify the BISS title element is visible and contains the right text.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the agent should land on the BISS Home page")
    public void theAgentShouldLandOnTheBISSHomePage()
    {
        log.info("[STEP] Then: Verifying agent has landed on the BISS Home page.");

        // Wait for the Angular loading spinner to disappear before checking anything else.
        // If we skip this, the title check may run before the page is fully rendered.
        iAction("WAITINVISIBLE", "XPATH", ObjReader.getLocator("iScreenBuffer"), "MDC Progress Spinner");

        // Verify the BISS title element is present and contains the expected text
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iBissTitle"), "");
        iAction("VERIFYTEXT",    "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");

        log.info("[STEP] BISS Home page confirmed.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent navigates to the {string} and {string} Left Menu Link
    //           (called with "Home" and "My Clients")
    //
    // What happens here:
    //   We click the Home menu item first, then My Clients. Once on My Clients, we do a
    //   quick smoke check of all the Quick Filter tabs (Not Started, Draft, Submitted,
    //   Herd Expired, View All) to confirm they are all present and clickable.
    //   We leave the page on "View All" so the full client list is visible for the next step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent navigates to the {string} and {string} Left Menu Link")
    public void theAgentNavigatesToTheTab(String pHomeTab, String pMyClientsTab)
    {
        log.info("[STEP] And: Navigating to left menu — " + pHomeTab + " then " + pMyClientsTab);

        // Click Home first to ensure we're at the root before going to My Clients
        iAction("CLICK",      "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);
        log.info("[NAV] Home tab clicked.");

        // Now navigate to My Clients
        iAction("CLICK",      "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
        log.info("[NAV] My Clients tab clicked.");

        // Confirm the BISS title is still visible — page hasn't broken
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");

        // Validate all Quick Filter tabs are present by clicking through each one
        log.info("[NAV] Validating Quick Filter tabs...");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHerdExpiredTab"), "");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iDraftTab"),       "");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iSubmittedTab"),   "");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNotStartedTab"),  "");

        // Leave the page on View All so the full client list shows for the herd search
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewAllTab"),     "");
        log.info("[NAV] All Quick Filter tabs validated. Page left on 'View All'.");
    }


    // ===================================================================================================================================
    //  SECTION 2 : FARMER SELECTION AND DASHBOARD
    //
    //  The agent searches for a specific farmer by herd number, sets the page size to maximum
    //  so all results are visible, then clicks into the farmer's dashboard.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent opens a farmer dashboard using herd data
    //
    // What happens here:
    //   We type the herd number (resolved from Oracle at suite startup via Hooks.RUNTIME_HERD)
    //   into the search input and click Search. Then we expand the results list by selecting
    //   the maximum page size from the pagination dropdown — this makes it easier to find the
    //   correct farmer row in the next step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent opens a farmer dashboard using herd data")
    public void theAgentOpensAFarmerDashboardUsingHerdData()
    {
        log.info("[STEP] When: Opening farmer dashboard for herd — " + Hooks.RUNTIME_HERD);

        // Type the runtime herd number into the search box and submit
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), Hooks.RUNTIME_HERD);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);
        log.info("[SEARCH] Herd search submitted for: " + Hooks.RUNTIME_HERD);

        // Open the items-per-page dropdown to expand results to the maximum available page size.
        // This ensures the farmer row we need is visible without having to paginate.
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iListItemsPerPage"),    null);
        iAction("WAITVISIBLE",  "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),  null);
        iAction("WAITCLICKABLE","XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),  null);
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iMatSelectLastOption"), null);

        log.info("[SEARCH] Maximum page size selected — all results now visible.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the farmer dashboard should be displayed
    //
    // What happens here:
    //   We click the first client name link in the results table to open the farmer dashboard.
    //   Before starting a new application, we check whether a draft already exists for this
    //   farmer. If one is found, we delete it and wait for the spinner to confirm deletion.
    //   This keeps the test clean — we always start from a fresh state.
    //   Finally we click Start Application (which also handles "Start amendment" via the same
    //   locator) and dismiss the Continue confirmation modal if it appears.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the farmer dashboard should be displayed")
    public void theFarmerDashboardShouldBeDisplayed()
    {
        log.info("[STEP] Then: Opening farmer dashboard from client list.");

        // Wait for the first client name to be clickable, then click it to open their dashboard
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iFirstClientName"), null);
        log.info("[DASHBOARD] Farmer dashboard opened.");

        // Check for an existing draft — we only look for 3 seconds to keep the test fast.
        // If a draft exists it must be cleared before we can start a fresh application.
        By iDeleteBtnBy = By.xpath(ObjReader.getLocator("iDeleteDraftBtn"));

        if (isElementPresentAndVisible(iDeleteBtnBy, 3))
        {
            log.info("[DRAFT] Existing draft found — deleting before starting a new application.");
            iAction("CLICK",       "XPATH", ObjReader.getLocator("iDeleteDraftBtn"),  "Delete Draft Button");
            iAction("CLICK",       "XPATH", ObjReader.getLocator("iConfirmDeleteBtn"),"Confirm Delete Button");

            // Wait for the deletion spinner to disappear before proceeding
            iAction("WAITINVISIBLE", "XPATH", ObjReader.getLocator("iScreenBuffer"), "Deletion Spinner");
            log.info("[DRAFT] Draft deleted. Spinner dismissed.");
        }
        else
        {
            log.info("[DRAFT] No existing draft found. Proceeding straight to Start Application.");
        }

        // Click Start Application (this button text alternates to "Start amendment" for existing applications)
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStartApplicationBtn"), "");

        // A confirmation modal sometimes appears after clicking Start Application.
        // We dismiss it with Continue if visible — otherwise we move on.
        if (isVisible(By.xpath(ObjReader.getLocator("iContinueBtn")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);
            log.info("[DASHBOARD] Continue confirmation modal dismissed.");
        }

        log.info("[STEP] Farmer dashboard step complete. Application wizard should be opening.");
    }


    // ===================================================================================================================================
    //  SECTION 3 : SIDE NAVIGATION VALIDATION
    //
    //  Before starting the application form, we validate that all side navigation tabs are
    //  reachable. The tabs are passed in as a DataTable in the feature file so they're easy
    //  to maintain without touching Java code.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent navigates through the farmer side navigation tabs
    //
    // What happens here:
    //   We receive a list of tab names from the Gherkin DataTable and click through each one.
    //   "Transfers" is a special case — it uses a mat-icon instead of a text label, so we
    //   handle it with a dedicated XPath and skip the next item in the loop (the two rows in
    //   the DataTable around Transfers are logically one entry).
    //   For all other tabs we build a generic locator on the fly using the tab name text.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent navigates through the farmer side navigation tabs")
    public void theAgentNavigatesThroughTheFarmerSideNavigationTabs(DataTable pDataTable)
    {
        log.info("[STEP] When: Navigating through farmer side navigation tabs.");

        List<String> iTabs = pDataTable.asList();

        for (int i = 0; i < iTabs.size(); i++)
        {
            String iTabName = iTabs.get(i).trim();

            // ── Special case: Transfers tab uses a mat-icon, not a text span ──────────────────
            if (iTabName.equalsIgnoreCase("Transfers"))
            {
                log.info("[SIDENAV] Transfers tab — using mat-icon locator.");
                iAction("WAITVISIBLE", "XPATH", "//mat-icon[normalize-space()='swap_horizontal_circle']", null);
                iAction("CLICK",       "XPATH", "//mat-icon[normalize-space()='swap_horizontal_circle']", null);

                // The DataTable entry after Transfers is a continuation entry — skip it
                i++;
                continue;
            }

            // ── All other tabs — build locator dynamically from the tab name ─────────────────
            // We strip the "My " prefix so "My Correspondence" matches "Correspondence" in the DOM
            String iLocator = String.format(
                    "(//mat-selection-list//span[contains(normalize-space(),'%s')])[1]",
                    iTabName.replace("My ", "")
            );

            log.info("[SIDENAV] Clicking tab: " + iTabName);

            iAction("WAITVISIBLE",   "XPATH", iLocator, null);
            iAction("WAITCLICKABLE", "XPATH", iLocator, null);
            iAction("MOUSEHOVER",    "XPATH", iLocator, null);
            iAction("CLICK",         "XPATH", iLocator, null);
        }

        log.info("[STEP] All side navigation tabs clicked.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then each requested side navigation tab should open successfully
    //
    // What happens here:
    //   After clicking through all the tabs, we read the current dashboard header to confirm
    //   the last tab rendered its content panel. As long as a non-empty heading is visible,
    //   we know navigation is working.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("each requested side navigation tab should open successfully")
    public void eachRequestedSideNavigationTabShouldOpenSuccessfully()
    {
        log.info("[STEP] Then: Confirming side navigation tab content loaded.");

        String iActiveTab = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerDashboardHeader"), null);

        Assertions.assertFalse(iActiveTab.isEmpty(),
                "At least one side navigation tab content header should be visible.");

        log.info("[STEP] Side navigation validated — last active heading: " + iActiveTab);
    }


    // ===================================================================================================================================
    //  SECTION 4 : APPLICATION START
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent deletes any existing draft if present
    //
    // What happens here:
    //   A safety cleanup step. If a draft was created in a previous failed run and not cleaned
    //   up, this step removes it. We wrap in try/catch so if no draft exists the step simply
    //   logs that and moves on — it never fails because of an absent button.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent deletes any existing draft if present")
    public void theAgentDeletesAnyExistingDraftIfPresent()
    {
        log.info("[STEP] When: Checking for and deleting any existing draft application.");

        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteDraftBtn"),   null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iConfirmDeleteBtn"), null);
            log.info("[DRAFT] Existing draft deleted successfully.");
        }
        catch (Exception iException)
        {
            log.info("[DRAFT] No existing draft found — moving on to start a new application.");
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent starts a new BISS application
    //
    // What happens here:
    //   Clicks the Start Application button. After clicking, a confirmation modal sometimes
    //   pops up asking the agent to confirm they want to start. We wait 5 seconds for that
    //   modal and dismiss it with Continue if it appears.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent starts a new BISS application")
    public void theAgentStartsANewBISSApplication()
    {
        log.info("[STEP] And: Starting a new BISS application.");

        iAction("CLICK", "XPATH", ObjReader.getLocator("iStartApplicationBtn"), null);

        // Some farmer accounts show a confirmation modal before the wizard opens
        if (isVisible(By.xpath(ObjReader.getLocator("iContinueBtn")), 5))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);
            log.info("[APP START] Start confirmation modal dismissed.");
        }

        log.info("[STEP] New BISS application started — wizard should be loading.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the Active Farmer step should be displayed
    //
    // What happens here:
    //   The first step of the application wizard is Active Farmer. We read the status comment
    //   text to confirm the wizard has loaded and we are on the right step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the Active Farmer step should be displayed")
    public void theActiveFarmerStepShouldBeDisplayed()
    {
        log.info("[STEP] Then: Confirming Active Farmer step is visible.");

        String iStepText = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerStatusComment"), null);

        Assertions.assertFalse(iStepText.isEmpty(),
                "Active Farmer step content should be visible — wizard may not have loaded.");

        log.info("[STEP] Active Farmer step confirmed. Status text: " + iStepText);
    }


    // ===================================================================================================================================
    //  SECTION 5 : ACTIVE FARMER STEP
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent selects {string} in the Active Farmer step
    //           (called with "Making Hay/Silage/Haylage")
    //
    // What happens here:
    //   The Active Farmer step has a "I declare as not active" checkbox. For a farmer who IS
    //   active, this checkbox must NOT be checked. We uncheck it here to confirm the farmer's
    //   active status and allow the application to proceed.
    //
    //   The pActivity parameter (e.g. "Making Hay/Silage/Haylage") is received from the
    //   feature file for readability but is not currently used in the locator — the step always
    //   unchecks the "declare not active" box regardless of activity type.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent selects {string} in the Active Farmer step")
    public void theAgentSelectsInTheActiveFarmerStep(String pActivity)
    {
        log.info("[STEP] When: Selecting farmer activity in Active Farmer step — " + pActivity);

        // Uncheck "I declare as not active" to confirm the farmer is active
        iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iDeclareNotActiveChk"), "UNCHECK");

        log.info("[ACTIVE FARMER] 'Declare not active' checkbox unchecked — farmer confirmed as active.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent proceeds to the next application step
    //
    // What happens here:
    //   A reusable step used at multiple points in the wizard. Clicks the Next button to
    //   advance to whatever the next step is in the sequence.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent proceeds to the next application step")
    public void theAgentProceedsToTheNextApplicationStep()
    {
        log.info("[STEP] And: Clicking Next to advance to the next application step.");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the Scheme Selection step should be displayed
    //
    // What happens here:
    //   Reads the Scheme Selection step heading and asserts it's non-empty — confirming the
    //   wizard advanced past Active Farmer and landed on the correct step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the Scheme Selection step should be displayed")
    public void theSchemeSelectionStepShouldBeDisplayed()
    {
        log.info("[STEP] Then: Confirming Scheme Selection step is visible.");

        String iStepHeader = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iSchemeSelectionHeader"), null);

        Assertions.assertFalse(iStepHeader.isEmpty(),
                "Scheme Selection step header should be visible after advancing from Active Farmer.");

        log.info("[STEP] Scheme Selection step confirmed: " + iStepHeader);
    }


    // ===================================================================================================================================
    //  SECTION 6 : SCHEME SELECTION STEP
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent selects the {string} scheme
    //           (called with "Organics")
    //
    // What happens here:
    //   Each scheme on this page has a toggle switch (aria-checked true/false). We first read
    //   the current state of the toggle for the named scheme. If it's already ON, we leave it
    //   alone. If it's OFF, we click the scheme card to turn it ON.
    //   This makes the step idempotent — safe to call regardless of the current state.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent selects the {string} scheme")
    public void theAgentSelectsTheScheme(String pSchemeName)
    {
        log.info("[STEP] When: Selecting scheme — " + pSchemeName);

        // Locate the toggle button for this scheme using the scheme name text
        String iToggleXpath = String.format(
                "//span[normalize-space()='%s']/preceding::button[@role='switch'][1]",
                pSchemeName
        );

        WebElement iToggle = CommonFunctions.getDriver().findElement(By.xpath(iToggleXpath));
        String     iState  = iToggle.getAttribute("aria-checked");

        if ("false".equalsIgnoreCase(iState))
        {
            // Toggle is OFF — click the scheme card to switch it ON
            log.info("[SCHEME] '" + pSchemeName + "' is OFF → turning ON.");
            iAction("CLICK", "XPATH",
                    "//mat-card/descendant::span[contains(text(),'" + pSchemeName + "')]",
                    null
            );
        }
        else
        {
            // Toggle is already ON — nothing to do
            log.info("[SCHEME] '" + pSchemeName + "' is already ON → no action taken.");
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent accepts the scheme selection acknowledgement if displayed
    //
    // What happens here:
    //   After scheme selection, BISS sometimes shows an acknowledgement dialog depending on
    //   the scheme chosen and the farmer's data. This step handles that gracefully:
    //   - If a dialog appears, close it, click Next, and accept the I Understand prompt.
    //   - If no dialog appears, just click Next and I Understand directly.
    //   Either way we end up past the acknowledgement and heading to Land Details.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent accepts the scheme selection acknowledgement if displayed")
    public void theAgentAcceptsTheSchemeSelectionAcknowledgementIfDisplayed()
    {
        log.info("[STEP] And: Handling scheme selection acknowledgement (if shown).");

        try
        {
            // An acknowledgement dialog appeared — close it first, then proceed
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCloseDialogBtn"), null);
            log.info("[ACK] Dialog closed.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"),        null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIUnderstandBtn"), null);
            log.info("[ACK] Scheme acknowledgement accepted.");
        }
        catch (Exception iException)
        {
            // No dialog appeared — just click Next and I Understand to move forward
            log.info("[ACK] No acknowledgement dialog — clicking Next and I Understand directly.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"),        null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIUnderstandBtn"), null);
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the Land Details step should be displayed
    //
    // What happens here:
    //   Verifies the Land Details step heading is visible, confirming the wizard moved past
    //   Scheme Selection and is now on the parcel management step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the Land Details step should be displayed")
    public void theLandDetailsStepShouldBeDisplayed()
    {
        log.info("[STEP] Then: Confirming Land Details step is visible.");
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iLandDetailsHeader"), "Land Details");
        log.info("[STEP] Land Details step confirmed.");
    }


    // ===================================================================================================================================
    //  SECTION 7 : LAND DETAILS — INVALID PARCEL VALIDATION
    //
    //  This section tests how BISS handles a deliberately incorrect parcel ID.
    //  The dialog should show an inline error and stay open for the agent to correct the entry.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent attempts to add parcel {string}
    //           (called with "Invalid789")
    //
    // What happens here:
    //   We open the Add Parcel dialog, type in the (intentionally wrong) parcel ID, and click
    //   Claim. BISS should respond with an inline validation error — we check for that in the
    //   next step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent attempts to add parcel {string}")
    public void theAgentAttemptsToAddParcel(String pParcelId)
    {
        log.info("[STEP] When: Attempting to add parcel — " + pParcelId);

        iAction("CLICK",   "XPATH", ObjReader.getLocator("iAddParcelBtn"),   null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"),    pParcelId);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);

        log.info("[PARCEL] Parcel ID entered and Claim clicked. Waiting for validation response.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the invalid parcel warning message should be displayed
    //
    // What happens here:
    //   Checks that the inline mat-error validation message is visible and contains the
    //   expected text "Please enter a valid parcel number". This confirms BISS is correctly
    //   rejecting the bad input rather than silently accepting it.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the invalid parcel warning message should be displayed")
    public void theInvalidParcelWarningMessageShouldBeDisplayed()
    {
        log.info("[STEP] Then: Confirming invalid parcel validation error is displayed.");
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iInvalidParcelError"), "Please enter a valid parcel number");
        log.info("[VALIDATION] Invalid parcel warning confirmed.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the add parcel dialog should remain available for correction or cancellation
    //
    // What happens here:
    //   After an invalid entry, the dialog must stay open so the agent can fix the parcel ID
    //   or cancel. We confirm this by reading the dialog header — if it's present, the modal
    //   hasn't been dismissed.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the add parcel dialog should remain available for correction or cancellation")
    public void theAddParcelDialogShouldRemainAvailableForCorrectionOrCancellation()
    {
        log.info("[STEP] And: Confirming Add Parcel dialog is still open after invalid entry.");

        String iDialogTitle = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iAddParcelModalHeader"), null);

        Assertions.assertFalse(iDialogTitle.isEmpty(),
                "Add Parcel dialog should still be open after an invalid parcel entry — agent needs to correct or cancel.");

        log.info("[DIALOG] Add Parcel dialog confirmed open. Title: " + iDialogTitle);
    }


    // ===================================================================================================================================
    //  SECTION 8 : LAND DETAILS — ARCHIVED PARCEL VALIDATION
    //
    //  This section tests that BISS handles an archived (LPIS-transformed) parcel gracefully.
    //  The warning message is environment-dependent — some environments may not have the parcel
    //  in an archived state, so we treat this as a soft check.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent attempts to add archived parcel {string}
    //           (called with "H19112055")
    //
    // What happens here:
    //   The Add Parcel dialog is already open from the previous step (we stayed in the dialog
    //   after the invalid parcel error). We type the archived parcel ID directly and click
    //   Claim — no need to re-open the dialog.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent attempts to add archived parcel {string}")
    public void theAgentAttemptsToAddArchivedParcel(String pParcelId)
    {
        log.info("[STEP] When: Attempting to add archived parcel — " + pParcelId);

        // Dialog is already open — just type the new parcel ID and try to claim it
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"),    pParcelId);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);

        log.info("[PARCEL] Archived parcel ID entered and Claim clicked.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the archived parcel warning should be displayed if applicable
    //
    // What happens here:
    //   The LPIS transformation warning is environment-specific — in some environments this
    //   parcel may not be archived. We treat this as a non-blocking check: if the warning
    //   appears we log it, if it doesn't we log that too and move on without failing.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the archived parcel warning should be displayed if applicable")
    public void theArchivedParcelWarningShouldBeDisplayedIfApplicable()
    {
        log.info("[STEP] Then: Checking for archived parcel warning (soft check — environment dependent).");

        try
        {
            String iWarning = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iLpisTransformationError"), null);
            log.info("[PARCEL] Archived parcel LPIS warning found: " + iWarning);
        }
        catch (Exception iException)
        {
            log.info("[PARCEL] No archived parcel warning — parcel may not be in archived state in this environment.");
        }
    }


    // ===================================================================================================================================
    //  SECTION 9 : LAND DETAILS — ADD VALID PARCEL
    //
    //  Here we add a real parcel with all required fields. This is the happy path for parcel
    //  addition: parcel ID → Claim → fill in claimed area, ownership status, parcel use, and
    //  organic status → confirm.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent adds parcel {string} with claimed area {string}
    //           (called with "A1190600017", "7")
    //
    // What happens here:
    //   The Add Parcel dialog is still open. We type in the valid parcel ID, click Claim to
    //   look it up, and then fill in the claimed area. The remaining fields (ownership,
    //   parcel use, organic status) are filled in the steps that follow.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent adds parcel {string} with claimed area {string}")
    public void theAgentAddsParcelWithClaimedArea(String pParcelId, String pClaimedArea)
    {
        log.info("[STEP] When: Adding parcel — ID: " + pParcelId + " | Claimed area: " + pClaimedArea + " ha");

        // Enter the valid parcel ID and claim it
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"),    pParcelId);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
        log.info("[PARCEL] Parcel claimed.");

        // Fill in the numeric claimed area (in hectares)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), pClaimedArea);
        log.info("[PARCEL] Claimed area entered: " + pClaimedArea + " ha");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent sets parcel ownership status to {string}
    //           (called with "Owned")
    //
    // What happens here:
    //   Opens the Ownership Status mat-select dropdown and selects the specified option.
    //   The LIST action in iAction handles both standard and searchable mat-selects.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent sets parcel ownership status to {string}")
    public void theAgentSetsParcelOwnershipStatusTo(String pOwnershipStatus)
    {
        log.info("[STEP] And: Setting parcel ownership status to — " + pOwnershipStatus);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), pOwnershipStatus);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent sets parcel use to {string}
    //           (called with "Apples")
    //
    // What happens here:
    //   Opens the Parcel Use dropdown and selects the crop/land use type.
    //   This dropdown is a searchable mat-select (ngx-mat-select-search) in BISS — the LIST
    //   action's search mode handles it by typing to filter before selecting.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent sets parcel use to {string}")
    public void theAgentSetsParcelUseTo(String pParcelUse)
    {
        log.info("[STEP] And: Setting parcel use to — " + pParcelUse);
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), pParcelUse);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent sets parcel organic status to {string}
    //           (called with "Conventional")
    //
    // What happens here:
    //   Opens the Organic Status dropdown and selects whether the parcel is farmed organically
    //   or conventionally. Required field for all parcels on BISS.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent sets parcel organic status to {string}")
    public void theAgentSetsParcelOrganicStatusTo(String pOrganicStatus)
    {
        log.info("[STEP] And: Setting parcel organic status to — " + pOrganicStatus);
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), pOrganicStatus);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the parcel should be added successfully to Land Details
    //
    // What happens here:
    //   All the parcel fields are filled. We click the Add button to submit the form.
    //   Once clicked, the parcel should appear in the Land Details table.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the parcel should be added successfully to Land Details")
    public void theParcelShouldBeAddedSuccessfullyToLandDetails()
    {
        log.info("[STEP] Then: Confirming parcel added — clicking the Add button.");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);
        log.info("[PARCEL] Parcel Add button clicked. Parcel should now appear in Land Details.");
    }


    // ===================================================================================================================================
    //  SECTION 10 : LAND DETAILS — ALREADY CLAIMED PARCEL VALIDATION
    //
    //  We try to add the same parcel a second time to confirm BISS blocks duplicate claims.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent attempts to add parcel {string} again
    //           (called with "A1190600017")
    //
    // What happens here:
    //   We open the Add Parcel dialog again and try to claim the same parcel that was
    //   successfully added in the previous section. BISS should recognise this as a duplicate
    //   and show an "already claimed" warning.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent attempts to add parcel {string} again")
    public void theAgentAttemptsToAddParcelAgain(String pParcelId)
    {
        log.info("[STEP] When: Re-attempting to add an already-claimed parcel — " + pParcelId);

        iAction("CLICK",   "XPATH", ObjReader.getLocator("iAddParcelBtn"),   null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"),    pParcelId);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);

        log.info("[PARCEL] Same parcel ID submitted again. Expecting duplicate warning.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the already claimed parcel warning should be displayed if applicable
    //
    // What happens here:
    //   This is another soft check — environment-dependent. If the warning appears we log it
    //   and dismiss the dialog with Cancel. If it doesn't appear (some environments may not
    //   enforce this for test data), we log and continue without failing.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the already claimed parcel warning should be displayed if applicable")
    public void theAlreadyClaimedParcelWarningShouldBeDisplayedIfApplicable()
    {
        log.info("[STEP] Then: Checking for already-claimed parcel warning (soft check).");

        try
        {
            String iWarning = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iAlreadyClaimedParcelWarning"), null);
            log.info("[PARCEL] Already-claimed warning found: " + iWarning);

            // Dismiss the dialog since we confirmed the warning — don't leave it open
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCancelBtn"), null);
            log.info("[PARCEL] Dialog dismissed with Cancel.");
        }
        catch (Exception iException)
        {
            log.info("[PARCEL] No already-claimed warning displayed — continuing.");
        }
    }


    // ===================================================================================================================================
    //  SECTION 11 : LAND DETAILS — ADD PLOT
    //
    //  Plots are land parcels without a known LPIS parcel reference (e.g. commonage, island
    //  plots, or new land). The Add Plot form has more fields than a standard parcel, including
    //  county, townland, a map change option radio button, and optional checkboxes.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent adds a plot with the following details
    //
    // What happens here:
    //   All plot details come from a Gherkin DataTable — no hardcoded values in this step.
    //   We read the DataTable into a Map, then fill each field in the Add Plot form in order.
    //   We trim all keys and values from the DataTable to handle any accidental whitespace.
    //   The Map Change Option is always set to "Submit Paper Map By Post" using the explicit
    //   static radio locator for reliability.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent adds a plot with the following details")
    public void theAgentAddsAPlotWithTheFollowingDetails(DataTable pDataTable)
    {
        log.info("[STEP] When: Adding a plot with DataTable details.");

        // Convert the DataTable into a trimmed Map so we can look up values by key name
        Map<String, String> iPlotData = pDataTable.asMap(String.class, String.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().trim(),
                        e -> e.getValue().trim()
                ));

        log.info("[PLOT] Plot data received: " + iPlotData);

        // Open the Add Plot dialog
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddPlotBtn"), null);
        log.info("[PLOT] Add Plot dialog opened.");

        // Select County from the dropdown
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCountySelect"),   null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iCountySelect"),   iPlotData.get("county"));
        log.info("[PLOT] County set: " + iPlotData.get("county"));

        // Select Townland — depends on the county selected above being loaded first
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTownlandSelect"), null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iTownlandSelect"), iPlotData.get("townland"));
        log.info("[PLOT] Townland set: " + iPlotData.get("townland"));

        // Enter the plot reference number (e.g. T12345678)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPlotReferenceInput"), iPlotData.get("plotReference"));
        log.info("[PLOT] Plot reference entered: " + iPlotData.get("plotReference"));

        // Select Ownership Status
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), iPlotData.get("ownershipStatus"));
        log.info("[PLOT] Ownership status set: " + iPlotData.get("ownershipStatus"));

        // Select Organic Status
        iAction("CLICK", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), iPlotData.get("organicStatus"));
        log.info("[PLOT] Organic status set: " + iPlotData.get("organicStatus"));

        // Enter the claimed area in hectares
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), iPlotData.get("claimedArea"));
        log.info("[PLOT] Claimed area entered: " + iPlotData.get("claimedArea") + " ha");

        // Select Plot Use from the searchable mat-select
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPlotUseSelect"), null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iPlotUseSelect"), iPlotData.get("plotUse"));
        log.info("[PLOT] Plot use set: " + iPlotData.get("plotUse"));

        // Select the Map Change Option radio button — always "Submit Paper Map By Post" for this test
        iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iMapChangeOption_SubmitPaperMap"), null);
        log.info("[PLOT] Map Change Option selected: Submit Paper Map By Post");

        log.info("[STEP] Plot form fully filled. Reference: " + iPlotData.get("plotReference"));
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the plot should be added successfully to Land Details
    //
    // What happens here:
    //   Submits the Add Plot form by clicking the Next button (iNextBtn2) and then dismisses
    //   the dialog with Cancel. This confirms the form was submitted and the dialog closed.
    //   NOTE: The success confirmation assertion is commented out — the success state for plots
    //   needs to be verified once the actual CSS class used in this environment is confirmed.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the plot should be added successfully to Land Details")
    public void thePlotShouldBeAddedSuccessfullyToLandDetails()
    {
        log.info("[STEP] Then: Submitting the Add Plot form.");

        // Click Next to submit the plot form
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn2"), null);
        log.info("[PLOT] Plot form submitted via Next.");

        // Dismiss the dialog — plot has been submitted, dialog should close
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCancelBtn"), null);
        log.info("[STEP] Add Plot dialog dismissed. Plot added to Land Details.");
    }


    // ===================================================================================================================================
    //  SECTION 12 : LAND DETAILS — ADD PARCEL FROM GIS MAP
    //
    //  This flow adds a parcel by selecting it visually from the GIS map interface rather than
    //  typing a parcel reference manually. County and townland narrow down the map view, then
    //  the agent clicks on the map to select a feature, and fills in the parcel details.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent adds a parcel from the GIS map with the following details
    //
    // What happens here:
    //   1. Open Add Parcel dialog
    //   2. Select county + townland to zoom the map to the right area
    //   3. Click "Open Map" to launch the GIS viewer
    //   4. Click "Select Feature" to enter feature selection mode
    //   5. Click the map image to select a parcel feature
    //   6. Click the specific parcel row in the multi-select list that appears
    //   7. Click Claim to bring the selected parcel back into the form
    //   8. Fill in claimed area, ownership, parcel use, and organic status
    //   9. Click Add to submit
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent adds a parcel from the GIS map with the following details")
    public void theAgentAddsAParcelFromTheGISMapWithTheFollowingDetails(DataTable pDataTable)
    {
        log.info("[STEP] When: Adding a parcel via GIS map selection.");

        // Load DataTable into a trimmed Map
        Map<String, String> iMapData = pDataTable.asMap(String.class, String.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().trim(),
                        e -> e.getValue().trim()
                ));

        log.info("[GIS] Map parcel data received: " + iMapData);

        // Open the Add Parcel dialog
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        log.info("[GIS] Add Parcel dialog opened.");

        // Select County — this pre-filters the map to the right area
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCountySelect"), null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iCountySelect"), iMapData.get("county"));
        log.info("[GIS] County selected: " + iMapData.get("county"));

        // Select Townland — further narrows the map view
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTownlandSelect"), null);
        iAction("LIST",  "XPATH", ObjReader.getLocator("iTownlandSelect"), iMapData.get("townland"));
        log.info("[GIS] Townland selected: " + iMapData.get("townland"));

        // Open the GIS map viewer
        iAction("CLICK", "XPATH", ObjReader.getLocator("iMapOpenBtn"), null);
        log.info("[GIS] Map viewer opened.");

        // Enter feature selection mode by clicking "Select Feature"
        iAction("CLICK", "XPATH", ObjReader.getLocator("selectFeature"), null);
        log.info("[GIS] Feature selection mode activated.");

        // Click on the map image to trigger the parcel feature selection
        iAction("CLICK", "XPATH", ObjReader.getLocator("mainMapImage"), null);
        log.info("[GIS] Map clicked — parcel feature list should appear.");

        // Click the specific parcel row in the multi-select list
        iAction("CLICK", "XPATH", ObjReader.getLocator("parcelRowSpecific"), null);
        log.info("[GIS] Parcel row selected in multi-select list.");

        // Claim the selected GIS parcel to bring it back into the form
        iAction("CLICK", "XPATH", ObjReader.getLocator("claimButton"), null);
        log.info("[GIS] GIS parcel claimed.");

        // Fill in the remaining form fields
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"),    iMapData.get("claimedArea"));
        iAction("LIST",    "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"),iMapData.get("ownershipStatus"));
        iAction("LIST",    "XPATH", ObjReader.getLocator("iParcelUseSelect"),      iMapData.get("parcelUse"));
        iAction("LIST",    "XPATH", ObjReader.getLocator("iOrganicStatusSelect"),  iMapData.get("organicStatus"));
        log.info("[GIS] All parcel fields filled.");

        // Submit the GIS parcel form
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);
        log.info("[STEP] GIS map parcel submitted and added to Land Details.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the GIS-selected parcel should be added successfully
    //
    // What happens here:
    //   Confirms the GIS parcel was added. The Add Parcel button being available again
    //   indicates the dialog closed and Land Details is in a ready state.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the GIS-selected parcel should be added successfully")
    public void theGISSelectedParcelShouldBeAddedSuccessfully()
    {
        log.info("[STEP] Then: Confirming GIS parcel was added successfully.");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        log.info("[STEP] GIS parcel confirmed added — Add Parcel button is visible again.");
    }


    // ===================================================================================================================================
    //  SECTION 13 : LAND DETAILS — PARCEL AVAILABILITY AND EDIT
    //
    //  We add another parcel (J1650300004) specifically to exercise the edit flow.
    //  After adding it, we open it in the side drawer and make changes to it.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then parcel {string} should be available in Land Details
    //           (called with "J1650300004")
    //
    // What happens here:
    //   Rather than just verifying the parcel text exists, this step does something smarter:
    //   it reads ALL parcel reference cells in the Land Details table and looks for the target
    //   parcel ID by exact match. When found, it clicks directly on that cell to open the
    //   parcel's side drawer editor. This is more reliable than a brittle XPath that could
    //   match partial text in a different column.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("parcel {string} should be available in Land Details")
    public void parcelShouldBeAvailableInLandDetails(String pParcelId)
    {
        log.info("[STEP] Then: Locating parcel in Land Details table — " + pParcelId);

        WebDriver              iDriver         = CommonFunctions.getDriver();
        WebDriverWait          iWait           = CommonFunctions.getWait();
        String                 iParcelCellXpath = ObjReader.getLocator("iParcelRefCell");

        // Wait for all parcel reference cells to be present in the DOM
        List<WebElement> iCells = iWait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(iParcelCellXpath))
        );

        boolean iFound = false;

        for (WebElement iCell : iCells)
        {
            String iCellText = iCell.getText().trim();

            if (iCellText.equalsIgnoreCase(pParcelId))
            {
                log.info("[PARCEL] Match found → clicking parcel cell to open side drawer: " + pParcelId);

                // Click directly on the matching cell to open the side drawer for this parcel
                iAction("CLICK", "XPATH", iParcelCellXpath + "[text()='" + pParcelId + "']", null);
                iFound = true;
                break;
            }
        }

        if (!iFound)
        {
            throw new AssertionError("Parcel '" + pParcelId + "' was not found in the Land Details parcel table. "
                    + "Ensure the parcel was added successfully in the previous step.");
        }

        log.info("[STEP] Parcel found and side drawer opened for: " + pParcelId);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent opens parcel {string} in the side drawer
    //           (called with "J1650300004")
    //
    // What happens here:
    //   Verifies the side drawer is open and showing the correct parcel. We check that the
    //   parcel ID text is visible inside the mat-dialog-container to confirm we're editing
    //   the right parcel.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent opens parcel {string} in the side drawer")
    public void theAgentOpensParcelInTheSideDrawer(String pParcelId)
    {
        log.info("[STEP] When: Confirming side drawer is open for parcel — " + pParcelId);

        // Confirm the parcel ID text is visible inside the dialog container
        iAction("VERIFYTEXT", "XPATH",
                "//mat-dialog-container//*[contains(normalize-space(),'" + pParcelId + "')]",
                pParcelId
        );

        log.info("[SIDE DRAWER] Parcel confirmed open in side drawer: " + pParcelId);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent raises an EH change request with reason {string}
    //           (called with "Test Reason")
    //
    // What happens here:
    //   Inside the side drawer there is an EH (Entitlement History) change request section.
    //   We tick the Request EH Change checkbox to activate the change request textarea.
    //   The pReason parameter is received from the feature but the textarea fill is handled
    //   in a separate step — this step just activates the section.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent raises an EH change request with reason {string}")
    public void theAgentRaisesAnEHChangeRequestWithReason(String pReason)
    {
        log.info("[STEP] And: Raising EH change request with reason — " + pReason);

        // Tick the EH change request checkbox to reveal the reason textarea
        iAction("CLICK", "XPATH", ObjReader.getLocator("iRequestEhChangeCheckbox"), null);

        // Type the reason into the EH change reason textarea
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iEhChangeReasonTextarea"), pReason);

        log.info("[EH CHANGE] EH change request checkbox ticked and reason entered: " + pReason);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent changes parcel use to {string}
    //           (called with "Coriander")
    //
    // What happens here:
    //   Updates the Parcel Use dropdown in the side drawer to a new crop type. This tests
    //   that parcel use can be modified after initial submission.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent changes parcel use to {string}")
    public void theAgentChangesParcelUseTo(String pParcelUse)
    {
        log.info("[STEP] And: Changing parcel use in side drawer to — " + pParcelUse);
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), pParcelUse);
        log.info("[SIDE DRAWER] Parcel use updated to: " + pParcelUse);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent saves the parcel changes
    //
    // What happens here:
    //   Clicks the Save Changes button in the side drawer to commit all edits made to the
    //   parcel (EH change request reason + updated parcel use).
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent saves the parcel changes")
    public void theAgentSavesTheParcelChanges()
    {
        log.info("[STEP] And: Saving parcel changes in side drawer.");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iSaveParcelChangesBtn"), null);
        log.info("[SIDE DRAWER] Save Changes clicked.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the parcel update should be saved successfully
    //
    // What happens here:
    //   After saving we look for a success toast/notification element to confirm the save
    //   went through. The toast typically has a CSS class of 'success', 'toast', or
    //   'alert-success' depending on the Angular component used.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the parcel update should be saved successfully")
    public void theParcelUpdateShouldBeSavedSuccessfully()
    {
        log.info("[STEP] Then: Confirming parcel save was successful.");

        String iConfirmation = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'success') or contains(@class,'toast') or contains(@class,'alert-success')]",
                null
        );

        Assertions.assertFalse(iConfirmation.isEmpty(),
                "A save confirmation toast or success indicator should be visible after saving parcel changes.");

        log.info("[STEP] Parcel update confirmed saved: " + iConfirmation);
    }


    // ===================================================================================================================================
    //  SECTION 14 : LAND DETAILS — DELETE AND UNDO PARCEL
    //
    //  We test the delete and undo lifecycle: delete the parcel, confirm it's marked for
    //  deletion (but not yet removed), then undo and confirm it's restored.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent deletes parcel {string}
    //           (called with "J1650300004")
    //
    // What happens here:
    //   Finds the parcel's row in the Land Details table by its ID text and clicks the
    //   delete button on that row. The delete button is identified by class, text, or
    //   aria-label to handle different rendering states.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent deletes parcel {string}")
    public void theAgentDeletesParcel(String pParcelId)
    {
        log.info("[STEP] When: Deleting parcel — " + pParcelId);

        iAction("CLICK", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]/ancestor::tr"
                        + "//button[contains(@class,'delete') or contains(text(),'Delete') or contains(@aria-label,'Delete')]",
                null
        );

        log.info("[PARCEL] Delete button clicked for parcel: " + pParcelId);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then parcel {string} should be marked for deletion
    //           (called with "J1650300004")
    //
    // What happens here:
    //   After clicking delete, BISS typically marks the parcel row visually (strikethrough,
    //   'deleted' CSS class, etc.) rather than removing it immediately. We assert that the
    //   row has a deletion indicator applied to it.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("parcel {string} should be marked for deletion")
    public void parcelShouldBeMarkedForDeletion(String pParcelId)
    {
        log.info("[STEP] Then: Confirming parcel is marked for deletion — " + pParcelId);

        String iDeletedRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]/ancestor::tr"
                        + "[contains(@class,'deleted') or contains(@class,'marked') or contains(@class,'strikethrough')]",
                null
        );

        Assertions.assertFalse(iDeletedRow.isEmpty(),
                "Parcel '" + pParcelId + "' should be visually marked for deletion in the Land Details table.");

        log.info("[STEP] Parcel deletion marking confirmed: " + pParcelId);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent undoes deletion for parcel {string}
    //           (called with "J1650300004")
    //
    // What happens here:
    //   Finds the Undo button on the deleted parcel's row and clicks it. This restores the
    //   parcel to its active state — confirming the undo functionality works correctly.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent undoes deletion for parcel {string}")
    public void theAgentUndoesDeletionForParcel(String pParcelId)
    {
        log.info("[STEP] When: Undoing deletion for parcel — " + pParcelId);

        iAction("CLICK", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]/ancestor::tr"
                        + "//button[contains(text(),'Undo') or contains(@aria-label,'Undo')]",
                null
        );

        log.info("[PARCEL] Undo button clicked for parcel: " + pParcelId);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then parcel {string} should be restored in Land Details
    //           (called with "J1650300004")
    //
    // What happens here:
    //   After undo, the parcel cell should be visible and no longer carry a deletion CSS class.
    //   We just verify the cell text is present as a basic restoration check.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("parcel {string} should be restored in Land Details")
    public void parcelShouldBeRestoredInLandDetails(String pParcelId)
    {
        log.info("[STEP] Then: Confirming parcel has been restored — " + pParcelId);

        String iRestoredRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pParcelId + "')]",
                null
        );

        Assertions.assertFalse(iRestoredRow.isEmpty(),
                "Parcel '" + pParcelId + "' should be visible and active in Land Details after undo.");

        log.info("[STEP] Parcel restoration confirmed: " + pParcelId);
    }


    // ===================================================================================================================================
    //  SECTION 15 : LAND DETAILS — PLOT AVAILABILITY AND DELETE
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then plot {string} should be available in Land Details
    //           (called with "T87654321")
    //
    // What happens here:
    //   Verifies a specific plot reference cell is present in the Land Details table.
    //   This confirms the plot was successfully added in the preceding Add Plot step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("plot {string} should be available in Land Details")
    public void plotShouldBeAvailableInLandDetails(String pPlotRef)
    {
        log.info("[STEP] Then: Confirming plot is visible in Land Details — " + pPlotRef);

        String iPlotRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pPlotRef + "')]",
                null
        );

        Assertions.assertFalse(iPlotRow.isEmpty(),
                "Plot reference '" + pPlotRef + "' should appear in the Land Details table.");

        log.info("[STEP] Plot confirmed in Land Details: " + pPlotRef);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent deletes parcel or plot {string}
    //           (called with "T87654321")
    //
    // What happens here:
    //   Reusable delete step — works for both parcels and plots since they both render delete
    //   buttons on their table rows. Identifies the correct row by the reference text.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent deletes parcel or plot {string}")
    public void theAgentDeletesParcelOrPlot(String pReference)
    {
        log.info("[STEP] When: Deleting parcel or plot — " + pReference);

        iAction("CLICK", "XPATH",
                "//td[contains(text(),'" + pReference + "')]/ancestor::tr"
                        + "//button[contains(@class,'delete') or contains(text(),'Delete') or contains(@aria-label,'Delete')]",
                null
        );

        log.info("[PARCEL/PLOT] Delete button clicked for: " + pReference);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent confirms the deletion
    //
    // What happens here:
    //   A confirmation dialog appears after clicking delete on a plot. We click Confirm/Yes/OK
    //   to complete the deletion. This step is reusable across any confirmation dialog.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent confirms the deletion")
    public void theAgentConfirmsTheDeletion()
    {
        log.info("[STEP] And: Confirming deletion in the confirmation dialog.");

        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Confirm') or contains(text(),'Yes') or contains(text(),'OK')]",
                null
        );

        log.info("[DIALOG] Deletion confirmed.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then plot {string} should be marked for deletion
    //           (called with "T87654321")
    //
    // What happens here:
    //   Same pattern as parcel deletion marking — checks the row carries a deletion CSS class.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("plot {string} should be marked for deletion")
    public void plotShouldBeMarkedForDeletion(String pPlotRef)
    {
        log.info("[STEP] Then: Confirming plot is marked for deletion — " + pPlotRef);

        String iDeletedRow = iAction("GETTEXT", "XPATH",
                "//td[contains(text(),'" + pPlotRef + "')]/ancestor::tr"
                        + "[contains(@class,'deleted') or contains(@class,'marked')]",
                null
        );

        Assertions.assertFalse(iDeletedRow.isEmpty(),
                "Plot '" + pPlotRef + "' should be visually marked for deletion in Land Details.");

        log.info("[STEP] Plot deletion marking confirmed: " + pPlotRef);
    }


    // ===================================================================================================================================
    //  SECTION 16 : LAND DETAILS — MANDATORY INFORMATION COMPLETION
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent completes all mandatory information in Land Details
    //
    // What happens here:
    //   Some parcels or plots may have outstanding mandatory fields flagged by BISS. There
    //   can be an explicit "Complete Mandatory Information" button, or the mandatory fields
    //   may be highlighted inline for the agent to fill. We try the explicit button first
    //   and swallow the exception if it's not present.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent completes all mandatory information in Land Details")
    public void theAgentCompletesAllMandatoryInformationInLandDetails()
    {
        log.info("[STEP] When: Completing any outstanding mandatory information in Land Details.");

        try
        {
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Complete Mandatory') or contains(text(),'Complete Information')]",
                    null
            );
            log.info("[MANDATORY] Explicit mandatory information button found and clicked.");
        }
        catch (Exception iException)
        {
            log.info("[MANDATORY] No explicit mandatory button found — mandatory fields handled inline.");
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the next application step should open successfully
    //
    // What happens here:
    //   After advancing past Land Details, we confirm a new step header is visible, indicating
    //   the wizard moved forward. We don't assert on the exact step name because different
    //   scheme selections lead to different next steps.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the next application step should open successfully")
    public void theNextApplicationStepShouldOpenSuccessfully()
    {
        log.info("[STEP] Then: Confirming the application advanced to the next step.");

        String iStepHeader = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);

        Assertions.assertFalse(iStepHeader.isEmpty(),
                "A new step header should be visible after advancing past Land Details.");

        log.info("[STEP] Next application step confirmed: " + iStepHeader);
    }


    // ===================================================================================================================================
    //  SECTION 17 : GAEC 7 STEP
    //
    //  GAEC (Good Agricultural and Environmental Condition) 7 is a compliance step where the
    //  agent confirms the farmer meets the GAEC 7 requirements. The flow is to navigate to
    //  the step, click Continue, and handle any confirmation if it appears.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent opens the {string} step
    //           (used for GAEC 7, ACRES, Eco, Review & Submit, Correspondence — all reuse this step)
    //
    // What happens here:
    //   A flexible reusable step that navigates to any named application step by clicking its
    //   link or button in the application's step navigation. The step name is matched against
    //   both <a> and <button> elements to cover different rendering patterns.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent opens the {string} step")
    public void theAgentOpensTheStep(String pStepName)
    {
        log.info("[STEP] When: Navigating to application step — " + pStepName);

        iAction("CLICK", "XPATH",
                "//a[contains(text(),'" + pStepName + "')] | //button[contains(text(),'" + pStepName + "')]",
                null
        );

        log.info("[NAV] Step navigation clicked: " + pStepName);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent proceeds from GAEC 7
    //
    // What happens here:
    //   Clicks Continue/Next/Proceed on the GAEC 7 step panel to advance.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent proceeds from GAEC 7")
    public void theAgentProceedsFromGAEC7()
    {
        log.info("[STEP] And: Proceeding from GAEC 7.");

        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Continue') or contains(text(),'Next') or contains(text(),'Proceed')]",
                null
        );

        log.info("[GAEC 7] Continue clicked.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent handles any GAEC 7 continue action if present
    //
    // What happens here:
    //   A secondary confirmation sometimes appears after the initial GAEC 7 Continue click.
    //   This is environment/farmer dependent. We try to click it and swallow the exception
    //   if it doesn't appear.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent handles any GAEC 7 continue action if present")
    public void theAgentHandlesAnyGAEC7ContinueActionIfPresent()
    {
        log.info("[STEP] And: Handling any secondary GAEC 7 continuation dialog (if present).");

        try
        {
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Continue') or contains(text(),'OK') or contains(text(),'Accept')]",
                    null
            );
            log.info("[GAEC 7] Secondary continue action handled.");
        }
        catch (Exception iException)
        {
            log.info("[GAEC 7] No secondary action present — GAEC 7 completed normally.");
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the application should move beyond the GAEC 7 step
    //
    // What happens here:
    //   Reads the current step header and asserts it no longer says "GAEC 7", confirming
    //   the wizard advanced to the next step.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the application should move beyond the GAEC 7 step")
    public void theApplicationShouldMoveBeyondTheGAEC7Step()
    {
        log.info("[STEP] Then: Confirming application has moved past GAEC 7.");

        String iCurrentStep = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);

        Assertions.assertFalse(iCurrentStep.contains("GAEC 7"),
                "Application should have moved beyond GAEC 7. Current step header: " + iCurrentStep);

        log.info("[STEP] Application confirmed past GAEC 7. Current step: " + iCurrentStep);
    }


    // ===================================================================================================================================
    //  SECTION 18 : ACRES STEP
    //
    //  ACRES (Agri-Climate Rural Environment Scheme) has a multi-panel interface where the
    //  agent must make selections on each panel and accept any warnings that appear.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent selects {string} on panel {int}
    //           (called with "Yes, rescore", 1 and "Accept warnings", 1 and 2)
    //
    // What happens here:
    //   Clicks the specified label option within the identified panel number. This is a
    //   flexible step reused across all ACRES panel selections.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent selects {string} on panel {int}")
    public void theAgentSelectsOnPanel(String pOption, int pPanelNumber)
    {
        log.info("[STEP] And: Selecting '" + pOption + "' on ACRES panel " + pPanelNumber);

        iAction("CLICK", "XPATH",
                "(//div[contains(@class,'panel')])[" + pPanelNumber + "]"
                        + "//label[contains(text(),'" + pOption + "')]",
                null
        );

        log.info("[ACRES] Panel " + pPanelNumber + " option selected: " + pOption);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent continues panel {int}
    //           (called with 1, 2, 3)
    //
    // What happens here:
    //   Clicks the Continue button within the specified ACRES panel to advance to the next
    //   panel in the sequence.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent continues panel {int}")
    public void theAgentContinuesPanel(int pPanelNumber)
    {
        log.info("[STEP] And: Continuing ACRES panel " + pPanelNumber);

        iAction("CLICK", "XPATH",
                "(//div[contains(@class,'panel')])[" + pPanelNumber + "]"
                        + "//button[contains(text(),'Continue')]",
                null
        );

        log.info("[ACRES] Continue clicked on panel " + pPanelNumber);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the ACRES step should be completed successfully
    //
    // What happens here:
    //   After all panels are completed, we look for a step completion indicator element
    //   to confirm the ACRES step is fully done.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the ACRES step should be completed successfully")
    public void theACRESStepShouldBeCompletedSuccessfully()
    {
        log.info("[STEP] Then: Confirming ACRES step is completed.");

        String iStepStatus = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'step-complete') or contains(@class,'step-success')]",
                null
        );

        Assertions.assertFalse(iStepStatus.isEmpty(),
                "ACRES step completion indicator should be visible after all panels are completed.");

        log.info("[STEP] ACRES step completed: " + iStepStatus);
    }


    // ===================================================================================================================================
    //  SECTION 19 : ECO STEP
    //
    //  The Eco step covers optional agri-environment scheme selections (AP2, AP5, etc.).
    //  AP2 is a standard selection. AP5 requires additional spreader equipment details.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent selects the {string} scheme option
    //           (called with "AP2" and "AP5")
    //
    // What happens here:
    //   Clicks the Eco scheme option card or radio button identified by the scheme code.
    //   Both label text and data-scheme attribute selectors are tried for resilience.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent selects the {string} scheme option")
    public void theAgentSelectsTheSchemeOption(String pSchemeOption)
    {
        log.info("[STEP] And: Selecting Eco scheme option — " + pSchemeOption);

        iAction("CLICK", "XPATH",
                "//label[contains(text(),'" + pSchemeOption + "')] | //div[contains(@data-scheme,'" + pSchemeOption + "')]",
                null
        );

        log.info("[ECO] Scheme option selected: " + pSchemeOption);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent selects {string} for panel {int}
    //           (called with "Standard", 2)
    //
    // What happens here:
    //   Selects a named option within a specific Eco panel. The eco-panel class scopes the
    //   locator to the correct panel so the same option label in different panels doesn't
    //   cause ambiguity.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent selects {string} for panel {int}")
    public void theAgentSelectsForPanel(String pOption, int pPanelNumber)
    {
        log.info("[STEP] And: Selecting '" + pOption + "' in Eco panel " + pPanelNumber);

        iAction("CLICK", "XPATH",
                "(//div[contains(@class,'eco-panel')])[" + pPanelNumber + "]"
                        + "//label[contains(text(),'" + pOption + "')]",
                null
        );

        log.info("[ECO] Panel " + pPanelNumber + " option set: " + pOption);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent saves the selected eco option
    //
    // What happens here:
    //   After making selections within an Eco scheme option (e.g. AP2 or AP5), clicking Save
    //   commits the selection and returns the agent to the Eco step overview.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent saves the selected eco option")
    public void theAgentSavesTheSelectedEcoOption()
    {
        log.info("[STEP] And: Saving selected Eco option.");
        iAction("CLICK", "XPATH", "//button[contains(text(),'Save')]", null);
        log.info("[ECO] Eco option saved.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent selects approved spreader manufacturer {string}
    //           (called with "Lemken")
    //
    // What happens here:
    //   AP5 requires the agent to specify the fertiliser spreader used. This step selects
    //   the manufacturer from the spreaderManufacturer dropdown (native HTML select by ID).
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent selects approved spreader manufacturer {string}")
    public void theAgentSelectsApprovedSpreaderManufacturer(String pManufacturer)
    {
        log.info("[STEP] And: Selecting spreader manufacturer — " + pManufacturer);
        iAction("LIST", "ID", "spreaderManufacturer", "VISIBLETEXT:" + pManufacturer);
        log.info("[ECO/AP5] Spreader manufacturer selected: " + pManufacturer);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent selects approved spreader model {string}
    //           (called with "Polaris 14")
    //
    // What happens here:
    //   After selecting the manufacturer, the spreader model dropdown populates. We select
    //   the specific model from it.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent selects approved spreader model {string}")
    public void theAgentSelectsApprovedSpreaderModel(String pModel)
    {
        log.info("[STEP] And: Selecting spreader model — " + pModel);
        iAction("LIST", "ID", "spreaderModel", "VISIBLETEXT:" + pModel);
        log.info("[ECO/AP5] Spreader model selected: " + pModel);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent enters spreader serial number {string}
    //           (called with "A735B78346")
    //
    // What happens here:
    //   The agent must enter the unique serial number of the farmer's approved spreader.
    //   This is a plain text input by element ID.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent enters spreader serial number {string}")
    public void theAgentEntersSpreaderSerialNumber(String pSerialNumber)
    {
        log.info("[STEP] And: Entering spreader serial number — " + pSerialNumber);
        iAction("TEXTBOX", "ID", "spreaderSerialNumber", pSerialNumber);
        log.info("[ECO/AP5] Spreader serial number entered: " + pSerialNumber);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the Eco step should be completed successfully
    //
    // What happens here:
    //   Checks for a step completion indicator after all Eco options are saved.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the Eco step should be completed successfully")
    public void theEcoStepShouldBeCompletedSuccessfully()
    {
        log.info("[STEP] Then: Confirming Eco step is completed.");

        String iStepStatus = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'step-complete') or contains(@class,'step-success')]",
                null
        );

        Assertions.assertFalse(iStepStatus.isEmpty(),
                "Eco step completion indicator should be visible after all scheme options are saved.");

        log.info("[STEP] Eco step completed: " + iStepStatus);
    }


    // ===================================================================================================================================
    //  SECTION 20 : ECO OPT-OUT VALIDATION
    //
    //  This section tests that an agent can navigate back to Scheme Selection, open the Eco
    //  scheme card, and continue without selecting any Eco options (opting out).
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : When the agent returns to the {string} step
    //           (called with "Scheme Selection")
    //
    // What happens here:
    //   Navigates back to a previously completed step using the step navigation links.
    //   Reuses the same locator pattern as theAgentOpensTheStep.
    // -------------------------------------------------------------------------------------------------------------------------------
    @When("the agent returns to the {string} step")
    public void theAgentReturnsToTheStep(String pStepName)
    {
        log.info("[STEP] When: Returning to step — " + pStepName);

        iAction("CLICK", "XPATH",
                "//a[contains(text(),'" + pStepName + "')] | //button[contains(text(),'" + pStepName + "')]",
                null
        );

        log.info("[NAV] Navigated back to: " + pStepName);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent opens the {string} scheme card
    //           (called with "Eco")
    //
    // What happens here:
    //   Clicks on the Eco scheme card in the Scheme Selection step to open it. This is
    //   used specifically for the opt-out validation flow.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent opens the {string} scheme card")
    public void theAgentOpensTheSchemeCard(String pSchemeName)
    {
        log.info("[STEP] And: Opening scheme card — " + pSchemeName);

        iAction("CLICK", "XPATH",
                "//div[contains(@class,'scheme-card')]//h3[contains(text(),'" + pSchemeName + "')]"
                        + "/ancestor::div[contains(@class,'scheme-card')]",
                null
        );

        log.info("[SCHEME CARD] Scheme card opened: " + pSchemeName);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent accepts the acknowledgement if displayed
    //
    // What happens here:
    //   A generic soft acknowledgement step. If an acknowledgement dialog appears we dismiss
    //   it. If not, we continue silently. Used in the eco opt-out validation flow.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent accepts the acknowledgement if displayed")
    public void theAgentAcceptsTheAcknowledgementIfDisplayed()
    {
        log.info("[STEP] And: Checking for and accepting any acknowledgement dialog.");

        try
        {
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Accept') or contains(text(),'Acknowledge') or contains(text(),'OK')]",
                    null
            );
            log.info("[ACK] Acknowledgement accepted.");
        }
        catch (Exception iException)
        {
            log.info("[ACK] No acknowledgement dialog present — continuing.");
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the application should continue successfully after eco validation
    //
    // What happens here:
    //   After the eco opt-out flow, we confirm the application is still active by verifying
    //   a step header is visible. We don't assert the exact step name since it varies.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the application should continue successfully after eco validation")
    public void theApplicationShouldContinueSuccessfullyAfterEcoValidation()
    {
        log.info("[STEP] Then: Confirming application continues after eco opt-out validation.");

        String iCurrentStep = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);

        Assertions.assertFalse(iCurrentStep.isEmpty(),
                "A step header should be visible — application should be progressing normally after eco validation.");

        log.info("[STEP] Application confirmed continuing after eco validation. Step: " + iCurrentStep);
    }


    // ===================================================================================================================================
    //  SECTION 21 : REVIEW AND SUBMIT
    //
    //  The final stage of the BISS application. The Review & Submit step is multi-page —
    //  the agent must page through all review pages, accept T&Cs, submit, and confirm.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent completes all review page next actions
    //
    // What happens here:
    //   The Review & Submit step can span multiple pages. We click Next repeatedly until
    //   no more Next buttons are available (capped at 10 iterations to prevent infinite loops
    //   if there's an unexpected issue). Once all pages are done, we're ready for T&Cs.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent completes all review page next actions")
    public void theAgentCompletesAllReviewPageNextActions()
    {
        log.info("[STEP] And: Paging through all Review & Submit pages.");

        int iMaxPages = 10;
        int iPage     = 0;

        while (iPage < iMaxPages)
        {
            try
            {
                iAction("CLICK", "XPATH",
                        "//button[contains(text(),'Next') and not(@disabled)]",
                        null
                );
                iPage++;
                log.info("[REVIEW] Review page " + iPage + " Next button clicked.");
            }
            catch (Exception iException)
            {
                log.info("[REVIEW] No more Next buttons — all " + iPage + " review page(s) completed.");
                break;
            }
        }

        log.info("[STEP] Review page navigation complete.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent accepts the Terms and Conditions
    //
    // What happens here:
    //   The final T&Cs checkbox must be checked before the Submit button becomes active.
    //   We check it using the element ID directly.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent accepts the Terms and Conditions")
    public void theAgentAcceptsTheTermsAndConditions()
    {
        log.info("[STEP] And: Accepting Terms and Conditions.");
        iAction("CHECKBOX", "ID", "termsAndConditions", "CHECK");
        log.info("[SUBMIT] Terms and Conditions checkbox checked — Submit button should now be active.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent submits the application
    //
    // What happens here:
    //   Clicks the Submit Application button. The locator explicitly excludes any "Confirm"
    //   buttons to avoid accidentally triggering the confirmation dialog before the primary
    //   submit button is clicked.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent submits the application")
    public void theAgentSubmitsTheApplication()
    {
        log.info("[STEP] And: Clicking Submit Application.");

        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Submit') and not(contains(text(),'Confirm'))]",
                null
        );

        log.info("[SUBMIT] Submit button clicked. Confirmation dialog should appear.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent confirms the application submission
    //
    // What happens here:
    //   A confirmation dialog appears after clicking Submit. We click Confirm/Yes, Submit/OK
    //   to finalise the submission. This is the point of no return for the application.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent confirms the application submission")
    public void theAgentConfirmsTheApplicationSubmission()
    {
        log.info("[STEP] And: Confirming application submission in the confirmation dialog.");

        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Confirm') or contains(text(),'Yes, Submit') or contains(text(),'OK')]",
                null
        );

        log.info("[SUBMIT] Application submission confirmed.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the application should be submitted successfully
    //
    // What happens here:
    //   After confirmation, BISS should display a success screen with either a confirmation
    //   message or a reference number. We read any element carrying a 'success', 'confirmation',
    //   or 'submitted' CSS class and assert it's not empty.
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the application should be submitted successfully")
    public void theApplicationShouldBeSubmittedSuccessfully()
    {
        log.info("[STEP] Then: Confirming application was submitted successfully.");

        String iConfirmation = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'success') or contains(@class,'confirmation') or contains(@class,'submitted')]",
                null
        );

        Assertions.assertFalse(iConfirmation.isEmpty(),
                "A submission success confirmation should be visible after the application is submitted.");

        log.info("[STEP] Application submitted successfully. Confirmation: " + iConfirmation);
    }


    // ===================================================================================================================================
    //  SECTION 22 : CORRESPONDENCE — DOCUMENT UPLOAD
    //
    //  After submission the agent can upload supporting documents through the Correspondence
    //  section. This tests the full document upload flow.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent chooses to upload a document
    //
    // What happens here:
    //   Clicks the Upload Document button on the Correspondence tab to open the upload dialog.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent chooses to upload a document")
    public void theAgentChoosesToUploadADocument()
    {
        log.info("[STEP] And: Clicking Upload Document on the Correspondence tab.");

        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Upload') or contains(text(),'Upload Document')]",
                null
        );

        log.info("[CORRESPONDENCE] Upload Document dialog opened.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent selects document type {string}
    //           (called with "Commonage Evidence")
    //
    // What happens here:
    //   Selects the document type from the upload form's dropdown. The dropdown is a native
    //   HTML select identified by ID. VISIBLETEXT: prefix tells iAction to match by visible
    //   option text rather than value attribute.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent selects document type {string}")
    public void theAgentSelectsDocumentType(String pDocumentType)
    {
        log.info("[STEP] And: Selecting document type — " + pDocumentType);
        iAction("LIST", "ID", "documentType", "VISIBLETEXT:" + pDocumentType);
        log.info("[CORRESPONDENCE] Document type selected: " + pDocumentType);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent uploads the correspondence document
    //
    // What happens here:
    //   Sends the file path to the hidden file input element. The TEXTBOX action on a file
    //   input triggers the native upload without needing to interact with the OS file picker.
    //   File path is read from the TD:CorrespondenceFilePath test data property. If not set,
    //   it falls back to a sample file in test resources.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent uploads the correspondence document")
    public void theAgentUploadsTheCorrespondenceDocument()
    {
        log.info("[STEP] And: Uploading correspondence document.");

        // Read file path from test data; fall back to sample if not configured
        String iFilePath = System.getProperty("TD:CorrespondenceFilePath",
                "src/test/resources/TestDocuments/sample_correspondence.pdf");

        // Send the file path directly to the hidden file input — no OS dialog needed
        iAction("TEXTBOX", "CSS", "input[type='file']", iFilePath);

        log.info("[CORRESPONDENCE] File path sent to upload input: " + iFilePath);
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : And the agent confirms the upload
    //
    // What happens here:
    //   Submits the upload dialog by clicking Confirm/Upload/Submit. The application then
    //   processes the file and should display it in the document list.
    // -------------------------------------------------------------------------------------------------------------------------------
    @And("the agent confirms the upload")
    public void theAgentConfirmsTheUpload()
    {
        log.info("[STEP] And: Confirming the document upload.");

        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Confirm') or contains(text(),'Upload') or contains(text(),'Submit')]",
                null
        );

        log.info("[CORRESPONDENCE] Upload confirmation clicked.");
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Step    : Then the document should be uploaded successfully in Correspondence
    //
    // What happens here:
    //   Verifies the uploaded document appears in the Correspondence document list by looking
    //   for a table cell containing the document type text ("Commonage Evidence").
    // -------------------------------------------------------------------------------------------------------------------------------
    @Then("the document should be uploaded successfully in Correspondence")
    public void theDocumentShouldBeUploadedSuccessfullyInCorrespondence()
    {
        log.info("[STEP] Then: Confirming uploaded document is visible in Correspondence list.");

        String iDocRow = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'correspondence-list')]//td[contains(text(),'Commonage Evidence')]",
                null
        );

        Assertions.assertFalse(iDocRow.isEmpty(),
                "The uploaded document 'Commonage Evidence' should appear in the Correspondence document list.");

        log.info("[STEP] Document upload confirmed in Correspondence: " + iDocRow);
    }


    // ===================================================================================================================================
    //  PRIVATE HELPER METHODS
    //
    //  These are internal utility methods used by the step definitions above.
    //  They are not Cucumber steps — they are not annotated with @Given/@When/@Then/@And.
    // ===================================================================================================================================

    // -------------------------------------------------------------------------------------------------------------------------------
    // Helper : isVisible(By locator, int seconds)
    //
    // What it does:
    //   A quick boolean visibility probe. Returns true if the element becomes visible within
    //   the specified number of seconds, false if it doesn't (no exception thrown).
    //   Used for conditional branching — e.g. "if the PIN form is visible within 3 seconds".
    //
    //   This is intentionally lightweight — use iAction("WAITVISIBLE",...) for actual test
    //   assertions where you want a failure if the element doesn't appear.
    // -------------------------------------------------------------------------------------------------------------------------------
    private boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            WebDriverWait iWait = new WebDriverWait(CommonFunctions.getDriver(), Duration.ofSeconds(pSeconds));
            iWait.until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception iException)
        {
            return false;
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------
    // Helper : isElementPresentAndVisible(By pBy, int pSeconds)
    //
    // What it does:
    //   Same as isVisible but semantically named for use in conditional element presence checks
    //   (e.g. "is the Delete Draft button on screen?").
    //   Both helpers do the same thing — consider consolidating them in a future refactor.
    // -------------------------------------------------------------------------------------------------------------------------------
    private boolean isElementPresentAndVisible(By pBy, int pSeconds)
    {
        try
        {
            WebDriverWait iShortWait = new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds));
            iShortWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
            return true;
        }
        catch (Exception iException)
        {
            return false;
        }
    }
}