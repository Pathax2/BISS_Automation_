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
//                 Retry flow for theAgentOpensAFarmerDashboardForPreliminaryCheckHerd:
//
//                   For each attempt (up to MAX_HERD_RETRIES):
//                     1. Search current herd on My Clients
//                     2. If 0 rows returned:
//                          → logout → DB re-query at next offset → INET validate
//                          → re-login if agent changed → update Hooks → retry
//                     3. If row found but marked "Herd expired":
//                          → logout → DB re-query at next offset → INET validate
//                          → re-login if agent changed → update Hooks → retry
//                     4. Row found and not expired → click through to farmer dashboard
//                     5. Navigate to Preliminary Checks tab
//                     6. If tab shows no records:
//                          → logout → DB re-query at next offset → INET validate
//                          → re-login if agent changed → update Hooks → retry
//                     7. Tab has records → iFound = true → break → proceed
//
//                 All three failure modes (0 rows, expired, empty prelim tab) go through
//                 the identical recovery path — same as TC_03 for consistency.
//
//                 All locators externalised to ObjectRepository.properties.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 17-04-2026 | Updated : 20-04-2026
// ===================================================================================================================================

package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ObjReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.*;

public class TC_18
{
    private static final Logger log = Logger.getLogger(TC_18.class.getName());


    // ── "Herd expired" span inside the expired column cell of a data row ──────
    private static final String EXPIRED_SPAN_XPATH =
            "//tbody[contains(@class,'mdc-data-table__content')]"
                    + "//tr[contains(@class,'client-list-element-row')]"
                    + "//td[contains(@class,'cdk-column-expired')]"
                    + "//span[normalize-space()='Herd expired']";

    // ── Preliminary Checks tab in the farmer dashboard ────────────────────────
    private static final String PRELIM_TAB_XPATH = "//span[@class='mdc-tab__text-label'][normalize-space()='Preliminary Checks']";

    // ── Data rows inside the Preliminary Checks table ─────────────────────────
    private static final String PRELIM_TABLE_ROW_XPATH = ".//td[contains(@class,'cdk-column-preliminaryChecks')][contains(normalize-space(),'Response required')]";

    // ── Empty state message on the Preliminary Checks tab ─────────────────────
    private static final String PRELIM_NO_RECORDS_XPATH = "//*[contains(normalize-space(),'no records to display')]";

