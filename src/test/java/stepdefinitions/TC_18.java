// ===================================================================================================================================
// File          : TC_18.java
// Package       : stepdefinitions
// Description   : Step definitions shared across TC_18 (Overclaim), TC_19 (Dual Claim)
//                 and TC_20 (No Agricultural Activity) Preliminary Checks automation.
//
//                 Herd numbers and agent usernames are resolved at runtime from:
//                   BISS_DATA — vwbs_land_validation (LVS_DESC = ‘Pending’)
//                   BISS_INET — tdcr_user_info / tdco_customer_asscs
//                 via the @Before(”@preliminary”) hook in Hooks.java.
//
//                 Retry flow for theAgentOpensAFarmerDashboardForPreliminaryCheckHerd:
//
//                   For each attempt (up to MAX_HERD_RETRIES):
//                     1. Search current herd on My Clients
//                     2. If 0 rows returned:
//                          → logout → DB re-query at next offset → INET validate
//                          → re-login if agent changed → update Hooks → retry
//                     3. If row found but marked “Herd expired”:
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
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

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
                log.info("[STEP] Selected '" + iResponseLabel
                        + "' on row " + (i + 1) + " of panel: " + iPanelLabel);
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
        boolean iSubmitGone = !isVisible(By.xpath(ObjReader.getLocator("iPrelimSubmitBtn")), 5);
        Assertions.assertTrue(iSubmitGone, "View preliminary checks button still visible after confirmation — " + "submission may not have completed successfully.");
        log.info("Preliminary Checks submitted and confirmed successfully.");
        Hooks.captureStep("Preliminary Checks — submission confirmed successfully");
    }


// ===================================================================================================================================
//  PRIVATE — RECOVERY (logout + DB re-query + INET validate + re-login)
// ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
// Function Name : recoverWithNewHerd
// Description   : Single recovery method called by ALL three failure modes.
//                 Mirrors TC_03 exactly:
//                   1. Logout current session
//                   2. Re-query BISS_DATA at offset = iAttempt+1
//                   3. INET-validate the herd for this check type
//                   4. Login as new agent (or same agent if unchanged)
//                   5. Navigate to My Clients
//                   6. Update Hooks fields
// Returns       : true if a valid replacement was found and session is ready, false if exhausted
// Parameters    : pCheckType — OVERCLAIM | DUAL_CLAIM | AGRI_ACTIVITY
//                 pAttempt   — current loop iteration (used as DB offset base)
//                 pYear      — scheme year
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 20-04-2026
// ***************************************************************************************************************************************************************************************
    private boolean recoverWithNewHerd(String pCheckType, int pAttempt, String pYear)
    {
        String iCurrentUsername = getPrelimUsername(pCheckType);
        int    iOffset          = pAttempt + 1;

        log.info("[PRELIM-RECOVER] Starting recovery | checkType=" + pCheckType + " | offset=" + iOffset);

        // ── Step 1: Logout immediately ────────────────────────────────────────────────────────
        performLogout();

        // ── Step 2: Re-query BISS_DATA for this check type at the next offset ────────────────
        database.DBRouter.runDB("DATA", "Preliminary checks herds", pYear, String.valueOf(iOffset));

        List<Map<String, Object>> iDbRows = database.DBRouter.getRows();

        if (iDbRows == null || iDbRows.isEmpty())
        {
            log.warning("[PRELIM-RECOVER] BISS_DATA returned 0 rows at offset=" + iOffset
                    + " — no more candidates. Stopping recovery.");
            return false;
        }

        // ── Step 3: Find and INET-validate the row for this check type ───────────────────────
        String iLvcDescTarget = getLvcDescForCheckType(pCheckType);
        String iNextHerd      = null;
        String iNextUsername  = null;

        for (Map<String, Object> iRow : iDbRows)
        {
            String iLvc  = Objects.toString(iRow.get("LVC_DESC"),    "").trim();
            String iHerd = Objects.toString(iRow.get("LVL_HERD_NO"), "").trim();

            if (!iLvc.equalsIgnoreCase(iLvcDescTarget) || iHerd.isEmpty()) continue;

            database.DBRouter.runDB("INET", "Get Login Id for herd", iHerd);
            String iUsername = database.DBRouter.getValue("USERNAME");

            if (iUsername != null && !iUsername.isBlank())
            {
                iNextHerd     = iHerd;
                iNextUsername = iUsername.trim();
                log.info("[PRELIM-RECOVER] INET-validated replacement: " + iLvcDescTarget + " → herd=" + iNextHerd + " | username=" + iNextUsername);
                break;
            }
            else
            {
                log.info("[PRELIM-RECOVER] No INET agent for " + iLvcDescTarget + " herd=" + iHerd + " at offset=" + iOffset + " — skipping.");
            }
        }

        if (iNextHerd == null)
        {
            log.warning("[PRELIM-RECOVER] No INET-validated herd found for " + pCheckType + " at offset=" + iOffset + " — stopping recovery.");
            return false;
        }

        // ── Step 4: Login (as new agent or same agent — always login since we logged out) ─────
        log.info("[PRELIM-RECOVER] Agent: " + iCurrentUsername + " → " + iNextUsername);
        performLogin(iNextUsername);

        // ── Step 5: Navigate to My Clients ───────────────────────────────────────────────────
        navigateToMyClients();

        // ── Step 6: Update Hooks fields ──────────────────────────────────────────────────────
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
                String iDynXpath = ObjReader.getLocator("iPinInputIndex")
                        .replace("{idx}", String.valueOf(idx));
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
        iAction("TEXTBOX",      "XPATH", ObjReader.getLocator("iAppSearchBar"),
                "Basic Income Support for Sustainability");
        iAction("VERIFYELEMENT","XPATH", ObjReader.getLocator("iSearchAppLabel"),     "");
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iBissLink"),           "");
        iAction("WAITINVISIBLE","XPATH", "iScreenBuffer",                             "Spinner");
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

}
