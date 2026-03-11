// ===================================================================================================================================
// File          : WaitStrategy.java
// Package       : utilities
// Description   : Named wait strategy enum for the BISS automation framework.
//                 Replaces raw inline ExpectedConditions calls in CommonFunctions with
//                 named, readable strategies that can be passed per-action or per-element type.
//
// Folder        : src/main/java/utilities/WaitStrategy.java
//
// Usage in iAction (pValueToEnter field supports WAIT:strategy prefix):
//   CommonFunctions.iAction("CLICK",  "XPATH", locator, "WAIT:CLICKABLE")
//   CommonFunctions.iAction("GETTEXT","XPATH", locator, "WAIT:VISIBLE")
//   CommonFunctions.iAction("CLICK",  "XPATH", locator, "WAIT:NONE")
//
// Default strategy if no WAIT: prefix supplied: VISIBLE
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public enum WaitStrategy
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // Wait until element is visible in the DOM and displayed on screen (default)
    // Use for: text fields, buttons, labels, dropdown controls
    // -------------------------------------------------------------------------------------------------------------------------------
    VISIBLE,

    // -------------------------------------------------------------------------------------------------------------------------------
    // Wait until element is both visible and enabled and ready to receive clicks
    // Use for: buttons, links, checkboxes, radio buttons
    // -------------------------------------------------------------------------------------------------------------------------------
    CLICKABLE,

    // -------------------------------------------------------------------------------------------------------------------------------
    // Wait until element is present in DOM (does not need to be visible)
    // Use for: hidden file upload inputs, off-screen elements
    // -------------------------------------------------------------------------------------------------------------------------------
    PRESENCE,

    // -------------------------------------------------------------------------------------------------------------------------------
    // Wait until element is no longer visible (disappears from screen)
    // Use for: loading spinners, overlay masks, toast messages
    // -------------------------------------------------------------------------------------------------------------------------------
    INVISIBLE,

    // -------------------------------------------------------------------------------------------------------------------------------
    // No wait — interact with element immediately
    // Use for: elements already confirmed visible by a previous step
    // -------------------------------------------------------------------------------------------------------------------------------
    NONE;

    // -------------------------------------------------------------------------------------------------------------------------------
    // Logger
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final Logger log = Logger.getLogger(WaitStrategy.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Function Name : applyAndGet
    // Description   : Applies this wait strategy to the given locator and returns the located WebElement.
    //                 INVISIBLE and NONE return null (no element reference needed for those use cases).
    // Parameters    : pWait       (WebDriverWait) - wait instance bound to current thread
    //                 pDriver     (WebDriver)     - driver instance bound to current thread
    //                 pBy         (By)            - Selenium locator
    //                 pObjectName (String)        - locator description for logging
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : pWait and pDriver must be non-null (thread must have launched browser)
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public WebElement applyAndGet(WebDriverWait pWait, WebDriver pDriver, By pBy, String pObjectName)
    {
        switch (this)
        {
            case VISIBLE:
                return pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));

            case CLICKABLE:
            {
                WebElement iElement = pWait.until(ExpectedConditions.visibilityOfElementLocated(pBy));
                pWait.until(ExpectedConditions.elementToBeClickable(iElement));
                return iElement;
            }

            case PRESENCE:
                return pWait.until(ExpectedConditions.presenceOfElementLocated(pBy));

            case INVISIBLE:
            {
                boolean iGone = pWait.until(ExpectedConditions.invisibilityOfElementLocated(pBy));
                if (!iGone)
                {
                    throw new RuntimeException("WAITINVISIBLE : Element still visible after wait. Locator : " + pObjectName);
                }
                return null;
            }

            case NONE:
            {
                // Direct find without any wait — element must already be in DOM
                try
                {
                    return pDriver.findElement(pBy);
                }
                catch (Exception iException)
                {
                    throw new RuntimeException("WAIT:NONE — element not found immediately. Locator : "
                            + pObjectName + " | Consider using WAIT:VISIBLE instead.");
                }
            }

            default:
                throw new RuntimeException("Unknown WaitStrategy : " + this.name());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : applyWithCustomTimeout
    // Description   : Applies this strategy with a one-off timeout override (seconds).
    //                 Useful when a specific action needs a longer/shorter wait than the global default.
    // Parameters    : pDriver         (WebDriver) - driver instance
    //                 pBy             (By)        - Selenium locator
    //                 pObjectName     (String)    - locator description
    //                 pTimeoutSeconds (int)       - custom timeout in seconds
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public WebElement applyWithCustomTimeout(WebDriver pDriver, By pBy, String pObjectName, int pTimeoutSeconds)
    {
        WebDriverWait iCustomWait = new WebDriverWait(pDriver, Duration.ofSeconds(pTimeoutSeconds));
        return applyAndGet(iCustomWait, pDriver, pBy, pObjectName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : fromString
    // Description   : Parses a WaitStrategy from a string (case-insensitive). Returns VISIBLE if unrecognised.
    //                 Used by iAction to parse the optional WAIT:strategy prefix from pValueToEnter.
    // Parameters    : pValue (String) - e.g. "VISIBLE", "CLICKABLE", "NONE"
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static WaitStrategy fromString(String pValue)
    {
        if (pValue == null || pValue.trim().isEmpty())
        {
            return VISIBLE;
        }

        try
        {
            return WaitStrategy.valueOf(pValue.trim().toUpperCase());
        }
        catch (IllegalArgumentException iException)
        {
            log.warning("[WaitStrategy] Unrecognised strategy '" + pValue + "'. Defaulting to VISIBLE.");
            return VISIBLE;
        }
    }
}