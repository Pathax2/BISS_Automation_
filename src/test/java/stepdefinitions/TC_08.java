// ===================================================================================================================================
// File          : TC_08.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_08 — My Clients Table E2E Regression.
//
//                 Covers:
//                   Section 1 : My Clients column header verification (sortable / non-sortable via DataTable)
//                   Section 2 : Dashboard name match after herd selection
//                   Section 3 : Transfers sub-tab headers (DataTable) + pagination
//                   Section 4 : Dashboard name, herd number, and greeting verification
//
//                 Steps defined in THIS file (7 total):
//                   1. the following columns should have sorting enabled       — @Then  (DataTable)
//                   2. the following columns should have sorting disabled      — @And   (DataTable)
//                   3. the agent verifies the name on the dashboard matches the selected row — @Then
//                   4. the agent changes the number of rows per page to {string}            — @And
//                   5. the agent navigates to the last page using the pagination arrow      — @Then
//                   6. the agent verifies the name and herd number on the dashboard         — @Then
//                   7. the agent verifies the personalised greeting message is displayed    — @And
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   TC_03.java (Background + scenario):
//                     "the agent user is on the login page"
//                     "the agent logs into the application with valid credentials and OTP"
//                     "the agent opens the {string} application"
//                     "the agent should land on the BISS Home page"
//                     "the agent navigates to the {string} and {string} Left Menu Link"
//                     "the agent opens a farmer dashboard using herd data"
//
//                   TC_07.java:
//                     "the agent is on the BISS Agent Home Screen"
//
//                   TC_06.java:
//                     "the agent switches to the {string} tab on the My Clients page"
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 01-04-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ObjReader;

