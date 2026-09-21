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

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefinitions.Hooks;
import utilities.ObjReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Duration;
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
        iAction("CLICK", "XPATH", "//a[contains(normalize-space(),'" + pTabName + "')] | " + "//mat-list-item[contains(.,'" + pTabName + "')]", null);
    }

    // ── Capture OwnerID ──────────────────────────────────────────────────────────
    @And("the agent captures the OwnerID of the herd")
    public void theAgentCapturesTheOwnerID()
    {

        log.info("[STEP] Capturing OwnerID");

        String ownerIdText = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iOwnerIDField"), null);

        log.info("Raw OwnerID text returned: " + ownerIdText);

        Pattern pattern = Pattern.compile("Owner\\s*Id:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(ownerIdText);

        if (matcher.find())
        {
            iCapturedOwnerID = matcher.group(1);
        }
        else
        {
            throw new RuntimeException("Unable to extract Owner ID from text: " + ownerIdText);
        }

        System.setProperty("lastCapturedOwnerID", iCapturedOwnerID);
        log.info("OwnerID captured: " + iCapturedOwnerID);

    }

    // ── Search herd and open ─────────────────────────────────────────────────────
    @When("the agent searches for herd {string} and opens it")
    public void theAgentSearchesForHerdAndOpensIt(String pHerd) throws InterruptedException {
        log.info("[STEP] Search and open herd: " + pHerd);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), pHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        Thread.sleep(1500);
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
        performLogout();
        getDriver().manage().deleteAllCookies();
        getDriver().navigate().to(Hooks.istaffUrl);
        log.info("[STEP] Staff logs into ENTSCore: " + pStaffUsername);

        // Navigate to Staff login page

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pStaffUsername);
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
        //iAction("CLICK", "XPATH", ObjReader.getLocator("iStaffOwnerIDLink"), null);
    }

    @And("the staff navigates to year {string} and {string} tab")
    public void theStaffNavigatesToYearAndTab(String pYear, String pTab)
    {
        log.info("[STEP] Staff navigates to year " + pYear + " → " + pTab);
        iAction("CLICK", "XPATH", "//a[contains(text(),'" + pYear + "')]", null);
        iAction("CLICK", "XPATH", "//a[contains(normalize-space(),'" + pTab + "')] | //span[contains(text(),'" + pTab + "')]", null);
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
    // ===================================================================================================================================
    //  PRIVATE HELPERS — Login / Logout / Navigation (reused by herd retry loop)
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : performLogout
    // Description   : Clicks the logout icon and waits for the landing page to confirm
    //                 the session has been terminated. Safe to call at any point when
    //                 the agent is inside the BISS portal.
    // Parameters    : none
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogout()
    {
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"),   null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"),  null);
            By iSadPOPup = By.xpath(ObjReader.getLocator("iLogoutPopup"));
            if (isVisible(iSadPOPup, 1)) {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutPopup"),  null);
            }


            // iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
            log.info("[TC13-RELOGIN] Logout complete.");
        }
        catch (Exception e)
        {
            log.warning("[TC13-RELOGIN] UI logout failed (" + e.getMessage() + ") — navigating to base URL as fallback.");
            getDriver().navigate().to(Hooks.iUrl);
            //iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : performLogin
    // Description   : Executes the full Keycloak login sequence for the given username.
    //                 Handles PIN screen (slots 1–7) and TOTP, same as the Background
    //                 login step — extracted here so the herd retry loop can call it
    //                 without duplicating logic.
    //                 Password is always read from TD:Password (same for all agents
    //                 in the test environment).
    //                 Terms & Conditions acceptance is handled if the screen appears.
    // Parameters    : pUsername (String) — Keycloak username of the agent to log in as
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogin(String pUsername) throws InterruptedException {
        log.info("[RELOGIN] Logging in as: " + pUsername);

        // Keycloak entry point
        getDriver().navigate().to(Hooks.iUrl);
        // iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),    null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),     pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"),null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),     "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),           null);

        // ── Account Expired detection ─────────────────────────────────────────────────────
        By iExpiredBy = By.xpath(ObjReader.getLocator("iAccountExpiredError"));
        if (isVisible(iExpiredBy, 3))
        {
            String iErrText = getDriver().findElement(iExpiredBy).getText().trim();
            if (iErrText.toLowerCase().contains("account expired") || iErrText.toLowerCase().contains("account has expired")|| iErrText.contains("Invalid username or password."))
            {
                log.warning("[TC13-RELOGIN] Account Expired for: " + pUsername + " — marking expired.");
                Hooks.markAgentExpired(pUsername);
                return;
            }
        }

        // PIN screen (appears for some accounts — safe to skip if not present)
        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));
        if (isVisible(iPinFormBy, 3))
        {
            log.info("[RELOGIN] PIN screen detected.");
            for (int idx = 1; idx <= 7; idx++)
            {
                String iDynXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));
                By iPinInputBy   = By.xpath(iDynXpath);
                if (isVisible(iPinInputBy, 1))
                {
                    org.openqa.selenium.WebElement iInput = getDriver().findElement(iPinInputBy);
                    if (iInput.getAttribute("disabled") == null && iInput.isEnabled())
                    {
                        iInput.clear();
                        iInput.sendKeys("1");
                        log.info("[RELOGIN] PIN slot " + idx + " filled.");
                    }
                }
            }
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iPinLoginBtn"),   null);
            Thread.sleep(2000);
            log.info("[LOGIN] PIN login submitted.");

            for (int iNext = 1; iNext <= 6; iNext++)
            {
                if (isVisible(By.xpath(ObjReader.getLocator("iNextBtnNewUser")), 1))
                {
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtnNewUser"), null);
                    log.info("Clicked Next button - Attempt " + iNext);
                }
                else
                {
                    log.info("Next button no longer available after " + (iNext - 1) + " clicks.");
                    break;
                }
            }
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"),   "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);
            log.info("[RELOGIN] PIN + TOTP submitted.");
        }
        else
        {
            // OTP-only path
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),  null);
            log.info("[RELOGIN] OTP-only login submitted.");
        }

        // Terms & Conditions (appears rarely — accept silently if shown)
        if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[RELOGIN] Terms & Conditions accepted.");
        }

        log.info("[RELOGIN] Login complete for: " + pUsername);


    }
    /** SIMPLE helper to avoid ambiguous method call */
    private static boolean isVisible(By locator, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(CommonFunctions.getDriver(), Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
