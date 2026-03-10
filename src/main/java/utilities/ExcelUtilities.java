package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtilities
{
    private final String iFilePath;
    private final DataFormatter iDataFormatter = new DataFormatter();

    public static Map<String, String> iCurrentTestDataRow = new HashMap<>();

    // ***************************************************************************************************************************************************************************************
    // Function Name : ExcelUtilities
    // Description   : Constructor used to initialize excel file path
    // Parameters    : pFilePath (String) - excel file path
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public ExcelUtilities(String pFilePath)
    {
        this.iFilePath = pFilePath;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRowCount
    // Description   : Returns last data row count from the given sheet
    // Parameters    : pSheetName (String) - sheet name
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    public int getRowCount(String pSheetName)
    {
        try (FileInputStream iInputStream = new FileInputStream(iFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : " + pSheetName);
            }

            return iSheet.getLastRowNum();
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to get row count from sheet : " + pSheetName + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCellValue
    // Description   : Returns cell value using row number and column name
    // Parameters    : pSheetName (String) - sheet name
    //                 pRowNumber (int) - row number
    //                 pColumnName (String) - column header
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    public String getCellValue(String pSheetName, int pRowNumber, String pColumnName)
    {
        try (FileInputStream iInputStream = new FileInputStream(iFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : " + pSheetName);
            }

            Row iHeaderRow = iSheet.getRow(0);
            int iColumnIndex = getColumnIndex(iHeaderRow, pColumnName);

            if (iColumnIndex == -1)
            {
                throw new RuntimeException("Column not found : " + pColumnName + " in sheet : " + pSheetName);
            }

            Row iRow = iSheet.getRow(pRowNumber);

            if (iRow == null)
            {
                return "";
            }

            Cell iCell = iRow.getCell(iColumnIndex);

            return iCell == null ? "" : iDataFormatter.formatCellValue(iCell).trim();
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to read cell value from sheet : " + pSheetName + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : setCellValue
    // Description   : Updates cell value using row number and column name
    // Parameters    : pSheetName (String) - sheet name
    //                 pRowNumber (int) - row number
    //                 pColumnName (String) - column header
    //                 pValue (String) - value to update
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    public void setCellValue(String pSheetName, int pRowNumber, String pColumnName, String pValue)
    {
        try (FileInputStream iInputStream = new FileInputStream(iFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : " + pSheetName);
            }

            Row iHeaderRow = iSheet.getRow(0);
            int iColumnIndex = getColumnIndex(iHeaderRow, pColumnName);

            if (iColumnIndex == -1)
            {
                throw new RuntimeException("Column not found : " + pColumnName + " in sheet : " + pSheetName);
            }

            Row iRow = iSheet.getRow(pRowNumber);
            if (iRow == null)
            {
                iRow = iSheet.createRow(pRowNumber);
            }

            Cell iCell = iRow.getCell(iColumnIndex);
            if (iCell == null)
            {
                iCell = iRow.createCell(iColumnIndex);
            }

            iCell.setCellValue(pValue);

            try (FileOutputStream iOutputStream = new FileOutputStream(iFilePath))
            {
                iWorkbook.write(iOutputStream);
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to update cell value in sheet : " + pSheetName + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : findRow
    // Description   : Finds row number where column value matches expected value
    // Parameters    : pSheetName (String) - sheet name
    //                 pColumnName (String) - column name
    //                 pExpectedValue (String) - value to search
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    public int findRow(String pSheetName, String pColumnName, String pExpectedValue)
    {
        try (FileInputStream iInputStream = new FileInputStream(iFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : " + pSheetName);
            }

            Row iHeaderRow = iSheet.getRow(0);
            int iColumnIndex = getColumnIndex(iHeaderRow, pColumnName);

            if (iColumnIndex == -1)
            {
                throw new RuntimeException("Column not found : " + pColumnName + " in sheet : " + pSheetName);
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iRow = iSheet.getRow(iRowIndex);

                if (iRow == null)
                {
                    continue;
                }

                Cell iCell = iRow.getCell(iColumnIndex);
                String iActualValue = iCell == null ? "" : iDataFormatter.formatCellValue(iCell).trim();

                if (iActualValue.equalsIgnoreCase(pExpectedValue.trim()))
                {
                    return iRowIndex;
                }
            }

            return -1;
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to find row in sheet : " + pSheetName + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadCurrentTestDataRow
    // Description   : Loads complete row from Data sheet using TestCase_ID
    // Parameters    : pSheetName (String) - usually Data
    //                 pTestCaseID (String) - testcase id
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    public void loadCurrentTestDataRow(String pSheetName, String pTestCaseID)
    {
        iCurrentTestDataRow.clear();

        try (FileInputStream iInputStream = new FileInputStream(iFilePath);
             Workbook iWorkbook = WorkbookFactory.create(iInputStream))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : " + pSheetName);
            }

            Row iHeaderRow = iSheet.getRow(0);
            int iTestCaseColumnIndex = getColumnIndex(iHeaderRow, "TestCase_ID");

            if (iTestCaseColumnIndex == -1)
            {
                throw new RuntimeException("Column TestCase_ID not found in sheet : " + pSheetName);
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iRow = iSheet.getRow(iRowIndex);

                if (iRow == null)
                {
                    continue;
                }

                String iCurrentTestCaseID = iDataFormatter.formatCellValue(iRow.getCell(iTestCaseColumnIndex)).trim();

                if (iCurrentTestCaseID.equalsIgnoreCase(pTestCaseID.trim()))
                {
                    for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                    {
                        String iHeader = iDataFormatter.formatCellValue(iHeaderRow.getCell(iColumnIndex)).trim();
                        String iValue = iRow.getCell(iColumnIndex) == null ? "" : iDataFormatter.formatCellValue(iRow.getCell(iColumnIndex)).trim();

                        if (!iHeader.isEmpty())
                        {
                            iCurrentTestDataRow.put(iHeader, iValue);
                        }
                    }
                    return;
                }
            }

            throw new RuntimeException("No test data row found for TestCase_ID : " + pTestCaseID);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to load current test data row | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTestDataValue
    // Description   : Returns current test data value by column name
    // Parameters    : pColumnName (String) - column name
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentTestDataValue(String pColumnName)
    {
        if (iCurrentTestDataRow.isEmpty())
        {
            throw new RuntimeException("Current test data row is not loaded.");
        }

        if (!iCurrentTestDataRow.containsKey(pColumnName))
        {
            throw new RuntimeException("Column not found in current test data row : " + pColumnName);
        }

        return iCurrentTestDataRow.get(pColumnName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getColumnIndex
    // Description   : Returns column index based on header name
    // Parameters    : pHeaderRow (Row) - header row
    //                 pColumnName (String) - column name
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // ***************************************************************************************************************************************************************************************
    private int getColumnIndex(Row pHeaderRow, String pColumnName)
    {
        for (int iColumnIndex = 0; iColumnIndex < pHeaderRow.getLastCellNum(); iColumnIndex++)
        {
            Cell iCell = pHeaderRow.getCell(iColumnIndex);
            String iHeader = iCell == null ? "" : iDataFormatter.formatCellValue(iCell).trim();

            if (iHeader.equalsIgnoreCase(pColumnName.trim()))
            {
                return iColumnIndex;
            }
        }

        return -1;
    }
}