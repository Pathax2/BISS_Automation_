// ===================================================================================================================================
// File          : CommonFunctions.java
// Package       : commonFunctions
// Description   : Core reusable utility class for BDD Cucumber Selenium automation framework.
//                 Provides browser lifecycle management, UI action execution, test data resolution,
//                 Word report generation, screenshot capture, and structured step logging.
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// Updated       : 26-03-2026 — Phase 1: step log table, cover page metadata, clearStepLog()
// ===================================================================================================================================

package commonFunctions;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import utilities.ExcelUtilities ;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CommonFunctions
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // ThreadLocal driver and wait — ensures full parallel execution safety across Cucumber threads
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final ThreadLocal<WebDriver>     iDriverHolder = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> iWaitHolder   = new ThreadLocal<>();

    // -------------------------------------------------------------------------------------------------------------------------------
    // Framework Logger
    // -------------------------------------------------------------------------------------------------------------------------------
    public static final Logger log = Logger.getLogger(CommonFunctions.class.getName());

    // -------------------------------------------------------------------------------------------------------------------------------
    // File Paths — sourced from system properties with fallback defaults to support multi-environment CI runs
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String iExecutionControlFilePath          = System.getProperty("execution.control.path",     "src/test/resources/Execution_Control_File/ExecutionControl.xlsx");
    private static final String iReportDocsDirectoryPath           = System.getProperty("report.docs.path",           "Test_Report/docs");
    private static final String iReportScreenshotsDirectoryPath    = System.getProperty("report.screenshots.path",    "Test_Report/screenshots");
    private static final int    iExplicitWaitSeconds               = Integer.parseInt(System.getProperty("explicit.wait.seconds", "30"));

    // -------------------------------------------------------------------------------------------------------------------------------
    // Shared Utilities
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final DataFormatter iDataFormatter = new DataFormatter();

    // -------------------------------------------------------------------------------------------------------------------------------
    // Date/Time Formatters
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final DateTimeFormatter iLogDateTimeFormatter                 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter iFileDateTimeFormatter                = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
    private static final DateTimeFormatter iFileDateTimeFormatterWithMilliSeconds = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss_SSS");

    // -------------------------------------------------------------------------------------------------------------------------------
    // Description cache — loaded once from ExecutionControl.xlsx to avoid repeated file I/O per screenshot
    // Key: TestCase_ID (trimmed, case-insensitive stored as uppercase) | Value: Description
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final Map<String, String> iDescriptionCache = new ConcurrentHashMap<>();
    private static volatile boolean          iDescriptionCacheLoaded = false;

    // -------------------------------------------------------------------------------------------------------------------------------
    // Phase 1 — Step log fields
    // iStepLog     : accumulates one StepLogEntry per iAction() call; written as a table by finalizeWordReport()
    // iStepCounter : increments with every iAction() call to provide a sequence number in the table
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final java.util.List<StepLogEntry> iStepLog = new java.util.ArrayList<>();
    private static int iStepCounter = 0;


    // ===============================================================================================================================
    // SECTION 1 : ThreadLocal Driver Accessors
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : getDriver
    // Description   : Returns the WebDriver instance bound to the current thread
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : launchBrowser() must have been called on this thread
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static WebDriver getDriver()
    {
        return iDriverHolder.get();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getWait
    // Description   : Returns the WebDriverWait instance bound to the current thread
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : launchBrowser() must have been called on this thread
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static WebDriverWait getWait()
    {
        return iWaitHolder.get();
    }


    // ===============================================================================================================================
    // SECTION 2 : Timestamp Utilities
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTimestamp
    // Description   : Returns current timestamp in formatted string for framework logging
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : None
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentTimestamp()
    {
        return LocalDateTime.now().format(iLogDateTimeFormatter);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentFileTimestamp
    // Description   : Returns current timestamp in file-safe format including seconds
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : None
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentFileTimestamp()
    {
        return LocalDateTime.now().format(iFileDateTimeFormatter);
    }


    // ===============================================================================================================================
    // SECTION 3 : Structured Step Logging
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : logStepStart
    // Description   : Logs action start with timestamp and action details
    // Parameters    : pActionType (String) - action type
    //                 pIdentifyBy (String) - locator strategy
    //                 pObjectName (String) - locator value
    //                 pValue (String) - input value
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void logStepStart(String pActionType, String pIdentifyBy, String pObjectName, String pValue)
    {
        log.info("[" + getCurrentTimestamp() + "] STEP START | ActionType=" + pActionType
                + " | IdentifyBy=" + pIdentifyBy
                + " | Object=" + pObjectName
                + " | Value=" + (pValue == null ? "" : pValue));
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : logStepPass
    // Description   : Logs action success with timestamp and execution duration
    // Parameters    : pActionType (String) - action type
    //                 pIdentifyBy (String) - locator strategy
    //                 pObjectName (String) - locator value
    //                 pValue (String) - input value
    //                 pDurationInMilliSeconds (long) - total action duration
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void logStepPass(String pActionType, String pIdentifyBy, String pObjectName, String pValue, long pDurationInMilliSeconds)
    {
        log.info("[" + getCurrentTimestamp() + "] STEP PASS  | ActionType=" + pActionType
                + " | IdentifyBy=" + pIdentifyBy
                + " | Object=" + pObjectName
                + " | Value=" + (pValue == null ? "" : pValue)
                + " | Duration=" + pDurationInMilliSeconds + " ms");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : logStepFail
    // Description   : Logs action failure with timestamp, execution duration, and reason
    // Parameters    : pActionType (String) - action type
    //                 pIdentifyBy (String) - locator strategy
    //                 pObjectName (String) - locator value
    //                 pValue (String) - input value
    //                 pDurationInMilliSeconds (long) - total action duration
    //                 pReason (String) - exception reason
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void logStepFail(String pActionType, String pIdentifyBy, String pObjectName, String pValue, long pDurationInMilliSeconds, String pReason)
    {
        log.severe("[" + getCurrentTimestamp() + "] STEP FAIL  | ActionType=" + pActionType
                + " | IdentifyBy=" + pIdentifyBy
                + " | Object=" + pObjectName
                + " | Value=" + (pValue == null ? "" : pValue)
                + " | Duration=" + pDurationInMilliSeconds + " ms"
                + " | Reason=" + pReason);
    }


    // ===============================================================================================================================
    // SECTION 4 : String Utilities
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : sanitizeFileName
    // Description   : Removes special characters from filename-safe strings
    // Parameters    : pInput (String) - raw filename input
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String sanitizeFileName(String pInput)
    {
        if (pInput == null || pInput.trim().isEmpty())
        {
            return "Unnamed";
        }

        return pInput.trim().replaceAll("[\\\\/:*?\"<>|\\s]+", "_");
    }


    // ===============================================================================================================================
    // SECTION 5 : Browser Lifecycle
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : launchBrowser
    // Description   : Launches the specified browser, initialises ThreadLocal driver and wait, and navigates to the given URL.
    //                 Supports Chrome, Firefox, and Edge. Headless mode is activated when the system property
    //                 "headless" is set to "true". Selenium 4 Manager resolves drivers automatically — no manual driver path needed.
    // Parameters    : pBrowserType (String) - browser to launch: CHROME | FIREFOX | EDGE
    //                 pUrl         (String) - application URL to navigate to
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Selenium 4.6+ must be on the classpath
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void launchBrowser(String pBrowserType, String pUrl)
    {
        try
        {
            if (pBrowserType == null || pBrowserType.trim().isEmpty())
            {
                throw new RuntimeException("Browser type cannot be blank. Supported values: CHROME, FIREFOX, EDGE.");
            }

            if (pUrl == null || pUrl.trim().isEmpty())
            {
                throw new RuntimeException("URL cannot be blank.");
            }

            boolean iHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
            WebDriver iDriver;

            switch (pBrowserType.trim().toUpperCase())
            {
                case "CHROME":
                {
                    ChromeOptions iOptions = new ChromeOptions();
                    iOptions.addArguments("--start-maximized");
                    iOptions.addArguments("--disable-notifications");
                    iOptions.addArguments("--disable-infobars");
                    iOptions.addArguments("--disable-extensions");

                    if (iHeadless)
                    {
                        iOptions.addArguments("--headless=new");
                        iOptions.addArguments("--window-size=1920,1080");
                    }

                    System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
                    iDriver = new ChromeDriver(iOptions);
                    break;
                }

                case "FIREFOX":
                {
                    FirefoxOptions iOptions = new FirefoxOptions();

                    if (iHeadless)
                    {
                        iOptions.addArguments("-headless");
                    }

                    iDriver = new FirefoxDriver(iOptions);
                    iDriver.manage().window().maximize();
                    break;
                }

                case "EDGE":
                {
                    EdgeOptions iOptions = new EdgeOptions();
                    iOptions.addArguments("--start-maximized");
                    iOptions.addArguments("--disable-notifications");

                    if (iHeadless)
                    {
                        iOptions.addArguments("--headless=new");
                        iOptions.addArguments("--window-size=1920,1080");
                    }

                    iDriver = new EdgeDriver(iOptions);
                    break;
                }

                default:
                    throw new RuntimeException("Unsupported browser type : '" + pBrowserType + "'. Supported values: CHROME, FIREFOX, EDGE.");
            }

            iDriverHolder.set(iDriver);
            iWaitHolder.set(new WebDriverWait(iDriver, Duration.ofSeconds(iExplicitWaitSeconds)));
            iDriver.get(pUrl.trim());

            log.info("[" + getCurrentTimestamp() + "] Browser launched successfully | Browser=" + pBrowserType.toUpperCase()
                    + " | Headless=" + iHeadless
                    + " | URL=" + pUrl);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Browser launch failed for browser [" + pBrowserType + "] : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : closeBrowser
    // Description   : Quits the WebDriver instance bound to the current thread and cleans up ThreadLocal references
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : launchBrowser() must have been called on this thread
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void closeBrowser()
    {
        try
        {
            WebDriver iDriver = iDriverHolder.get();

            if (iDriver != null)
            {
                iDriver.quit();
                log.info("[" + getCurrentTimestamp() + "] Browser closed successfully.");
            }
            else
            {
                log.warning("[" + getCurrentTimestamp() + "] closeBrowser called but no active driver found on this thread.");
            }
        }
        catch (Exception iException)
        {
            log.warning("[" + getCurrentTimestamp() + "] Error while closing browser : " + iException.getMessage());
        }
        finally
        {
            iDriverHolder.remove();
            iWaitHolder.remove();
        }
    }


    // ===============================================================================================================================
    // SECTION 6 : Element Interaction Helpers
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : highlightElement
    // Description   : Temporarily highlights the target element with a red border so that the current action is visually visible
    // Parameters    : pDriver  (WebDriver)   - active browser driver instance
    //                 pElement (WebElement)  - target element to highlight
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
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

            iJavaScriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", pElement, "border: 3px solid red; background: rgba(255,0,0,0.03);");

            new Actions(pDriver).pause(Duration.ofMillis(400)).perform();

            iJavaScriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", pElement, iOriginalStyle == null ? "" : iOriginalStyle);
        }
        catch (Exception iException)
        {
            log.warning("[" + getCurrentTimestamp() + "] Unable to highlight element : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : scrollIntoView
    // Description   : Scrolls the page until the target element comes into the visible area
    // Parameters    : pDriver  (WebDriver)  - active browser driver instance
    //                 pElement (WebElement) - target element to scroll into view
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
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

            ((JavascriptExecutor) pDriver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                    pElement
            );
        }
        catch (Exception iException)
        {
            log.warning("[" + getCurrentTimestamp() + "] Unable to scroll element into view : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getByLocator
    // Description   : Converts locator strategy string and locator value into a Selenium By object
    // Parameters    : pIdentifyBy (String) - locator strategy: ID | NAME | XPATH | CSS | CSSSELECTOR | CSS_SELECTOR |
    //                                        CLASSNAME | CLASS_NAME | TAGNAME | TAG_NAME | LINKTEXT | LINK_TEXT |
    //                                        PARTIALLINKTEXT | PARTIAL_LINK_TEXT
    //                 pObjectName (String) - locator value
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Locator strategy and value should be valid and non-blank
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
                throw new RuntimeException("Unsupported IdentifyBy value : '" + pIdentifyBy
                        + "'. Supported values: ID, NAME, XPATH, CSS, CSSSELECTOR, CSS_SELECTOR, "
                        + "CLASSNAME, CLASS_NAME, TAGNAME, TAG_NAME, LINKTEXT, LINK_TEXT, PARTIALLINKTEXT, PARTIAL_LINK_TEXT");
        }
    }


    // ===============================================================================================================================
    // SECTION 7 : iAction — Per-Type Private Action Methods
    // ===============================================================================================================================

    private static void performClick(WebDriver pDriver, WebDriverWait pWait, By pBy, String pObjectName)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        try
        {
            iElement.click();
        }
        catch (ElementClickInterceptedException iException)
        {
            log.warning("[" + getCurrentTimestamp() + "] Normal click intercepted. Retrying with JavaScript click for locator : " + pObjectName);
            ((JavascriptExecutor) pDriver).executeScript("arguments[0].click();", iElement);
        }
    }

    private static void performDoubleClick(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
        new Actions(pDriver).doubleClick(iElement).perform();
    }

    private static void performRightClick(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
        new Actions(pDriver).contextClick(iElement).perform();
    }

    private static void performHover(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
        new Actions(pDriver).moveToElement(iElement).perform();
    }

    private static void performTextBox(WebDriver pDriver, WebDriverWait pWait, By pBy, String pValue)
    {
        if (pValue == null)
        {
            throw new RuntimeException("TEXTBOX action requires a non-null value.");
        }

        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        pWait.until(iWebDriver ->
        {
            String iReadOnly = iElement.getAttribute("readonly");
            return iElement.isEnabled() && (iReadOnly == null || iReadOnly.equalsIgnoreCase("false"));
        });

        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
        iElement.clear();
        iElement.sendKeys(pValue);
    }

    private static void performClearTextBox(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        pWait.until(iWebDriver ->
        {
            String iReadOnly = iElement.getAttribute("readonly");
            return iElement.isEnabled() && (iReadOnly == null || iReadOnly.equalsIgnoreCase("false"));
        });

        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
        iElement.clear();
    }

    private static void performRadioButton(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        if (!iElement.isSelected())
        {
            iElement.click();
        }
        else
        {
            log.info("[" + getCurrentTimestamp() + "] Radio button is already selected. No click performed.");
        }
    }

    private static void performCheckBox(WebDriver pDriver, WebDriverWait pWait, By pBy, String pValue)
    {
        if (pValue == null || pValue.trim().isEmpty())
        {
            throw new RuntimeException("CHECKBOX action requires CHECK or UNCHECK as value.");
        }

        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        String iCheckboxAction = pValue.trim().toUpperCase();

        if ("CHECK".equals(iCheckboxAction))
        {
            if (!iElement.isSelected())
            {
                iElement.click();
            }
            else
            {
                log.info("[" + getCurrentTimestamp() + "] Checkbox is already checked. No click performed.");
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
                log.info("[" + getCurrentTimestamp() + "] Checkbox is already unchecked. No click performed.");
            }
        }
        else
        {
            throw new RuntimeException("CHECKBOX only accepts CHECK or UNCHECK. Passed value : '" + pValue + "'");
        }
    }

    private static void performList(WebDriver pDriver, WebDriverWait pWait, By pBy, String pValue)
    {
        if (pValue == null || pValue.trim().isEmpty())
        {
            throw new RuntimeException("LIST action requires a selection value.");
        }

        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));

        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        String iTagName    = iElement.getTagName().toLowerCase().trim();
        String iListValue  = pValue.trim();

        log.info("[" + getCurrentTimestamp() + "] LIST | tag=<" + iTagName + "> | value=" + iListValue);

        if ("mat-select".equals(iTagName))
        {
            performMatSelect(pDriver, pWait, iElement, iListValue);
        }
        else if ("select".equals(iTagName))
        {
            pWait.until(iWebDriver -> new Select(iElement).getOptions().size() > 0);
            performNativeSelect(iElement, iListValue);
        }
        else
        {
            log.warning("[" + getCurrentTimestamp() + "] LIST | Unexpected tag <" + iTagName
                    + "> — attempting mat-select click approach as fallback.");
            performMatSelect(pDriver, pWait, iElement, iListValue);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : performMatSelect
    // Description   : Intelligent Angular Material <mat-select> handler.
    //                 Supports both standard and searchable mat-select (ngx-mat-select-search).
    //                 Supports INDEX:, VISIBLETEXT:, VALUE:, or plain text selection strategies.
    // Author        : Aniket Pathare
    // Date Updated  : 24-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void performMatSelect(WebDriver pDriver, WebDriverWait pWait,
                                         WebElement pElement, String pValue)
    {
        // ── STEP 1 : OPEN PANEL ──────────────────────────────────────────────────────────
        try
        {
            String iIsExpanded = pElement.getAttribute("aria-expanded");
            if (!"true".equalsIgnoreCase(iIsExpanded))
            {
                pElement.click();
            }
            else
            {
                log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Panel already open.");
            }
        }
        catch (ElementClickInterceptedException iEx)
        {
            log.warning("[" + getCurrentTimestamp() + "] MAT-SELECT | Click intercepted → Using JS click");
            ((JavascriptExecutor) pDriver).executeScript("arguments[0].click();", pElement);
        }

        // ── STEP 2 : WAIT FOR PANEL ──────────────────────────────────────────────────────
        By iPanelBy = By.cssSelector(
                "div[role='listbox'].mat-mdc-select-panel, " +
                        "div[role='listbox'].mdc-menu-surface--open, " +
                        "div[role='listbox']"
        );

        WebElement iPanel = pWait.until(
                ExpectedConditions.visibilityOfElementLocated(iPanelBy)
        );

        log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Panel opened.");

        // ── STEP 3 : DETECT SEARCH MODE ──────────────────────────────────────────────────
        boolean iIsSearchable =
                !iPanel.findElements(By.cssSelector("mat-option.contains-mat-select-search")).isEmpty();

        if (iIsSearchable)
        {
            log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Search-enabled mat-select detected.");
        }

        // ── STEP 4 : RESOLVE VALUE STRATEGY ──────────────────────────────────────────────
        String iUpper = pValue.toUpperCase();

        if (iUpper.startsWith("INDEX:"))
        {
            int iIndex = Integer.parseInt(pValue.substring("INDEX:".length()).trim());

            By iOptionLocator = iIsSearchable
                    ? By.cssSelector("mat-option.optionListItem")
                    : By.cssSelector("mat-option:not(.contains-mat-select-search)");

            java.util.List<WebElement> iOptions = pWait.until(driver ->
            {
                java.util.List<WebElement> opts = iPanel.findElements(iOptionLocator);
                return (opts.size() > iIndex) ? opts : null;
            });

            WebElement iTarget = iOptions.get(iIndex);
            scrollIntoView(pDriver, iTarget);
            highlightElement(pDriver, iTarget);
            iTarget.click();

            log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Selected by INDEX = " + iIndex);
        }
        else
        {
            String iText;

            if (iUpper.startsWith("VISIBLETEXT:"))
                iText = pValue.substring("VISIBLETEXT:".length()).trim();
            else if (iUpper.startsWith("VALUE:"))
            {
                iText = pValue.substring("VALUE:".length()).trim();
                log.warning("MAT-SELECT | VALUE: prefix treated as visible text.");
            }
            else
                iText = pValue.trim();

            final String iFinalText = iText;

            if (iIsSearchable)
            {
                By iSearchInputBy = By.cssSelector(
                        "mat-option.contains-mat-select-search input.mat-select-search-input:not(.mat-select-search-hidden)"
                );

                WebElement iSearchInput = pWait.until(
                        ExpectedConditions.visibilityOfElementLocated(iSearchInputBy)
                );

                iSearchInput.clear();
                iSearchInput.sendKeys(iFinalText);

                log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Search typed: '" + iFinalText + "'");

                pWait.until(driver ->
                        !iPanel.findElements(By.cssSelector("mat-option.optionListItem")).isEmpty()
                );
            }

            By iOptionsBy = iIsSearchable
                    ? By.cssSelector("mat-option.optionListItem")
                    : By.cssSelector("mat-option:not(.contains-mat-select-search)");

            WebElement iTarget = pWait.until(driver ->
            {
                java.util.List<WebElement> options = iPanel.findElements(iOptionsBy);

                for (WebElement opt : options)
                {
                    try
                    {
                        if (opt.getText().trim().equalsIgnoreCase(iFinalText))
                            return opt;
                    }
                    catch (StaleElementReferenceException stale)
                    {
                        return null;
                    }
                }
                return null;
            });

            if (iTarget == null)
            {
                StringBuilder buf = new StringBuilder();
                try
                {
                    iPanel.findElements(By.cssSelector(
                            "mat-option.optionListItem, mat-option:not(.contains-mat-select-search)")
                    ).forEach(o -> buf.append("'").append(o.getText().trim()).append("' | "));
                }
                catch (Exception ignored) {}

                pressEscape(pDriver);
                throw new RuntimeException(
                        "MAT-SELECT | No option found matching: '" + iFinalText + "'\n" +
                                "Available: " + buf
                );
            }

            scrollIntoView(pDriver, iTarget);
            highlightElement(pDriver, iTarget);
            iTarget.click();

            log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Selected option: '" + iFinalText + "'");
        }

        // ── STEP 5 : WAIT FOR PANEL TO CLOSE ─────────────────────────────────────────────
        try
        {
            pWait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div[role='listbox'].mdc-menu-surface--open")
            ));
        }
        catch (Exception ignored)
        {
        }

        log.info("[" + getCurrentTimestamp() + "] MAT-SELECT | Selection complete.");
    }

    private static void performNativeSelect(WebElement pElement, String pListValue)
    {
        Select iSelect      = new Select(pElement);
        String iUpperValue  = pListValue.toUpperCase();

        if (iUpperValue.startsWith("VISIBLETEXT:"))
        {
            iSelect.selectByVisibleText(pListValue.substring("VISIBLETEXT:".length()).trim());
        }
        else if (iUpperValue.startsWith("VALUE:"))
        {
            iSelect.selectByValue(pListValue.substring("VALUE:".length()).trim());
        }
        else if (iUpperValue.startsWith("INDEX:"))
        {
            iSelect.selectByIndex(Integer.parseInt(pListValue.substring("INDEX:".length()).trim()));
        }
        else
        {
            iSelect.selectByVisibleText(pListValue);
        }

        log.info("[" + getCurrentTimestamp() + "] NATIVE SELECT | Selected: '" + pListValue + "'");
    }

    private static void pressEscape(WebDriver pDriver)
    {
        try
        {
            pDriver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        }
        catch (Exception ignored) {}
    }

    private static void performDragDrop(WebDriver pDriver, WebDriverWait pWait, By pSourceBy, String pValue)
    {
        if (pValue == null || pValue.trim().isEmpty())
        {
            throw new RuntimeException("DRAGDROP action requires target locator in format IDENTIFYBY:::LOCATORVALUE.");
        }

        String[] iTargetDetails = pValue.split(":::", 2);

        if (iTargetDetails.length != 2)
        {
            throw new RuntimeException("DRAGDROP value format is invalid. Expected: IDENTIFYBY:::LOCATORVALUE. Received: '" + pValue + "'");
        }

        By iTargetBy = getByLocator(iTargetDetails[0].trim(), iTargetDetails[1].trim());

        WebElement iSourceElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pSourceBy));
        WebElement iTargetElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(iTargetBy));

        scrollIntoView(pDriver, iSourceElement);
        highlightElement(pDriver, iSourceElement);
        scrollIntoView(pDriver, iTargetElement);
        highlightElement(pDriver, iTargetElement);

        new Actions(pDriver).dragAndDrop(iSourceElement, iTargetElement).perform();
    }

    private static void performUploadFile(WebDriver pDriver, WebDriverWait pWait, By pBy, String pValue)
    {
        if (pValue == null || pValue.trim().isEmpty())
        {
            throw new RuntimeException("UPLOADFILE action requires an absolute file path.");
        }

        WebElement iElement = pWait.until(ExpectedConditions.presenceOfElementLocated(pBy));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
        iElement.sendKeys(pValue);
    }

    private static void performWaitVisible(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        try {
            Wait<WebDriver> fluentWait = new FluentWait<>(pDriver)
                    .withTimeout(Duration.ofSeconds(20))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);

            WebElement element = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));

            scrollIntoView(pDriver, element);
            highlightElement(pDriver, element);

        } catch (Exception e) {
            throw new RuntimeException("WAITVISIBLE failed for locator: " + pBy.toString()
                    + " | Reason: " + e.getMessage(), e);
        }
    }

    private static void performWaitClickable(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
    }

    private static void performWaitInvisible(WebDriver pDriver, WebDriverWait pWait, By pBy, String pObjectName)
    {
        By iParentBy = resolveSpinnerParentLocator(pBy, pObjectName);

        log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — watching locator : " + pObjectName);

        try
        {
            pWait.until(iWebDriver ->
            {
                try
                {
                    java.util.List<WebElement> iElements = iWebDriver.findElements(iParentBy);

                    if (iElements.isEmpty())
                    {
                        log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — spinner container not found in DOM (already removed).");
                        return true;
                    }

                    WebElement iSpinner    = iElements.get(0);
                    String     iAriaHidden = iSpinner.getAttribute("aria-hidden");
                    boolean    iIsHidden   = "true".equalsIgnoreCase(iAriaHidden);
                    boolean    iIsGone     = !iSpinner.isDisplayed();

                    if (iIsHidden || iIsGone)
                    {
                        log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — spinner resolved"
                                + " | aria-hidden=" + iAriaHidden
                                + " | isDisplayed=" + iSpinner.isDisplayed());
                        return true;
                    }

                    return false;
                }
                catch (org.openqa.selenium.StaleElementReferenceException iStale)
                {
                    log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — spinner element went stale (removed from DOM).");
                    return true;
                }
            });

            log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — spinner dismissed successfully : " + pObjectName);
        }
        catch (org.openqa.selenium.TimeoutException iTimeout)
        {
            try
            {
                java.util.List<WebElement> iElements = pDriver.findElements(iParentBy);

                if (iElements.isEmpty())
                {
                    log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — spinner not in DOM after timeout — treating as dismissed.");
                    return;
                }

                WebElement iSpinner = iElements.get(0);
                String     iOpacity = (String) ((JavascriptExecutor) pDriver)
                        .executeScript("return window.getComputedStyle(arguments[0]).opacity;", iSpinner);
                String     iDisplay = (String) ((JavascriptExecutor) pDriver)
                        .executeScript("return window.getComputedStyle(arguments[0]).display;",  iSpinner);

                boolean iGoneByStyle = "0".equals(iOpacity) || "none".equals(iDisplay);

                if (iGoneByStyle)
                {
                    log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — spinner dismissed via JS computed style"
                            + " | opacity=" + iOpacity + " | display=" + iDisplay);
                    return;
                }

                throw new RuntimeException("WAITINVISIBLE timed out. Spinner still visible after all strategies."
                        + " | opacity=" + iOpacity
                        + " | display=" + iDisplay
                        + " | Locator: " + pObjectName);
            }
            catch (RuntimeException iRe)
            {
                throw iRe;
            }
            catch (Exception iJsException)
            {
                throw new RuntimeException("WAITINVISIBLE failed. Spinner did not dismiss within wait period. Locator : " + pObjectName, iJsException);
            }
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : resolveSpinnerParentLocator
    // Description   : When the caller passes the deep SVG circle locator, this resolves it upward
    //                 to the nearest .mdc-circular-progress parent div which is the element that
    //                 actually carries the aria-hidden attribute MDC toggles.
    //                 If a non-SVG locator is passed, it is returned unchanged.
    // Parameters    : pBy         (By)    - original locator from iAction call
    //                 pObjectName (String) - raw locator string for string-based inspection
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static By resolveSpinnerParentLocator(By pBy, String pObjectName)
    {
        if (pObjectName != null && (
                pObjectName.contains("mdc-circular-progress__gap-patch") ||
                        pObjectName.contains("name()='circle'")                  ||
                        pObjectName.contains("name()='svg'")))
        {
            log.info("[" + getCurrentTimestamp() + "] WAITINVISIBLE — SVG spinner locator detected."
                    + " Resolving to parent .mdc-circular-progress container for aria-hidden check.");
            return By.cssSelector(".mdc-circular-progress");
        }

        return pBy;
    }

    private static void performScrollToElement(WebDriver pDriver, WebDriverWait pWait, By pBy)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);
    }

    private static String performGetText(WebDriver pDriver, WebDriverWait pWait, By pBy, String pObjectName)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(iWebDriver ->
        {
            String iText  = iElement.getText();
            String iValue = iElement.getAttribute("value");
            return (iText != null && !iText.trim().isEmpty())
                    || (iValue != null && !iValue.trim().isEmpty());
        });

        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        String iReturnedText = iElement.getText().trim();

        if (iReturnedText.isEmpty())
        {
            String iValueAttribute = iElement.getAttribute("value");
            iReturnedText = iValueAttribute == null ? "" : iValueAttribute.trim();
        }

        if (iReturnedText.isEmpty())
        {
            log.warning("[" + getCurrentTimestamp() + "] GETTEXT returned empty string for locator : " + pObjectName
                    + ". Element may not have text content or value attribute.");
        }
        else
        {
            log.info("[" + getCurrentTimestamp() + "] GETTEXT value captured : '" + iReturnedText + "'");
        }

        return iReturnedText;
    }

    private static String performVerifyText(WebDriver pDriver, WebDriverWait pWait, By pBy, String pValue)
    {
        if (pValue == null)
        {
            throw new RuntimeException("VERIFYTEXT action requires an expected value.");
        }

        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        String iActualText = iElement.getText().trim();

        if (iActualText.isEmpty())
        {
            String iValueAttribute = iElement.getAttribute("value");
            iActualText = iValueAttribute == null ? "" : iValueAttribute.trim();
        }

        String iResolvedExpected;
        boolean iVerificationPassed;
        String  iMatchMode;

        if (pValue.trim().toUpperCase().startsWith("EXACT:"))
        {
            iResolvedExpected  = pValue.trim().substring("EXACT:".length()).trim();
            iMatchMode         = "EXACT";
            iVerificationPassed = iActualText.equalsIgnoreCase(iResolvedExpected);
        }
        else if (pValue.trim().toUpperCase().startsWith("STARTSWITH:"))
        {
            iResolvedExpected  = pValue.trim().substring("STARTSWITH:".length()).trim();
            iMatchMode         = "STARTSWITH";
            iVerificationPassed = iActualText.toLowerCase().startsWith(iResolvedExpected.toLowerCase());
        }
        else
        {
            iResolvedExpected  = pValue.trim().toUpperCase().startsWith("CONTAINS:")
                    ? pValue.trim().substring("CONTAINS:".length()).trim()
                    : pValue.trim();
            iMatchMode         = "CONTAINS";
            iVerificationPassed = iActualText.toLowerCase().contains(iResolvedExpected.toLowerCase());
        }

        if (!iVerificationPassed)
        {
            throw new AssertionError("VERIFYTEXT failed [Mode=" + iMatchMode + "]"
                    + " | Expected : '" + iResolvedExpected + "'"
                    + " | Actual   : '" + iActualText + "'");
        }

        return iActualText;
    }

    private static void performVerifyElement(WebDriver pDriver, WebDriverWait pWait, By pBy, String pObjectName)
    {
        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        if (!iElement.isDisplayed())
        {
            throw new AssertionError("VERIFYELEMENT failed. Element is not displayed for locator : " + pObjectName);
        }
    }

    private static void performCalendar(WebDriver pDriver, WebDriverWait pWait, By pBy, String pValue)
    {
        if (pValue == null || pValue.trim().isEmpty())
        {
            throw new RuntimeException("CALENDAR action requires a date value.");
        }

        WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
        pWait.until(ExpectedConditions.elementToBeClickable(iElement));
        scrollIntoView(pDriver, iElement);
        highlightElement(pDriver, iElement);

        try
        {
            iElement.clear();
        }
        catch (Exception ignored)
        {
        }

        try
        {
            iElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            iElement.sendKeys(Keys.DELETE);
        }
        catch (Exception ignored)
        {
        }

        iElement.sendKeys(pValue);
        iElement.sendKeys(Keys.TAB);
    }


    // ===============================================================================================================================
    // SECTION 8 : iAction — Public Entry Point
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : iAction
    // Description   : Performs reusable UI actions dispatched to dedicated private methods per action type.
    //                 Supports: CLICK, DOUBLECLICK, RIGHTCLICK, HOVER, MOUSEHOVER, TEXTBOX, CLEARTEXTBOX,
    //                           RADIOBUTTON, CHECKBOX, LIST, DRAGDROP, UPLOADFILE, WAITVISIBLE, WAITCLICKABLE,
    //                           WAITINVISIBLE, SCROLLTOELEMENT, GETTEXT, VERIFYTEXT, VERIFYELEMENT,
    //                           CALENDAR (also accepts legacy typo CALENDER)
    // Parameters    : pActionType   (String) - action to perform (case-insensitive)
    //                 pIdentifyBy   (String) - locator strategy
    //                 pObjectName   (String) - locator value
    //                 pValueToEnter (String) - input value where applicable; supports TD:ColumnName syntax
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : launchBrowser() must have been called on this thread before invoking iAction
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String iAction(String pActionType, String pIdentifyBy, String pObjectName, String pValueToEnter)
    {
        String iReturnedText = "";
        long   iStartTime    = System.currentTimeMillis();

        try
        {
            WebDriver     iDriver = iDriverHolder.get();
            WebDriverWait iWait   = iWaitHolder.get();

            if (iDriver == null || iWait == null)
            {
                throw new RuntimeException("WebDriver or WebDriverWait is not initialised on this thread. Call launchBrowser() before iAction().");
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
            By     iBy            = getByLocator(pIdentifyBy, pObjectName);
            String iActionType    = pActionType.trim().toUpperCase();

            logStepStart(iActionType, pIdentifyBy, pObjectName, iResolvedValue);

            switch (iActionType)
            {
                case "CLICK":
                    performClick(iDriver, iWait, iBy, pObjectName);
                    break;

                case "DOUBLECLICK":
                    performDoubleClick(iDriver, iWait, iBy);
                    break;

                case "RIGHTCLICK":
                    performRightClick(iDriver, iWait, iBy);
                    break;

                case "HOVER":
                case "MOUSEHOVER":
                    performHover(iDriver, iWait, iBy);
                    break;

                case "TEXTBOX":
                    performTextBox(iDriver, iWait, iBy, iResolvedValue);
                    break;

                case "CLEARTEXTBOX":
                    performClearTextBox(iDriver, iWait, iBy);
                    break;

                case "RADIOBUTTON":
                    performRadioButton(iDriver, iWait, iBy);
                    break;

                case "CHECKBOX":
                    performCheckBox(iDriver, iWait, iBy, iResolvedValue);
                    break;

                case "LIST":
                    performList(iDriver, iWait, iBy, iResolvedValue);
                    break;

                case "DRAGDROP":
                    performDragDrop(iDriver, iWait, iBy, iResolvedValue);
                    break;

                case "UPLOADFILE":
                    performUploadFile(iDriver, iWait, iBy, iResolvedValue);
                    break;

                case "WAITVISIBLE":
                    performWaitVisible(iDriver, iWait, iBy);
                    break;

                case "WAITCLICKABLE":
                    performWaitClickable(iDriver, iWait, iBy);
                    break;

                case "WAITINVISIBLE":
                    performWaitInvisible(iDriver,iWait, iBy, pObjectName);
                    break;

                case "SCROLLTOELEMENT":
                    performScrollToElement(iDriver, iWait, iBy);
                    break;

                case "GETTEXT":
                    iReturnedText = performGetText(iDriver, iWait, iBy, pObjectName);
                    break;

                case "VERIFYTEXT":
                    iReturnedText = performVerifyText(iDriver, iWait, iBy, iResolvedValue);
                    break;

                case "VERIFYELEMENT":
                    performVerifyElement(iDriver, iWait, iBy, pObjectName);
                    break;

                case "CALENDAR":
                case "CALENDER": // Legacy alias retained for backward compatibility — use CALENDAR going forward
                    performCalendar(iDriver, iWait, iBy, iResolvedValue);
                    break;

                default:
                    throw new RuntimeException("Unsupported action type : '" + pActionType + "'.");
            }

            long iDuration = System.currentTimeMillis() - iStartTime;
            logStepPass(iActionType, pIdentifyBy, pObjectName, iResolvedValue, iDuration);
            appendToStepLog(iActionType, pObjectName, iResolvedValue, "PASS", iDuration, ""); // ← Phase 1: record PASS
        }
        catch (Exception iException)
        {
            long iDuration = System.currentTimeMillis() - iStartTime;
            logStepFail(pActionType, pIdentifyBy, pObjectName, pValueToEnter, iDuration, iException.getMessage());
            appendToStepLog(pActionType, pObjectName, pValueToEnter, "FAIL", iDuration, iException.getMessage()); // ← Phase 1: record FAIL
            throw new RuntimeException("iAction failed | Action=[" + pActionType + "] | Object=[" + pObjectName + "] | Reason : " + iException.getMessage(), iException);
        }

        return iReturnedText;
    }


    // ===============================================================================================================================
    // SECTION 9 : Verification Utilities
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyErrorMessage
    // Description   : Validates whether the expected error message is contained within the actual error message (case-insensitive)
    // Parameters    : pActualErrorMessage   (String) - message captured from the application
    //                 pExpectedErrorMessage (String) - expected message to validate
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Actual error message should already be captured before calling this method
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

        String iActualMessage   = pActualErrorMessage.trim();
        String iExpectedMessage = pExpectedErrorMessage.trim();

        if (!iActualMessage.toLowerCase().contains(iExpectedMessage.toLowerCase()))
        {
            throw new AssertionError("Error message validation FAILED"
                    + "\n  Expected (partial match) : '" + iExpectedMessage + "'"
                    + "\n  Actual message           : '" + iActualMessage + "'");
        }

        log.info("[" + getCurrentTimestamp() + "] Error message verified successfully"
                + " | Expected : '" + iExpectedMessage + "'"
                + " | Actual : '" + iActualMessage + "'");

        return "Error message verified: '" + iExpectedMessage + "'";
    }


    // ===============================================================================================================================
    // SECTION 10 : Screenshot
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : takeScreenshot
    // Description   : Captures a viewport screenshot and saves to disk. Delegates to ScreenshotManager.
    // Parameters    : pStepName (String) - logical step name used in screenshot file naming
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : launchBrowser() must have been called on this thread
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String takeScreenshot(String pStepName)
    {
        return utilities.ScreenshotManager.captureViewport(iDriverHolder.get(), pStepName);
    }


    // ===============================================================================================================================
    // SECTION 11 : Word Report
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : clearStepLog
    // Description   : Resets the step log list and counter for a new test case.
    //                 Called from Hooks.@BeforeAll so every test case starts with a clean log.
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void clearStepLog()
    {
        iStepLog.clear();
        iStepCounter = 0;
        log.info("[" + getCurrentTimestamp() + "] Step log cleared for new test case.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : appendToStepLog
    // Description   : Records one iAction() execution result into the in-memory step log.
    //                 Private — called only from the success path and catch block of iAction().
    //                 Masks the value for TEXTBOX actions when the value text contains "password" or "pin".
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void appendToStepLog(String pAction, String pLocator, String pValue,
                                        String pStatus, long pDurationMs, String pFailReason)
    {
        try
        {
            iStepCounter++;

            String iDisplayValue = pValue;
            if ("TEXTBOX".equalsIgnoreCase(pAction) && pValue != null
                    && (pValue.toLowerCase().contains("password") || pValue.toLowerCase().contains("pin")))
            {
                iDisplayValue = "****";
            }

            iStepLog.add(new StepLogEntry(iStepCounter, pAction, pLocator,
                    iDisplayValue, pStatus, pDurationMs, pFailReason));
        }
        catch (Exception iException)
        {
            log.warning("[" + getCurrentTimestamp() + "] Step log append failed: " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : startWordReport
    // Description   : Creates a Word (.docx) report with a full cover page and metadata table.
    //                 Returns {XWPFDocument, String} — document object and file path.
    //                 The document is written to disk once here; all screenshots accumulate in
    //                 memory and the final write happens in finalizeWordReport().
    // Parameters    : pTestCaseID (String) - currently executing test case ID
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026 | Updated: 26-03-2026 (cover page + metadata table)
    // ***************************************************************************************************************************************************************************************
    public static Object[] startWordReport(String pTestCaseID)
    {
        try
        {
            if (pTestCaseID == null || pTestCaseID.trim().isEmpty())
            {
                throw new RuntimeException("TestCase_ID cannot be blank while creating Word report.");
            }

            String iDescription = getDescriptionFromExecutionControl(pTestCaseID);
            String iTimestamp   = getCurrentFileTimestamp();
            String iStartedAt   = getCurrentTimestamp();

            // Resolve run metadata from system properties set by TestRunner + Hooks
            String iEnvironment = System.getProperty("environment",         "—");
            String iBrowser     = System.getProperty("browser",             "—");
            String iBuildNumber = System.getProperty("bamboo.buildNumber",  "LOCAL");
            String iBuildPlan   = System.getProperty("bamboo.buildPlanKey", "—");
            String iRetryCount  = System.getProperty("retry.count",         "1");
            String iHerd        = System.getProperty("TD:HerdNumber",       "—");
            String iUsername    = System.getProperty("TD:Username",         "—");

            File iDocsDirectory = new File(iReportDocsDirectoryPath);
            if (!iDocsDirectory.exists()) { iDocsDirectory.mkdirs(); }

            String       iFileName = sanitizeFileName(pTestCaseID) + "_" + iTimestamp + ".docx";
            String       iFullPath = iDocsDirectory.getAbsolutePath() + File.separator + iFileName;
            XWPFDocument iDocument = new XWPFDocument();

            // ── TITLE ──────────────────────────────────────────────────────────────────────
            XWPFParagraph iTitlePara = iDocument.createParagraph();
            iTitlePara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun iTitleRun = iTitlePara.createRun();
            iTitleRun.setBold(true);
            iTitleRun.setFontSize(18);
            iTitleRun.setFontFamily("Calibri");
            iTitleRun.setText("BISS Automation — Test Execution Report");
            iTitleRun.addBreak();

            // ── SUBTITLE ───────────────────────────────────────────────────────────────────
            XWPFParagraph iSubPara = iDocument.createParagraph();
            iSubPara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun iSubRun = iSubPara.createRun();
            iSubRun.setBold(true);
            iSubRun.setFontSize(13);
            iSubRun.setFontFamily("Calibri");
            iSubRun.setText(pTestCaseID + "  —  " + iDescription);
            iSubRun.addBreak();
            iSubRun.addBreak();

            // ── METADATA TABLE ─────────────────────────────────────────────────────────────
            String[][] iMetaRows = {
                    { "Test Case ID",  pTestCaseID  },
                    { "Description",   iDescription },
                    { "Environment",   iEnvironment },
                    { "Browser",       iBrowser     },
                    { "Herd Number",   iHerd        },
                    { "Username",      iUsername    },
                    { "Build Number",  iBuildNumber },
                    { "Build Plan",    iBuildPlan   },
                    { "Retry Count",   iRetryCount  },
                    { "Started At",    iStartedAt   }
            };

            org.apache.poi.xwpf.usermodel.XWPFTable iMetaTable = iDocument.createTable(iMetaRows.length, 2);
            iMetaTable.setWidth("80%");

            for (int i = 0; i < iMetaRows.length; i++)
            {
                org.apache.poi.xwpf.usermodel.XWPFTableRow iRow = iMetaTable.getRow(i);

                // Label cell — grey background, bold
                org.apache.poi.xwpf.usermodel.XWPFTableCell iLabel = iRow.getCell(0);
                iLabel.setColor("D9D9D9");
                XWPFRun iLabelRun = iLabel.getParagraphArray(0).createRun();
                iLabelRun.setBold(true);
                iLabelRun.setFontSize(10);
                iLabelRun.setFontFamily("Calibri");
                iLabelRun.setText(iMetaRows[i][0]);

                // Value cell — white background
                XWPFRun iValueRun = iRow.getCell(1).getParagraphArray(0).createRun();
                iValueRun.setFontSize(10);
                iValueRun.setFontFamily("Calibri");
                iValueRun.setText(iMetaRows[i][1]);
            }

            // ── SPACER + SECTION HEADING ───────────────────────────────────────────────────
            iDocument.createParagraph().createRun().addBreak();
            XWPFRun iSectionRun = iDocument.createParagraph().createRun();
            iSectionRun.setBold(true);
            iSectionRun.setFontSize(12);
            iSectionRun.setFontFamily("Calibri");
            iSectionRun.setText("Execution Log");
            iSectionRun.addBreak();

            // Initial write — creates the file on disk
            try (FileOutputStream iOut = new FileOutputStream(iFullPath))
            {
                iDocument.write(iOut);
            }

            log.info("[" + getCurrentTimestamp() + "] Word report created : " + iFullPath);
            return new Object[]{iDocument, iFullPath};
        }
        catch (Exception iException)
        {
            log.severe("[" + getCurrentTimestamp() + "] Failed to create Word report : " + iException.getMessage());
            throw new RuntimeException("Failed to create Word report : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : captureStepScreenshot
    // Description   : On-demand step screenshot — delegates to ScreenshotManager.captureAndEmbed().
    //                 Called via Hooks.captureStep("label") from any step definition.
    // Parameters    : pDocument  (XWPFDocument) - the in-memory Word doc (Hooks.iDocument)
    //                 pDocPath   (String)        - report file path (used for logging only)
    //                 pStepLabel (String)        - descriptive caption shown in the Word report
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void captureStepScreenshot(XWPFDocument pDocument, String pDocPath, String pStepLabel)
    {
        utilities.ScreenshotManager.captureAndEmbed(iDriverHolder.get(), pDocument, pStepLabel);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : addScreenshotToReport
    // Description   : Failure screenshot — delegates to ScreenshotManager.captureForFailure().
    //                 Returns the PNG file path so Hooks can pass it to ReportManager.
    // Parameters    : pDocument   (XWPFDocument) - the in-memory Word doc (Hooks.iDocument)
    //                 pDocPath    (String)        - report file path (used for logging only)
    //                 pTestCaseID (String)        - currently executing test case ID
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String addScreenshotToReport(XWPFDocument pDocument, String pDocPath, String pTestCaseID)
    {
        return utilities.ScreenshotManager.captureForFailure(iDriverHolder.get(), pDocument, pTestCaseID);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : finalizeWordReport
    // Description   : Appends the full step execution log table then writes the document to disk.
    //                 This is the single disk-write point for the entire report — called once at end of test.
    // Parameters    : pDocument (XWPFDocument) - active in-memory Word document object
    //                 pDocPath  (String)        - report full file path
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026 | Updated: 26-03-2026 (step log table added before save)
    // ***************************************************************************************************************************************************************************************
    public static void finalizeWordReport(XWPFDocument pDocument, String pDocPath)
    {
        try
        {
            if (pDocument == null)
            {
                log.warning("[" + getCurrentTimestamp() + "] Word document is null. Finalize step skipped.");
                return;
            }

            if (pDocPath == null || pDocPath.trim().isEmpty())
            {
                throw new RuntimeException("Word report path is blank. Cannot finalize report.");
            }

            // ── STEP LOG TABLE ─────────────────────────────────────────────────────────────
            if (!iStepLog.isEmpty())
            {
                // Section heading
                XWPFRun iHeadRun = pDocument.createParagraph().createRun();
                iHeadRun.setBold(true);
                iHeadRun.setFontSize(12);
                iHeadRun.setFontFamily("Calibri");
                iHeadRun.setText("Step Execution Log  (" + iStepLog.size() + " steps)");
                iHeadRun.addBreak();

                // Table: header row + one data row per step
                org.apache.poi.xwpf.usermodel.XWPFTable iTable =
                        pDocument.createTable(iStepLog.size() + 1, 6);
                iTable.setWidth("100%");

                // ── Header row — dark navy ─────────────────────────────────────────────────
                String[] iHeaders = { "#", "Action", "Element (locator)", "Value", "Status", "Duration" };
                org.apache.poi.xwpf.usermodel.XWPFTableRow iHeaderRow = iTable.getRow(0);
                for (int c = 0; c < iHeaders.length; c++)
                {
                    org.apache.poi.xwpf.usermodel.XWPFTableCell iCell = iHeaderRow.getCell(c);
                    iCell.setColor("1F4E79");
                    XWPFRun iRun = iCell.getParagraphArray(0).createRun();
                    iRun.setBold(true);
                    iRun.setFontSize(9);
                    iRun.setFontFamily("Calibri");
                    iRun.setColor("FFFFFF");
                    iRun.setText(iHeaders[c]);
                }

                // ── Data rows — light green for PASS, light red for FAIL ──────────────────
                for (int r = 0; r < iStepLog.size(); r++)
                {
                    StepLogEntry iEntry  = iStepLog.get(r);
                    boolean      iPassed = "PASS".equals(iEntry.iStatus);
                    String       iColor  = iPassed ? "E2EFDA" : "FCE4D6";

                    org.apache.poi.xwpf.usermodel.XWPFTableRow iRow = iTable.getRow(r + 1);

                    String[] iValues = {
                            String.valueOf(iEntry.iStepNumber),
                            iEntry.iAction,
                            iEntry.iLocator,
                            iEntry.iValue,
                            iEntry.iStatus,
                            iEntry.iDurationMs + " ms"
                                    + ((!iPassed && !iEntry.iFailReason.isEmpty()) ? "  —  " + iEntry.iFailReason : "")
                    };

                    for (int c = 0; c < iValues.length; c++)
                    {
                        org.apache.poi.xwpf.usermodel.XWPFTableCell iCell = iRow.getCell(c);
                        iCell.setColor(iColor);
                        XWPFRun iRun = iCell.getParagraphArray(0).createRun();
                        iRun.setFontSize(8);
                        iRun.setFontFamily("Calibri");
                        iRun.setBold(c == 4);
                        if (c == 4) { iRun.setColor(iPassed ? "375623" : "9C0006"); }
                        iRun.setText(iValues[c]);
                    }
                }

                pDocument.createParagraph().createRun().addBreak();
            }
            else
            {
                XWPFRun iNoSteps = pDocument.createParagraph().createRun();
                iNoSteps.setFontSize(9);
                iNoSteps.setFontFamily("Calibri");
                iNoSteps.setColor("808080");
                iNoSteps.setText("(No step log entries recorded for this test case.)");
                iNoSteps.addBreak();
            }

            // ── END TIMESTAMP ──────────────────────────────────────────────────────────────
            XWPFRun iEndRun = pDocument.createParagraph().createRun();
            iEndRun.setBold(true);
            iEndRun.setFontSize(9);
            iEndRun.setFontFamily("Calibri");
            iEndRun.setText("Execution completed at: " + getCurrentTimestamp());

            // ── SINGLE DISK WRITE ──────────────────────────────────────────────────────────
            try (FileOutputStream iOut = new FileOutputStream(pDocPath))
            {
                pDocument.write(iOut);
            }

            pDocument.close();

            log.info("[" + getCurrentTimestamp() + "] Word report finalized : " + pDocPath);
        }
        catch (Exception iException)
        {
            log.severe("[" + getCurrentTimestamp() + "] Failed to finalize Word report : " + iException.getMessage());
            throw new RuntimeException("Failed to finalize Word report : " + iException.getMessage(), iException);
        }
    }


    // ===============================================================================================================================
    // SECTION 12 : Test Data Resolution
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : getTestDataValue
    // Description   : Returns the value from the currently loaded test data row for the supplied column name
    // Parameters    : pColumnName (String) - column name from the TestData sheet
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Current test data row must already be loaded via ExcelUtilities before calling this method
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getTestDataValue(String pColumnName)
    {
        return ExcelUtilities.getCurrentTestDataValue(pColumnName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : resolveValue
    // Description   : Resolves the actual runtime value. If input starts with "TD:" the column value is fetched from
    //                 the current test data row via ExcelUtilities. Otherwise returns the raw value as-is.
    // Parameters    : pValue (String) - direct value or TD:ColumnName syntax
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : When using TD: syntax, current test data row must already be loaded via ExcelUtilities
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


    // ===============================================================================================================================
    // SECTION 13 : ExecutionControl.xlsx — Description Cache & Reader
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadDescriptionCache
    // Description   : Reads the full ExecutionControl.xlsx once into an in-memory map at framework startup.
    //                 Thread-safe via double-checked locking. Subsequent calls are no-ops.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : ExecutionControl.xlsx must exist and contain TestCase_ID and Description columns
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void loadDescriptionCache()
    {
        if (iDescriptionCacheLoaded)
        {
            return;
        }

        synchronized (iDescriptionCache)
        {
            if (iDescriptionCacheLoaded)
            {
                return;
            }

            try (FileInputStream iFileInputStream = new FileInputStream(iExecutionControlFilePath);
                 Workbook iWorkbook = WorkbookFactory.create(iFileInputStream))
            {
                Sheet iSheet = iWorkbook.getSheetAt(0);

                if (iSheet == null)
                {
                    throw new RuntimeException("Sheet at index 0 is not present in : " + iExecutionControlFilePath);
                }

                Row iHeaderRow = iSheet.getRow(0);

                if (iHeaderRow == null)
                {
                    throw new RuntimeException("Header row is missing in ExecutionControl.xlsx.");
                }

                int iTestCaseIDColumnIndex  = -1;
                int iDescriptionColumnIndex = -1;

                for (int iColIdx = 0; iColIdx < iHeaderRow.getLastCellNum(); iColIdx++)
                {
                    String iColumnName = getCellValue(iHeaderRow, iColIdx).trim();

                    if (iColumnName.equalsIgnoreCase("TestCase_ID"))
                    {
                        iTestCaseIDColumnIndex = iColIdx;
                    }
                    else if (iColumnName.equalsIgnoreCase("Description"))
                    {
                        iDescriptionColumnIndex = iColIdx;
                    }
                }

                if (iTestCaseIDColumnIndex == -1 || iDescriptionColumnIndex == -1)
                {
                    throw new RuntimeException("Required columns TestCase_ID and/or Description not found in ExecutionControl.xlsx.");
                }

                for (int iRowIdx = 1; iRowIdx <= iSheet.getLastRowNum(); iRowIdx++)
                {
                    Row iRow = iSheet.getRow(iRowIdx);

                    if (iRow == null)
                    {
                        continue;
                    }

                    String iTestCaseID  = getCellValue(iRow, iTestCaseIDColumnIndex).trim();
                    String iDescription = getCellValue(iRow, iDescriptionColumnIndex).trim();

                    if (!iTestCaseID.isEmpty())
                    {
                        iDescriptionCache.put(iTestCaseID.toUpperCase(), iDescription);
                    }
                }

                iDescriptionCacheLoaded = true;
                log.info("[" + getCurrentTimestamp() + "] ExecutionControl description cache loaded. Total entries: " + iDescriptionCache.size());
            }
            catch (Exception iException)
            {
                throw new RuntimeException("Failed to load ExecutionControl.xlsx into description cache : " + iException.getMessage(), iException);
            }
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getDescriptionFromExecutionControl
    // Description   : Returns the Description for the supplied TestCase_ID from the in-memory cache.
    //                 Triggers cache load automatically on first call if not already loaded.
    // Parameters    : pTestCaseID (String) - currently executing test case ID
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : ExecutionControl.xlsx must be accessible at framework startup
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String getDescriptionFromExecutionControl(String pTestCaseID)
    {
        if (!iDescriptionCacheLoaded)
        {
            loadDescriptionCache();
        }

        String iDescription = iDescriptionCache.get(pTestCaseID.trim().toUpperCase());

        if (iDescription == null)
        {
            throw new RuntimeException("No description found in ExecutionControl.xlsx for TestCase_ID : '" + pTestCaseID + "'.");
        }

        return iDescription;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCellValue
    // Description   : Safely returns a cell value as a String from the given row and column index using Apache POI DataFormatter
    // Parameters    : pRow         (Row) - Apache POI row object
    //                 pColumnIndex (int) - target column index (zero-based)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
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

    public static void waitForAngular() {
        WebDriver driver = getDriver();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        getWait().until(d -> js.executeScript("return document.readyState").equals("complete"));

        js.executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                        "if (window.getAllAngularTestabilities) {" +
                        "  var testabilities = window.getAllAngularTestabilities();" +
                        "  var count = testabilities.length;" +
                        "  testabilities.forEach(function (testability) {" +
                        "    testability.whenStable(function () {" +
                        "      count--;" +
                        "      if (count === 0) callback(); });" +
                        "  });" +
                        "} else { callback(); }");
    }

    public static WebElement waitForVisibilityOfElement(By locator, int timeInSec) {
        WebDriver driver = getDriver();

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeInSec))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    // ===============================================================================================================================
    // SECTION 14 : StepLogEntry — Inner class (Phase 1)
    //
    // Data model for one row in the step execution log table written by finalizeWordReport().
    // One instance is created per iAction() call via appendToStepLog().
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Inner Class : StepLogEntry
    // Author      : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created: 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static class StepLogEntry
    {
        public final int    iStepNumber;
        public final String iAction;
        public final String iLocator;
        public final String iValue;
        public final String iStatus;       // PASS | FAIL
        public final long   iDurationMs;
        public final String iTimestamp;
        public final String iFailReason;   // blank on PASS rows

        public StepLogEntry(int pStep, String pAction, String pLocator,
                            String pValue, String pStatus, long pDurationMs, String pFailReason)
        {
            this.iStepNumber = pStep;
            this.iAction     = pAction     == null ? "" : pAction.trim();
            this.iLocator    = pLocator    == null ? "" : cap(pLocator.trim(), 60);
            this.iValue      = pValue      == null ? "" : pValue.trim();
            this.iStatus     = pStatus     == null ? "FAIL" : pStatus.trim().toUpperCase();
            this.iDurationMs = pDurationMs;
            this.iTimestamp  = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            this.iFailReason = pFailReason == null ? "" : cap(pFailReason.trim(), 120);
        }

        private static String cap(String s, int max)
        {
            return (s == null || s.length() <= max) ? s : s.substring(0, max) + "...";
        }
    }
}