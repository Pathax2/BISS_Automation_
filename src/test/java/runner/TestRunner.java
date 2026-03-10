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
    // Description   : Reads execution control, loads current test data row, resolves URL from Config using Environment, and executes feature
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    @Test
    public void executeSelectedTestCases()
    {
        try
        {
            List<Map<String, String>> iExecutionRows = ExcelUtilities.readExecutionControlSheet(iControlFilePath);

            if (iExecutionRows.isEmpty())
            {
                throw new RuntimeException("No rows found in ExecutionControl.xlsx");
            }

            for (Map<String, String> iRowData : iExecutionRows)
            {
                String iExecutionFlag = iRowData.getOrDefault("Execution", "").trim();
                String iTestCaseID = iRowData.getOrDefault("TestCase_ID", "").trim();
                String iEnvironment = iRowData.getOrDefault("Environment", "").trim();

                if (!iExecutionFlag.equalsIgnoreCase("Y"))
                {
                    continue;
                }

                if (iTestCaseID.isEmpty())
                {
                    throw new RuntimeException("TestCase_ID is blank in Execution Control row");
                }

                if (iEnvironment.isEmpty())
                {
                    throw new RuntimeException("Environment is blank in Execution Control row for TestCase_ID : " + iTestCaseID);
                }

                String iUrl = ExcelUtilities.getUrlByEnvironment(iTestDataFilePath, iEnvironment);
                ExcelUtilities.loadCurrentTestDataRow(iTestDataFilePath, iTestCaseID);

                File iFeatureFile = ExcelUtilities.findFeatureFile(iFeatureDirectoryPath, iTestCaseID);

                if (iFeatureFile == null)
                {
                    throw new RuntimeException("No feature file found for TestCase_ID : " + iTestCaseID);
                }

                System.setProperty("testcase", iTestCaseID);
                System.setProperty("url", iUrl);
                System.setProperty("env", iEnvironment);

                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("[RUNNING] TestCase_ID : " + iTestCaseID);
                System.out.println("[INFO] Environment : " + iEnvironment);
                System.out.println("[INFO] URL         : " + iUrl);
                System.out.println("[INFO] Feature     : " + iFeatureFile.getAbsolutePath());

                byte iExitStatus = Main.run(
                        new String[]
                                {
                                        iFeatureFile.getAbsolutePath(),
                                        "--glue", "stepdefinitions",
                                        "--plugin", "pretty",
                                        "--plugin", "summary",
                                        "--plugin", "html:target/cucumber-reports/" + iTestCaseID + ".html",
                                        "--plugin", "json:target/cucumber-json/" + iTestCaseID + ".json"
                                },
                        Thread.currentThread().getContextClassLoader()
                );

                if (iExitStatus != 0)
                {
                    throw new RuntimeException("Execution failed for TestCase_ID : " + iTestCaseID);
                }

                break;
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Execution failed in TestRunner : " + iException.getMessage(), iException);
        }
    }
}