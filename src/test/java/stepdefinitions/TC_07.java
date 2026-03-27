// ===================================================================================================================================
// File          : TC_07.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_07 — Regression Suite for Bugs and Features in BISS - 2.
//
//                 Covers two active scenarios:
//
//                   AT-TC-07 : Active Farmer Screen Error (BISSAGL-119)
//                                - Apply Not Started quick filter
//                                - Pick herd dynamically from row 10
//                                - Start application → verify Active Status checkbox
//                                - Delete the created draft
//
//                   AT-TC-13 : Active Farmer Approved — missing admin check message (BISSAGL-121)
//                                - Apply Not Started quick filter
//                                - Pick herd dynamically from row 10
//                                - Start application (admin check text validation commented pending fix)
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  — all UI interactions
//                   ObjReader.getLocator("keyName")                   — all locator lookups
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                          → TC_01_Login.java
//                   "the agent logs into the application..."                       → TC_01_Login.java
//                   "the agent opens the BISS application"                         → TC_01_Login.java
//                   "the agent should land on the BISS Home page"                  → TC_01_Login.java
//                   "the agent navigates to the Home and My Clients..."            → TC_04.java
//                   "the agent is on the BISS Agent Home Screen"                   → TC_03.java
//                   "the agent clicks on the View Dashboard button"                → TC_05.java
//                   "the agent clicks on the {string} farmer dashboard button"     → TC_03.java
//                   "the agent clicks on the {string} dialog button"               → TC_03.java
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjReader;

import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_07
{
    private static final Logger log = Logger.getLogger(TC_07.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent applies the {string} quick filter
    // Description   : Clicks the named quick filter button on the My Clients page.
    //                 e.g. "Not Started", "Draft", "Submitted", "View all", "Herd Expired"
    // Parameters    : pFilter (String) - filter label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent applies the {string} quick filter")
    public void theAgentAppliesthequickFilter(String pFilter)
    {
        iAction("CLICK", "XPATH",
                "//button[normalize-space()='" + pFilter.trim() + "']"
                        + " | //a[normalize-space()='" + pFilter.trim() + "']"
                        + " | //*[contains(@class,'filter') and normalize-space()='" + pFilter.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent picks a herd from the list
    // Description   : Clicks the first available herd row in the My Clients results list.
    //                 Used to select any herd without specifying a hardcoded herd number.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent picks a herd from the list")
    public void theAgentPicksAHerdFromTheList()
    {
        iAction("CLICK", "XPATH",
                "(//mat-row | //tbody/tr)[1]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent selects the herd from row {int}
    // Description   : Reads the herd number from the nth row in the My Clients list and
    //                 searches for it, then clicks the matching row.
    //                 Row index is 1-based.
    // Parameters    : pRow (int) - row number e.g. 10
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent selects the herd from row {int}")
    public void theAgentSelectsTheHerdFromRow(int pRow)
    {
        String iHerdXpath = "(//mat-row | //tbody/tr)[" + pRow + "]"
                + "//*[contains(@class,'herd') or @data-column='herd']"
                + " | (//mat-cell | //td)[" + pRow + "]";
        String iHerd = iAction("GETTEXT", "XPATH", iHerdXpath, null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iHerdSearchField"), iHerd.trim());
        iAction("CLICK", "XPATH",
                "//mat-row[.//*[normalize-space()='" + iHerd.trim() + "']]"
                        + " | //tr[.//*[normalize-space()='" + iHerd.trim() + "']]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the active status checkbox is present on the Active Farmer screen
    // Description   : Verifies the Active Status checkbox is visible on the Active Farmer step
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the active status checkbox is present on the Active Farmer screen")
    public void theActiveStatusCheckboxIsPresentOnTheActiveFarmerScreen()
    {
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iActiveStatusCheckbox"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the admin check message is visible on the Active Farmer screen
    // Description   : Verifies the "May be subject to administrative checks" message is visible.
    //                 Currently commented in AT-TC-13 — kept here for when the scenario is re-enabled.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the admin check message is visible on the Active Farmer screen")
    public void theAdminCheckMessageIsVisibleOnTheActiveFarmerScreen()
    {
        iAction("VERIFYTEXT", "XPATH",
                ObjReader.getLocator("iAdminCheckText"),
                "CONTAINS:May be subject to administrative checks");
    }
}