package commonFunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utilities.ExcelUtilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.logging.Logger;

public class CommonFunctions
{
    public static WebDriver iDriver;
    public static WebDriverWait iWait;
    public static Logger log = Logger.getLogger(CommonFunctions.class.getName());
        // ***************************************************************************************************************************************************************************************
        // Function Name : highlightElement
        // Description   : Temporarily highlights a web element with a red border to make actions visible during automation execution
        // Parameters    : iDriver (WebDriver) - active browser driver instance
        //                 iElement (WebElement) - target web element to highlight
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : Browser should be launched and element must exist in DOM
        // Date Created  : 06-03-2026
        // ***************************************************************************************************************************************************************************************

        public static void highlightElement(WebDriver iDriver, WebElement iElement)
        {
                try
                    {
                        JavascriptExecutor iJS = (JavascriptExecutor) iDriver;
                        String iOriginalStyle = iElement.getAttribute("style");
                        iJS.executeScript("arguments[0].setAttribute('style','border:3px solid red;');", iElement);
                        Thread.sleep(300);
                        iJS.executeScript("arguments[0].setAttribute('style', arguments[1]);", iElement, iOriginalStyle);

                    }
                catch (Exception iException)
                    {
                        System.out.println("Unable to highlight element : " + iException.getMessage());
                    }
        }

        // ***************************************************************************************************************************************************************************************
        // Function Name : scrollIntoView
        // Description   : Scrolls the page until the target element is brought into the visible area
        // Parameters    : iDriver (WebDriver) - active browser driver instance
        //                 iElement (WebElement) - target web element to scroll into view
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : Browser should be launched and element must exist in DOM
        // Date Created  : 07-03-2026
        // ***************************************************************************************************************************************************************************************
        public static void scrollIntoView(WebDriver iDriver, WebElement iElement)
            {
                try
                {
                    JavascriptExecutor iJS = (JavascriptExecutor) iDriver;
                    iJS.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", iElement);
                }
                catch (Exception iException)
                {
                    System.out.println("Unable to scroll element into view : " + iException.getMessage());
                }
            }

