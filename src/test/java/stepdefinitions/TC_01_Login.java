package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjectRepositoryReader;

public class TC_01_Login
{
    // ***************************************************************************************************************************************************************************************
    // Function Name : userLaunchesTheApplication
    // Description   : Launches application URL from Config sheet loaded through TestRunner
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : URL should already be loaded from TestData Config sheet into runtime property
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    @Given("User Launches the application")
    public void userLaunchesTheApplication()
    {
        String iApplicationURL = System.getProperty("url");

        if (iApplicationURL == null || iApplicationURL.trim().isEmpty())
        {
            throw new RuntimeException("Application URL is blank. Please check Config sheet in TestData.xlsx and TestRunner loading.");
        }

        CommonFunctions.iDriver.get(iApplicationURL);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : userEntersValid
    // Description   : Enters value from current TestData row into the target field using actual column name from feature file
    // Parameters    : iColumnName (String) - actual column name from TestData sheet like Username or Password
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : Current TestData row should already be loaded for executing TestCase_ID
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("user enters valid {string}")
    public void userEntersValid(String iColumnName)
    {
        if (iColumnName.equalsIgnoreCase("Username"))
        {
            String iLocatorType = ObjectRepositoryReader.getLocatorType("login.username.id");
            String iLocatorValue = ObjectRepositoryReader.getLocatorValue("login.username.id");

            CommonFunctions.iAction("TEXTBOX", iLocatorType, iLocatorValue, "TD:Username");
        }
        else if (iColumnName.equalsIgnoreCase("Password"))
        {
            String iLocatorType = ObjectRepositoryReader.getLocatorType("login.password.id");
            String iLocatorValue = ObjectRepositoryReader.getLocatorValue("login.password.id");

            CommonFunctions.iAction("TEXTBOX", iLocatorType, iLocatorValue, "TD:Password");
        }
        else
        {
            throw new RuntimeException("Unsupported column passed in feature file : " + iColumnName);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : userClicksOnLoginButton
    // Description   : Clicks on login button using object repository
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : Login button locator should be available in ObjectRepository.properties
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("user clicks on Login button")
    public void userClicksOnLoginButton()
    {
        String iLocatorType = ObjectRepositoryReader.getLocatorType("login.button.id");
        String iLocatorValue = ObjectRepositoryReader.getLocatorValue("login.button.id");

        CommonFunctions.iAction("CLICK", iLocatorType, iLocatorValue, "");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : userShouldBeSuccessfullyAbleToLogin
    // Description   : Verifies login success using current URL check after clicking login
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : Login action should already be completed
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    @Then("User should be successfully able to login")
    public void userShouldBeSuccessfullyAbleToLogin()
    {
        String iCurrentURL = CommonFunctions.iDriver.getCurrentUrl();

        if (iCurrentURL == null || iCurrentURL.trim().isEmpty())
        {
            throw new AssertionError("Login validation failed because current URL is blank.");
        }

        if (iCurrentURL.toLowerCase().contains("login"))
        {
            throw new AssertionError("Login validation failed. User is still on login page. Current URL : " + iCurrentURL);
        }

        System.out.println("Login successful. Current URL after login : " + iCurrentURL);
    }
}