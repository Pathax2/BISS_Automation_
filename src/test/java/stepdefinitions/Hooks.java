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
    public static XWPFDocument iDocument    = null;
    public static String       iDocPath     = "";
    public static String       iTestCaseID  = "";
    public static String       iEnvironment = "";
    public static String       iUrl         = "";

    private static final StringBuilder iAccumulatedErrors  = new StringBuilder();
    private static String              iLastScreenshotPath = "";

    private static final String iTestDataFilePath  = "src/test/resources/Test_Data/TestData.xlsx";
    private static final String iTestDataSheetName = "Data";
    private static final String iConfigSheetName   = "Config";
    private static final String iDefaultBrowser    = "CHROME";

    @BeforeAll
    public static void beforeAllExecution()
    {
        try
        {
            iTestCaseID  = System.getProperty("testcase",    "").trim();
            iEnvironment = System.getProperty("environment", "").trim();

            if (iTestCaseID.isEmpty())  { throw new RuntimeException("TestCase_ID system property missing."); }
            if (iEnvironment.isEmpty()) { throw new RuntimeException("Environment system property missing."); }

            iAccumulatedErrors.setLength(0);
            iLastScreenshotPath = "";
            iDocument           = null;
            iDocPath            = "";

            CommonFunctions.log.info("========== EXECUTION START : " + iTestCaseID + " ==========");
            CommonFunctions.loadDescriptionCache();

            ExcelUtilities iTestDataExcel = new ExcelUtilities(iTestDataFilePath);
            iTestDataExcel.loadCurrentTestDataRow(iTestDataSheetName, iTestCaseID);

            int iConfigRow = iTestDataExcel.findRow(iConfigSheetName, "Env", iEnvironment);
            if (iConfigRow == -1) { throw new RuntimeException("Environment not found in Config sheet : " + iEnvironment); }

            iUrl = iTestDataExcel.getCellValue(iConfigSheetName, iConfigRow, "URL").trim();
            if (iUrl.isEmpty()) { throw new RuntimeException("URL blank for environment : " + iEnvironment); }

            String iBrowserType = System.getProperty("browser", iDefaultBrowser).trim().toUpperCase();
            CommonFunctions.launchBrowser(iBrowserType, iUrl);

            Object[] iReportObjects = CommonFunctions.startWordReport(iTestCaseID);
            iDocument = (XWPFDocument) iReportObjects[0];
            iDocPath  = String.valueOf(iReportObjects[1]);

            CommonFunctions.log.info("BeforeAll complete | URL=" + iUrl + " | Report=" + iDocPath);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("BeforeAll failed for [" + iTestCaseID + "] : " + iException.getMessage(), iException);
        }
    }

    @Before
    public void beforeScenarioExecution(Scenario pScenario)
    {
        CommonFunctions.log.info("---------- Scenario START : " + pScenario.getName() + " ----------");
    }

    @After
    public void afterScenarioExecution(Scenario pScenario)
    {
        try
        {
            if (pScenario.isFailed())
            {
                CommonFunctions.log.severe("---------- Scenario FAILED : " + pScenario.getName() + " ----------");

                iAccumulatedErrors.append("Scenario [")
                        .append(pScenario.getName())
                        .append("] FAILED. Status=")
                        .append(pScenario.getStatus().name())
                        .append("\n");

                try
                {
                    String iSafeName = iTestCaseID + "_" + pScenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                    String iShotPath = CommonFunctions.takeScreenshot(iSafeName);
                    iLastScreenshotPath = iShotPath;

                    if (iDocument != null && !iDocPath.isEmpty())
                    {
                        CommonFunctions.addScreenshotToReport(iDocument, iDocPath, iTestCaseID);
                    }
                }
                catch (Exception iShotException)
                {
                    CommonFunctions.log.severe("Screenshot failed : " + iShotException.getMessage());
                }
            }
            else
            {
                CommonFunctions.log.info("---------- Scenario PASSED : " + pScenario.getName() + " ----------");
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("After hook error : " + iException.getMessage());
        }
    }

    @AfterAll
    public static void afterAllExecution()
    {
        // Publish failure details as system properties for TestRunner -> ReportManager pickup
        try
        {
            if (iAccumulatedErrors.length() > 0)
            {
                System.setProperty("lastFailureReason." + iTestCaseID, iAccumulatedErrors.toString().trim());
            }
            if (!iLastScreenshotPath.isEmpty())
            {
                System.setProperty("lastScreenshotPath." + iTestCaseID, iLastScreenshotPath);
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Failed to publish failure properties : " + iException.getMessage());
        }

        try
        {
            if (iDocument != null && !iDocPath.isEmpty())
            {
                CommonFunctions.finalizeWordReport(iDocument, iDocPath);
            }
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Word report finalization failed : " + iException.getMessage());
        }

        try
        {
            CommonFunctions.closeBrowser();
        }
        catch (Exception iException)
        {
            CommonFunctions.log.severe("Browser close failed : " + iException.getMessage());
        }

        CommonFunctions.log.info("========== EXECUTION END : " + iTestCaseID + " ==========");
    }
}