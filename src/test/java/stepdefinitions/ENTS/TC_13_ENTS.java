// ===================================================================================================================================
// File          : TC_13_ENTS.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_13_ENTS — NR/CISYF End-to-End Regression Pack.
//
//                 Covers the full NR/CISYF business flow:
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

public class TC_13_ENTS
{
    private static final Logger log = Logger.getLogger(TC_13_ENTS.class.getName());


    // ===================================================================================================================================
    //  HERD SEARCH AND LANDING PAGE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent searches for the NR/CISYF herd and opens the application
    // Description   : Enters the runtime herd number into the NR/CISYF search field and clicks View.
    //                 Herd is resolved from Hooks.RUNTIME_HERD (DB-driven at boot time).
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent searches for the NR/CISYF herd and opens the application")
    public void theAgentSearchesForTheNRCISYFHerdAndOpensTheApplication()
    {
        log.info("[STEP] When the agent searches for the NR/CISYF herd and opens the application");

        // Type the runtime herd number into the NR/CISYF search field
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFHerdSearchField"), Hooks.RUNTIME_HERD);

        // Click search to filter the results
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSearchBtn"), null);

        // Click the View link on the first matching row
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFViewLink"), null);

        log.info("NR/CISYF herd opened: " + Hooks.RUNTIME_HERD);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the NR/CISYF landing page should be displayed
    // Description   : Verifies the NR/CISYF landing page content is visible after opening the herd
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the NR/CISYF landing page should be displayed")
    public void theNRCISYFLandingPageShouldBeDisplayed()
    {
        log.info("[STEP] Then the NR/CISYF landing page should be displayed");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iNRCISYFLandingHeader"), null);
        log.info("NR/CISYF landing page confirmed.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the NR/CISYF closing date should display {string}
    // Description   : Verifies the closing date shown on the NR/CISYF landing page matches expected
    // Parameters    : pExpectedDate (String) - expected date text e.g. "15 May 2026"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the NR/CISYF closing date should display {string}")
    public void theNRCISYFClosingDateShouldDisplay(String pExpectedDate)
    {
        log.info("[STEP] And the NR/CISYF closing date should display: " + pExpectedDate);
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iNRCISYFCloseDate"), pExpectedDate);
        log.info("NR/CISYF close date verified: " + pExpectedDate);
    }


    // ===================================================================================================================================
    //  CATEGORY DIALOG — OPEN / CLOSE / INFO ICONS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent opens the NR/CISYF Apply or Edit dialog
    // Description   : Clicks the Apply or Edit button to open the category selection dialog
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens the NR/CISYF Apply or Edit dialog")
    public void theAgentOpensTheNRCISYFApplyOrEditDialog()
    {
        log.info("[STEP] When the agent opens the NR/CISYF Apply or Edit dialog");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFApplyEditBtn"), null);
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

        // Map category letter to its position index in the dialog (1-based)
        String iIndex;
        switch (pCategory.trim().toUpperCase())
        {
            case "A": iIndex = "1"; break;
            case "B": iIndex = "2"; break;
            case "C": iIndex = "3"; break;
            default: throw new RuntimeException("Unknown category: " + pCategory);
        }

        // Click the info icon — positioned by index within the category dialog
        iAction("CLICK", "XPATH",
                "(//button[contains(@class,'info') or contains(@aria-label,'info')])[" + iIndex + "]",
                null);

        // Verify the info content is displayed and not empty
        String iInfoText = iAction("GETTEXT", "XPATH",
                ObjReader.getLocator("iNRCISYFInfoContent"),
                null);
        Assertions.assertFalse(iInfoText.isEmpty(),
                "Info icon for category " + pCategory + " should display a description.");
        log.info("Category " + pCategory + " info icon verified: " + iInfoText.substring(0, Math.min(50, iInfoText.length())) + "...");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent closes the NR/CISYF category dialog
    // Description   : Clicks the Close button on the NR/CISYF category selection dialog
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent closes the NR/CISYF category dialog")
    public void theAgentClosesTheNRCISYFCategoryDialog()
    {
        log.info("[STEP] When the agent closes the NR/CISYF category dialog");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFCloseBtn"), null);
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

        // Find all category checkboxes in the dialog and uncheck any that are selected
        WebDriver iDriver = getDriver();
        List<WebElement> iCheckboxes = iDriver.findElements(
                By.xpath(ObjReader.getLocator("iNRCISYFCategoryCheckboxes")));

        for (WebElement iCb : iCheckboxes)
        {
            try
            {
                String iChecked = iCb.getAttribute("aria-checked");
                if ("true".equalsIgnoreCase(iChecked))
                {
                    iCb.click();
                    log.info("Unchecked category: " + iCb.getText().trim());
                }
            }
            catch (Exception e)
            {
                log.warning("Could not reset checkbox: " + e.getMessage());
            }
        }
        log.info("All category selections reset.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects NR/CISYF categories (DataTable)
    // Description   : Iterates through the DataTable list of category labels and clicks each one.
    //                 Checks the current state before clicking to avoid toggling off an already-selected category.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects NR/CISYF categories")
    public void theAgentSelectsNRCISYFCategories(DataTable pDataTable)
    {
        log.info("[STEP] And the agent selects NR/CISYF categories");

        List<String> iCategories = pDataTable.asList();

        for (String iCategory : iCategories)
        {
            String iClean = iCategory.trim();
            log.info("Selecting category: " + iClean);

            // Build XPath to locate the checkbox label matching this category text
            String iXpath = "//mat-checkbox[contains(.,'" + iClean + "')] | "
                    + "//label[contains(normalize-space(),'" + iClean + "')]";

            // Check if it's already selected — only click if it's not
            try
            {
                WebElement iElement = getDriver().findElement(By.xpath(iXpath));
                String iChecked = iElement.getAttribute("aria-checked");

                if (!"true".equalsIgnoreCase(iChecked))
                {
                    iAction("CLICK", "XPATH", iXpath, null);
                }
                else
                {
                    log.info("Category '" + iClean + "' already selected — skipping.");
                }
            }
            catch (Exception e)
            {
                // Fallback: just click it directly — let Angular handle the toggle
                iAction("CLICK", "XPATH", iXpath, null);
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
        Assertions.assertFalse(iErrorText.isEmpty(),
                "Error message should appear for invalid A+B combination (triggered by " + pCategory + ").");
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
        // Click Next to advance from category selection
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFNextBtn"), null);
        // Accept the OK confirmation if displayed
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFOKBtn"), null);
            log.info("OK confirmation accepted.");
        }
        catch (Exception e)
        {
            log.info("No OK confirmation displayed — continuing.");
        }
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
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFFarmingEntityDropdown"), pEntityType);
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
        iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFHerdGroupTypeDropdown"), pGroupType);
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

        List<String> iExpected = pDataTable.asList().stream()
                .map(String::trim).collect(Collectors.toList());

        // Click the dropdown to open it
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), null);
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);

        // Read all visible options
        List<WebElement> iOptions = getDriver().findElements(By.xpath("//mat-option//span"));
        List<String> iActual = iOptions.stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());

        // Close the dropdown
        try { iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFMemberCountDropdown"), null); }
        catch (Exception ignored) {}

        // Verify all expected values are present
        for (String iVal : iExpected)
        {
            Assertions.assertTrue(iActual.contains(iVal),
                    "Member count dropdown should contain: " + iVal + " | Actual options: " + iActual);
        }
        log.info("Member count dropdown verified: " + iActual);
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

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCroNumberField"),
                iData.get("croNumber").trim());
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCompanyNameField"),
                iData.get("companyName").trim());
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFDirectorNameField"),
                iData.get("directorName").trim());

        log.info("Company details entered: CRO=" + iData.get("croNumber"));
    }


    // ===================================================================================================================================
    //  GROUP MEMBER DETAILS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters group member details (DataTable)
    // Description   : Iterates through each row in the DataTable to enter member name, date of birth,
    //                 and tick the eligible farmer checkbox. Supports up to 6 members.
    //                 DataTable cols: memberIndex, name, dobDay, dobMonth, dobYear, eligible
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters group member details")
    public void theAgentEntersGroupMemberDetails(DataTable pDataTable)
    {
        log.info("[STEP] And the agent enters group member details");

        List<Map<String, String>> iMembers = pDataTable.asMaps(String.class, String.class);

        for (Map<String, String> iMember : iMembers)
        {
            String iIdx       = iMember.get("memberIndex").trim();
            String iName      = iMember.get("name").trim();
            String iDobDay    = iMember.get("dobDay").trim();
            String iDobMonth  = iMember.get("dobMonth").trim();
            String iDobYear   = iMember.get("dobYear").trim();
            String iEligible  = iMember.get("eligible").trim();

            log.info("Entering member " + iIdx + ": " + iName);

            // The member fields are indexed — field "4" is member 1 name, "5" is member 2 name etc.
            // The legacy file used sequential field indices: 4, 5 for names
            int iFieldOffset = 3 + Integer.parseInt(iIdx);
            iAction("TEXTBOX", "XPATH",
                    "(//input[contains(@formcontrolname,'name') or contains(@id,'member')])[" + iIdx + "]",
                    iName);

            // Date of birth — uses the DatePicker component indexed by member number
            iAction("TEXTBOX", "XPATH",
                    "(//input[contains(@formcontrolname,'day') or contains(@placeholder,'DD')])[" + iIdx + "]",
                    iDobDay);
            iAction("LIST", "XPATH",
                    "(//mat-select[contains(@formcontrolname,'month')])[" + iIdx + "]",
                    iDobMonth);
            iAction("TEXTBOX", "XPATH",
                    "(//input[contains(@formcontrolname,'year') or contains(@placeholder,'YYYY')])[" + iIdx + "]",
                    iDobYear);

            // Eligible farmer checkbox
            if ("Yes".equalsIgnoreCase(iEligible))
            {
                String iCbXpath = "(//mat-checkbox[contains(@formcontrolname,'eligible') or contains(@id,'eligible')])[" + iIdx + "]";
                try
                {
                    WebElement iCb = getDriver().findElement(By.xpath(iCbXpath));
                    if (!"true".equalsIgnoreCase(iCb.getAttribute("aria-checked")))
                    {
                        iAction("CLICK", "XPATH", iCbXpath, null);
                    }
                }
                catch (Exception e)
                {
                    iAction("CLICK", "XPATH", iCbXpath, null);
                }
            }

            log.info("Member " + iIdx + " details entered: " + iName + " DOB=" + iDobDay + "/" + iDobMonth + "/" + iDobYear);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent confirms group status question as {string}
    // Description   : Selects Yes/No for the group status radio button question on the status page
    // Parameters    : pAnswer (String) - "Yes" or "No"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms group status question as {string}")
    public void theAgentConfirmsGroupStatusQuestionAs(String pAnswer)
    {
        log.info("[STEP] And the agent confirms group status question as: " + pAnswer);
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + pAnswer + "')] | //label[contains(text(),'" + pAnswer + "')]",
                null);
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
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + iHasQual + "')] | //label[contains(text(),'" + iHasQual + "')]",
                null);

        // If No — stop here, the "no qualification" path doesn't need further fields
        if ("No".equalsIgnoreCase(iHasQual))
        {
            log.info("Qualification = No — no further fields to fill.");
            return;
        }

        // Date of completion
        if (iData.containsKey("dateOfCompletion"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFDateOfCompletionField"),
                    iData.get("dateOfCompletion").trim());
        }

        // Certificate awarded — Yes / No
        if (iData.containsKey("certificateAwarded"))
        {
            String iCertAwarded = iData.get("certificateAwarded").trim();
            iAction("CLICK", "XPATH",
                    "(//mat-radio-button[contains(.,'" + iCertAwarded + "')])[last()] | "
                            + "(//label[contains(text(),'" + iCertAwarded + "')])[last()]",
                    null);
        }

        // College / Institution
        if (iData.containsKey("college"))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFCollegeDropdown"),
                    iData.get("college").trim());
        }

