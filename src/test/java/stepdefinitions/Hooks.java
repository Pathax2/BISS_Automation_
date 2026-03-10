package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Hooks
{
    public static XWPFDocument iDocument;
    public static String iDocPath = "";
    public static String iTestCaseID = "";
    public static String iUrl = "";
    public static String iEnvironment = "";
    public static String iModel = "";

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeAllExecution
    // Description   : Executes once before all scenarios. Reads runtime values from system properties,
    //                 launches browser, opens application URL, and starts Word report creation.
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : TestRunner should already set testcase, url, env, and model using System.setProperty
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @BeforeAll
    public static void beforeAllExecution()
    {
        try
        {
            // Read runtime values sent from TestRunner
            iTestCaseID = System.getProperty("testcase", "").trim();
            iUrl = System.getProperty("url", "").trim();
            iEnvironment = System.getProperty("env", "").trim();
            iModel = System.getProperty("model", "").trim();

            // Basic validation before starting execution
            if (iTestCaseID.isEmpty())
            {
                throw new RuntimeException("System property 'testcase' is blank. Please verify TestRunner.java");
            }

            if (iUrl.isEmpty())
            {
                throw new RuntimeException("System property 'url' is blank. Please verify Config sheet reading in TestRunner.java");
            }

            // Launch browser only once
            CommonFunctions.launchBrowser();

            if (CommonFunctions.iDriver == null)
            {
                throw new RuntimeException("WebDriver is null after browser launch.");
            }

            // Open application URL
            CommonFunctions.iDriver.get(iUrl);

            // Create Word report using your actual CommonFunctions method
            Object[] iReportDetails = CommonFunctions.startWordReport(iTestCaseID);

            if (iReportDetails != null && iReportDetails.length == 2)
            {
                iDocument = (XWPFDocument) iReportDetails[0];
                iDocPath = String.valueOf(iReportDetails[1]);
            }

            // Logging execution details
            CommonFunctions.log.info("======================================================================");
            CommonFunctions.log.info("Automation execution started");
            CommonFunctions.log.info("TestCase_ID : " + iTestCaseID);
            CommonFunctions.log.info("Environment : " + iEnvironment);
            CommonFunctions.log.info("URL         : " + iUrl);
            CommonFunctions.log.info("Model       : " + iModel);
            CommonFunctions.log.info("Doc Path    : " + iDocPath);
            CommonFunctions.log.info("======================================================================");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("BeforeAll execution failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : beforeScenarioExecution
    // Description   : Executes before every scenario and writes scenario details into console log
    // Parameters    : pScenario (Scenario) - currently executing cucumber scenario
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Browser should already be launched successfully
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Before
    public void beforeScenarioExecution(Scenario pScenario)
    {
        try
        {
            CommonFunctions.log.info("--------------------------------------------------------------------");
            CommonFunctions.log.info("Scenario started : " + pScenario.getName());
            CommonFunctions.log.info("Scenario URI     : " + pScenario.getUri());
            CommonFunctions.log.info("--------------------------------------------------------------------");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Before scenario hook failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterScenarioExecution
    // Description   : Executes after every scenario. If scenario fails, screenshot is captured
    //                 and appended to the Word report.
    // Parameters    : pScenario (Scenario) - completed cucumber scenario
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Word report should already be initialized
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @After
    public void afterScenarioExecution(Scenario pScenario)
    {
        try
        {
            String iScenarioName = pScenario.getName();

            if (pScenario.isFailed())
            {
                CommonFunctions.log.severe("Scenario failed : " + iScenarioName);

                // Add screenshot to Word report only if document exists
                if (iDocument != null && iDocPath != null && !iDocPath.trim().isEmpty())
                {
                    CommonFunctions.addScreenshotToReport(iDocument, iDocPath, iTestCaseID);
                    CommonFunctions.log.info("Failure screenshot added to report for test case : " + iTestCaseID);
                }
                else
                {
                    CommonFunctions.log.warning("Word report document/path is not available, so screenshot was not added.");
                }
            }
            else
            {
                CommonFunctions.log.info("Scenario passed : " + iScenarioName);
            }

            CommonFunctions.log.info("Scenario completed : " + iScenarioName);
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("After scenario hook failed : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : afterAllExecution
    // Description   : Executes once after all scenarios are completed. Finalizes the Word report
    //                 and closes the browser instance.
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Browser may be open and report may be initialized
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @AfterAll
    public static void afterAllExecution()
    {
        try
        {
            // Finalize Word report if available
            if (iDocument != null && iDocPath != null && !iDocPath.trim().isEmpty())
            {
                CommonFunctions.finalizeWordReport(iDocument, iDocPath);
                CommonFunctions.log.info("Word report finalized successfully : " + iDocPath);
            }
            else
            {
                CommonFunctions.log.warning("Word report object not available, so finalize step was skipped.");
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("AfterAll report finalization failed : " + iException.getMessage());
        }
        finally
        {
            try
            {
                if (CommonFunctions.iDriver != null)
                {
                    CommonFunctions.iDriver.quit();
                    CommonFunctions.log.info("Browser closed successfully after execution");
                }
            }
            catch (Exception iException)
            {
                CommonFunctions.log.severe("Browser close failed in AfterAll : " + iException.getMessage());
            }
        }
    }
}