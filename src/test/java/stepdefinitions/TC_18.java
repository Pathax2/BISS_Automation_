// ===================================================================================================================================
// File          : TC_18.java
// Package       : stepdefinitions
// Description   : Step definitions shared across TC_18 (Overclaim), TC_19 (Dual Claim)
//                 and TC_20 (No Agricultural Activity) Preliminary Checks automation.
//
//                 Herd numbers and agent usernames are resolved at runtime from:
//                   BISS_DATA - vwbs_land_validation (LVS_DESC = 'Pending')
//                   BISS_INET - tdcr_user_info / tdco_customer_asscs
//                 via the @Before("@preliminary") hook in Hooks.java.
//
//                 Retry flow for theAgentOpensAFarmerDashboardForPreliminaryCheckHerd:
//
//                   For each attempt (up to MAX_HERD_RETRIES):
//                     1. Search current herd on My Clients
//                     2. If 0 rows returned:
//                          -> logout -> DB re-query at next offset -> INET validate
//                          -> re-login if agent changed -> update Hooks -> retry
//                     3. If row found but marked "Herd expired":
//                          -> same recovery path
//                     4. Row found and not expired -> click through to farmer dashboard
//                     5. Navigate to Preliminary Checks tab
//                     6. If tab shows no records:
//                          -> same recovery path
//                     7. Tab has records -> iFound = true -> break -> proceed
//
//                 All three failure modes go through the identical recovery path - same as TC_03 for consistency.
//                 All locators externalised to ObjectRepository.properties.
//
//                 Playwright migration notes (behaviour is unchanged):
//                   - getDriver().findElements(By.xpath(x))      -> countOf(x) / rowsOf(x): an immediate count or a
//                     snapshot list, exactly like Selenium findElements (no waiting).
//                   - WebElement.getAttribute(...)               -> UiHelpers.getAttribute(locator, name)
//                   - WebElement.getText()                       -> Locator.innerText()
//                   - input.getAttribute("value")                -> UiHelpers.inputValue(locator)
//                   - Row-scoped ".//..." XPaths                 -> row.locator("xpath=.//...") - Playwright resolves a
//                     leading dot against the row, so the ObjectRepository values are used unchanged.
//                   - JavascriptExecutor clear + sendKeys + input/change events -> UiHelpers.setValueByJs(locator, value),
//                     which sets the value and fires input, change and blur so Angular reactive forms pick it up.
//                   - JavaScript click on a mat-select           -> UiHelpers.jsClick(locator)
//                   - Thread.sleep(ms)                           -> pause(ms) (page.waitForTimeout, same duration)
//                   - WebDriverWait + visibilityOfElementLocated -> UiHelpers.isVisible(locator, seconds)
//                   - getDriver().navigate().to(url)             -> UiHelpers.navigateTo(url)
//                   - PIN slots: clear() + sendKeys("1")         -> fill("") + pressSequentially("1"), disabled slots skipped
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 17-04-2026 | Updated : 20-04-2026
// Updated       : 18-09-2026 - Migrated to Playwright (no Selenium API left; same steps, same locators, same flow)
// ===================================================================================================================================

package stepdefinitions;

import com.microsoft.playwright.Locator;
import commonFunctions.CommonFunctions;
import commonFunctions.UiHelpers;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import utilities.ObjReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;

public class TC_18
{
    private static final Logger log = Logger.getLogger(TC_18.class.getName());


    // -- "Herd expired" span inside the expired column cell of a data row ------------------------------------------
    private static final String EXPIRED_SPAN_XPATH =
            "//tbody[contains(@class,'mdc-data-table__content')]"
                    + "//tr[contains(@class,'client-list-element-row')]"
                    + "//td[contains(@class,'cdk-column-expired')]"
                    + "//span[normalize-space()='Herd expired']";

    // -- Preliminary Checks tab in the farmer dashboard ------------------------------------------------------------
    private static final String PRELIM_TAB_XPATH = "//span[@class='mdc-tab__text-label'][normalize-space()='Preliminary Checks']";

    // -- Data rows inside the Preliminary Checks table -------------------------------------------------------------
    private static final String PRELIM_TABLE_ROW_XPATH = ".//td[contains(@class,'cdk-column-preliminaryChecks')][contains(normalize-space(),'Response required')]";

    // -- Empty state message on the Preliminary Checks tab ---------------------------------------------------------
    private static final String PRELIM_NO_RECORDS_XPATH = "//*[contains(normalize-space(),'no records to display')]";

