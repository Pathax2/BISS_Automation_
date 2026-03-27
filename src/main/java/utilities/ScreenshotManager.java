// ===================================================================================================================================
// File          : ScreenshotManager.java
// Package       : utilities
// Description   : Central visual evidence manager for the BISS automation framework.
//
//                 Owns ALL screenshot capture and Word document image embedding.
//                 CommonFunctions delegates here — no screenshot logic lives there anymore.
//
//                 Responsibilities:
//                   CAPTURE  — viewport, full-page, element crop (pre-existing)
//                   EMBED    — captureAndEmbed()    on-demand step screenshot into Word doc
//                              captureForFailure()  failure screenshot into Word doc
//                   DIVIDER  — appendScenarioDivider()  green/red scenario header in Word doc
//                   CLEANUP  — auto-delete screenshots older than retention period (pre-existing)
//
//                 Call surface for the rest of the framework:
//                   Hooks.captureStep("label")                  → captureAndEmbed()
//                   Hooks.@After on failure                     → captureForFailure()
//                   Hooks.@After always                         → appendScenarioDivider()
//                   CommonFunctions.takeScreenshot(stepName)    → captureViewport()
//
// Folder        : src/main/java/utilities/ScreenshotManager.java
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// Updated       : 26-03-2026  — Phase 1 Word report embedding moved here from CommonFunctions
// ===================================================================================================================================

