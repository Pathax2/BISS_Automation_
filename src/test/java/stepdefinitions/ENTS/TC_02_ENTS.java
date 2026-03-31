// ===================================================================================================================================
// File          : TC_02_ENTS.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_02_ENTS — Transfer Application E2E (Different Agent).
//
//                 This file contains ONLY the steps unique to TC_02_ENTS. All other steps
//                 are reused from existing step definition classes:
//
//                 ┌──────────────────────────────────────────────────────────┬────────────────────┐
//                 │ Step                                                     │ Defined In         │
//                 ├──────────────────────────────────────────────────────────┼────────────────────┤
//                 │ the agent user is on the login page                      │ TC_03.java         │
//                 │ the agent logs into the application...                   │ TC_03.java         │
//                 │ the agent opens the {string} application                 │ TC_03.java         │
//                 │ the agent should land on the BISS Home page              │ TC_03.java         │
//                 │ the agent navigates to ... Left Menu Link                │ TC_03.java         │
//                 │ the agent switches to the {string} tab...                │ TC_06.java         │
//                 │ the agent creates a transfer application...              │ TC_01_ENTS.java    │
//                 │ the agent uploads the transferor signature document      │ TC_01_ENTS.java    │
//                 │ the agent sends the transfer for acceptance              │ TC_01_ENTS.java    │
//                 │ the transfer key should be captured                      │ TC_01_ENTS.java    │
//                 │ the agent navigates to the transferee acceptance flow    │ TC_01_ENTS.java    │
//                 │ the agent enters the transfer key and views...           │ TC_01_ENTS.java    │
//                 │ the agent submits the transfer to DAFM with notes...     │ TC_01_ENTS.java    │
//                 │ the transfer should be submitted successfully            │ TC_01_ENTS.java    │
//                 │ the agent navigates back to the Transfers client list    │ TC_01_ENTS.java    │
//                 └──────────────────────────────────────────────────────────┴────────────────────┘
//
//                 New steps in this file (2 total):
//                   1. the agent initiates a transfer search for herd without entitlements (DataTable)
//                   2. the Add Entitlement button should not be present
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.ObjReader;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_02_ENTS
{
    private static final Logger log = Logger.getLogger(TC_02_ENTS.class.getName());


    // ===================================================================================================================================
    //  NEGATIVE CASE — HERD WITHOUT ENTITLEMENTS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent initiates a transfer search for herd without entitlements (DataTable)
    // Description   : Performs the transferor flow up to the point where the Add Entitlement
    //                 button would normally appear. Stops BEFORE clicking Add — the next step
    //                 asserts the button is absent.
    //
    //                 This is a partial version of TC_01_ENTS.theAgentCreatesATransferApplicationWithTheFollowingDetails()
    //                 — it does steps 1–4 (search, create, search transferee, select type, click Next)
    //                 but intentionally omits step 5 (add entitlement) because the herd has none.
    //
    //                 DataTable keys:
    //                   transferorHerd (String) - herd with no entitlements e.g. "V1861254"
    //                   transfereeHerd (String) - transferee herd number
    //                   transfereeName (String) - transferee full name
    //                   transferType   (String) - transfer type code e.g. "203"
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent initiates a transfer search for herd without entitlements")
    public void theAgentInitiatesATransferSearchForHerdWithoutEntitlements(DataTable pDataTable)
    {
        log.info("[STEP] When the agent initiates a transfer search for herd without entitlements");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransferorHerd = iData.get("transferorHerd").trim();
        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iTransfereeName = iData.get("transfereeName").trim();
        String iTransferType   = iData.get("transferType").trim();

        // ── Search for the transferor herd ───────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iTransferorHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
        log.info("Transferor herd opened (no entitlements expected): " + iTransferorHerd);

        // ── Create Transfer Application ──────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCreateTransferBtn"), null);

        // ── Search for the transferee ────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTypeSearchBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeHerdField"), iTransfereeHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeNameField"), iTransfereeName);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSearchBtn"), null);
        log.info("Transferee searched: " + iTransfereeName + " (" + iTransfereeHerd + ")");

        // ── Select the transfer type ─────────────────────────────────────────────────────
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + iTransferType + "')] | "
                        + "//tr[contains(.,'" + iTransferType + "')]//input | "
                        + "//*[@value='" + iTransferType + "']",
                null);

        // ── Click Next — should land on the entitlement page ─────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        log.info("Transfer setup complete — now on entitlement page. Expecting NO Add button for herd: " + iTransferorHerd);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the Add Entitlement button should not be present
    // Description   : Verifies the "Add" entitlement button is NOT visible on the entitlement page.
    //                 This confirms the transferor herd has no entitlements available for transfer.
    //                 Uses findElements() (returns empty list if not found) rather than findElement()
    //                 (throws exception) to perform a clean negative assertion.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the Add Entitlement button should not be present")
    public void theAddEntitlementButtonShouldNotBePresent()
    {
        log.info("[STEP] Then the Add Entitlement button should not be present");

        // Use findElements to avoid NoSuchElementException — returns an empty list if absent
        List<WebElement> iAddButtons = getDriver().findElements(
                By.xpath(ObjReader.getLocator("iTransferAddEntitlementBtn")));

        // Filter to only visible ones — the button might exist in DOM but be hidden
        long iVisibleCount = iAddButtons.stream().filter(WebElement::isDisplayed).count();

        Assertions.assertEquals(0, iVisibleCount,
                "Add Entitlement button should NOT be present for a herd without entitlements.");

        log.info("Negative validation passed — Add Entitlement button is absent as expected.");
    }
}