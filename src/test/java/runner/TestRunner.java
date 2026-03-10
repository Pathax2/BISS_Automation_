package runner;

import io.cucumber.core.cli.Main;
import org.junit.jupiter.api.Test;
import utilities.ExcelUtilities;

public class TestRunner
{
    public static final String iExecutionControlFilePath = "src/test/resources/Execution_Control_File/ExecutionControl.xlsx";
    public static final String iExecutionControlSheetName = "Sheet1";
    public static final String iFeatureDirectoryPath = "src/test/resources/Test_Cases/";

    // ***************************************************************************************************************************************************************************************
    // Function Name : executeSelectedTestCases
    // Description   : Reads execution control file, executes all rows where Execution = Y, passes values to hooks
    //                 and updates status column as PASS or FAIL
    // Parameters    : None
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    @Test
    public void executeSelectedTestCases()
    {
        runFrameworkExecution();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : main
    // Description   : Main method added for direct execution support
    // Parameters    : pArgs (String[]) - command line arguments
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    public static void main(String[] pArgs)
    {
        runFrameworkExecution();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : runFrameworkExecution
    // Description   : Common execution method used by both JUnit and main method
    // Parameters    : None
    // Author        : Aniket Pathare | Aniket.Pathare@agriculture.gov.ie
    // ***************************************************************************************************************************************************************************************
    private static void runFrameworkExecution()
    {
        ExcelUtilities iExecutionExcel = new ExcelUtilities(iExecutionControlFilePath);
        int iRowCount = iExecutionExcel.getRowCount(iExecutionControlSheetName);
        boolean iAnyExecutionFound = false;

        for (int iRowNumber = 1; iRowNumber <= iRowCount; iRowNumber++)
        {
            try
            {
                String iExecutionFlag = iExecutionExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Execution").trim();

                if (!iExecutionFlag.equalsIgnoreCase("Y"))
                {
                    continue;
                }

                iAnyExecutionFound = true;

                String iTestCaseID = iExecutionExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "TestCase_ID").trim();
                System.out.println(iTestCaseID);
                String iEnvironment = iExecutionExcel.getCellValue(iExecutionControlSheetName, iRowNumber, "Environment").trim();

                if (iTestCaseID.isEmpty())
                {
                    throw new RuntimeException("TestCase_ID is blank for row number : " + iRowNumber);
                }

                if (iEnvironment.isEmpty())
                {
                    throw new RuntimeException("Environment is blank for TestCase_ID : " + iTestCaseID);
                }

                System.setProperty("testcase", iTestCaseID);
                System.setProperty("environment", iEnvironment);

                String iFeaturePath = iFeatureDirectoryPath + iTestCaseID + ".feature";

                System.out.println("======================================================================");
                System.out.println("Starting Execution");
                System.out.println("TestCase_ID : " + iTestCaseID);
                System.out.println("Environment : " + iEnvironment);
                System.out.println("FeaturePath : " + iFeaturePath);
                System.out.println("======================================================================");

                byte iExecutionStatus = Main.run(
                        new String[]{
                                iFeaturePath,
                                "--glue", "stepdefinitions",
                                "--plugin", "pretty",
                                "--plugin", "summary",
                                "--plugin", "html:target/cucumber-reports/" + iTestCaseID + ".html",
                                "--plugin", "json:target/cucumber-json/" + iTestCaseID + ".json"
                        },
                        Thread.currentThread().getContextClassLoader()
                );

                if (iExecutionStatus == 0)
                {
                    iExecutionExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "PASS");
                }
                else
                {
                    iExecutionExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "FAIL");
                }
            }
            catch (Exception iException)
            {
                try
                {
                    iExecutionExcel.setCellValue(iExecutionControlSheetName, iRowNumber, "Status", "FAIL");
                }
                catch (Exception ignored)
                {
                }

                throw new RuntimeException("Execution failed at row number : " + iRowNumber + " | Reason : " + iException.getMessage(), iException);
            }
        }

        if (!iAnyExecutionFound)
        {
            throw new RuntimeException("No rows marked as Y in ExecutionControl.xlsx");
        }
    }
}
