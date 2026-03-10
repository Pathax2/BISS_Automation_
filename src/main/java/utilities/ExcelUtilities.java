package utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtilities
{
    public static Map<String, String> iCurrentTestDataRow = new HashMap<>();

    // ***************************************************************************************************************************************************************************************
    // Function Name : readExecutionControlSheet
    // Description   : Reads ExecutionControl.xlsx and stores each row as key-value data using header names
    // Parameters    : iControlFilePath (String) - full path of ExecutionControl.xlsx
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static List<Map<String, String>> readExecutionControlSheet(String iControlFilePath)
    {
        List<Map<String, String>> iExecutionRows = new ArrayList<>();
        DataFormatter iDataFormatter = new DataFormatter();

        try (FileInputStream iFileInputStream = new FileInputStream(iControlFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
        {
            Sheet iSheet = iWorkbook.getSheetAt(0);
            Row iHeaderRow = iSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row is missing in ExecutionControl.xlsx");
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iCurrentRow = iSheet.getRow(iRowIndex);

                if (iCurrentRow == null)
                {
                    continue;
                }

                Map<String, String> iRowData = new HashMap<>();

                for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                {
                    String iColumnName = iDataFormatter.formatCellValue(iHeaderRow.getCell(iColumnIndex)).trim();
                    String iColumnValue = iDataFormatter.formatCellValue(iCurrentRow.getCell(iColumnIndex)).trim();
                    iRowData.put(iColumnName, iColumnValue);
                }

                iExecutionRows.add(iRowData);
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to read ExecutionControl.xlsx : " + iException.getMessage(), iException);
        }

        return iExecutionRows;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getUrlByEnvironment
    // Description   : Reads Config sheet from TestData.xlsx and returns URL for the supplied environment
    // Parameters    : iTestDataFilePath (String) - full path of TestData.xlsx
    //                 iEnvironment (String) - environment value from ExecutionControl sheet like QA/UAT
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getUrlByEnvironment(String iTestDataFilePath, String iEnvironment)
    {
        DataFormatter iDataFormatter = new DataFormatter();

        try (FileInputStream iFileInputStream = new FileInputStream(iTestDataFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet("Config");

            if (iSheet == null)
            {
                throw new RuntimeException("Config sheet not found in TestData.xlsx");
            }

            Row iHeaderRow = iSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row missing in Config sheet");
            }

            int iEnvColumnIndex = -1;
            int iUrlColumnIndex = -1;

            for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
            {
                String iColumnName = iDataFormatter.formatCellValue(iHeaderRow.getCell(iColumnIndex)).trim();

                if (iColumnName.equalsIgnoreCase("Env"))
                {
                    iEnvColumnIndex = iColumnIndex;
                }
                else if (iColumnName.equalsIgnoreCase("URL"))
                {
                    iUrlColumnIndex = iColumnIndex;
                }
            }

            if (iEnvColumnIndex == -1 || iUrlColumnIndex == -1)
            {
                throw new RuntimeException("Env or URL column not found in Config sheet");
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iCurrentRow = iSheet.getRow(iRowIndex);

                if (iCurrentRow == null)
                {
                    continue;
                }

                String iExcelEnvironment = iDataFormatter.formatCellValue(iCurrentRow.getCell(iEnvColumnIndex)).trim();
                String iUrl = iDataFormatter.formatCellValue(iCurrentRow.getCell(iUrlColumnIndex)).trim();

                if (iExcelEnvironment.equalsIgnoreCase(iEnvironment.trim()))
                {
                    if (iUrl.isEmpty())
                    {
                        throw new RuntimeException("URL is blank in Config sheet for environment : " + iEnvironment);
                    }
                    return iUrl;
                }
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to read URL from Config sheet for environment : " + iEnvironment + " | Reason : " + iException.getMessage(), iException);
        }

        throw new RuntimeException("No matching environment found in Config sheet for : " + iEnvironment);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadCurrentTestDataRow
    // Description   : Loads the matching row from Data sheet based on TestCase_ID and stores it for current execution
    // Parameters    : iTestDataFilePath (String) - full path of TestData.xlsx
    //                 iTestCaseID (String) - current TestCase_ID selected from Execution Control
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void loadCurrentTestDataRow(String iTestDataFilePath, String iTestCaseID)
    {
        iCurrentTestDataRow.clear();

        DataFormatter iDataFormatter = new DataFormatter();

        try (FileInputStream iFileInputStream = new FileInputStream(iTestDataFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet("Data");

            if (iSheet == null)
            {
                throw new RuntimeException("Data sheet not found in TestData.xlsx");
            }

            Row iHeaderRow = iSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row missing in Data sheet");
            }

            int iTestCaseIDColumnIndex = -1;

            for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
            {
                String iColumnName = iDataFormatter.formatCellValue(iHeaderRow.getCell(iColumnIndex)).trim();

                if (iColumnName.equalsIgnoreCase("TestCase_ID"))
                {
                    iTestCaseIDColumnIndex = iColumnIndex;
                    break;
                }
            }

            if (iTestCaseIDColumnIndex == -1)
            {
                throw new RuntimeException("TestCase_ID column not found in Data sheet");
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iCurrentRow = iSheet.getRow(iRowIndex);

                if (iCurrentRow == null)
                {
                    continue;
                }

                String iExcelTestCaseID = iDataFormatter.formatCellValue(iCurrentRow.getCell(iTestCaseIDColumnIndex)).trim();

                if (iExcelTestCaseID.equalsIgnoreCase(iTestCaseID.trim()))
                {
                    for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                    {
                        String iColumnName = iDataFormatter.formatCellValue(iHeaderRow.getCell(iColumnIndex)).trim();
                        String iColumnValue = iDataFormatter.formatCellValue(iCurrentRow.getCell(iColumnIndex)).trim();
                        iCurrentTestDataRow.put(iColumnName, iColumnValue);
                    }
                    return;
                }
            }

            throw new RuntimeException("No matching TestData row found for TestCase_ID : " + iTestCaseID);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to load current TestData row for TestCase_ID : " + iTestCaseID + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTestDataValue
    // Description   : Returns the value from current loaded TestData row based on column name
    // Parameters    : iColumnName (String) - column name from Data sheet
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentTestDataValue(String iColumnName)
    {
        String iValue = iCurrentTestDataRow.get(iColumnName);

        if (iValue == null)
        {
            throw new RuntimeException("Column not found in current TestData row : " + iColumnName);
        }

        return iValue.trim();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : findFeatureFile
    // Description   : Finds the matching feature file based on TestCase_ID from the Test_Cases folder
    // Parameters    : iFeatureDirectoryPath (String) - path of feature directory
    //                 iTestCaseID (String) - test case id to search in feature file names
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static File findFeatureFile(String iFeatureDirectoryPath, String iTestCaseID)
    {
        File iFeatureDirectory = new File(iFeatureDirectoryPath);

        if (!iFeatureDirectory.exists() || !iFeatureDirectory.isDirectory())
        {
            throw new RuntimeException("Feature directory not found : " + iFeatureDirectoryPath);
        }

        File[] iFeatureFiles = iFeatureDirectory.listFiles((iDirectory, iFileName) ->
                iFileName.toLowerCase().contains(iTestCaseID.toLowerCase()) &&
                        iFileName.toLowerCase().endsWith(".feature")
        );

        if (iFeatureFiles == null || iFeatureFiles.length == 0)
        {
            return null;
        }

        if (iFeatureFiles.length > 1)
        {
            throw new RuntimeException("Multiple feature files found for TestCase_ID : " + iTestCaseID);
        }

        return iFeatureFiles[0];
    }
}