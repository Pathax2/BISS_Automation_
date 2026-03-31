// ===================================================================================================================================
// File          : TC_03_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_03_ENTS — Transfer Application E2E (Different Agent / Cross-Agent).
//
//                 This file contains ONLY the steps unique to cross-agent transfers.
//                 The critical difference from TC_01_ENTS / TC_02_ENTS: the Transferee acceptance
//                 requires logging out of the current agent session and re-logging in as a
//                 completely different agent before accepting the transfer.
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
//                 └──────────────────────────────────────────────────────────────────┴────────────────────┘
//
//                 New steps in this file (3 total):
//                   1. the agent logs out and re-logs in as the transferee agent
//                   2. the agent logs out and re-logs in as the transferor agent
//                   3. the agent completes the cross-agent transferee acceptance flow (DataTable)
//
//                 NOTE: This class needs access to the iCapturedTransferKey from TC_01_ENTS.
//                 Since TC_01_ENTS stores it as an instance variable, and Cucumber creates
//                 separate instances per class, we use a static shared field for cross-class access.
//                 Alternatively, the transfer key can be published via System.setProperty()
//                 in TC_01_ENTS.theTransferKeyShouldBeCaptured() — uncomment the bridge line there.
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

public class TC_03_ENTS
{
    private static final Logger log = Logger.getLogger(TC_03_ENTS.class.getName());

    // -------------------------------------------------------------------------------------------------------------------------------
    // Agent credentials — resolved from test data or system properties.
    // The transferor agent is the one who logged in via the Background (set by Hooks).
    // The transferee agent credentials are loaded from TestData.xlsx or overridden via -D props.
    //
    // These are read from system properties set by Hooks/TestRunner:
    //   TD:TransfereeUsername / TD:TransfereePassword  — for the transferee agent
    //   TD:Username / TD:Password                      — for the transferor agent (original)
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String TRANSFEREE_USERNAME_PROP = "TD:TransfereeUsername";
    private static final String TRANSFEROR_USERNAME_PROP = "TD:Username";


    // ===================================================================================================================================
    //  CROSS-AGENT SESSION MANAGEMENT — Logout and Re-login
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the transferee agent
    // Description   : Exits the current BISS session, logs out fully, then performs a fresh
    //                 login using the Transferee agent's credentials. Navigates back to
    //                 My Clients → Transfers tab ready for the acceptance flow.
    //
    //                 This is the step that makes cross-agent transfers different from same-agent:
    //                 the Transferee is a completely different user with different herds.
    //
    //                 Credential resolution order:
    //                   1. System property TD:TransfereeUsername (set by Hooks from TestData.xlsx)
    //                   2. System property transferee.username (set via -D in Maven/Bamboo)
    //                   3. Fails if neither is available
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the transferee agent")
    public void theAgentLogsOutAndReLogsInAsTheTransfereeAgent()
    {
        log.info("[STEP] When the agent logs out and re-logs in as the transferee agent");

        // ── Step 1 : Exit BISS portal ────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iExitBISSLink"), null);

        // ── Step 2 : Click Logout ────────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutBtn"), null);
        log.info("Logged out of transferor agent session.");

        // ── Step 3 : Resolve transferee credentials ──────────────────────────────────────
        String iTransfereeUsername = System.getProperty(TRANSFEREE_USERNAME_PROP,
                System.getProperty("transferee.username", "")).trim();

        if (iTransfereeUsername.isEmpty())
        {
            throw new RuntimeException("Transferee username not available. Set TD:TransfereeUsername in TestData.xlsx "
                    + "or pass -Dtransferee.username=xxx on the command line.");
        }

        // ── Step 4 : Fresh login as the transferee agent ─────────────────────────────────
        // Temporarily override the runtime username so the shared login step uses transferee creds
        String iOriginalUsername = stepdefinitions.Hooks.RUNTIME_USERNAME;
        stepdefinitions.Hooks.RUNTIME_USERNAME = iTransfereeUsername;

        // Hit the login page — the browser should already be there after logout
        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), iTransfereeUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // Handle OTP/PIN — same approach as TC_03's login step
        handlePostLoginOTP();

        // ── Step 5 : Navigate to BISS → My Clients → Transfers ──────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);

        // Switch to Transfers tab
        iAction("CLICK", "XPATH",
                "//div[contains(@class,'mat-tab-label')]//span[normalize-space()='Transfers']"
                        + " | //a[normalize-space()='Transfers']",
                null);

