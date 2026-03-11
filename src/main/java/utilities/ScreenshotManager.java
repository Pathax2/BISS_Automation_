// ===================================================================================================================================
// File          : ScreenshotManager.java
// Package       : utilities
// Description   : Dedicated screenshot utility for the BISS automation framework.
//                 Decouples all screenshot logic from CommonFunctions.
//                 Supports:
//                   • Viewport screenshot  — current visible browser area (default, fast)
//                   • Full-page screenshot — stitches entire scrollable page via JS
//                   • Element screenshot   — crops to the bounding box of a specific WebElement
//                   • Auto-cleanup         — deletes screenshots older than N days on startup
//
// Folder        : src/main/java/utilities/ScreenshotManager.java
//
// CommonFunctions.takeScreenshot() delegates to ScreenshotManager.captureViewport().
// Hooks.afterScenarioExecution() can call ScreenshotManager.captureFullPage() for richer failure evidence.
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ScreenshotManager
{
    private static final Logger log = Logger.getLogger(ScreenshotManager.class.getName());

    private static final String iScreenshotsDirectory =
            ConfigManager.getOrDefault("report.screenshots.path", "Test_Report/screenshots");

    private static final int iRetentionDays =
            ConfigManager.getInt("screenshot.retention.days", 30);

    private static final DateTimeFormatter iTimestampFormatter =
            DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss_SSS");

    // -------------------------------------------------------------------------------------------------------------------------------
    // Run cleanup once at class load time — removes stale screenshots on every framework startup
    // -------------------------------------------------------------------------------------------------------------------------------
    static
    {
        cleanupOldScreenshots();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor
    // -------------------------------------------------------------------------------------------------------------------------------
    private ScreenshotManager() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : captureViewport
    // Description   : Captures the current visible browser viewport and saves as PNG.
    //                 This is the standard screenshot used for step logging and failure evidence.
    // Parameters    : pDriver   (WebDriver) - active driver on current thread
    //                 pStepName (String)    - logical step name used in file naming
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
    //                 Provides complete evidence of page state at point of failure.
    // Parameters    : pDriver   (WebDriver) - active driver on current thread
    //                 pStepName (String)    - logical step name used in file naming
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Browser must support JavaScript execution (Chrome, Firefox, Edge all do)
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String captureFullPage(WebDriver pDriver, String pStepName)
    {
        JavascriptExecutor iJs = (JavascriptExecutor) pDriver;

        // Store original window size
        long iOrigWidth  = (Long) iJs.executeScript("return document.body.scrollWidth;");
        long iOrigHeight = (Long) iJs.executeScript("return document.body.scrollHeight;");

        try
        {
            if (pDriver == null)
            {
                throw new RuntimeException("Driver is null. Cannot capture full-page screenshot.");
            }

            ensureDirectoryExists();

            // Expand to full document size
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
            // Always restore scroll behaviour
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
    //                 Useful for capturing a specific form field, error message, or UI component
    //                 without noise from the surrounding page.
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

            // Take full viewport screenshot first
            File iFullScreenshot = ((TakesScreenshot) pDriver).getScreenshotAs(OutputType.FILE);
            BufferedImage iFullImage = ImageIO.read(iFullScreenshot);

            // Get element bounding box
            Point  iLocation  = pElement.getLocation();
            int    iElemX     = iLocation.getX();
            int    iElemY     = iLocation.getY();
            int    iElemWidth = pElement.getSize().getWidth();
            int    iElemHeight= pElement.getSize().getHeight();

            // Guard against out-of-bounds crops
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

    // ***************************************************************************************************************************************************************************************
    // Function Name : cleanupOldScreenshots
    // Description   : Deletes PNG files in the screenshots directory that are older than the
    //                 configured retention period (screenshot.retention.days).
    //                 Called once at class initialisation. Set retention.days=0 to disable.
    // Parameters    : None
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
            if (!iDir.exists() || !iDir.isDirectory())
            {
                return;
            }

            LocalDate iCutoff    = LocalDate.now().minusDays(iRetentionDays);
            File[]    iFiles     = iDir.listFiles((dir, name) -> name.endsWith(".png"));
            int       iDeleted   = 0;

            if (iFiles == null) { return; }

            for (File iFile : iFiles)
            {
                long   iLastModified = iFile.lastModified();
                LocalDate iFileDate  = new java.util.Date(iLastModified)
                        .toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();

                if (iFileDate.isBefore(iCutoff))
                {
                    if (iFile.delete()) { iDeleted++; }
                }
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

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------------------------------------------------------------

    private static String buildFilePath(String pStepName, String pType)
    {
        String iSafeName  = sanitize(pStepName);
        String iTimestamp = LocalDateTime.now().format(iTimestampFormatter);
        return iScreenshotsDirectory + File.separator + iSafeName + "_" + pType + "_" + iTimestamp + ".png";
    }

    private static String sanitize(String pInput)
    {
        if (pInput == null || pInput.trim().isEmpty()) { return "Unnamed"; }
        return pInput.trim().replaceAll("[\\\\/:*?\"<>|\\s]+", "_");
    }

    private static void ensureDirectoryExists()
    {
        File iDir = new File(iScreenshotsDirectory);
        if (!iDir.exists()) { iDir.mkdirs(); }
    }
}