    // -- Breadcrumb / left-menu back to My Clients -----------------------------------------------------------------
    private static final String MY_CLIENTS_BACK_XPATH = "//breadcrumb//a[contains(.,'My Clients')] | //a[normalize-space()='My Clients']";


// ===================================================================================================================================
//  FARMER DASHBOARD - PRELIMINARY CHECK HERD
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens a farmer dashboard for preliminary check herd {string}
    // Description   : Resolves herd + username from Hooks, searches My Clients, validates the row
    //                 is not expired, clicks through to the dashboard, and confirms the Preliminary
    //                 Checks tab has records.
    //
    //                 All three failure modes trigger the SAME recovery path:
    //                   Failure 1 - 0 rows in My Clients
    //                   Failure 2 - Row found but "Herd expired"
    //                   Failure 3 - Preliminary Checks tab shows no records
    //
    //                 Recovery path (mirrors TC_03 exactly):
    //                   -> logout current session
    //                   -> re-query BISS_DATA ("Preliminary checks herds") at offset = iAttempt+1
    //                   -> INET-validate the returned herd for this check type
    //                   -> login as the resolved agent -> navigate to My Clients
    //                   -> update Hooks fields -> loop
    //
    //                 pCheckType values: OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY
    // Parameters    : pCheckType - identifies which preliminary check type to open
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026 | Updated: 18-09-2026 (Playwright - countOf replaces findElements)
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens a farmer dashboard for preliminary check herd {string}")
    public void theAgentOpensAFarmerDashboardForPreliminaryCheckHerd(String pCheckType)
    {
        log.info("[STEP] When the agent opens a farmer dashboard for preliminary check herd: " + pCheckType);

        final int    MAX_HERD_RETRIES = 15;
        final String YEAR             = System.getProperty("herd.year", "2026").trim();
        final String iClientRowsXpath = ObjReader.getLocator("clientTableRows");

        boolean iFound = false;

        for (int iAttempt = 0; iAttempt < MAX_HERD_RETRIES; iAttempt++)
        {
            String iCurrentHerd     = getPrelimHerd(pCheckType);
            String iCurrentUsername = getPrelimUsername(pCheckType);

            log.info("[PRELIM-RETRY] Attempt " + (iAttempt + 1) + "/" + MAX_HERD_RETRIES + " | checkType=" + pCheckType + " | herd=" + iCurrentHerd + " | agent=" + iCurrentUsername);

            // -- Search for the herd on My Clients ------------------------------------------------------------------
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iCurrentHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);
            pause(1000);

            int iRowCount = countOf(iClientRowsXpath);

            // ================================================================
            // FAILURE 1 - 0 rows returned
            // ================================================================
            if (iRowCount == 0)
            {
                log.warning("[PRELIM-RETRY] FAILURE 1 | herd=" + iCurrentHerd + " returned 0 rows in My Clients.");
                boolean iRecovered = recoverWithNewHerd(pCheckType, iAttempt, YEAR);
                if (!iRecovered) break;
                continue;
            }

            // ================================================================
            // FAILURE 2 - Row found but "Herd expired"
            // ================================================================
            boolean iHerdExpired = countOf(EXPIRED_SPAN_XPATH) > 0;

            if (iHerdExpired)
            {
                log.warning("[PRELIM-RETRY] FAILURE 2 | herd=" + iCurrentHerd + " is marked 'Herd expired'.");
                boolean iRecovered = recoverWithNewHerd(pCheckType, iAttempt, YEAR);
                if (!iRecovered) break;
                continue;
            }

            // -- Row found and not expired - click through to the farmer dashboard ----------------------------------
            log.info("[PRELIM-RETRY] Row valid - clicking through to farmer dashboard.");
            //iAction("CLICK", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
            //pause(1500);

            // -- Navigate to Preliminary Checks tab -----------------------------------------------------------------
            iAction("CLICK",   "XPATH", PRELIM_TAB_XPATH, null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iCurrentHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);
            pause(2000);

            // ================================================================
            // FAILURE 3 - Preliminary Checks tab shows no records
            // ================================================================
            int     iPrelimRows     = countOf(PRELIM_TABLE_ROW_XPATH);
            boolean iPrelimTabEmpty = iPrelimRows == 0 || countOf(PRELIM_NO_RECORDS_XPATH) > 0;

            if (iPrelimTabEmpty)
            {
                log.warning("[PRELIM-RETRY] FAILURE 3 | herd=" + iCurrentHerd + " Preliminary Checks tab has no records.");

                // Navigate back to My Clients before recovery
                // (we are inside a farmer dashboard so the breadcrumb is available)
                try
                {
                    iAction("CLICK", "XPATH", MY_CLIENTS_BACK_XPATH, null);
                    pause(1000);
                }
                catch (Exception e)
                {
                    log.warning("[PRELIM-RETRY] Breadcrumb not found - using left menu.");
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
                }

                boolean iRecovered = recoverWithNewHerd(pCheckType, iAttempt, YEAR);
                if (!iRecovered) break;
                continue;
            }

            // -- All checks passed -----------------------------------------------------------------------------------
            log.info("[PRELIM-RETRY] Attempt " + (iAttempt + 1) + " succeeded | herd=" + iCurrentHerd + " | " + iPrelimRows + " prelim record(s) found.");
            iFound = true;
            break;
        }

        if (!iFound)
        {
            throw new RuntimeException(
                    "[PRELIM-RETRY] Could not find a usable herd for '" + pCheckType
                            + "' after " + MAX_HERD_RETRIES + " attempts."
                            + " Last herd tried: "   + getPrelimHerd(pCheckType)
                            + " | Last agent: "      + getPrelimUsername(pCheckType)
                            + ". Verify vwbs_land_validation has LVT_DESC='Preliminary Check'"
                            + " AND LVS_DESC='Pending' AND LVC_DESC='"
                            + getLvcDescForCheckType(pCheckType)
                            + "' with a non-expired herd registration and a valid BISS_INET agent.");
        }

        log.info("[STEP] Farmer dashboard ready | checkType=" + pCheckType + " | herd=" + getPrelimHerd(pCheckType) + " | agent=" + getPrelimUsername(pCheckType));
    }


