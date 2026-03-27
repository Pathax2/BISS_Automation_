// ===================================================================================================================================
// File          : TC_10.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_10 — BISSAGL-20695 Reference Number Agent User.
//
//                 Covers the Create Client form flow:
//
//                   1. Navigate to No Herd Number tab on My Clients
//                   2. Open the Create Client form via stepper button
//                   3. Fill in all form fields: name, address lines, county, eircode,
//                      contact number, BISS checkbox, herd number
//                   4. Submit the form and handle the post-creation dialog sequence
//                      (I understand → Edit → Close)
//                   5. Open Create Client form a second time and re-enter name
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
//                   "the agent clicks on the {string} stepper button"              → TC_03.java
//                   "the agent selects {string} from the {string} dropdown"        → TC_06.java
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 26-03-2026
// ===================================================================================================================================

package stepdefinitions;

import io.cucumber.java.en.And;
import utilities.ObjReader;

import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_10
{
    private static final Logger log = Logger.getLogger(TC_10.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent enters {string} in the {string} form field
    // Description   : Types text into the named Angular form control field in the Create Client form.
    //                 The field identifier maps to the formcontrolname, id, or name attribute.
    // Parameters    : pText  (String) - value to type e.g. "Kale", "D12345"
    //                 pField (String) - form control identifier e.g. "name", "add1", "eircode"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters {string} in the {string} form field")
    public void theAgentEntersInFormField(String pText, String pField)
    {
        iAction("TEXTBOX", "XPATH",
                "//*[@formcontrolname='" + pField.trim() + "']"
                        + " | //*[@id='" + pField.trim() + "']"
                        + " | //*[@name='" + pField.trim() + "']",
                pText);
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent ticks the {string} checkbox
    // Description   : Checks the named checkbox on the Create Client form.
    //                 Handles both native checkboxes and Angular Material mat-checkbox components.
    // Parameters    : pLabel (String) - visible checkbox label e.g. "BISS"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent ticks the {string} checkbox")
    public void theAgentTicksCheckbox(String pLabel)
    {
        iAction("CHECKBOX", "XPATH",
                "//label[normalize-space()='" + pLabel.trim() + "']/preceding-sibling::input[@type='checkbox']"
                        + " | //mat-checkbox[.//*[normalize-space()='" + pLabel.trim() + "']]",
                "CHECK");
    }
}