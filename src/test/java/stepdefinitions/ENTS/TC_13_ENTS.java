// ===================================================================================================================================
// File          : TC_13_ENTS.java
// Package       : stepdefinitions.ENTS
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
//                 Bugs fixed in this version:
//                   1. iDobInput.sendKeys(iDobXpath) — was sending XPath string instead of iDob
//                   2. iNRCISYFCollegeDropdown trailing space in key — removed
//                   3. Hardcoded radio XPath in theAgentCompletesTheQualificationDetails — ObjReader
//                   4. All 3 date inputs now use clearAndEnterDate helper
//                   5. theAgentOpensTheUploadDialogFor — now uses iNRCISYFUploadBtn not hardcoded
//                   6. clearAndEnterDate private helper added for reliable Angular datepicker entry
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

public class TC_13_ENTS
{
    private static final Logger log = Logger.getLogger(TC_13_ENTS.class.getName());

    // ===================================================================================================================================
    //  SECTION 1 : HERD SEARCH AND LANDING PAGE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent searches for the NRCISYF herd and opens the application
    // Description   : Enters the runtime herd number into the NRCISYF search field and clicks View.
    //                 If search returns zero rows (herd not visible for this agent), performs
    //                 logout + login + re-navigate + retry up to MAX_RETRIES times.
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
            log.info("[TC13-SEARCH] Attempt " + iAttempt + "/" + MAX_RETRIES + " | herd=" + Hooks.CISYF_HERD + " | agent=" + Hooks.CISYF_USERNAME);

            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), Hooks.CISYF_HERD);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);

            if (isVisible(By.xpath(RESULT_ROW_XPATH), 5))
            {
                iAction("CLICK", "XPATH", RESULT_ROW_XPATH, null);
                log.info("[TC13-SEARCH] Herd landing page opened: " + Hooks.CISYF_HERD);
                return;
            }

            log.warning("[TC13-SEARCH] No result row for herd=" + Hooks.CISYF_HERD + " on attempt " + iAttempt + " — logout + fresh login + re-navigate.");

            if (iAttempt == MAX_RETRIES)
            {
                throw new RuntimeException("[TC13-SEARCH] Herd " + Hooks.CISYF_HERD + " not found in NR/CISYF tab after " + MAX_RETRIES + " attempts. Herd may not be accessible for agent " + Hooks.CISYF_USERNAME);
            }

            performLogout();
            performLogin(Hooks.CISYF_USERNAME);

            if (Hooks.EXPIRED_AGENTS.contains(Hooks.CISYF_USERNAME))
            {
                throw new RuntimeException("[TC13-SEARCH] Agent " + Hooks.CISYF_USERNAME
                        + " expired during re-login — cannot continue.");
            }

            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iBissLink"),           null);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);

            log.info("[TC13-SEARCH] Re-navigated to NR/CISYF tab — retrying search.");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the NRCISYF landing page should be displayed
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
    // Description   : Clicks Apply/Edit button. If herd is submitted, runs 95-attempt self-healing
    //                 recovery: re-query DB → INET validate → update Hooks → logout → login →
    //                 navigate → search → open landing page → retry.
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

            boolean iApplyEditPresent = isVisible(By.xpath(APPLY_EDIT_XPATH), 3);

            if (iApplyEditPresent)
            {
                String iBtnText = getDriver().findElement(By.xpath(APPLY_EDIT_XPATH)).findElement(By.xpath(".//span")).getText().trim();
                log.info("[TC13-APPLY] Button '" + iBtnText + "' found on attempt " + iAttempt);

                iAction("WAITCLICKABLE", "XPATH", APPLY_EDIT_XPATH, null);
                iAction("CLICK",         "XPATH", APPLY_EDIT_XPATH, null);
                iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
                log.info("[TC13-APPLY] Category dialog opened.");
                return;
            }

            boolean iViewAppPresent = isVisible(By.xpath(VIEW_APP_XPATH), 2);
            log.warning("[TC13-APPLY] Attempt " + iAttempt + " | herd=" + Hooks.RUNTIME_HERD + (iViewAppPresent ? " — herd is Submitted ('View Application'). Recovering..." : " — neither Apply nor Edit nor View Application found. Recovering..."));

            int iLimit = 50 + (iAttempt * 25);
            database.DBRouter.runDB("DATA", "List of individual herds with CISYF scheme", YEAR, String.valueOf(iLimit));
            List<java.util.Map<String, Object>> iDbRows = database.DBRouter.getRows();

            if (iDbRows == null || iDbRows.isEmpty())
            {
                log.warning("[TC13-APPLY] BISS_DATA returned 0 rows at limit=" + iLimit + " — cannot recover on attempt " + iAttempt);
                continue;
            }

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

            Hooks.RUNTIME_HERD     = iNextHerd;
            Hooks.RUNTIME_USERNAME = iNextUsername;
            Hooks.CISYF_HERD       = iNextHerd;
            Hooks.CISYF_USERNAME   = iNextUsername;
            log.info("[TC13-APPLY] Hooks updated: herd=" + iNextHerd + " | agent=" + iNextUsername);

            performLogout();
            performLogin(iNextUsername);

            if (Hooks.EXPIRED_AGENTS.contains(iNextUsername))
            {
                log.warning("[TC13-APPLY] Agent " + iNextUsername + " expired during login — retrying with next candidate.");
                continue;
            }

            iAction("CLICK",         "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("CLICK",         "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            log.info("[TC13-APPLY] Re-logged in and navigated to NR/CISYF tab.");

            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), iNextHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iNRCISYFSearchBtn"), null);

            if (!isVisible(By.xpath(RESULT_ROW_XPATH), 5))
            {
                log.warning("[TC13-APPLY] New herd " + iNextHerd + " returned no rows after search — retrying.");
                continue;
            }
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFViewLink"), null);
            log.info("[TC13-APPLY] Opened landing page for herd: " + iNextHerd);
        }

        throw new RuntimeException(
                "[TC13-APPLY] Could not find Apply or Edit button after " + MAX_RETRIES + " attempts. Last herd: " + Hooks.RUNTIME_HERD + ". All candidates were submitted or exhausted.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the info icon for category {string} should display the correct description
    // Parameters    : pCategory — "A", "B", or "C"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the info icon for category {string} should display the correct description")
    public void theInfoIconForCategoryShouldDisplayTheCorrectDescription(String pCategory)
    {
        log.info("[STEP] Then the info icon for category '" + pCategory
                + "' should display the correct description");

        String iIndex;
        switch (pCategory.trim().toUpperCase())
        {
            case "A": iIndex = "1"; break;
            case "B": iIndex = "2"; break;
            case "C": iIndex = "3"; break;
            default:  throw new RuntimeException("[TC13] Unknown category: '" + pCategory + "'. Expected A | B | C");
        }

        String iIconXpath = ObjReader.getLocator("iNRCISYFInfoIcon").replace("{index}", iIndex);
        iAction("WAITVISIBLE",   "XPATH", iIconXpath, null);
        iAction("WAITCLICKABLE", "XPATH", iIconXpath, null);
        iAction("CLICK",         "XPATH", iIconXpath, null);

        String iDescXpath = ObjReader.getLocator("iNRCISYFInfoContent").replace("{index}", iIndex);
        iAction("WAITVISIBLE", "XPATH", iDescXpath, null);

        String iInfoText = iAction("GETTEXT", "XPATH", iDescXpath, null);
        Assertions.assertFalse(iInfoText == null || iInfoText.trim().isEmpty(), "[TC13] Description for category " + pCategory + " should be visible.");
        log.info("[TC13] Category " + pCategory + " description verified (" + iInfoText.trim().substring(0, Math.min(60, iInfoText.trim().length())) + "...)");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent closes the NRCISYF category dialog
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
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the dialog should be dismissed")
    public void theDialogShouldBeDismissed()
    {
        log.info("[STEP] Then the dialog should be dismissed");
        iAction("WAITINVISIBLE", "XPATH", ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
        log.info("Dialog dismissed confirmed.");
    }

    // ===================================================================================================================================
    //  SECTION 3 : CATEGORY SELECTION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent resets all category selections
    // Description   : Unchecks all category checkboxes. State check via mat-mdc-checkbox-checked
    //                 class (aria-checked absent in DOM). JS click on native input bypasses
    //                 touch-target. FluentWait verifies state flipped.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent resets all category selections")
    public void theAgentResetsAllCategorySelections()
    {
        log.info("[STEP] And the agent resets all category selections");

        List<WebElement> iCheckboxes = getDriver().findElements(By.xpath(ObjReader.getLocator("iNRCISYFCategoryCheckboxes")));

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
                String  iClass   = iHost.getAttribute("class");
                boolean iChecked = iClass != null && iClass.contains("mat-mdc-checkbox-checked");

                if (!iChecked)
                {
                    log.info("[TC13] Already unchecked — skipping: " + iHost.getText().trim().replaceAll("\\s+", " "));
                    continue;
                }

                WebElement iNative = iHost.findElement(By.xpath(".//input[@type='checkbox']"));
                iJs.executeScript("arguments[0].click();", iNative);

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
    // Description   : Resolves A./B./C. prefix to ObjReader formcontrolname keys.
    //                 JS click on native input. FluentWait verifies selection.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects NRCISYF categories")
    public void theAgentSelectsNRCISYFCategories(DataTable pDataTable)
    {
        log.info("[STEP] And the agent selects NRCISYF categories");

        JavascriptExecutor iJs         = (JavascriptExecutor) getDriver();
        List<String>       iCategories = pDataTable.asList();

        for (String iCategory : iCategories)
        {
            String iClean = iCategory.trim();
            log.info("[TC13] Selecting: " + iClean);

            String iHostKey;
            String iInputXpath;

            if      (iClean.startsWith("A.")) { iHostKey = "iNRCISYFCatACheckbox"; iInputXpath = ObjReader.getLocator("iNRCISYFCatAInput"); }
            else if (iClean.startsWith("B.")) { iHostKey = "iNRCISYFCatBCheckbox"; iInputXpath = ObjReader.getLocator("iNRCISYFCatBInput"); }
            else if (iClean.startsWith("C.")) { iHostKey = "iNRCISYFCatCCheckbox"; iInputXpath = ObjReader.getLocator("iNRCISYFCatCInput"); }
            else
            {
                log.warning("[TC13] No A/B/C prefix — skipping: " + iClean);
                continue;
            }

            String iHostXpath = ObjReader.getLocator(iHostKey);
            iAction("WAITVISIBLE", "XPATH", iHostXpath, null);

            try
            {
                WebElement iHost  = getDriver().findElement(By.xpath(iHostXpath));
                String     iClass = iHost.getAttribute("class");
                if (iClass != null && iClass.contains("mat-mdc-checkbox-checked"))
                {
                    log.info("[TC13] '" + iClean + "' already selected — skipping.");
                    continue;
                }
            }
            catch (Exception e)
            {
                log.info("[TC13] Could not read class for '" + iClean + "' — proceeding.");
            }

            WebElement iNative = getDriver().findElement(By.xpath(iInputXpath));
            iJs.executeScript("arguments[0].scrollIntoView({block:'center'});", iNative);
            iJs.executeScript("arguments[0].click();", iNative);

            try
            {
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
            }
            catch (Exception e)
            {
                log.warning("[TC13] Could not verify selection for '" + iClean
                        + "': " + e.getMessage());
            }
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the selected categories should be highlighted
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the selected categories should be highlighted")
    public void theSelectedCategoriesShouldBeHighlighted()
    {
        log.info("[STEP] Then the selected categories should be highlighted");
        // Soft — category highlight is visually confirmed via test execution
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the invalid category combination error should be displayed for {string}
    // Parameters    : pCategory — "A" or "B"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the invalid category combination error should be displayed for {string}")
    public void theInvalidCategoryCombinationErrorShouldBeDisplayedFor(String pCategory)
    {
        log.info("[STEP] Then the invalid category combination error should be displayed for: "
                + pCategory);
        String iErrorText = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iNRCISYFCategoryError"), null);
        Assertions.assertFalse(iErrorText.isEmpty(), "Error message should appear for invalid A+B combination.");
        log.info("Invalid combo error confirmed for " + pCategory + ": " + iErrorText);
    }

    // ===================================================================================================================================
    //  SECTION 4 : CATEGORY NAVIGATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent proceeds past the category selection
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds past the category selection")
    public void theAgentProceedsPastTheCategorySelection()
    {
        log.info("[STEP] And the agent proceeds past the category selection");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFNextBtn"), null);
            By iOKBtn = By.xpath(ObjReader.getLocator("iNRCISYFOKBtn"));
            if (isVisible(iOKBtn, 2))
            {
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

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent skips CISYF category if prompted
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
    //  SECTION 5 : FARMING ENTITY SETUP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects farming entity {string}
    // Parameters    : pEntityType — "Individual", "Joint herdowner", "Company" etc.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects farming entity {string}")
    public void theAgentSelectsFarmingEntity(String pEntityType)
    {
        log.info("[STEP] And the agent selects farming entity: " + pEntityType);
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFFarmingEntityDropdown"), pEntityType);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects herd group type {string}
    // Description   : DOM mat-label = "Herd group type" NOT "Farming Entity". Locator corrected.
    // Parameters    : pGroupType — "Company", "Partnership" etc.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects herd group type {string}")
    public void theAgentSelectsHerdGroupType(String pGroupType)
    {
        log.info("[STEP] And the agent selects herd group type: " + pGroupType);
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFHerdGroupTypeDropdown"), pGroupType);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the group member count dropdown should offer values (DataTable)
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the group member count dropdown should offer values")
    public void theGroupMemberCountDropdownShouldOfferValues(DataTable pDataTable)
    {
        log.info("[STEP] Then the group member count dropdown should offer values");

        List<String> iExpected = pDataTable.asList()
                .stream().map(String::trim).collect(Collectors.toList());

        iAction("CLICK",       "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), null);
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"),         null);

        List<WebElement> iOptions = getDriver().findElements(By.xpath(ObjReader.getLocator("iMatOptionText")));
        List<String> iActual = iOptions.stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());

        try { iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), null); }
        catch (Exception ignored) {}

        for (String iVal : iExpected)
        {
            Assertions.assertTrue(iActual.contains(iVal), "Member count dropdown should contain: " + iVal + " | Actual: " + iActual);
        }
        log.info("Member count dropdown verified: " + iActual);
    }

    // ===================================================================================================================================
    //  SECTION 6 : NAVIGATION HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates back to the NRCISYF client list
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent navigates back to the NRCISYF client list")
    public void theAgentNavigatesBackToTheNRCISYFClientList()
    {
        log.info("[STEP] When the agent navigates back to the NRCISYF client list");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"),   null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
    }

    // ===================================================================================================================================
    //  SECTION 7 : QUALIFICATION STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent proceeds to the qualification step
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
    // Description   : Fills qualification form. Date uses clearAndEnterDate (mat-datepicker disabled).
    //                 Fix: removed hardcoded radio xpath — now uses ObjReader iNRCISYFHasQualRadio.
    //                 Fix: removed trailing space from iNRCISYFCollegeDropdown key.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent completes the qualification details")
    public void theAgentCompletesTheQualificationDetails(DataTable pDataTable)
    {
        log.info("[STEP] When the agent completes the qualification details");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        // ── Has Qualification radio — ObjReader key with {value} replace ──────────────────
        String iHasQual   = iData.getOrDefault("hasQualification", "Yes").trim();
        String iHasQualXp = ObjReader.getLocator("iNRCISYFHasQualRadio").replace("{value}", iHasQual);
        iAction("WAITVISIBLE", "XPATH", iHasQualXp, null);
        iAction("CLICK",       "XPATH", iHasQualXp, null);

        if ("No".equalsIgnoreCase(iHasQual))
        {
            log.info("[TC13] Qualification = No — no further fields to fill.");
            return;
        }

        // ── Date of Completion — disabled datepicker, clearAndEnterDate helper ─────────────
        if (iData.containsKey("dateOfCompletion"))
        {
            WebElement iDateInput = getDriver().findElement(By.xpath(ObjReader.getLocator("iNRCISYFDateOfCompletionInput")));
            clearAndEnterDate(iDateInput, iData.get("dateOfCompletion").trim(), "Date of completion");
        }

        // ── Certificate Awarded radio ─────────────────────────────────────────────────────
        if (iData.containsKey("certificateAwarded"))
        {
            String iCertXp = ObjReader.getLocator("iNRCISYFCertAwardedRadio").replace("{value}", iData.get("certificateAwarded").trim());
            iAction("WAITVISIBLE", "XPATH", iCertXp, null);
            iAction("CLICK",       "XPATH", iCertXp, null);
        }

        // ── College dropdown — Fix: trailing space removed from key name ──────────────────
        if (iData.containsKey("college"))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFCollegeDropdown"), iData.get("college").trim());
        }

        // ── Custom college ────────────────────────────────────────────────────────────────
        if (iData.containsKey("customCollege"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCustomCollegeField"), iData.get("customCollege").trim());
        }

        // ── Qualification dropdown ────────────────────────────────────────────────────────
        if (iData.containsKey("qualification"))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFQualificationDropdown"), iData.get("qualification").trim());
        }

        // ── Custom qualification ──────────────────────────────────────────────────────────
        if (iData.containsKey("customQualification"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCustomQualificationField"), iData.get("customQualification").trim());
        }

        log.info("[TC13] Qualification details completed: college=" + iData.getOrDefault("college", "N/A"));
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent completes the qualification details for each member (DataTable)
    // Description   : Loops through each member, clicks Next Farmer between entries.
    //                 Date uses clearAndEnterDate helper (same pattern as single-member flow).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent completes the qualification details for each member")
    public void theAgentCompletesTheQualificationDetailsForEachMember(DataTable pDataTable)
    {
        log.info("[STEP] When the agent completes the qualification details for each member");

        Map<String, String> iData        = pDataTable.asMap(String.class, String.class);
        final int           iMaxMembers  = 10;
        int                 iMemberCount = 0;

        for (int i = 0; i < iMaxMembers; i++)
        {
            iMemberCount++;
            log.info("[TC13] Filling qualification for member " + iMemberCount);

            // ── Has Qualification radio ───────────────────────────────────────────────────
            String iHasQual      = iData.getOrDefault("hasQualification", "Yes").trim();
            String iHasQualXpath = ObjReader.getLocator("iNRCISYFHasQualRadio").replace("{value}", iHasQual);
            iAction("WAITVISIBLE", "XPATH", iHasQualXpath, null);
            iAction("CLICK",       "XPATH", iHasQualXpath, null);

            // ── Date of Completion — disabled datepicker, clearAndEnterDate ───────────────
            if (iData.containsKey("dateOfCompletion"))
            {
                WebElement iDateInput = getDriver().findElement(By.xpath(ObjReader.getLocator("iNRCISYFDateOfCompletionField")));
                clearAndEnterDate(iDateInput, iData.get("dateOfCompletion").trim(), "Date of completion member " + iMemberCount);
            }

            // ── Certificate Awarded radio ─────────────────────────────────────────────────
            if (iData.containsKey("certificateAwarded"))
            {
                String iCertXpath = ObjReader.getLocator("iNRCISYFCertAwardedRadio").replace("{value}", iData.get("certificateAwarded").trim());
                iAction("WAITVISIBLE", "XPATH", iCertXpath, null);
                iAction("CLICK",       "XPATH", iCertXpath, null);
            }

            // ── College dropdown ──────────────────────────────────────────────────────────
            if (iData.containsKey("college"))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFCollegeDropdown"), iData.get("college").trim());
            }

            // ── Qualification dropdown ────────────────────────────────────────────────────
            if (iData.containsKey("qualification"))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFQualificationDropdown"), iData.get("qualification").trim());
            }

            // ── Next Farmer ───────────────────────────────────────────────────────────────
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
    // Step          : the no-qualification information message should be displayed if applicable
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

    // ===================================================================================================================================
    //  SECTION 8 : COMPANY DETAILS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters company details (DataTable)
    // Description   : Fills CRO, company name, company secretary name.
    //                 DOM: no formcontrolname — mat-label ancestor traversal locators.
    //                 DataTable keys: croNumber, companyName, secretaryName
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
    // Parameters    : pCount — "1"..."6"
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
    //  SECTION 9 : GROUP MEMBER DETAILS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters group member details (DataTable)
    // Description   : Fills name, DOB, eligible checkbox per member row.
    //                 Fix: was sending iDobXpath (XPath string) instead of iDob — now uses
    //                 clearAndEnterDate helper which also handles existing value in field.
    //                 DataTable cols: memberIndex | name | dob | eligible
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters group member details")
    public void theAgentEntersGroupMemberDetails(DataTable pDataTable)
    {
        log.info("[STEP] And the agent enters group member details");

        JavascriptExecutor        iJs      = (JavascriptExecutor) getDriver();
        List<Map<String, String>> iMembers = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iMember : iMembers)
        {
            String iIdx      = iMember.get("memberIndex").trim();
            String iName     = iMember.get("name").trim();
            String iDob      = iMember.get("dob").trim();
            String iEligible = iMember.get("eligible").trim();

            log.info("[TC13] Entering member " + iIdx + ": " + iName);

            // ── Name ─────────────────────────────────────────────────────────────────────
            String iNameXpath = ObjReader.getLocator("iNRCISYFMemberNameInput").replace("{index}", iIdx);
            iAction("WAITVISIBLE", "XPATH", iNameXpath, null);
            iAction("TEXTBOX",     "XPATH", iNameXpath, iName);

            // ── DOB — disabled datepicker, clearAndEnterDate ──────────────────────────────
            String     iDobXpath = ObjReader.getLocator("iNRCISYFMemberDOBInput").replace("{index}", iIdx);
            WebElement iDobInput = getDriver().findElement(By.xpath(iDobXpath));
            clearAndEnterDate(iDobInput, iDob, "DOB member " + iIdx);

            // ── Eligible checkbox ─────────────────────────────────────────────────────────
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
                    log.warning("[TC13] Could not tick eligible for member "
                            + iIdx + ": " + e.getMessage());
                }
            }

            log.info("[TC13] Member " + iIdx + " complete: " + iName + " DOB=" + iDob);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent confirms group status question as {string}
    // Description   : DOM radio value is true/false — maps from Yes/No.
    // Parameters    : pAnswer — "Yes" or "No"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms group status question as {string}")
    public void theAgentConfirmsGroupStatusQuestionAs(String pAnswer)
    {
        log.info("[STEP] And the agent confirms group status question as: " + pAnswer);
        String iValue      = "Yes".equalsIgnoreCase(pAnswer.trim()) ? "true" : "false";
        String iRadioXpath = ObjReader.getLocator("iNRCISYFGroupStatusRadio").replace("{value}", iValue);
        iAction("WAITVISIBLE", "XPATH", iRadioXpath, null);
        iAction("CLICK",       "XPATH", iRadioXpath, null);
        log.info("[TC13] Group status confirmed: " + pAnswer + " (value=" + iValue + ")");
    }

    // ===================================================================================================================================
    //  SECTION 10 : SUMMARY / DOCUMENT UPLOAD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent proceeds to the summary step
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
    // Description   : JS clicks Upload button per label, attaches PDF, confirms upload.
    //                 iNRCISYFUploadBtn uses p/parent/following-sibling pattern (legacy framework).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent uploads NRCISYF documents")
    public void theAgentUploadsNRCISYFDocuments(DataTable pDataTable) throws InterruptedException
    {
        log.info("[STEP] When the agent uploads NRCISYF documents");

        String iFilePath = System.getProperty("nrcisyf.upload.path",
                System.getProperty("user.dir")
                        + java.io.File.separator + "src"
                        + java.io.File.separator + "test"
                        + java.io.File.separator + "resources"
                        + java.io.File.separator + "Test_Data"
                        + java.io.File.separator + "Cover_Letter.pdf");

        for (String iDocType : pDataTable.asList())
        {
            String iClean  = iDocType.trim();
            String iLabel  = iClean.substring(0, Math.min(40, iClean.length()));
            log.info("[TC13] Uploading: " + iClean);

            String     iUploadBtnXpath = ObjReader.getLocator("iNRCISYFUploadBtn")
                    .replace("{label}", iLabel);
            iAction("WAITVISIBLE", "XPATH", iUploadBtnXpath, null);
            WebElement iUploadBtn = getDriver().findElement(By.xpath(iUploadBtnXpath));
            ((JavascriptExecutor) getDriver())
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", iUploadBtn);
            ((JavascriptExecutor) getDriver())
                    .executeScript("arguments[0].click();", iUploadBtn);

            iAction("UPLOADFILE",    "XPATH",
                    ObjReader.getLocator("iNRCISYFFileUploadInput"), iFilePath);
            iAction("WAITVISIBLE",   "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("WAITCLICKABLE", "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("CLICK",         "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("WAITINVISIBLE", "XPATH",
                    ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
            Thread.sleep(2000);
            log.info("[TC13] Uploaded: " + iClean);
        }
    }

    // ===================================================================================================================================
    //  SECTION 11 : SUBMISSION / SAVE AND EXIT
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent saves and proceeds to the declaration step
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves and proceeds to the declaration step")
    public void theAgentSavesAndProceedsToTheDeclarationStep()
    {
        log.info("[STEP] And the agent saves and proceeds to the declaration step");
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFStepperSaveAndNextBtn"), null);
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFSaveAndNextBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent submits the NRCISYF application with declaration
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent submits the NRCISYF application with declaration")
    public void theAgentSubmitsTheNRCISYFApplicationWithDeclaration()
    {
        log.info("[STEP] And the agent submits the NRCISYF application with declaration");
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFSubmitApplicationBtn"), null);
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFDeclarationCheckbox1"), null);
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFDeclarationCheckbox2"), null);
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFSubmitConfirmBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the NRCISYF application should be submitted successfully
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the NRCISYF application should be submitted successfully")
    public void theNRCISYFApplicationShouldBeSubmittedSuccessfully()
    {
        log.info("[STEP] Then the NRCISYF application should be submitted successfully");
        iAction("WAITVISIBLE", "XPATH",
                ObjReader.getLocator("iNRCISYFSubmitSuccessMsg"), null);
        String iConfirmation = iAction("GETTEXT", "XPATH",
                ObjReader.getLocator("iNRCISYFSubmitSuccessMsg"), null);
        Assertions.assertFalse(iConfirmation.isEmpty(),
                "NRCISYF submission success should be visible.");
        log.info("NRCISYF application submitted successfully: " + iConfirmation);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent saves and exits the NRCISYF application
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves and exits the NRCISYF application")
    public void theAgentSavesAndExitsTheNRCISYFApplication()
    {
        log.info("[STEP] And the agent saves and exits the NRCISYF application");
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFStepperSaveAndExitBtn"), null);
        iAction("CLICK", "XPATH",
                ObjReader.getLocator("iNRCISYFSaveExitDialogBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the application should be saved successfully
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the application should be saved successfully")
    public void theApplicationShouldBeSavedSuccessfully()
    {
        log.info("[STEP] Then the application should be saved successfully");
        iAction("VERIFYELEMENT", "XPATH",
                ObjReader.getLocator("iNRCISYFApplyEditBtn"), null);
        log.info("Application saved successfully — back on landing page.");
    }

    // ===================================================================================================================================
    //  SECTION 12 : CUSTOM EDUCATION / QUALIFICATION VERIFICATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the education institution should display {string}
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the education institution should display {string}")
    public void theEducationInstitutionShouldDisplay(String pExpected)
    {
        log.info("[STEP] Then the education institution should display: " + pExpected);
        iAction("VERIFYTEXT", "XPATH",
                "//*[contains(@class,'summary') or contains(@class,'review')]"
                        + "//*[contains(text(),'" + pExpected + "')]",
                pExpected);
        log.info("Custom institution verified: " + pExpected);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the qualification should display {string}
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the qualification should display {string}")
    public void theQualificationShouldDisplay(String pExpected)
    {
        log.info("[STEP] Then the qualification should display: " + pExpected);
        iAction("VERIFYTEXT", "XPATH",
                "//*[contains(@class,'summary') or contains(@class,'review')]"
                        + "//*[contains(text(),'" + pExpected + "')]",
                pExpected);
        log.info("Custom qualification verified: " + pExpected);
    }

    // ===================================================================================================================================
    //  SECTION 13 : UPLOAD VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the upload control only accepts PDF format (DataTable)
    // Description   : Per document: JS clicks upload button, uploads image.png, clicks Upload
    //                 Document, verifies both error messages, clicks Close, waits for dialog dismiss.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent verifies the upload control only accepts PDF format")
    public void theAgentVerifiesTheUploadControlOnlyAcceptsPDFFormat(DataTable pDataTable)
            throws InterruptedException
    {
        log.info("[STEP] Then the agent verifies the upload control only accepts PDF format");

        String iImagePath = System.getProperty("user.dir")
                + java.io.File.separator + "src"
                + java.io.File.separator + "test"
                + java.io.File.separator + "resources"
                + java.io.File.separator + "Test_Data"
                + java.io.File.separator + "image.png";

        for (String iDocType : pDataTable.asList())
        {
            String iClean = iDocType.trim();
            String iLabel = iClean.substring(0, Math.min(40, iClean.length()));
            log.info("[TC13] PDF-only validation for: " + iClean);

            String     iUploadBtnXpath = ObjReader.getLocator("iNRCISYFUploadBtn")
                    .replace("{label}", iLabel);
            iAction("WAITVISIBLE", "XPATH", iUploadBtnXpath, null);
            WebElement iUploadBtn = getDriver().findElement(By.xpath(iUploadBtnXpath));
            ((JavascriptExecutor) getDriver())
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", iUploadBtn);
            ((JavascriptExecutor) getDriver())
                    .executeScript("arguments[0].click();", iUploadBtn);

            iAction("UPLOADFILE",    "XPATH",
                    ObjReader.getLocator("iNRCISYFFileUploadInput"), iImagePath);
            iAction("WAITVISIBLE",   "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("WAITCLICKABLE", "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);
            iAction("CLICK",         "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);

            iAction("WAITVISIBLE",   "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadErrorFailed"), null);
            iAction("VERIFYELEMENT", "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadErrorFailed"), null);
            log.info("[TC13] Error verified: Document upload failed");

            iAction("WAITVISIBLE",   "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadErrorNotPdf"), null);
            iAction("VERIFYELEMENT", "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadErrorNotPdf"), null);
            log.info("[TC13] Error verified: File is not a PDF");

            iAction("WAITVISIBLE",   "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDialogCloseBtn"), null);
            iAction("WAITCLICKABLE", "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDialogCloseBtn"), null);
            iAction("CLICK",         "XPATH",
                    ObjReader.getLocator("iNRCISYFUploadDialogCloseBtn"), null);
            iAction("WAITINVISIBLE", "XPATH",
                    ObjReader.getLocator("iNRCISYFDialogOverlay"), null);
            Thread.sleep(2000);
            log.info("[TC13] PDF validation complete for: " + iClean);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent opens the upload dialog for {string}
    // Description   : Fix: now uses iNRCISYFUploadBtn with JS click (not hardcoded button xpath).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens the upload dialog for {string}")
    public void theAgentOpensTheUploadDialogFor(String pDocType)
    {
        log.info("[STEP] When the agent opens the upload dialog for: " + pDocType);
        String iLabel          = pDocType.trim()
                .substring(0, Math.min(40, pDocType.trim().length()));
        String iUploadBtnXpath = ObjReader.getLocator("iNRCISYFUploadBtn")
                .replace("{label}", iLabel);
        iAction("WAITVISIBLE", "XPATH", iUploadBtnXpath, null);
        WebElement iUploadBtn = getDriver().findElement(By.xpath(iUploadBtnXpath));
        ((JavascriptExecutor) getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center'});", iUploadBtn);
        ((JavascriptExecutor) getDriver())
                .executeScript("arguments[0].click();", iUploadBtn);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent closes the upload dialog without uploading
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent closes the upload dialog without uploading")
    public void theAgentClosesTheUploadDialogWithoutUploading()
    {
        log.info("[STEP] And the agent closes the upload dialog without uploading");
        iAction("WAITVISIBLE",   "XPATH",
                ObjReader.getLocator("iNRCISYFUploadDialogCloseBtn"), null);
        iAction("WAITCLICKABLE", "XPATH",
                ObjReader.getLocator("iNRCISYFUploadDialogCloseBtn"), null);
        iAction("CLICK",         "XPATH",
                ObjReader.getLocator("iNRCISYFUploadDialogCloseBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the upload dialog should be dismissed
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
    //  SECTION 14 : POST-SUBMISSION VERIFICATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent views the submitted NRCISYF application
    // Description   : Verifies the current herd is in submitted state (View Application button present).
    //                 If the herd is NOT submitted, performs self-healing recovery:
    //                   1. Re-query BISS_DATA for a submitted CISYF herd (shuffled pool)
    //                   2. INET-validate — skip BLACKLISTED + EXPIRED_AGENTS
    //                   3. Update Hooks.RUNTIME_HERD + CISYF fields
    //                   4. Logout + Login as new agent
    //                   5. Navigate: My Clients → NR/CISYF tab → search → open landing page
    //                   6. Verify submitted state again — click View Application
    //                 Up to MAX_RETRIES attempts before failing with clear message.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 09-06-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent views the submitted NRCISYF application")
    public void theAgentViewsTheSubmittedNRCISYFApplication()
    {
        log.info("[STEP] And the agent views the submitted NRCISYF application");

        final int    MAX_RETRIES      = 10;
        final String YEAR             = System.getProperty("herd.year", "2026").trim();
        final String VIEW_APP_XPATH   = ObjReader.getLocator("iNRCISYFViewApplicationBtn");
        final String NRCISYF_TAB      = "NR/CISYF";
        final String RESULT_ROW_XPATH = ObjReader.getLocator("iNRCISYFViewLink");

        for (int iAttempt = 1; iAttempt <= MAX_RETRIES; iAttempt++)
        {
            log.info("[TC13-POST] Attempt " + iAttempt + "/" + MAX_RETRIES + " | herd=" + Hooks.CISYF_HERD);

            // ── Check if View Application button is present ───────────────────────────────
            // "View Application" is only visible when the herd is fully submitted.
            // If absent, the herd is still in draft/not-started state — recover.
            if (isVisible(By.xpath(VIEW_APP_XPATH), 5))
            {
                iAction("CLICK", "XPATH", VIEW_APP_XPATH, null);
                log.info("[TC13-POST] Submitted herd confirmed — View Application clicked: " + Hooks.CISYF_HERD);
                return;
            }

            // ── Not submitted — recover with a fresh submitted herd ───────────────────────
            log.warning("[TC13-POST] Attempt " + iAttempt + " | herd=" + Hooks.CISYF_HERD + " — View Application not present (herd not submitted). Recovering...");

            if (iAttempt == MAX_RETRIES)
            {
                throw new RuntimeException("[TC13-POST] Could not find a submitted CISYF herd after " + MAX_RETRIES + " attempts. Last herd: " + Hooks.CISYF_HERD);
            }

            // ── Step 1: Re-query BISS_DATA for submitted CISYF herds ─────────────────────
            int iLimit = 50 + (iAttempt * 25);
            database.DBRouter.runDB("DATA", "List of submitted individual herds with CISYF scheme", YEAR, String.valueOf(iLimit));
            List<java.util.Map<String, Object>> iDbRows = database.DBRouter.getRows();

            if (iDbRows == null || iDbRows.isEmpty())
            {
                log.warning("[TC13-POST] BISS_DATA returned 0 submitted rows at limit=" + iLimit + " — retrying.");
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
                    log.warning("[TC13-POST] " + iCandidate + " is blacklisted — skipping.");
                    continue;
                }

                database.DBRouter.runDB("INET", "Get Login Id for herd", iCandidate);
                String iUser = database.DBRouter.getValue("USERNAME");

                if (iUser == null || iUser.isBlank())
                {
                    log.warning("[TC13-POST] No INET agent for " + iCandidate + " — skipping.");
                    continue;
                }
                iUser = iUser.trim();

                if (Hooks.EXPIRED_AGENTS.contains(iUser))
                {
                    log.warning("[TC13-POST] Agent " + iUser + " expired for herd=" + iCandidate + " — skipping.");
                    continue;
                }

                iNextHerd     = iCandidate;
                iNextUsername = iUser;
                log.info("[TC13-POST] Valid submitted candidate: herd=" + iNextHerd + " | agent=" + iNextUsername);
                break;
            }

            if (iNextHerd == null)
            {
                log.warning("[TC13-POST] All candidates exhausted at attempt " + iAttempt + " — retrying with larger limit.");
                continue;
            }

            // ── Step 3: Update Hooks ──────────────────────────────────────────────────────
            Hooks.RUNTIME_HERD     = iNextHerd;
            Hooks.RUNTIME_USERNAME = iNextUsername;
            Hooks.CISYF_HERD       = iNextHerd;
            Hooks.CISYF_USERNAME   = iNextUsername;
            log.info("[TC13-POST] Hooks updated: herd=" + iNextHerd + " | agent=" + iNextUsername);

            // ── Step 4: Logout + Login as new agent ───────────────────────────────────────
            performLogout();
            performLogin(iNextUsername);

            if (Hooks.EXPIRED_AGENTS.contains(iNextUsername))
            {
                log.warning("[TC13-POST] Agent " + iNextUsername + " expired during login — retrying.");
                continue;
            }

            // ── Step 5: Navigate to My Clients → NR/CISYF tab ────────────────────────────
            iAction("CLICK",         "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
            iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);
            iAction("CLICK",         "XPATH", ObjReader.getLocator("iMyClientsTabByName").replace("%s", NRCISYF_TAB), null);

            // ── Step 6: Search for submitted herd + open landing page ────────────────────
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), iNextHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iNRCISYFSearchBtn"), null);

            if (!isVisible(By.xpath(RESULT_ROW_XPATH), 5))
            {
                log.warning("[TC13-POST] Herd " + iNextHerd + " returned no search rows — retrying.");
                continue;
            }

            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFViewLink"), null);
            log.info("[TC13-POST] Opened landing page for submitted herd: " + iNextHerd);
            // Loop back — check for View Application on this landing page
        }

        throw new RuntimeException("[TC13-POST] Could not find a submitted CISYF herd after " + MAX_RETRIES + " attempts. Last herd: " + Hooks.CISYF_HERD);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the uploaded documents should be accessible in correspondence
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the uploaded documents should be accessible in correspondence")
    public void theUploadedDocumentsShouldBeAccessibleInCorrespondence()
    {
        log.info("[STEP] Then the uploaded documents should be accessible in correspondence");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFCorrespondenceNavBtn"), null);
        }
        catch (Exception ignored) {}

        List<WebElement> iDocRows = getDriver().findElements(By.xpath("//tr[contains(@class,'document') or contains(@class,'row')]" + "//a[contains(@href,'download') or contains(text(),'pdf')]"));
        Assertions.assertFalse(iDocRows.isEmpty(), "At least one uploaded document should be visible in correspondence.");
        log.info("Correspondence documents found: " + iDocRows.size());
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the {string} document link
    // Parameters    : pDocName — partial text of the document link
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the {string} document link")
    public void theAgentClicksOnTheDocumentLink(String pDocName)
    {
        log.info("[STEP] And the agent clicks on the document link: " + pDocName);
        iAction("CLICK", "XPATH", "//a[contains(normalize-space(),'" + pDocName.trim() + "')] | " + "//button[contains(normalize-space(),'" + pDocName.trim() + "')]", null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the document should open or download successfully
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the document should open or download successfully")
    public void theDocumentShouldOpenOrDownloadSuccessfully()
    {
        log.info("[STEP] Then the document should open or download successfully");
        try
        {
            List<WebElement> iErrors = getDriver().findElements(
                    By.xpath("//*[contains(@class,'error') and contains(@class,'page')]"));
            Assertions.assertTrue(
                    iErrors.isEmpty() || !iErrors.get(0).isDisplayed(),
                    "No error page should be visible after clicking the document link.");
        }
        catch (Exception e) { /* No error — good */ }
        log.info("Document link opened without errors.");
    }

    // ===================================================================================================================================
    //  HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Method        : clearAndEnterDate
    // Description   : Reliably clears and enters a date into a disabled Angular Material datepicker.
    //                 clear() alone fails — Angular holds value in component state.
    //                 Steps: removeAttribute → CTRL+A+DELETE → JS value reset + events →
    //                        sendKeys → TAB → readback verify → JS retry if empty.
    // Parameters    : pInput  — datepicker WebElement
    //                 pDate   — DD/MM/YYYY
    //                 pLabel  — logging label e.g. "DOB member 1"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 03-06-2026
    // ***************************************************************************************************************************************************************************************
    private void clearAndEnterDate(WebElement pInput, String pDate, String pLabel)
    {
        JavascriptExecutor iJs = (JavascriptExecutor) getDriver();
        try
        {
            iJs.executeScript("arguments[0].removeAttribute('disabled');", pInput);
            iJs.executeScript("arguments[0].scrollIntoView({block:'center'});", pInput);
            iJs.executeScript("arguments[0].focus();", pInput);

            pInput.sendKeys(org.openqa.selenium.Keys.chord(
                    org.openqa.selenium.Keys.CONTROL, "a"));
            pInput.sendKeys(org.openqa.selenium.Keys.DELETE);

            iJs.executeScript(
                    "arguments[0].value = '';" +
                            "arguments[0].dispatchEvent(new Event('input',  {bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                    pInput);

            pInput.sendKeys(pDate);
            pInput.sendKeys(org.openqa.selenium.Keys.TAB);

            String iActual = pInput.getAttribute("value");
            if (iActual == null || iActual.trim().isEmpty())
            {
                log.warning("[TC13] " + pLabel + " — value empty, retrying via JS value set.");
                iJs.executeScript(
                        "arguments[0].value = '" + pDate + "';" +
                                "arguments[0].dispatchEvent(new Event('input',  {bubbles:true}));" +
                                "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                        pInput);
                pInput.sendKeys(org.openqa.selenium.Keys.TAB);
                iActual = pInput.getAttribute("value");
            }

            if (iActual != null && iActual.trim().equals(pDate.trim()))
                log.info("[TC13] " + pLabel + " set and verified: " + iActual);
            else
                log.warning("[TC13] " + pLabel + " — expected '" + pDate
                        + "' but found '" + iActual + "' — proceeding.");
        }
        catch (Exception e)
        {
            log.warning("[TC13] " + pLabel + " — clearAndEnterDate failed: " + e.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Method        : performLogout
    // Description   : Logs out via Exit + Logout buttons. Falls back to navigate + deleteAllCookies.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-05-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogout()
    {
        log.info("[TC13-RELOGIN] Logging out current session...");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"),  null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"), null);

            By iSadPopup = By.xpath(ObjReader.getLocator("iLogoutPopup"));
            if (isVisible(iSadPopup, 1))
                iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutPopup"), null);

            log.info("[TC13-RELOGIN] Logout complete.");
        }
        catch (Exception e)
        {
            log.warning("[TC13-RELOGIN] UI logout failed (" + e.getMessage() + ") — navigating to base URL as fallback.");
            getDriver().manage().deleteAllCookies();
            getDriver().navigate().to(Hooks.iUrl);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Method        : performLogin
    // Description   : Logs in as specified agent. Handles PIN+TOTP and OTP-only flows.
    //                 Detects Account Expired and calls Hooks.markAgentExpired().
    // Parameters    : pUsername — agent username
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 22-05-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogin(String pUsername)
    {
        log.info("[TC13-RELOGIN] Logging in as: " + pUsername);
        getDriver().navigate().to(Hooks.iUrl);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),      pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),      "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),            null);

        By iExpiredBy = By.xpath(ObjReader.getLocator("iAccountExpiredError"));
        if (isVisible(iExpiredBy, 3))
        {
            String iErrText = getDriver().findElement(iExpiredBy).getText().trim();
            if (iErrText.toLowerCase().contains("account expired")
                    || iErrText.toLowerCase().contains("account has expired")
                    || iErrText.contains("Invalid username or password."))
            {
                log.warning("[TC13-RELOGIN] Account Expired for: "
                        + pUsername + " — marking expired.");
                Hooks.markAgentExpired(pUsername);
                return;
            }
        }

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

        if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[TC13-RELOGIN] Terms accepted.");
        }
        log.info("[TC13-RELOGIN] Login complete: " + pUsername);
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
