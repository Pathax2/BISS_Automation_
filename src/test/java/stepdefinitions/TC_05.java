// ===================================================================================================================================
// File          : TC_05.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_05 — Verify the My Clients Page Functionalities.
//
//                 Covers two scenarios:
//
//                   1. Tab switching — verifies all sub-tabs on the My Clients page are
//                      clickable and navigable:
//                        - Transfers
//                        - NR/CISYF
//                        - BISS Applications
//
//                   2. Search and navigate — verifies the agent can:
//                        - Search for a herd number
//                        - Click the matching row
//                        - Navigate to the farmer dashboard via View Dashboard button
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  — all UI interactions
//                   ObjReader.getLocator("keyName")                   — all locator lookups
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                   → TC_01_Login.java
//                   "the agent logs into the application..."                → TC_01_Login.java
//                   "the agent opens the BISS application"                  → TC_01_Login.java
//                   "the agent should land on the BISS Home page"           → TC_01_Login.java
//                   "the agent is on the BISS Agent Home Screen"            → TC_03.java
//                   "the agent navigates to the Home and My Clients..."     → TC_04.java
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

public class TC_05
{
    private static final Logger log = Logger.getLogger(TC_05.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent switches to the {string} tab on the My Clients page
    // Description   : Clicks the named sub-tab inside the My Clients page tab bar.
    //                 Handles: "Transfers", "NR/CISYF", "BISS Applications", "No Herd Number"
    // Parameters    : pTab (String) - sub-tab label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent switches to the {string} tab on the My Clients page")
    public void theAgentSwitchesToTabOnMyClientsPage(String pTab)
    {
        iAction("CLICK", "XPATH",
                "//mat-tab-header//div[normalize-space()='" + pTab.trim() + "']"
                        + " | //button[normalize-space()='" + pTab.trim() + "']"
                        + " | //a[normalize-space()='" + pTab.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent searches for herd number {string}
    // Description   : Types the given herd number into the My Clients herd search field
    //                 and waits for the results table to update
    // Parameters    : pHerd (String) - herd number to search e.g. "N7010276"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent searches for herd number {string}")
    public void theAgentSearchesForHerdNumber(String pHerd)
    {
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iHerdSearchField"), pHerd);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the row for client {string}
    // Description   : Clicks the table row in the My Clients list that matches the given herd number
    // Parameters    : pHerd (String) - herd number identifying the row e.g. "N7010276"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the row for client {string}")
    public void theAgentClicksOnTheRowForClient(String pHerd)
    {
        iAction("CLICK", "XPATH",
                "//mat-row[.//*[normalize-space()='" + pHerd.trim() + "']]"
                        + " | //tr[.//*[normalize-space()='" + pHerd.trim() + "']]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the View Dashboard button
    // Description   : Clicks the View Dashboard button to navigate to the selected farmer's dashboard.
    //                 Also used in TC_03.java — defined there and reused here automatically by Cucumber.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clicks on the View Dashboard button")
    public void theAgentClicksOnViewDashboardButton()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewDashboardBtn"), null);
    }
}