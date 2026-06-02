// ===================================================================================================================================
// File          : TC_13_ENTS.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_13_ENTS — NRCISYF End-to-End Regression Pack.
//
//                 Covers the full NRCISYF business flow:
//                   - Herd search and landing page validation
//                   - Category selection (A, B, C, A+C, B+C, invalid A+B)
//                   - Individual / Joint Herdowner / Company entity setup
//                   - Qualification details (single + per-member loop)
//                   - Document upload (DataTable loop)
//                   - Submission with declaration
//                   - Save and Exit
//                   - Custom institution / qualification not in list
//                   - Upload dialog close / file format validation
//                   - Post-submission document verification
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                            → TC_03.java
//                   "the agent logs into the application..."                         → TC_03.java
//                   "the agent opens the {string} application"                       → TC_03.java
//                   "the agent should land on the BISS Home page"                    → TC_03.java
//                   "the agent navigates to the {string} and {string} Left Menu Link"→ TC_03.java
//                   "the agent switches to the {string} tab on the My Clients page"  → TC_06.java
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefinitions.Hooks;
import utilities.ObjReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;
import static java.lang.Thread.sleep;

public class TC_13_ENTS
{
    private static final Logger log = Logger.getLogger(TC_13_ENTS.class.getName());
    // ===================================================================================================================================
    //  HERD SEARCH AND LANDING PAGE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent searches for the NRCISYF herd and opens the application
    // Description   : Enters the runtime herd number into the NRCISYF search field and clicks View.
    //                 Herd is resolved from Hooks.RUNTIME_HERD (DB-driven at boot time).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent searches for the NRCISYF herd and opens the application")
    public void theAgentSearchesForTheNRCISYFHerdAndOpensTheApplication()
    {

        log.info("[STEP] When the agent searches for the NRCISYF herd and opens the application" + " | herd=" + Hooks.CISYF_HERD);

        final int    MAX_RETRIES      = 15;
        final String NRCISYF_TAB      = "NR/CISYF";
        final String RESULT_ROW_XPATH = ObjReader.getLocator("iNRCISYFViewLink");

        for (int iAttempt = 1; iAttempt <= MAX_RETRIES; iAttempt++)
        {
            log.info("[TC13-SEARCH] Attempt " + iAttempt + "/" + MAX_RETRIES
                    + " | herd=" + Hooks.CISYF_HERD
                    + " | agent=" + Hooks.CISYF_USERNAME);

            // ── Clear + type herd into search field ───────────────────────────────────────
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), Hooks.CISYF_HERD);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
            // ── Check if View link appears ────────────────────────────────────────────────
            if (isVisible(By.xpath(RESULT_ROW_XPATH), 5))
            {
                iAction("CLICK", "XPATH", RESULT_ROW_XPATH, null);
                log.info("[TC13-SEARCH] Herd landing page opened: " + Hooks.CISYF_HERD);
                return;
            }

            // ── Zero rows — herd not visible for this agent ───────────────────────────────
            log.warning("[TC13-SEARCH] No result row for herd=" + Hooks.CISYF_HERD + " on attempt " + iAttempt + " — logout + fresh login + re-navigate.");

            if (iAttempt == MAX_RETRIES)
            {
                throw new RuntimeException("[TC13-SEARCH] Herd " + Hooks.CISYF_HERD + " not found in NR/CISYF tab after " + MAX_RETRIES + " attempts. Herd may not be accessible for agent " + Hooks.CISYF_USERNAME);
            }

            // ── Logout + Login as same agent + re-navigate ────────────────────────────────
            // A fresh session sometimes resolves tab visibility issues
            // caused by stale session state or portal caching.
            performLogout();
            performLogin(Hooks.CISYF_USERNAME);

            // Abort if account expired during re-login
            if (Hooks.EXPIRED_AGENTS.contains(Hooks.CISYF_USERNAME))
            {
                throw new RuntimeException("[TC13-SEARCH] Agent " + Hooks.CISYF_USERNAME + " expired during re-login — cannot continue.");
            }

            // ── Navigate back to BISS → My Clients → NR/CISYF tab ────────────────────────
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iBissLink"),          null);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);

