package commonFunctions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ExcelUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class CommonFunctions
{
    public static WebDriver iDriver;
    public static WebDriverWait iWait;
    public static final Logger log = Logger.getLogger(CommonFunctions.class.getName());

    private static final String iExecutionControlFilePath = "src/test/resources/Execution_Control_File/ExecutionControl.xlsx";
    private static final String iReportDocsDirectoryPath = "Test_Report/docs";
    private static final String iReportScreenshotsDirectoryPath = "Test_Report/screenshots";
    private static final DataFormatter iDataFormatter = new DataFormatter();

    // ***************************************************************************************************************************************************************************************
    // Function Name : highlightElement
    // Description   : Temporarily highlights the target element with a red border so that the current action is visually visible
    // Parameters    : pDriver (WebDriver) - active browser driver instance
    //                 pElement (WebElement) - target element to highlight
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Browser should be launched and element should be available
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void highlightElement(WebDriver pDriver, WebElement pElement)
    {
        try
        {
            if (pDriver == null || pElement == null)
            {
                return;
            }

            JavascriptExecutor iJavaScriptExecutor = (JavascriptExecutor) pDriver;
            String iOriginalStyle = pElement.getAttribute("style");

            iJavaScriptExecutor.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    pElement,
                    "border: 3px solid red; background: rgba(255,0,0,0.03);"
            );

            Thread.sleep(300);

            iJavaScriptExecutor.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    pElement,
                    iOriginalStyle == null ? "" : iOriginalStyle
            );
        }
        catch (Exception iException)
        {
            log.warning("Unable to highlight element : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : scrollIntoView
    // Description   : Scrolls the page until the target element comes into visible area
    // Parameters    : pDriver (WebDriver) - active browser driver instance
    //                 pElement (WebElement) - target element which needs to be scrolled into view
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Browser should be launched and element should be available
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void scrollIntoView(WebDriver pDriver, WebElement pElement)
    {
        try
        {
            if (pDriver == null || pElement == null)
            {
                return;
            }

            JavascriptExecutor iJavaScriptExecutor = (JavascriptExecutor) pDriver;
            iJavaScriptExecutor.executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                    pElement
            );
        }
        catch (Exception iException)
        {
            log.warning("Unable to scroll element into view : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : launchBrowser
    // Description   : Launches Chrome browser, initializes driver and explicit wait, and keeps a single browser instance per execution
    // Parameters    : None
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Chrome browser should be installed on the machine
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void launchBrowser(String pUrl)
    {
        try
        {
            WebDriverManager.chromedriver().setup();

            ChromeOptions iChromeOptions = new ChromeOptions();
            iChromeOptions.addArguments("--start-maximized");
            iChromeOptions.addArguments("--disable-notifications");
            iChromeOptions.addArguments("--disable-infobars");
            iChromeOptions.addArguments("--disable-extensions");

            iDriver = new ChromeDriver(iChromeOptions);
            iWait = new WebDriverWait(iDriver, Duration.ofSeconds(20));
            iDriver.get(pUrl);

            log.info("Chrome browser launched successfully.");
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Browser launch failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : iAction
    // Description   : Performs reusable UI actions for different control types such as click, textbox, dropdown, checkbox and gettext
    // Parameters    : pActionType   (String) - action to perform
    //                 pIdentifyBy   (String) - locator strategy such as ID, NAME, XPATH, CSS, CLASS_NAME, TAG_NAME, LINK_TEXT
    //                 pObjectName   (String) - locator value
    //                 pValueToEnter (String) - input value where applicable. Supports TD:ColumnName format also
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Browser and wait should already be initialized
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String iAction(String pActionType, String pIdentifyBy, String pObjectName, String pValueToEnter)
    {
        String iReturnedText = "";

        try
        {
            if (iDriver == null || iWait == null)
            {
                throw new RuntimeException("WebDriver or WebDriverWait is not initialized. Please launch browser first.");
            }

            if (pActionType == null || pActionType.trim().isEmpty())
            {
                throw new RuntimeException("Action type cannot be blank.");
            }

            if (pIdentifyBy == null || pIdentifyBy.trim().isEmpty())
            {
                throw new RuntimeException("IdentifyBy cannot be blank.");
            }

            if (pObjectName == null || pObjectName.trim().isEmpty())
            {
                throw new RuntimeException("ObjectName cannot be blank.");
            }

            String iResolvedValue = resolveValue(pValueToEnter);
            By iBy = getByLocator(pIdentifyBy, pObjectName);
            Actions iActions = new Actions(iDriver);
            String iActionType = pActionType.trim().toUpperCase();

            switch (iActionType)
            {
                case "CLICK":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);

                    try
                    {
                        iElement.click();
                    }
                    catch (ElementClickInterceptedException iException)
                    {
                        log.warning("Normal click intercepted. Trying JavaScript click for locator : " + pObjectName);
                        ((JavascriptExecutor) iDriver).executeScript("arguments[0].click();", iElement);
                    }
                    break;
                }

                case "DOUBLECLICK":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);
                    iActions.doubleClick(iElement).perform();
                    break;
                }

                case "RIGHTCLICK":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);
                    iActions.contextClick(iElement).perform();
                    break;
                }

                case "HOVER":
                case "MOUSEHOVER":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);
                    iActions.moveToElement(iElement).perform();
                    break;
                }

                case "TEXTBOX":
                {
                    if (iResolvedValue == null)
                    {
                        throw new RuntimeException("TEXTBOX action requires a value.");
                    }

                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    iWait.until(iWebDriver ->
                    {
                        String iReadOnly = iElement.getAttribute("readonly");
                        return iElement.isEnabled() && (iReadOnly == null || iReadOnly.equalsIgnoreCase("false"));
                    });

                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);
                    iElement.clear();
                    iElement.sendKeys(iResolvedValue);
                    break;
                }

                case "CLEARTEXTBOX":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    iWait.until(iWebDriver ->
                    {
                        String iReadOnly = iElement.getAttribute("readonly");
                        return iElement.isEnabled() && (iReadOnly == null || iReadOnly.equalsIgnoreCase("false"));
                    });

                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);
                    iElement.clear();
                    break;
                }

                case "RADIOBUTTON":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);

                    if (!iElement.isSelected())
                    {
                        iElement.click();
                    }
                    else
                    {
                        log.info("Radio button is already selected. No click performed.");
                    }
                    break;
                }

                case "CHECKBOX":
                {
                    if (iResolvedValue == null || iResolvedValue.trim().isEmpty())
                    {
                        throw new RuntimeException("CHECKBOX action requires CHECK or UNCHECK as value.");
                    }

                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);

                    String iCheckboxAction = iResolvedValue.trim().toUpperCase();

                    if ("CHECK".equals(iCheckboxAction))
                    {
                        if (!iElement.isSelected())
                        {
                            iElement.click();
                        }
                        else
                        {
                            log.info("Checkbox is already checked. No click performed.");
                        }
                    }
                    else if ("UNCHECK".equals(iCheckboxAction))
                    {
                        if (iElement.isSelected())
                        {
                            iElement.click();
                        }
                        else
                        {
                            log.info("Checkbox is already unchecked. No click performed.");
                        }
                    }
                    else
                    {
                        throw new RuntimeException("CHECKBOX only accepts CHECK or UNCHECK. Passed value : " + iResolvedValue);
                    }
                    break;
                }

                case "LIST":
                {
                    if (iResolvedValue == null || iResolvedValue.trim().isEmpty())
                    {
                        throw new RuntimeException("LIST action requires a selection value.");
                    }

                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(ExpectedConditions.elementToBeClickable(iElement));
                    iWait.until(iWebDriver -> new Select(iElement).getOptions().size() > 0);

                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);

                    Select iSelect = new Select(iElement);
                    String iListValue = iResolvedValue.trim();

                    if (iListValue.toUpperCase().startsWith("VISIBLETEXT:"))
                    {
                        iSelect.selectByVisibleText(iListValue.substring("VISIBLETEXT:".length()).trim());
                    }
                    else if (iListValue.toUpperCase().startsWith("VALUE:"))
                    {
                        iSelect.selectByValue(iListValue.substring("VALUE:".length()).trim());
                    }
                    else if (iListValue.toUpperCase().startsWith("INDEX:"))
                    {
                        int iIndex = Integer.parseInt(iListValue.substring("INDEX:".length()).trim());
                        iSelect.selectByIndex(iIndex);
                    }
                    else
                    {
                        iSelect.selectByVisibleText(iListValue);
                    }
                    break;
                }

                case "GETTEXT":
                {
                    WebElement iElement = iWait.until(ExpectedConditions.visibilityOfElementLocated(iBy));
                    iWait.until(iWebDriver ->
                    {
                        String iText = iElement.getText();
                        String iValue = iElement.getAttribute("value");

                        return (iText != null && !iText.trim().isEmpty()) ||
                                (iValue != null && !iValue.trim().isEmpty());
                    });

                    scrollIntoView(iDriver, iElement);
                    highlightElement(iDriver, iElement);

                    iReturnedText = iElement.getText().trim();

                    if (iReturnedText.isEmpty())
                    {
                        String iValueAttribute = iElement.getAttribute("value");
                        iReturnedText = iValueAttribute == null ? "" : iValueAttribute.trim();
                    }

                    log.info("GETTEXT value captured successfully : " + iReturnedText);
                    break;
                }

                default:
                    throw new RuntimeException("Unsupported action type : " + pActionType);
            }

            log.info("iAction PASSED | ActionType=" + pActionType
                    + " | IdentifyBy=" + pIdentifyBy
                    + " | Object=" + pObjectName
                    + " | Value=" + (iResolvedValue == null ? "" : iResolvedValue));
        }
        catch (Exception iException)
        {
            log.severe("iAction FAILED | ActionType=" + pActionType
                    + " | IdentifyBy=" + pIdentifyBy
                    + " | Object=" + pObjectName
                    + " | Reason=" + iException.getMessage());

            throw new RuntimeException("iAction failed for action [" + pActionType + "] on object [" + pObjectName + "] : " + iException.getMessage(), iException);
        }

        return iReturnedText;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyErrorMessage
    // Description   : Validates whether the expected error message exists inside the actual error message
    // Parameters    : pActualErrorMessage   (String) - message captured from application
    //                 pExpectedErrorMessage (String) - expected message to validate
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Actual error message should already be captured
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String verifyErrorMessage(String pActualErrorMessage, String pExpectedErrorMessage)
    {
        if (pActualErrorMessage == null)
        {
            throw new AssertionError("Actual error message is NULL. Expected message was : '" + pExpectedErrorMessage + "'");
        }

        if (pExpectedErrorMessage == null || pExpectedErrorMessage.trim().isEmpty())
        {
            throw new AssertionError("Expected error message cannot be NULL or empty.");
        }

        String iActualMessage = pActualErrorMessage.trim();
        String iExpectedMessage = pExpectedErrorMessage.trim();

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
    // Description   : Creates a Word report for the selected test case by reading its description from ExecutionControl.xlsx
    // Parameters    : pTestCaseID (String) - currently executing test case id
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : ExecutionControl.xlsx should exist and contain TestCase_ID and Description columns
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static Object[] startWordReport(String pTestCaseID)
    {
        XWPFDocument iDocument;
        String iFullPath;

        try
        {
            if (pTestCaseID == null || pTestCaseID.trim().isEmpty())
            {
                throw new RuntimeException("TestCase_ID cannot be blank while creating Word report.");
            }

            String iDescription = getDescriptionFromExecutionControl(pTestCaseID);

            DateTimeFormatter iFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
            String iTimeStamp = LocalDateTime.now().format(iFormatter);

            File iDocsDirectory = new File(iReportDocsDirectoryPath);
            if (!iDocsDirectory.exists())
            {
                iDocsDirectory.mkdirs();
            }

            String iFileName = pTestCaseID + "_" + iTimeStamp + ".docx";
            iFullPath = iDocsDirectory.getAbsolutePath() + File.separator + iFileName;

            iDocument = new XWPFDocument();

            XWPFParagraph iHeadingParagraph = iDocument.createParagraph();
            XWPFRun iHeadingRun = iHeadingParagraph.createRun();
            iHeadingRun.setBold(true);
            iHeadingRun.setFontSize(16);
            iHeadingRun.setText("Test Report: " + pTestCaseID);

            XWPFParagraph iDescriptionParagraph = iDocument.createParagraph();
            XWPFRun iDescriptionRun = iDescriptionParagraph.createRun();
            iDescriptionRun.setText("Description: " + iDescription);

            XWPFParagraph iStartTimeParagraph = iDocument.createParagraph();
            XWPFRun iStartTimeRun = iStartTimeParagraph.createRun();
            iStartTimeRun.setText("Started at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            try (FileOutputStream iFileOutputStream = new FileOutputStream(iFullPath))
            {
                iDocument.write(iFileOutputStream);
            }

            log.info("Word report created successfully : " + iFullPath);
            return new Object[]{iDocument, iFullPath};
        }
        catch (Exception iException)
        {
            log.severe("Failed to create Word report : " + iException.getMessage());
            throw new RuntimeException("Failed to create Word report : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : addScreenshotToReport
    // Description   : Captures screenshot from browser and appends it into the existing Word report along with test case caption
    // Parameters    : pDocument   (XWPFDocument) - active Word document object
    //                 pDocPath    (String) - report full path
    //                 pTestCaseID (String) - currently executing test case id
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Driver should be initialized and Word report should already be created
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void addScreenshotToReport(XWPFDocument pDocument, String pDocPath, String pTestCaseID)
    {
        try
        {
            if (iDriver == null)
            {
                throw new RuntimeException("Driver is not initialized. Cannot capture screenshot.");
            }

            if (pDocument == null)
            {
                throw new RuntimeException("Word document object is null. Cannot append screenshot.");
            }

            if (pDocPath == null || pDocPath.trim().isEmpty())
            {
                throw new RuntimeException("Word document path is blank. Cannot save screenshot into report.");
            }

            DateTimeFormatter iFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss_SSS");
            String iTimeStamp = LocalDateTime.now().format(iFormatter);

            File iScreenshotsDirectory = new File(iReportScreenshotsDirectoryPath);
            if (!iScreenshotsDirectory.exists())
            {
                iScreenshotsDirectory.mkdirs();
            }

            String iPngPath = iScreenshotsDirectory.getAbsolutePath()
                    + File.separator
                    + pTestCaseID
                    + "_"
                    + iTimeStamp
                    + ".png";

            File iSourceFile = ((TakesScreenshot) iDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(iSourceFile.toPath(), new File(iPngPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            String iDescription = getDescriptionFromExecutionControl(pTestCaseID);
            String iCaption = pTestCaseID + " - " + iDescription;

            XWPFParagraph iCaptionParagraph = pDocument.createParagraph();
            XWPFRun iCaptionRun = iCaptionParagraph.createRun();
            iCaptionRun.setBold(true);
            iCaptionRun.setText(iCaption);

            XWPFParagraph iImageParagraph = pDocument.createParagraph();
            XWPFRun iImageRun = iImageParagraph.createRun();

            try (InputStream iImageInputStream = new FileInputStream(iPngPath))
            {
                iImageRun.addPicture(
                        iImageInputStream,
                        XWPFDocument.PICTURE_TYPE_PNG,
                        iPngPath,
                        Units.toEMU(450),
                        Units.toEMU(300)
                );
            }

            try (FileOutputStream iFileOutputStream = new FileOutputStream(pDocPath))
            {
                pDocument.write(iFileOutputStream);
            }

            log.info("Screenshot added to report successfully : " + iPngPath);
        }
        catch (Exception iException)
        {
            log.severe("Failed to add screenshot to report : " + iException.getMessage());
            throw new RuntimeException("Failed to add screenshot to report : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : finalizeWordReport
    // Description   : Saves and closes the Word report document
    // Parameters    : pDocument (XWPFDocument) - active Word document object
    //                 pDocPath  (String) - report full path
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Word report should already be created
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void finalizeWordReport(XWPFDocument pDocument, String pDocPath)
    {
        try
        {
            if (pDocument == null)
            {
                log.warning("Word document is null. Finalize step skipped.");
                return;
            }

            if (pDocPath == null || pDocPath.trim().isEmpty())
            {
                throw new RuntimeException("Word report path is blank. Cannot finalize report.");
            }

            try (FileOutputStream iFileOutputStream = new FileOutputStream(pDocPath))
            {
                pDocument.write(iFileOutputStream);
            }

            pDocument.close();

            log.info("Word report finalized successfully : " + pDocPath);
        }
        catch (Exception iException)
        {
            log.severe("Failed to finalize Word report : " + iException.getMessage());
            throw new RuntimeException("Failed to finalize Word report : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getTestDataValue
    // Description   : Returns value from the currently loaded test data row based on the supplied column name
    // Parameters    : pColumnName (String) - column name from TestData sheet
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Current test data row should already be loaded through ExcelUtilities
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getTestDataValue(String pColumnName)
    {
        return ExcelUtilities.getCurrentTestDataValue(pColumnName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : resolveValue
    // Description   : Resolves actual value. If input starts with TD: then fetches value from current test data row
    // Parameters    : pValue (String) - direct value or TD:ColumnName syntax
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Current test data row should already be loaded before using TD: syntax
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String resolveValue(String pValue)
    {
        if (pValue == null)
        {
            return null;
        }

        if (pValue.trim().startsWith("TD:"))
        {
            String iColumnName = pValue.trim().substring(3).trim();
            return getTestDataValue(iColumnName);
        }

        return pValue;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getByLocator
    // Description   : Converts locator strategy and locator value into Selenium By object
    // Parameters    : pIdentifyBy (String) - locator strategy
    //                 pObjectName (String) - locator value
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : Locator strategy and value should be valid
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static By getByLocator(String pIdentifyBy, String pObjectName)
    {
        switch (pIdentifyBy.trim().toUpperCase())
        {
            case "ID":
                return By.id(pObjectName);

            case "NAME":
                return By.name(pObjectName);

            case "XPATH":
                return By.xpath(pObjectName);

            case "CSS":
            case "CSSSELECTOR":
            case "CSS_SELECTOR":
                return By.cssSelector(pObjectName);

            case "CLASSNAME":
            case "CLASS_NAME":
                return By.className(pObjectName);

            case "TAGNAME":
            case "TAG_NAME":
                return By.tagName(pObjectName);

            case "LINKTEXT":
            case "LINK_TEXT":
                return By.linkText(pObjectName);

            case "PARTIALLINKTEXT":
            case "PARTIAL_LINK_TEXT":
                return By.partialLinkText(pObjectName);

            default:
                throw new RuntimeException("Unsupported IdentifyBy value : " + pIdentifyBy);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getDescriptionFromExecutionControl
    // Description   : Reads ExecutionControl.xlsx and returns the Description for the supplied TestCase_ID
    // Parameters    : pTestCaseID (String) - currently executing test case id
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : ExecutionControl.xlsx should contain TestCase_ID and Description columns
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String getDescriptionFromExecutionControl(String pTestCaseID)
    {
        try (FileInputStream iFileInputStream = new FileInputStream(iExecutionControlFilePath);
             Workbook iWorkbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(iFileInputStream))
        {
            Sheet iSheet = iWorkbook.getSheetAt(0);

            if (iSheet == null)
            {
                throw new RuntimeException("Execution Control sheet is not present in file : " + iExecutionControlFilePath);
            }

            Row iHeaderRow = iSheet.getRow(0);

            if (iHeaderRow == null)
            {
                throw new RuntimeException("Header row is missing in Execution Control sheet.");
            }

            int iTestCaseIDColumnIndex = -1;
            int iDescriptionColumnIndex = -1;

            for (int iColumnIndex = 0; iColumnIndex < iHeaderRow.getLastCellNum(); iColumnIndex++)
            {
                String iColumnName = getCellValue(iHeaderRow, iColumnIndex).trim();

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
                throw new RuntimeException("Required columns TestCase_ID and Description were not found in ExecutionControl.xlsx");
            }

            for (int iRowIndex = 1; iRowIndex <= iSheet.getLastRowNum(); iRowIndex++)
            {
                Row iRow = iSheet.getRow(iRowIndex);

                if (iRow == null)
                {
                    continue;
                }

                String iExcelTestCaseID = getCellValue(iRow, iTestCaseIDColumnIndex).trim();

                if (iExcelTestCaseID.equalsIgnoreCase(pTestCaseID.trim()))
                {
                    return getCellValue(iRow, iDescriptionColumnIndex).trim();
                }
            }
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to fetch description from ExecutionControl.xlsx for TestCase_ID : " + pTestCaseID + ". Reason : " + iException.getMessage(), iException);
        }

        throw new RuntimeException("No description found in ExecutionControl.xlsx for TestCase_ID : " + pTestCaseID);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCellValue
    // Description   : Safely returns cell value as String from the given row and column index
    // Parameters    : pRow (Row) - excel row object
    //                 pColumnIndex (int) - target column index
    // Author        : Aniket Pathare | 20050492@mydbs.ie
    // Precondition  : None
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
}