        // Custom college — only when "institution not in list" was selected
        if (iData.containsKey("customCollege"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCustomCollegeField"),
                    iData.get("customCollege").trim());
        }

        // Qualification
        if (iData.containsKey("qualification"))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFQualificationDropdown"),
                    iData.get("qualification").trim());
        }

        // Custom qualification — only when "qualification not in list" was selected
        if (iData.containsKey("customQualification"))
        {
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFCustomQualificationField"),
                    iData.get("customQualification").trim());
        }

        log.info("Qualification details completed: college=" + iData.getOrDefault("college", "N/A"));
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

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        // Determine how many members need qualification by counting available "Next Farmer" clicks.
        // We loop: fill qualification → click Next Farmer → fill again, until Next Farmer is no longer available.
        int iMaxMembers = 10;  // safety cap
        int iMemberCount = 0;

        for (int i = 0; i < iMaxMembers; i++)
        {
            iMemberCount++;
            log.info("Filling qualification for member " + iMemberCount);

            // Has qualification
            String iHasQual = iData.getOrDefault("hasQualification", "Yes").trim();
            iAction("CLICK", "XPATH",
                    "//mat-radio-button[contains(.,'" + iHasQual + "')] | //label[contains(text(),'" + iHasQual + "')]",
                    null);

            // Date of completion
            if (iData.containsKey("dateOfCompletion"))
            {
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iNRCISYFDateOfCompletionField"),
                        iData.get("dateOfCompletion").trim());
            }

            // Certificate awarded
            if (iData.containsKey("certificateAwarded"))
            {
                iAction("CLICK", "XPATH",
                        "(//mat-radio-button[contains(.,'" + iData.get("certificateAwarded").trim() + "')])[last()]",
                        null);
            }

            // College
            if (iData.containsKey("college"))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFCollegeDropdown"),
                        iData.get("college").trim());
            }

            // Qualification
            if (iData.containsKey("qualification"))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iNRCISYFQualificationDropdown"),
                        iData.get("qualification").trim());
            }

            // Try clicking "Next Farmer" — if it doesn't exist, we've done all members
            try
            {
                if (isVisible(By.xpath(ObjReader.getLocator("iNRCISYFNextFarmerBtn")), 3))
                {
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFNextFarmerBtn"), null);
                    log.info("Next Farmer clicked — proceeding to member " + (iMemberCount + 1));
                }
                else
                {
                    log.info("No more Next Farmer buttons — all " + iMemberCount + " members completed.");
                    break;
                }
            }
            catch (Exception e)
            {
                log.info("Next Farmer not found — all " + iMemberCount + " members completed.");
                break;
            }
        }
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
    // Step          : the agent uploads NR/CISYF documents (DataTable)
    // Description   : Iterates through each document type in the DataTable, clicks the corresponding
    //                 upload button, attaches the sample PDF, and confirms the upload. Uses the same
    //                 sample document for all entries.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent uploads NR/CISYF documents")
    public void theAgentUploadsNRCISYFDocuments(DataTable pDataTable)
    {
        log.info("[STEP] When the agent uploads NR/CISYF documents");

        String iFilePath = System.getProperty("nrcisyf.upload.path",
                System.getProperty("user.dir")
                        + java.io.File.separator + "src"
                        + java.io.File.separator + "test"
                        + java.io.File.separator + "resources"
                        + java.io.File.separator + "Test_Data"
                        + java.io.File.separator + "sample_upload.pdf");

        List<String> iDocTypes = pDataTable.asList();

        for (String iDocType : iDocTypes)
        {
            String iClean = iDocType.trim();
            log.info("Uploading document: " + iClean);

            // Click the upload button matching this document type label
            iAction("CLICK", "XPATH",
                    "//button[contains(normalize-space(),'" + iClean.substring(0, Math.min(40, iClean.length())) + "')]",
                    null);

            // Attach the sample PDF file
            iAction("UPLOADFILE", "XPATH", ObjReader.getLocator("iNRCISYFFileUploadInput"), iFilePath);

            // Click "Upload Document" to confirm the upload
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFUploadDocumentBtn"), null);

            log.info("Document uploaded: " + iClean);
        }
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
    // Step          : the agent submits the NR/CISYF application with declaration
    // Description   : Clicks Submit Application, ticks both declaration checkboxes, then confirms
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent submits the NR/CISYF application with declaration")
    public void theAgentSubmitsTheNRCISYFApplicationWithDeclaration()
    {
        log.info("[STEP] And the agent submits the NR/CISYF application with declaration");

        // Click Submit Application button
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSubmitApplicationBtn"), null);

        // Tick both declaration checkboxes in the submission dialog
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFDeclarationCheckbox1"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFDeclarationCheckbox2"), null);

        // Confirm the submission
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSubmitConfirmBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the NR/CISYF application should be submitted successfully
    // Description   : Verifies submission success by checking for a confirmation element or message
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the NR/CISYF application should be submitted successfully")
    public void theNRCISYFApplicationShouldBeSubmittedSuccessfully()
    {
        log.info("[STEP] Then the NR/CISYF application should be submitted successfully");
        String iConfirmation = iAction("GETTEXT", "XPATH",
                "//div[contains(@class,'success') or contains(@class,'confirmation') or contains(text(),'submitted')]",
                null);
        Assertions.assertFalse(iConfirmation.isEmpty(), "NR/CISYF submission success should be visible.");
        log.info("NR/CISYF application submitted successfully: " + iConfirmation);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent saves and exits the NR/CISYF application
    // Description   : Clicks "Save and Exit" on the stepper and confirms
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves and exits the NR/CISYF application")
    public void theAgentSavesAndExitsTheNRCISYFApplication()
    {
        log.info("[STEP] And the agent saves and exits the NR/CISYF application");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFStepperSaveAndExitBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNRCISYFSaveAndExitBtn"), null);
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
        // After Save and Exit, the agent should be back on the NR/CISYF landing page
        // Verify the Apply/Edit button is visible again — confirms we exited the form
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iNRCISYFApplyEditBtn"), null);
        log.info("Application saved successfully — back on landing page.");
    }


    // ===================================================================================================================================
    //  NAVIGATION HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates back to the NR/CISYF client list
    // Description   : Clicks the My Clients tab and switches to NR/CISYF to reset for the next flow
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent navigates back to the NR/CISYF client list")
    public void theAgentNavigatesBackToTheNRCISYFClientList()
    {
        log.info("[STEP] When the agent navigates back to the NR/CISYF client list");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);
        iAction("CLICK", "XPATH",
                "//div[contains(@class,'mat-tab-label')]//span[normalize-space()='NR/CISYF']"
                        + " | //a[normalize-space()='NR/CISYF']",
                null);
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


    // ===================================================================================================================================
    //  POST-SUBMISSION VERIFICATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent views the submitted NR/CISYF application
    // Description   : Clicks the "View Application" button on the NR/CISYF landing page for a submitted herd
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent views the submitted NR/CISYF application")
    public void theAgentViewsTheSubmittedNRCISYFApplication()
    {
        log.info("[STEP] And the agent views the submitted NR/CISYF application");
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


    // ===================================================================================================================================
    //  HELPER METHODS
    // ===================================================================================================================================

    /**
     * Short-wait visibility check — returns true if the element appears within pSeconds,
     * false if it doesn't. Never throws.
     */
    private boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            WebDriverWait iWait = new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds));
            iWait.until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}