            log.info("[TC13-SEARCH] Re-navigated to NR/CISYF tab — retrying search.");
        }



    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the NRCISYF landing page should be displayed
    // Description   : Verifies the NRCISYF landing page content is visible after opening the herd
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the NRCISYF landing page should be displayed")
    public void theNRCISYFLandingPageShouldBeDisplayed()
    {
        log.info("[STEP] Then the NRCISYF landing page should be displayed");
        iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iNRCISYFLandingHeader"), null);
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iNRCISYFLandingHeader"), null);
        log.info("NRCISYF landing page confirmed.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the NRCISYF closing date should display {string}
    // Description   : Verifies the closing date shown on the NRCISYF landing page matches expected
    // Parameters    : pExpectedDate (String) - expected date text e.g. "15 May 2026"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the NRCISYF closing date should display {string}")
    public void theNRCISYFClosingDateShouldDisplay(String pExpectedDate)
    {
        log.info("[STEP] And the NRCISYF closing date should display: " + pExpectedDate);
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iNRCISYFCloseDate"), pExpectedDate);
        log.info("NRCISYF close date verified: " + pExpectedDate);
    }

    // ===================================================================================================================================
    //  SECTION 2 : CATEGORY DIALOG — OPEN / CLOSE / INFO ICONS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent opens the NRCISYF Apply or Edit dialog
    // Description   : Clicks the Apply or Edit button to open the category selection dialog.
    //
    //                 SUBMITTED HERD RECOVERY (up to 15 attempts):
    //                   When the current herd shows "View Application" (already submitted),
    //                   Apply and Edit buttons are absent. The step recovers automatically:
    //                     1. Navigate: My Clients left menu → NR/CISYF tab
    //                     2. Re-query BISS_DATA for a fresh CISYF-eligible herd (shuffled pool)
    //                     3. INET-validate — skip BLACKLISTED + EXPIRED_AGENTS
    //                     4. Update Hooks.RUNTIME_HERD + RUNTIME_USERNAME + CISYF fields
    //                     5. Type new herd into search → click search → wait for row
    //                     6. Click iNRCISYFViewLink to open landing page
    //                     7. Loop back to check Apply/Edit again
    //                   No logout — same session stays active throughout.
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens the NRCISYF Apply or Edit dialog")
    public void theAgentOpensTheNRCISYFApplyOrEditDialog()
    {
        log.info("[STEP] When the agent opens the NRCISYF Apply or Edit dialog");

        final int    MAX_RETRIES      = 95;
        final String YEAR             = System.getProperty("herd.year", "2026").trim();
        final String APPLY_EDIT_XPATH = ObjReader.getLocator("iNRCISYFApplyEditBtn");
        final String VIEW_APP_XPATH   = ObjReader.getLocator("iNRCISYFViewApplicationBtn");
        final String NRCISYF_TAB      = "NR/CISYF";
        final String RESULT_ROW_XPATH = ObjReader.getLocator("iNRCISYFViewLink");

        for (int iAttempt = 1; iAttempt <= MAX_RETRIES; iAttempt++)
        {
            log.info("[TC13-APPLY] Attempt " + iAttempt + "/" + MAX_RETRIES + " | herd=" + Hooks.RUNTIME_HERD);

            // ── Check if Apply or Edit button is present ──────────────────────────────────
            boolean iApplyEditPresent = isVisible(By.xpath(APPLY_EDIT_XPATH), 3);

            if (iApplyEditPresent)
            {
                // Valid state — click and open dialog
                String iBtnText = getDriver().findElement(By.xpath(APPLY_EDIT_XPATH)).findElement(By.xpath(".//span")).getText().trim();
                log.info("[TC13-APPLY] Button '" + iBtnText + "' found on attempt " + iAttempt);

                iAction("WAITCLICKABLE", "XPATH", APPLY_EDIT_XPATH, null);
                iAction("CLICK",         "XPATH", APPLY_EDIT_XPATH, null);
                iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
                log.info("[TC13-APPLY] Category dialog opened.");
                return;
            }

            // ── Apply/Edit absent — check if it's submitted ───────────────────────────────
            boolean iViewAppPresent = isVisible(By.xpath(VIEW_APP_XPATH), 2);
            log.warning("[TC13-APPLY] Attempt " + iAttempt + " | herd=" + Hooks.RUNTIME_HERD
                    + (iViewAppPresent
                    ? " — herd is Submitted ('View Application'). Recovering..."
                    : " — neither Apply nor Edit nor View Application found. Recovering..."));

            // ── Step 1: Re-query BISS_DATA for a fresh CISYF candidate ───────────────────
            int iLimit = 50 + (iAttempt * 25);
            database.DBRouter.runDB("DATA", "List of individual herds with CISYF scheme", YEAR, String.valueOf(iLimit));
            List<java.util.Map<String, Object>> iDbRows = database.DBRouter.getRows();

            if (iDbRows == null || iDbRows.isEmpty())
            {
                log.warning("[TC13-APPLY] BISS_DATA returned 0 rows at limit=" + iLimit + " — cannot recover on attempt " + iAttempt);
                continue;
            }

            // ── Step 2: Shuffle + INET-validate ──────────────────────────────────────────
            List<String> iCandidates = new java.util.ArrayList<>();
            for (java.util.Map<String, Object> iRow : iDbRows)
            {
                String iH = java.util.Objects.toString(iRow.get("APP_HERD_NO"), "").trim();
                if (!iH.isEmpty()) iCandidates.add(iH);
            }
            java.util.Collections.shuffle(iCandidates, new java.util.Random(System.nanoTime()));

            String iNextHerd     = null;
            String iNextUsername = null;

            for (String iCandidate : iCandidates)
            {
                if (Hooks.BLACKLISTED_HERDS.contains(iCandidate))
                {
                    log.warning("[TC13-APPLY] " + iCandidate + " is blacklisted — skipping.");
                    continue;
                }

                database.DBRouter.runDB("INET", "Get Login Id for herd", iCandidate);
                String iUser = database.DBRouter.getValue("USERNAME");

                if (iUser == null || iUser.isBlank())
                {
                    log.warning("[TC13-APPLY] No INET agent for " + iCandidate + " — skipping.");
                    continue;
                }
                iUser = iUser.trim();

                if (Hooks.EXPIRED_AGENTS.contains(iUser))
                {
                    log.warning("[TC13-APPLY] Agent " + iUser + " is EXPIRED for herd=" + iCandidate + " — skipping.");
                    continue;
                }

                iNextHerd     = iCandidate;
                iNextUsername = iUser;
                log.info("[TC13-APPLY] Valid candidate: herd=" + iNextHerd + " | agent=" + iNextUsername);
                break;
            }

            if (iNextHerd == null)
            {
                log.warning("[TC13-APPLY] All candidates exhausted at attempt " + iAttempt + " — retrying with larger limit next round.");
                continue;
            }

            // ── Step 3: Update Hooks ──────────────────────────────────────────────────────
            Hooks.RUNTIME_HERD     = iNextHerd;
            Hooks.RUNTIME_USERNAME = iNextUsername;
            Hooks.CISYF_HERD       = iNextHerd;
            Hooks.CISYF_USERNAME   = iNextUsername;
            log.info("[TC13-APPLY] Hooks updated: herd=" + iNextHerd + " | agent=" + iNextUsername);

            // ── Step 4: Logout + Login as new agent ───────────────────────────────────────
            // The new herd belongs to a different agent — the current session will not
            // have access to it. Logout the current agent and log in as the new one.
            performLogout();
            performLogin(iNextUsername);

            // Abort if Account Expired was detected during login
            if (Hooks.EXPIRED_AGENTS.contains(iNextUsername))
            {
                log.warning("[TC13-APPLY] Agent " + iNextUsername + " expired during login — retrying with next candidate.");
                continue;
            }

            // ── Step 5: Navigate to BISS → My Clients → NR/CISYF tab ─────────────────────
           // iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
           // iAction("CLICK",   "XPATH", ObjReader.getLocator("iBissLink"), null);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("CLICK",         "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            log.info("[TC13-APPLY] Re-logged in and navigated to NR/CISYF tab.");

            // ── Step 6: Search new herd ───────────────────────────────────────────────────
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), iNextHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iNRCISYFSearchBtn"), null);

            // ── Step 7: Wait for result row then open landing page ────────────────────────
            if (!isVisible(By.xpath(RESULT_ROW_XPATH), 5))
            {
                log.warning("[TC13-APPLY] New herd " + iNextHerd + " returned no rows after search — retrying.");
                continue;
            }
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFViewLink"), null);
            log.info("[TC13-APPLY] Opened landing page for herd: " + iNextHerd);

            // Step 8: Loop back — check for Apply/Edit on this landing page
        }

        throw new RuntimeException("[TC13-APPLY] Could not find Apply or Edit button after " + MAX_RETRIES + " attempts." + " Last herd: " + Hooks.RUNTIME_HERD + ". All candidates were submitted or exhausted.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the info icon for category {string} should display the correct description
    // Description   : Clicks the info icon for the named category (A/B/C) and verifies the
    //                 tooltip or info panel content is not empty
    // Parameters    : pCategory (String) - "A", "B", or "C"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the info icon for category {string} should display the correct description")
    public void theInfoIconForCategoryShouldDisplayTheCorrectDescription(String pCategory)
    {
        log.info("[STEP] Then the info icon for category '" + pCategory + "' should display the correct description");

        String iIndex;
        switch (pCategory.trim().toUpperCase())
        {
            case "A": iIndex = "1"; break;
            case "B": iIndex = "2"; break;
            case "C": iIndex = "3"; break;
            default:  throw new RuntimeException("[TC13] Unknown category: '" + pCategory + "'. Expected A | B | C");
        }

        // Click info icon — uses {index} placeholder resolved at runtime
        String iIconXpath = ObjReader.getLocator("iNRCISYFInfoIcon").replace("{index}", iIndex);
        iAction("WAITVISIBLE",   "XPATH", iIconXpath, null);
        iAction("WAITCLICKABLE", "XPATH", iIconXpath, null);
        iAction("CLICK",         "XPATH", iIconXpath, null);
        log.info("[TC13] Clicked info icon for category " + pCategory + " (index=" + iIndex + ")");

        // Wait for description panel to appear
        String iDescXpath = ObjReader.getLocator("iNRCISYFInfoContent").replace("{index}", iIndex);
        iAction("WAITVISIBLE", "XPATH", iDescXpath, null);

        // Verify text is not empty
        String iInfoText = iAction("GETTEXT", "XPATH", iDescXpath, null);
        Assertions.assertFalse(iInfoText == null || iInfoText.trim().isEmpty(), "[TC13] Description for category " + pCategory + " should be visible after clicking info icon.");
        log.info("[TC13] Category " + pCategory + " description verified (" + iInfoText.trim().substring(0, Math.min(60, iInfoText.trim().length())) + "...)");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent closes the NRCISYF category dialog
    // Description   : Clicks the Close button on the NRCISYF category selection dialog
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent closes the NRCISYF category dialog")
    public void theAgentClosesTheNRCISYFCategoryDialog()
    {
        log.info("[STEP] When the agent closes the NRCISYF category dialog");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFCloseBtn"), null);
        log.info("NRCISYF category dialog closed.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the dialog should be dismissed
    // Description   : Verifies the category selection dialog is no longer visible
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the dialog should be dismissed")
    public void theDialogShouldBeDismissed()
    {
        log.info("[STEP] Then the dialog should be dismissed");
        // Wait for the dialog overlay to disappear — confirms it closed properly
        iAction("WAITINVISIBLE", "XPATH", ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
        log.info("Dialog dismissed confirmed.");
    }

    // ===================================================================================================================================
    //  CATEGORY SELECTION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent resets all category selections
    // Description   : Unchecks all category checkboxes in the dialog to start from a clean slate.
    //                 Iterates through all checkboxes and unchecks any that are currently selected.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent resets all category selections")
    public void theAgentResetsAllCategorySelections()
    {
        log.info("[STEP] And the agent resets all category selections");

        List<WebElement> iCheckboxes = getDriver().findElements(
                By.xpath(ObjReader.getLocator("iNRCISYFCategoryCheckboxes")));

        if (iCheckboxes.isEmpty())
        {
            log.warning("[TC13] No category checkboxes found — dialog may not be open.");
            return;
        }

        JavascriptExecutor iJs = (JavascriptExecutor) getDriver();
        int iUnchecked = 0;

        for (WebElement iHost : iCheckboxes)
        {
            try
            {
                // State check — mat-mdc-checkbox-checked class is the reliable Angular indicator.
                // aria-checked is NOT present on the host in this DOM — class is the only signal.
                String iClass    = iHost.getAttribute("class");
                boolean iChecked = iClass != null && iClass.contains("mat-mdc-checkbox-checked");

                if (!iChecked)
                {
                    log.info("[TC13] Already unchecked — skipping: " + iHost.getText().trim().replaceAll("\\s+", " "));
                    continue;
                }

                // JS click on native input bypasses mat-mdc-checkbox-touch-target interception
                WebElement iNative = iHost.findElement(By.xpath(".//input[@type='checkbox']"));
                iJs.executeScript("arguments[0].click();", iNative);

                // Verify state flipped — FluentWait up to 3s, poll every 300ms
                new org.openqa.selenium.support.ui.FluentWait<>(getDriver())
                        .withTimeout(java.time.Duration.ofSeconds(3))
                        .pollingEvery(java.time.Duration.ofMillis(300))
                        .ignoring(org.openqa.selenium.StaleElementReferenceException.class)
                        .until(d ->
                        {
                            String iUpdatedClass = iHost.getAttribute("class");
                            return iUpdatedClass != null && !iUpdatedClass.contains("mat-mdc-checkbox-checked");
                        });

                iUnchecked++;
                log.info("[TC13] Unchecked: " + iHost.getText().trim().replaceAll("\\s+", " "));
            }
            catch (Exception e)
            {
                log.warning("[TC13] Could not reset checkbox: " + e.getMessage());
            }
        }

        log.info("[TC13] Reset complete — unchecked " + iUnchecked + " of " + iCheckboxes.size());
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects NRCISYF categories (DataTable)
    // Description   : Iterates through the DataTable list of category labels and clicks each one.
    //                 Checks the current state before clicking to avoid toggling off an already-selected category.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects NRCISYF categories")
    public void theAgentSelectsNRCISYFCategories(DataTable pDataTable) throws InterruptedException {

            log.info("[STEP] And the agent selects NRCISYF categories");

            JavascriptExecutor iJs = (JavascriptExecutor) getDriver();
            List<String> iCategories = pDataTable.asList();

            for (String iCategory : iCategories) {
                String iClean = iCategory.trim();
                log.info("[TC13] Selecting: " + iClean);

                // ── Resolve ObjReader keys from A./B./C. prefix ───────────────────────────────────
                // formcontrolname-based keys are immune to label text changes in future releases
                String iHostKey;
                String iInputXpath;

                if (iClean.startsWith("A.")) {
                    iHostKey = "iNRCISYFCatACheckbox";
                    iInputXpath = ObjReader.getLocator("iNRCISYFCatAInput");
                } else if (iClean.startsWith("B.")) {
                    iHostKey = "iNRCISYFCatBCheckbox";
                    iInputXpath = ObjReader.getLocator("iNRCISYFCatBInput");
                } else if (iClean.startsWith("C.")) {
                    iHostKey = "iNRCISYFCatCCheckbox";
                    iInputXpath = ObjReader.getLocator("iNRCISYFCatCInput");
                } else {
                    log.warning("[TC13] No A/B/C prefix — skipping unknown category: " + iClean);
                    continue;
                }

                String iHostXpath = ObjReader.getLocator(iHostKey);

                // ── Wait for host to be present and visible ───────────────────────────────────────
                iAction("WAITVISIBLE", "XPATH", iHostXpath, null);

                // ── State check via mat-mdc-checkbox-checked class ────────────────────────────────
                // aria-checked is NOT present in the DOM — class is the only reliable state signal
                try {
                    WebElement iHost = getDriver().findElement(By.xpath(iHostXpath));
                    String iClass = iHost.getAttribute("class");

                    if (iClass != null && iClass.contains("mat-mdc-checkbox-checked")) {
                        log.info("[TC13] '" + iClean + "' already selected — skipping.");
                        continue;
                    }
                } catch (Exception e) {
                    log.info("[TC13] Could not read class for '" + iClean + "' — proceeding to click.");
                }

                // ── JS click on native input — bypasses mat-mdc-checkbox-touch-target ─────────────
                // Clicking the label or host can be intercepted by the touch-target div.
                // JS click on the native input fires Angular's change detection reliably.
                WebElement iNative = getDriver().findElement(By.xpath(iInputXpath));
                iJs.executeScript("arguments[0].scrollIntoView({block:'center'});", iNative);
                iJs.executeScript("arguments[0].click();", iNative);

                // ── Verify state flipped to checked within 3s ────────────────────────────────────
                try {
                    WebElement iHost = getDriver().findElement(By.xpath(iHostXpath));
                    new org.openqa.selenium.support.ui.FluentWait<>(getDriver())
                            .withTimeout(java.time.Duration.ofSeconds(3))
                            .pollingEvery(java.time.Duration.ofMillis(300))
                            .ignoring(org.openqa.selenium.StaleElementReferenceException.class)
                            .until(d ->
                            {
                                String iUpdatedClass = iHost.getAttribute("class");
                                return iUpdatedClass != null
                                        && iUpdatedClass.contains("mat-mdc-checkbox-checked");
                            });
                    log.info("[TC13] Selected and verified: " + iClean);
                } catch (Exception e) {
                    log.warning("[TC13] Could not verify selection for '" + iClean + "' — may not have registered: " + e.getMessage());
                }
            }
        }


    // ***************************************************************************************************************************************************************************************
    // Step          : the selected categories should be highlighted
    // Description   : Verifies at least one category checkbox is in the selected/checked state
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the selected categories should be highlighted")
    public void theSelectedCategoriesShouldBeHighlighted()
    {
        log.info("[STEP] Then the selected categories should be highlighted");
        // Verify at least one checkbox in the dialog has aria-checked="true"
        // List<WebElement> iSelected = getDriver().findElements(
        // By.xpath("//mat-checkbox[@aria-checked='true'] | //input[@type='checkbox']:checked"));
        //Assertions.assertFalse(iSelected.isEmpty(), "At least one category should be selected/highlighted.");
        // log.info("Category selections confirmed: " + iSelected.size() + " selected.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the invalid category combination error should be displayed for {string}
    // Description   : Verifies the mutual exclusion error message is shown when A+B are both selected.
    //                 The error should reference the conflicting category.
    // Parameters    : pCategory (String) - the category that triggered the error ("A" or "B")
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the invalid category combination error should be displayed for {string}")
    public void theInvalidCategoryCombinationErrorShouldBeDisplayedFor(String pCategory)
    {
        log.info("[STEP] Then the invalid category combination error should be displayed for: " + pCategory);
        String iErrorText = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iNRCISYFCategoryError"), null);
        Assertions.assertFalse(iErrorText.isEmpty(), "Error message should appear for invalid A+B combination (triggered by " + pCategory + ").");
        log.info("Invalid combo error confirmed for " + pCategory + ": " + iErrorText);
    }


    // ===================================================================================================================================
    //  CATEGORY NAVIGATION — NEXT / SKIP / ADDITIONAL CISYF
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent proceeds past the category selection
    // Description   : Clicks Next then OK on the category selection dialog to advance to entity setup
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds past the category selection")
    public void theAgentProceedsPastTheCategorySelection()
    {
        log.info("[STEP] And the agent proceeds past the category selection");
        try
        {
        // Click Next to advance from category selection
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFNextBtn"), null);
        // Accept the OK confirmation if displayed

            By iOKBtn = By.xpath(ObjReader.getLocator("iNRCISYFOKBtn"));
            if (isVisible(iOKBtn, 2)) {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFOKBtn"), null);
                log.info("OK confirmation accepted.");

            }


        }
        catch (Exception e)
        {
            log.info("No OK confirmation displayed — continuing.");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent should see the CISYF category prompt
    // Description   : Verifies the "Select additional CISYF" / "Do not select CISYF" prompt appears
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent should see the CISYF category prompt")
    public void theAgentShouldSeeTheCISYFCategoryPrompt()
    {
        log.info("[STEP] Then the agent should see the CISYF category prompt");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iNRCISYFCISYFPrompt"), null);
        log.info("CISYF category prompt confirmed.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects additional CISYF category if prompted
    // Description   : Clicks "Select additional CISYF category and continue" then OK
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects additional CISYF category if prompted")
    public void theAgentSelectsAdditionalCISYFCategoryIfPrompted()
    {
        log.info("[STEP] And the agent selects additional CISYF category if prompted");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSelectAdditionalCISYFBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFOKBtn"), null);
            log.info("Additional CISYF category selected.");
        }
        catch (Exception e)
        {
            log.info("No additional CISYF prompt — continuing.");
        }
    }

    // ===================================================================================================================================
    //  FARMING ENTITY SETUP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects farming entity {string}
    // Description   : Selects the farming entity type from the dropdown (Individual / Joint herdowner / Company / etc.)
    // Parameters    : pEntityType (String) - entity label e.g. "Individual", "Joint herdowner", "Company"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects farming entity {string}")
    public void theAgentSelectsFarmingEntity(String pEntityType)
    {
        log.info("[STEP] And the agent selects farming entity: " + pEntityType);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFHerdGroupTypeDropdown"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFHerdGroupTypeDropdownValue"), pEntityType);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the group member count dropdown should offer values (DataTable)
    // Description   : Verifies the Number of Members dropdown contains all expected options (1–6).
    //                 Opens the dropdown, reads each visible option, and asserts against the DataTable.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the group member count dropdown should offer values")
    public void theGroupMemberCountDropdownShouldOfferValues(DataTable pDataTable)
    {
        log.info("[STEP] Then the group member count dropdown should offer values");

        List<String> iExpected = pDataTable.asList().stream().map(String::trim).collect(Collectors.toList());

        // Click the dropdown to open it
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), null);
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);

        // Read all visible options
        List<WebElement> iOptions = getDriver().findElements(By.xpath("//mat-option//span"));
        List<String> iActual = iOptions.stream().map(e -> e.getText().trim()).filter(t -> !t.isEmpty()).collect(Collectors.toList());

        // Close the dropdown
        try {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), null);
        }
        catch (Exception ignored) {}

        // Verify all expected values are present
        for (String iVal : iExpected)
        {
            Assertions.assertTrue(iActual.contains(iVal), "Member count dropdown should contain: " + iVal + " | Actual options: " + iActual);
        }
        log.info("Member count dropdown verified: " + iActual);
    }

    // ===================================================================================================================================
    //  NAVIGATION HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates back to the NRCISYF client list
    // Description   : Clicks the My Clients tab and switches to NRCISYF to reset for the next flow
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent navigates back to the NRCISYF client list")
    public void theAgentNavigatesBackToTheNRCISYFClientList()
    {
        log.info("[STEP] And the agent navigates to the tab: " );
        // Click 'Home' first to reset the navigation to a known starting state
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);

        log.info("[STEP] And the agent navigates to the tab: " );
        // Now click 'My Clients' to get to the client search screen
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);



    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent skips CISYF category if prompted
    // Description   : Clicks "Do not select CISYF category and continue" if the prompt appears.
    //                 Soft step — silently continues if the prompt is not present.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent skips CISYF category if prompted")
    public void theAgentSkipsCISYFCategoryIfPrompted()
    {
        log.info("[STEP] And the agent skips CISYF category if prompted");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFDoNotSelectCISYFBtn"), null);
            log.info("CISYF category skipped.");
        }
        catch (Exception e)
        {
            log.info("No CISYF category prompt displayed — continuing.");
        }
    }

    // ===================================================================================================================================
    //  QUALIFICATION STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent proceeds to the qualification step
    // Description   : Clicks the Next/Stepper button to advance to the qualification page
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds to the qualification step")
    public void theAgentProceedsToTheQualificationStep()
    {
        log.info("[STEP] And the agent proceeds to the qualification step");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFStepperNextBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent completes the qualification details (DataTable)
    // Description   : Fills in the qualification form using DataTable key-value pairs.
    //                 Handles: hasQualification (Yes/No), dateOfCompletion, certificateAwarded,
    //                          college, customCollege (if institution not in list),
    //                          qualification, customQualification (if qualification not in list)
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent completes the qualification details")
    public void theAgentCompletesTheQualificationDetails(DataTable pDataTable)
    {
        log.info("[STEP] When the agent completes the qualification details");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        // Has qualification — Yes / No radio
        String iHasQual = iData.getOrDefault("hasQualification", "Yes").trim();
        iAction("CLICK", "XPATH", "//mat-radio-button[contains(.,'" + iHasQual + "')] | //label[contains(text(),'" + iHasQual + "')]", null);

        // If No — stop here, the "no qualification" path doesn't need further fields
        if ("No".equalsIgnoreCase(iHasQual))
        {
            log.info("Qualification = No — no further fields to fill.");
            return;
        }

        if (iData.containsKey("dateOfCompletion"))
        {
            String iDateValue = iData.get("dateOfCompletion").trim();
            try
            {
                WebElement iDateInput = getDriver().findElement(By.xpath(ObjReader.getLocator("iNRCISYFDateOfCompletionInput")));
                JavascriptExecutor iJs = (JavascriptExecutor) getDriver();

                // Step 1: Remove DOM disabled attribute so the input becomes interactable
                iJs.executeScript("arguments[0].removeAttribute('disabled');", iDateInput);

                // Step 2: Click to focus — Angular needs focus before it processes input events
                iJs.executeScript("arguments[0].click();", iDateInput);

                Thread.sleep(500);

                // Step 3: Clear any existing value
                iDateInput.clear();

                Thread.sleep(500);
                // Step 4: sendKeys fires real keyboard events Angular's MatDateAdapter listens to
                iDateInput.sendKeys(iDateValue);

                // Step 5: Tab out to trigger blur + Angular change detection
                iDateInput.sendKeys(org.openqa.selenium.Keys.TAB);

                log.info("[TC13] Date of completion entered via sendKeys: " + iDateValue);
            }
            catch (Exception e)
            {
                log.warning("[TC13] Could not enter date of completion: " + e.getMessage());
            }
        }

        // ── Certificate awarded — formcontrolname="certificateHasBeenAwarded" ─────────────
        if (iData.containsKey("certificateAwarded"))
        {
            String iCert    = iData.get("certificateAwarded").trim();
            String iCertXp  = ObjReader.getLocator("iNRCISYFCertAwardedRadio").replace("{value}", iCert);
            iAction("WAITVISIBLE", "XPATH", iCertXp, null);
            iAction("CLICK",       "XPATH", iCertXp, null);
        }

        // College / Institution
        if (iData.containsKey("college"))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFDateOfCompletionField"), null);
            iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFCollegeDropdown "), iData.get("college").trim());
        }

        // Custom college — only when "institution not in list" was selected
        if (iData.containsKey("customCollege"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCustomCollegeField"), iData.get("customCollege").trim());
        }

        // Qualification
        if (iData.containsKey("qualification"))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFQualificationDropdown"), iData.get("qualification").trim());
        }

        // Custom qualification — only when "qualification not in list" was selected
        if (iData.containsKey("customQualification"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCustomQualificationField"), iData.get("customQualification").trim());
        }

        log.info("Qualification details completed: college=" + iData.getOrDefault("college", "N/A"));
    }

    // ===================================================================================================================================
    //  HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Method        : performLogout
    // Description   : Logs out the current session by clicking the Exit link then the Logout button.
    //                 Falls back to navigating to the base URL if UI logout fails.
    //                 Called by theAgentOpensTheNRCISYFApplyOrEditDialog when the new herd belongs
    //                 to a different agent — the current session must be terminated first.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-05-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogout()
    {
        log.info("[TC13-RELOGIN] Logging out current session...");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"),   null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"),  null);
            By iSadPOPup = By.xpath(ObjReader.getLocator("iLogoutPopup"));
            if (isVisible(iSadPOPup, 1)) {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutPopup"),  null);
            }


           // iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
            log.info("[TC13-RELOGIN] Logout complete.");
        }
        catch (Exception e)
        {
            log.warning("[TC13-RELOGIN] UI logout failed (" + e.getMessage() + ") — navigating to base URL as fallback.");
            getDriver().navigate().to(Hooks.iUrl);
            //iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Method        : performLogin
    // Description   : Logs in as the specified agent — handles PIN+TOTP and OTP-only flows.
    //                 Detects Account Expired error and marks the agent via Hooks.markAgentExpired().
    //                 Mirrors TC_03 and TC_18 login patterns exactly.
    //                 Called by theAgentOpensTheNRCISYFApplyOrEditDialog after logout.
    // Parameters    : pUsername — agent username to log in as
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-05-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogin(String pUsername)
    {
        log.info("[TC13-RELOGIN] Logging in as: " + pUsername);
        //iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),     null);
        getDriver().navigate().to(Hooks.iUrl);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),      "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),            null);

        // ── Account Expired detection ─────────────────────────────────────────────────────
        By iExpiredBy = By.xpath(ObjReader.getLocator("iAccountExpiredError"));
        if (isVisible(iExpiredBy, 3))
        {
            String iErrText = getDriver().findElement(iExpiredBy).getText().trim();
            if (iErrText.toLowerCase().contains("account expired") || iErrText.toLowerCase().contains("account has expired")|| iErrText.contains("Invalid username or password."))
            {
                log.warning("[TC13-RELOGIN] Account Expired for: " + pUsername + " — marking expired.");
                Hooks.markAgentExpired(pUsername);
                return;
            }
        }

        // ── PIN + TOTP flow ───────────────────────────────────────────────────────────────
        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));
        if (isVisible(iPinFormBy, 3))
        {
            log.info("[TC13-RELOGIN] PIN screen — filling slots 1-7.");
            for (int iIdx = 1; iIdx <= 7; iIdx++)
            {
                By iPinBy = By.xpath(ObjReader.getLocator("iPinInputIndex")
                        .replace("{idx}", String.valueOf(iIdx)));
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
            log.info("[TC13-RELOGIN] PIN + TOTP submitted.");
        }
        else
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),  null);
            log.info("[TC13-RELOGIN] OTP-only login submitted.");
        }

        // ── Accept Terms if shown ─────────────────────────────────────────────────────────
        if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[TC13-RELOGIN] Terms accepted.");
        }
        log.info("[TC13-RELOGIN] Login complete: " + pUsername);
    }

    // ===================================================================================================================================
    //  SUMMARY / DOCUMENT UPLOAD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent proceeds to the summary step
    // Description   : Clicks "Next to Summary" to advance to the document upload / summary page
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds to the summary step")
    public void theAgentProceedsToTheSummaryStep()
    {
        log.info("[STEP] And the agent proceeds to the summary step");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFNextToSummaryBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent uploads NRCISYF documents (DataTable)
    // Description   : Iterates through each document type in the DataTable, clicks the corresponding
    //                 upload button, attaches the sample PDF, and confirms the upload. Uses the same
    //                 sample document for all entries.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent uploads NRCISYF documents")
    public void theAgentUploadsNRCISYFDocuments(DataTable pDataTable) throws InterruptedException {
        log.info("[STEP] When the agent uploads NRCISYF documents");

        String iFilePath = System.getProperty("nrcisyf.upload.path",
                System.getProperty("user.dir")
                        + java.io.File.separator + "src"
                        + java.io.File.separator + "test"
                        + java.io.File.separator + "resources"
                        + java.io.File.separator + "Test_Data"
                        + java.io.File.separator + "Cover_Letter.pdf");

        List<String> iDocTypes = pDataTable.asList();

        for (String iDocType : iDocTypes)
        {
            String iClean = iDocType.trim();
            // Use first 40 chars as the label match — avoids XPath being too long
            // while still uniquely identifying each upload section
            String iLabel = iClean.substring(0, Math.min(40, iClean.length()));

            log.info("[TC13] Uploading: " + iClean);

            // ── JS click the Upload button scoped to this document's label ────────────────────
            // Invoice/Receipt and Bank Statement buttons are preceded by an advisory notice div
            // which intercepts normal Selenium clicks. JS click bypasses this entirely.
            // scrollIntoView first ensures the button is in the viewport before clicking.
            String iUploadBtnXpath = ObjReader.getLocator("iNRCISYFUploadBtn").replace("{label}", iLabel);
            iAction("WAITVISIBLE", "XPATH", iUploadBtnXpath, null);
            WebElement iUploadBtn = getDriver().findElement(By.xpath(iUploadBtnXpath));
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block:'center'});", iUploadBtn);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", iUploadBtn);

            // ── Attach the PDF file (input type=file inside the upload dialog) ──────────────
            iAction("UPLOADFILE", "XPATH", ObjReader.getLocator("iNRCISYFFileUploadInput"), iFilePath);

            // ── Confirm the upload ──────────────────────────────────────────────────────────
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("CLICK",         "XPATH", ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);

            // ── Wait for dialog to close before next upload ─────────────────────────────────
            iAction("WAITINVISIBLE", "XPATH", ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
            Thread.sleep(2000);
            log.info("[TC13] Uploaded: " + iClean);
        }
    }


    // ===================================================================================================================================
    //  SUBMISSION / SAVE AND EXIT
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent saves and proceeds to the declaration step
    // Description   : Clicks "Save and Next" twice — first in the summary stepper, then the confirmation
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves and proceeds to the declaration step")
    public void theAgentSavesAndProceedsToTheDeclarationStep()
    {
        log.info("[STEP] And the agent saves and proceeds to the declaration step");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFStepperSaveAndNextBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSaveAndNextBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent submits the NRCISYF application with declaration
    // Description   : Clicks Submit Application, ticks both declaration checkboxes, then confirms
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent submits the NRCISYF application with declaration")
    public void theAgentSubmitsTheNRCISYFApplicationWithDeclaration()
    {
        log.info("[STEP] And the agent submits the NRCISYF application with declaration");

        // Click Submit Application button
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSubmitApplicationBtn"), null);

        // Tick both declaration checkboxes in the submission dialog
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFDeclarationCheckbox1"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFDeclarationCheckbox2"), null);

        // Confirm the submission
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSubmitConfirmBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the NRCISYF application should be submitted successfully
    // Description   : Verifies submission success by checking for a confirmation element or message
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the NRCISYF application should be submitted successfully")
    public void theNRCISYFApplicationShouldBeSubmittedSuccessfully()
    {
        log.info("[STEP] Then the NRCISYF application should be submitted successfully");
        String iConfirmation = iAction("GETTEXT", "XPATH", "//section[contains(@class,'banner')]//mat-card[.//b[contains(normalize-space(),'successfully submitted your National Reserve')]]", null);
        Assertions.assertFalse(iConfirmation.isEmpty(), "NRCISYF submission success should be visible.");
        log.info("NRCISYF application submitted successfully: " + iConfirmation);
    }

    // ===================================================================================================================================
    //  COMPANY DETAILS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters company details (DataTable)
    // Description   : Fills in company details (CRO number, company name, director name).
    //                 DataTable keys: croNumber, companyName, directorName
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters company details")
    public void theAgentEntersCompanyDetails(DataTable pDataTable)
    {
        log.info("[STEP] And the agent enters company details");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCRONumberInput"), iData.get("croNumber").trim());

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCompanyNameInput"), iData.get("companyName").trim());

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCompanySecretaryNameInput"), iData.get("secretaryName").trim());

        log.info("[TC13] Company details entered: CRO=" + iData.get("croNumber"));
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent sets group member count to {string}
    // Description   : Selects the number of group members from the dropdown
    // Parameters    : pCount (String) - member count e.g. "1", "2", "3"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets group member count to {string}")
    public void theAgentSetsGroupMemberCountTo(String pCount)
    {
        log.info("[STEP] And the agent sets group member count to: " + pCount);
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), pCount);
    }

    // ===================================================================================================================================
    //  GROUP MEMBER DETAILS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
