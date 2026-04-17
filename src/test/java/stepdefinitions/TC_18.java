package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilities.ObjReader;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.*;

// ===================================================================================================================================
// File          : TC_18.java
// Package       : stepdefinitions
// Description   : Step definitions shared across TC_18 (Overclaim), TC_19 (Dual Claim)
//                 and TC_20 (No Agricultural Activity) Preliminary Checks automation.
//
//                 Herd numbers and agent usernames are resolved at runtime from:
//                   BISS_DATA — vwbs_land_validation (LVS_DESC = 'Pending')
//                   BISS_INET — tdcr_user_info / tdco_customer_asscs
//                 via the @Before("@preliminary") hook in Hooks.java.
//
//                 All locators are externalised to ObjectRepository.properties.
//                 All iAction calls follow framework conventions:
//                   local vars: i prefix | params: p prefix
//                   ObjReader.getLocator("key") — never raw XPath inline
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 17-04-2026
// ===================================================================================================================================

public class TC_18
{
    private static final Logger log = Logger.getLogger(TC_18.class.getName());


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens a farmer dashboard for preliminary check herd "OVERCLAIM"
    // Description   : Resolves the runtime herd from Hooks based on pCheckType key,
    //                 then searches for that herd on My Clients and navigates to the dashboard.
    //                 pCheckType accepted values: OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY
    //                 Each maps to a pair of static fields in Hooks populated by @preliminary hook.
    // Parameters    : pCheckType — string key identifying which check type herd to open
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens a farmer dashboard for preliminary check herd {string}")
    public void theAgentOpensAFarmerDashboardForPreliminaryCheckHerd(String pCheckType)
    {
        log.info("[STEP] When the agent opens a farmer dashboard for preliminary check herd: " + pCheckType);

        // ── Resolve herd and username from Hooks based on check type ──────────────────────────
        String iHerd;
        String iUsername;

        switch (pCheckType.trim().toUpperCase())
        {
            case "OVERCLAIM":
                iHerd     = Hooks.OVERCLAIM_HERD;
                iUsername = Hooks.OVERCLAIM_USERNAME;
                break;
            case "DUAL_CLAIM":
                iHerd     = Hooks.DUAL_CLAIM_HERD;
                iUsername = Hooks.DUAL_CLAIM_USERNAME;
                break;
            case "AGRI_ACTIVITY":
                iHerd     = Hooks.AGRI_ACTIVITY_HERD;
                iUsername = Hooks.AGRI_ACTIVITY_USERNAME;
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown preliminary check type: '" + pCheckType + "'. " +
                                "Expected one of: OVERCLAIM, DUAL_CLAIM, AGRI_ACTIVITY.");
        }

        Assertions.assertNotNull(iHerd,
                "Herd for check type '" + pCheckType + "' is null — " +
                        "verify @preliminary hook ran and BISS_DATA returned a Pending row for this check type.");
        Assertions.assertNotNull(iUsername,
                "Username for check type '" + pCheckType + "' is null — " +
                        "verify BISS_INET returned a login ID for herd: " + iHerd);

        log.info("[STEP] Resolved herd=" + iHerd + " | username=" + iUsername + " for check type=" + pCheckType);

