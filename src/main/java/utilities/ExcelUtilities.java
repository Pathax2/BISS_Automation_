// ===================================================================================================================================
// File          : ExcelUtilities.java
// Package       : utilities
// Description   : Apache POI-based Excel utility for the BISS automation framework.
//                 Provides row reading, cell read/write, row search, and test data loading.
//                 iCurrentTestDataRow is ThreadLocal — safe for parallel Cucumber execution.
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

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
import java.util.logging.Logger;

public class ExcelUtilities
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // Instance fields
    // -------------------------------------------------------------------------------------------------------------------------------
    private final String        iFilePath;
    private final DataFormatter iDataFormatter = new DataFormatter();

    // -------------------------------------------------------------------------------------------------------------------------------
    // Logger
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final Logger log = Logger.getLogger(ExcelUtilities.class.getName());

    // -------------------------------------------------------------------------------------------------------------------------------
    // ThreadLocal test data row — each parallel thread maintains its own isolated copy.
    // Replaces the previous static HashMap which caused data contamination under parallel runs.
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final ThreadLocal<Map<String, String>> iCurrentTestDataRow =
            ThreadLocal.withInitial(HashMap::new);

    // ***************************************************************************************************************************************************************************************
    // Function Name : ExcelUtilities (Constructor)
    // Description   : Initialises the utility with the path to the target Excel file
    // Parameters    : pFilePath (String) - absolute or relative path to the Excel file
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public ExcelUtilities(String pFilePath)
    {
        if (pFilePath == null || pFilePath.trim().isEmpty())
        {
            throw new RuntimeException("ExcelUtilities: file path cannot be blank.");
        }

        this.iFilePath = pFilePath.trim();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRowCount
    // Description   : Returns the index of the last data row in the given sheet (1-based, same as Apache POI getLastRowNum)
    // Parameters    : pSheetName (String) - name of the target sheet
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Sheet must exist in the Excel file
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public int getRowCount(String pSheetName)
    {
        try (FileInputStream iFis  = new FileInputStream(iFilePath);
             Workbook iWorkbook    = WorkbookFactory.create(iFis))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : '" + pSheetName + "' in file : " + iFilePath);
            }

            return iSheet.getLastRowNum();
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to get row count from sheet : '" + pSheetName + "' | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCellValue
    // Description   : Returns cell value as String using row number and column header name
    // Parameters    : pSheetName  (String) - name of the target sheet
    //                 pRowNumber  (int)    - row index (0 = header, 1 = first data row)
    //                 pColumnName (String) - column header text to locate the cell
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Sheet and column must exist; row must be within bounds
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public String getCellValue(String pSheetName, int pRowNumber, String pColumnName)
    {
        try (FileInputStream iFis  = new FileInputStream(iFilePath);
             Workbook iWorkbook    = WorkbookFactory.create(iFis))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : '" + pSheetName + "' in file : " + iFilePath);
            }

            Row iHeaderRow    = iSheet.getRow(0);
            int iColumnIndex  = getColumnIndex(iHeaderRow, pColumnName);

            if (iColumnIndex == -1)
            {
                throw new RuntimeException("Column not found : '" + pColumnName + "' in sheet : '" + pSheetName + "'");
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
            throw new RuntimeException("Unable to read cell from sheet : '" + pSheetName
                    + "' | Column : '" + pColumnName + "' | Row : " + pRowNumber
                    + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : setCellValue
    // Description   : Writes a value to the specified cell identified by row number and column header name.
    //                 Creates the row and/or cell if they do not already exist.
    // Parameters    : pSheetName  (String) - name of the target sheet
    //                 pRowNumber  (int)    - row index
    //                 pColumnName (String) - column header text
    //                 pValue      (String) - value to write
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Sheet must exist; column must be present in the header row
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public void setCellValue(String pSheetName, int pRowNumber, String pColumnName, String pValue)
    {
        try (FileInputStream iFis  = new FileInputStream(iFilePath);
             Workbook iWorkbook    = WorkbookFactory.create(iFis))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : '" + pSheetName + "' in file : " + iFilePath);
            }

            Row iHeaderRow   = iSheet.getRow(0);
            int iColumnIndex = getColumnIndex(iHeaderRow, pColumnName);

            if (iColumnIndex == -1)
            {
                throw new RuntimeException("Column not found : '" + pColumnName + "' in sheet : '" + pSheetName + "'");
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

            try (FileOutputStream iFos = new FileOutputStream(iFilePath))
            {
                iWorkbook.write(iFos);
            }

            log.info("Cell updated | Sheet='" + pSheetName + "' | Column='" + pColumnName
                    + "' | Row=" + pRowNumber + " | Value='" + pValue + "'");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to update cell in sheet : '" + pSheetName
                    + "' | Column : '" + pColumnName + "' | Row : " + pRowNumber
                    + " | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : findRow
    // Description   : Searches a sheet for the first row where the given column value matches the expected value (case-insensitive)
    // Parameters    : pSheetName     (String) - name of the target sheet
    //                 pColumnName    (String) - column header to search within
    //                 pExpectedValue (String) - value to match
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Sheet and column must exist
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public int findRow(String pSheetName, String pColumnName, String pExpectedValue)
    {
        try (FileInputStream iFis  = new FileInputStream(iFilePath);
             Workbook iWorkbook    = WorkbookFactory.create(iFis))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : '" + pSheetName + "' in file : " + iFilePath);
            }

            Row iHeaderRow   = iSheet.getRow(0);
            int iColumnIndex = getColumnIndex(iHeaderRow, pColumnName);

            if (iColumnIndex == -1)
            {
                throw new RuntimeException("Column not found : '" + pColumnName + "' in sheet : '" + pSheetName + "'");
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iRow = iSheet.getRow(iRowIndex);

                if (iRow == null) { continue; }

                Cell   iCell        = iRow.getCell(iColumnIndex);
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
            throw new RuntimeException("Unable to find row in sheet : '" + pSheetName
                    + "' | Column : '" + pColumnName + "' | Value : '" + pExpectedValue
                    + "' | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadCurrentTestDataRow
    // Description   : Loads all column values for the row matching pTestCaseID into the ThreadLocal test data map.
    //                 Previous map content is cleared before loading — each test case starts with a clean state.
    // Parameters    : pSheetName  (String) - sheet name (typically "Data")
    //                 pTestCaseID (String) - test case ID to locate the correct data row
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Sheet must contain a TestCase_ID column; the supplied ID must exist in the sheet
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public void loadCurrentTestDataRow(String pSheetName, String pTestCaseID)
    {
        iCurrentTestDataRow.get().clear();

        try (FileInputStream iFis  = new FileInputStream(iFilePath);
             Workbook iWorkbook    = WorkbookFactory.create(iFis))
        {
            Sheet iSheet = iWorkbook.getSheet(pSheetName);

            if (iSheet == null)
            {
                throw new RuntimeException("Sheet not found : '" + pSheetName + "' in file : " + iFilePath);
            }

            Row iHeaderRow         = iSheet.getRow(0);
            int iTestCaseColIndex  = getColumnIndex(iHeaderRow, "TestCase_ID");

            if (iTestCaseColIndex == -1)
            {
                throw new RuntimeException("Column TestCase_ID not found in sheet : '" + pSheetName + "'");
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iRow = iSheet.getRow(iRowIndex);

                if (iRow == null) { continue; }

                String iCurrentID = iDataFormatter.formatCellValue(iRow.getCell(iTestCaseColIndex)).trim();

                if (iCurrentID.equalsIgnoreCase(pTestCaseID.trim()))
                {
                    for (int iColIdx = 0; iColIdx < iHeaderRow.getLastCellNum(); iColIdx++)
                    {
                        String iHeader = iDataFormatter.formatCellValue(iHeaderRow.getCell(iColIdx)).trim();
                        String iValue  = iRow.getCell(iColIdx) == null ? ""
                                : iDataFormatter.formatCellValue(iRow.getCell(iColIdx)).trim();

                        if (!iHeader.isEmpty())
                        {
                            iCurrentTestDataRow.get().put(iHeader, iValue);
                        }
                    }

                    log.info("Test data loaded for TestCase_ID : '" + pTestCaseID
                            + "' | Columns loaded : " + iCurrentTestDataRow.get().size());
                    return;
                }
            }

            throw new RuntimeException("No test data row found for TestCase_ID : '" + pTestCaseID
                    + "' in sheet : '" + pSheetName + "'");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to load test data row | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTestDataValue
    // Description   : Returns a value from the current thread's test data map by column name.
    //                 Called statically by CommonFunctions.getTestDataValue() via the TD: resolution chain.
    // Parameters    : pColumnName (String) - column name to look up
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : loadCurrentTestDataRow() must have been called on this thread before this method
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentTestDataValue(String pColumnName)
    {
        Map<String, String> iRow = iCurrentTestDataRow.get();

        if (iRow == null || iRow.isEmpty())
        {
            throw new RuntimeException("Test data row is not loaded on this thread. Call loadCurrentTestDataRow() first.");
        }

        if (!iRow.containsKey(pColumnName))
        {
            throw new RuntimeException("Column '" + pColumnName + "' not found in current test data row. "
                    + "Available columns: " + iRow.keySet());
        }

        return iRow.get(pColumnName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getColumnIndex
    // Description   : Searches the header row for a column matching pColumnName (case-insensitive). Returns -1 if not found.
    // Parameters    : pHeaderRow  (Row)    - header row object
    //                 pColumnName (String) - column name to find
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : pHeaderRow must not be null
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private int getColumnIndex(Row pHeaderRow, String pColumnName)
    {
        if (pHeaderRow == null)
        {
            return -1;
        }

        for (int iColIdx = 0; iColIdx < pHeaderRow.getLastCellNum(); iColIdx++)
        {
            Cell   iCell   = pHeaderRow.getCell(iColIdx);
            String iHeader = iCell == null ? "" : iDataFormatter.formatCellValue(iCell).trim();

            if (iHeader.equalsIgnoreCase(pColumnName.trim()))
            {
                return iColIdx;
            }
        }

        return -1;
    }
}