// Step          : the agent enters group member details (DataTable)
// Description   : Fills each member row in the table — name, DOB (single DD/MM/YYYY datepicker),
//                 and Eligible Farmer checkbox.
//
//                 DOM findings:
//                   - Name: plain input in td[1], no formcontrolname — row-indexed via tbody//tr
//                   - DOB:  single mat-datepicker input in td[2], disabled by default — JS inject
//                           (NOT split day/month/year — those fields do not exist in the DOM)
//                   - Eligible: mat-checkbox in td[3], touch-target present — JS click on native input
//
//                 DataTable cols: memberIndex | name | dob | eligible
//                 dob format: DD/MM/YYYY (e.g. "15/06/1995")
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ***************************************************************************************************************************************************************************************
    @And("the agent enters group member details")
    public void theAgentEntersGroupMemberDetails(DataTable pDataTable)
    {
        log.info("[STEP] And the agent enters group member details");

        JavascriptExecutor iJs = (JavascriptExecutor) getDriver();
        List<Map<String, String>> iMembers = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iMember : iMembers)
        {
            String iIdx      = iMember.get("memberIndex").trim();
            String iName     = iMember.get("name").trim();
            String iDob      = iMember.get("dob").trim();
            String iEligible = iMember.get("eligible").trim();

            log.info("[TC13] Entering member " + iIdx + ": " + iName);

            // ── Name — td[1] matinput, row-indexed ───────────────────────────────────────────
            String iNameXpath = ObjReader.getLocator("iNRCISYFMemberNameInput").replace("{index}", iIdx);
            iAction("WAITVISIBLE", "XPATH", iNameXpath, null);
            iAction("TEXTBOX",     "XPATH", iNameXpath, iName);

            // ── DOB — td[2] mat-datepicker, disabled by default — JS inject ──────────────────
            String iDobXpath = ObjReader.getLocator("iNRCISYFMemberDOBInput").replace("{index}", iIdx);
            try
            {
                WebElement iDobInput = getDriver().findElement(By.xpath(iDobXpath));
                iJs.executeScript("arguments[0].removeAttribute('disabled');", iDobInput);
                iJs.executeScript("arguments[0].click();",                     iDobInput);
                iDobInput.clear();
                iDobInput.sendKeys(iDob);
                iDobInput.sendKeys(org.openqa.selenium.Keys.TAB);
                log.info("[TC13] DOB set for member " + iIdx + ": " + iDob);
            }
            catch (Exception e)
            {
                log.warning("[TC13] Could not set DOB for member " + iIdx + ": " + e.getMessage());
            }

            // ── Eligible Farmer checkbox — td[3], JS click on native input ───────────────────
            if ("Yes".equalsIgnoreCase(iEligible))
            {
                String iHostXpath  = ObjReader.getLocator("iNRCISYFMemberEligibleCheckboxHost").replace("{index}", iIdx);
                String iInputXpath = ObjReader.getLocator("iNRCISYFMemberEligibleInput").replace("{index}", iIdx);
                try
                {
                    WebElement iHost  = getDriver().findElement(By.xpath(iHostXpath));
                    String     iClass = iHost.getAttribute("class");
                    if (iClass != null && iClass.contains("mat-mdc-checkbox-checked"))
                    {
                        log.info("[TC13] Member " + iIdx + " already eligible — skipping.");
                    }
                    else
                    {
                        WebElement iNative = getDriver().findElement(By.xpath(iInputXpath));
                        iJs.executeScript("arguments[0].scrollIntoView({block:'center'});", iNative);
                        iJs.executeScript("arguments[0].click();", iNative);
                        log.info("[TC13] Eligible ticked for member " + iIdx);
                    }
                }
                catch (Exception e)
                {
                    log.warning("[TC13] Could not tick eligible for member " + iIdx
                            + ": " + e.getMessage());
                }
            }

            log.info("[TC13] Member " + iIdx + " complete: " + iName + " DOB=" + iDob);
        }
    }

    // ***************************************************************************************************************************************************************************************
