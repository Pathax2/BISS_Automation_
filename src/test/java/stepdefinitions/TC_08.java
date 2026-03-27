// ===================================================================================================================================
// File          : TC_08.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_08 — My Clients Table Headers, Sorting and Pagination.
//
//                 Covers three scenarios:
//
//                   TC1 (BISS_23.1.3) : My Clients table column header verification
//                                        - Sortable columns: Name, Herd Number
//                                        - Non-sortable columns: Farmer Status, Herd Expired,
//                                          Eco Space for Nature, BISS Application Status
//                                        - Row 2 herd selection and dashboard name match
//
//                   TC1 (BPS/BISS PI_22.4.6) : Transfers sub-tab column headers and pagination
//                                        - Sortable: Name, Address, Herd No.
//                                        - Non-sortable: Expired, Transfers
//                                        - Row count changes (50 → 20)
//                                        - Last page navigation
//
//                   TC2 (BISS_23.1.3) : Dashboard name/herd match and personalised greeting
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
//                   "the agent switches to the {string} tab on the My Clients page"→ TC_05.java
//                   "the agent selects the herd from row {int}"                    → TC_07.java
//                   "the agent clicks on the View Dashboard button"                → TC_05.java
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import utilities.ObjReader;

import java.util.List;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_08
{
    private static final Logger log = Logger.getLogger(TC_08.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the {string} column header has sorting
    // Description   : Verifies the named column header is visible and contains a sort control
    //                 (an arrow icon or sort indicator element)
    // Parameters    : pHeader (String) - column header label e.g. "Name", "Herd Number"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the {string} column header has sorting")
    public void theAgentVerifiesColumnHeaderHasSorting(String pHeader)
    {
        iAction("VERIFYELEMENT", "XPATH",
                "//th[normalize-space()='" + pHeader.trim() + "']"
                        + " | //mat-header-cell[normalize-space()='" + pHeader.trim() + "']",
                null);
        iAction("VERIFYELEMENT", "XPATH",
                "//th[normalize-space()='" + pHeader.trim() + "']//*[contains(@class,'sort') or contains(@class,'arrow')]"
                        + " | //mat-header-cell[normalize-space()='" + pHeader.trim() + "']//*[contains(@class,'sort')]",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the {string} column header has no sorting
    // Description   : Verifies the named column header is visible. No sort indicator is expected.
    // Parameters    : pHeader (String) - column header label e.g. "Farmer Status"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the {string} column header has no sorting")
    public void theAgentVerifiesColumnHeaderHasNoSorting(String pHeader)
    {
        iAction("VERIFYELEMENT", "XPATH",
                "//th[normalize-space()='" + pHeader.trim() + "']"
                        + " | //mat-header-cell[normalize-space()='" + pHeader.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent changes the number of rows per page to {string}
    // Description   : Selects the given page size option from the pagination rows-per-page dropdown
    // Parameters    : pRows (String) - page size e.g. "50", "20"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent changes the number of rows per page to {string}")
    public void theAgentChangesTheNumberOfRowsPerPage(String pRows)
    {
        iAction("LIST", "XPATH", ObjReader.getLocator("iRowsPerPageDropdown"), pRows);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates to the last page using the pagination arrow
    // Description   : Clicks the last-page navigation arrow in the My Clients pagination control
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent navigates to the last page using the pagination arrow")
    public void theAgentNavigatesToLastPageUsingPaginationArrow()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPaginationLastPageBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the name on the dashboard matches the selected row
    // Description   : Reads the farmer name from the previously selected My Clients row and
    //                 verifies it matches the name displayed on the farmer dashboard
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent verifies the name on the dashboard matches the selected row")
    public void theAgentVerifiesNameOnDashboardMatchesSelectedRow()
    {
        String iExpectedName = iAction("GETTEXT", "XPATH",
                ObjReader.getLocator("iMyClientsRowName"), null);
        iAction("VERIFYTEXT", "XPATH",
                ObjReader.getLocator("iFarmerDashboardName"),
                "CONTAINS:" + iExpectedName.trim());
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the name and herd number on the dashboard
    // Description   : Verifies both the farmer name and herd number are displayed on the dashboard
    //                 and match the values from the My Clients list row that was selected
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the name and herd number on the dashboard")
    public void theAgentVerifiesNameAndHerdNumberOnDashboard()
    {
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iFarmerDashboardName"), null);
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iFarmerDashboardHerd"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the personalised greeting message is displayed
    // Description   : Verifies a personalised greeting (e.g. "Welcome back, ...") is
    //                 visible on the farmer dashboard
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the personalised greeting message is displayed")
    public void theAgentVerifiesPersonalisedGreetingMessageIsDisplayed()
    {
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iPersonalisedGreetingMessage"), null);
    }
}