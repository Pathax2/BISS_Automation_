package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import utilities.ExcelUtilities;

public class Hooks
{
    public static XWPFDocument iDocument;
    public static String iDocPath = "";
    public static String iTestCaseID = "";
    public static String iEnvironment = "";
    public static String iUrl = "";

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeAllExecution
    // Description   : Executes once before current testcase feature starts. Reads testcase and environment from runner,
    //                 loads test data, fetches url from config sheet, launches browser and starts report.
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    @BeforeAll
    public static void beforeAllExecution()
    {
        try
        {
            iTestCaseID = System.getProperty("testcase", "").trim();
            iEnvironment = System.getProperty("environment", "").trim();

            if (iTestCaseID.isEmpty())
            {
                throw new RuntimeException("TestCase_ID not received from TestRunner.");
            }

            if (iEnvironment.isEmpty())
            {
                throw new RuntimeException("Environment not received from TestRunner.");
            }

            ExcelUtilities iTestDataExcel = new ExcelUtilities("src/test/resources/Test_Data/TestData.xlsx");
            iTestDataExcel.loadCurrentTestDataRow("Data", iTestCaseID);

            int iConfigRow = iTestDataExcel.findRow("Config", "Env", iEnvironment);

            if (iConfigRow == -1)
            {
                throw new RuntimeException("Environment not found in Config sheet : " + iEnvironment);
            }

            iUrl = iTestDataExcel.getCellValue("Config", iConfigRow, "URL");

            if (iUrl.isEmpty())
            {
                throw new RuntimeException("URL is blank for environment : " + iEnvironment);
            }

            CommonFunctions.launchBrowser(iUrl);

            Object[] iReportObjects = CommonFunctions.startWordReport(iTestCaseID);
            iDocument = (XWPFDocument) iReportObjects[0];
            iDocPath = String.valueOf(iReportObjects[1]);

            CommonFunctions.log.info("Execution started for TestCase_ID : " + iTestCaseID);
            CommonFunctions.log.info("Environment : " + iEnvironment);
            CommonFunctions.log.info("URL : " + iUrl);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("BeforeAll execution failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeScenarioExecution
    // Description   : Executes before every scenario and logs scenario details
    // Parameters    : pScenario (Scenario) - current scenario
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    @Before
    public void beforeScenarioExecution(Scenario pScenario)
    {
        CommonFunctions.log.info("Scenario started : " + pScenario.getName());
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterScenarioExecution
    // Description   : Executes after every scenario. Adds screenshot to report if scenario fails
    // Parameters    : pScenario (Scenario) - current scenarios
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    @After
    public void afterScenarioExecution(Scenario pScenario)
    {
        try
        {
            if (pScenario.isFailed())
            {
                CommonFunctions.log.severe("Scenario failed : " + pScenario.getName());

                if (iDocument != null && iDocPath != null && !iDocPath.isEmpty())
                {
                    CommonFunctions.addScreenshotToReport(iDocument, iDocPath, iTestCaseID);
                }
            }
            else
            {
                CommonFunctions.log.info("Scenario passed : " + pScenario.getName());
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("After scenario hook failed : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterAllExecution
    // Description   : Executes once after current testcase feature finishes. Finalizes report and closes browser
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    @AfterAll
    public static void afterAllExecution()
    {
        try
        {
            if (iDocument != null && iDocPath != null && !iDocPath.isEmpty())
            {
                CommonFunctions.finalizeWordReport(iDocument, iDocPath);
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Report finalize failed : " + iException.getMessage());
        }
        finally
        {
            try
            {
                if (CommonFunctions.iDriver != null)
                {
                    CommonFunctions.iDriver.quit();
                    CommonFunctions.log.info("Browser closed successfully for TestCase_ID : " + iTestCaseID);
                }
            }
            catch (Exception iException)
            {
                CommonFunctions.log.severe("Browser close failed : " + iException.getMessage());
            }
        }
    }
}
