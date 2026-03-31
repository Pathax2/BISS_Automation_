// ===================================================================================================================================
// TC_15_ENTS.java | package stepdefinitions.ENTS
// Staff ENTSCore login + verification steps (shared by TC_15 and TC_17)
//
// New steps (5):
//   1. the staff user logs into ENTSCore as {string}
//   2. the staff searches for the herd by OwnerID
//   3. the staff navigates to year {string} and {string} tab
//   4. the NRCISYF submission should be visible in ENTSCore
//   5. the transfer submission should be visible in ENTSCore
//
// Also includes shared navigation steps:
//   6. the agent navigates to the {string} side nav tab
//   7. the agent captures the OwnerID of the herd
//   8. the agent searches for herd {string} and opens it
//   9. the agent verifies the entitlement position is displayed
//
// Author: Aniket Pathare | Created: 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.ObjReader;
import java.util.logging.Logger;
import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_15_ENTS
{
    private static final Logger log = Logger.getLogger(TC_15_ENTS.class.getName());
    private static String iCapturedOwnerID = "";

    // ── Side nav tab navigation ──────────────────────────────────────────────────
    @And("the agent navigates to the {string} side nav tab")
    public void theAgentNavigatesToTheSideNavTab(String pTabName)
    {
        log.info("[STEP] Navigate to side nav tab: " + pTabName);
        iAction("CLICK", "XPATH",
                "//a[contains(normalize-space(),'" + pTabName + "')] | "
                        + "//mat-list-item[contains(.,'" + pTabName + "')]", null);
    }

    // ── Capture OwnerID ──────────────────────────────────────────────────────────
    @And("the agent captures the OwnerID of the herd")
    public void theAgentCapturesTheOwnerID()
    {
        log.info("[STEP] Capturing OwnerID");
        iCapturedOwnerID = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iOwnerIDField"), null);
        System.setProperty("lastCapturedOwnerID", iCapturedOwnerID);
        log.info("OwnerID captured: " + iCapturedOwnerID);
    }

    // ── Search herd and open ─────────────────────────────────────────────────────
    @When("the agent searches for herd {string} and opens it")
    public void theAgentSearchesForHerdAndOpensIt(String pHerd)
    {
        log.info("[STEP] Search and open herd: " + pHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), pHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
    }

    // ── Verify entitlement position ──────────────────────────────────────────────
    @Then("the agent verifies the entitlement position is displayed")
    public void theAgentVerifiesEntitlementPosition()
    {
        log.info("[STEP] Verifying entitlement position");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iEntitlementPositionTable"), null);
    }

    // ── Staff login to ENTSCore ──────────────────────────────────────────────────
    @When("the staff user logs into ENTSCore as {string}")
    public void theStaffUserLogsIntoENTSCore(String pStaffUsername)
    {
        log.info("[STEP] Staff logs into ENTSCore: " + pStaffUsername);
        // Navigate to Staff login page
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStaffLoginLink"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iStaffUsernameField"), pStaffUsername);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        // Data protection checkbox
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStaffDataProtectionCheckbox"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);
        // Navigate to ENTS
        iAction("CLICK", "XPATH", ObjReader.getLocator("iENTSLink"), null);
        log.info("Staff logged into ENTSCore: " + pStaffUsername);
    }

    @And("the staff searches for the herd by OwnerID")
    public void theStaffSearchesForHerdByOwnerID()
    {
        log.info("[STEP] Staff searching by OwnerID: " + iCapturedOwnerID);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iStaffHerdSearchField"), iCapturedOwnerID);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStaffSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStaffOwnerIDLink"), null);
    }

    @And("the staff navigates to year {string} and {string} tab")
    public void theStaffNavigatesToYearAndTab(String pYear, String pTab)
    {
        log.info("[STEP] Staff navigates to year " + pYear + " → " + pTab);
        iAction("CLICK", "XPATH", "//a[contains(text(),'" + pYear + "')]", null);
        iAction("CLICK", "XPATH",
                "//a[contains(normalize-space(),'" + pTab + "')] | //span[contains(text(),'" + pTab + "')]", null);
    }

    @Then("the NRCISYF submission should be visible in ENTSCore")
    public void theNRCISYFSubmissionShouldBeVisibleInENTSCore()
    {
        log.info("[STEP] Verifying NRCISYF submission in ENTSCore");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iStaffNRCISYFSection"), null);
    }

    @Then("the transfer submission should be visible in ENTSCore")
    public void theTransferSubmissionShouldBeVisibleInENTSCore()
    {
        log.info("[STEP] Verifying transfer submission in ENTSCore");
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iStaffTransfersSection"), null);
    }
}