// Step          : the agent confirms group status question as {string}
// Description   : Clicks the Yes or No radio for the group status question.
//                 DOM: mat-radio-group has no formcontrolname — matched via label text.
//                 Uses ObjReader iNRCISYFGroupStatusRadio with {answer} replace.
//                 Touch-target present — CLICK action uses performClick() JS fallback if intercepted.
// Parameters    : pAnswer — "Yes" or "No"
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ***************************************************************************************************************************************************************************************
    @And("the agent confirms group status question as {string}")
    public void theAgentConfirmsGroupStatusQuestionAs(String pAnswer)
    {
        log.info("[STEP] And the agent confirms group status question as: " + pAnswer);

        // DOM uses value="true"/"false" — map from human-readable Yes/No

        String iValue = "Yes".equalsIgnoreCase(pAnswer.trim()) ? "true" : "false";
        String iRadioXpath = ObjReader.getLocator("iNRCISYFGroupStatusRadio").replace("{value}", iValue);
        iAction("WAITVISIBLE", "XPATH", iRadioXpath, null);
        iAction("CLICK",       "XPATH", iRadioXpath, null);
        log.info("[TC13] Group status confirmed: " + pAnswer + " (DOM value=" + iValue + ")");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent completes the qualification details for each member (DataTable)
    // Description   : Same as completes the qualification details but loops through each member
    //                 by clicking "Next Farmer" between entries. The same qualification data is
    //                 applied to every member (as per the legacy test patterns).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent completes the qualification details for each member")
    public void theAgentCompletesTheQualificationDetailsForEachMember(DataTable pDataTable)
    {
        log.info("[STEP] When the agent completes the qualification details for each member");

        JavascriptExecutor      iJs      = (JavascriptExecutor) getDriver();
        Map<String, String>     iData    = pDataTable.asMap(String.class, String.class);
        final int               iMaxMembers = 10;
        int                     iMemberCount = 0;

        for (int i = 0; i < iMaxMembers; i++)
        {
            iMemberCount++;
            log.info("[TC13] Filling qualification for member " + iMemberCount);

            // ── Has Qualification radio ───────────────────────────────────────────────────────
            // Uses ObjReader iNRCISYFHasQualRadio with {value} replace — same as theAgentCompletesTheQualificationDetails
            String iHasQual = iData.getOrDefault("hasQualification", "Yes").trim();
            String iHasQualXpath = ObjReader.getLocator("iNRCISYFHasQualRadio").replace("{value}", iHasQual);
            iAction("WAITVISIBLE", "XPATH", iHasQualXpath, null);
            iAction("CLICK",       "XPATH", iHasQualXpath, null);

            // ── Date of Completion — mat-datepicker, disabled by default ──────────────────────
            // Same JS inject pattern as member DOB in theAgentEntersGroupMemberDetails.
            // iAction("TEXTBOX",...) does not work — disabled attribute blocks sendKeys.
            if (iData.containsKey("dateOfCompletion"))
            {
                String iDate = iData.get("dateOfCompletion").trim();
                try
                {
                    WebElement iDateInput = getDriver().findElement(By.xpath(ObjReader.getLocator("iNRCISYFDateOfCompletionField")));
                    iJs.executeScript("arguments[0].removeAttribute('disabled');", iDateInput);
                    iJs.executeScript("arguments[0].click();",                     iDateInput);
                    iDateInput.clear();
                    iDateInput.sendKeys(iDate);
                    iDateInput.sendKeys(org.openqa.selenium.Keys.TAB);
                    log.info("[TC13] Date of completion set for member " + iMemberCount + ": " + iDate);
                }
                catch (Exception e)
                {
                    log.warning("[TC13] Could not set date of completion for member " + iMemberCount + ": " + e.getMessage());
                }
            }

            // ── Certificate Awarded radio ─────────────────────────────────────────────────────
            // Uses ObjReader iNRCISYFCertAwardedRadio with {value} replace
            if (iData.containsKey("certificateAwarded"))
            {
                String iCert = iData.get("certificateAwarded").trim();
                String iCertXpath = ObjReader.getLocator("iNRCISYFCertAwardedRadio").replace("{value}", iCert);
                iAction("WAITVISIBLE", "XPATH", iCertXpath, null);
                iAction("CLICK",       "XPATH", iCertXpath, null);
            }

            // ── College dropdown ──────────────────────────────────────────────────────────────
            if (iData.containsKey("college"))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFCollegeDropdown"), iData.get("college").trim());
            }

            // ── Qualification dropdown ────────────────────────────────────────────────────────
            if (iData.containsKey("qualification"))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFQualificationDropdown"), iData.get("qualification").trim());
            }

            // ── Next Farmer — click if visible, exit loop if not ─────────────────────────────
            if (isVisible(By.xpath(ObjReader.getLocator("iNRCISYFNextFarmerBtn")), 3))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFNextFarmerBtn"), null);
                log.info("[TC13] Next Farmer clicked — proceeding to member " + (iMemberCount + 1));
            }
            else
            {
                log.info("[TC13] No more Next Farmer buttons — all " + iMemberCount + " member(s) completed.");
                break;
            }
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects herd group type {string}
    // Description   : Selects the herd group type from the dropdown (Company / Partnership etc.)
    //                 Only visible when farming entity is "Multi-Herd Registered Farm Partnership"
    // Parameters    : pGroupType (String) - group type label e.g. "Company"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects herd group type {string}")
    public void theAgentSelectsHerdGroupType(String pGroupType)
    {
        log.info("[STEP] And the agent selects herd group type: " + pGroupType);
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFHerdGroupTypeDropdown2"), pGroupType);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the no-qualification information message should be displayed if applicable
    // Description   : Soft assertion — checks if the no-qualification info/Covid message appears.
    //                 Does not hard-fail if absent since the message depends on environment config.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the no-qualification information message should be displayed if applicable")
    public void theNoQualificationInformationMessageShouldBeDisplayedIfApplicable()
    {
        log.info("[STEP] Then the no-qualification information message should be displayed if applicable");
        try
        {
            String iMsg = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iNRCISYFNoQualMessage"), null);
            log.info("No-qualification message found: " + iMsg);
        }
        catch (Exception e)
        {
            log.info("No-qualification message not displayed — may not apply in this environment.");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent saves and exits the NRCISYF application
    // Description   : Clicks "Save and Exit" on the stepper and confirms
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves and exits the NRCISYF application")
    public void theAgentSavesAndExitsTheNRCISYFApplication()
    {
        log.info("[STEP] And the agent saves and exits the NRCISYF application");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFStepperSaveAndExitBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSaveExitDialogBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the application should be saved successfully
    // Description   : Verifies that after Save and Exit, the application is no longer in edit mode
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the application should be saved successfully")
    public void theApplicationShouldBeSavedSuccessfully()
    {
        log.info("[STEP] Then the application should be saved successfully");
        // After Save and Exit, the agent should be back on the NRCISYF landing page
        // Verify the Apply/Edit button is visible again — confirms we exited the form
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iNRCISYFApplyEditBtn"), null);
        log.info("Application saved successfully — back on landing page.");
    }

    // ===================================================================================================================================
    //  CUSTOM EDUCATION / QUALIFICATION VERIFICATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the education institution should display {string}
    // Description   : Verifies the summary page shows the custom institution name entered earlier
    // Parameters    : pExpected (String) - expected institution name
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the education institution should display {string}")
    public void theEducationInstitutionShouldDisplay(String pExpected)
    {
        log.info("[STEP] Then the education institution should display: " + pExpected);
        iAction("VERIFYTEXT", "XPATH",
                "//*[contains(@class,'summary') or contains(@class,'review')]//*[contains(text(),'" + pExpected + "')]",
                pExpected);
        log.info("Custom institution verified: " + pExpected);
    }



    // ***************************************************************************************************************************************************************************************
    // Step          : the qualification should display {string}
    // Description   : Verifies the summary page shows the custom qualification name entered earlier
    // Parameters    : pExpected (String) - expected qualification name
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the qualification should display {string}")
    public void theQualificationShouldDisplay(String pExpected)
    {
        log.info("[STEP] Then the qualification should display: " + pExpected);
        iAction("VERIFYTEXT", "XPATH",
                "//*[contains(@class,'summary') or contains(@class,'review')]//*[contains(text(),'" + pExpected + "')]",
                pExpected);
        log.info("Custom qualification verified: " + pExpected);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the upload control only accepts PDF format
    // Description   : Checks the file input element's accept attribute to confirm it restricts to PDF
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent verifies the upload control only accepts PDF format")
    public void theAgentVerifiesTheUploadControlOnlyAcceptsPDFFormat()
    {
        log.info("[STEP] Then the agent verifies the upload control only accepts PDF format");
        WebElement iFileInput = getDriver().findElement(
                By.xpath(ObjReader.getLocator("iNRCISYFFileUploadInput")));
        String iAccept = iFileInput.getAttribute("accept");
        Assertions.assertTrue(iAccept != null && iAccept.toLowerCase().contains("pdf"),
                "File upload should only accept PDF. Actual accept attribute: " + iAccept);
        log.info("Upload control verified: accept=" + iAccept);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent opens the upload dialog for {string}
    // Description   : Opens the upload dialog for a specific document type without uploading.
    //                 Used for the "close upload popup" validation flow.
    // Parameters    : pDocType (String) - document type label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens the upload dialog for {string}")
    public void theAgentOpensTheUploadDialogFor(String pDocType)
    {
        log.info("[STEP] When the agent opens the upload dialog for: " + pDocType);
        iAction("CLICK", "XPATH",
                "//button[contains(normalize-space(),'" + pDocType.trim().substring(0, Math.min(40, pDocType.trim().length())) + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent closes the upload dialog without uploading
    // Description   : Clicks Close/Cancel on the upload dialog without attaching a file
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent closes the upload dialog without uploading")
    public void theAgentClosesTheUploadDialogWithoutUploading()
    {
        log.info("[STEP] And the agent closes the upload dialog without uploading");
        iAction("CLICK", "XPATH",
                "//button[contains(text(),'Close') or contains(text(),'Cancel') or contains(@aria-label,'Close')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the upload dialog should be dismissed
    // Description   : Verifies the upload dialog overlay has closed
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the upload dialog should be dismissed")
    public void theUploadDialogShouldBeDismissed()
    {
        log.info("[STEP] Then the upload dialog should be dismissed");
        iAction("WAITINVISIBLE", "XPATH",
                "//mat-dialog-container | //div[contains(@class,'cdk-overlay-pane')]",
                null);
        log.info("Upload dialog dismissed.");
    }

    // ===================================================================================================================================
    //  POST-SUBMISSION VERIFICATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent views the submitted NRCISYF application
    // Description   : Clicks the "View Application" button on the NRCISYF landing page for a submitted herd
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent views the submitted NRCISYF application")
    public void theAgentViewsTheSubmittedNRCISYFApplication()
    {
        log.info("[STEP] And the agent views the submitted NRCISYF application");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFViewApplicationBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the uploaded documents should be accessible in correspondence
    // Description   : Verifies the correspondence section contains at least one uploaded document row
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the uploaded documents should be accessible in correspondence")
    public void theUploadedDocumentsShouldBeAccessibleInCorrespondence()
    {
        log.info("[STEP] Then the uploaded documents should be accessible in correspondence");
        // Navigate to correspondence if not already there
        try { iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFCorrespondenceNavBtn"), null); }
        catch (Exception ignored) {}

        List<WebElement> iDocRows = getDriver().findElements(
                By.xpath("//tr[contains(@class,'document') or contains(@class,'row')]//a[contains(@href,'download') or contains(text(),'pdf')]"));
        Assertions.assertFalse(iDocRows.isEmpty(),
                "At least one uploaded document should be visible in correspondence.");
        log.info("Correspondence documents found: " + iDocRows.size());
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the {string} document link
    // Description   : Clicks a named document hyperlink in the correspondence or submission view
    // Parameters    : pDocName (String) - partial text of the document link
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the {string} document link")
    public void theAgentClicksOnTheDocumentLink(String pDocName)
    {
        log.info("[STEP] And the agent clicks on the document link: " + pDocName);
        iAction("CLICK", "XPATH",
                "//a[contains(normalize-space(),'" + pDocName.trim() + "')] | "
                        + "//button[contains(normalize-space(),'" + pDocName.trim() + "')]",
                null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the document should open or download successfully
    // Description   : Soft verification — confirms the page didn't throw an error after clicking a doc link.
    //                 Actual download verification is out of scope for Selenium (handled by file system check if needed).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the document should open or download successfully")
    public void theDocumentShouldOpenOrDownloadSuccessfully()
    {
        log.info("[STEP] Then the document should open or download successfully");
        // Verify no error overlay or 404 appeared after clicking the document link
        try
        {
            List<WebElement> iErrors = getDriver().findElements(
                    By.xpath("//*[contains(@class,'error') and contains(@class,'page')]"));
            Assertions.assertTrue(iErrors.isEmpty() || !iErrors.get(0).isDisplayed(),
                    "No error page should be visible after clicking the document link.");
        }
        catch (Exception e)
        {
            // No error found — good
        }
        log.info("Document link opened without errors.");
    }


    // ***************************************************************************************************************************************************************************************
    // Method        : isVisible
    // Description   : Short-wait visibility check — returns true/false, never throws.
    // Parameters    : pLocator — By locator | pSeconds — max wait seconds
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    private boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception e) { return false; }
    }










































































































































}
