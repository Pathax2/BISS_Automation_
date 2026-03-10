package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.ObjectRepositoryReader;

public class TC_01_Login
{
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

    @And("user clicks on Login button")
    public void userClicksOnLoginButton()
    {
        String iLocatorType = ObjectRepositoryReader.getLocatorType("login.button.id");
        String iLocatorValue = ObjectRepositoryReader.getLocatorValue("login.button.id");

        CommonFunctions.iAction("CLICK", iLocatorType, iLocatorValue, "");
    }

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