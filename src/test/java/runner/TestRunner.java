package runner;

import io.cucumber.core.cli.Main;
import org.junit.jupiter.api.Test;
import utilities.ExcelUtilities;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestRunner
{
    private static final String iControlFilePath = "src/test/resources/Execution_Control_File/ExecutionControl.xlsx";
    private static final String iTestDataFilePath = "src/test/resources/Test_Data/TestData.xlsx";
    private static final String iFeatureDirectoryPath = "src/test/resources/Test_Cases";

    // ***************************************************************************************************************************************************************************************
    // Function Name : executeSelectedTestCases
    // Description   : Reads execution control, loads current test data row based on selected TestCase_ID,
    //                 resolves matching feature files, and executes them one by one using Cucumber CLI
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : ExecutionControl.xlsx, TestData.xlsx, and feature files should be available in expected folders
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    @Test
    public void executeSelectedTestCases()
    {
        try
        {
            List<Map<String, String>> iExecutionRows = ExcelUtilities.readExecutionControlSheet(iControlFilePath);
            Map<String, String> iEnvironmentUrlMap = ExcelUtilities.readEnvironmentConfigSheet(iTestDataFilePath);

            if (iExecutionRows.isEmpty())
            {
                System.out.println("[INFO] No rows found in ExecutionControl.xlsx");
                return;
            }

            for (Map<String, String> iRowData : iExecutionRows)
            {
                String iExecutionFlag = iRowData.getOrDefault("Execution", "").trim();
                String iTestCaseID = iRowData.getOrDefault("TestCase_ID", "").trim();
                String iEnvironment = iRowData.getOrDefault("Environment", "").trim();
                String iModelName = iRowData.getOrDefault("Model", "").trim();

                if (!iExecutionFlag.equalsIgnoreCase("Y"))
                {
                    System.out.println("[SKIPPED] " + iTestCaseID + " because Execution flag is not Y");
                    continue;
                }

                if (iTestCaseID.isEmpty())
                {
                    throw new RuntimeException("TestCase_ID is blank in Execution Control row");
                }

                if (!iEnvironmentUrlMap.containsKey(iEnvironment))
                {
                    throw new RuntimeException("No URL found in Config sheet for environment : " + iEnvironment + " | TestCase : " + iTestCaseID);
                }

                ExcelUtilities.loadCurrentTestDataRow(iTestDataFilePath, iTestCaseID);

                String iUrl = iEnvironmentUrlMap.get(iEnvironment);
                File iFeatureFile = ExcelUtilities.findFeatureFile(iFeatureDirectoryPath, iTestCaseID);

                if (iFeatureFile == null)
                {
                    throw new RuntimeException("No feature file found for TestCase_ID : " + iTestCaseID);
                }

                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("[RUNNING] TestCase_ID : " + iTestCaseID);
                System.out.println("[INFO] Environment : " + iEnvironment);
                System.out.println("[INFO] URL         : " + iUrl);
                System.out.println("[INFO] Model       : " + iModelName);
                System.out.println("[INFO] Feature     : " + iFeatureFile.getAbsolutePath());

                System.setProperty("testcase", iTestCaseID);
                System.setProperty("url", iUrl);
                System.setProperty("env", iEnvironment);
                System.setProperty("model", iModelName);

                byte iExitStatus = Main.run(
                        new String[]
                                {
                                        iFeatureFile.getAbsolutePath(),
                                        "--glue", "stepdefinitions",
                                        "--plugin", "pretty",
                                        "--plugin", "summary",
                                        "--plugin", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                                        "--plugin", "html:target/cucumber-reports/" + iTestCaseID + ".html",
                                        "--plugin", "json:target/cucumber-json/" + iTestCaseID + ".json"
                                },
                        Thread.currentThread().getContextClassLoader()
                );

                if (iExitStatus == 0)
                {
                    System.out.println("[PASS] " + iTestCaseID);
                }
                else
                {
                    throw new RuntimeException("Execution failed for TestCase_ID : " + iTestCaseID);
                }
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Execution failed in TestRunner : " + iException.getMessage(), iException);
        }
    }
}