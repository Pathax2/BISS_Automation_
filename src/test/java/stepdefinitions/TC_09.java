// ===================================================================================================================================
// File          : TC_09.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_09 - BISSAGL-20849
//                 Verifies an agent can view payments and land details for a client
//                 without a technical error appearing on screen.
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  - all UI interactions
//
//                 Playwright migration notes:
//                   - The local isVisible(By, seconds) helper used WebDriverWait; it now uses
//                     UiHelpers.isVisible(Locator, seconds), which waits the same number of seconds and
//                     returns false instead of throwing.
//                   - .first() is added so a locator matching several elements behaves like Selenium findElement
//                     (Playwright would otherwise fail with a strict mode violation).
//                   - Selenium imports (By, WebDriverWait, ExpectedConditions) and the unused ObjReader import removed.
//                   - Every other step goes through iAction, which is already Playwright-based - logic unchanged.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// Updated       : 18-09-2026 - Migrated to Playwright (UiHelpers.isVisible, Selenium imports removed)
// ===================================================================================================================================

package stepdefinitions;

import com.microsoft.playwright.Locator;
import commonFunctions.UiHelpers;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;

public class TC_09
{
    private static final Logger log = Logger.getLogger(TC_09.class.getName());


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects "2023" from the "schemeYear" dropdown
    // Description   : Selects the specified scheme year from the named mat-select dropdown.
    //                 The dropdown is identified by its id attribute - pDropdownName maps directly to it.
    //                 iAction LIST opens the mat-select and clicks the matching option in the overlay panel.
    // Parameters    : pYear         - the scheme year to select e.g. "2023"
    //                 pDropdownName - the id of the mat-select e.g. "schemeYear"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} from the {string} dropdown")
    public void theAgentSelectsFromTheDropdown(String pYear, String pDropdownName)
    {
        log.info("[STEP] And the agent selects '" + pYear + "' from the '" + pDropdownName + "' dropdown");

        // Scheme year mat-select is identified by id attribute, not formcontrolname
        // Wrapped inside biss-scheme-year custom component - id is the stable anchor
        String iDropdownXpath = "//mat-select[@id='" + pDropdownName + "']";

        iAction("LIST", "XPATH", iDropdownXpath, pYear);
        log.info("Selected '" + pYear + "' from dropdown with id='" + pDropdownName + "'.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent clicks on the "Payments" tab
    // Description   : Clicks the named tab on the Applications / Payments screen.
    //                 Tabs are plain span elements inside div.tab-style, NOT Angular Material mat-tab components.
    // Parameters    : pTabName - the visible tab label text e.g. "Payments"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - no change in logic)
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the {string} tab")
    public void theAgentClicksOnTheTab(String pTabName)
    {
        log.info("[STEP] And the agent clicks on the '" + pTabName + "' tab");

        // Target by text content - covers both tab-border-style and no-tab-border-style states
        String iTabXpath = "//div[contains(@class,'tab-style')]//span[normalize-space()='" + pTabName + "']";

        iAction("WAITVISIBLE",   "XPATH", iTabXpath, null);
        iAction("WAITCLICKABLE", "XPATH", iTabXpath, null);
        iAction("CLICK",         "XPATH", iTabXpath, null);
        log.info("Clicked on tab: " + pTabName);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then Agent Verifies No "Technical error..." Error shown on page
    // Description   : Asserts that the specified error message text is NOT present anywhere on the current page.
    //                 Used to verify defect BISSAGL-20849 is resolved - the technical error message should not
    //                 appear after navigating to Payments.
    //                 isVisible returns false instead of throwing, so the happy path passes cleanly.
    // Parameters    : pErrorMessage - the exact error text to verify is absent
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // Date Updated  : 18-09-2026 (Playwright - UiHelpers.isVisible)
    // ***************************************************************************************************************************************************************************************
    @Then("Agent Verifes No {string} Error shown on page.")
    public void agentVerifiesNoErrorShownOnPage(String pErrorMessage)
    {
        log.info("[STEP] Then Agent Verifies No '" + pErrorMessage + "' Error shown on page.");

        // Search for any element on the page that contains the error message text -
        // uses contains() to handle partial text matches and surrounding whitespace
        String iErrorXpath = "//*[contains(normalize-space(),'" + pErrorMessage + "')]";

        boolean iErrorPresent = isVisible(iErrorXpath, 3);

        Assertions.assertFalse(iErrorPresent,
                "Technical error message was found on screen - defect BISSAGL-20849 may have regressed. "
                        + "Error text: '" + pErrorMessage + "'");

        log.info("No technical error message found on page. Defect BISSAGL-20849 is not regressed.");
    }


    // ===================================================================================================================================
    //  PRIVATE HELPERS (Playwright replacements for the Selenium helpers in this class)
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : xp
    // Description   : Builds a Playwright Locator from an XPath string (the framework adds the "xpath=" prefix).
    // Parameters    : pXpath (String) - XPath expression
    // Returns       : Locator
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 18-09-2026
    // ***************************************************************************************************************************************************************************************
    private static Locator xp(String pXpath)
    {
        return UiHelpers.byXpath(pXpath);
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
        return UiHelpers.isVisible(xp(pXpath).first(), pSeconds);
    }
}
