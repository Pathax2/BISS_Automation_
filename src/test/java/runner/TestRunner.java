package runner;

import io.cucumber.core.cli.Main;
import org.junit.jupiter.api.Test;
import utilities.ExcelUtilities;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestRunner
{
    // ***************************************************************************************************************************************************************************************
    // Variable Name  : iControlFilePath
    // Description    : Path of Execution Control Excel file
    // ***************************************************************************************************************************************************************************************
    private static final String iControlFilePath = "src/test/resources/Execution_Control_File/ExecutionControl.xlsx";

    // ***************************************************************************************************************************************************************************************
    // Variable Name  : iTestDataFilePath
    // Description    : Path of Test Data Excel file
    // ***************************************************************************************************************************************************************************************
    private static final String iTestDataFilePath = "src/test/resources/Test_Data/TestData.xlsx";

    // ***************************************************************************************************************************************************************************************
    // Variable Name  : iFeatureDirectoryPath
    // Description    : Path where all feature files are stored
    // ***************************************************************************************************************************************************************************************
    private static final String iFeatureDirectoryPath = "src/test/resources/Test_Cases";

    // ***************************************************************************************************************************************************************************************
    // Function Name : executeSelectedTestCases
    // Description   : Reads ExecutionControl.xlsx, checks which test case is marked as Y in Execution column,
    //                 fetches Environment, gets matching URL from Config sheet, loads that test case data row,
    //                 finds corresponding feature file, and runs the scenario through Cucumber.
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : ExecutionControl.xlsx, TestData.xlsx, and feature files should exist in expected path
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    @Test
    public void executeSelectedTestCases()
    {
        try
        {
            // Step 1: Read all rows from Execution Control sheet
            List<Map<String, String>> iExecutionRows = ExcelUtilities.readExecutionControlSheet(iControlFilePath);

            // Step 2: Validate that at least one row is present
            if (iExecutionRows == null || iExecutionRows.isEmpty())
            {
                throw new RuntimeException("No rows found in ExecutionControl.xlsx");
            }

            boolean iIsAnyTestCaseExecuted = false;

            // Step 3: Loop through each row from Execution Control Excel
            for (Map<String, String> iRowData : iExecutionRows)
            {
                // These column names are based on your Excel structure
                String iExecutionFlag = iRowData.getOrDefault("Execution", "").trim();
                String iTestCaseID = iRowData.getOrDefault("TestCase_ID", "").trim();
                String iEnvironment = iRowData.getOrDefault("Environment", "").trim();

                // Step 4: Skip rows which are not marked as Y
                if (!iExecutionFlag.equalsIgnoreCase("Y"))
                {
                    continue;
                }

                // Step 5: Basic validations
                if (iTestCaseID.isEmpty())
                {
                    throw new RuntimeException("TestCase_ID is blank in Execution Control sheet for one of the selected rows.");
                }

                if (iEnvironment.isEmpty())
                {
                    throw new RuntimeException("Environment is blank in Execution Control sheet for TestCase_ID : " + iTestCaseID);
                }

                // Step 6: Get application URL from Config sheet using Environment
                String iUrl = ExcelUtilities.getUrlByEnvironment(iTestDataFilePath, iEnvironment);

                if (iUrl == null || iUrl.trim().isEmpty())
                {
                    throw new RuntimeException("URL is blank for Environment : " + iEnvironment);
                }

                // Step 7: Load current row from Data sheet based on TestCase_ID
                // After loading, other classes can directly fetch values like Username, Password etc.
                ExcelUtilities.loadCurrentTestDataRow(iTestDataFilePath, iTestCaseID);

                // Step 8: Find matching feature file from Test_Cases folder
                File iFeatureFile = ExcelUtilities.findFeatureFile(iFeatureDirectoryPath, iTestCaseID);

                if (iFeatureFile == null)
                {
                    throw new RuntimeException("No feature file found for TestCase_ID : " + iTestCaseID);
                }

                // Step 9: Store runtime values in system properties so Hooks / Step Definitions can use them
                System.setProperty("testcase", iTestCaseID);
                System.setProperty("url", iUrl);
                System.setProperty("env", iEnvironment);

                // Optional model property if later you want to drive model from Excel
                // For now keeping it blank safely
                System.setProperty("model", iRowData.getOrDefault("Model", "").trim());

                // Step 10: Print execution summary in console
                System.out.println("==================================================================================================================");
                System.out.println("STARTING EXECUTION");
                System.out.println("TestCase_ID  : " + iTestCaseID);
                System.out.println("Environment  : " + iEnvironment);
                System.out.println("URL          : " + iUrl);
                System.out.println("Feature File : " + iFeatureFile.getAbsolutePath());
                System.out.println("==================================================================================================================");

                // Step 11: Run cucumber for the matched feature file
                byte iExecutionStatus = Main.run(
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

                // Step 12: If cucumber returns non-zero, execution failed
                if (iExecutionStatus != 0)
                {
                    throw new RuntimeException("Execution failed for TestCase_ID : " + iTestCaseID);
                }

                iIsAnyTestCaseExecuted = true;

                // Step 13:
                // Currently stopping after first Y row.
                // This keeps it simple and matches your present execution style.
                // If later you want multiple Y rows to execute in one run, remove the break.
                break;
            }

            // Step 14: If no Y found in Execution column, fail clearly
            if (!iIsAnyTestCaseExecuted)
            {
                throw new RuntimeException("No test case is marked as Y in ExecutionControl.xlsx");
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Execution failed in TestRunner : " + iException.getMessage(), iException);
        }
    }
}