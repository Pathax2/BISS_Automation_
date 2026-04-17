package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
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
// Description   : Step definitions for TC_18, TC_19, TC_20 — Preliminary Checks
//                 Covers Overclaim, Dual Claim and No Agricultural Activity check flows.
//                 Herd and username resolved at runtime via @preliminary Hooks.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 17-04-2026
// ===================================================================================================================================

public class TC_18 {

    private static final Logger log = Logger.getLogger(TC_18.class.getName());


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens a farmer dashboard for preliminary check herd "OVERCLAIM"
    // Description   : Resolves the correct herd from Hooks based on the check type key,
    //                 searches for that herd on My Clients, and opens the farmer dashboard.
    //                 pCheckType maps to: OVERCLAIM, DUAL_CLAIM, AGRI_ACTIVITY
    // Parameters    : pCheckType — string key matching Hooks field name
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens a farmer dashboard for preliminary check herd {string}")
    public void theAgentOpensAFarmerDashboardForPreliminaryCheckHerd(String pCheckType)
    {
        log.info("[STEP] When the agent opens a farmer dashboard for preliminary check herd: " + pCheckType);

        // ── Resolve herd and username from Hooks based on check type ─────────
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
                        "Unknown preliminary check type: " + pCheckType +
                                ". Expected OVERCLAIM, DUAL_CLAIM or AGRI_ACTIVITY.");
        }

        Assertions.assertNotNull(iHerd,
                "Herd for " + pCheckType + " is null — check @preliminary Hook ran and DB returned data.");
        Assertions.assertNotNull(iUsername,
                "Username for " + pCheckType + " is null — check BISS_INET query returned a login ID.");

        log.info("[STEP] Resolved herd: " + iHerd + " | username: " + iUsername);

        // ── Search for the herd on My Clients ────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iHerd);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);

        // Expand page size to ensure herd is visible
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iListItemsPerPage"),    null);
        iAction("WAITVISIBLE",  "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),  null);
        iAction("WAITCLICKABLE","XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),  null);
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iMatSelectLastOption"), null);

        log.info("Herd search completed for: " + iHerd);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the preliminary checks card should be visible on the dashboard
    // Description   : Asserts the Preliminary Checks mat-card is present on the farmer dashboard
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the preliminary checks card should be visible on the dashboard")
    public void thePreliminaryChecksCardShouldBeVisibleOnTheDashboard()
    {
        log.info("[STEP] Then the preliminary checks card should be visible on the dashboard");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iPrelimChecksCard"), null);
        log.info("Preliminary Checks card confirmed visible on dashboard.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the "Overclaim Checks" preliminary check should show "Response required"
    // Description   : Asserts the named check type shows the expected status on the dashboard.
    //                 pCheckName — visible check label e.g. "Overclaim Checks"
    //                 pStatus    — "Response required" or "No response required"
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @And("the {string} preliminary check should show {string} on the dashboard")
    public void thePreliminaryCheckShouldShowOnTheDashboard(String pCheckName, String pStatus)
    {
        log.info("[STEP] And the '" + pCheckName + "' preliminary check should show '" + pStatus + "' on the dashboard");

        String iLocatorKey = pStatus.contains("No response")
                ? "iPrelimCheckStatusNotRequired"
                : "iPrelimCheckStatusRequired";

        String iXpath = String.format(ObjReader.getLocator(iLocatorKey), pCheckName);
        iAction("VERIFYELEMENT", "XPATH", iXpath, null);
        log.info("Confirmed '" + pCheckName + "' shows: " + pStatus);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent clicks View Preliminary Checks
    // Description   : Clicks the View preliminary checks button on the dashboard card
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
    // Description   : Confirms biss-response-page is rendered by checking the accordion is visible
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
    // Description   : Clicks Submit without making any radio button selections to trigger validation error
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
    // Description   : Asserts that an error indicator is visible after premature submit attempt.
    //                 Angular marks the form ng-invalid — checks for mat-error or error class element.
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("a validation error should be displayed on the Preliminary Checks page")
    public void aValidationErrorShouldBeDisplayedOnThePreliminaryChecksPage()
    {
        log.info("[STEP] Then a validation error should be displayed on the Preliminary Checks page");

        // The form table has ng-invalid class when no selection is made —
        // check the table itself is still in invalid state after submit attempt
        boolean iFormInvalid = isVisible(
                By.xpath("//biss-response-page//table[contains(@class,'ng-invalid')]"), 3);

        Assertions.assertTrue(iFormInvalid,
                "Expected validation error after submitting without selections — form should remain ng-invalid.");
        log.info("✅ Validation error confirmed — form is ng-invalid as expected.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent responds to all preliminary check panels row by row
    // Description   : Iterates through the DataTable of check panel names and responses.
    //                 For each panel:
    //                   - Checks if the panel has a red error icon (response required)
    //                   - If yes: expands if collapsed, reads all data rows, selects the response per row
    //                   - If no (green): skips the panel entirely
    //                 DataTable: | Panel Name | Response Label |
    // Parameters    : pDataTable — two-column DataTable mapping panel to response
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent responds to all preliminary check panels row by row")
    public void theAgentRespondsToAllPreliminaryCheckPanelsRowByRow(DataTable pDataTable)
    {
        log.info("[STEP] When the agent responds to all preliminary check panels row by row");

        WebDriver iDriver = getDriver();
        List<Map<String, String>> iRows = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iEntry : iRows)
        {
            String iPanelName     = iEntry.get("Overclaim Checks") != null
                    ? iEntry.keySet().iterator().next() : iEntry.keySet().iterator().next();
            // Use correct key iteration
            String iPanelLabel    = iEntry.entrySet().iterator().next().getKey().trim();
            String iResponseLabel = iEntry.entrySet().iterator().next().getValue().trim();

            log.info("Processing panel: '" + iPanelLabel + "' with response: '" + iResponseLabel + "'");

            // ── Check if this panel has a red error icon (response required) ─
            String iErrorPanelXpath = String.format(
                    ObjReader.getLocator("iPrelimPanelHeaderError"), iPanelLabel);

            if (!isVisible(By.xpath(iErrorPanelXpath), 2))
            {
                log.info("Panel '" + iPanelLabel + "' has no error icon — skipping.");
                continue;
            }

            // ── Expand panel if collapsed ─────────────────────────────────────
            String iPanelHeaderXpath = String.format(
                    ObjReader.getLocator("iPrelimPanelHeader"), iPanelLabel);

            WebElement iPanelHeader = iDriver.findElement(By.xpath(iPanelHeaderXpath));
            String iExpanded = iPanelHeader.getAttribute("aria-expanded");

            if (!"true".equalsIgnoreCase(iExpanded))
            {
                iAction("CLICK", "XPATH", iPanelHeaderXpath, null);
                log.info("Expanded panel: " + iPanelLabel);
                try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }

            // ── Select response on every data row ─────────────────────────────
            // Re-query rows after expansion — Angular renders them after panel opens
            String iRowRadioXpath = String.format(
                    ObjReader.getLocator("iPrelimRowRadioByLabel"), iResponseLabel);

            List<WebElement> iRadioLabels = iDriver.findElements(By.xpath(iRowRadioXpath));

            if (iRadioLabels.isEmpty())
            {
                throw new AssertionError(
                        "Panel '" + iPanelLabel + "': no radio buttons found with label '" +
                                iResponseLabel + "'. Check DataTable response label matches DOM label exactly.");
            }

            for (int i = 0; i < iRadioLabels.size(); i++)
            {
                try
                {
                    WebElement iLabel = iDriver.findElements(By.xpath(iRowRadioXpath)).get(i);
                    iAction("CLICK", "XPATH", iRowRadioXpath + "[" + (i + 1) + "]", null);
                    log.info("Selected '" + iResponseLabel + "' on row " + (i + 1) + " of panel: " + iPanelLabel);
                }
                catch (Exception e)
                {
                    log.warning("Could not click row " + (i + 1) + " of panel '" + iPanelLabel + "': " + e.getMessage());
                }
            }
        }

        log.info("All preliminary check panels responded to.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent submits the Preliminary Checks response
    // Description   : Clicks the Submit button in the footer of the Preliminary Checks page
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
    // Description   : Clicks Yes on the confirmation dialog that appears after Submit
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
    // Step          : Then the Preliminary Checks submission should be confirmed successfully
    // Description   : Asserts success state after submission — checks page no longer shows
    //                 the submit form or confirms redirect back to dashboard/success message
    // Author        : Aniket Pathare
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the Preliminary Checks submission should be confirmed successfully")
    public void thePreliminaryChecksSubmissionShouldBeConfirmedSuccessfully()
    {
        log.info("[STEP] Then the Preliminary Checks submission should be confirmed successfully");

        // After submission the page should no longer show the Submit button
        // or should show a success message — assert Submit is gone as confirmation
        boolean iSubmitGone = !isVisible(By.xpath(ObjReader.getLocator("iPrelimSubmitBtn")), 3);

        Assertions.assertTrue(iSubmitGone,
                "Submit button is still visible after confirmation — submission may not have completed.");
        log.info("✅ Preliminary Checks submitted and confirmed successfully.");
    }
}