// ===================================================================================================================================
//  PRELIMINARY CHECKS - BUSINESS STEPS
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Then the preliminary checks card should be visible on the dashboard
    // Description   : Opens the herd with the row View link, then asserts the
    //                 biss-portal-preliminary-checks mat-card is visible on the farmer dashboard.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the preliminary checks card should be visible on the dashboard")
    public void thePreliminaryChecksCardShouldBeVisibleOnTheDashboard()
    {
        log.info("[STEP] Then the preliminary checks card should be visible on the dashboard");

        iAction("CLICK", "XPATH", ".//td[contains(@class,'cdk-column-action')]//a[normalize-space()='View']", null);
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iPrelimChecksCard"), null);
        log.info("Preliminary Checks card confirmed visible on farmer dashboard.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the {string} preliminary check should show {string} on the dashboard
    // Description   : Asserts the named check type row shows the expected status on the dashboard.
    // Parameters    : pCheckName - check type label as shown on dashboard e.g. "Overclaim Checks"
    //                 pStatus    - "Response required" or "No response required"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the {string} preliminary check should show {string} on the dashboard")
    public void thePreliminaryCheckShouldShowOnTheDashboard(String pCheckName, String pStatus)
    {
        log.info("[STEP] And the '" + pCheckName + "' preliminary check should show '" + pStatus + "' on the dashboard");

        String iLocatorKey = pStatus.toLowerCase().contains("no response") ? "iPrelimCheckStatusNotRequired" : "iPrelimCheckStatusRequired";
        String iXpath      = String.format(ObjReader.getLocator(iLocatorKey), pCheckName);

        iAction("VERIFYELEMENT", "XPATH", iXpath, null);
        log.info("Confirmed '" + pCheckName + "' shows: " + pStatus);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent clicks View Preliminary Checks
    // Description   : Clicks the "View preliminary checks" button on the dashboard card.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
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
    // Description   : Confirms the prelims-accordion inside biss-response-page is visible.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the Preliminary Checks response page should be displayed")
    public void thePreliminaryChecksResponsePageShouldBeDisplayed()
    {
        log.info("[STEP] Then the Preliminary Checks response page should be displayed");

        iAction("VERIFYELEMENT", "XPATH", "//biss-response-page//mat-accordion[contains(@class,'prelims-accordion')]", null);
        log.info("Preliminary Checks response page confirmed.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent clicks Submit on the Preliminary Checks page without selecting any response
    // Description   : Negative test - Submit clicked before any radio selection.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @When("the agent clicks Submit on the Preliminary Checks page without selecting any response")
    public void theAgentClicksSubmitWithoutSelectingAnyResponse()
    {
        log.info("[STEP] When the agent clicks Submit without selecting any response - negative test");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iPrelimSubmitBtn"), null);
        log.info("Submit clicked without selections - expecting validation error.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then a validation error should be displayed on the Preliminary Checks page
    // Description   : Asserts the form stays ng-invalid after a premature submit.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - UiHelpers.isVisible)
    // ***************************************************************************************************************************************************************************************
    @Then("a validation error should be displayed on the Preliminary Checks page")
    public void aValidationErrorShouldBeDisplayedOnThePreliminaryChecksPage()
    {
        log.info("[STEP] Then a validation error should be displayed on the Preliminary Checks page");

        boolean iFormInvalid = isVisible(ObjReader.getLocator("iPrelimFormInvalid"), 3);

        Assertions.assertTrue(iFormInvalid, "Expected form to remain ng-invalid after submitting without selections.");
        log.info("Validation error confirmed - form is ng-invalid as expected.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent responds to all preliminary check panels row by row
    // Description   : Reads a DataTable of panel names and responses. For each panel:
    //                   1. Skips it when there is no red error icon (no response required)
    //                   2. Expands the panel when it is collapsed (aria-expanded = false)
    //                   3. Counts the row-level radios matching the response label
    //                   4. Clicks each radio by index - the locator is resolved again per click, so an Angular
    //                      re-render cannot leave a stale element behind
    //                 DataTable format:
    //                   | Panel Name                      | Response    |
    //                   | Overclaim Checks                | Accept      |
    //                   | Dual Claim Checks               | No, Keep it |
    //                   | No Agricultural Activity Checks | Reject      |
    // Parameters    : pDataTable - DataTable with headers "Panel Name" and "Response"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - getAttribute / countOf)
    // ***************************************************************************************************************************************************************************************
    @When("the agent responds to all preliminary check panels row by row")
    public void theAgentRespondsToAllPreliminaryCheckPanelsRowByRow(DataTable pDataTable)
    {
        log.info("[STEP] When the agent responds to all preliminary check panels row by row");

        List<Map<String, String>> iTableRows = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iEntry : iTableRows)
        {
            String iPanelLabel    = iEntry.get("Panel Name").trim();
            String iResponseLabel = iEntry.get("Response").trim();

            log.info("[STEP] Panel: '" + iPanelLabel + "' | Response: '" + iResponseLabel + "'");

            // -- 1. Skip panels without the red error icon ------------------------------------------------------------
            String iErrorPanelXpath = String.format(ObjReader.getLocator("iPrelimPanelHeaderError"), iPanelLabel);
            if (!isVisible(iErrorPanelXpath, 2))
            {
                log.info("[STEP] Panel '" + iPanelLabel + "' - no response required. Skipping.");
                continue;
            }

            // -- 2. Expand panel if collapsed -------------------------------------------------------------------------
            String iPanelHeaderXpath = String.format(ObjReader.getLocator("iPrelimPanelHeader"), iPanelLabel);
            String iExpanded         = UiHelpers.getAttribute(xp(iPanelHeaderXpath), "aria-expanded");

            if (!"true".equalsIgnoreCase(iExpanded))
            {
                iAction("CLICK", "XPATH", iPanelHeaderXpath, null);
                log.info("[STEP] Expanded panel: " + iPanelLabel);
                pause(800);
            }

            // -- 3. Count the row-level radios matching the response label --------------------------------------------
            String iRowRadioXpath = String.format(ObjReader.getLocator("iPrelimRowRadioByLabel"), iResponseLabel);
            int    iRadioCount    = countOf(iRowRadioXpath);

            if (iRadioCount == 0)
            {
                throw new AssertionError("Panel '" + iPanelLabel + "': no row-level radio buttons found with label '"
                        + iResponseLabel + "'. Verify label matches DOM text exactly.");
            }

            log.info("[STEP] Found " + iRadioCount + " row(s) to respond to in panel: " + iPanelLabel);

            // -- 4. Click each radio - the indexed locator is resolved again per click --------------------------------
            for (int i = 0; i < iRadioCount; i++)
            {
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
    // Description   : Clicks Submit after all required radio buttons have been selected.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
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
    // Description   : Clicks Yes on the confirmation dialog after Submit.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent confirms the Preliminary Checks submission")
    public void theAgentConfirmsThePreliminaryChecksSubmission()
    {
        log.info("[STEP] Then the agent confirms the Preliminary Checks submission");

        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iPrelimConfirmDialogYesBtn"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iPrelimConfirmDialogYesBtn"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iPrelimConfirmDialogYesBtn"), null);
        log.info("Confirmation dialog - Yes clicked.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the Preliminary Checks submission should be confirmed successfully
    // Description   : Asserts the submission completed - "Response required" is gone from the dashboard card.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026 | Updated: 18-09-2026 (Playwright - UiHelpers.isVisible)
    // ***************************************************************************************************************************************************************************************
    @And("the Preliminary Checks submission should be confirmed successfully")
    public void thePreliminaryChecksSubmissionShouldBeConfirmedSuccessfully()
    {
        log.info("[STEP] And the Preliminary Checks submission should be confirmed successfully");
        log.info("[STEP] Verifying 'Response required' is NOT displayed on the Preliminary Checks card");

        String iResponseRequiredXpath =
                "//mat-card[.//mat-card-title[normalize-space()='Preliminary Checks']]"
                        + "//span[contains(@class,'dashboard-clarification-text')"
                        + " and normalize-space()='Response required']";

        boolean iResponseRequiredVisible = isVisible(iResponseRequiredXpath, 3);

        Assertions.assertFalse(iResponseRequiredVisible, "'Response required' is still visible on the Preliminary Checks dashboard card");
        log.info("Preliminary Checks submitted and confirmed successfully.");
        Hooks.captureStep("Preliminary Checks - submission confirmed successfully");
    }


// ===================================================================================================================================
//  PRIVATE - RECOVERY (logout + DB re-query + INET validate + re-login)
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : recoverWithNewHerd
    // Description   : Single recovery method called by ALL three failure modes.
    //                 Steps:
    //                   1. Logout current session
    //                   2. Try consecutive DB offsets (startOffset to startOffset+MAX_OFFSET_TRIES)
    //                      until an INET-validated herd is found for this check type.
    //                      Prints the full DB result table to the console on every query.
    //                   3. Login as the resolved agent (always - the session was terminated in step 1)
    //                   4. Navigate to My Clients
    //                   5. Update Hooks fields
    // Returns       : true if a valid replacement was found and the session is ready, false if exhausted
    // Parameters    : pCheckType - OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY
    //                 pAttempt   - current outer loop iteration (offset starts at pAttempt+1)
    //                 pYear      - scheme year
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    private boolean recoverWithNewHerd(String pCheckType, int pAttempt, String pYear)
    {
        String iCurrentUsername = getPrelimUsername(pCheckType);
        int    iStartOffset     = pAttempt + 1;

        // How many consecutive offsets to probe before giving up in a single recovery call.
        // Covers the case where several consecutive herds have no INET agent.
        final int MAX_OFFSET_TRIES = 10;

        log.info("[PRELIM-RECOVER] Starting recovery | checkType=" + pCheckType + " | startOffset=" + iStartOffset);

        // -- Step 1: Logout immediately ----------------------------------------------------------------------------
        performLogout();

        // -- Step 2: Try consecutive offsets until an INET-valid herd is found --------------------------------------
        String iLvcDescTarget = getLvcDescForCheckType(pCheckType);
        String iNextHerd      = null;
        String iNextUsername  = null;

        for (int iOff = iStartOffset; iOff < iStartOffset + MAX_OFFSET_TRIES; iOff++)
        {
            log.info("[PRELIM-RECOVER] Querying BISS_DATA | LVC_DESC='" + iLvcDescTarget + "' | offset=" + iOff);

            database.DBRouter.runDB("DATA", "Preliminary checks herds", pYear, String.valueOf(iOff));

            List<Map<String, Object>> iDbRows = database.DBRouter.getRows();

            // Print the full result table so you can see exactly what the DB returned
            printDbResultTable("PRELIMINARY CHECKS HERDS offset=" + iOff, iDbRows);

            if (iDbRows == null || iDbRows.isEmpty())
            {
                log.warning("[PRELIM-RECOVER] BISS_DATA returned 0 rows at offset=" + iOff + " - no more Pending rows exist for year=" + pYear + ". Stopping.");
                break;
            }

            for (Map<String, Object> iRow : iDbRows)
            {
                String iLvc  = Objects.toString(iRow.get("LVC_DESC"),    "").trim();
                String iHerd = Objects.toString(iRow.get("LVL_HERD_NO"), "").trim();

                if (!iLvc.equalsIgnoreCase(iLvcDescTarget) || iHerd.isEmpty()) continue;

                log.info("[PRELIM-RECOVER] Checking BISS_INET for " + iLvcDescTarget + " herd=" + iHerd);

                database.DBRouter.runDB("INET", "Get Login Id for herd", iHerd);
                String iUsername = database.DBRouter.getValue("USERNAME");

                log.info("[PRELIM-RECOVER] INET result - herd=" + iHerd + " | USERNAME=" + (iUsername == null ? "null" : "'" + iUsername + "'"));

                if (iUsername != null && !iUsername.isBlank())
                {
                    iNextHerd     = iHerd;
                    iNextUsername = iUsername.trim();
                    log.info("[PRELIM-RECOVER] INET-validated replacement: " + iLvcDescTarget + " -> herd=" + iNextHerd + " | username=" + iNextUsername + " (offset=" + iOff + ")");
                    break;
                }
                else
                {
                    log.warning("[PRELIM-RECOVER] No INET agent for " + iLvcDescTarget + " herd=" + iHerd + " at offset=" + iOff + " - trying offset=" + (iOff + 1));
                }
            }

            if (iNextHerd != null) break;  // found a valid herd - stop probing offsets
        }

        if (iNextHerd == null)
        {
            log.warning("[PRELIM-RECOVER] Exhausted offsets " + iStartOffset + " to "
                    + (iStartOffset + MAX_OFFSET_TRIES - 1) + " for " + pCheckType
                    + " - no INET-validated herd found. Stopping recovery.");
            return false;
        }

        // -- Step 3: Login (always - the session was terminated in step 1) -------------------------------------------
        log.info("[PRELIM-RECOVER] Agent: " + iCurrentUsername + " -> " + iNextUsername);
        performLogin(iNextUsername);

        // -- Step 4: Navigate to My Clients --------------------------------------------------------------------------
        navigateToMyClients();

        // -- Step 5: Update Hooks fields -----------------------------------------------------------------------------
        setPrelimHerd(pCheckType, iNextHerd, iNextUsername);
        Hooks.RUNTIME_USERNAME = iNextUsername;

        log.info("[PRELIM-RECOVER] Recovery complete | " + pCheckType + " -> herd=" + iNextHerd + " | username=" + iNextUsername);
        return true;
    }


// ===================================================================================================================================
//  PRIVATE - HOOKS FIELD ACCESSORS
// ===================================================================================================================================

    private String getPrelimHerd(String pCheckType)
    {
        switch (pCheckType.trim().toUpperCase())
        {
            case "OVERCLAIM":     return Hooks.OVERCLAIM_HERD;
            case "DUAL_CLAIM":    return Hooks.DUAL_CLAIM_HERD;
            case "AGRI_ACTIVITY": return Hooks.AGRI_ACTIVITY_HERD;
            default: throw new IllegalArgumentException("Unknown check type: '" + pCheckType + "'. Expected: OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY");
        }
    }

    private String getPrelimUsername(String pCheckType)
    {
        switch (pCheckType.trim().toUpperCase())
        {
            case "OVERCLAIM":     return Hooks.OVERCLAIM_USERNAME;
            case "DUAL_CLAIM":    return Hooks.DUAL_CLAIM_USERNAME;
            case "AGRI_ACTIVITY": return Hooks.AGRI_ACTIVITY_USERNAME;
            default: throw new IllegalArgumentException("Unknown: " + pCheckType);
        }
    }

    private void setPrelimHerd(String pCheckType, String pHerd, String pUsername)
    {
        switch (pCheckType.trim().toUpperCase())
        {
            case "OVERCLAIM":
                Hooks.OVERCLAIM_HERD      = pHerd;
                Hooks.OVERCLAIM_USERNAME  = pUsername;
                break;
            case "DUAL_CLAIM":
                Hooks.DUAL_CLAIM_HERD     = pHerd;
                Hooks.DUAL_CLAIM_USERNAME = pUsername;
                break;
            case "AGRI_ACTIVITY":
                Hooks.AGRI_ACTIVITY_HERD     = pHerd;
                Hooks.AGRI_ACTIVITY_USERNAME = pUsername;
                break;
            default: throw new IllegalArgumentException("Unknown: " + pCheckType);
        }
    }

    private String getLvcDescForCheckType(String pCheckType)
    {
        switch (pCheckType.trim().toUpperCase())
        {
            case "OVERCLAIM":     return "Overclaim";
            case "DUAL_CLAIM":    return "Dual claim";
            case "AGRI_ACTIVITY": return "Agricultural Activity";
            default: throw new IllegalArgumentException("Unknown: " + pCheckType);
        }
    }


// ===================================================================================================================================
//  PRIVATE - NAVIGATION (mirrors TC_03 exactly)
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : performLogout
    // Description   : Exits the application and waits for the landing page, which confirms the session is gone.
    //                 If the logout click fails (session already expired), the base URL is opened instead.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026 | Updated: 18-09-2026 (Playwright - UiHelpers.navigateTo)
    // ***************************************************************************************************************************************************************************************
    private void performLogout()
    {
        log.info("[RELOGIN] Logging out current session...");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"),   null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"),  null);

            // Wait for the welcome/landing page login button to appear -
            // this confirms the session is fully terminated and we are back at the start
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
            log.info("[RELOGIN] Logout complete - landing page visible.");
        }
        catch (Exception e)
        {
            // If logout fails (e.g. the session already expired), navigate directly to
            // the base URL to force a clean state rather than leaving a broken session
            log.warning("[RELOGIN] Logout click failed (" + e.getMessage() + ") - navigating to base URL as fallback.");
            UiHelpers.navigateTo(Hooks.iUrl);
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : performLogin
    // Description   : Logs in as the given agent: username, password, then either the PIN + TOTP screen
    //                 or the OTP-only screen, and finally the Terms and Conditions dialog when it appears.
    // Parameters    : pUsername (String) - agent username resolved from BISS_INET
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026 | Updated: 18-09-2026 (Playwright - fill / pressSequentially replace clear / sendKeys)
    // ***************************************************************************************************************************************************************************************
    private void performLogin(String pUsername)
    {
        log.info("[RELOGIN] Logging in as: " + pUsername);

        iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),     null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),      "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),            null);

        if (isVisible(ObjReader.getLocator("iPinForm"), 3))
        {
            log.info("[RELOGIN] PIN screen detected - filling slots 1-7.");

            for (int idx = 1; idx <= 7; idx++)
            {
                String iDynXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));

                if (isVisible(iDynXpath, 1))
                {
                    Locator iInput    = xp(iDynXpath);
                    boolean iDisabled = iInput.getAttribute("disabled") != null;

                    if (!iDisabled && iInput.isEnabled())
                    {
                        iInput.fill("");
                        iInput.pressSequentially("1");
                    }
                }
            }

            iAction("CLICK",   "XPATH", ObjReader.getLocator("iPinLoginBtn"),   null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"),   "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);
            log.info("[RELOGIN] PIN + TOTP submitted.");
        }
        else
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),  null);
            log.info("[RELOGIN] OTP-only login submitted.");
        }

        if (isVisible(ObjReader.getLocator("iAcceptTermsCheckbox"), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[RELOGIN] Terms and Conditions accepted.");
        }
        log.info("[RELOGIN] Login complete for: " + pUsername);
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : navigateToMyClients
    // Description   : Searches for the BISS application on the portal home screen and opens My Clients.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026 | Updated: 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    private void navigateToMyClients()
    {
        log.info("[RELOGIN] Navigating to My Clients...");

        iAction("CLICK",         "XPATH", ObjReader.getLocator("iAppSearchBar"), "");
        iAction("TEXTBOX",       "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        //iAction("VERIFYELEMENT","XPATH", ObjReader.getLocator("iSearchAppLabel"), "");
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iBissLink"), "");
        // NOTE (carried over from Selenium): the key name is passed instead of its locator, so this wait returns at once
        iAction("WAITINVISIBLE", "XPATH", "iScreenBuffer", "Spinner");
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"),   null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
        iAction("CLICK",         "XPATH", ObjReader.getLocator("iViewAllTab"),         "");
        log.info("[RELOGIN] My Clients ready.");
    }


// ===================================================================================================================================
//  OVERCLAIM AND AGRICULTURAL ACTIVITY ERROR RESOLUTION
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent resolves any overclaim value exceeds EH errors if present
    // Description   : After clicking Submit, rows with claimed area > eligible (ha) show a
    //                 "Value exceeds EH" error inline in the cdk-column-claimedArea cell.
    //
    //                 Three scenarios handled:
    //                   1. No suffix (single parcel e.g. "A1150100017"):
    //                        -> 1 row in group -> enter the eligible value directly (claimed = eligible)
    //                   2. Suffix present, split into 2+ ("A1150100017 A" + "A1150100017 B"):
    //                        -> group all suffix rows by base parcel number
    //                        -> distribute the shared eligible pool proportionally to each row's claimed value
    //                        -> non-last rows: floor to 2dp (never rounds up over the pool)
    //                        -> last row: pool - running total (absorbs the rounding remainder)
    //                   3. Split into N parts (A/B/C/...): same proportional logic, scales to any N
    //
    //                 Row-scoped locators (.//td...) come from ObjectRepository and are resolved against each row
    //                 with row.locator("xpath=..."), which is the Playwright equivalent of
    //                 WebElement.findElements(By.xpath(".//...")).
    //                 If no errors are present the step passes silently. The feature file calls Submit again afterwards.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 21-04-2026 | Updated: 18-09-2026 (Playwright - setValueByJs replaces the JS clear + sendKeys block)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent resolves any overclaim value exceeds EH errors if present")
    public void theAgentResolvesAnyOverclaimValueExceedsEHErrorsIfPresent()
    {
        log.info("[STEP] Then the agent resolves any overclaim value exceeds EH errors if present");

        // -- All data rows - absolute XPath from ObjectRepository ---------------------------------------------------
        String        iRowsXpath = ObjReader.getLocator("iPrelimOverclaimDataRows");
        List<Locator> iAllRows   = rowsOf(iRowsXpath);

        if (iAllRows.isEmpty())
        {
            log.info("[OVERCLAIM-FIX] No data rows found - nothing to fix.");
            return;
        }

        // -- Parallel lists - one entry per error row found ----------------------------------------------------------
        List<Integer> iErrorRowIndexes   = new ArrayList<>();
        List<String>  iBaseParcelNumbers = new ArrayList<>();
        List<Double>  iEligibleValues    = new ArrayList<>();
        List<Double>  iOriginalClaimed   = new ArrayList<>();

        for (int i = 0; i < iAllRows.size(); i++)
        {
            Locator iRow = iAllRows.get(i);

            // -- Only process rows that have a "Value exceeds EH" error ---------------------------------------------
            if (inRow(iRow, "iPrelimValueExceedsEHError").count() == 0) continue;

            // -- Read parcel number - strip the trailing single-letter split suffix ----------------------------------
            // DOM: <td class="cdk-column-parcelNo"> A1150100017 <span> B </span>
            Locator iParcelCells = inRow(iRow, "iPrelimParcelNoCell");
            if (iParcelCells.count() == 0) continue;

            String iFullParcel = iParcelCells.first().innerText().trim();
            String iBaseParcel = iFullParcel.replaceAll("\\s+[A-Z]$", "").trim();

            // -- Read eligible (ha) ----------------------------------------------------------------------------------
            Locator iEligibleCells = inRow(iRow, "iPrelimEligibleCell");
            if (iEligibleCells.count() == 0) continue;

            double iEligible;
            try
            {
                iEligible = Double.parseDouble(iEligibleCells.first().innerText().trim().replaceAll("[^0-9.]", ""));
            }
            catch (NumberFormatException e)
            {
                log.warning("[OVERCLAIM-FIX] Row " + (i + 1) + " - could not parse eligible value. Skipping.");
                continue;
            }

            // -- Read the pre-filled claimed value from the input ----------------------------------------------------
            Locator iInputs = inRow(iRow, "iPrelimClaimedInput");
            if (iInputs.count() == 0) continue;

            String iClaimedRaw = UiHelpers.inputValue(iInputs);
            if (iClaimedRaw == null || iClaimedRaw.trim().isEmpty()) iClaimedRaw = "0";

            double iClaimed;
            try
            {
                iClaimed = Double.parseDouble(iClaimedRaw.trim().replaceAll("[^0-9.]", ""));
            }
            catch (NumberFormatException e)
            {
                log.warning("[OVERCLAIM-FIX] Row " + (i + 1) + " - could not parse claimed value '" + iClaimedRaw + "'. Skipping.");
                continue;
            }

            iErrorRowIndexes.add(i);
            iBaseParcelNumbers.add(iBaseParcel);
            iEligibleValues.add(iEligible);
            iOriginalClaimed.add(iClaimed);

            log.info("[OVERCLAIM-FIX] Error row " + (i + 1) + " | parcel=" + iFullParcel + " | base=" + iBaseParcel + " | eligible=" + iEligible + " | claimed=" + iClaimed);
        }

        if (iErrorRowIndexes.isEmpty())
        {
            log.info("[OVERCLAIM-FIX] No 'Value exceeds EH' errors found - nothing to fix.");
            return;
        }

        // -- Group error rows by base parcel number ------------------------------------------------------------------
        Map<String, List<Integer>> iGroupMap = new LinkedHashMap<>();
        for (int g = 0; g < iErrorRowIndexes.size(); g++)
        {
            String iKey = iBaseParcelNumbers.get(g);
            iGroupMap.computeIfAbsent(iKey, k -> new ArrayList<>()).add(g);
        }

        // -- Compute the assigned value per row ----------------------------------------------------------------------
        double[] iAssignedValues = new double[iErrorRowIndexes.size()];

        for (Map.Entry<String, List<Integer>> iGroup : iGroupMap.entrySet())
        {
            String        iParcel    = iGroup.getKey();
            List<Integer> iGroupIdxs = iGroup.getValue();
            double        iPool      = iEligibleValues.get(iGroupIdxs.get(0));

            // -- CASE 1: Single parcel - no suffix, not split -------------------------------------------------------
            if (iGroupIdxs.size() == 1)
            {
                iAssignedValues[iGroupIdxs.get(0)] = iPool;
                log.info("[OVERCLAIM-FIX] Single parcel '" + iParcel + "' | not split | entering eligible=" + iPool);
                continue;
            }

            // -- CASE 2 & 3: Split parcel - proportional distribution across N rows ---------------------------------
            double iTotalClaimed = 0.0;
            for (int g : iGroupIdxs) iTotalClaimed += iOriginalClaimed.get(g);

            log.info("[OVERCLAIM-FIX] Split parcel '" + iParcel + "' | rows=" + iGroupIdxs.size() + " | pool=" + iPool + " | totalClaimed=" + iTotalClaimed);

            // Edge case - all claimed values are 0, distribute the pool evenly
            if (iTotalClaimed == 0.0)
            {
                double iEven = Math.floor((iPool / iGroupIdxs.size()) * 100.0) / 100.0;
                for (int g : iGroupIdxs) iAssignedValues[g] = iEven;
                log.warning("[OVERCLAIM-FIX] Split parcel '" + iParcel + "' - total claimed is 0, distributing evenly: " + iEven + " each.");
                continue;
            }

            // Non-last rows: floor to 2dp - guarantees the partial sum never exceeds the pool
            double iRunningTotal = 0.0;
            for (int gi = 0; gi < iGroupIdxs.size() - 1; gi++)
            {
                int    g           = iGroupIdxs.get(gi);
                double iProportion = iOriginalClaimed.get(g) / iTotalClaimed;
                double iShare      = Math.floor(iProportion * iPool * 100.0) / 100.0;
                iAssignedValues[g] = iShare;
                iRunningTotal     += iShare;
                log.info("[OVERCLAIM-FIX]   Row " + (iErrorRowIndexes.get(g) + 1) + " | proportion=" + String.format("%.4f", iProportion) + " | share=" + iShare);
            }

            // Last row absorbs the remainder - group total = pool exactly, regardless of N
            int    iLastG     = iGroupIdxs.get(iGroupIdxs.size() - 1);
            double iLastShare = Math.round((iPool - iRunningTotal) * 100.0) / 100.0;
            iAssignedValues[iLastG] = iLastShare;
            log.info("[OVERCLAIM-FIX]   Row " + (iErrorRowIndexes.get(iLastG) + 1) + " | last row remainder=" + iLastShare + " | group total=" + String.format("%.2f", iRunningTotal + iLastShare));
        }

        // -- Write the computed values into the DOM ------------------------------------------------------------------
        // Re-read all rows so the write uses the current DOM after any Angular re-render
        iAllRows = rowsOf(iRowsXpath);

        for (int g = 0; g < iErrorRowIndexes.size(); g++)
        {
            int     iRowIndex    = iErrorRowIndexes.get(g);
            String  iValueString = String.format("%.2f", iAssignedValues[g]);
            Locator iRow         = iAllRows.get(iRowIndex);
            Locator iInput       = inRow(iRow, "iPrelimClaimedInput").first();

            // Sets the value and fires input, change and blur so the Angular reactive form picks it up
            // (Selenium needed a JS clear, sendKeys and two dispatchEvent calls for the same result)
            UiHelpers.setValueByJs(iInput, iValueString);

            log.info("[OVERCLAIM-FIX] Written " + iValueString + " -> row " + (iRowIndex + 1) + " (parcel=" + iBaseParcelNumbers.get(g) + ")");
        }

        log.info("[OVERCLAIM-FIX] All overclaim errors resolved. Ready to re-submit.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent resolves any agricultural activity dropdown errors if present
    // Description   : After clicking Submit on the Preliminary Checks page, rows in the
    //                 No Agricultural Activity panel that have "Yes, change" selected show a
    //                 mandatory mat-select dropdown in cdk-column-otherAgActivity.
    //                 If the dropdown was not pre-filled, Angular marks it ng-invalid /
    //                 aria-invalid="true" and blocks submission.
    //
    //                 For every data row in biss-response-agactivity-table:
    //                   1. Check whether the mat-select in cdk-column-otherAgActivity is empty / invalid
    //                      (aria-invalid="true" or the mat-mdc-select-empty class)
    //                   2. If invalid -> JavaScript click on the mat-select to open the CDK overlay panel
    //                   3. Wait for the overlay to appear
    //                   4. Click the FIRST mat-option in the panel
    //                   5. If not invalid -> skip silently
    //
    //                 Safe to always include - with no invalid dropdowns it passes silently.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 22-04-2026 | Updated: 18-09-2026 (Playwright - jsClick / isVisible, no Thread.sleep)
    // ***************************************************************************************************************************************************************************************
    @Then("the agent resolves any agricultural activity dropdown errors if present")
    public void theAgentResolvesAnyAgriculturalActivityDropdownErrorsIfPresent()
    {
        log.info("[STEP] Then the agent resolves any agricultural activity dropdown errors if present");

        // -- All data rows scoped to biss-response-agactivity-table -------------------------------------------------
        // The absolute XPath from ObjectRepository is scoped to the agri activity component, so the Overclaim and
        // Dual Claim tables are never touched
        String        iRowsXpath = ObjReader.getLocator("iPrelimAgriActivityDataRows");
        List<Locator> iAllRows   = rowsOf(iRowsXpath);

        if (iAllRows.isEmpty())
        {
            log.info("[AGRI-FIX] No Agricultural Activity rows found - nothing to fix.");
            return;
        }

        String iFirstOptionXpath = ObjReader.getLocator("iPrelimCdkOverlayFirstOption");
        int    iFixCount         = 0;

        for (int i = 0; i < iAllRows.size(); i++)
        {
            Locator iRow       = iAllRows.get(i);
            Locator iDropdowns = inRow(iRow, "iPrelimAgriActivityDropdown");

            if (iDropdowns.count() == 0)
            {
                log.info("[AGRI-FIX] Row " + (i + 1) + " - no dropdown present. Skipping.");
                continue;
            }

            Locator iDropdown = iDropdowns.first();

            // -- Check whether the dropdown is invalid / empty -------------------------------------------------------
            // Angular marks it aria-invalid="true" when the required field has no selection.
            // The mat-mdc-select-empty class is a belt-and-braces guard.
            String iAriaInvalid = UiHelpers.getAttribute(iDropdown, "aria-invalid");
            String iClasses     = UiHelpers.getAttribute(iDropdown, "class");

            boolean iIsInvalid = "true".equalsIgnoreCase(iAriaInvalid)
                    || (iClasses != null && iClasses.contains("mat-mdc-select-empty"));

            if (!iIsInvalid)
            {
                log.info("[AGRI-FIX] Row " + (i + 1) + " - dropdown already has a value. Skipping.");
                continue;
            }

            log.info("[AGRI-FIX] Row " + (i + 1) + " - dropdown is empty/invalid. Selecting first option.");

            // -- Open the CDK overlay panel --------------------------------------------------------------------------
            // JavaScript click - an Angular mat-select trigger can be intercepted by an overlay
            UiHelpers.jsClick(iDropdown);

            // -- Wait for the CDK overlay panel to appear ------------------------------------------------------------
            // The panel is appended to the body as a CDK overlay - not inside the table row
            if (!isVisible(iFirstOptionXpath, 5))
            {
                log.warning("[AGRI-FIX] Row " + (i + 1) + " - CDK overlay did not appear after clicking the dropdown. Skipping.");
                continue;
            }

            // -- Click the first mat-option from the overlay panel ---------------------------------------------------
            String iOptionText = xp(iFirstOptionXpath).innerText().trim();
            iAction("CLICK", "XPATH", iFirstOptionXpath, null);

            iFixCount++;
            log.info("[AGRI-FIX] Row " + (i + 1) + " - selected first option: '" + iOptionText + "'");

            // Re-read the rows after the Angular re-render so the next iteration uses the current DOM
            iAllRows = rowsOf(iRowsXpath);
        }

        if (iFixCount > 0)
        {
            log.info("[AGRI-FIX] Fixed " + iFixCount + " agricultural activity dropdown(s). Ready to re-submit.");
        }
        else
        {
            log.info("[AGRI-FIX] No invalid dropdowns found - submission can proceed.");
        }
    }


// ===================================================================================================================================
//  PRIVATE - UTILITIES
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : xp
    // Description   : Builds a Playwright Locator from an XPath string, pinned to the FIRST match so a locator that
    //                 matches several elements behaves like Selenium findElement.
    // Parameters    : pXpath (String) - XPath expression
    // Returns       : Locator
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static Locator xp(String pXpath)
    {
        return UiHelpers.byXpath(pXpath).first();
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
        return UiHelpers.byXpath(pXpath).count();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : rowsOf
    // Description   : Snapshot of every matching row - replacement for driver.findElements(By.xpath(...)).
    //                 Each entry can be scoped further with inRow(...).
    // Parameters    : pXpath (String) - XPath expression for the rows
    // Returns       : List<Locator> - one Locator per row (empty when nothing matches)
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static List<Locator> rowsOf(String pXpath)
    {
        return UiHelpers.byXpath(pXpath).all();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : inRow
    // Description   : Resolves a row-scoped Object Repository XPath (".//td[...]") against one row -
    //                 the Playwright equivalent of WebElement.findElements(By.xpath(".//...")).
    // Parameters    : pRow        (Locator) - the row
    //                 pLocatorKey (String)  - ObjectRepository key holding a relative XPath
    // Returns       : Locator - all matches inside that row
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static Locator inRow(Locator pRow, String pLocatorKey)
    {
        return pRow.locator("xpath=" + ObjReader.getLocator(pLocatorKey));
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
        return UiHelpers.isVisible(xp(pXpath), pSeconds);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : pause
    // Description   : Fixed wait - replacement for Thread.sleep(ms), same duration.
    // Parameters    : pMilliseconds (long)
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static void pause(long pMilliseconds)
    {
        CommonFunctions.getPage().waitForTimeout(pMilliseconds);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : printDbResultTable
    // Description   : Prints the full DB result set to the console as a formatted table.
    //                 Called after every BISS_DATA query inside recoverWithNewHerd so you can
    //                 see exactly what was returned (or that nothing was returned) for each offset.
    // Parameters    : pQueryLabel - label to identify which query this result belongs to
    //                 pRows       - the result rows from DBRouter.getRows()
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026
    // ***************************************************************************************************************************************************************************************
    private void printDbResultTable(String pQueryLabel, List<Map<String, Object>> pRows)
    {
        if (pRows == null || pRows.isEmpty())
        {
            log.info("[DB-RESULT] " + pQueryLabel + " - 0 rows returned");
            return;
        }

        StringBuilder iSb = new StringBuilder();
        iSb.append("\n[DB-RESULT] ").append(pQueryLabel)
                .append(" - ").append(pRows.size()).append(" row(s):\n");

        // Build the column list from the first row keys
        Map<String, Object> iFirstRow = pRows.get(0);
        String[] iCols = iFirstRow.keySet().toArray(new String[0]);

        // Compute the display width per column (capped at 40 chars to keep the table readable)
        int[] iWidths = new int[iCols.length];
        for (int c = 0; c < iCols.length; c++)
        {
            iWidths[c] = iCols[c].length();
            for (Map<String, Object> iRow : pRows)
            {
                String iVal = Objects.toString(iRow.get(iCols[c]), "null");
                if (iVal.length() > iWidths[c]) iWidths[c] = Math.min(iVal.length(), 40);
            }
        }

        // Build the separator and header lines
        StringBuilder iSep = new StringBuilder("  +-");
        StringBuilder iHdr = new StringBuilder("  | ");
        for (int c = 0; c < iCols.length; c++)
        {
            iSep.append("-".repeat(iWidths[c])).append("-+-");
            iHdr.append(padRight(iCols[c], iWidths[c])).append(" | ");
        }

        iSb.append(iSep).append("\n")
                .append(iHdr).append("\n")
                .append(iSep).append("\n");

        // Build the data rows
        for (Map<String, Object> iRow : pRows)
        {
            StringBuilder iLine = new StringBuilder("  | ");
            for (int c = 0; c < iCols.length; c++)
            {
                String iVal = Objects.toString(iRow.get(iCols[c]), "null");
                if (iVal.length() > 40) iVal = iVal.substring(0, 37) + "...";
                iLine.append(padRight(iVal, iWidths[c])).append(" | ");
            }
            iSb.append(iLine).append("\n");
        }

        iSb.append(iSep);
        log.info(iSb.toString());
    }

    private String padRight(String s, int n)
    {
        if (s == null) s = "";
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }
}
