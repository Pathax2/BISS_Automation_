package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

public class Hooks
{
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

            CommonFunctions.launchBrowser();

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