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
    // ***************************************************************************************************************************************************************************************
    // Variable Name : iCurrentTestDataRow
    // Description   : Stores currently selected test case row data from Data sheet so that step definitions can use it directly
    // ***************************************************************************************************************************************************************************************
    public static Map<String, String> iCurrentTestDataRow = new HashMap<>();

    // ***************************************************************************************************************************************************************************************
    // Variable Name : iDataFormatter
    // Description   : Apache POI formatter used to safely read Excel cell values as String
    // ***************************************************************************************************************************************************************************************
    private static final DataFormatter iDataFormatter = new DataFormatter();

    // ***************************************************************************************************************************************************************************************
    // Function Name : readExecutionControlSheet
    // Description   : Reads ExecutionControl.xlsx first sheet and stores each data row in key-value format using header names
    // Parameters    : pControlFilePath (String) - full path of ExecutionControl.xlsx
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Excel file should exist and first row should contain valid headers
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static List<Map<String, String>> readExecutionControlSheet(String pControlFilePath)
    {
        List<Map<String, String>> iExecutionRows = new ArrayList<>();

        try (FileInputStream iFileInputStream = new FileInputStream(pControlFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
        {
            // Taking first sheet because ExecutionControl workbook usually keeps control data in the first sheet
            Sheet iSheet = iWorkbook.getSheetAt(0);

            if (iSheet == null)
            {
                throw new RuntimeException("Execution Control sheet is not present in file : " + pControlFilePath);
            }

            Row iHeaderRow = iSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row is missing in Execution Control sheet.");
            }

            // Loop through all rows after header
            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iCurrentRow = iSheet.getRow(iRowIndex);

                // Skip null rows
                if (iCurrentRow == null)
                {
                    continue;
                }

                // Skip completely blank rows
                if (isRowCompletelyBlank(iCurrentRow, iHeaderRow.getLastCellNum()))
                {
                    continue;
                }

                Map<String, String> iRowData = new HashMap<>();

                // Read each column using header as key
                for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                {
                    String iHeaderName = getCellValue(iHeaderRow, iColumnIndex).trim();
                    String iCellValue = getCellValue(iCurrentRow, iColumnIndex).trim();

                    // Only store if header is not blank
                    if (!iHeaderName.isEmpty())
                    {
                        iRowData.put(iHeaderName, iCellValue);
                    }
                }

                iExecutionRows.add(iRowData);
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to read ExecutionControl.xlsx. Reason : " + iException.getMessage(), iException);
        }

        return iExecutionRows;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getUrlByEnvironment
    // Description   : Reads Config sheet from TestData.xlsx and returns the URL matching the given environment value
    // Parameters    : pTestDataFilePath (String) - full path of TestData.xlsx
    //                 pEnvironment (String) - environment value from ExecutionControl sheet such as QA, UAT, PROD
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Config sheet should contain Env and URL columns
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getUrlByEnvironment(String pTestDataFilePath, String pEnvironment)
    {
        try (FileInputStream iFileInputStream = new FileInputStream(pTestDataFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
        {
            Sheet iConfigSheet = iWorkbook.getSheet("Config");

            if (iConfigSheet == null)
            {
                throw new RuntimeException("Config sheet not found in TestData.xlsx");
            }

            Row iHeaderRow = iConfigSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row is missing in Config sheet.");
            }

            int iEnvColumnIndex = getColumnIndex(iHeaderRow, "Env");
            int iUrlColumnIndex = getColumnIndex(iHeaderRow, "URL");

            if (iEnvColumnIndex == -1)
            {
                throw new RuntimeException("Env column not found in Config sheet.");
            }

            if (iUrlColumnIndex == -1)
            {
                throw new RuntimeException("URL column not found in Config sheet.");
            }

            // Loop through all config rows and find matching environment
            for (int iRowIndex = 1; iRowIndex <= iConfigSheet.getLastRowNum(); iRowIndex++)
            {
                Row iCurrentRow = iConfigSheet.getRow(iRowIndex);

                if (iCurrentRow == null)
                {
                    continue;
                }

                if (isRowCompletelyBlank(iCurrentRow, iHeaderRow.getLastCellNum()))
                {
                    continue;
                }

                String iExcelEnvironment = getCellValue(iCurrentRow, iEnvColumnIndex).trim();
                String iExcelUrl = getCellValue(iCurrentRow, iUrlColumnIndex).trim();

                if (iExcelEnvironment.equalsIgnoreCase(pEnvironment.trim()))
                {
                    if (iExcelUrl.isEmpty())
                    {
                        throw new RuntimeException("URL is blank in Config sheet for environment : " + pEnvironment);
                    }

                    return iExcelUrl;
                }
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to fetch URL for environment : " + pEnvironment + ". Reason : " + iException.getMessage(), iException);
        }

        throw new RuntimeException("No matching environment found in Config sheet for : " + pEnvironment);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadCurrentTestDataRow
    // Description   : Loads one matching row from Data sheet based on TestCase_ID and stores it in memory for current execution
    // Parameters    : pTestDataFilePath (String) - full path of TestData.xlsx
    //                 pTestCaseID (String) - test case id selected from execution control sheet
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Data sheet should contain TestCase_ID column
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void loadCurrentTestDataRow(String pTestDataFilePath, String pTestCaseID)
    {
        // Clear old row data before loading fresh row
        iCurrentTestDataRow.clear();

        try (FileInputStream iFileInputStream = new FileInputStream(pTestDataFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
        {
            Sheet iDataSheet = iWorkbook.getSheet("Data");

            if (iDataSheet == null)
            {
                throw new RuntimeException("Data sheet not found in TestData.xlsx");
            }

            Row iHeaderRow = iDataSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row is missing in Data sheet.");
            }

            int iTestCaseIdColumnIndex = getColumnIndex(iHeaderRow, "TestCase_ID");

            if (iTestCaseIdColumnIndex == -1)
            {
                throw new RuntimeException("TestCase_ID column not found in Data sheet.");
            }

            // Loop through all rows and match TestCase_ID
            for (int iRowIndex = 1; iRowIndex <= iDataSheet.getLastRowNum(); iRowIndex++)
            {
                Row iCurrentRow = iDataSheet.getRow(iRowIndex);

                if (iCurrentRow == null)
                {
                    continue;
                }

                if (isRowCompletelyBlank(iCurrentRow, iHeaderRow.getLastCellNum()))
                {
                    continue;
                }

                String iExcelTestCaseID = getCellValue(iCurrentRow, iTestCaseIdColumnIndex).trim();

                if (iExcelTestCaseID.equalsIgnoreCase(pTestCaseID.trim()))
                {
                    // Store the complete row using header names
                    for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                    {
                        String iHeaderName = getCellValue(iHeaderRow, iColumnIndex).trim();
                        String iCellValue = getCellValue(iCurrentRow, iColumnIndex).trim();

                        if (!iHeaderName.isEmpty())
                        {
                            iCurrentTestDataRow.put(iHeaderName, iCellValue);
                        }
                    }

                    return;
                }
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to load test data row for TestCase_ID : " + pTestCaseID + ". Reason : " + iException.getMessage(), iException);
        }

        throw new RuntimeException("No matching row found in Data sheet for TestCase_ID : " + pTestCaseID);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTestDataValue
    // Description   : Returns a value from the currently loaded test data row using the column name
    // Parameters    : pColumnName (String) - column name from Data sheet, for example Username, Password, FirstName etc.
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : loadCurrentTestDataRow must be executed before calling this method
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentTestDataValue(String pColumnName)
    {
        if (iCurrentTestDataRow == null || iCurrentTestDataRow.isEmpty())
        {
            throw new RuntimeException("Current test data row is not loaded. Please load test data before fetching values.");
        }

        if (!iCurrentTestDataRow.containsKey(pColumnName))
        {
            throw new RuntimeException("Column name not found in current test data row : " + pColumnName);
        }

        String iValue = iCurrentTestDataRow.get(pColumnName);

        return iValue == null ? "" : iValue.trim();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTestDataMap
    // Description   : Returns the complete currently loaded test data row map
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : loadCurrentTestDataRow must be executed before calling this method
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static Map<String, String> getCurrentTestDataMap()
    {
        if (iCurrentTestDataRow == null || iCurrentTestDataRow.isEmpty())
        {
            throw new RuntimeException("Current test data row is not loaded.");
        }

        return new HashMap<>(iCurrentTestDataRow);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : findFeatureFile
    // Description   : Finds the matching feature file from the given folder using TestCase_ID in file name
    // Parameters    : pFeatureDirectoryPath (String) - path where feature files are available
    //                 pTestCaseID (String) - current test case id for example TC_01_Login
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Feature folder should exist and feature file naming should include TestCase_ID
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static File findFeatureFile(String pFeatureDirectoryPath, String pTestCaseID)
    {
        File iFeatureDirectory = new File(pFeatureDirectoryPath);

        if (!iFeatureDirectory.exists())
        {
            throw new RuntimeException("Feature directory does not exist : " + pFeatureDirectoryPath);
        }

        if (!iFeatureDirectory.isDirectory())
        {
            throw new RuntimeException("Provided feature path is not a directory : " + pFeatureDirectoryPath);
        }

        File[] iFeatureFiles = iFeatureDirectory.listFiles((iDirectory, iFileName) ->
                iFileName.toLowerCase().endsWith(".feature")
                        && iFileName.toLowerCase().contains(pTestCaseID.toLowerCase())
        );

        if (iFeatureFiles == null || iFeatureFiles.length == 0)
        {
            return null;
        }

        if (iFeatureFiles.length > 1)
        {
            throw new RuntimeException("Multiple feature files found for TestCase_ID : " + pTestCaseID + ". Please keep only one matching feature file.");
        }

        return iFeatureFiles[0];
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getColumnIndex
    // Description   : Finds and returns the column index by header name from the given header row
    // Parameters    : pHeaderRow (Row) - header row object
    //                 pColumnName (String) - column name to search
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Header row should not be null
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static int getColumnIndex(Row pHeaderRow, String pColumnName)
    {
        for (int iColumnIndex = 0; iColumnIndex < pHeaderRow.getLastCellNum(); iColumnIndex++)
        {
            String iHeaderName = getCellValue(pHeaderRow, iColumnIndex).trim();

            if (iHeaderName.equalsIgnoreCase(pColumnName.trim()))
            {
                return iColumnIndex;
            }
        }

        return -1;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCellValue
    // Description   : Safely returns cell value as String from given row and column index
    // Parameters    : pRow (Row) - Excel row object
    //                 pColumnIndex (int) - target column index
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Row object can be null, method handles safely
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String getCellValue(Row pRow, int pColumnIndex)
    {
        if (pRow == null)
        {
            return "";
        }

        if (pRow.getCell(pColumnIndex) == null)
        {
            return "";
        }

        return iDataFormatter.formatCellValue(pRow.getCell(pColumnIndex));
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : isRowCompletelyBlank
    // Description   : Checks whether the complete row is blank based on all expected columns
    // Parameters    : pRow (Row) - current row to validate
    //                 pTotalColumns (int) - number of columns to check
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : None
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static boolean isRowCompletelyBlank(Row pRow, int pTotalColumns)
    {
        if (pRow == null)
        {
            return true;
        }

        for (int iColumnIndex = 0; iColumnIndex < pTotalColumns; iColumnIndex++)
        {
            String iCellValue = getCellValue(pRow, iColumnIndex).trim();

            if (!iCellValue.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}