        // ***************************************************************************************************************************************************************************************
        // Function Name : iAction
        // Description   : Performs reusable UI actions with action-specific explicit wait chains per element type.
        //                 Each action type applies the minimum required wait conditions before interaction,
        //                 ensuring reliable execution without over-waiting.
        // Parameters    : iActionType   (String) - action to perform (CLICK / DOUBLECLICK / RIGHTCLICK / HOVER / MOUSEHOVER /
        //                                          TEXTBOX / CLEARTEXTBOX / RADIOBUTTON / CHECKBOX / LIST / GETTEXT)
        //                 iIdentifyBy   (String) - locator strategy (ID / NAME / XPATH / CSS / CSSSELECTOR / CSS_SELECTOR /
        //                                          CLASSNAME / CLASS_NAME / TAGNAME / TAG_NAME / LINKTEXT / LINK_TEXT /
        //                                          PARTIALLINKTEXT / PARTIAL_LINK_TEXT)
        //                 iObjectName   (String) - actual locator value
        //                 iValueToEnter (String) - value/state for applicable actions
        //                                          For CHECKBOX : CHECK / UNCHECK
        //                                          For LIST     : VISIBLETEXT: / VALUE: / INDEX: / plain visible text
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : iDriver and iWait should be initialized; target element should exist in DOM
        // Date Created  : 07-03-2026
        // ***************************************************************************************************************************************************************************************
        public static String iAction(String iActionType, String iIdentifyBy, String iObjectName, String iValueToEnter)
        {
            String iText = "";

            try
            {
                if (iDriver == null)
                {
                    throw new RuntimeException("WebDriver not initialized. Ensure Hooks BeforeAll launches browser.");
                }

                iValueToEnter = resolveValue(iValueToEnter);

                By iBy;

                switch (iIdentifyBy.trim().toUpperCase())
                {
                    case "ID":                              iBy = By.id(iObjectName);              break;
                    case "NAME":                            iBy = By.name(iObjectName);            break;
                    case "XPATH":                           iBy = By.xpath(iObjectName);           break;
                    case "CSS":
                    case "CSSSELECTOR":
                    case "CSS_SELECTOR":                    iBy = By.cssSelector(iObjectName);     break;
                    case "CLASSNAME":
                    case "CLASS_NAME":                      iBy = By.className(iObjectName);       break;
                    case "TAGNAME":
                    case "TAG_NAME":                        iBy = By.tagName(iObjectName);         break;
                    case "LINKTEXT":
                    case "LINK_TEXT":                       iBy = By.linkText(iObjectName);        break;
                    case "PARTIALLINKTEXT":
                    case "PARTIAL_LINK_TEXT":               iBy = By.partialLinkText(iObjectName); break;
                    default: throw new Exception("Unsupported IdentifyBy : " + iIdentifyBy);
                }

                Actions iActions = new Actions(iDriver);

                switch (iActionType.trim().toUpperCase())
                {
                    case "CLICK":
                        WebElement iClickElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iClickElement));
                        scrollIntoView(iDriver, iClickElement);
                        highlightElement(iDriver, iClickElement);
                        try
                        {
                            iClickElement.click();
                        }
                        catch (ElementClickInterceptedException iException)
                        {
                            log.warning("Standard click intercepted on [" + iIdentifyBy + "=" + iObjectName + "] - retrying with JS click.");
                            ((JavascriptExecutor) iDriver).executeScript("arguments[0].click();", iClickElement);
                        }
                        break;

                    case "DOUBLECLICK":
                        WebElement iDblElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iDblElement));
                        scrollIntoView(iDriver, iDblElement);
                        highlightElement(iDriver, iDblElement);
                        iActions.doubleClick(iDblElement).perform();
                        break;

                    case "RIGHTCLICK":
                        WebElement iRightElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iRightElement));
                        scrollIntoView(iDriver, iRightElement);
                        highlightElement(iDriver, iRightElement);
                        iActions.contextClick(iRightElement).perform();
                        break;

                    case "HOVER":
                    case "MOUSEHOVER":
                        WebElement iHoverElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        scrollIntoView(iDriver, iHoverElement);
                        iActions.moveToElement(iHoverElement).perform();
                        break;

                    case "TEXTBOX":
                        if (iValueToEnter == null)
                        {
                            throw new Exception("TEXTBOX action requires a value for [" + iIdentifyBy + "=" + iObjectName + "]");
                        }
                        WebElement iTextElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iTextElement));
                        iWait.until(iWebDriver ->
                        {
                            String iReadOnly = iTextElement.getAttribute("readonly");
                            return iTextElement.isEnabled() && (iReadOnly == null || iReadOnly.equalsIgnoreCase("false"));
                        });
                        scrollIntoView(iDriver, iTextElement);
                        highlightElement(iDriver, iTextElement);
                        iTextElement.clear();
                        iTextElement.sendKeys(iValueToEnter);
                        break;

                    case "CLEARTEXTBOX":
                        WebElement iClearElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iClearElement));
                        iWait.until(iWebDriver ->
                        {
                            String iReadOnly = iClearElement.getAttribute("readonly");
                            return iClearElement.isEnabled() && (iReadOnly == null || iReadOnly.equalsIgnoreCase("false"));
                        });
                        scrollIntoView(iDriver, iClearElement);
                        highlightElement(iDriver, iClearElement);
                        iClearElement.clear();
                        break;

                    case "RADIOBUTTON":
                        WebElement iRadioElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iRadioElement));
                        scrollIntoView(iDriver, iRadioElement);
                        highlightElement(iDriver, iRadioElement);
                        if (!iRadioElement.isSelected())
                        {
                            iRadioElement.click();
                        }
                        else
                        {
                            log.info("RADIOBUTTON already selected - skipping click | [" + iIdentifyBy + "=" + iObjectName + "]");
                        }
                        break;

                    case "CHECKBOX":
                        if (iValueToEnter == null || iValueToEnter.isBlank())
                        {
                            throw new Exception("CHECKBOX requires CHECK or UNCHECK for [" + iIdentifyBy + "=" + iObjectName + "]");
                        }
                        WebElement iCheckElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iCheckElement));
                        scrollIntoView(iDriver, iCheckElement);
                        highlightElement(iDriver, iCheckElement);
                        switch (iValueToEnter.trim().toUpperCase())
                        {
                            case "CHECK":
                                if (!iCheckElement.isSelected()) iCheckElement.click();
                                else log.info("CHECKBOX already checked - skipping | [" + iIdentifyBy + "=" + iObjectName + "]");
                                break;
                            case "UNCHECK":
                                if (iCheckElement.isSelected()) iCheckElement.click();
                                else log.info("CHECKBOX already unchecked - skipping | [" + iIdentifyBy + "=" + iObjectName + "]");
                                break;
                            default:
                                throw new Exception("CHECKBOX only accepts CHECK or UNCHECK. Got: " + iValueToEnter);
                        }
                        break;

                    case "LIST":
                        if (iValueToEnter == null || iValueToEnter.isBlank())
                        {
                            throw new Exception("LIST requires a selection value for [" + iIdentifyBy + "=" + iObjectName + "]");
                        }
                        WebElement iListElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(ExpectedConditions.elementToBeClickable(iListElement));
                        iWait.until(iWebDriver -> new Select(iListElement).getOptions().size() > 0);
                        scrollIntoView(iDriver, iListElement);
                        highlightElement(iDriver, iListElement);
                        Select iSelect = new Select(iListElement);
                        if (iValueToEnter.toUpperCase().startsWith("VISIBLETEXT:"))
                            iSelect.selectByVisibleText(iValueToEnter.substring("VISIBLETEXT:".length()).trim());
                        else if (iValueToEnter.toUpperCase().startsWith("VALUE:"))
                            iSelect.selectByValue(iValueToEnter.substring("VALUE:".length()).trim());
                        else if (iValueToEnter.toUpperCase().startsWith("INDEX:"))
                            iSelect.selectByIndex(Integer.parseInt(iValueToEnter.substring("INDEX:".length()).trim()));
                        else
                            iSelect.selectByVisibleText(iValueToEnter.trim());
                        break;

                    case "GETTEXT":
                        WebElement iGetTextElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                        iWait.until(iWebDriver ->
                        {
                            String iRenderedText = iGetTextElement.getText();
                            String iRenderedValue = iGetTextElement.getAttribute("value");
                            return (iRenderedText != null && !iRenderedText.trim().isEmpty()) ||
                                    (iRenderedValue != null && !iRenderedValue.trim().isEmpty());
                        });
                        scrollIntoView(iDriver, iGetTextElement);
                        iText = iGetTextElement.getText().trim();
                        if (iText.isEmpty())
                        {
                            iText = iGetTextElement.getAttribute("value");
                            if (iText == null) iText = "";
                        }
                        log.info("GETTEXT | [" + iIdentifyBy + "=" + iObjectName + "] | Value: " + iText);
                        break;

                    default:
                        throw new Exception("Unsupported IActionType : " + iActionType);
                }
            }
            catch (Exception iException)
            {
                log.severe("iAction FAILED | ActionType=" + iActionType
                        + " | IdentifyBy=" + iIdentifyBy
                        + " | Object=" + iObjectName
                        + " | Reason=" + iException.getMessage());
            }

            return iText;
        }

        // ***************************************************************************************************************************************************************************************
        // Function Name : verifyErrorMessage
        // Description   : Validates whether the expected error message exists inside the actual error message with
        //                 null safety and clear assertion reporting
        // Parameters    : iActualErrorMessage   (String) - message captured from application
        //                 iExpectedErrorMessage (String) - expected message to validate
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : Actual error message should be captured from UI/API response
        // Date Created  : 07-03-2026
        // ***************************************************************************************************************************************************************************************
        public static String verifyErrorMessage(String iActualErrorMessage, String iExpectedErrorMessage)
            {
                if (iActualErrorMessage == null)
                {
                    throw new AssertionError("Actual error message is NULL. Expected message was: '" + iExpectedErrorMessage + "'");
                }

                if (iExpectedErrorMessage == null || iExpectedErrorMessage.trim().isEmpty())
                {
                    throw new AssertionError("Expected error message cannot be NULL or empty.");
                }

                String iActualMessage = iActualErrorMessage.trim();
                String iExpectedMessage = iExpectedErrorMessage.trim();

                // Case-insensitive comparison (recommended for UI validations)
                if (!iActualMessage.toLowerCase().contains(iExpectedMessage.toLowerCase()))
                {
                    throw new AssertionError(
                            "Error message validation FAILED\n" +
                                    "Expected (partial match): '" + iExpectedMessage + "'\n" +
                                    "Actual message: '" + iActualMessage + "'"
                    );
                }

                log.info("Error message verified successfully | Expected: '" + iExpectedMessage + "' | Actual: '" + iActualMessage + "'");
                return "Error message verified: '" + iExpectedMessage + "'";
            }


        // ***************************************************************************************************************************************************************************************
        // Function Name : startWordReport
        // Description   : Creates a Word report for the given test case by fetching description from ExecutionControl.xlsx
        // Parameters    : iTestCaseID (String) - test case id for which report should be created
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : ExecutionControl.xlsx should exist and contain TestCase_ID and Description columns
        // Date Created  : 07-03-2026
        // ***************************************************************************************************************************************************************************************
            public static Object[] startWordReport(String iTestCaseID)
            {
                XWPFDocument iDocument = null;
                String iFullPath = "";

                try
                {
                    String iDescription = "";
                    String iControlFilePath = "src/test/resources/Execution_Control_File/ExecutionControl.xlsx";

                    FileInputStream iFileInputStream = new FileInputStream(iControlFilePath);
                    Workbook iWorkbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(iFileInputStream);
                    Sheet iSheet = iWorkbook.getSheetAt(0);

                    int iTestCaseIDColumnIndex = -1;
                    int iDescriptionColumnIndex = -1;

                    Row iHeaderRow = iSheet.getRow(0);

                    for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                    {
                        String iColumnName = iHeaderRow.getCell(iColumnIndex).getStringCellValue().trim();

                        if (iColumnName.equalsIgnoreCase("TestCase_ID"))
                        {
                            iTestCaseIDColumnIndex = iColumnIndex;
                        }
                        else if (iColumnName.equalsIgnoreCase("Description"))
                        {
                            iDescriptionColumnIndex = iColumnIndex;
                        }
                    }

                    if (iTestCaseIDColumnIndex == -1 || iDescriptionColumnIndex == -1)
                    {
                        throw new Exception("Required columns TestCase_ID / Description not found in ExecutionControl.xlsx");
                    }

                    boolean iTestCaseFound = false;

                    for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
                    {
                        Row iRow = iSheet.getRow(iRowIndex);

                        if (iRow != null && iRow.getCell(iTestCaseIDColumnIndex) != null)
                        {
                            String iExcelTestCaseID = iRow.getCell(iTestCaseIDColumnIndex).toString().trim();

                            if (iExcelTestCaseID.equalsIgnoreCase(iTestCaseID.trim()))
                            {
                                iDescription = iRow.getCell(iDescriptionColumnIndex).toString().trim();
                                iTestCaseFound = true;
                                break;
                            }
                        }
                    }

                    iWorkbook.close();
                    iFileInputStream.close();

                    if (!iTestCaseFound)
                    {
                        throw new Exception("No description found for test case id : " + iTestCaseID);
                    }

                    DateTimeFormatter iFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
                    String iTimeStamp = LocalDateTime.now().format(iFormatter);

                    File iDocsDirectory = new File("Test_Report/docs");
                    if (!iDocsDirectory.exists())
                    {
                        iDocsDirectory.mkdirs();
                    }

                    String iFileName = iTestCaseID + "_" + iTimeStamp + ".docx";
                    iFullPath = iDocsDirectory.getAbsolutePath() + File.separator + iFileName;

                    iDocument = new XWPFDocument();

                    XWPFParagraph iHeadingParagraph = iDocument.createParagraph();
                    XWPFRun iHeadingRun = iHeadingParagraph.createRun();
                    iHeadingRun.setBold(true);
                    iHeadingRun.setFontSize(16);
                    iHeadingRun.setText("Test Report: " + iTestCaseID);

                    XWPFParagraph iDescriptionParagraph = iDocument.createParagraph();
                    XWPFRun iDescriptionRun = iDescriptionParagraph.createRun();
                    iDescriptionRun.setText("Description: " + iDescription);

                    XWPFParagraph iStartTimeParagraph = iDocument.createParagraph();
                    XWPFRun iStartTimeRun = iStartTimeParagraph.createRun();
                    iStartTimeRun.setText("Started at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    FileOutputStream iFileOutputStream = new FileOutputStream(iFullPath);
                    iDocument.write(iFileOutputStream);
                    iFileOutputStream.close();

                    log.info("Word report created successfully : " + iFullPath);
                }
                catch (Exception iException)
                {
                    log.severe("Failed to create Word report : " + iException.getMessage());
                }

                return new Object[]{iDocument, iFullPath};
            }


        // ***************************************************************************************************************************************************************************************
        // Function Name : addScreenshotToReport
        // Description   : Captures screenshot and appends it with caption into the existing Word report
        // Parameters    : iDocument   (XWPFDocument) - active Word document object
        //                 iDocPath    (String)       - Word document full path
        //                 iTestCaseID (String)       - test case id for caption and screenshot naming
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : Driver should be initialized and Word report should already be created
        // Date Created  : 07-03-2026
        // ***************************************************************************************************************************************************************************************
            public static void addScreenshotToReport(XWPFDocument iDocument, String iDocPath, String iTestCaseID)
            {
                try
                {
                    DateTimeFormatter iFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss_SSS");
                    String iTimeStamp = LocalDateTime.now().format(iFormatter);

                    File iScreenshotsDirectory = new File("Test_Report/screenshots");
                    if (!iScreenshotsDirectory.exists())
                    {
                        iScreenshotsDirectory.mkdirs();
                    }

                    String iPngPath = iScreenshotsDirectory.getAbsolutePath() + File.separator + iTestCaseID + "_" + iTimeStamp + ".png";

                    File iSourceFile = ((TakesScreenshot) iDriver).getScreenshotAs(OutputType.FILE);
                    Files.copy(iSourceFile.toPath(), new File(iPngPath).toPath());

                    String iDescription = "";
                    String iControlFilePath = "src/test/resources/Execution_Control_File/ExecutionControl.xlsx";

                    FileInputStream iFileInputStream = new FileInputStream(iControlFilePath);
                    Workbook iWorkbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(iFileInputStream);
                    Sheet iSheet = iWorkbook.getSheetAt(0);

                    int iTestCaseIDColumnIndex = -1;
                    int iDescriptionColumnIndex = -1;

                    Row iHeaderRow = iSheet.getRow(0);

                    for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
                    {
                        String iColumnName = iHeaderRow.getCell(iColumnIndex).getStringCellValue().trim();

                        if (iColumnName.equalsIgnoreCase("TestCase_ID"))
                        {
                            iTestCaseIDColumnIndex = iColumnIndex;
                        }
                        else if (iColumnName.equalsIgnoreCase("Description"))
                        {
                            iDescriptionColumnIndex = iColumnIndex;
                        }
                    }

                    for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
                    {
                        Row iRow = iSheet.getRow(iRowIndex);

                        if (iRow != null && iRow.getCell(iTestCaseIDColumnIndex) != null)
                        {
                            String iExcelTestCaseID = iRow.getCell(iTestCaseIDColumnIndex).toString().trim();

                            if (iExcelTestCaseID.equalsIgnoreCase(iTestCaseID.trim()))
                            {
                                iDescription = iRow.getCell(iDescriptionColumnIndex).toString().trim();
                                break;
                            }
                        }
                    }

                    iWorkbook.close();
                    iFileInputStream.close();

                    String iCaption = iTestCaseID + " - " + iDescription;

                    XWPFParagraph iCaptionParagraph = iDocument.createParagraph();
                    XWPFRun iCaptionRun = iCaptionParagraph.createRun();
                    iCaptionRun.setText(iCaption);

                    XWPFParagraph iImageParagraph = iDocument.createParagraph();
                    XWPFRun iImageRun = iImageParagraph.createRun();

                    InputStream iImageInputStream = new FileInputStream(iPngPath);
                    iImageRun.addPicture(
                            iImageInputStream,
                            XWPFDocument.PICTURE_TYPE_PNG,
                            iPngPath,
                            450,
                            300
                    );
                    iImageInputStream.close();

                    FileOutputStream iFileOutputStream = new FileOutputStream(iDocPath);
                    iDocument.write(iFileOutputStream);
                    iFileOutputStream.close();

                    log.info("Screenshot added to report successfully : " + iPngPath);
                }
                catch (Exception iException)
                {
                    log.severe("Failed to add screenshot to report : " + iException.getMessage());
                }
            }


        // ***************************************************************************************************************************************************************************************
        // Function Name : finalizeWordReport
        // Description   : Saves and closes the Word report document
        // Parameters    : iDocument (XWPFDocument) - active Word document object
        //                 iDocPath  (String)       - Word document full path
        // Author        : Aniket Pathare | aniket.pathare
        // Precondition  : Word report should already be created
        // Date Created  : 07-03-2026
        // ***************************************************************************************************************************************************************************************
            public static void finalizeWordReport(XWPFDocument iDocument, String iDocPath)
            {
                try
                {
                    FileOutputStream iFileOutputStream = new FileOutputStream(iDocPath);
                    iDocument.write(iFileOutputStream);
                    iFileOutputStream.close();
                    iDocument.close();

                    log.info("Word report finalized successfully : " + iDocPath);
                }
                catch (Exception iException)
                {
                    log.severe("Failed to finalize Word report : " + iException.getMessage());
                }
            }


    // ***************************************************************************************************************************************************************************************
    // Function Name : getTestDataValue
    // Description   : Returns value from currently loaded TestData row based on supplied column name
    // Parameters    : iColumnName (String) - column name from TestData sheet
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getTestDataValue(String iColumnName)
    {
        return ExcelUtilities.getCurrentTestDataValue(iColumnName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : resolveValue
    // Description   : Resolves actual value. If input starts with TD: then fetch value from current TestData row
    // Parameters    : iValue (String) - direct value or TD:ColumnName syntax
    // Author        : Aniket Pathare | aniket.pathare
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String resolveValue(String iValue)
    {
        if (iValue == null)
        {
            return null;
        }

        if (iValue.trim().startsWith("TD:"))
        {
            String iColumnName = iValue.trim().substring(3).trim();
            return getTestDataValue(iColumnName);
        }

        return iValue;
    }

}