    // ── Breadcrumb / left-menu back to My Clients ─────────────────────────────
    private static final String MY_CLIENTS_BACK_XPATH = "//breadcrumb//a[contains(.,'My Clients')] | //a[normalize-space()='My Clients']";


// ===================================================================================================================================
//  FARMER DASHBOARD — PRELIMINARY CHECK HERD
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
// Step          : When the agent opens a farmer dashboard for preliminary check herd {string}
// Description   : Resolves herd + username from Hooks, searches My Clients, validates the row
//                 is not expired, clicks through to the dashboard, and confirms the Preliminary
//                 Checks tab has records.
//
//                 All three failure modes trigger the SAME recovery path:
//                   Failure 1 — 0 rows in My Clients
//                   Failure 2 — Row found but "Herd expired"
//                   Failure 3 — Preliminary Checks tab shows no records
//
//                 Recovery path (mirrors TC_03 exactly):
//                   → logout current session
//                   → re-query BISS_DATA ("Preliminary checks herds") at offset = iAttempt+1
//                   → INET-validate the returned herd for this check type
//                   → if agent changed: login as new agent → navigate to My Clients
//                   → if same agent: navigate to My Clients
//                   → update Hooks fields → loop
//
//                 pCheckType values: OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY
// Parameters    : pCheckType — identifies which preliminary check type to open
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 20-04-2026
// ***************************************************************************************************************************************************************************************
    @When("the agent opens a farmer dashboard for preliminary check herd {string}")
    public void theAgentOpensAFarmerDashboardForPreliminaryCheckHerd(String pCheckType)
    {
        log.info("[STEP] When the agent opens a farmer dashboard for preliminary check herd: " + pCheckType);

        final int    MAX_HERD_RETRIES = 15;
        final String YEAR             = System.getProperty("herd.year", "2026").trim();
        final By     iClientRowsBy    = By.xpath(ObjReader.getLocator("clientTableRows"));

        boolean iFound = false;

        for (int iAttempt = 0; iAttempt < MAX_HERD_RETRIES; iAttempt++)
        {
            String iCurrentHerd     = getPrelimHerd(pCheckType);
            String iCurrentUsername = getPrelimUsername(pCheckType);

            log.info("[PRELIM-RETRY] Attempt " + (iAttempt + 1) + "/" + MAX_HERD_RETRIES + " | checkType=" + pCheckType + " | herd="     + iCurrentHerd + " | agent="    + iCurrentUsername);

            // ── Search for the herd on My Clients ────────────────────────────────────────────
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iCurrentHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

            List<WebElement> iRows = getDriver().findElements(iClientRowsBy);

            // ════════════════════════════════════════════════════════════════
            // FAILURE 1 — 0 rows returned
            // ════════════════════════════════════════════════════════════════
            if (iRows.isEmpty())
            {
                log.warning("[PRELIM-RETRY] FAILURE 1 | herd=" + iCurrentHerd + " returned 0 rows in My Clients.");
                boolean iRecovered = recoverWithNewHerd(pCheckType, iAttempt, YEAR);
                if (!iRecovered) break;
                continue;
            }

            // ════════════════════════════════════════════════════════════════
            // FAILURE 2 — Row found but "Herd expired"
            // ════════════════════════════════════════════════════════════════
            boolean iHerdExpired = !getDriver().findElements(By.xpath(EXPIRED_SPAN_XPATH)).isEmpty();

            if (iHerdExpired)
            {
                log.warning("[PRELIM-RETRY] FAILURE 2 | herd=" + iCurrentHerd + " is marked 'Herd expired'.");
                boolean iRecovered = recoverWithNewHerd(pCheckType, iAttempt, YEAR);
                if (!iRecovered) break;
                continue;
            }

            // ── Row found and not expired — click through to the farmer dashboard ────────────
            log.info("[PRELIM-RETRY] Row valid — clicking through to farmer dashboard.");
            //iAction("CLICK", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
            //try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

            // ── Navigate to Preliminary Checks tab ───────────────────────────────────────────
            iAction("CLICK", "XPATH", PRELIM_TAB_XPATH, null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iCurrentHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

            // ════════════════════════════════════════════════════════════════
            // FAILURE 3 — Preliminary Checks tab shows no records
            // ════════════════════════════════════════════════════════════════
            List<WebElement> iPrelimRows = getDriver().findElements(By.xpath(PRELIM_TABLE_ROW_XPATH));

            boolean iPrelimTabEmpty = iPrelimRows.isEmpty() || !getDriver().findElements(By.xpath(PRELIM_NO_RECORDS_XPATH)).isEmpty();

            if (iPrelimTabEmpty)
            {
                log.warning("[PRELIM-RETRY] FAILURE 3 | herd=" + iCurrentHerd + " Preliminary Checks tab has no records.");

                // Navigate back to My Clients before recovery
                // (we are inside a farmer dashboard so breadcrumb is available)
                try
                {
                    iAction("CLICK", "XPATH", MY_CLIENTS_BACK_XPATH, null);
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
                catch (Exception e)
                {
                    log.warning("[PRELIM-RETRY] Breadcrumb not found — using left menu.");
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
                }

                boolean iRecovered = recoverWithNewHerd(pCheckType, iAttempt, YEAR);
                if (!iRecovered) break;
                continue;
            }

            // ── All checks passed ─────────────────────────────────────────────────────────────
            log.info("[PRELIM-RETRY] ✓ Attempt " + (iAttempt + 1) + " succeeded | herd=" + iCurrentHerd + " | " + iPrelimRows.size() + " prelim record(s) found.");
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
//  PRELIMINARY CHECKS — BUSINESS STEPS
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
// Step          : Then the preliminary checks card should be visible on the dashboard
// Description   : Asserts biss-portal-preliminary-checks mat-card is visible on farmer dashboard.
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 17-04-2026
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
//                 pCheckName — e.g. "Overclaim Checks", "Dual Claim Checks"
//                 pStatus    — "Response required" or "No response required"
// Parameters    : pCheckName — check type label as shown on dashboard
//                 pStatus    — expected status string
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 17-04-2026
// ***************************************************************************************************************************************************************************************
    @And("the {string} preliminary check should show {string} on the dashboard")
    public void thePreliminaryCheckShouldShowOnTheDashboard(String pCheckName, String pStatus)
    {
        log.info("[STEP] And the '" + pCheckName + "' preliminary check should show '" + pStatus + "' on the dashboard");
        String iLocatorKey = pStatus.toLowerCase().contains("no response") ? "iPrelimCheckStatusNotRequired" : "iPrelimCheckStatusRequired";
        String iXpath = String.format(ObjReader.getLocator(iLocatorKey), pCheckName);
        iAction("VERIFYELEMENT", "XPATH", iXpath, null);
        log.info("Confirmed '" + pCheckName + "' shows: " + pStatus);
    }


    // ***************************************************************************************************************************************************************************************
// Step          : When the agent clicks View Preliminary Checks
// Description   : Clicks the "View preliminary checks" button on the dashboard card.
// Author        : Aniket Pathare | aniket.pathare@government.ie
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
// Description   : Confirms the prelims-accordion inside biss-response-page is visible.
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 17-04-2026
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
// Description   : Negative test — Submit clicked before any radio selection.
// Author        : Aniket Pathare | aniket.pathare@government.ie
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
// Description   : Asserts form stays ng-invalid after premature submit.
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 17-04-2026
// ***************************************************************************************************************************************************************************************
    @Then("a validation error should be displayed on the Preliminary Checks page")
    public void aValidationErrorShouldBeDisplayedOnThePreliminaryChecksPage()
    {
        log.info("[STEP] Then a validation error should be displayed on the Preliminary Checks page");
        boolean iFormInvalid = isVisible(By.xpath(ObjReader.getLocator("iPrelimFormInvalid")), 3);
        Assertions.assertTrue(iFormInvalid, "Expected form to remain ng-invalid after submitting without selections.");
        log.info("Validation error confirmed — form is ng-invalid as expected.");
    }


    // ***************************************************************************************************************************************************************************************
// Step          : When the agent responds to all preliminary check panels row by row
// Description   : Reads DataTable of panel names and responses. For each panel:
//                   1. Skips if green icon (no response required)
//                   2. Expands panel if collapsed (aria-expanded = false)
//                   3. Finds all row-level radios matching the response label
//                   4. Clicks each radio — re-queries DOM per click to avoid stale refs
//                 DataTable format:
//                   | Panel Name                      | Response    |
//                   | Overclaim Checks                | Accept      |
//                   | Dual Claim Checks               | No, Keep it |
//                   | No Agricultural Activity Checks | Reject      |
// Parameters    : pDataTable — DataTable with headers "Panel Name" and "Response"
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 17-04-2026
// ***************************************************************************************************************************************************************************************
    @When("the agent responds to all preliminary check panels row by row")
    public void theAgentRespondsToAllPreliminaryCheckPanelsRowByRow(DataTable pDataTable)
    {
        log.info("[STEP] When the agent responds to all preliminary check panels row by row");

        WebDriver iDriver = getDriver();
        List<Map<String, String>> iTableRows = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iEntry : iTableRows)
        {
            String iPanelLabel    = iEntry.get("Panel Name").trim();
            String iResponseLabel = iEntry.get("Response").trim();

            log.info("[STEP] Panel: '" + iPanelLabel + "' | Response: '" + iResponseLabel + "'");

            // ── 1. Skip panels with green icon ───────────────────────────────────────────────
            String iErrorPanelXpath = String.format(ObjReader.getLocator("iPrelimPanelHeaderError"), iPanelLabel);
            if (!isVisible(By.xpath(iErrorPanelXpath), 2))
            {
                log.info("[STEP] Panel '" + iPanelLabel + "' — no response required. Skipping.");
                continue;
            }

            // ── 2. Expand panel if collapsed ─────────────────────────────────────────────────
            String iPanelHeaderXpath = String.format(ObjReader.getLocator("iPrelimPanelHeader"), iPanelLabel);
            WebElement iPanelHeader = iDriver.findElement(By.xpath(iPanelHeaderXpath));
            if (!"true".equalsIgnoreCase(iPanelHeader.getAttribute("aria-expanded")))
            {
                iAction("CLICK", "XPATH", iPanelHeaderXpath, null);
                log.info("[STEP] Expanded panel: " + iPanelLabel);
                try { Thread.sleep(800); } catch (InterruptedException ie)
                { Thread.currentThread().interrupt(); }
            }

            // ── 3. Find all row-level radios matching the response label ──────────────────────
            String iRowRadioXpath = String.format(ObjReader.getLocator("iPrelimRowRadioByLabel"), iResponseLabel);
            List<WebElement> iRadioLabels = iDriver.findElements(By.xpath(iRowRadioXpath));

            if (iRadioLabels.isEmpty())
            {
                throw new AssertionError("Panel '" + iPanelLabel + "': no row-level radio buttons found with label '" + iResponseLabel + "'. Verify label matches DOM text exactly.");
            }

            log.info("[STEP] Found " + iRadioLabels.size() + " row(s) to respond to in panel: " + iPanelLabel);

            // ── 4. Click each radio — re-query per click to avoid stale refs ─────────────────
            for (int i = 0; i < iRadioLabels.size(); i++)
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
// Description   : Clicks Yes on the confirmation dialog after Submit.
// Author        : Aniket Pathare | aniket.pathare@government.ie
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
// Description   : Asserts submission completed — Submit button disappears after confirmation.
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 17-04-2026
// ***************************************************************************************************************************************************************************************
    @And("the Preliminary Checks submission should be confirmed successfully")
    public void thePreliminaryChecksSubmissionShouldBeConfirmedSuccessfully()
    {
        log.info("[STEP] And the Preliminary Checks submission should be confirmed successfully");

        log.info("[STEP] Verifying 'Response required' is NOT displayed on the Preliminary Checks card");

        String responseRequiredXpath =
                "//mat-card[.//mat-card-title[normalize-space()='Preliminary Checks']]" +
                        "//span[contains(@class,'dashboard-clarification-text')" +
                        " and normalize-space()='Response required']";
        boolean isResponseRequiredVisible = isVisible(By.xpath(responseRequiredXpath), 3);
        Assertions.assertFalse(isResponseRequiredVisible, "'Response required' is still visible on the Preliminary Checks dashboard card");
        log.info("Preliminary Checks submitted and confirmed successfully.");
        Hooks.captureStep("Preliminary Checks — submission confirmed successfully");
    }


// ===================================================================================================================================
//  PRIVATE — RECOVERY (logout + DB re-query + INET validate + re-login)
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : recoverWithNewHerd
    // Description   : Single recovery method called by ALL three failure modes.
    //                 Steps:
    //                   1. Logout current session
    //                   2. Try consecutive DB offsets (startOffset to startOffset+MAX_OFFSET_TRIES)
    //                      until an INET-validated herd is found for this check type.
    //                      Prints full DB result table to console on every query.
    //                   3. Login as resolved agent (always — session was terminated in step 1)
    //                   4. Navigate to My Clients
    //                   5. Update Hooks fields
    //
    // PATCH (20-04-2026): Previously tried only a single offset then returned false.
    //                     Now retries up to MAX_OFFSET_TRIES consecutive offsets so that
    //                     herds with no INET agent are skipped automatically instead of
    //                     causing the entire recovery to abort.
    //
    // Returns       : true if a valid replacement was found and session is ready, false if exhausted
    // Parameters    : pCheckType — OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY
    //                 pAttempt   — current outer loop iteration (offset starts at pAttempt+1)
    //                 pYear      — scheme year
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026
    // ***************************************************************************************************************************************************************************************
    private boolean recoverWithNewHerd(String pCheckType, int pAttempt, String pYear)
    {
        String iCurrentUsername = getPrelimUsername(pCheckType);
        int    iStartOffset     = pAttempt + 1;

        // How many consecutive offsets to probe before giving up in a single recovery call.
        // Covers the case where several consecutive herds have no INET agent.
        final int MAX_OFFSET_TRIES = 10;

        log.info("[PRELIM-RECOVER] Starting recovery | checkType=" + pCheckType
                + " | startOffset=" + iStartOffset);

        // ── Step 1: Logout immediately ────────────────────────────────────────────────────────
        performLogout();

        // ── Step 2: Try consecutive offsets until INET-valid herd found ──────────────────────
        String iLvcDescTarget = getLvcDescForCheckType(pCheckType);
        String iNextHerd      = null;
        String iNextUsername  = null;

        for (int iOff = iStartOffset; iOff < iStartOffset + MAX_OFFSET_TRIES; iOff++)
        {
            log.info("[PRELIM-RECOVER] Querying BISS_DATA | LVC_DESC='" + iLvcDescTarget + "' | offset=" + iOff);

            database.DBRouter.runDB("DATA", "Preliminary checks herds", pYear, String.valueOf(iOff));

            List<Map<String, Object>> iDbRows = database.DBRouter.getRows();

            // Print full result table so you can see exactly what DB returned
            printDbResultTable("PRELIMINARY CHECKS HERDS offset=" + iOff, iDbRows);

            if (iDbRows == null || iDbRows.isEmpty())
            {
                log.warning("[PRELIM-RECOVER] BISS_DATA returned 0 rows at offset=" + iOff + " — no more Pending rows exist for year=" + pYear + ". Stopping.");
                break;
            }

            for (Map<String, Object> iRow : iDbRows)
            {
                String iLvc  = Objects.toString(iRow.get("LVC_DESC"),    "").trim();
                String iHerd = Objects.toString(iRow.get("LVL_HERD_NO"), "").trim();

                if (!iLvc.equalsIgnoreCase(iLvcDescTarget) || iHerd.isEmpty()) continue;

                log.info("[PRELIM-RECOVER] Checking BISS_INET for " + iLvcDescTarget
                        + " herd=" + iHerd);

                database.DBRouter.runDB("INET", "Get Login Id for herd", iHerd);
                String iUsername = database.DBRouter.getValue("USERNAME");

                log.info("[PRELIM-RECOVER] INET result → herd=" + iHerd + " | USERNAME=" + (iUsername == null ? "null" : "'" + iUsername + "'"));

                if (iUsername != null && !iUsername.isBlank())
                {
                    iNextHerd     = iHerd;
                    iNextUsername = iUsername.trim();
                    log.info("[PRELIM-RECOVER] INET-validated replacement: " + iLvcDescTarget + " → herd=" + iNextHerd + " | username=" + iNextUsername + " (offset=" + iOff + ")");
                    break;
                }
                else
                {
                    log.warning("[PRELIM-RECOVER] No INET agent for " + iLvcDescTarget + " herd=" + iHerd + " at offset=" + iOff + " — trying offset=" + (iOff + 1));
                }
            }

            if (iNextHerd != null) break;  // found a valid herd — stop probing offsets
        }

        if (iNextHerd == null)
        {
            log.warning("[PRELIM-RECOVER] Exhausted offsets " + iStartOffset + " to "
                    + (iStartOffset + MAX_OFFSET_TRIES - 1) + " for " + pCheckType
                    + " — no INET-validated herd found. Stopping recovery.");
            return false;
        }

        // ── Step 3: Login (always — session was terminated in step 1) ────────────────────────
        log.info("[PRELIM-RECOVER] Agent: " + iCurrentUsername + " → " + iNextUsername);
        performLogin(iNextUsername);

        // ── Step 4: Navigate to My Clients ───────────────────────────────────────────────────
        navigateToMyClients();

        // ── Step 5: Update Hooks fields ──────────────────────────────────────────────────────
        setPrelimHerd(pCheckType, iNextHerd, iNextUsername);
        Hooks.RUNTIME_USERNAME = iNextUsername;

        log.info("[PRELIM-RECOVER] Recovery complete | " + pCheckType + " → herd=" + iNextHerd + " | username=" + iNextUsername);
        return true;
    }


// ===================================================================================================================================
//  PRIVATE — HOOKS FIELD ACCESSORS
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
//  PRIVATE — NAVIGATION (mirrors TC_03 exactly)
// ===================================================================================================================================

    private void performLogout()
    {
        log.info("[RELOGIN] Logging out current session...");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"), null);

            // Wait for the welcome/landing page login button to appear —
            // this confirms the session is fully terminated and we are back at the start
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
            log.info("[RELOGIN] Logout complete — landing page visible.");
        }
        catch (Exception e)
        {
            // If logout fails (e.g. session already expired), navigate directly to
            // the base URL to force a clean state rather than leaving a broken session
            log.warning("[RELOGIN] Logout click failed (" + e.getMessage()
                    + ") — navigating to base URL as fallback.");
            getDriver().navigate().to(Hooks.iUrl);
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        }
    }


    private void performLogin(String pUsername)
    {
        log.info("[RELOGIN] Logging in as: " + pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),     null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),      "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),            null);

        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));
        if (isVisible(iPinFormBy, 3))
        {
            log.info("[RELOGIN] PIN screen detected — filling slots 1–7.");
            for (int idx = 1; idx <= 7; idx++)
            {
                String iDynXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));
                By iPinBy = By.xpath(iDynXpath);
                if (isVisible(iPinBy, 1))
                {
                    WebElement iInput = getDriver().findElement(iPinBy);
                    if (iInput.getAttribute("disabled") == null && iInput.isEnabled())
                    {
                        iInput.clear();
                        iInput.sendKeys("1");
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

        if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[RELOGIN] Terms & Conditions accepted.");
        }
        log.info("[RELOGIN] Login complete for: " + pUsername);
    }

    private void navigateToMyClients()
    {
        log.info("[RELOGIN] Navigating to My Clients...");
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iAppSearchBar"),       "");
        iAction("TEXTBOX",      "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        //iAction("VERIFYELEMENT","XPATH", ObjReader.getLocator("iSearchAppLabel"),     "");
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iBissLink"),           "");
        iAction("WAITINVISIBLE","XPATH", "iScreenBuffer", "Spinner");
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"),   null);
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iViewAllTab"),         "");
        log.info("[RELOGIN] My Clients ready.");
    }


// ===================================================================================================================================
//  PRIVATE — UTILITIES
// ===================================================================================================================================

    private static boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            new WebDriverWait(CommonFunctions.getDriver(), Duration.ofSeconds(pSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception e) { return false; }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : printDbResultTable
    // Description   : Prints the full DB result set to the console as a formatted table.
    //                 Called after every BISS_DATA query inside recoverWithNewHerd so you can
    //                 see exactly what was returned (or that nothing was returned) for each offset.
    //                 Aids diagnosis when the recovery loop exhausts all offsets.
    // Parameters    : pQueryLabel — label to identify which query this result belongs to
    //                 pRows       — the result rows from DBRouter.getRows()
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 20-04-2026
    // ***************************************************************************************************************************************************************************************
    private void printDbResultTable(String pQueryLabel, List<Map<String, Object>> pRows)
    {
        if (pRows == null || pRows.isEmpty())
        {
            log.info("[DB-RESULT] " + pQueryLabel + " → 0 rows returned");
            return;
        }

        StringBuilder iSb = new StringBuilder();
        iSb.append("\n[DB-RESULT] ").append(pQueryLabel)
                .append(" → ").append(pRows.size()).append(" row(s):\n");

        // Build column list from first row keys
        Map<String, Object> iFirstRow = pRows.get(0);
        String[] iCols = iFirstRow.keySet().toArray(new String[0]);

        // Compute display width per column (capped at 40 chars to keep table readable)
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

        // Build separator and header lines
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

        // Build data rows
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

    // ***************************************************************************************************************************************************************************************
// Step          : Then the agent resolves any overclaim value exceeds EH errors if present
// Description   : After clicking Submit, rows with claimed area > eligible (ha) show a
//                 "Value exceeds EH" error inline in the cdk-column-claimedArea cell.
//
//                 Three scenarios handled:
//
//                   1. No suffix (single parcel e.g. "A1150100017"):
//                        → 1 row in group → enter the eligible value directly
//                          claimed = eligible (always ≤ eligible, no overclaim)
//
//                   2. Suffix present, split into 2+ (e.g. "A1150100017 A" + "A1150100017 B"):
//                        → Group all suffix rows by base parcel number
//                        → Distribute the shared eligible pool proportionally based on
//                          each row's original claimed value
//                        → Non-last rows: floor to 2dp (never rounds up over pool)
//                        → Last row: pool − running total (absorbs rounding remainder)
//                        → Group total guaranteed = eligible pool exactly
//
//                   3. Split into N parts (A/B/C/...):
//                        → Same proportional logic as case 2, scales to any N
//
//                 All locators sourced from ObjectRepository.properties via ObjReader.
//                 Row-scoped relative XPaths (.//td...) are used via iRow.findElements()
//                 since iAction always resolves from document root and cannot be scoped
//                 to a specific parent WebElement.
//
//                 If no errors are present the step passes silently.
//                 Feature file calls Submit again after this step.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 21-04-2026
// ***************************************************************************************************************************************************************************************
    @Then("the agent resolves any overclaim value exceeds EH errors if present")
    public void theAgentResolvesAnyOverclaimValueExceedsEHErrorsIfPresent()
    {
        log.info("[STEP] Then the agent resolves any overclaim value exceeds EH errors if present");

        WebDriver          iDriver = getDriver();
        JavascriptExecutor iJs     = (JavascriptExecutor) iDriver;

        // ── All data rows — absolute XPath from ObjectRepository ──────────────────────────
        List<WebElement> iAllRows = iDriver.findElements(By.xpath(ObjReader.getLocator("iPrelimOverclaimDataRows")));

        if (iAllRows.isEmpty())
        {
            log.info("[OVERCLAIM-FIX] No data rows found — nothing to fix.");
            return;
        }

        // ── Parallel lists — one entry per error row found ────────────────────────────────
        List<Integer> iErrorRowIndexes   = new java.util.ArrayList<>();
        List<String>  iBaseParcelNumbers = new java.util.ArrayList<>();
        List<Double>  iEligibleValues    = new java.util.ArrayList<>();
        List<Double>  iOriginalClaimed   = new java.util.ArrayList<>();

        for (int i = 0; i < iAllRows.size(); i++)
        {
            WebElement iRow = iAllRows.get(i);

            // ── Only process rows that have a "Value exceeds EH" error ───────────────────
            // Relative XPath scoped to this row — cannot use iAction (root-based only)
            List<WebElement> iErrors = iRow.findElements(By.xpath(ObjReader.getLocator("iPrelimValueExceedsEHError")));
            if (iErrors.isEmpty()) continue;

            // ── Read parcel number — strip trailing single-letter split suffix ────────────
            // DOM: <td class="cdk-column-parcelNo"> A1150100017 <span> B </span>
            // Full getText() = "A1150100017  B"   Base = strip trailing " A" / " B" etc.
            List<WebElement> iParcelCells = iRow.findElements(By.xpath(ObjReader.getLocator("iPrelimParcelNoCell")));
            if (iParcelCells.isEmpty()) continue;

            String iFullParcel = iParcelCells.get(0).getText().trim();
            String iBaseParcel = iFullParcel.replaceAll("\\s+[A-Z]$", "").trim();

            // ── Read eligible (ha) — relative XPath scoped to this row ───────────────────
            List<WebElement> iEligibleCells = iRow.findElements(By.xpath(ObjReader.getLocator("iPrelimEligibleCell")));
            if (iEligibleCells.isEmpty()) continue;

            double iEligible;
            try
            {
                iEligible = Double.parseDouble(iEligibleCells.get(0).getText().trim().replaceAll("[^0-9.]", ""));
            }
            catch (NumberFormatException e)
            {
                log.warning("[OVERCLAIM-FIX] Row " + (i + 1) + " — could not parse eligible value. Skipping.");
                continue;
            }

            // ── Read pre-filled claimed value from input — relative XPath ─────────────────
            List<WebElement> iInputs = iRow.findElements(By.xpath(ObjReader.getLocator("iPrelimClaimedInput")));
            if (iInputs.isEmpty()) continue;

            String iClaimedRaw = iInputs.get(0).getAttribute("value");
            if (iClaimedRaw == null || iClaimedRaw.trim().isEmpty()) iClaimedRaw = "0";

            double iClaimed;
            try
            {
                iClaimed = Double.parseDouble(iClaimedRaw.trim().replaceAll("[^0-9.]", ""));
            }
            catch (NumberFormatException e)
            {
                log.warning("[OVERCLAIM-FIX] Row " + (i + 1) + " — could not parse claimed value '" + iClaimedRaw + "'. Skipping.");
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
            log.info("[OVERCLAIM-FIX] No 'Value exceeds EH' errors found — nothing to fix.");
            return;
        }

        // ── Group error rows by base parcel number ────────────────────────────────────────
        java.util.Map<String, List<Integer>> iGroupMap = new java.util.LinkedHashMap<>();
        for (int g = 0; g < iErrorRowIndexes.size(); g++)
        {
            String iKey = iBaseParcelNumbers.get(g);
            iGroupMap.computeIfAbsent(iKey, k -> new java.util.ArrayList<>()).add(g);
        }

        // ── Compute assigned value per row ────────────────────────────────────────────────
        double[] iAssignedValues = new double[iErrorRowIndexes.size()];

        for (Map.Entry<String, List<Integer>> iGroup : iGroupMap.entrySet())
        {
            String        iParcel    = iGroup.getKey();
            List<Integer> iGroupIdxs = iGroup.getValue();
            double        iPool      = iEligibleValues.get(iGroupIdxs.get(0));

            // ── CASE 1: Single parcel — no suffix, not split ──────────────────────────────
            // Enter the eligible value directly — claimed = eligible (≤ eligible, no error)
            if (iGroupIdxs.size() == 1)
            {
                iAssignedValues[iGroupIdxs.get(0)] = iPool;
                log.info("[OVERCLAIM-FIX] Single parcel '" + iParcel + "' | not split | entering eligible=" + iPool);
                continue;
            }

            // ── CASE 2 & 3: Split parcel — proportional distribution across N rows ────────
            double iTotalClaimed = 0.0;
            for (int g : iGroupIdxs) iTotalClaimed += iOriginalClaimed.get(g);

            log.info("[OVERCLAIM-FIX] Split parcel '" + iParcel + "' | rows=" + iGroupIdxs.size() + " | pool=" + iPool + " | totalClaimed=" + iTotalClaimed);

            // Edge case — all claimed values are 0, distribute pool evenly
            if (iTotalClaimed == 0.0)
            {
                double iEven = Math.floor((iPool / iGroupIdxs.size()) * 100.0) / 100.0;
                for (int g : iGroupIdxs) iAssignedValues[g] = iEven;
                log.warning("[OVERCLAIM-FIX] Split parcel '" + iParcel + "' — total claimed is 0, distributing evenly: " + iEven + " each.");
                continue;
            }

            // Non-last rows: floor to 2dp — guarantees partial sum never exceeds pool
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

            // Last row absorbs remainder — group total = pool exactly, regardless of N
            int    iLastG     = iGroupIdxs.get(iGroupIdxs.size() - 1);
            double iLastShare = Math.round((iPool - iRunningTotal) * 100.0) / 100.0;
            iAssignedValues[iLastG] = iLastShare;
            log.info("[OVERCLAIM-FIX]   Row " + (iErrorRowIndexes.get(iLastG) + 1) + " | last row remainder=" + iLastShare + " | group total=" + String.format("%.2f", iRunningTotal + iLastShare));
        }

        // ── Write computed values into the DOM ────────────────────────────────────────────
        // Re-fetch all rows fresh to avoid stale element references after Angular re-render
        iAllRows = iDriver.findElements(By.xpath(ObjReader.getLocator("iPrelimOverclaimDataRows")));

        for (int g = 0; g < iErrorRowIndexes.size(); g++)
        {
            int        iRowIndex    = iErrorRowIndexes.get(g);
            String     iValueString = String.format("%.2f", iAssignedValues[g]);
            WebElement iRow         = iAllRows.get(iRowIndex);

            // Relative XPath scoped to this row — cannot use iAction (root-based only)
            WebElement iInput = iRow.findElement(By.xpath(ObjReader.getLocator("iPrelimClaimedInput")));

            // Step 1: Clear via JS — Angular reactive forms ignore Selenium clear alone
            iJs.executeScript("arguments[0].value = '';", iInput);
            iInput.clear();

            // Step 2: Type the computed value
            iInput.sendKeys(iValueString);

            // Step 3: Fire Angular input + change events so reactive binding picks up value
            iJs.executeScript("arguments[0].dispatchEvent(new Event('input',  {bubbles: true}));" + "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));", iInput);

            log.info("[OVERCLAIM-FIX] Written " + iValueString + " → row " + (iRowIndex + 1) + " (parcel=" + iBaseParcelNumbers.get(g) + ")");
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
//                   1. Check if the mat-select in cdk-column-otherAgActivity is
//                      empty / invalid (aria-invalid="true" or mat-mdc-select-empty class)
//                   2. If invalid → click the mat-select to open the CDK overlay panel
//                   3. Wait for the overlay to appear
//                   4. Click the FIRST mat-option in the panel
//                   5. If not invalid → skip silently
//
//                 Step is safe to always include — if no invalid dropdowns are present
//                 it passes silently without affecting the submission flow.
//
//                 Locators used:
//                   iPrelimAgriActivityDataRows  — all data rows in agri activity table
//                   iPrelimAgriActivityDropdown  — mat-select inside otherAgActivity cell
//                   iPrelimCdkOverlayFirstOption — first mat-option in CDK overlay panel
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 22-04-2026
// ***************************************************************************************************************************************************************************************
    @Then("the agent resolves any agricultural activity dropdown errors if present")
    public void theAgentResolvesAnyAgriculturalActivityDropdownErrorsIfPresent() throws InterruptedException {
        log.info("[STEP] Then the agent resolves any agricultural activity dropdown errors if present");

        WebDriver iDriver = getDriver();

        // ── All data rows scoped to biss-response-agactivity-table ────────────────────────
        // Using absolute XPath from ObjectRepository — scoped to the agri activity component
        // so we never accidentally touch the Overclaim or Dual Claim table rows
        List<WebElement> iAllRows = iDriver.findElements(By.xpath(ObjReader.getLocator("iPrelimAgriActivityDataRows")));

        if (iAllRows.isEmpty())
        {
            log.info("[AGRI-FIX] No Agricultural Activity rows found — nothing to fix.");
            return;
        }

        int iFixCount = 0;

        for (int i = 0; i < iAllRows.size(); i++)
        {
            WebElement iRow = iAllRows.get(i);

            // ── Find the mat-select inside cdk-column-otherAgActivity for this row ─────────
            // Relative XPath scoped to this row — cannot use iAction (root-based only)
            List<WebElement> iDropdowns = iRow.findElements(By.xpath(ObjReader.getLocator("iPrelimAgriActivityDropdown")));

            if (iDropdowns.isEmpty())
            {
                log.info("[AGRI-FIX] Row " + (i + 1) + " — no dropdown present. Skipping.");
                continue;
            }

            WebElement iDropdown = iDropdowns.get(0);

            // ── Check if dropdown is invalid / empty ──────────────────────────────────────
            // Angular marks it aria-invalid="true" when the required field has no selection.
            // Also check for mat-mdc-select-empty class as a belt-and-braces guard.
            String iAriaInvalid = iDropdown.getAttribute("aria-invalid");
            String iClasses     = iDropdown.getAttribute("class");

            boolean iIsInvalid = "true".equalsIgnoreCase(iAriaInvalid) || (iClasses != null && iClasses.contains("mat-mdc-select-empty"));

            if (!iIsInvalid)
            {
                log.info("[AGRI-FIX] Row " + (i + 1) + " — dropdown already has a value. Skipping.");
                continue;
            }

            log.info("[AGRI-FIX] Row " + (i + 1) + " — dropdown is empty/invalid. Selecting first option.");

            // ── Click the mat-select trigger to open the CDK overlay panel ─────────────────
            // Use JavaScript click — Angular mat-select triggers can be intercepted by overlays
            ((JavascriptExecutor) iDriver).executeScript("arguments[0].click();", iDropdown);

            // ── Wait for the CDK overlay panel to appear ──────────────────────────────────
            // The panel is appended to the body as a CDK overlay — not inside the table row
            try
            {
                Thread.sleep(1000);
                new org.openqa.selenium.support.ui.WebDriverWait(iDriver, java.time.Duration.ofSeconds(5)).until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjReader.getLocator("iPrelimCdkOverlayFirstOption"))));
            }
            catch (Exception e)
            {
                log.warning("[AGRI-FIX] Row " + (i + 1) + " — CDK overlay did not appear after clicking dropdown. Skipping. " + e.getMessage());
                continue;
            }

            // ── Click the first mat-option from the overlay panel ─────────────────────────
            // Absolute XPath from ObjectRepository — panel is in body overlay, not in row
            WebElement iFirstOption = iDriver.findElement(By.xpath(ObjReader.getLocator("iPrelimCdkOverlayFirstOption")));
            Thread.sleep(1000);
            String iOptionText = iFirstOption.getText().trim();
            iFirstOption.click();

            iFixCount++;
            log.info("[AGRI-FIX] Row " + (i + 1) + " — selected first option: '" + iOptionText + "'");

            // Re-fetch rows after Angular re-renders to avoid stale element on next iteration
            iAllRows = iDriver.findElements(By.xpath(ObjReader.getLocator("iPrelimAgriActivityDataRows")));
        }

        if (iFixCount > 0)
        {
            log.info("[AGRI-FIX] Fixed " + iFixCount + " agricultural activity dropdown(s). Ready to re-submit.");
        }
        else
        {
            log.info("[AGRI-FIX] No invalid dropdowns found — submission can proceed.");
        }
    }
}