        // ── Search for the herd on My Clients ──────────────────────────────────────────────────
        // Uses the same herd search locators as TC_03 — herdSearchInput / herdSearchBtn
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iHerd);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);

        // Expand page size so the herd row is visible in the list
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iListItemsPerPage"),    null);
        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),  null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),  null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iMatSelectLastOption"), null);

        log.info("[STEP] Herd search completed for: " + iHerd);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the preliminary checks card should be visible on the dashboard
    // Description   : Asserts the biss-portal-preliminary-checks mat-card is present and
    //                 visible on the farmer dashboard after navigating to the herd.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the preliminary checks card should be visible on the dashboard")
    public void thePreliminaryChecksCardShouldBeVisibleOnTheDashboard()
    {
        log.info("[STEP] Then the preliminary checks card should be visible on the dashboard");

        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iPrelimChecksCard"), null);
        log.info("Preliminary Checks card confirmed visible on farmer dashboard.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the "Overclaim Checks" preliminary check should show "Response required"
    // Description   : Asserts the named check type row on the dashboard shows the expected status.
    //                 pCheckName — visible label e.g. "Overclaim Checks", "Dual Claim Checks"
    //                 pStatus    — "Response required" or "No response required"
    //                 Locator uses %s substitution on the dashboard status XPath patterns.
    // Parameters    : pCheckName — check type label as shown on the dashboard
    //                 pStatus    — expected status string
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @And("the {string} preliminary check should show {string} on the dashboard")
    public void thePreliminaryCheckShouldShowOnTheDashboard(String pCheckName, String pStatus)
    {
        log.info("[STEP] And the '" + pCheckName + "' preliminary check should show '" + pStatus + "' on the dashboard");

        // Choose locator key based on expected status
        String iLocatorKey = pStatus.toLowerCase().contains("no response")
                ? "iPrelimCheckStatusNotRequired"
                : "iPrelimCheckStatusRequired";

        // Substitute the check name into the generic %s locator
        String iXpath = String.format(ObjReader.getLocator(iLocatorKey), pCheckName);

        iAction("VERIFYELEMENT", "XPATH", iXpath, null);
        log.info("✅ Confirmed '" + pCheckName + "' shows: " + pStatus);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent clicks View Preliminary Checks
    // Description   : Clicks the "View preliminary checks" button on the dashboard card.
    //                 Navigates to biss-response-page with the prelims-accordion.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent clicks View Preliminary Checks")
    public void theAgentClicksViewPreliminaryChecks()
    {
        log.info("[STEP] When the agent clicks View Preliminary Checks");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iPrelimViewChecksBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iPrelimViewChecksBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iPrelimViewChecksBtn"), null);
        log.info("Navigated to Preliminary Checks response page.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Preliminary Checks response page should be displayed
    // Description   : Confirms the Preliminary Checks response page is rendered by asserting
    //                 the prelims-accordion inside biss-response-page is visible.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the Preliminary Checks response page should be displayed")
    public void thePreliminaryChecksResponsePageShouldBeDisplayed()
    {
        log.info("[STEP] Then the Preliminary Checks response page should be displayed");

        iAction("VERIFYELEMENT", "XPATH",
                "//biss-response-page//mat-accordion[contains(@class,'prelims-accordion')]", null);
        log.info("Preliminary Checks response page confirmed.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent clicks Submit on the Preliminary Checks page without selecting any response
    // Description   : Clicks the Submit button before making any radio button selection.
    //                 This is the negative test — Angular should keep the form ng-invalid
    //                 and display a validation error.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent clicks Submit on the Preliminary Checks page without selecting any response")
    public void theAgentClicksSubmitWithoutSelectingAnyResponse()
    {
        log.info("[STEP] When the agent clicks Submit without selecting any response — negative test");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        log.info("Submit clicked without selections — expecting validation error.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then a validation error should be displayed on the Preliminary Checks page
    // Description   : Asserts the form is still in an invalid state after the premature submit.
    //                 Angular sets ng-invalid on the table when required radio groups have no
    //                 selection. Checks for the ng-invalid class on the biss-table element.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("a validation error should be displayed on the Preliminary Checks page")
    public void aValidationErrorShouldBeDisplayedOnThePreliminaryChecksPage()
    {
        log.info("[STEP] Then a validation error should be displayed on the Preliminary Checks page");

        // Angular marks the table ng-invalid when any required radio group has no selection
        // This is the most reliable indicator — no separate error message element exists
        boolean iFormInvalid = isVisible(
                By.xpath(ObjReader.getLocator("iPrelimFormInvalid")), 3);

        Assertions.assertTrue(iFormInvalid,
                "Expected the form to remain ng-invalid after submitting without selections. " +
                        "The table should have class 'ng-invalid' on the biss-table element.");

        log.info("✅ Validation error confirmed — form is ng-invalid as expected.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent responds to all preliminary check panels row by row
    // Description   : Reads the DataTable of panel names and response labels.
    //                 For each panel in the DataTable:
    //                   1. Checks if the panel has a red error icon (response required)
    //                      using iPrelimPanelHeaderError locator — if green, skips the panel.
    //                   2. Expands the panel if it is collapsed (aria-expanded = false).
    //                   3. Waits for panel content to render after expansion.
    //                   4. Finds all row-level radio button labels matching the response text
    //                      inside tbody (not the header bulk-select row).
    //                   5. Clicks each matching label one by one — re-queries DOM before each
    //                      click to avoid StaleElementReferenceException.
    //                 DataTable format (header row required):
    //                   | Panel Name                      | Response          |
    //                   | Overclaim Checks                | Accept            |
    //                   | Dual Claim Checks               | No, Keep it       |
    //                   | No Agricultural Activity Checks | Reject            |
    // Parameters    : pDataTable — DataTable with headers "Panel Name" and "Response"
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent responds to all preliminary check panels row by row")
    public void theAgentRespondsToAllPreliminaryCheckPanelsRowByRow(DataTable pDataTable)
    {
        log.info("[STEP] When the agent responds to all preliminary check panels row by row");

        WebDriver iDriver = getDriver();

        // asMaps uses the first row as header keys — DataTable must have | Panel Name | Response |
        List<Map<String, String>> iTableRows = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iEntry : iTableRows)
        {
            String iPanelLabel    = iEntry.get("Panel Name").trim();
            String iResponseLabel = iEntry.get("Response").trim();

            log.info("[STEP] Panel: '" + iPanelLabel + "' | Response: '" + iResponseLabel + "'");

            // ── 1. Check if this panel needs a response (red error icon) ─────────────────────
            String iErrorPanelXpath = String.format(
                    ObjReader.getLocator("iPrelimPanelHeaderError"), iPanelLabel);

            if (!isVisible(By.xpath(iErrorPanelXpath), 2))
            {
                log.info("[STEP] Panel '" + iPanelLabel + "' has green icon — no response required. Skipping.");
                continue;
            }

            // ── 2. Expand panel if collapsed ─────────────────────────────────────────────────
            String iPanelHeaderXpath = String.format(
                    ObjReader.getLocator("iPrelimPanelHeader"), iPanelLabel);

            WebElement iPanelHeader = iDriver.findElement(By.xpath(iPanelHeaderXpath));
            String iAriaExpanded    = iPanelHeader.getAttribute("aria-expanded");

            if (!"true".equalsIgnoreCase(iAriaExpanded))
            {
                iAction("CLICK", "XPATH", iPanelHeaderXpath, null);
                log.info("[STEP] Expanded panel: " + iPanelLabel);

                // Wait for Angular to render panel content after expansion
                try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }

            // ── 3. Find all row-level radio labels matching the response ──────────────────────
            // Scoped to tbody to exclude the header bulk-select radio group
            String iRowRadioXpath = String.format(
                    ObjReader.getLocator("iPrelimRowRadioByLabel"), iResponseLabel);

            List<WebElement> iRadioLabels = iDriver.findElements(By.xpath(iRowRadioXpath));

            if (iRadioLabels.isEmpty())
            {
                throw new AssertionError(
                        "Panel '" + iPanelLabel + "': no row-level radio buttons found with label '" +
                                iResponseLabel + "'. " +
                                "Verify the response label matches the DOM label text exactly " +
                                "(case-sensitive, including commas and spaces).");
            }

            log.info("[STEP] Found " + iRadioLabels.size() + " row(s) to respond to in panel: " + iPanelLabel);

            // ── 4. Click each row radio label — re-query before each to avoid stale refs ──────
            for (int i = 0; i < iRadioLabels.size(); i++)
            {
                // Re-query the list on every iteration — Angular may re-render rows
                String iIndexedXpath = "(" + iRowRadioXpath + ")[" + (i + 1) + "]";

                iAction("WAITVISIBLE",   "XPATH", iIndexedXpath, null);
                iAction("WAITCLICKABLE", "XPATH", iIndexedXpath, null);
                iAction("CLICK",         "XPATH", iIndexedXpath, null);
                log.info("[STEP] Selected '" + iResponseLabel + "' on row " + (i + 1) + " of panel: " + iPanelLabel);
            }
        }

        log.info("[STEP] All preliminary check panels responded to.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent submits the Preliminary Checks response
    // Description   : Clicks the Submit button in the footer of the Preliminary Checks page
    //                 after all required radio buttons have been selected.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent submits the Preliminary Checks response")
    public void theAgentSubmitsThePreliminaryChecksResponse()
    {
        log.info("[STEP] When the agent submits the Preliminary Checks response");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        log.info("Preliminary Checks Submit clicked.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent confirms the Preliminary Checks submission
    // Description   : Clicks Yes on the confirmation dialog that appears after Submit.
    //                 The dialog is a mat-dialog-container with a Yes button.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent confirms the Preliminary Checks submission")
    public void theAgentConfirmsThePreliminaryChecksSubmission()
    {
        log.info("[STEP] Then the agent confirms the Preliminary Checks submission");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iPrelimConfirmDialogYesBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iPrelimConfirmDialogYesBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iPrelimConfirmDialogYesBtn"), null);
        log.info("Confirmation dialog — Yes clicked.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the Preliminary Checks submission should be confirmed successfully
    // Description   : Asserts the submission completed successfully.
    //                 After confirmation the Submit button should no longer be visible —
    //                 its disappearance is the most reliable indicator the page transitioned.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @And("the Preliminary Checks submission should be confirmed successfully")
    public void thePreliminaryChecksSubmissionShouldBeConfirmedSuccessfully()
    {
        log.info("[STEP] And the Preliminary Checks submission should be confirmed successfully");

        // After submission and confirmation the Submit button should disappear
        // as the page transitions away from the response form
        boolean iSubmitGone = !isVisible(By.xpath(ObjReader.getLocator("iPrelimSubmitBtn")), 5);

        Assertions.assertTrue(iSubmitGone,
                "Submit button is still visible after confirmation — " +
                        "the Preliminary Checks submission may not have completed successfully.");

        log.info("✅ Preliminary Checks submitted and confirmed successfully.");
        Hooks.captureStep("Preliminary Checks — submission confirmed successfully");
    }
}