// ===================================================================================================================================
// File          : TC_02_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_02_ENTS - Transfer Application negative case (herd without entitlements).
//
//                 Steps in this file (2):
//                   1. the agent initiates a transfer search for herd without entitlements (DataTable)
//                   2. the Add Entitlement button should not be present
//
//                 Every other step of TC_02_ENTS.feature is already defined:
//                   login / portal navigation                       -> stepdefinitions.TC_03
//                   tab switching                                   -> stepdefinitions.TC_06
//                   create / upload / send / key / submit / back    -> stepdefinitions.ENTS.TC_01_ENTS
//
//                 Playwright edition (22-09-2026):
//                   - Same step texts, same ObjectRepository keys, same flow.
//                   - The transferor herd is searched with EntsSession.openTransferOutHerd, which waits for the row of
//                     THAT herd and skips rows marked expired. Selenium clicked the first View link on the page, which
//                     could open an unrelated row before the search result arrived.
//                   - DataTable values may be runtime tokens such as {transferee.herd} (utilities.EntsTestData).
//
//                 IMPORTANT - the transferor herd of this test case:
//                   The ENTS Agent Login query only returns herds holding MORE THAN 10 entitlements, so it can never
//                   supply the herd this test case needs. Keep transferorHerd hard-coded in the feature file (the
//                   Selenium version used V1861254), or ask for a "herds with no entitlements" query and a
//                   {transferor.herd.noents} token. The transferee cells can still be tokens.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
// ===================================================================================================================================

package stepdefinitions.ENTS;

import com.microsoft.playwright.Locator;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import utilities.EntsTestData;
import utilities.ObjReader;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static stepdefinitions.ENTS.EntsSession.pause;
import static stepdefinitions.ENTS.EntsSession.xp;

public class TC_02_ENTS
{
    private static final Logger log = Logger.getLogger(TC_02_ENTS.class.getName());


    // ===================================================================================================================================
    //  NEGATIVE CASE - HERD WITHOUT ENTITLEMENTS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent initiates a transfer search for herd without entitlements (DataTable)
    // Description   : Runs the transferor flow up to the entitlement page and stops there. The next step asserts that
    //                 the Add button is absent.
    //                 This is TC_01_ENTS.theAgentCreatesATransferApplicationWithTheFollowingDetails() without step 5
    //                 (add entitlement), because the herd has none.
    //
    //                 DataTable keys:
    //                   transferorHerd (String) - herd with no entitlements, e.g. "V1861254" (hard-coded, see header)
    //                   transfereeHerd (String) - herd number, or {transferee.herd}
    //                   transfereeName (String) - full name,   or {transferee.name}
    //                   transferType   (String) - transfer type code, e.g. "203"
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright)
    // ***************************************************************************************************************************************************************************************
    @When("the agent initiates a transfer search for herd without entitlements")
    public void theAgentInitiatesATransferSearchForHerdWithoutEntitlements(DataTable pDataTable)
    {
        log.info("[STEP] When the agent initiates a transfer search for herd without entitlements");

        Map<String, String> iData = EntsTestData.resolveTable(pDataTable.asMap(String.class, String.class));

        String iTransferorHerd = iData.get("transferorHerd").trim();
        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iTransfereeName = iData.get("transfereeName").trim();
        String iTransferType   = iData.get("transferType").trim();

        // Search the transferor herd and open its row (waits for that row, skips expired rows)
        EntsSession.openTransferOutHerd(iTransferorHerd);
        log.info("Transferor herd opened (no entitlements expected): " + iTransferorHerd);

        // Create Transfer Application
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCreateTransferBtn"), null);

        // Search for the transferee
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iTransferTypeSearchBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeHerdField"), iTransfereeHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfereeNameField"), iTransfereeName);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iTransferDialogSearchBtn"), null);
        log.info("Transferee searched: " + iTransfereeName + " (" + iTransfereeHerd + ")");

        // Select the transfer type (same union XPath as TC_01_ENTS)
        pause(1000);
        iAction("CLICK", "XPATH",
                "//mat-radio-button[contains(.,'" + iTransferType + "')] | "
                        + "//tr[contains(.,'" + iTransferType + "')]//input | "
                        + "//*[@value='" + iTransferType + "']",
                null);
        pause(1000);

        // Next - lands on the entitlement page
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferNextBtn"), null);

        log.info("Transfer setup complete - on the entitlement page. Expecting NO Add button for herd: " + iTransferorHerd);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the Add Entitlement button should not be present
    // Description   : Asserts that no visible Add button is on the entitlement page and that the
    //                 "You currently have no entitlements to select" label is shown.
    //                 Change from Selenium: counts the VISIBLE Add buttons with Playwright locators instead of
    //                 findElements + isDisplayed, and gives the page a moment to render the empty state first.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright)
    // ***************************************************************************************************************************************************************************************
    @Then("the Add Entitlement button should not be present")
    public void theAddEntitlementButtonShouldNotBePresent()
    {
        log.info("[STEP] Then the Add Entitlement button should not be present");

        // Wait for the empty-state label rather than asserting on a page that is still rendering
        boolean iNoEntitlementLabel = EntsSession.isVisible(ObjReader.getLocator("iTransferNoEntitlementLabel"), 10);

        List<Locator> iAddButtons   = xp(ObjReader.getLocator("iTransferAddEntitlementBtns")).all();
        int           iVisibleCount = 0;
        for (Locator iBtn : iAddButtons)
        {
            if (iBtn.isVisible()) iVisibleCount++;
        }

        log.info("[TRANSFER] Visible Add button(s): " + iVisibleCount
                + " | 'no entitlements' label shown: " + iNoEntitlementLabel);

        Assertions.assertEquals(0, iVisibleCount,
                "Add Entitlement button should NOT be present for a herd without entitlements.");
        Assertions.assertTrue(iNoEntitlementLabel,
                "The 'You currently have no entitlements to select' label should be shown for a herd without entitlements.");

        log.info("Negative validation passed - Add Entitlement button is absent as expected.");
    }
}