        log.info("Re-logged in as transferee agent: " + iTransfereeUsername);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : the agent logs out and re-logs in as the transferor agent
    // Description   : Exits the transferee session and re-logs in using the original transferor
    //                 agent credentials. Navigates back to My Clients → Transfers tab.
    //                 Called between sections to switch back to the transferor for the next transfer.
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs out and re-logs in as the transferor agent")
    public void theAgentLogsOutAndReLogsInAsTheTransferorAgent()
    {
        log.info("[STEP] When the agent logs out and re-logs in as the transferor agent");

        // ── Exit and logout ──────────────────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iExitBISSLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutBtn"), null);
        log.info("Logged out of transferee agent session.");

        // ── Resolve transferor credentials ───────────────────────────────────────────────
        String iTransferorUsername = System.getProperty(TRANSFEROR_USERNAME_PROP,
                stepdefinitions.Hooks.RUNTIME_USERNAME).trim();

        // ── Fresh login as the transferor agent ──────────────────────────────────────────
        stepdefinitions.Hooks.RUNTIME_USERNAME = iTransferorUsername;

        iAction("CLICK", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), iTransferorUsername);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // Handle OTP/PIN
        handlePostLoginOTP();

        // ── Navigate to BISS → My Clients → Transfers ───────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);

        iAction("CLICK", "XPATH",
                "//div[contains(@class,'mat-tab-label')]//span[normalize-space()='Transfers']"
                        + " | //a[normalize-space()='Transfers']",
                null);

        log.info("Re-logged in as transferor agent: " + iTransferorUsername);
    }


    // ===================================================================================================================================
    //  CROSS-AGENT TRANSFEREE ACCEPTANCE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : the agent completes the cross-agent transferee acceptance flow (DataTable)
    // Description   : After the transferee agent has logged in, this step:
    //                 1. Searches for the transferee herd in the Transfers tab
    //                 2. Clicks View on the searched herd
    //                 3. Clicks View on the transferee dashboard row
    //                 4. Enters the captured transfer key
    //                 5. Views the transfer application
    //                 6. Enters transferee notes
    //                 7. Submits to DAFM with T&C acceptance
    //
    //                 DataTable keys:
    //                   transfereeHerd (String) - herd number to search for
    //                   notes          (String) - transferee notes e.g. "Approved Test"
    //
    //                 Depends on: transfer key captured by TC_01_ENTS.theTransferKeyShouldBeCaptured()
    //                 The key is read from System.getProperty("lastCapturedTransferKey") which
    //                 TC_01_ENTS publishes after capturing it.
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    @And("the agent completes the cross-agent transferee acceptance flow")
    public void theAgentCompletesTheCrossAgentTransfereeAcceptanceFlow(DataTable pDataTable)
    {
        log.info("[STEP] And the agent completes the cross-agent transferee acceptance flow");

        Map<String, String> iData = pDataTable.asMap(String.class, String.class);

        String iTransfereeHerd = iData.get("transfereeHerd").trim();
        String iNotes          = iData.get("notes").trim();

        // ── Search for the transferee herd ───────────────────────────────────────────────
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTransfersHerdSearchField"), iTransfereeHerd);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersSearchBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iTransfersViewLink"), null);
        log.info("Transferee herd opened: " + iTransfereeHerd);

        // ── Click View on the transferee dashboard ───────────────────────────────────────
        // The View button in the dashboard may be scoped by herd number
        iAction("CLICK", "XPATH",
                "//tr[contains(.,'" + iTransfereeHerd + "')]//button[contains(text(),'View')] | "
                        + "//button[contains(text(),'View')]",
                null);

        // ── Enter the captured transfer key ──────────────────────────────────────────────
        String iTransferKey = System.getProperty("lastCapturedTransferKey", "");
        Assertions.assertFalse(iTransferKey.isEmpty(),
                "Transfer key must have been captured in the Transferor flow before the Transferee can accept.");

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

        log.info("Cross-agent transferee acceptance completed for herd: " + iTransfereeHerd);
    }


    // ===================================================================================================================================
    //  HELPER — Post-login OTP/PIN handling
    //
    //  Extracted as a private method because it's called twice per section (once for transferee
    //  login, once for transferor re-login). Follows the same logic as TC_03.java's login step
    //  but is self-contained here to avoid coupling to TC_03's internal implementation.
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : handlePostLoginOTP
    // Description   : Handles the OTP / PIN flow that appears after submitting username + password.
    //                 Checks for PIN screen first (up to 7 slots), then TOTP, then plain OTP.
    //                 Also handles the T&C acceptance screen if it appears.
    //                 Mirrors the logic in TC_03.theAgentLogsIntoTheApplicationWithValidCredentialsAndOTP()
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 31-03-2026
    // ***************************************************************************************************************************************************************************************
    private void handlePostLoginOTP()
    {
        WebDriver iDriver = getDriver();

        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

        if (isVisible(iPinFormBy, 3))
        {
            log.info("[LOGIN-XAGENT] PIN screen detected. Entering PIN 1→7.");

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

        log.info("[LOGIN-XAGENT] Post-login OTP handling complete.");
    }


    /**
     * Short-wait visibility check — returns true if the element appears within pSeconds.
     * Never throws. Same pattern as TC_03.java's isVisible() helper.
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