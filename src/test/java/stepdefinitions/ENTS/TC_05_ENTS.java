// ===================================================================================================================================
// File          : TC_05_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_05_ENTS — Transfer Application E2E (Agent to Individual).
//
//                 This file contains ONLY the steps unique to Agent-to-Individual transfers.
//                 The critical difference from TC_03_ENTS (Agent-to-Agent):
//                   - Individual users log in via a DIFFERENT login page URL
//                   - Individual usernames are non-agent format (e.g. "PAUDYFROG", "TERENCE1")
//                   - A "Close personal details" dialog appears after Individual login
//                   - Individual navigates directly to "Transfers" tab (no My Clients → tab switch)
//                   - Individual sees the transfer on their dashboard without a herd search
//
//                 ┌──────────────────────────────────────────────────────────────────┬────────────────────┐
//                 │ Reused Steps                                                     │ Defined In         │
//                 ├──────────────────────────────────────────────────────────────────┼────────────────────┤
//                 │ the agent user is on the login page                              │ TC_03.java         │
//                 │ the agent logs into the application...                           │ TC_03.java         │
//                 │ the agent opens the {string} application                         │ TC_03.java         │
//                 │ the agent should land on the BISS Home page                      │ TC_03.java         │
//                 │ the agent navigates to ... Left Menu Link                        │ TC_03.java         │
//                 │ the agent switches to the {string} tab...                        │ TC_06.java         │
//                 │ the agent creates a transfer application with the following...   │ TC_01_ENTS.java    │
//                 │ the agent uploads the transferor signature document              │ TC_01_ENTS.java    │
//                 │ the agent sends the transfer for acceptance                      │ TC_01_ENTS.java    │
//                 │ the transfer key should be captured                              │ TC_01_ENTS.java    │
//                 │ the transfer should be submitted successfully                    │ TC_01_ENTS.java    │
//                 │ the agent logs out and re-logs in as the transferor agent        │ TC_03_ENTS.java    │
//                 └──────────────────────────────────────────────────────────────────┴────────────────────┘
//
//                 New steps in this file (2 total):
//                   1. the agent logs out and re-logs in as the individual transferee {string}
//                   2. the individual completes the transferee acceptance flow (DataTable)
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

import commonFunctions.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ObjReader;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import static commonFunctions.CommonFunctions.iAction;
import static commonFunctions.CommonFunctions.getDriver;

public class TC_05_ENTS
{
    private static final Logger log = Logger.getLogger(TC_05_ENTS.class.getName());


    // ===================================================================================================================================
    //  INDIVIDUAL LOGIN — Logout agent session, re-login as Individual
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the individual transferee {string}
    // Description   : Exits the current BISS/agent session, logs out fully, then performs a
    //                 fresh login using the INDIVIDUAL login page with the supplied username.
    //
    //                 Key differences from TC_03_ENTS.theAgentLogsOutAndReLogsInAsTheTransfereeAgent():
    //                   1. Navigates to the Individual login page (different URL from agent login)
    //                   2. Uses the Individual username directly (e.g. "PAUDYFROG") not from TD property
    //                   3. Dismisses the "Close personal details" dialog that appears for Individual users
    //                   4. Navigates to BISS → Transfers directly (no My Clients tab switch)
    //
    // Parameters    : pIndividualUsername (String) - Individual login username e.g. "PAUDYFROG", "TERENCE1"
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the individual transferee {string}")
    public void theAgentLogsOutAndReLogsInAsTheIndividualTransferee(String pIndividualUsername)
    {
        log.info("[STEP] When the agent logs out and re-logs in as the individual transferee: " + pIndividualUsername);

        // ── Step 1 : Exit BISS and logout ────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iExitBISSLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutBtn"), null);
        log.info("Logged out of agent session.");

        // ── Step 2 : Navigate to Individual login page ───────────────────────────────────
        // The Individual login page has a different URL from the agent portal login.
        // The locator iIndividualLoginPageUrl resolves the correct URL from ObjectRepository.
        // If the logout lands on the main portal page, we may need to click a link to get
        // to the individual login page.
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIndividualLoginLink"), null);
        }
        catch (Exception e)
        {
            // Already on the login page — continue
            log.info("Already on login page — skipping Individual login link click.");
        }

        // ── Step 3 : Login as Individual ─────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

