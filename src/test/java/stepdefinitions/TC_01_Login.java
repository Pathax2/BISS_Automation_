// ===================================================================================================================================
// File          : TC_01_Login.java
// Package       : stepdefinitions
// Description   : Step definitions for TC_01 — Login into the application.
//                 Maps Gherkin steps from TC_01.feature to CommonFunctions actions.
//                 Locators sourced exclusively from ObjReader (ObjectRepository.properties).
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjReader;

import static commonFunctions.CommonFunctions.iAction;

public class TC_01_Login
{
    // ***************************************************************************************************************************************************************************************
    // Function Name : launchApplication
    // Description   : Given step — confirms application launch. Browser is already open from Hooks.beforeAllExecution().
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Hooks.beforeAllExecution() must have completed successfully
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Given("User Launches the application")
    public void launchApplication()
    {
        CommonFunctions.log.info("Application launched via Hooks. Browser is active and URL loaded.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : enterValidFieldValue
    // Description   : Enters Username or Password based on the string argument passed from the feature file.
    //                 Username flow : clicks Welcome Login button → enters username → clicks Continue button
    //                 Password flow : enters password into the password field
    // Parameters    : pColumnName (String) - "Username" or "Password" as passed from the Gherkin step
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Browser must be open; test data row must be loaded for TD: resolution
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user enters valid {string}")
    public void enterValidFieldValue(String pColumnName)
    {
        try
        {
            if (pColumnName == null || pColumnName.trim().isEmpty())
            {
                throw new RuntimeException("Field name passed from feature file cannot be blank.");
            }

            if (pColumnName.equalsIgnoreCase("Username"))
            {
                CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), "");
                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), "TD:Username");
                CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), "");
                CommonFunctions.log.info("Username entered and Continue clicked successfully.");
            }
            else if (pColumnName.equalsIgnoreCase("Password"))
            {
                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
                CommonFunctions.log.info("Password entered successfully.");
            }
            else
            {
                throw new RuntimeException("Unsupported field value from feature file : '" + pColumnName + "'. Accepted values: Username, Password.");
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Failed to enter value for field : '" + pColumnName + "' | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : clickLoginButton
    // Description   : Clicks the login submit button and enters the MFA OTP code.
    //                 NOTE: OTP value "111111" is a placeholder — replace with dynamic OTP generation or
    //                 a TD:OTP test data column when a real authenticator integration is in place.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Username and password must already have been entered
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user clicks on Login button")
    public void clickLoginButton()
    {
        try
        {
            CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), "");
            CommonFunctions.log.info("Login button clicked.");
            CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            CommonFunctions.log.info("MFA OTP entered successfully.");
            CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), "");
            CommonFunctions.log.info("Login button clicked.");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Failed during login button click or OTP entry : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyLoginSuccessful
    // Description   : Verifies login success by reading text from the dashboard title element.
    //                 BUG FIX: Original code used iLoginbtn (the login button locator) for GETTEXT after login —
    //                 this is incorrect. The correct locator is iDashboardTitle which displays after a successful login.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Login must have been completed; dashboard must be loaded
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("User should be successfully able to login")
    public void verifyLoginSuccessful()
    {
        try
        {
            // Verify dashboard title element is visible — confirms successful login and page load
            CommonFunctions.iAction("VERIFYELEMENT", ObjReader.getLocatorType("iDashboardTitle"), ObjReader.getLocator("iDashboardTitle"), "");

            // Capture dashboard title text for log confirmation
            String iDashboardText = CommonFunctions.iAction("GETTEXT", ObjReader.getLocatorType("iDashboardTitle"), ObjReader.getLocator("iDashboardTitle"), "");

            if (iDashboardText == null || iDashboardText.trim().isEmpty())
            {
                throw new AssertionError("Login verification failed. Dashboard title text is blank after login.");
            }

            CommonFunctions.log.info("Login verified successfully. Dashboard title : '" + iDashboardText + "'");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Login verification step failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : LogoutApplication
    // Description   : Clicks the logout Sign Out Button.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Username and password must already have been entered
    // Date Created  : 12-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("A user logging out of an application")
    public void LogoutApplication()
    {
        try
        {
            CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"), "");
            CommonFunctions.log.info("Logout/Signout button clicked.");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Failed during logging out of application : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : enterValidFieldValue
    // Description   : Enters Username or Password based on the string argument passed from the feature file.
    //                 Username flow : clicks Welcome Login button → enters username → clicks Continue button
    //                 Password flow : enters password into the password field
    // Parameters    : pColumnName (String) - "Username" or "Password" as passed from the Gherkin step
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Browser must be open; test data row must be loaded for TD: resolution
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user enters invalid credentials {string}")
    public void enterInvalidCredentials(String pColumnName)
    {
        try
        {
            if (pColumnName == null || pColumnName.trim().isEmpty())
            {
                throw new RuntimeException("Field name passed from feature file cannot be blank.");
            }
            // Hit the initial 'Log In' button on the BISS landing screen to get to the Keycloak form
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

            if (pColumnName.equalsIgnoreCase("Username"))
            {

            }
            else if (pColumnName.equalsIgnoreCase("Password"))
            {
                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), "TD:Username");
                CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), "");
                CommonFunctions.log.info("Username entered and Continue clicked successfully.");

                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "Invalid");
                CommonFunctions.log.info("Invalid Password entered successfully.");

                CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), "");
                CommonFunctions.log.info("Login button clicked.");

                // Verify Authentication Page shows error message when invalid credentials are passed
                CommonFunctions.iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iInvalidCredErr"), "");

                // Capture dashboard title text for log confirmation
                String iErrText = CommonFunctions.iAction("GETTEXT", "XPATH", ObjReader.getLocator("iInvalidCredErr"), "");

                if (iErrText == null || iErrText.trim().isEmpty())
                {
                    throw new AssertionError("Login verification failed.Error Message should get displayed post entering invalid credentials");
                }
                CommonFunctions.log.info("Invalid Credentials Error Message Validation Completed : '" + iErrText + "'");
                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
                CommonFunctions.log.info("Password entered successfully.");
            }
            else
            {
                throw new RuntimeException("Unsupported field value from feature file : '" + pColumnName + "'. Accepted values: Username, Password.");
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Failed to enter value for field : '" + pColumnName + "' | Reason : " + iException.getMessage(), iException);
        }
    }
}
