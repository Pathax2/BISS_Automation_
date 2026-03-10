package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjReader;
public class TC_01_Login
{
    // ***************************************************************************************************************************************************************************************
    // Function Name : launchApplication
    // Description   : Launch application step. Actual browser launch already happens in hooks.
    // Parameters    : None
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    @Given("User Launches the application")
    public void launchApplication()
    {
        CommonFunctions.log.info("Application launched successfully through Hooks.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : enterValidFieldValue
    // Description   : Enters username or password based on feature file input
    // Parameters    : pColumnName (String) - Username or Password
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    @When("user enters valid {string}")
    public void enterValidFieldValue(String pColumnName)
    {
        try
        {
            if (pColumnName.equalsIgnoreCase("Username"))
            {
                CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocatorValue("iWelcomeLoginBtn"),"");
                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocatorValue("iUsernametxtbox"), "TD:Username");
                CommonFunctions.log.info("Username entered successfully.");

                CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocatorValue("iUsernameContinuebtn"),"");
            }
            else if (pColumnName.equalsIgnoreCase("Password"))
            {
                CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocatorValue("iPasswordtxtbox"), "TD:Password");
                CommonFunctions.log.info("Password entered successfully.");
            }
            else
            {
                throw new RuntimeException("Unsupported field value passed from feature file : " + pColumnName);
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Failed while entering value for field : " + pColumnName + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : clickLoginButton
    // Description   : Clicks login button
    // Parameters    : None
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    @When("user clicks on Login button")
    public void clickLoginButton()
    {
        try
        {
            CommonFunctions.iAction("CLICK", "XPATH", ObjReader.getLocatorValue("iLoginbtn"),"");
            CommonFunctions.log.info("Login button clicked successfully.");
            CommonFunctions.iAction("TEXTBOX", "XPATH", ObjReader.getLocatorValue("iOPTtxtbox"), "111111");
            CommonFunctions.log.info("Microsoft Authenticator one-time code  entered successfully.");

        }
        catch (Exception iException)
        {
            throw new RuntimeException("Failed while clicking login button : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyLoginSuccessful
    // Description   : Verifies login success by checking dashboard element text
    // Parameters    : None
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    @Then("User should be successfully able to login")
    public void verifyLoginSuccessful()
    {
        try
        {
            String iDashboardText = CommonFunctions.iAction("GETTEXT", "XPATH", ObjReader.getLocatorValue("iLoginbtn"),"");

            if (iDashboardText == null || iDashboardText.trim().isEmpty())
            {
                throw new AssertionError("Login verification failed. Dashboard element text is blank.");
            }

            CommonFunctions.log.info("Login verified successfully. Dashboard text : " + iDashboardText);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Login verification failed : " + iException.getMessage(), iException);
        }
    }
}