        // Enter the Individual username — passed directly from the feature file, not from TD
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), pIndividualUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);

        // Enter the password — uses the shared TD:Password property
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // ── Step 4 : Handle OTP ──────────────────────────────────────────────────────────
        handlePostLoginOTP();

        // ── Step 5 : Dismiss "Close personal details" dialog ─────────────────────────────
        // Individual users get a personal details popup after first login or session refresh.
        // Agents don't see this — it's unique to the individual login flow.
        try
        {
            if (isVisible(By.xpath(ObjReader.getLocator("iPersonalDetailsCloseBtn")), 5))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iPersonalDetailsCloseBtn"), null);
                log.info("Personal details dialog dismissed.");
            }
        }
        catch (Exception e)
        {
            log.info("No personal details dialog displayed — continuing.");
        }

        // ── Step 6 : Navigate to BISS → Transfers ───────────────────────────────────────
        // Individual users access BISS the same way (search + click), but then go directly
        // to the Transfers tab — they don't have a My Clients tab.
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);

        // Click directly on the Transfers tab — Individual portal layout differs from agent
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersTabDirect"), null);

        log.info("Re-logged in as individual transferee: " + pIndividualUsername);
    }


    // ===================================================================================================================================
    //  INDIVIDUAL TRANSFEREE ACCEPTANCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the individual completes the transferee acceptance flow (DataTable)
    // Description   : After the individual user has logged in, this step:
    //                 1. Clicks View on the transfer row matching the herd number on the dashboard
    //                    (Individual doesn't need to search — their transfers are listed directly)
    //                 2. Enters the captured transfer key
    //                 3. Views the transfer application
    //                 4. Enters transferee notes
    //                 5. Submits to DAFM with T&C acceptance
    //
    //                 Key differences from TC_03_ENTS.theAgentCompletesTheCrossAgentTransfereeAcceptanceFlow():
    //                   - No herd search step — Individual sees transfers directly on their dashboard
    //                   - No "View Link for Searched Herd" step — goes straight to the transfer row
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number to match on the dashboard
    //                   notes          (String) - transferee notes e.g. "Approved Test"
    //
    //                 Depends on: transfer key from System.getProperty("lastCapturedTransferKey")
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the individual completes the transferee acceptance flow")
    public void theIndividualCompletesTheTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] And the individual completes the transferee acceptance flow");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();

        // ── Click View on the transfer row ───────────────────────────────────────────────
        // Individual dashboard shows transfers directly — click View for the matching herd
        iAction("CLICK", "XPATH",
                "//tr[contains(.,'" + iTransfereeHerd + "')]//button[contains(text(),'View')] | "
                        + "//button[contains(text(),'View')]",
                null);
        log.info("Transfer row opened for herd: " + iTransfereeHerd);

        // ── Enter the captured transfer key ──────────────────────────────────────────────
        String iTransferKey = System.getProperty("lastCapturedTransferKey", "");
        Assertions.assertFalse(iTransferKey.isEmpty(),
                "Transfer key must have been captured in the Transferor flow before the Individual can accept.");

        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferKeyInputField"), iTransferKey);
        log.info("Transfer key entered: " + iTransferKey);

        // ── View the transfer application ────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferViewApplicationBtn"), null);

        // ── Enter transferee notes ───────────────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransferNotesField"), iNotes);

        // ── Submit to DAFM ───────────────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferSubmitToDAFMBtn"), null);

        // Accept T&C
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferTandCCheckbox"), null);

        // Confirm submission
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransferDialogSubmitBtn"), null);

        log.info("Individual transferee acceptance completed for herd: " + iTransfereeHerd);
    }


    // ===================================================================================================================================
    //  HELPERS
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : handlePostLoginOTP
    // Description   : Handles OTP / PIN flow after Individual login. Same logic as TC_03_ENTS
    //                 but kept as a private method here to avoid cross-class coupling.
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    private void handlePostLoginOTP()
    {
        WebDriver iDriver = getDriver();
        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

        if (isVisible(iPinFormBy, 3))
        {
            log.info("[LOGIN-INDIVIDUAL] PIN screen detected. Entering PIN 1→7.");

            for (int idx = 1; idx <= 7; idx++)
            {
                String iDynamicXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));
                By iPinBy = By.xpath(iDynamicXpath);

                if (isVisible(iPinBy, 1))
                {
                    try
                    {
                        org.openqa.selenium.WebElement iInput = iDriver.findElement(iPinBy);
                        if (iInput.getAttribute("disabled") == null && iInput.isEnabled())
                        {
                            iInput.clear();
                            iInput.sendKeys("1");
                        }
                    }
                    catch (Exception ignored) {}
                }
            }

            iAction("CLICK", "XPATH", ObjReader.getLocator("iPinLoginBtn"), null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"), "111111");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);

            // Handle T&C if present
            if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
            }
        }
        else
        {
            // Simple OTP flow
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);
        }

        log.info("[LOGIN-INDIVIDUAL] Post-login OTP handling complete.");
    }

    /**
     * Short-wait visibility check — returns true if the element appears within pSeconds.
     */
    private boolean isVisible(By pLocator, int pSeconds)
    {
        try
        {
            WebDriverWait iWait = new WebDriverWait(getDriver(), Duration.ofSeconds(pSeconds));
            iWait.until(ExpectedConditions.visibilityOfElementLocated(pLocator));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}