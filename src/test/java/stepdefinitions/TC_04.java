// ===================================================================================================================================
// File          : TC_04.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_04 — Verify the Dashboard Page Functionalities.
//
//                 Validates all major interactive elements on the BISS Agent Home Dashboard:
//
//                   1. Quick Links section:
//                        - Transfer of Entitlements hyperlink
//                        - CISYF hyperlink
//                   2. BISS Applications section:
//                        - View Clients button
//                   3. News section:
//                        - Read More button
//                   4. Contact Us button
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

public class TC_04
{
    private static final Logger log = Logger.getLogger(TC_04.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent navigates to the {string} tab
    // Description   : Clicks the named top-level navigation tab on the BISS portal header
    //                 e.g. "Home", "My Clients"
    // Parameters    : pTab (String) - visible tab label
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent navigates to the {string} tab")
    public void theAgentNavigatesToTab(String pTab)
    {
        // Click 'Home' first to reset the navigation to a known starting state
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the {string} hyperlink in the Quick Links section
    // Description   : Clicks the named hyperlink inside the Quick Links section of the Dashboard
    // Parameters    : pLink (String) - hyperlink label e.g. "Transfer of Entitlements", "CISYF"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent clicks on the {string} hyperlink in the Quick Links section")
    public void theAgentClicksOnQuickLinksHyperlink(String pLink)
    {
        // The spinner needs to disappear before we try to read the page title —
        // otherwise we might check the heading while the page is still loading
        iAction("WAITINVISIBLE", "XPATH", "iMDCGenericSpinner", "MDC Progress Spinner");
        iAction("CLICK", "XPATH", "//div[normalize-space()='"+ pLink +"']/following-sibling::div//a", null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the View Clients button in the BISS Applications section
    // Description   : Clicks the View Clients button inside the BISS Applications section
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the View Clients button in the BISS Applications section")
    public void theAgentClicksOnViewClientsButton()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewTotalClientsBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the Read More button in the News section
    // Description   : Clicks the Read More button inside the News section of the Dashboard
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("the agent clicks on the Read More button in the News section")
    public void theAgentClicksOnReadMoreInNewsSection()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iReadMoreBtn"), null);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent clicks on the Contact Us button
    // Description   : Clicks the Contact Us button on the Agent Dashboard
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent clicks on the Contact Us button")
    public void theAgentClicksOnContactUsButton()
    {
        iAction("CLICK", "XPATH", ObjReader.getLocator("iContactUsBtn"), null);
    }
}