package utilities;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ScreenshotManager
{
    private static final Logger log = Logger.getLogger(ScreenshotManager.class.getName());

    // -------------------------------------------------------------------------------------------------------------------------------
    // Configuration — read from application.properties via ConfigManager
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String iScreenshotsDirectory =
            ConfigManager.getOrDefault("report.screenshots.path", "Test_Report/screenshots");

    private static final int iRetentionDays =
            ConfigManager.getInt("screenshot.retention.days", 30);

    private static final DateTimeFormatter iTimestampFormatter =
            DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss_SSS");

    private static final DateTimeFormatter iDisplayFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");

    // -------------------------------------------------------------------------------------------------------------------------------
    // Run cleanup once at class load time — removes stale screenshots on every framework startup
    // -------------------------------------------------------------------------------------------------------------------------------
    static
    {
        cleanupOldScreenshots();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor — static utility class, not instantiable
    // -------------------------------------------------------------------------------------------------------------------------------
    private ScreenshotManager() {}


    // ===============================================================================================================================
    // SECTION 1 : CAPTURE — take a screenshot and save to disk, return the file path
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : captureViewport
    // Description   : Captures the current visible browser viewport and saves as PNG.
    //                 This is the standard screenshot used by CommonFunctions.takeScreenshot().
    // Parameters    : pDriver   (WebDriver) - active driver on current thread
    //                 pStepName (String)    - logical step name used in the file name
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Browser must be launched and driver must be active
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String captureViewport(WebDriver pDriver, String pStepName)
    {
        try
        {
            if (pDriver == null)
            {
                throw new RuntimeException("Driver is null. Cannot capture viewport screenshot.");
            }

            ensureDirectoryExists();

            String iFilePath = buildFilePath(pStepName, "viewport");
            File   iSource   = ((TakesScreenshot) pDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(iSource.toPath(), new File(iFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("[ScreenshotManager] Viewport screenshot : " + iFilePath);
            return iFilePath;
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[ScreenshotManager] Viewport capture failed : " + iException.getMessage(), iException);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : captureFullPage
    // Description   : Captures the full scrollable page by temporarily expanding the browser viewport
    //                 to the document height using JavaScript, taking the screenshot, then restoring.
    // Parameters    : pDriver   (WebDriver) - active driver on current thread
    //                 pStepName (String)    - logical step name used in file naming
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Browser must support JavaScript execution
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String captureFullPage(WebDriver pDriver, String pStepName)
    {
        JavascriptExecutor iJs = (JavascriptExecutor) pDriver;

        long iOrigWidth  = (Long) iJs.executeScript("return document.body.scrollWidth;");
        long iOrigHeight = (Long) iJs.executeScript("return document.body.scrollHeight;");

        try
        {
            if (pDriver == null)
            {
                throw new RuntimeException("Driver is null. Cannot capture full-page screenshot.");
            }

            ensureDirectoryExists();

            iJs.executeScript("document.body.style.overflow = 'visible';");
            pDriver.manage().window().setSize(
                    new org.openqa.selenium.Dimension((int) iOrigWidth, (int) iOrigHeight));

            String iFilePath = buildFilePath(pStepName, "fullpage");
            File   iSource   = ((TakesScreenshot) pDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(iSource.toPath(), new File(iFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("[ScreenshotManager] Full-page screenshot : " + iFilePath);
            return iFilePath;
        }
        catch (Exception iException)
        {
            log.warning("[ScreenshotManager] Full-page capture failed, falling back to viewport : " + iException.getMessage());
            return captureViewport(pDriver, pStepName);
        }
        finally
        {
            try
            {
                iJs.executeScript("document.body.style.overflow = '';");
                pDriver.manage().window().maximize();
            }
            catch (Exception ignored) {}
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : captureElement
    // Description   : Crops the viewport screenshot to the bounding box of the specified WebElement.
    // Parameters    : pDriver   (WebDriver)  - active driver on current thread
    //                 pElement  (WebElement) - the element to crop to
    //                 pStepName (String)     - logical step name used in file naming
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Element must be visible in the current viewport
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String captureElement(WebDriver pDriver, WebElement pElement, String pStepName)
    {
        try
        {
            if (pDriver == null || pElement == null)
            {
                throw new RuntimeException("Driver or element is null. Cannot capture element screenshot.");
            }

            ensureDirectoryExists();

            File          iFullScreenshot = ((TakesScreenshot) pDriver).getScreenshotAs(OutputType.FILE);
            BufferedImage iFullImage      = ImageIO.read(iFullScreenshot);

            Point iLocation   = pElement.getLocation();
            int   iElemX      = iLocation.getX();
            int   iElemY      = iLocation.getY();
            int   iElemWidth  = pElement.getSize().getWidth();
            int   iElemHeight = pElement.getSize().getHeight();

            int iImgWidth  = iFullImage.getWidth();
            int iImgHeight = iFullImage.getHeight();
            int iCropX     = Math.max(0, Math.min(iElemX,     iImgWidth  - 1));
            int iCropY     = Math.max(0, Math.min(iElemY,     iImgHeight - 1));
            int iCropW     = Math.min(iElemWidth,  iImgWidth  - iCropX);
            int iCropH     = Math.min(iElemHeight, iImgHeight - iCropY);

            BufferedImage iCroppedImage = iFullImage.getSubimage(iCropX, iCropY, iCropW, iCropH);

            String iFilePath = buildFilePath(pStepName, "element");
            ImageIO.write(iCroppedImage, "PNG", new File(iFilePath));

            log.info("[ScreenshotManager] Element screenshot : " + iFilePath);
            return iFilePath;
        }
        catch (IOException iException)
        {
            log.warning("[ScreenshotManager] Element crop failed, falling back to viewport : " + iException.getMessage());
            return captureViewport(pDriver, pStepName);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[ScreenshotManager] Element capture failed : " + iException.getMessage(), iException);
        }
    }


    // ===============================================================================================================================
    // SECTION 2 : EMBED — capture screenshot and write it straight into the Word doc
    //
    // These are the methods that moved here from CommonFunctions in Phase 1.
    // CommonFunctions now calls these instead of duplicating the logic.
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : captureAndEmbed
    // Description   : ON-DEMAND screenshot — called from Hooks.captureStep("your label") by step definitions.
    //                 Takes a viewport screenshot right now and embeds it into the in-memory Word doc
    //                 with the supplied label as the caption.
    //
    //                 Non-fatal: if anything fails the warning is logged and the test continues.
    //                 A missed screenshot must never abort a passing test step.
    //
    //                 Caption style: navy text  "STEP CAPTURE: <label>  |  <timestamp>"
    //                 Image size   : 450 x 300 pts (fits A4 portrait with margins)
    //                 Spacer       : one blank line appended after each image for readability
    //
    // Parameters    : pDriver    (WebDriver)    - active driver on current thread
    //                 pDocument  (XWPFDocument) - the in-memory Word doc (Hooks.iDocument)
    //                 pStepLabel (String)        - descriptive caption shown in the Word report
    //                                             e.g. "Farmer dashboard loaded"
    //                                             e.g. "Parcel A1190600017 added — Land Details"
    //
    // Called from   : ScreenshotManager .captureStepScreenshot()
    //                 which is called by Hooks.captureStep()
    //                 which is called by step definitions: Hooks.captureStep("label")
    //
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void captureAndEmbed(WebDriver pDriver, XWPFDocument pDocument, String pStepLabel)
    {
        try
        {
            // Guard: driver must be active
            if (pDriver == null)
            {
                log.warning("[ScreenshotManager] captureAndEmbed — driver is null, skipping. Label: " + pStepLabel);
                return;
            }

            // Guard: Word document must be open
            if (pDocument == null)
            {
                log.warning("[ScreenshotManager] captureAndEmbed — document is null, skipping. Label: " + pStepLabel);
                return;
            }

            // ── 1. Capture screenshot to disk ───────────────────────────────────────────────
            String iSafeLabel   = sanitize(pStepLabel == null || pStepLabel.isBlank() ? "Step" : pStepLabel);
            String iTimestamp   = LocalDateTime.now().format(iTimestampFormatter);
            String iDisplayTime = LocalDateTime.now().format(iDisplayFormatter);
            String iPngFileName = iSafeLabel + "_step_" + iTimestamp + ".png";
            String iPngPath     = buildAbsolutePath(iPngFileName);

            ensureDirectoryExists();

            File iSourceFile = ((TakesScreenshot) pDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(iSourceFile.toPath(), new File(iPngPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("[ScreenshotManager] Step screenshot saved : " + iPngPath);

            // ── 2. Write caption into Word doc ──────────────────────────────────────────────
            // Navy text so step captures are visually distinct from red failure captions
            XWPFParagraph iCaptionParagraph = pDocument.createParagraph();
            XWPFRun       iCaptionRun       = iCaptionParagraph.createRun();
            iCaptionRun.setBold(true);
            iCaptionRun.setFontSize(9);
            iCaptionRun.setFontFamily("Calibri");
            iCaptionRun.setColor("1F4E79");
            iCaptionRun.setText("  STEP CAPTURE: " + pStepLabel + "   |   " + iDisplayTime);
            iCaptionRun.addBreak();

            // ── 3. Embed image ──────────────────────────────────────────────────────────────
            embedImage(pDocument, iPngPath, iPngFileName);

            // ── 4. Blank spacer line for readability ────────────────────────────────────────
            pDocument.createParagraph().createRun().addBreak();

            log.info("[ScreenshotManager] Step screenshot embedded. Label: " + pStepLabel);
        }
        catch (Exception iException)
        {
            // Non-fatal — a missed screenshot never fails the test
            log.warning("[ScreenshotManager] captureAndEmbed failed for [" + pStepLabel + "] : " + iException.getMessage());
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : captureForFailure
    // Description   : FAILURE screenshot — called from Hooks @After when a scenario has failed.
    //                 Takes a viewport screenshot and embeds it with a red failure caption.
    //                 This replaces the old CommonFunctions.addScreenshotToReport().
    //
    //                 Caption style: red bold text  "FAILURE: <testCaseID>  |  <timestamp>"
    //                 Image size   : 450 x 300 pts
    //
    // Parameters    : pDriver     (WebDriver)    - active driver on current thread
    //                 pDocument   (XWPFDocument) - the in-memory Word doc (Hooks.iDocument)
    //                 pTestCaseID (String)        - the current test case ID (e.g. TC_03)
    //
    // Returns       : String — the absolute path to the screenshot PNG on disk
    //                          (used by Hooks to record iLastScreenshotPath for ReportManager)
    //
    // Called from   : CommonFunctions.addScreenshotToReport()
    //                 which is called by Hooks.@After on scenario failure
    //
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String captureForFailure(WebDriver pDriver, XWPFDocument pDocument, String pTestCaseID)
    {
        String iPngPath = "";

        try
        {
            if (pDriver == null)
            {
                log.warning("[ScreenshotManager] captureForFailure — driver is null, skipping.");
                return iPngPath;
            }

            ensureDirectoryExists();

            String iTimestamp   = LocalDateTime.now().format(iTimestampFormatter);
            String iDisplayTime = LocalDateTime.now().format(iDisplayFormatter);
            String iPngFileName = sanitize(pTestCaseID) + "_FAIL_" + iTimestamp + ".png";
            iPngPath            = buildAbsolutePath(iPngFileName);

            // ── 1. Capture screenshot ───────────────────────────────────────────────────────
            File iSourceFile = ((TakesScreenshot) pDriver).getScreenshotAs(OutputType.FILE);
            Files.copy(iSourceFile.toPath(), new File(iPngPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("[ScreenshotManager] Failure screenshot saved : " + iPngPath);

            // ── 2. Embed into Word doc if available ─────────────────────────────────────────
            if (pDocument != null)
            {
                // Red caption — visually distinct from the navy step-capture captions
                XWPFParagraph iCaptionParagraph = pDocument.createParagraph();
                XWPFRun       iCaptionRun       = iCaptionParagraph.createRun();
                iCaptionRun.setBold(true);
                iCaptionRun.setFontSize(9);
                iCaptionRun.setFontFamily("Calibri");
                iCaptionRun.setColor("C00000");   // dark red
                iCaptionRun.setText("  FAILURE: " + pTestCaseID + "   |   " + iDisplayTime);
                iCaptionRun.addBreak();

                embedImage(pDocument, iPngPath, iPngFileName);

                pDocument.createParagraph().createRun().addBreak();

                log.info("[ScreenshotManager] Failure screenshot embedded in Word report.");
            }
        }
        catch (Exception iException)
        {
            log.severe("[ScreenshotManager] captureForFailure failed : " + iException.getMessage());
        }

        return iPngPath;
    }


    // ===============================================================================================================================
    // SECTION 3 : WORD DOC STRUCTURE — scenario divider
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : appendScenarioDivider
    // Description   : Writes a coloured scenario header block into the Word report.
    //                 Called from Hooks @After for every scenario — pass or fail.
    //
    //                 PASSED scenarios get a green header (hex 70AD47, white text).
    //                 FAILED scenarios get a red header (hex FF0000, white text).
    //
    //                 Opening the Word doc, you can scan the coloured headers top-to-bottom
    //                 and immediately see which scenarios passed and which failed — no need to
    //                 scroll through screenshots to find the failures.
    //
    // Parameters    : pDocument     (XWPFDocument) - the in-memory Word doc (Hooks.iDocument)
    //                 pScenarioName (String)        - the Cucumber scenario name
    //                 pPassed       (boolean)       - true = PASSED header, false = FAILED header
    //                 pDurationMs   (long)          - scenario duration in milliseconds
    //
    // Called from   : ScreenshotManager.appendScenarioDivider()
    //                 which is called by Hooks.@After
    //
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void appendScenarioDivider(XWPFDocument pDocument, String pScenarioName,
                                             boolean pPassed, long pDurationMs)
    {
        try
        {
            if (pDocument == null) { return; }

            String iStatus      = pPassed ? "PASSED" : "FAILED";
            String iFillColor   = pPassed ? "70AD47" : "FF0000";  // green : red
            String iTextColor   = "FFFFFF";                       // white text on both
            long   iSecs        = (pDurationMs / 1000) % 60;
            long   iMins        = pDurationMs / (1000 * 60);
            String iDuration    = iMins > 0 ? iMins + "m " + iSecs + "s" : iSecs + "s";
            String iDisplayTime = LocalDateTime.now().format(iDisplayFormatter);

            // Spacer before the divider
            pDocument.createParagraph().createRun().addBreak();

            // Coloured paragraph block — applies paragraph-level background shading
            XWPFParagraph iDivParagraph = pDocument.createParagraph();
            iDivParagraph.setAlignment(ParagraphAlignment.LEFT);

            // Apply shading to the paragraph container
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr iPPr =
                    iDivParagraph.getCTP().addNewPPr();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd iShd =
                    iPPr.addNewShd();
            iShd.setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd.CLEAR);
            iShd.setColor("auto");
            iShd.setFill(iFillColor);

            // Text run inside the coloured paragraph
            XWPFRun iDivRun = iDivParagraph.createRun();
            iDivRun.setBold(true);
            iDivRun.setFontSize(11);
            iDivRun.setFontFamily("Calibri");
            iDivRun.setColor(iTextColor);
            iDivRun.setText("  " + iStatus
                    + "  |  " + pScenarioName
                    + "  |  Duration: " + iDuration
                    + "  |  " + iDisplayTime);

            // Spacer after the divider
            pDocument.createParagraph().createRun().addBreak();

            log.info("[ScreenshotManager] Scenario divider added : " + pScenarioName + " [" + iStatus + "]");
        }
        catch (Exception iException)
        {
            // Non-fatal — a missing divider never fails the test
            log.warning("[ScreenshotManager] appendScenarioDivider failed : " + iException.getMessage());
        }
    }


    // ===============================================================================================================================
    // SECTION 4 : CLEANUP — auto-delete old screenshots
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : cleanupOldScreenshots
    // Description   : Deletes PNG files older than the configured retention period.
    //                 Runs once at class initialisation. Set screenshot.retention.days=0 to disable.
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void cleanupOldScreenshots()
    {
        if (iRetentionDays <= 0)
        {
            log.info("[ScreenshotManager] Screenshot cleanup disabled (retention.days=0).");
            return;
        }

        try
        {
            File iDir = new File(iScreenshotsDirectory);
            if (!iDir.exists() || !iDir.isDirectory()) { return; }

            LocalDate iCutoff  = LocalDate.now().minusDays(iRetentionDays);
            File[]    iFiles   = iDir.listFiles((dir, name) -> name.endsWith(".png"));
            int       iDeleted = 0;

            if (iFiles == null) { return; }

            for (File iFile : iFiles)
            {
                LocalDate iFileDate = new java.util.Date(iFile.lastModified())
                        .toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();

                if (iFileDate.isBefore(iCutoff) && iFile.delete()) { iDeleted++; }
            }

            if (iDeleted > 0)
            {
                log.info("[ScreenshotManager] Cleaned up " + iDeleted
                        + " screenshot(s) older than " + iRetentionDays + " days.");
            }
        }
        catch (Exception iException)
        {
            log.warning("[ScreenshotManager] Cleanup failed : " + iException.getMessage());
        }
    }


    // ===============================================================================================================================
    // SECTION 5 : PRIVATE HELPERS
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : embedImage
    // Description   : Writes a PNG file into the in-memory Word document as an inline picture.
    //                 Shared by captureAndEmbed() and captureForFailure() to avoid duplication.
    // Parameters    : pDocument   (XWPFDocument) - target Word document
    //                 pPngPath    (String)        - absolute path to the PNG file on disk
    //                 pPngFileName(String)        - file name used as the embedded image name
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void embedImage(XWPFDocument pDocument, String pPngPath, String pPngFileName)
            throws Exception
    {
        XWPFParagraph iImageParagraph = pDocument.createParagraph();
        XWPFRun       iImageRun       = iImageParagraph.createRun();

        try (InputStream iInputStream = new FileInputStream(pPngPath))
        {
            iImageRun.addPicture(
                    iInputStream,
                    Document.PICTURE_TYPE_PNG,
                    pPngFileName,
                    Units.toEMU(450),   // 450 pts wide  — fits A4 portrait with margins
                    Units.toEMU(300)    // 300 pts tall
            );
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildFilePath
    // Description   : Builds the absolute file path for a screenshot using the step name and type suffix.
    // Parameters    : pStepName (String) - logical step name (sanitised before use)
    //                 pType     (String) - suffix: "viewport", "fullpage", "element"
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String buildFilePath(String pStepName, String pType)
    {
        String iSafeName  = sanitize(pStepName);
        String iTimestamp = LocalDateTime.now().format(iTimestampFormatter);
        return iScreenshotsDirectory + File.separator + iSafeName + "_" + pType + "_" + iTimestamp + ".png";
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildAbsolutePath
    // Description   : Builds an absolute path for a file already given a full filename.
    // Parameters    : pFileName (String) - the complete file name including extension
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 26-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String buildAbsolutePath(String pFileName)
    {
        return iScreenshotsDirectory + File.separator + pFileName;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : sanitize
    // Description   : Removes filesystem-unsafe characters from a file name segment.
    // Parameters    : pInput (String) - raw input string
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String sanitize(String pInput)
    {
        if (pInput == null || pInput.trim().isEmpty()) { return "Unnamed"; }
        return pInput.trim().replaceAll("[\\\\/:*?\"<>|\\s]+", "_");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : ensureDirectoryExists
    // Description   : Creates the screenshots directory if it does not already exist.
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void ensureDirectoryExists()
    {
        File iDir = new File(iScreenshotsDirectory);
        if (!iDir.exists()) { iDir.mkdirs(); }
    }
}