// ===================================================================================================================================
// File          : TC_09.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_09 — BISSAGL-20849 Agent unable to view payments
//                 or land details for client.
//
//                 Covers one active scenario:
//
//                   - Login with a specific fixed agent username (agr14385)
//                   - Search for a specific herd in My Clients (D3350968)
//                   - Navigate to Applications / Payments via side navigation
//                   - Select scheme year 2023 from dropdown
//                   - Click the Payments tab
//                   - Verify no technical error message is displayed on the page
//
//                 Naming conventions used throughout:
//                   iAction(actionType, identifyBy, locator, value)  — all UI interactions
//                   ObjReader.getLocator("keyName")                   — all locator lookups
//
//                 Reused steps (defined elsewhere, bound automatically by Cucumber):
//                   "the agent user is on the login page"                          → TC_01_Login.java
//                   "the agent opens the BISS application"                         → TC_01_Login.java
//                   "the agent should land on the BISS Home page"                  → TC_01_Login.java
//                   "the agent navigates to the Home and My Clients..."            → TC_04.java
//                   "the agent searches for herd number {string}"                  → TC_05.java
//                   "the agent clicks on the View Dashboard button"                → TC_05.java
//                   "the agent selects {string} from the {string} dropdown"        → TC_06.java
//                   "the agent clicks on the {string} tab"                         → TC_09.java (below)
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjReader;

import java.util.List;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_09
{
    private static final Logger log = Logger.getLogger(TC_09.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs into the application with the username {string} and valid credentials
    // Description   : Enters the given fixed username into the login field then completes
    //                 the standard password and OTP flow.
    //                 Used in TC_09 Background where a specific agent account is required
    //                 rather than the dynamically resolved runtime username.
    // Parameters    : pUsername (String) - agent username e.g. "agr14385"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs into the application with the username {string} and valid credentials")
    public void theAgentLogsInWithSpecificUsername(String pUsername)
    {
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iNewAgentLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernameTxtbox"),   pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginBtn"),         null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordTxtbox"),   "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginBtn"),         null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOTPTxtbox"),        "TD:OTP");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginBtn"),         null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the {string} link in the side navigation bar
    // Description   : Clicks the named link in the left-side navigation bar on the farmer dashboard.
    //                 e.g. "Applications / Payments", "Correspondence", "Land Details"
    // Parameters    : pLink (String) - link label text
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clicks on the {string} link in the side navigation bar")
    public void theAgentClicksOnLinkInSideNavBar(String pLink)
    {
        iAction("CLICK", "XPATH",
                "//a[normalize-space()='" + pLink.trim() + "']"
                        + " | //span[normalize-space()='" + pLink.trim() + "']"
                        + " | //li[normalize-space()='" + pLink.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the {string} tab
    // Description   : Clicks the named tab on the Applications / Payments screen
    //                 e.g. "Payments", "Applications"
    // Parameters    : pTab (String) - visible tab label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the {string} tab")
    public void theAgentClicksOnTab(String pTab)
    {
        iAction("CLICK", "XPATH",
                "//button[normalize-space()='" + pTab.trim() + "']"
                        + " | //a[normalize-space()='" + pTab.trim() + "']"
                        + " | //mat-tab-label[normalize-space()='" + pTab.trim() + "']",
                null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : no technical error is displayed on the page for herd {string}
    // Description   : Verifies that the technical error message documented in BISSAGL-20849
    //                 is not visible on the current page for the given herd number.
    //                 The assertion fails if the error element is found and displayed.
    // Parameters    : pHerd (String) - herd number for context in the assertion message
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("no technical error is displayed on the page for herd {string}")
    public void noTechnicalErrorIsDisplayedOnThePageForHerd(String pHerd)
    {
        String iErrorText = "Techinal error getting information extra information for herd number";
        List<org.openqa.selenium.WebElement> iErrorElements =
                getDriver().findElements(
                        org.openqa.selenium.By.xpath(
                                "//*[contains(normalize-space(),'" + iErrorText + "')]"));

        if (!iErrorElements.isEmpty() && iErrorElements.get(0).isDisplayed())
        {
            throw new AssertionError(
                    "Technical error message should NOT be visible for herd [" + pHerd + "] "
                            + "but was found on the page: " + iErrorText);
        }

        log.info("[TC_09] Verified — no technical error visible for herd: " + pHerd);
    }
}