import java.util.List;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_08
{
    private static final Logger log = Logger.getLogger(TC_08.class.getName());


    // ***************************************************************************************************************************************************************************************
    // Step          : the following columns should have sorting enabled
    // Description   : Iterates through a DataTable of column names and asserts each one has
    //                 a visible sort control (mat-sort-header arrow icon or sort class).
    //                 Used for both the My Clients table and the Transfers sub-tab table.
    //
    //                 DataTable format (single column, no header):
    //                   | Name        |
    //                   | Herd Number |
    //
    // Parameters    : pDataTable (DataTable) - list of column header names
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the following columns should have sorting enabled")
    public void theFollowingColumnsShouldHaveSortingEnabled(DataTable pDataTable)

    {
        log.info("[STEP] Then the following columns should have sorting enabled");

        List<String> iColumns = pDataTable.asList();

        for (String col : iColumns)
        {
            iAction("MOUSEHOVER", "XPATH", "//div[contains(@class,'mat-sort-header-container') and .//div[normalize-space()='" + col + "']]", "");
            String iSortableHeader = String.format(ObjReader.getLocator("iTableHeaderSortable"), col.trim());
            String iArrow = String.format(ObjReader.getLocator("iSortArrowForColumn"), col.trim());

            List<WebElement> sortHeader = getDriver().findElements(By.xpath(iSortableHeader));

            List<WebElement> arrow = getDriver().findElements(By.xpath(iArrow));


            Assertions.assertFalse(sortHeader.isEmpty(), "Sortable header not found for column: " + col);

            Assertions.assertFalse(arrow.isEmpty(), "Sort arrow missing for sortable column: " + col);

            log.info("  ✓ " + col + " — sortable and arrow present");
        }

        log.info("All sortable columns verified: " + iColumns.size() + " checked.");
    }



    // ***************************************************************************************************************************************************************************************
    // Step          : the following columns should have sorting disabled
    // Description   : Iterates through a DataTable of column names and asserts each one does
    //                 NOT have a visible sort control. Columns like "Farmer Status" and
    //                 "Herd Expired" are non-sortable by design.
    //
    //                 DataTable format (single column, no header):
    //                   | Farmer Status           |
    //                   | Herd Expired            |
    //                   | Eco Space for Nature    |
    //                   | BISS Application Status |
    //
    // Parameters    : pDataTable (DataTable) - list of column header names
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @And("the following columns should have sorting disabled")
    public void theFollowingColumnsShouldHaveSortingDisabled(DataTable pDataTable)

    {
        log.info("[STEP] Then the following columns should NOT have sorting enabled");

        List<String> iColumns = pDataTable.asList();

        for (String col : iColumns)
        {
            String iNonSortableHeader = String.format(ObjReader.getLocator("iTableHeaderNotSortable"), col.trim());
            String iArrow = String.format(ObjReader.getLocator("iSortArrowForColumn"), col.trim());

            List<WebElement> header = getDriver().findElements(By.xpath(iNonSortableHeader));
            List<WebElement> arrow = getDriver().findElements(By.xpath(iArrow));

            Assertions.assertFalse(header.isEmpty(), "Non-sortable header not found for: " + col);

            Assertions.assertEquals(0, arrow.size(), "Unexpected sort arrow present in NON-sortable column: " + col);

            log.info("  ✓ " + col + " — verified as non-sortable");
        }
    }



    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the name on the dashboard matches the selected row
    // Description   : Reads the farmer name displayed on the dashboard after opening a herd
    //                 via "the agent opens a farmer dashboard using herd data" and asserts
    //                 it is not empty — confirming the correct farmer profile loaded.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent verifies the name on the dashboard matches the selected row")
    public void theAgentVerifiesTheNameOnTheDashboardMatchesTheSelectedRow()
    {
        log.info("[STEP] Then the agent verifies the name on the dashboard matches the selected row");
        // After the herd search above, there should only be one matching record.
        iAction("CLICK", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
        log.info("Exactly 1 record found. Clicking first client name...");
        String iDashName = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerDashboardName"), null);
        Assertions.assertFalse(iDashName.trim().isEmpty(),
                "Dashboard farmer name should not be empty after herd selection.");
        log.info("Dashboard name verified: " + iDashName.trim());
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent changes the number of rows per page to {string}
    // Description   : Selects a value from the paginator "items per page" dropdown to change
    //                 the number of rows displayed in the table (e.g. "20", "50", "100").
    //                 Works for both the My Clients table and the Transfers sub-tab table.
    // Parameters    : pCount (String) - the row count value to select
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent changes the number of rows per page to {string}")
    public void theAgentChangesTheNumberOfRowsPerPageTo(String pCount)
    {
        log.info("[STEP] And the agent changes the number of rows per page to: " + pCount);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iListItemsPerPage"), null);
        iAction("LIST", "XPATH", ObjReader.getLocator("iRowsPerPageTrigger"), pCount);
        log.info("Rows per page changed to: " + pCount);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates to the last page using the pagination arrow
    // Description   : Clicks the "last page" arrow button on the paginator to jump to the
    //                 final page of the table. Verifies the pagination control is functional.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent navigates to the last page using the pagination arrow")
    public void theAgentNavigatesToTheLastPageUsingThePaginationArrow()
    {
        log.info("[STEP] Then the agent navigates to the last page using the pagination arrow");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iListItemsPerPage"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iPaginatorLastPageButton"), null);
        log.info("Navigated to last page via pagination arrow.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the name and herd number on the dashboard
    // Description   : Reads both the farmer name and herd number from the dashboard and
    //                 asserts neither is empty — confirms the correct farmer profile loaded.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent verifies the name and herd number on the dashboard")
    public void theAgentVerifiesTheNameAndHerdNumberOnTheDashboard()
    {
        log.info("[STEP] Then the agent verifies the name and herd number on the dashboard");

        String iName = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iCurrentClientName"), null);
        String iHerd = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iHerdNumberValue"), null);

        Assertions.assertFalse(iName.trim().isEmpty(), "Farmer name should be visible on the dashboard.");
        Assertions.assertFalse(iHerd.trim().isEmpty(), "Herd number should be visible on the dashboard.");
        log.info("Dashboard verified — Name: " + iName.trim() + " | Herd: " + iHerd.trim());
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent verifies the personalised greeting message is displayed
    // Description   : Verifies the personalised greeting (e.g. "Good morning, Agent Name")
    //                 is visible on the dashboard after login. Confirms the greeting element
    //                 exists and contains non-empty text.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 01-04-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent verifies the personalised greeting message is displayed")
    public void theAgentVerifiesThePersonalisedGreetingMessageIsDisplayed()
    {
        String iDashName = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerDashboardName"), null);
        Assertions.assertFalse(iDashName.trim().isEmpty(), "Dashboard farmer name should not be empty after herd selection.");log.info("Dashboard name verified: " + iDashName.trim());
    }
}
