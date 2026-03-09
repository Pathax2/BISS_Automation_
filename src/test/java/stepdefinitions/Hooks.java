package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Hooks
{
    public static XWPFDocument iDocument;
    public static String iDocPath = "";
    public static String iTestCaseID = "";
    public static String iUrl = "";
    public static String iModel = "";

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeAllExecution
    // Description   : Launches browser once before all scenarios and opens the application URL
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : Runtime property url should be available from TestRunner
    // Date Created  : 07-03-2026
    // ***************************************************************************************************************************************************************************************
    @BeforeAll
    public static void beforeAllExecution()
    {
        try
        {
            iUrl = System.getProperty("url", "").trim();
            iModel = System.getProperty("model", "").trim();

            WebDriverManager.chromedriver().setup();

            ChromeOptions iChromeOptions = new ChromeOptions();
            iChromeOptions.addArguments("--start-maximized");

            CommonFunctions.iDriver = new ChromeDriver(iChromeOptions);
            CommonFunctions.iWait = new WebDriverWait(CommonFunctions.iDriver, Duration.ofSeconds(20));

            if (!iUrl.isEmpty())
            {
                CommonFunctions.iDriver.get(iUrl);
            }

            CommonFunctions.log.info("Browser launched once before all scenarios");
            CommonFunctions.log.info("Application URL : " + iUrl);
            CommonFunctions.log.info("Model           : " + iModel);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("BeforeAll hook failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterAllExecution
    // Description   : Closes browser once after all scenarios are completed
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : Browser should already be launched
    // Date Created  : 07-03-2026
    // ***************************************************************************************************************************************************************************************
    @AfterAll
    public static void afterAllExecution()
    {
        try
        {
            if (CommonFunctions.iDriver != null)
            {
                CommonFunctions.iDriver.quit();
                CommonFunctions.log.info("Browser closed after all scenarios");
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("AfterAll hook failed : " + iException.getMessage());
        }
    }
}