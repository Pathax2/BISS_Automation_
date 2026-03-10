package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TC_01_Login
{

    // ***************************************************************************************************************************************************************************************
    // Function Name : launchApplication
    // Description   : Verifies that application launch step is executed. Browser launch and URL navigation
    //                 are already handled inside Hooks.java BeforeAll method.
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Hooks should have already launched browser and opened the application URL
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Given("User Launches the application")
    public void launchApplication()
    {
        try
        {
            CommonFunctions.log.info("Application launch step executed successfully.");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed during application launch step: " + e.getMessage(), e);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : enterUsername
    // Description   : Enters username into username textbox. Username value is fetched from TestData.xlsx
    //                 using TD:Username mapping
    // Parameters    : pColumnName (String) - column name from feature file (Username)
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Username textbox should be present on the login page
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user enters valid {string}")
    public void enterUsername(String pColumnName)
    {
        try
        {
            CommonFunctions.iAction(
                    "TEXTBOX",
                    "ID",
                    "username",
                    "TD:" + pColumnName
            );

            CommonFunctions.log.info("Username entered successfully using column: " + pColumnName);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed while entering username: " + e.getMessage(), e);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : enterPassword
    // Description   : Enters password into password textbox. Password value is fetched from TestData.xlsx
    //                 using TD:Password mapping
    // Parameters    : pColumnName (String) - column name from feature file (Password)
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Password textbox should be present on the login page
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user enters valid {string}")
    public void enterPassword(String pColumnName)
    {
        try
        {
            CommonFunctions.iAction(
                    "TEXTBOX",
                    "ID",
                    "password",
                    "TD:" + pColumnName
            );

            CommonFunctions.log.info("Password entered successfully using column: " + pColumnName);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed while entering password: " + e.getMessage(), e);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : clickLoginButton
    // Description   : Clicks the login button on the login page
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Login button should be visible and clickable
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user clicks on Login button")
    public void clickLoginButton()
    {
        try
        {
            CommonFunctions.iAction(
                    "CLICK",
                    "ID",
                    "loginButton",
                    ""
            );

            CommonFunctions.log.info("Login button clicked successfully.");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed while clicking login button: " + e.getMessage(), e);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyLoginSuccessful
    // Description   : Verifies that user is logged into the application by checking presence of dashboard element
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Successful login should redirect user to dashboard/home page
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("User should be successfully able to login")
    public void verifyLoginSuccessful()
    {
        try
        {
            String iDashboardText = CommonFunctions.iAction(
                    "GETTEXT",
                    "ID",
                    "dashboard",
                    ""
            );

            if (iDashboardText == null || iDashboardText.isEmpty())
            {
                throw new AssertionError("Login verification failed. Dashboard text not found.");
            }

            CommonFunctions.log.info("Login verified successfully. Dashboard text: " + iDashboardText);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Login verification failed: " + e.getMessage(), e);
        }
    }

}