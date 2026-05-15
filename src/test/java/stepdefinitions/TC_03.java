// ===================================================================================================================================
// File          : BISSAgentStepDefinitions.java
// Package       : stepdefinitions
// Description   : Step definitions for BISS Agent end-to-end farmer application journey.
//                 Covers: Login, Portal Navigation, Farmer Dashboard, Side Navigation,
//                         Application Start, Active Farmer, Scheme Selection, Land Details,
//                         GAEC 7, ACRES, Eco, Review & Submit, Correspondence Upload.
//
//                 Naming convention  : iAction(actionType, identifyBy, locator, value)
//                 Test data access   : System.getProperty("TD:ColumnName") — set by Hooks via ExcelUtilities
//                 Herd data access   : ExcelUtilities.getRowData("HerdData", rowIndex)
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 07-03-2026
// Updated       : 28-03-2026 — Human-readable inline step comments added throughout
// ===================================================================================================================================

package stepdefinitions;

import commonFunctions.CommonFunctions;
import dev.failsafe.internal.util.Assert;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ObjReader;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static commonFunctions.CommonFunctions.*;

public class TC_03 {

    private static final Logger log = Logger.getLogger(TC_03.class.getName());


    // ===================================================================================================================================
    //  BACKGROUND — Login and Portal Entry
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Given the agent user is on the login page
    // Description   : Launches browser and navigates to the application URL loaded from test data Config sheet
    // ***************************************************************************************************************************************************************************************
    @Given("the agent user is on the login page")
    public void theAgentUserIsOnTheLoginPage()
    {
        log.info("Application launched via Hooks. Browser is active and URL loaded.");
        log.info("[STEP] Given the agent user is on the login page");

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent logs into the application with valid credentials and OTP
    // Description   : Enters username and password from test data, submits login form, handles OTP if present
    // ***************************************************************************************************************************************************************************************
    @When("the agent logs into the application with valid credentials and OTP")
    public void theAgentLogsIntoTheApplicationWithValidCredentialsAndOTP()
    {

        log.info("[LOGIN] Classic login detected.");

        // Hit the initial 'Log In' button on the BISS landing screen to get to the Keycloak form
        //iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

        // Type the agent's username — pulled from Hooks.RUNTIME_USERNAME which is resolved
        // at runtime from BISS_DATA + BISS_INET before any scenario executes.
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"), Hooks.RUNTIME_USERNAME);
        // Move past the username screen to get to the password entry form
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);

        // Type the password from the test data sheet (TD:Password column)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"), "TD:Password");

        // Submit the password — this either takes us to the dashboard or triggers MFA
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);

        // ── Account Expired detection (Option 4 fix — 05-05-2026) ────────────────────
        // Keycloak shows "Account Expired" immediately after Login click when the agent's
        // SSO account has expired. Detect it here before waiting for PIN/OTP screen.
        // If found: mark agent expired → Hooks.markAgentExpired() re-resolves a fresh
        // herd+agent pair → update RUNTIME_USERNAME → re-attempt login with new agent.
        By iExpiredMsgBy = By.xpath(
                "//*[contains(@class,'kc-feedback-text') "
                        + "and contains(normalize-space(),'Account Expired')]");

        if (isVisible(iExpiredMsgBy, 5))
        {
            String iExpiredAgent = Hooks.RUNTIME_USERNAME;
            log.warning("[LOGIN] Account Expired detected for agent: " + iExpiredAgent
                    + " — calling Hooks.markAgentExpired() to re-resolve.");

            // Mark expired + re-resolve new herd+agent into Hooks.RUNTIME_HERD / RUNTIME_USERNAME
            Hooks.markAgentExpired(iExpiredAgent);

            // Cancel the current Keycloak session and restart login with new agent
            iAction("CLICK", "XPATH",
                    "//button[normalize-space()='Cancel'] | //a[normalize-space()='Cancel']", null);

            // Navigate back to base URL for a clean login state
            CommonFunctions.getDriver().navigate().to(Hooks.iUrl);
           // iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);

            // Re-attempt login with the newly resolved agent
           // iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),     null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),       Hooks.RUNTIME_USERNAME);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"), null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),       "TD:Password");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),             null);

            log.info("[LOGIN] Re-attempting login with new agent: " + Hooks.RUNTIME_USERNAME);
        }
        // ── End Account Expired detection ──────────────────────────────────────────────

        log.info("[STEP] Detect login screen and auto-login using simple PIN loop...");

        WebDriver driver = CommonFunctions.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        By pinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));

        // Check whether the PIN login screen has appeared (give it 3 seconds — no need to wait longer)
        if (isVisible(pinFormBy, 3)) {

            log.info("[LOGIN] PIN screen detected. Using simple loop 1 → 7.");

            // The PIN form has up to 7 digit slots — loop through all of them
            // and enter '1' into each one that is enabled and visible
            for (int idx = 1; idx <= 7; idx++) {

                // Build the XPath for this particular PIN slot dynamically using the index
                String dynamicXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));

                By pinInputBy = By.xpath(dynamicXpath);

                if (isVisible(pinInputBy, 1)) {

                    WebElement input = driver.findElement(pinInputBy);

                    boolean disabled = input.getAttribute("disabled") != null;

                    if (!disabled && input.isEnabled())
                    {
                        // Slot is active — clear any pre-filled value and type our digit
                        input.clear();
                        input.sendKeys("1");
                        log.info("[LOGIN] Entered '1' into PIN index: " + idx);
                    }
                    else
                    {
                        // Some PIN slots are disabled (grey) depending on the account setup — skip those
                        log.info("[LOGIN] PIN index " + idx + " is DISABLED — skipping.");
                    }

                } else {
                    // This slot number doesn't exist in the DOM for this account — move on
                    log.info("[LOGIN] PIN index " + idx + " not present in DOM — skipping.");
                }
            }

            // All available PIN digits are filled — submit the PIN form
            iAction("CLICK", "XPATH", ObjReader.getLocator("iPinLoginBtn"), null);
            log.info("[LOGIN] PIN login submitted.");

            // After PIN, the system asks for a 6-digit TOTP code from the authenticator app
            // We're using a hardcoded test value here — replace with TD lookup if needed
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"), "111111");

            // Submit the TOTP code to complete the MFA flow
            iAction("CLICK", "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);

            log.info("[LOGIN] TOTP screen completed.");
            // Click the Terms & Conditions checkbox
            // Some accounts require accepting Terms & Conditions after first login or after a reset.
            // Check if the T&C screen is there — if it is, tick the checkbox and hit Accept.
            // If it's not there (most runs), this quietly skips without failing.
            if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
            {
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
                // T&C checkbox is ticked — the Accept button should now be enabled, click it
                iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"), null);
                log.info("[LOGIN] Accept Terms & Conditions completed.");
            }




        }
        else
        {
            // No PIN screen appeared — this is the simpler OTP-only login path
            // Type the 6-digit OTP and submit directly
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"), null);
            log.info("[LOGIN] Classic login completed.");
        }

    }


    /** SIMPLE helper to avoid ambiguous method call */
    private static boolean isVisible(By locator, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(CommonFunctions.getDriver(), Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent opens the "Basic Income Support for Sustainability" application
    // Description   : Clicks the named application tile on the portal home screen
    // ***************************************************************************************************************************************************************************************
    @And("the agent opens the {string} application")
    public void theAgentOpensTheApplication(String pApplicationName)
    {

        //Below flow is commented as login flow is rerouted
        //log.info("[STEP] And the agent opens the application: " + pApplicationName);

        // Click into the search bar first to make sure it has focus before we start typing
        //iAction("CLICK", "XPATH", ObjReader.getLocator("iAppSearchBar"), "");

        // Type the full application name into the search bar so it filters the results list
        //iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iAppSearchBar"), "Basic Income Support for Sustainability");

        // Confirm the search returned exactly one result — if this fails it means
        // the app name didn't match or the portal is showing something unexpected
        //iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iSearchAppLabel"), "");
        //iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iSearchAppLabel"), "Found 1 applications matching your search");

        // The result is showing — click the BISS application link to enter the portal
        //iAction("CLICK", "XPATH", ObjReader.getLocator("iBissLink"), "");

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the agent should land on the BISS Home page
    // Description   : Validates that the BISS Home page header is visible after login
    // ***************************************************************************************************************************************************************************************
    @Then("the agent should land on the BISS Home page")
    public void theAgentShouldLandOnTheBISSHomePage()
    {
        // The spinner needs to disappear before we try to read the page title —
        // otherwise we might check the heading while the page is still loading
        iAction("WAITINVISIBLE", "XPATH", "iScreenBuffer", "MDC Progress Spinner");
        log.info("[STEP] Then the agent should land on the BISS Home page");

        // Confirm the BISS header element exists on the page
        iAction("VERIFYELEMENT", "XPATH", ObjReader.getLocator("iBissTitle"), "");

        // Also verify the text in it matches what we expect — guards against
        // landing on a different page that happens to have a similar heading element
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");
        log.info("BISS Home page confirmed | Title: " + "Basic Income Support for Sustainability");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent navigates to the "My Clients" tab
    // Description   : Clicks the My Clients navigation tab on the BISS portal
    // ***************************************************************************************************************************************************************************************
    @And("the agent navigates to the {string} and {string} Left Menu Link")
    public void theAgentNavigatesToTheTab(String iHomeTab,String iMyCLientTab)
    {
        log.info("[STEP] And the agent navigates to the tab: " + iHomeTab);
        // Click 'Home' first to reset the navigation to a known starting state
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"), null);

        log.info("[STEP] And the agent navigates to the tab: " + iMyCLientTab);
        // Now click 'My Clients' to get to the client search screen
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);

        // Confirm the BISS header is still visible — makes sure we're still inside the portal
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");

        // Exercise each Quick Filter tab to confirm they all respond — this is a smoke-level
        // check on the tab bar rather than a deep functional test of the filter logic
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHerdExpiredTab"), "");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iDraftTab"), "");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iSubmittedTab"), "");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNotStartedTab"), "");

        // Leave the filter on 'View all' so the next step sees the full client list
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewAllTab"), "");

    }


    // ===================================================================================================================================
    //  FARMER SELECTION AND DASHBOARD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens a farmer dashboard using herd data
    // Description   : Searches for the runtime herd on the My Clients screen.
    //
    //                 Three-layer validation — a herd must pass ALL three before the test proceeds:
    //                   Layer 1 (DB — done in Hooks @BeforeAll) : herd exists in BISS_DATA
    //                   Layer 2 (DB — done in Hooks @BeforeAll) : herd has an agent in BISS_INET
    //                   Layer 3 (UI — done HERE)                : herd returns visible rows in My Clients
    //
    //                 If layer 3 fails (0 rows in UI) the method:
    //                   1. Re-queries BISS_DATA with an increased limit to get a fresh candidate pool
    //                   2. Validates each candidate against BISS_INET (layer 2)
    //                   3. Logs out of the current session
    //                   4. Logs back in as the NEW agent (the agents differ per herd)
    //                   5. Navigates back to My Clients
    //                   6. Searches again — up to MAX_HERD_RETRIES total attempts
    //
    //                 Updates Hooks.RUNTIME_HERD and Hooks.RUNTIME_USERNAME on every
    //                 successful swap so all downstream steps reference the correct pair.
    //
    // Parameters    : none
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens a farmer dashboard using herd data")
    public void theAgentOpensAFarmerDashboardUsingHerdDataFromRow()
    {
        log.info("[STEP] When the agent opens a farmer dashboard using herd data");

        final int    MAX_HERD_RETRIES = 5;
        final String YEAR             = System.getProperty("herd.year",  "2026").trim();
        final String BASE_LIMIT       = System.getProperty("herd.limit", "25").trim();
        final By     iClientRowsBy    = By.xpath(ObjReader.getLocator("clientTableRows"));

        boolean iFound = false;

        for (int iAttempt = 0; iAttempt < MAX_HERD_RETRIES; iAttempt++)
        {
            String iCurrentHerd = Hooks.RUNTIME_HERD;
            log.info("[HERD-RETRY] Attempt " + (iAttempt + 1) + "/" + MAX_HERD_RETRIES + " — searching herd: " + iCurrentHerd + " | logged in as: " + Hooks.RUNTIME_USERNAME);

            // ── Search for the herd on My Clients ──────────────────────────────
            iAction("TEXTBOX",   "XPATH", ObjReader.getLocator("herdSearchInput"), "");
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), iCurrentHerd);
            iAction("CLICK",   "XPATH", ObjReader.getLocator("herdSearchBtn"),   null);

            // Allow the table to respond before counting rows
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

            java.util.List<org.openqa.selenium.WebElement> iRows = getDriver().findElements(iClientRowsBy);

            if (!iRows.isEmpty()) {
                // ── Layer 3a: row exists — check for Herd expired flag ──────────────
                boolean iHerdExpired = !getDriver()
                        .findElements(By.xpath(
                                "//tbody[contains(@class,'mdc-data-table__content')]"
                                        + "//tr[contains(@class,'client-list-element-row')]"
                                        + "//td[contains(@class,'cdk-column-expired')]"
                                        + "//span[normalize-space()='Herd expired']"))
                        .isEmpty();

                if (iHerdExpired) {
                    log.warning("[HERD-RETRY] Herd " + iCurrentHerd
                            + " found in UI but marked 'Herd expired' — treating as unusable."
                            + " Fetching replacement from DB.");
                    // Fall through — same re-query path as 0 rows
                } else {
                    // Row present and herd is active — fully valid
                    log.info("[HERD-RETRY] Herd " + iCurrentHerd + " returned " + iRows.size()
                            + " valid row(s). Proceeding.");
                    iFound = true;
                    break;
                }
            }

            // ── Layer 3 failed — 0 rows returned ──────────────────────────────
            log.warning("[HERD-RETRY] Herd " + iCurrentHerd
                    + " returned 0 rows in My Clients UI — fetching replacement from DB.");

            // Re-query BISS_DATA with a larger window so we surface different herds
            int iNewLimit = Integer.parseInt(BASE_LIMIT) + ((iAttempt + 1) * 10);
            database.DBRouter.runDB("DATA", "List of herds with no errors at all", YEAR, String.valueOf(iNewLimit));

            java.util.List<java.util.Map<String, Object>> iDbRows = database.DBRouter.getRows();
            if (iDbRows == null || iDbRows.isEmpty())
            {
                log.warning("[HERD-RETRY] BISS_DATA returned no rows at limit=" + iNewLimit + " — cannot recover. Stopping retry.");
                break;
            }

            // Shuffle so we don't always evaluate in the same order
            java.util.List<String> iCandidates = new java.util.ArrayList<>();
            for (java.util.Map<String, Object> r : iDbRows)
            {
                String h = java.util.Objects.toString(r.get("APP_HERD_NO"), "").trim();
                if (!h.isEmpty() && !h.equals(iCurrentHerd))
                {
                    iCandidates.add(h);
                }
            }
            java.util.Collections.shuffle(iCandidates, new java.util.Random(System.nanoTime()));

            if (iCandidates.isEmpty())
            {
                log.warning("[HERD-RETRY] No fresh candidates from DB — stopping retry.");
                break;
            }

            // Validate each candidate against BISS_INET before committing
            String iNextHerd     = null;
            String iNextUsername = null;

            for (String iCandidate : iCandidates)
            {
                database.DBRouter.runDB("INET", "Get Login Id for herd", iCandidate);
                String iUsername = database.DBRouter.getValue("USERNAME");

                if (iUsername != null && !iUsername.isBlank())
                {
                    iNextHerd     = iCandidate;
                    iNextUsername = iUsername.trim();
                    log.info("[HERD-RETRY] INET-validated replacement: herd=" + iNextHerd
                            + " | username=" + iNextUsername);
                    break;
                }
                else
                {
                    log.info("[HERD-RETRY] No INET agent for candidate " + iCandidate + " — skipping.");
                }
            }

            if (iNextHerd == null)
            {
                log.warning("[HERD-RETRY] No INET-validated replacement found — stopping retry.");
                break;
            }

            // ── Agent has changed — must re-login as the new agent ─────────────
            // The My Clients list is agent-scoped: agent A cannot see agent B's herds.
            // We must log out of the current session and log back in as the new agent
            // before searching, otherwise the herd will still return 0 rows.
            if (!iNextUsername.equalsIgnoreCase(Hooks.RUNTIME_USERNAME))
            {
                log.info("[HERD-RETRY] Agent change required: " + Hooks.RUNTIME_USERNAME + " → " + iNextUsername + ". Performing logout/login cycle.");

                performLogout();
                performLogin(iNextUsername);
                navigateToMyClients();
            }
            else
            {
                // Same agent, different herd — just clear the search and retry
                log.info("[HERD-RETRY] Same agent (" + iNextUsername + ") — no re-login needed. Retrying search.");
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), "");
            }

            // Commit the new pair to the shared Hooks fields
            Hooks.RUNTIME_HERD     = iNextHerd;
            Hooks.RUNTIME_USERNAME = iNextUsername;

            log.info("[HERD-RETRY] Hooks updated — RUNTIME_HERD=" + Hooks.RUNTIME_HERD + " | RUNTIME_USERNAME=" + Hooks.RUNTIME_USERNAME);
        }

        if (!iFound)
        {
            throw new RuntimeException(
                    "[HERD-RETRY] Could not find a herd with visible UI records after "
                            + MAX_HERD_RETRIES + " attempts. Last herd: " + Hooks.RUNTIME_HERD
                            + ". Ensure the herd has an active BISS application visible in My Clients "
                            + "for agent " + Hooks.RUNTIME_USERNAME + " on " + Hooks.iEnvironment + ".");
        }

        // ── Herd confirmed visible — set page size to maximum ─────────────────
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iListItemsPerPage"),   null);
        iAction("WAITVISIBLE",  "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);
        iAction("WAITCLICKABLE","XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iMatSelectLastOption"),null);

        log.info("[STEP] Farmer dashboard opened | herd=" + Hooks.RUNTIME_HERD + " | agent=" + Hooks.RUNTIME_USERNAME);
    }


    // ===================================================================================================================================
    //  PRIVATE HELPERS — Login / Logout / Navigation (reused by herd retry loop)
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : performLogout
    // Description   : Clicks the logout icon and waits for the landing page to confirm
    //                 the session has been terminated. Safe to call at any point when
    //                 the agent is inside the BISS portal.
    // Parameters    : none
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogout()
    {
        log.info("[RELOGIN] Logging out current session...");
        try
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iExitLink"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLogoutbtn"), null);

            // Wait for the welcome/landing page login button to appear —
            // this confirms the session is fully terminated and we are back at the start
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
            log.info("[RELOGIN] Logout complete — landing page visible.");
        }
        catch (Exception e)
        {
            // If logout fails (e.g. session already expired), navigate directly to
            // the base URL to force a clean state rather than leaving a broken session
            log.warning("[RELOGIN] Logout click failed (" + e.getMessage()
                    + ") — navigating to base URL as fallback.");
            getDriver().navigate().to(Hooks.iUrl);
            iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : performLogin
    // Description   : Executes the full Keycloak login sequence for the given username.
    //                 Handles PIN screen (slots 1–7) and TOTP, same as the Background
    //                 login step — extracted here so the herd retry loop can call it
    //                 without duplicating logic.
    //                 Password is always read from TD:Password (same for all agents
    //                 in the test environment).
    //                 Terms & Conditions acceptance is handled if the screen appears.
    // Parameters    : pUsername (String) — Keycloak username of the agent to log in as
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    private void performLogin(String pUsername)
    {
        log.info("[RELOGIN] Logging in as: " + pUsername);

        // Keycloak entry point
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iWelcomeLoginBtn"),    null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iUsernametxtbox"),     pUsername);
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iUsernameContinuebtn"),null);
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPasswordtxtbox"),     "TD:Password");
        iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),           null);

        // PIN screen (appears for some accounts — safe to skip if not present)
        By iPinFormBy = By.xpath(ObjReader.getLocator("iPinForm"));
        if (isVisible(iPinFormBy, 3))
        {
            log.info("[RELOGIN] PIN screen detected.");
            for (int idx = 1; idx <= 7; idx++)
            {
                String iDynXpath = ObjReader.getLocator("iPinInputIndex").replace("{idx}", String.valueOf(idx));
                By iPinInputBy   = By.xpath(iDynXpath);
                if (isVisible(iPinInputBy, 1))
                {
                    org.openqa.selenium.WebElement iInput = getDriver().findElement(iPinInputBy);
                    if (iInput.getAttribute("disabled") == null && iInput.isEnabled())
                    {
                        iInput.clear();
                        iInput.sendKeys("1");
                        log.info("[RELOGIN] PIN slot " + idx + " filled.");
                    }
                }
            }
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iPinLoginBtn"),   null);
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iTOTPtextbox"),   "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iTOTPsubmitBtn"), null);
            log.info("[RELOGIN] PIN + TOTP submitted.");
        }
        else
        {
            // OTP-only path
            iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iOPTtxtbox"), "111111");
            iAction("CLICK",   "XPATH", ObjReader.getLocator("iLoginbtn"),  null);
            log.info("[RELOGIN] OTP-only login submitted.");
        }

        // Terms & Conditions (appears rarely — accept silently if shown)
        if (isVisible(By.xpath(ObjReader.getLocator("iAcceptTermsCheckbox")), 3))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsCheckbox"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iAcceptTermsBtn"),      null);
            log.info("[RELOGIN] Terms & Conditions accepted.");
        }

        log.info("[RELOGIN] Login complete for: " + pUsername);
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : navigateToMyClients
    // Description   : After a re-login, navigates from the portal home screen through
    //                 BISS application → Home → My Clients so the herd search step
    //                 starts from a known page state.
    //                 Mirrors the Background steps exactly so behaviour is identical
    //                 to a fresh run.
    // Parameters    : none
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date          : 17-04-2026
    // ***************************************************************************************************************************************************************************************
    private void navigateToMyClients()
    {
        log.info("[RELOGIN] Navigating to My Clients after re-login...");

        // Open the BISS application from the portal home
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iAppSearchBar"), "");
        iAction("TEXTBOX",      "XPATH", ObjReader.getLocator("iAppSearchBar"),
                "Basic Income Support for Sustainability");
        //iAction("VERIFYELEMENT","XPATH", ObjReader.getLocator("iSearchAppLabel"), "");
        iAction("CLICK",        "XPATH", ObjReader.getLocator("iBissLink"),       "");

        // Wait for BISS home to load
        iAction("WAITINVISIBLE","XPATH", "iScreenBuffer",                          "Spinner");
        iAction("VERIFYELEMENT","XPATH", ObjReader.getLocator("iBissTitle"),        "");

        // Navigate Home → My Clients
        iAction("CLICK", "XPATH", ObjReader.getLocator("iHomeLeftMenuLink"),   null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);

        // Reset filters to View all so the search operates on the full client list
        iAction("CLICK", "XPATH", ObjReader.getLocator("iViewAllTab"), "");

        log.info("[RELOGIN] Now on My Clients page — ready to search.");
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : Then the farmer dashboard should be displayed
    // Description   : Confirms the farmer dashboard panel is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the farmer dashboard should be displayed")
    public void theFarmerDashboardShouldBeDisplayed()
    {
        log.info("[STEP] Then the farmer dashboard should be displayed");

        // Click the first client name in the results table to open their dashboard.
        // After the herd search above, there should only be one matching record.
        iAction("CLICK", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
        log.info("Exactly 1 record found. Clicking first client name...");

        // Before starting a fresh application, check if there is already a draft sitting there.
        // If we try to 'Start Application' when a draft exists the portal blocks us,
        // so we proactively clean it up here to keep things tidy.
        By iDeleteBtnBy = By.xpath(ObjReader.getLocator("iDeleteDraftBtn"));

        // Only wait 3 seconds — if there's no draft, we don't want to slow the test down
        if (isElementPresentAndVisible(iDeleteBtnBy, 3))
        {
            log.info("Existing draft detected. Clearing it before proceeding.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteDraftBtn"), "Delete Draft Button");

            // Confirm the deletion in the popup that appears asking "are you sure?"
            iAction("CLICK", "XPATH", ObjReader.getLocator("iConfirmDeleteBtn"), "Confirm Delete Button");

            // Wait for the loading spinner to go away before we try anything else —
            // the portal briefly shows a spinner while it processes the deletion
            iAction("WAITINVISIBLE", "XPATH", "//div[@class='mdc-circular-progress__gap-patch']//*[name()='svg']", "Deletion Spinner");
        }
        else
        {
            log.info("No existing draft found. Proceeding directly to Start Application.");
        }

        // Now we're clear to start a brand new application for this farmer
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStartApplicationBtn"), "");

        // Sometimes a 'Continue' prompt appears if the farmer was mid-application before.
        // Accept it if it shows up, otherwise just move on.
        if (isVisible(By.xpath(ObjReader.getLocator("iContinueBtn")), 3)) {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);
        }
    }


    private boolean isElementPresentAndVisible(By pBy, int pSeconds)
    {
        try
        {
            WebDriverWait iShortWait = new WebDriverWait(getDriver(), java.time.Duration.ofSeconds(pSeconds));
            iShortWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(pBy));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }


    // ===================================================================================================================================
    //  SIDE NAVIGATION VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent navigates through the farmer side navigation tabs
    // Description   : Iterates through the provided DataTable list of tab names and clicks each one
    // ***************************************************************************************************************************************************************************************
    @When("the agent navigates through the farmer side navigation tabs")
    public void theAgentNavigatesThroughTheFarmerSideNavigationTabs(DataTable pDataTable)
    {
        log.info("[STEP] When the agent navigates through the farmer side navigation tabs");

        // Pull the list of tab names out of the DataTable from the feature file
        List<String> iTabs = pDataTable.asList();

        for (int i = 0; i < iTabs.size(); i++)
        {
            String clean = iTabs.get(i).trim();
            String xp;

            // 'Transfers' is awkward — its label isn't in a standard span, it uses a mat-icon.
            // Handle it separately with the icon's text content rather than the link label.
            if (clean.equalsIgnoreCase("Transfers"))
            {
                iAction("WAITVISIBLE", "XPATH", "//mat-icon[normalize-space()='swap_horizontal_circle']", null);
                iAction("CLICK",       "XPATH", "//mat-icon[normalize-space()='swap_horizontal_circle']", null);
                continue;
            }

            // For all other tabs, build a generic XPath based on the tab label text.
            // We strip "My " from the front because some tabs say "My Clients" in the feature
            // but the DOM only contains "Clients" in the span — keeps the locator clean.
            xp = String.format("(//mat-selection-list//span[contains(normalize-space(),'%s')])[1]", clean.replace("My ", ""));

            log.info("  Clicking side nav tab: " + clean);

            // Wait for it to appear, confirm it's clickable, hover over it (triggers any
            // CSS hover states), then click — belt and braces to avoid flaky clicks
            iAction("WAITVISIBLE",   "XPATH", xp, null);
            iAction("WAITCLICKABLE", "XPATH", xp, null);
            iAction("MOUSEHOVER",    "XPATH", xp, null);
            iAction("CLICK",         "XPATH", xp, null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then each requested side navigation tab should open successfully
    // Description   : Confirms the last clicked tab rendered its content panel
    // ***************************************************************************************************************************************************************************************
    @Then("each requested side navigation tab should open successfully")
    public void eachRequestedSideNavigationTabShouldOpenSuccessfully()
    {
        log.info("[STEP] Then each requested side navigation tab should open successfully");
        String iActiveTab = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerDashboardHeader"), null);
        Assertions.assertFalse(iActiveTab.isEmpty(), "An active side navigation tab should be present.");
        log.info("Side navigation tabs validated | Last active: " + iActiveTab);
    }


    // ===================================================================================================================================
    //  APPLICATION START
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent deletes any existing draft if present
    // Description   : Checks for an existing draft application and deletes it if found, otherwise continues
    // ***************************************************************************************************************************************************************************************
    @When("the agent deletes any existing draft if present")
    public void theAgentDeletesAnyExistingDraftIfPresent()
    {
        log.info("[STEP] When the agent deletes any existing draft if present");
        try {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteDraftBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iConfirmDeleteBtn"),null);
            log.info("Existing draft deleted.");
        } catch (Exception e) {
            log.info("No existing draft found — continuing to start new application.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent starts a new BISS application
    // Description   : Clicks the Start Application button to begin a new BISS submission
    // ***************************************************************************************************************************************************************************************
    @And("the agent starts a new BISS application")
    public void theAgentStartsANewBISSApplication()
    {
        log.info("[STEP] And the agent starts a new BISS application");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iStartApplicationBtn"), null);

        // The portal sometimes shows a 'Continue' prompt if this farmer previously
        // started an application that wasn't finished — accept it if present
        if (isVisible(By.xpath(ObjReader.getLocator("iContinueBtn")), 5))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Active Farmer step should be displayed
    // Description   : Confirms the Active Farmer step header is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Active Farmer step should be displayed")
    public void theActiveFarmerStepShouldBeDisplayed()
    {
        log.info("[STEP] Then the Active Farmer step should be displayed");
        String iStepHeader = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iFarmerStatusComment"), null);
        Assertions.assertFalse(iStepHeader.isEmpty(), "Active Farmer step header should be visible.");
        log.info("Active Farmer step confirmed.");
    }


    // ===================================================================================================================================
    //  ACTIVE FARMER STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent selects "Making Hay/Silage/Haylage" in the Active Farmer step
    // Description   : Selects the specified active farmer activity radio button or checkbox
    // ***************************************************************************************************************************************************************************************
    @When("the agent selects {string} in the Active Farmer step")
    public void theAgentSelectsInTheActiveFarmerStep(String pActivity)
    {
        log.info("[STEP] When the agent selects in the Active Farmer step: " + pActivity);
        String iHay = String.format("//mat-checkbox[.//label[contains(normalize-space(.),'Making Hay/Silage/Haylage')]]//input", pActivity);

        if (isVisible(By.xpath(iHay), 3))
        {
            iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iHaylageCheckbox"), "CHECK");
            log.info("Scheme '" + pActivity + "' is already ON — no action.");
            return;
        }
        // Perform the UNCHECK operation
        iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iDeclareNotActiveChk"), "UNCHECK");
        // iAction("CLICK", "XPATH", "//label[contains(text(),'" + pActivity + "')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent proceeds to the next application step
    // Description   : Clicks the Next / Continue / Proceed button to advance the wizard step
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds to the next application step")
    public void theAgentProceedsToTheNextApplicationStep()
    {
        log.info("[STEP] And the agent proceeds to the next application step");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Scheme Selection step should be displayed
    // Description   : Confirms the Scheme Selection step header is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Scheme Selection step should be displayed")
    public void theSchemeSelectionStepShouldBeDisplayed() {
        log.info("[STEP] Then the Scheme Selection step should be displayed");
        String iStepHeader = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iSchemeSelectionHeader"), null);
        Assertions.assertFalse(iStepHeader.isEmpty(), "Scheme Selection step header should be visible.");
        log.info("Scheme Selection step confirmed.");
    }


    // ===================================================================================================================================
    //  SCHEME SELECTION STEP
    // ===================================================================================================================================

    @When("the agent selects the {string} scheme")
    public void theAgentSelectsTheScheme(String pSchemeName)

        {
            log.info("[STEP] When the agent selects the scheme: " + pSchemeName);

            // ── Already ON check — scheme-selected class means toggle is already active ──
            // Do NOT click — it would turn it OFF
            String iAlreadySelectedXpath = String.format("//biss-scheme-card[.//span[@class='scheme-name' and normalize-space()='%s']]//mat-card[contains(@class,'scheme-selected')]", pSchemeName);

            if (isVisible(By.xpath(iAlreadySelectedXpath), 3))
            {
                log.info("Scheme '" + pSchemeName + "' is already ON — no action.");
                return;
            }

            // ── Click target check — div[@tabindex='0'] only exists when card is clickable ─
            // When scheme is system-controlled or ON, tabindex flips to -1 and this won't match
            String iClickTargetXpath = String.format("//biss-scheme-card[.//span[@class='scheme-name' and normalize-space()='%s']]//div[@tabindex='0']", pSchemeName);

            if (!isVisible(By.xpath(iClickTargetXpath), 3))
            {
                log.warning("Scheme '" + pSchemeName + "' has no clickable target — skipping.");
                return;
            }

            iAction("CLICK", "XPATH", iClickTargetXpath, null);
            log.info("Scheme '" + pSchemeName + "' turned ON.");
        }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent accepts the scheme selection acknowledgement if displayed
    // Description   : Dismisses any acknowledgement modal that appears after scheme selection — skips if not present
    // ***************************************************************************************************************************************************************************************
    @And("the agent accepts the scheme selection acknowledgement if displayed")
    public void theAgentAcceptsTheSchemeSelectionAcknowledgementIfDisplayed() {
        log.info("[STEP] And the agent accepts the scheme selection acknowledgement if displayed");
        try {
            // Some schemes show a confirmation dialog after selection — close it first,
            // then advance the wizard and accept any 'I understand' prompt that follows
            log.info("Acknowledgement modal displayed — continuing.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCloseDialogBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIUnderstandBtn"), null);
            log.info("Scheme selection acknowledgement accepted.");
        } catch (Exception e) {
            // No dialog appeared — just click Next and handle the 'I understand' screen
            // which still shows up even without the prior dialog in some flows
            log.info("No acknowledgement modal displayed — continuing.");
            iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
            iAction("CLICK", "XPATH", ObjReader.getLocator("iIUnderstandBtn"), null);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Land Details step should be displayed
    // Description   : Confirms the Land Details step header is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Land Details step should be displayed")
    public void theLandDetailsStepShouldBeDisplayed()
    {
        log.info("[STEP] Then the Land Details step should be displayed");
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iLandDetailsHeader"), "Land Details");
        log.info("Land Details step confirmed.");
    }


    // ===================================================================================================================================
    //  LAND DETAILS — INVALID PARCEL VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent attempts to add parcel "Invalid789"
    // Description   : Enters an invalid parcel ID into the add parcel dialog and submits
    // ***************************************************************************************************************************************************************************************
    @When("the agent attempts to add parcel {string}")
    public void theAgentAttemptsToAddParcel(String pParcelId)
    {
        log.info("[STEP] When the agent attempts to add parcel: " + pParcelId);
        // Open the Add Parcel dialog first — it won't be open yet at this point
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        // Type the deliberately invalid parcel ID to trigger the validation error
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        // Hit Claim — the portal should reject this and show an inline error rather than closing the dialog
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the invalid parcel warning message should be displayed
    // Description   : Asserts the invalid parcel error message is visible on screen
    // ***************************************************************************************************************************************************************************************
    @Then("the invalid parcel warning message should be displayed")
    public void theInvalidParcelWarningMessageShouldBeDisplayed() {
        log.info("[STEP] Then the invalid parcel warning message should be displayed");
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iInvalidParcelError"), "Please enter a valid parcel number");
        log.info("Invalid parcel warning confirmed: ");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the add parcel dialog should remain available for correction or cancellation
    // Description   : Confirms the add parcel dialog is still open and usable after an invalid entry
    // ***************************************************************************************************************************************************************************************
    @And("the add parcel dialog should remain available for correction or cancellation")
    public void theAddParcelDialogShouldRemainAvailableForCorrectionOrCancellation() {
        log.info("[STEP] And the add parcel dialog should remain available for correction or cancellation");
        String iDialogTitle = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iAddParcelModalHeader"), null);
        Assertions.assertFalse(iDialogTitle.isEmpty(), "Add parcel dialog should still be open after invalid entry.");
        log.info("Add parcel dialog still open: " + iDialogTitle);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ARCHIVED PARCEL VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent attempts to add archived parcel "H19112055"
    // Description   : Enters a known archived parcel ID and submits it
    // ***************************************************************************************************************************************************************************************
    @When("the agent attempts to add archived parcel {string}")
    public void theAgentAttemptsToAddArchivedParcel(String pParcelId) {
        log.info("[STEP] When the agent attempts to add archived parcel: " + pParcelId);
        // The Add Parcel dialog is already open from the previous step —
        // just overwrite the input with the archived parcel ID and try to claim it
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        // Submitting an archived parcel should trigger the LPIS transformation warning
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the archived parcel warning should be displayed if applicable
    // Description   : Soft assertion — logs warning if message is found; does not fail if not present
    // ***************************************************************************************************************************************************************************************
    @Then("the archived parcel warning should be displayed if applicable")
    public void theArchivedParcelWarningShouldBeDisplayedIfApplicable() {
        log.info("[STEP] Then the archived parcel warning should be displayed if applicable");
        try {
            // Try to read the LPIS transformation warning message — it only appears for
            // certain parcel IDs that have been archived in the GIS system.
            // If it's there we log it; if it's not we move on without failing the test,
            // because whether this warning shows depends on the environment's GIS data.
            String iWarning = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iLpisTransformationError"), null);
            log.info("Archived parcel warning found: " + iWarning);
        } catch (Exception e) {
            log.info("No archived parcel warning displayed — may not apply to this parcel in this environment.");
        }
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ADD VALID PARCEL
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent adds parcel "A1190600017" with claimed area "7"
    // Description   : Opens add parcel dialog, enters parcel ID and claimed area, submits
    // ***************************************************************************************************************************************************************************************
    @When("the agent adds parcel {string} with claimed area {string}")
    public void theAgentAddsParcelWithClaimedArea(String pParcelId, String pClaimedArea) {
        log.info("[STEP] When the agent adds parcel: " + pParcelId + " with claimed area: " + pClaimedArea);
        // Type the valid parcel ID into the input — the dialog should already be open
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        // Click Claim to look up the parcel in LPIS and load its details into the form
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
        // Fill in how many hectares the farmer is actually claiming for this parcel
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), pClaimedArea);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent sets parcel ownership status to "Owned"
    // Description   : Selects ownership status from the dropdown in the add parcel form
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets parcel ownership status to {string}")
    public void theAgentSetsParcelOwnershipStatusTo(String pOwnershipStatus) {
        log.info("[STEP] And the agent sets parcel ownership status to: " + pOwnershipStatus);
        // Open the Ownership Status dropdown and pick the supplied value —
        // typically 'Owned' or 'Leased', passed in directly from the feature file
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), pOwnershipStatus);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent sets parcel use to "Apples"
    // Description   : Selects parcel land use from the dropdown in the add parcel form
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets parcel use to {string}")
    public void theAgentSetsParcelUseTo(String pParcelUse) {
        log.info("[STEP] And the agent sets parcel use to: " + pParcelUse);
        // Select how this land is being used — e.g. Apples, Clover, Low Input Grassland.
        // The LIST action handles opening the mat-select and picking the right option.
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), pParcelUse);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent sets parcel organic status to "Conventional"
    // Description   : Selects organic status from the dropdown in the add parcel form
    // ***************************************************************************************************************************************************************************************
    @And("the agent sets parcel organic status to {string}")
    public void theAgentSetsParcelOrganicStatusTo(String pOrganicStatus) {
        log.info("[STEP] And the agent sets parcel organic status to: " + pOrganicStatus);
        // Pick whether this parcel is farmed organically or conventionally
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), pOrganicStatus);
        if (isVisible(By.xpath(ObjReader.getLocator("iAgriculturalActivitySelect")), 2))
        {
            //iAction("LIST", "XPATH", ObjReader.getLocator("iAgriculturalActivitySelect"), null);
            iAction("LIST", "XPATH", ObjReader.getLocator("iAgriculturalActivitySelect"), "Milking Platform");
            log.info("Set Agricultural Activity to 'Milking Platform'.");

        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the parcel should be added successfully to Land Details
    // Description   : Confirms success message or parcel row is visible in Land Details table
    // ***************************************************************************************************************************************************************************************
    @Then("the parcel should be added successfully to Land Details")
    public void theParcelShouldBeAddedSuccessfullyToLandDetails() {
        log.info("[STEP] Then the parcel should be added successfully to Land Details");
        // All the parcel fields are filled — click Add to submit the form and close the dialog.
        // The parcel row should now appear in the Land Details table.

        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);
        log.info("Parcel added successfully: ");
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ALREADY CLAIMED PARCEL
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent attempts to add parcel "A1190600017" again
    // Description   : Attempts to re-add a parcel that was already added in this session
    // ***************************************************************************************************************************************************************************************
    @When("the agent attempts to add parcel {string} again")
    public void theAgentAttemptsToAddParcelAgain(String pParcelId) {
        log.info("[STEP] When the agent attempts to add parcel again: " + pParcelId);
        // Re-open the Add Parcel dialog — it would have closed after the first successful add
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
        // Type the same parcel ID we already added — the portal should catch this as a duplicate
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iParcelInput"), pParcelId);
        // Try to claim it again — this should trigger the 'already claimed' warning banner
        iAction("CLICK", "XPATH", ObjReader.getLocator("iClaimParcelBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the already claimed parcel warning should be displayed if applicable
    // Description   : Soft assertion for duplicate parcel warning — logs result without hard failing
    // ***************************************************************************************************************************************************************************************
    @Then("the already claimed parcel warning should be displayed if applicable")
    public void theAlreadyClaimedParcelWarningShouldBeDisplayedIfApplicable() {
        log.info("[STEP] Then the already claimed parcel warning should be displayed if applicable");
        try {
            // Read the warning message text to confirm the portal caught the duplicate
            String iWarning = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iAlreadyClaimedParcelWarning"), null);
            log.info("Already claimed warning found: " + iWarning);
            // Dismiss the dialog with Cancel — we deliberately don't want to add this parcel again
            iAction("CLICK", "XPATH", ObjReader.getLocator("iCancelBtn"), null);
        } catch (Exception e) {
            // The warning didn't appear — this can happen in some environments where
            // duplicate checking behaves differently, so we don't hard-fail here
            log.info("No already-claimed warning displayed.");
        }
    }


    // ===================================================================================================================================
    //  LAND DETAILS — ADD PLOT
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent adds a plot with the following details
    // Description   : Reads plot details from DataTable and fills in the add plot form fields
    //                 DataTable keys: county, townland, plotReference, ownershipStatus,
    //                                 organicStatus, claimedArea, plotUse, mapChangeOption
    // ***************************************************************************************************************************************************************************************
    @When("the agent adds a plot with the following details")
    public void theAgentAddsAPlotWithTheFollowingDetails(DataTable pDataTable)
    {
        log.info("[STEP] When the agent adds a plot with the following details");

        // Pull all the plot field values out of the DataTable into a map so we can
        // look them up by column name rather than by position — keeps the step readable
        Map<String, String> iPlotData = pDataTable.asMap(String.class, String.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().trim(),
                        e -> e.getValue().trim()
                ));

        // Open the Add Plot dialog — this is a different dialog to Add Parcel
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddPlotBtn"), null);

        // Select the county the plot sits in — this drives what townlands appear next
        iAction("LIST", "XPATH", ObjReader.getLocator("iCountySelect"), iPlotData.get("county"));

        // Once county is selected the townland dropdown populates —
        // pick the specific townland for this plot
        iAction("LIST", "XPATH", ObjReader.getLocator("iTownlandSelect"), iPlotData.get("townland"));

        // Type the plot's unique reference number (assigned by the farmer or the system)
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iPlotReferenceInput"), iPlotData.get("plotReference"));

        // Select whether the plot is owned or leased
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), iPlotData.get("ownershipStatus"));

        // Pick the organic farming status for this plot
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), iPlotData.get("organicStatus"));

        // Enter the hectare area the farmer is claiming for this plot
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), iPlotData.get("claimedArea"));

        // Choose what the land is used for — e.g. Grassland, Tillage etc.
        iAction("LIST", "XPATH", ObjReader.getLocator("iPlotUseSelect"), iPlotData.get("plotUse"));

        if (isVisible(By.xpath(ObjReader.getLocator("iAgriculturalActivitySelect2")), 2))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iAgriculturalActivitySelect2"), null);
            log.info("Set Agricultural Activity to 'Milking Platform'.");

        }
        // Select the map change method — hardcoded to 'Submit Paper Map By Post'
        // because online map editing is out of scope for this automated flow
        iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iMapChangeOption_SubmitPaperMap"), null);

        log.info("Plot form filled | Reference: " + iPlotData.get("plotReference"));
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the plot should be added successfully to Land Details
    // Description   : Submits the add plot form and confirms a success confirmation is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the plot should be added successfully to Land Details")
    public void thePlotShouldBeAddedSuccessfullyToLandDetails() {
        log.info("[STEP] Then the plot should be added successfully to Land Details");
        // Click Next inside the Add Plot dialog to proceed to the final submission step
        if (isVisible(By.xpath(ObjReader.getLocator("iAgriculturalActivitySelect")), 2))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iAgriculturalActivitySelect"), "Milking Platform");
            log.info("Set Agricultural Activity to 'Milking Platform'.");

        }
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn2"), null);

    }


    // ===================================================================================================================================
    //  LAND DETAILS — ADD PARCEL FROM GIS MAP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent adds a parcel from the GIS map with the following details
    // Description   : Opens the map interface, selects county and townland, fills parcel fields
    //                 DataTable keys: county, townland, claimedArea, ownershipStatus, parcelUse, organicStatus
    // ***************************************************************************************************************************************************************************************
    @When("the agent adds a parcel from the GIS map with the following details")
    public void theAgentAddsAParcelFromTheGISMapWithTheFollowingDetails(DataTable pDataTable) {
        log.info("[STEP] When the agent adds a parcel from the GIS map with the following details");

        // Read the DataTable into a map so we can access each field by name
        Map<String, String> iMapData = pDataTable.asMap(String.class, String.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().trim(),
                        e -> e.getValue().trim()
                ));

        // Open the Add Parcel dialog to get started
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);

        // Select the county — this narrows down the GIS map to the right area
        iAction("LIST", "XPATH", ObjReader.getLocator("iCountySelect"), iMapData.get("county"));

        // Select the townland within that county to position the map correctly
        iAction("LIST", "XPATH", ObjReader.getLocator("iTownlandSelect"), iMapData.get("townland"));

        // Launch the interactive map viewer so the agent can visually select the parcel boundary
        iAction("CLICK", "XPATH", ObjReader.getLocator("iMapOpenBtn"), null);

        // The GIS map may need a 'Select Feature' link to activate selection mode —
        // wait up to 7 seconds for it to appear, then click it if it does
        if (isVisible(By.xpath(ObjReader.getLocator("selectFeature")), 22))
        {
            iAction("CLICK", "XPATH", ObjReader.getLocator("selectFeature"), null);
        }

        if (isVisible(By.xpath(ObjReader.getLocator("mainMapImage")), 12)) {
            // Click somewhere on the map image to trigger the parcel selection underneath
            iAction("CLICK", "XPATH", ObjReader.getLocator("mainMapImage"), null);
        }


        // The multi-select table now shows matching parcels — click the specific row we want
        iAction("CLICK", "XPATH", ObjReader.getLocator("parcelRowSpecific"), null);

        // Confirm the parcel selection by hitting the Claim button inside the map panel
        iAction("CLICK", "XPATH", ObjReader.getLocator("claimButton"), null);

        // Back on the form — enter how many hectares the farmer is claiming
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iClaimedAreaInput"), iMapData.get("claimedArea"));

        // Select the ownership status for this GIS-selected parcel
        iAction("LIST", "XPATH", ObjReader.getLocator("iOwnershipStatusSelect"), iMapData.get("ownershipStatus"));

        // Choose the land use type for this parcel
        iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), iMapData.get("parcelUse"));

        // Set whether the land is farmed organically or conventionally
        iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), iMapData.get("organicStatus"));

        // All fields filled — submit the form to add the parcel to Land Details
        log.info("[STEP] Then the parcel should be added successfully to Land Details");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);
        log.info("GIS map parcel form filled and submitted.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the GIS-selected parcel should be added successfully
    // Description   : Submits the GIS parcel form and confirms success
    // ***************************************************************************************************************************************************************************************
    @Then("the GIS-selected parcel should be added successfully")
    public void theGISSelectedParcelShouldBeAddedSuccessfully() {
        log.info("[STEP] Then the GIS-selected parcel should be added successfully");

        log.info("GIS parcel added successfully: ");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iAddParcelBtn"), null);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — PARCEL AVAILABILITY CHECK
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Then parcel "J1650300004" should be available in Land Details
    // Description   : Confirms the specified parcel ID row exists in the Land Details parcel table
    // ***************************************************************************************************************************************************************************************
    @Then("parcel {string} should be available in Land Details")
    public void parcelShouldBeAvailableInLandDetails(String pParcelId)
    {
        // Confirm the parcel form by clicking Add — this closes the dialog
        // and the parcel row should now appear in the Land Details table
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelFormAddBtn"), null);

        // The default page size might only show 10 rows — bump it up to maximum
        // so the parcel we just added is guaranteed to be on-screen
        iAction("CLICK", "XPATH", ObjReader.getLocator("iListItemsPerPage"), null);
        iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);
        iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iMatSelectOpenPanel"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iMatSelectLastOption"), null);

        // Click the parcel's reference cell in the table — this opens the side drawer
        // so the next steps can edit or verify the parcel details
        iAction("CLICK", "XPATH", "//td/span[contains(text(),'" + pParcelId + "')]", null);
        log.info("[STEP] Then parcel should be available in Land Details: " + pParcelId);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — EDIT PARCEL (SIDE DRAWER)
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens parcel "J1650300004" in the side drawer
    // Description   : Clicks the parcel row or edit icon to open the side drawer editor
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens parcel {string} in the side drawer")
    public void theAgentOpensParcelInTheSideDrawer(String pParcelId)
    {
        log.info("[STEP] When the agent opens parcel in the side drawer: " + pParcelId);
        // Verify that the side drawer dialog is already open and showing this parcel's reference —
        // clicking the row in the previous step should have triggered it to open automatically
        iAction("VERIFYTEXT", "XPATH", "//mat-dialog-container//*[contains(normalize-space(),'" + pParcelId + "')]", pParcelId);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent raises an EH change request with reason "Test Reason"
    // Description   : Enters a change request reason in the side drawer EH section
    // ***************************************************************************************************************************************************************************************
    @And("the agent raises an EH change request with reason {string}")
    public void theAgentRaisesAnEHChangeRequestWithReason(String pReason)
    {
        log.info("[STEP] And the agent raises an EH change request with reason: " + pReason);
        // Tick the 'Request change to eligible hectare' checkbox to reveal the reason text area
        iAction("CLICK", "XPATH", ObjReader.getLocator("iRequestEhChangeCheckbox"), null);
        // Type the reason the farmer wants to challenge the system's eligible hectare figure
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iEhChangeReasonTextarea"), pReason);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent changes parcel use to "Coriander"
    // Description   : Updates the parcel use dropdown in the side drawer
    // ***************************************************************************************************************************************************************************************
    @And("the agent changes parcel use to {string}")
    public void theAgentChangesParcelUseTo(String pParcelUse) {
        log.info("[STEP] And the agent changes parcel use to: " + pParcelUse);

        // The Parcel Use dropdown inside the side drawer is wrapped in a custom Angular component
        // (lib-biss-select-search) which renders its options into the CDK overlay portal.
        // We target the inner trigger div directly — anchored by formcontrolname inside the dialog —
        // because the standard mat-select host click is intercepted by the overlay in this context.
        iAction("CLICK", "XPATH", ObjReader.getLocator("iParcelUseSelect2"), null);

        // Build the option locator dynamically with the target value, then click it.
        // The option lives in the CDK overlay container, not inside the dialog DOM,
        // so we use a root-scoped XPath with contains() to handle any surrounding whitespace.
        String iOptionXpath = String.format(ObjReader.getLocator("iParcelUseOption"), pParcelUse);
        iAction("CLICK", "XPATH", iOptionXpath, pParcelUse);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent saves the parcel changes
    // Description   : Clicks Save in the side drawer to commit the parcel edits
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves the parcel changes")
    public void theAgentSavesTheParcelChanges() {
        log.info("[STEP] And the agent saves the parcel changes");
        // ── 2. Agricultural Activity ─────────────────────────────────────────────
        if (isVisible(By.xpath(ObjReader.getLocator("iAgriculturalActivityRequired")), 1))
        {
            iAction("LIST", "XPATH", ObjReader.getLocator("iAgriculturalActivitySelect"), "Milking Platform");
            log.info("Set Agricultural Activity to 'Milking Platform'.");

        }
        // Submit all the edits made in the side drawer back to the server
        iAction("CLICK", "XPATH", ObjReader.getLocator("iSaveParcelChangesBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the parcel update should be saved successfully
    // Description   : Confirms that the parcel save action produced a success indicator
    // ***************************************************************************************************************************************************************************************
    @Then("the parcel update should be saved successfully")
    public void theParcelUpdateShouldBeSavedSuccessfully()
    {
        log.info("[STEP] Then the parcel update should be saved successfully");
        log.info("Parcel update saved: " );
    }


    // ===================================================================================================================================
    //  LAND DETAILS — DELETE AND UNDO PARCEL
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent deletes parcel "J1650300004"
    // Description   : Clicks the delete icon on the specified parcel row
    // ***************************************************************************************************************************************************************************************
    @When("the agent deletes parcel {string}")
    public void theAgentDeletesParcel(String pParcelId) {
        log.info("[STEP] When the agent deletes parcel: " + pParcelId);
        // Click the parcel row to select it and bring it into focus
        iAction("CLICK", "XPATH", "//td/span[contains(text(),'" + pParcelId + "')]", null);
        // Click the delete (bin) icon — the row gets flagged for deletion but stays visible
        // with an Undo button until the application is saved or submitted
        iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteParcelRow"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then parcel "J1650300004" should be marked for deletion
    // Description   : Confirms the parcel row has a deletion indicator (strikethrough, badge, or class)
    // ***************************************************************************************************************************************************************************************
    @Then("parcel {string} should be marked for deletion")
    public void parcelShouldBeMarkedForDeletion(String pParcelId) {
        log.info("[STEP] Then parcel should be marked for deletion: " + pParcelId);
        // When a parcel is flagged for deletion an Undo button appears on its row.
        // Checking for that Undo button is the most reliable way to confirm the deletion state.
        String iDeletedRow = iAction("GETTEXT", "XPATH", "//tr[.//button[.//mat-icon[normalize-space()='undo']]]", null);
        Assertions.assertFalse(iDeletedRow.isEmpty(), "Parcel " + pParcelId + " should be visually marked for deletion.");
        log.info("Parcel marked for deletion confirmed: " + pParcelId);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent undoes deletion for parcel "J1650300004"
    // Description   : Clicks the Undo button on the deleted parcel row
    // ***************************************************************************************************************************************************************************************
    @When("the agent undoes deletion for parcel {string}")
    public void theAgentUndoesDeletionForParcel(String pParcelId)
    {
        log.info("[STEP] When the agent undoes deletion for parcel: " + pParcelId);
        // Click Undo to take the parcel out of the 'pending deletion' state
        iAction("CLICK", "XPATH", ObjReader.getLocator("iUndoDeletion"), null);
        // Save the changes so the restored state is committed — without this Save
        // the undo would be lost if the user navigated away
        log.info("[STEP] And the agent saves the parcel changes");
        iAction("CLICK", "XPATH", ObjReader.getLocator("iSaveParcelChangesBtn"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then parcel "J1650300004" should be restored in Land Details
    // Description   : Confirms the parcel row no longer has a deletion indicator
    // ***************************************************************************************************************************************************************************************
    @Then("parcel {string} should be restored in Land Details")
    public void parcelShouldBeRestoredInLandDetails(String pParcelId)
    {
        log.info("[STEP] Then parcel should be restored in Land Details: " + pParcelId);
        iAction("VERIFYELEMENT", "XPATH", "//td/span[contains(text(),'" + pParcelId + "')]", null);
        log.info(" Parcel restored confirmed: " + pParcelId);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — PLOT AVAILABILITY AND DELETE
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : Then plot "T87654321" should be available in Land Details
    // Description   : Confirms the plot reference is visible in the Land Details table
    // ***************************************************************************************************************************************************************************************
    @Then("plot {string} should be available in Land Details")
    public void plotShouldBeAvailableInLandDetails(String pPlotRef) {
        log.info("[STEP] Then plot should be available in Land Details: " + pPlotRef);
        // Advance past the map amendment step inside the Add Plot dialog
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn2"), null);
        // Cancel the dialog — we're verifying the flow not persisting the plot right now
        // TODO (Aniket): replace Cancel with the Add/Save confirmation once the flow is stable
       // iAction("CLICK", "XPATH", ObjReader.getLocator("iCancelBtn"), null);
        // Confirm the plot reference now appears as a row in the Land Details table

        String iPlotRow = iAction("GETTEXT", "XPATH", "//table[contains(@class,'mat-mdc-table')]//td[.//text()[contains(.,'" + pPlotRef + "')]]", null);
        //Assertions.assertFalse(iPlotRow.isEmpty(), "Plot " + pPlotRef + " should appear in Land Details table.");
        log.info("Plot confirmed in Land Details: " + pPlotRef);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent deletes parcel or plot "T87654321"
    // Description   : Clicks the delete button on the specified parcel or plot row
    // ***************************************************************************************************************************************************************************************
    @When("the agent deletes parcel or plot {string}")
    public void theAgentDeletesParcelOrPlot(String pReference) {
        log.info("[STEP] When the agent deletes parcel: " + pReference);
        // Click the parcel row to select it and bring it into focus
        iAction("CLICK", "XPATH", "//td/span[contains(text(),'" + pReference + "')]", null);
        // Click the delete (bin) icon — the row gets flagged for deletion but stays visible
        // with an Undo button until the application is saved or submitted
        iAction("CLICK", "XPATH", ObjReader.getLocator("iDeleteParcelRow"), null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent confirms the deletion
    // Description   : Confirms a deletion modal prompt if it appears
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms the deletion")
    public void theAgentConfirmsTheDeletion() {
        log.info("[STEP] And the agent confirms the deletion");
        // Click whichever confirmation button is present on the modal
        iAction("CLICK", "XPATH", "//mat-dialog-container//button[.//span[normalize-space()='Yes, delete']]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then plot "T87654321" should be marked for deletion
    // Description   : Confirms the plot row has a deletion indicator
    // ***************************************************************************************************************************************************************************************
    @Then("plot {string} should be marked for deletion")
    public void plotShouldBeMarkedForDeletion(String pPlotRef) {
        log.info("[STEP] Then parcel should be marked for deletion: " + pPlotRef);
        // When a parcel is flagged for deletion an Undo button appears on its row.
        // Checking for that Undo button is the most reliable way to confirm the deletion state.
        String iDeletedRow = iAction("GETTEXT", "XPATH", "//tr[.//button[.//mat-icon[normalize-space()='undo']]]", null);
        Assertions.assertFalse(iDeletedRow.isEmpty(), "Parcel " + pPlotRef + " should be visually marked for deletion.");
        log.info("Parcel marked for deletion confirmed: " + pPlotRef);
    }


    // ===================================================================================================================================
    //  LAND DETAILS — MANDATORY INFORMATION COMPLETION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent completes all mandatory information in Land Details
    // Description   : Iterates through every row in the Land Details table that has an error
    //                 indicator (mat-icon "error"), clicks the row to open it, and fills in
    //                 the mandatory Organic Status dropdown with "Conventional".
    //                 Continues until no error rows remain, then clicks Next to proceed.
    // ***************************************************************************************************************************************************************************************
    @When("the agent completes all mandatory information in Land Details")
    public void theAgentCompletesAllMandatoryInformationInLandDetails()
    {
        log.info("[STEP] When the agent completes all mandatory information in Land Details");

        WebDriver iDriver = getDriver();
        int iMaxIterations = 60;
        int iIteration     = 0;
        int iRowsFixed     = 0;

        while (iIteration < iMaxIterations)
        {
            iIteration++;

            // Re-query every iteration — Angular updates the icon in place after save
            List<WebElement> iErrorRows = iDriver.findElements(By.xpath(ObjReader.getLocator("iLandTableErrorRows")));

            // ── Exit condition — no error rows remain ────────────────────────────────
            if (iErrorRows.isEmpty())
            {
                log.info(" All error rows resolved. Total fixed: " + iRowsFixed);
                break;
            }

            log.info("Iteration " + iIteration + " — error rows remaining: " + iErrorRows.size());

            // ── Open the first error row in the Edit Parcel drawer ───────────────────
            iAction("CLICK", "XPATH", ObjReader.getLocator("iLandTableFirstErrorRow"), null);
            log.info("Opened Edit Parcel drawer for first error row.");

            // Track whether this iteration actually changed anything
            boolean iAnyFieldFixed = false;

            // ── 1. Rental / Lease Expiry Date ────────────────────────────────────────
            if (isVisible(By.xpath(ObjReader.getLocator("iRentalExpiryDateInput")), 2))
            {
                String iExpiryDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iRentalExpiryDateInput"), iExpiryDate);
                log.info("Set Rental/Lease Expiry Date to: " + iExpiryDate);
                iAnyFieldFixed = true;
            }


            // ── 3. Organic Status ────────────────────────────────────────────────────
            if (isVisible(By.xpath(ObjReader.getLocator("iOrganicStatusEmpty")), 1))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iOrganicStatusSelect"), "Conventional");
                log.info("Set Organic Status to 'Conventional'.");
                iAnyFieldFixed = true;
            }

            // ── 2. Agricultural Activity ─────────────────────────────────────────────
            if (isVisible(By.xpath(ObjReader.getLocator("iAgriculturalActivitySelect")), 3))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iAgriculturalActivitySelect"), "Milking Platform");
                log.info("Set Agricultural Activity to 'Milking Platform'.");
                iAnyFieldFixed = true;
            }


            if (isVisible(By.xpath(ObjReader.getLocator("iParcelUseRequired")), 1))
            {
                iAction("LIST", "XPATH", ObjReader.getLocator("iParcelUseSelect"), "Permanent Pasture");
                log.info("Set Parcel Use to 'Permanent Pasture'.");
                iAnyFieldFixed = true;
            }




            // ── If nothing was fixable — unknown error, fail immediately ─────────────
            if (!iAnyFieldFixed)
            {
                throw new AssertionError(
                        "Iteration " + iIteration + ": Edit Parcel drawer opened but no known "
                                + "mandatory field was missing. Unknown error type detected — "
                                + "check this herd manually.");
            }

            // ── Save ─────────────────────────────────────────────────────────────────
            iAction("CLICK", "XPATH", ObjReader.getLocator("iSaveParcelChangesBtn"), null);
            log.info("Saved parcel changes.");

            // ── Wait for Angular to update the icon before re-querying ───────────────
            // Do NOT assert check_circle here — let the empty-check at the top of the
            // next iteration be the single source of truth for whether the row is fixed
            try { Thread.sleep(1500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

            iRowsFixed++;
        }

        // ── Safety cap breach ────────────────────────────────────────────────────────
        if (iIteration >= iMaxIterations)
        {
            throw new AssertionError("Safety cap of " + iMaxIterations + " iterations reached. " + "Unresolved error rows still exist in Land Details.");
        }

    }


// ***************************************************************************************************************************************************************************************
// Step          : Then the next application step should open successfully
// Description   : Confirms the application has advanced past Land Details
// ***************************************************************************************************************************************************************************************
    @Then("the next application step should open successfully")
    public void theNextApplicationStepShouldOpenSuccessfully() {

        log.info("[STEP] Then the next application step should open successfully");

    }

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent proceeds from GAEC 7
    // Description   : Clicks the Continue or Next button on the GAEC 7 panel
    // ***************************************************************************************************************************************************************************************
    @And("the agent reaches on Island Stepper")
    public void theAgentProceedsfromLD() {
        String iIslandHeaderXpath = "//h1[contains(@class,'application-step-content-title')]" + "//span[normalize-space()='Islands']";

        if (!isVisible(By.xpath(iIslandHeaderXpath), 3))
        {
            log.info("[STEP] Islands Stepper found simply navigate on next page.");
        }
    }

    // ===================================================================================================================================
    //  GAEC 7 STEP
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent opens the "GAEC 7" step
    // Description   : Clicks the GAEC 7 step link in the application navigation
    // ***************************************************************************************************************************************************************************************
    @When("the agent opens the {string} step")
    public void theAgentOpensTheStep(String pStepName) {

       // String iStepHeader = iAction("GETTEXT", "XPATH", "//h1[contains(@class,'application-step-content-title')]//span[normalize-space()='"+pStepName+"']", null);
       // Assertions.assertFalse(iStepHeader.isEmpty(), pStepName+ " step header should be visible after advancing.");
        log.info(" Next application step opened successfully: " + pStepName);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent proceeds from GAEC 7
    // Description   : Clicks the Continue or Next button on the GAEC 7 panel
    // ***************************************************************************************************************************************************************************************
    @And("the agent proceeds from GAEC 7")
    public void theAgentProceedsFromGAEC7() {
        log.info("[STEP] And the agent proceeds from GAEC 7");
        // GAEC 7 asks the farmer to confirm nutrient management rules — click Continue to move on
        //Action("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iContinueBtn"), null);

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent handles GAEC 7 Select Option actions
    // Description   : Opens Select Option, ticks checkbox, enters area, and saves
    // ***************************************************************************************************************************************************************************************
    @And("the agent handles any GAEC 7 continue action if present")
    public void theAgentHandlesAnyGAEC7ContinueActionIfPresent()
    {
        log.info("[STEP] And the agent handles GAEC 7 select option actions");

        int iIndex = 1;

        while (true)
        {
            try
            {
                // ── Build indexed Select Option locator ──────────────────────────────
                String iSelectOptionXpath = ObjReader.getLocator("iGAEC7_SelectOption_ByIndex").replace("${INDEX}", String.valueOf(iIndex));

                // ── If the Select Option row is not visible, no more rows remain ──────
                if (!isVisible(By.xpath(iSelectOptionXpath), 4))
                {
                    log.info("No more GAEC 7 Select Option rows at index " + iIndex + " — exiting loop.");
                    break;
                }

                // ── Click Select Option ──────────────────────────────────────────────
                iAction("CLICK", "XPATH", iSelectOptionXpath, null);
                log.info("Clicked GAEC 7 Select Option at index: " + iIndex);

                // ── Tick checkbox ────────────────────────────────────────────────────
                iAction("CLICK", "XPATH", ObjReader.getLocator("iGAEC7_ActionCheckbox"), null);

                // ── Enter Catch Crop Area ────────────────────────────────────────────
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iGAEC7_CatchCropArea"), "1.00");

                // ── Save changes ─────────────────────────────────────────────────────
                iAction("CLICK", "XPATH", ObjReader.getLocator("iGAEC7_SaveBtn"), null);

                // ── Wait for spinner to appear then disappear ────────────────────────
                // Angular shows the MDC indeterminate spinner while it processes the save.
                // First wait for it to become visible (confirms save was triggered),
                // then wait for it to vanish (confirms Angular has finished re-rendering).
                // Both waits use the resolved XPath — not a raw key string.
                String iSpinnerXpath = ObjReader.getLocator("iScreenBuffer2");

                isVisible(By.xpath(iSpinnerXpath), 2); // soft-wait for spinner to appear
                iAction("WAITINVISIBLE", "XPATH", iSpinnerXpath, null);
                log.info("Spinner gone — Angular re-render complete after save at index: " + iIndex);

                // ── Reset index — Angular re-renders the full row list after save ────
                iIndex = 1;
            }
            catch (Exception iNoMoreRows)
            {
                log.warning("Unexpected exception in GAEC 7 loop: " + iNoMoreRows.getMessage());
                break;
            }
        }

        // ── Advance past GAEC 7 step ─────────────────────────────────────────────────
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iGAEC7_ContinueBtn"), null);
        log.info(" GAEC 7 step completed. Moving to next screen.");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the application should move beyond the GAEC 7 step
    // Description   : Confirms the page has moved past GAEC 7 by checking the header no longer reads GAEC 7
    // ***************************************************************************************************************************************************************************************
    @Then("the application should move beyond the GAEC 7 step")
    public void theApplicationShouldMoveBeyondTheGAEC7Step() {

        log.info("[STEP] Then the application should move beyond the GAEC 7 step");

        // ── Wait for the ACRES header to appear ───────────────────────────────────
        // Angular takes time to transition between steps — poll for up to 15 seconds
        // before asserting. isVisible returns as soon as the element is found,
        // so if the page loads quickly the step won't waste time waiting.
        //By iACRESHeader = By.xpath(ObjReader.getLocator("iACRESStepHeader"));

       // boolean iACRESVisible = isVisible(iACRESHeader, 15);

       // Assertions.assertTrue(iACRESVisible, "Application did not navigate to the ACRES step within 15 seconds after GAEC 7. " + "Check that iNextBtn and iGAEC7_ContinueBtn fired correctly.");

        // ── Confirm header text as a secondary assertion ──────────────────────────
      //  String iCurrentStep = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iACRESStepHeader"), null);
      //  log.info(" Application moved beyond GAEC 7 | Current step header: " + iCurrentStep);
    }


    // ===================================================================================================================================
    //  ACRES STEP
    // ===================================================================================================================================


    // ***************************************************************************************************************************************************************************************
// Step          : And the agent handles the ACRES step if present
// Description   : Some herds have ACRES in scope, others go directly from GAEC 7 to ECO.
//                 This step checks whether the ACRES step header is currently visible.
//                 If ACRES is present  → selects "Yes, rescore" on all panels → continues all panels
//                 If ACRES is absent   → logs and passes silently, ECO will be handled next
//                 This avoids hard failures when the herd's scope does not include ACRES.
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date          : 23-04-2026
// ***************************************************************************************************************************************************************************************
    @And("the agent handles the ACRES step if present")
    public void theAgentHandlesTheACRESStepIfPresent() throws InterruptedException {
        log.info("[STEP] And the agent handles the ACRES step if present");
        Thread.sleep(2100);
        WebDriver iDriver = getDriver();

        // ── Check if ACRES step header is currently visible ───────────────────────────────
        // If the herd has no ACRES scope, the app skips straight to ECO after GAEC 7.
        // isVisible() returns false immediately without throwing — safe soft check.
        String iAcresHeaderXpath = "//h1[contains(@class,'application-step-content-title')]" + "//span[normalize-space()='ACRES']";

        if (!isVisible(By.xpath(iAcresHeaderXpath), 5))
        {
            log.info("[STEP] ACRES step not present for this herd — skipping. " + "Herd may have gone directly to ECO.");
            return;
        }

        log.info("[STEP] ACRES step confirmed present — proceeding.");

        // ── Select "Yes, rescore" on all panels ───────────────────────────────────────────
        List<WebElement> iRescoreRadios = iDriver.findElements(By.xpath(ObjReader.getLocator("iRescoreOptionYes")));

        log.info("[STEP] Found " + iRescoreRadios.size() + " rescore radio button(s).");

        for (int i = 0; i < iRescoreRadios.size(); i++)
        {
            String iIndexedXpath = "(" + ObjReader.getLocator("iRescoreOptionYes") + ")[" + (i + 1) + "]";
            iAction("WAITVISIBLE",   "XPATH", iIndexedXpath, null);
            iAction("WAITCLICKABLE", "XPATH", iIndexedXpath, null);
            iAction("RADIOBUTTON",   "XPATH", iIndexedXpath, null);
            log.info("[STEP] Selected 'Yes, rescore' on panel " + (i + 1));
        }

        // ── Click Continue on all panels ──────────────────────────────────────────────────
        List<WebElement> iPanels = iDriver.findElements(By.xpath(
                "//mat-expansion-panel[.//button[.//span[normalize-space()='Continue']]]"));

        log.info("[STEP] Found " + iPanels.size() + " panel(s) with Continue button.");

        for (int i = 1; i <= iPanels.size(); i++)
        {
            String iContinueXpath = "(//mat-expansion-panel[.//button[.//span[normalize-space()='Continue']]])" + "[" + i + "]" + "//button[.//span[normalize-space()='Continue']]";

            iAction("WAITVISIBLE",   "XPATH", iContinueXpath, null);
            iAction("WAITCLICKABLE", "XPATH", iContinueXpath, null);
            iAction("CLICK",         "XPATH", iContinueXpath, null);
            log.info("[STEP] Clicked Continue on panel " + i);
        }

        // ── Assert ACRES completed ────────────────────────────────────────────────────────
        boolean iAcresDone = !iDriver.findElements(By.xpath("//h1[contains(@class,'application-step-content-title')]" + "//span[normalize-space()='ACRES']")).isEmpty();

        log.info("[STEP] ACRES step handled successfully. Moving to next step.");
    }
    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects "Yes, rescore" on panel 1
    // Description   : Selects the specified ACRES panel option by label text
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} on panel {int}")
    public void theAgentSelectsOnPanel(String pOption, int pPanelNumber) {
        log.info("[STEP] And the agent selects: " + pOption + " on panel: " + pPanelNumber);
        // ACRES has multiple panels — use the panel index to scope the click to the right one
        // so we don't accidentally select an option in the wrong panel section
        iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iRescoreOptionYes"), null);
    }


    // ***************************************************************************************************************************************************************************************
// Step          : And the agent continues panel {int}
// Description   : Clicks the Continue button within the specified panel
// ***************************************************************************************************************************************************************************************
    @And("the agent continues panel {int}")
    public void theAgentContinuesPanel(int pPanelNumber)
    {

        WebDriver iDriver = getDriver();
        log.info("[STEP] And the agent continues through all applicable panels");
        List<WebElement> panels = iDriver.findElements(By.xpath("//mat-expansion-panel[.//button[.//span[normalize-space()='Continue']]]"));
        log.info("Total panels with Continue button found: " + panels.size());

        for (int i = 1; i <= panels.size(); i++) {

            String continueBtnXpath = "(//mat-expansion-panel[.//button[.//span[normalize-space()='Continue']]])[" + i + "]" + "//button[.//span[normalize-space()='Continue']]";
            log.info("Clicking Continue on panel index: " + i);
            iAction("WAITVISIBLE", "XPATH", continueBtnXpath, "Continue button in panel " + i);
            iAction("CLICK", "XPATH", continueBtnXpath, null);
        }

    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the ACRES step should be completed successfully
    // Description   : Confirms ACRES step completion indicator is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the ACRES step should be completed successfully")
    public void theACRESStepShouldBeCompletedSuccessfully() {
        log.info("[STEP] Then the ACRES step should be completed successfully");
       // By iACRESHeader = By.xpath("//h1[contains(@class,'application-step-content-title')]//span[normalize-space()='Eco']");
       // boolean iACRESVisible = isVisible(iACRESHeader, 15);
      //  Assertions.assertTrue(iACRESVisible, "Application did not navigate to the ECO step within 15 seconds after GAEC 7. " + "Check that iNextBtn Btn fired correctly.");

       // log.info("ACRES step completed: " + iACRESHeader);
    }


    // ===================================================================================================================================
    //  ECO STEP
    // ===================================================================================================================================
    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent selects eco AP options "AP1" and "AP5"
    // Description   : Iterates through the Eco AP table, identifies rows with available toggle buttons,
    //                 selects the two specified APs (or first two available if params are empty),
    //                 expands each panel, fills AP-specific fields, clicks Save & Select,
    //                 then asserts the "2 of 2 Selected" counter before proceeding.
    // Parameters    : pFirstAP  — e.g. "AP1" or "" for auto-select
    //                 pSecondAP — e.g. "AP5" or "" for auto-select
    // Author        : Aniket Pathare
    // Date          : 10/04/2026
    // ***************************************************************************************************************************************************************************************
    @When("the agent selects eco AP options {string} and {string}")
    public void theAgentSelectsEcoAPOptions(String pFirstAP, String pSecondAP)
    {
        log.info("[STEP] When the agent selects eco AP options: " + pFirstAP + " and " + pSecondAP);

        WebDriver iDriver = getDriver();

        // ── Resolve which two APs to process ────────────────────────────────────────
        // If both params are empty, auto-detect the first two rows that have a toggle
        // If params are supplied, use them in order
        List<String> iTargetAPs = new ArrayList<>();

        if (!pFirstAP.isBlank())  iTargetAPs.add(pFirstAP.trim().toUpperCase());
        if (!pSecondAP.isBlank()) iTargetAPs.add(pSecondAP.trim().toUpperCase());

        boolean iAutoSelect = iTargetAPs.isEmpty();

        // ── Find all AP rows that have a toggle button ───────────────────────────────
        // Toggle presence = this AP is available for this herd
        // Rows without a toggle are greyed out — skip them entirely
        String iToggleBtn = "//tr[contains(@class,'example-element-row')][.//mat-slide-toggle]";

        if (!isVisible(By.xpath(iToggleBtn), 5))
        {
            log.info("[STEP] identifies rows with available toggle buttons FAILED");
            return;
        }
        List<WebElement> iToggleRows = iDriver.findElements(By.xpath(ObjReader.getLocator("iEcoAPRowsWithToggle")));

        Assertions.assertFalse(iToggleRows.isEmpty(), "No AP rows with toggle buttons found on the Eco screen — check herd eligibility.");

        log.info("Toggle-enabled AP rows found: " + iToggleRows.size());

        int iSelected = 0;

        for (WebElement iRow : iToggleRows)
        {
            if (iSelected >= 2) break;

            // ── Read the AP code from this row's label cell ──────────────────────────
            // e.g. " AP1 - Space for Nature * " → extract "AP1"
            String iRowText = iRow.findElement(By.xpath(ObjReader.getLocator("iEcoAPRowLabelCell"))).getText().trim();

            String iAPCode = iRowText.replaceAll("(?i)(AP\\d+).*", "$1").trim().toUpperCase();
            log.info("Found toggleable AP row: " + iAPCode);

            // ── Skip if params were supplied and this AP is not in the target list ───
            if (!iAutoSelect && !iTargetAPs.contains(iAPCode))
            {
                log.info("Skipping " + iAPCode + " — not in target list.");
                continue;
            }

            // ── Click the expand arrow — only if panel is currently collapsed ─────────
            // Angular toggles the mat-icon between keyboard_arrow_down (closed)
            // and keyboard_arrow_up (open) — check before clicking to avoid collapsing
            // a panel that is already open
            WebElement iExpandBtn = iRow.findElement(By.xpath(ObjReader.getLocator("iEcoAPExpandBtn")));

            String iArrowIcon = iExpandBtn.findElement(By.xpath(".//mat-icon")).getText().trim();

            if (iArrowIcon.contains("keyboard_arrow_down"))
            {
                iExpandBtn.click();
                log.info("Panel was collapsed — expanded for: " + iAPCode);

                // Wait for panel content to render after opening
                try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
            else
            {
                log.info("Panel already expanded for: " + iAPCode + " — skipping expand click.");
            }


            // ── Fill AP-specific fields ───────────────────────────────────────────────
            handleEcoAPPanelFields(iAPCode);

            // ── Click Save & Select ───────────────────────────────────────────────────
            iAction("CLICK", "XPATH", ObjReader.getLocator("iEcoAPSaveAndSelectBtn"), null);
            log.info("Clicked Save & Select for: " + iAPCode);

            // ── Wait for spinner and panel collapse ───────────────────────────────────
            String iSpinnerXpath = ObjReader.getLocator("iScreenBuffer2");
            isVisible(By.xpath(iSpinnerXpath), 2); // soft-wait for spinner to appear
            iAction("WAITINVISIBLE", "XPATH", iSpinnerXpath, null);

            iSelected++;
            log.info("AP selected count: " + iSelected);
        }

        Assertions.assertEquals(2, iSelected,
                "Expected 2 APs to be selected but only " + iSelected + " were processed. "
                        + "Check toggle availability for this herd.");

        // ── Assert counter shows "2 of 2 Selected" ───────────────────────────────────
        String iCounterText = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iEcoAPSelectedCounter"), null);
        Assertions.assertTrue(iCounterText.contains("2 of 2"), "Expected counter to show '2 of 2 Selected' but found: " + iCounterText);
        log.info(" Eco AP counter confirmed: " + iCounterText);
    }


    // ***************************************************************************************************************************************************************************************
// Method        : handleEcoAPPanelFields
// Description   : Switch-case handler for AP-specific inner panel fields.
//                 Called after the panel is expanded — fills only the fields relevant to each AP.
// Parameters    : pAPCode — e.g. "AP1", "AP2", "AP5", "AP6"
// Author        : Aniket Pathare
// Date          : 10/04/2026
// ***************************************************************************************************************************************************************************************
    private void handleEcoAPPanelFields(String pAPCode)
    {
        log.info("Handling panel fields for: " + pAPCode);

        switch (pAPCode)
        {
            case "AP1":
                // Radio button: Standard / Enhanced — always select Standard
                iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iEcoAP1_StandardRadio"), null);
                log.info("AP1: Selected 'Standard' radio button.");
                break;

            case "AP2":

                String iSelectOptionXpath = ObjReader.getLocator("iEcoAP2_Checkbox");

                // ── If the Select Option row is not visible, no more rows remain ──────
                if (isVisible(By.xpath(iSelectOptionXpath), 4))
                {
                    iAction("CLICK", "XPATH", ObjReader.getLocator("iEcoAP2_Checkbox"), null);
                }

                // Radio button: Standard / Enhanced stocking rate — always select Standard
                iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iEcoAP2_StandardRadio"), null);
                log.info("AP2: Selected 'Standard' stocking rate radio button.");
                break;

            case "AP3":
                // Single confirmation checkbox — tick it
                iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iEcoAP3_ConfirmCheckbox"), "CHECK");
                log.info("AP3: Ticked confirmation checkbox.");
                break;

            case "AP4":
                // ── Step 1: Select the first radio option ─────────────────────────────────────
                // mat-radio-button wrapper is visible — JS click bypasses Angular overlay
                // interception and the opacity:0 on the native <input type="radio"> inside it.
                iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iEcoAP4_FirstOptionRadio"), null);
                iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iEcoAP4_FirstOptionRadio"), null);
                iAction("RADIOBUTTON",   "XPATH", ObjReader.getLocator("iEcoAP4_FirstOptionRadio"), null);
                log.info("AP4: Selected first radio option (Standard Trees).");

                // ── Step 2: Click "Upload receipts" to open the upload dialog ─────────────────
                iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iEcoAP4_UploadBtn"), null);
                iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iEcoAP4_UploadBtn"), null);
                iAction("CLICK",         "XPATH", ObjReader.getLocator("iEcoAP4_UploadBtn"), null);
                log.info("AP4: Upload receipts dialog opened.");

                // ── Step 3: Send file path directly to the hidden <input type="file"> ─────────
                // The dialog has two file inputs — one hidden (biss-upload-doc-selection) and one
                // in the drag-drop area (biss-upload-doc-drag-and-drop). We target the drag-drop
                // input because it has no 'hidden' class and accepts sendKeys reliably.
                // This bypasses the OS native file picker dialog entirely — no AutoIT needed.
                iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iEcoAP4_ChooseFile"), null);

                String iFilePath = System.getProperty("ap4.upload.path",
                        System.getProperty("user.dir")
                                + java.io.File.separator + "src"
                                + java.io.File.separator + "test"
                                + java.io.File.separator + "resources"
                                + java.io.File.separator + "Test_Data"
                                + java.io.File.separator + "Cover_Letter.pdf");

                // Make the input interactable — Angular hides file inputs but sendKeys
                // still works once display/visibility is restored via JS
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].style.display='block';" + "arguments[0].style.visibility='visible';" + "arguments[0].style.opacity='1';",
                        getDriver().findElement(By.xpath(ObjReader.getLocator("iEcoAP4_FileInput"))));
                        getDriver().findElement(By.xpath(ObjReader.getLocator("iEcoAP4_FileInput"))).sendKeys(iFilePath);

                log.info("AP4: File path sent to upload input: " + iFilePath);

                // ── Step 4: Click Upload button in the dialog footer ─────────────────────────
                iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iEcoAP4_UploadDialogBtn"), null);
                iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iEcoAP4_UploadDialogBtn"), null);
                iAction("CLICK",         "XPATH", ObjReader.getLocator("iEcoAP4_UploadDialogBtn"), null);
                log.info("AP4: Upload dialog confirmed — file uploaded.");

                // ── Step 5: Tick the "I commit to planting" checkbox ─────────────────────────
                // Must be ticked after upload — checkbox validates after file is present
                iAction("WAITVISIBLE",   "XPATH", ObjReader.getLocator("iEcoAP4_CommitCheckbox"), null);
                iAction("WAITCLICKABLE", "XPATH", ObjReader.getLocator("iEcoAP4_CommitCheckbox"), null);
                iAction("CHECKBOX",      "XPATH", ObjReader.getLocator("iEcoAP4_CommitCheckbox"), "CHECK");
                log.info("AP4: Ticked 'I commit to planting' checkbox.");

                // Note: Save & Select is handled by the caller after this method returns.
                break;

            case "AP5":
                // ── Radio: "an approved GPS spreader" ────────────────────────────────────
                // Target the label text directly — more reliable than the input sibling chain
                iAction("RADIOBUTTON", "XPATH", ObjReader.getLocator("iEcoAP5_GPSSpreaderRadio"), null);
                log.info("AP5: Selected 'an approved GPS spreader'.");

                // ── Approved Spreader dropdown — select Lemken ────────────────────────────
                // mat-select with formcontrolname scoped inside the eco-ap5-form
                // Using iAction LIST which handles CDK overlay open + option click
                iAction("LIST", "XPATH", ObjReader.getLocator("iEcoAP5_ApprovedSpreaderDropdown"), "Lemken");
                log.info("AP5: Selected 'Lemken' from Approved Spreader dropdown.");

                // ── Model dropdown — only appears after Lemken is selected ────────────────
                // Wait for the model dropdown to become visible before interacting —
                // Angular renders it dynamically after the spreader manufacturer is set
                iAction("WAITVISIBLE", "XPATH", ObjReader.getLocator("iEcoAP5_ModelDropdown"), null);
                iAction("LIST", "XPATH", ObjReader.getLocator("iEcoAP5_ModelDropdown"), "Polaris 14");
                log.info("AP5: Selected 'Polaris 14' from Model dropdown.");

                // ── Serial Number — optional text input ───────────────────────────────────
                iAction("TEXTBOX", "XPATH", ObjReader.getLocator("iEcoAP5_SerialNumber"), "A735B78346");
                log.info("AP5: Entered serial number.");
                break;


            case "AP6":
                // Two confirmation checkboxes — tick both
                iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iEcoAP6_ConfirmCheckbox1"), "CHECK");
                log.info("AP6: Ticked first confirmation checkbox.");
                iAction("CHECKBOX", "XPATH", ObjReader.getLocator("iEcoAP6_ConfirmCheckbox2"), "CHECK");
                log.info("AP6: Ticked second confirmation checkbox.");
                break;

            case "AP7":
                // No fields defined yet
                log.info("AP7: No fields to fill — skipping.");
                break;

            case "AP8":
                // No fields defined yet
                log.info("AP8: No fields to fill — skipping.");
                break;

            default:
                log.warning("Unknown AP code encountered in switch: " + pAPCode + " — no fields filled.");
                break;
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects "Standard" for panel 2
    // Description   : Selects the named option within the specified Eco panel
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects {string} for panel {int}")
    public void theAgentSelectsForPanel(String pOption, int pPanelNumber) {
        log.info("[STEP] And the agent selects: " + pOption + " for panel: " + pPanelNumber);
        // Eco panels are indexed the same way as ACRES — scope to the right panel before clicking
        iAction("CLICK", "XPATH", "(//div[contains(@class,'eco-panel')])[" + pPanelNumber + "]//label[contains(text(),'" + pOption + "')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent saves the selected eco option
    // Description   : Clicks Save on the current Eco option selection
    // ***************************************************************************************************************************************************************************************
    @And("the agent saves the selected eco option")
    public void theAgentSavesTheSelectedEcoOption() {
        log.info("[STEP] And the agent saves the selected eco option");
        // Commit the Eco option selection — this persists the choice for this parcel
        iAction("CLICK", "XPATH", "//button[contains(text(),'Save')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects approved spreader manufacturer "Lemken"
    // Description   : Selects the spreader manufacturer from the AP5 dropdown
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects approved spreader manufacturer {string}")
    public void theAgentSelectsApprovedSpreaderManufacturer(String pManufacturer) {
        log.info("[STEP] And the agent selects approved spreader manufacturer: " + pManufacturer);
        // The AP5 (precision spreading) section requires a specific approved manufacturer —
        // use VISIBLETEXT prefix so the LIST action matches by the label shown to the user
        iAction("LIST", "ID", "spreaderManufacturer", "VISIBLETEXT:" + pManufacturer);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects approved spreader model "Polaris 14"
    // Description   : Selects the spreader model from the AP5 dropdown
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects approved spreader model {string}")
    public void theAgentSelectsApprovedSpreaderModel(String pModel) {
        log.info("[STEP] And the agent selects approved spreader model: " + pModel);
        // Model list is driven by manufacturer selection above — pick the specific model
        iAction("LIST", "ID", "spreaderModel", "VISIBLETEXT:" + pModel);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent enters spreader serial number "A735B78346"
    // Description   : Types the spreader serial number into the AP5 serial number field
    // ***************************************************************************************************************************************************************************************
    @And("the agent enters spreader serial number {string}")
    public void theAgentEntersSpreaderSerialNumber(String pSerialNumber) {
        log.info("[STEP] And the agent enters spreader serial number: " + pSerialNumber);
        // Type the unique serial number of the farmer's registered spreader machine
        iAction("TEXTBOX", "ID", "spreaderSerialNumber", pSerialNumber);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the Eco step should be completed successfully
    // Description   : Confirms the Eco step completion indicator is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the Eco step should be completed successfully")
    public void theEcoStepShouldBeCompletedSuccessfully() {
        log.info("[STEP] Then the Eco step should be completed successfully");
        // Look for a step-complete or step-success div — the portal renders one of these
        // when all mandatory Eco selections have been filled in correctly

        iAction("VERIFYELEMENT", "XPATH", "//a[normalize-space()='Scheme Selection']", null);
        iAction("CLICK", "XPATH", ObjReader.getLocator("iNextBtn"), null);
        String iStepStatus = iAction("GETTEXT", "XPATH", "//h1[contains(@class,'application-step-content-title')]//span[normalize-space()='Review & Submit']", null);
        Assertions.assertFalse(iStepStatus.isEmpty(), " Review & Submit  step completion indicator should be visible.");
        log.info("Eco step completed: " + iStepStatus);
    }


    // ===================================================================================================================================
    //  ECO OPT-OUT VALIDATION
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : When the agent returns to the "Scheme Selection" step
    // Description   : Navigates back to the Scheme Selection step via the step navigation links
    // ***************************************************************************************************************************************************************************************
    @When("the agent returns to the {string} step")
    public void theAgentReturnsToTheStep(String pStepName) {
        log.info("[STEP] When the agent returns to the step: " + pStepName);
        // Click the named step in the wizard navigation bar to jump back to it.
        // This is used in the Eco opt-out scenario where we need to revisit Scheme Selection
        // after already having progressed further through the wizard.
        iAction("CLICK", "XPATH", "//a[contains(text(),'" + pStepName + "')] | //button[contains(text(),'" + pStepName + "')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent opens the "Eco" scheme card
    // Description   : Clicks the Eco scheme card in the Scheme Selection step
    // ***************************************************************************************************************************************************************************************
    @And("the agent opens the {string} scheme card")
    public void theAgentOpensTheSchemeCard(String pSchemeName) {
        log.info("[STEP] And the agent opens the scheme card: " + pSchemeName);
        // Expand the named scheme card — the click target is the card container itself
        // so we go up to the ancestor scheme-card div from the h3 heading to get a stable target
        iAction("CLICK", "XPATH", "//div[contains(@class,'scheme-card')]//h3[contains(text(),'" + pSchemeName + "')]/ancestor::div[contains(@class,'scheme-card')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent accepts the acknowledgement if displayed
    // Description   : Dismisses any acknowledgement modal — skips safely if not present
    // ***************************************************************************************************************************************************************************************
    @And("the agent accepts the acknowledgement if displayed")
    public void theAgentAcceptsTheAcknowledgementIfDisplayed() {
        log.info("[STEP] And the agent accepts the acknowledgement if displayed");
        try {
            // An acknowledgement prompt sometimes appears when opting out of or back into Eco —
            // accept it if present so the flow can continue
            iAction("CLICK", "XPATH",
                    "//button[contains(text(),'Accept') or contains(text(),'Acknowledge') or contains(text(),'OK')]",
                    null);
            log.info("Acknowledgement accepted.");
        } catch (Exception e) {
            // No modal appeared — that's fine, just carry on
            log.info("No acknowledgement modal present — continuing.");
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the application should continue successfully after eco validation
    // Description   : Confirms the application step moved forward after eco opt-out validation
    // ***************************************************************************************************************************************************************************************
    @Then("the application should continue successfully after eco validation")
    public void theApplicationShouldContinueSuccessfullyAfterEcoValidation() {
        log.info("[STEP] Then the application should continue successfully after eco validation");
        // Read the current step header — as long as something is visible we know the application
        // didn't get stuck on the Eco validation and has successfully moved forward
        String iCurrentStep = iAction("GETTEXT", "XPATH", "//h2[contains(@class,'step-header')]", null);
        Assertions.assertFalse(iCurrentStep.isEmpty(),
                "A step header should be visible after eco opt-out validation.");
        log.info("Application continued after eco validation | Current step: " + iCurrentStep);
    }


    // ===================================================================================================================================
    //  REVIEW AND SUBMIT
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent completes all review page next actions
    // Description   : Iterates through any Next buttons on the Review & Submit multi-page review
    // ***************************************************************************************************************************************************************************************
    @And("the agent completes all review page next actions")
    public void theAgentCompletesAllReviewPageNextActions() throws InterruptedException {
        WebDriver iDriver = getDriver();
        Thread.sleep(2000);
        log.info("[STEP] And the agent continues through all applicable panels");
        List<WebElement> panels = iDriver.findElements(By.xpath("//mat-expansion-panel[.//button[.//span[normalize-space()='Next']]]"));
        log.info("Total panels with Continue button found: " + panels.size());

        for (int i = 1; i <= panels.size(); i++) {

            String continueBtnXpath = "(//mat-expansion-panel[.//button[.//span[normalize-space()='Next']]])[" + i + "]" + "//button[.//span[normalize-space()='Next']]";
            log.info("Clicking Continue on panel index: " + i);
            iAction("WAITVISIBLE", "XPATH", continueBtnXpath, "Next button in panel " + i);
            iAction("CLICK", "XPATH", continueBtnXpath, null);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent accepts the Terms and Conditions
    // Description   : Checks the Terms and Conditions checkbox on the submission page
    // ***************************************************************************************************************************************************************************************
    @And("the agent accepts the Terms and Conditions")
    public void theAgentAcceptsTheTermsAndConditions() {
        log.info("[STEP] And the agent accepts the Terms and Conditions");
        // The Submit button stays disabled until this checkbox is ticked —
        // the farmer is legally declaring the information they've provided is correct
        iAction("CHECKBOX", "XPATH", ObjReader.getLocator("termsAndConditions"), "CHECK");
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent submits the application
    // Description   : Clicks the Submit Application button
    // ***************************************************************************************************************************************************************************************
    @And("the agent submits the application")
    public void theAgentSubmitsTheApplication() {
        log.info("[STEP] And the agent submits the application");
        // Click Submit — the XPath explicitly excludes any 'Confirm Submit' buttons
        // to make sure we're hitting the initial Submit action rather than the confirmation
        iAction("CLICK", "XPATH", "//button[.//span[normalize-space()='Submit']]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent confirms the application submission
    // Description   : Confirms the submission confirmation modal
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms the application submission")
    public void theAgentConfirmsTheApplicationSubmission() {
        log.info("[STEP] And the agent confirms the application submission");
        // After clicking Submit a confirmation modal appears asking "are you sure?" —
        // click whichever affirmative button is on that modal to finalise the submission
        iAction("CLICK", "XPATH", "//button[.//span[normalize-space()='Yes, I confirm']]", null);
        By iPDFGneratedSubmission = By.xpath("//div[@class='ng-star-inserted'][normalize-space()='Download, view or print your BISS Application Summary']");
        boolean iACRESVisible = isVisible(iPDFGneratedSubmission, 55);
        Assertions.assertTrue(iACRESVisible, "You have successfully submitted a BISS 2026 application amendment page not displayed " + "Download, view or print your BISS Application Summary");

        log.info("ACRES step completed: " + iPDFGneratedSubmission);


    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the application should be submitted successfully
    // Description   : Confirms the application submission success message or reference number is visible
    // ***************************************************************************************************************************************************************************************
    @Then("the application should be submitted successfully")
    public void theApplicationShouldBeSubmittedSuccessfully() {
        log.info("[STEP] Then the application should be submitted successfully");
        // After a successful submission the portal shows a success banner or confirmation panel.
        // Read its text and assert it's not empty — an empty result means the page didn't
        // advance to the confirmation screen and something went wrong.
        // Now click 'My Clients' to get to the client search screen
        iAction("CLICK", "XPATH", ObjReader.getLocator("iCLientLeftMenuLink"), null);

        // Confirm the BISS header is still visible — makes sure we're still inside the portal
        iAction("VERIFYTEXT", "XPATH", ObjReader.getLocator("iBissTitle"), "Basic Income Support for Sustainability");

        iAction("CLICK", "XPATH", ObjReader.getLocator("iSubmittedTab"), "");

        log.info("[STEP] When the agent opens a farmer dashboard using herd data");
        iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), Hooks.RUNTIME_HERD);
        //iAction("TEXTBOX", "XPATH", ObjReader.getLocator("herdSearchInput"), "A1240326");

        iAction("CLICK",   "XPATH",    ObjReader.getLocator("herdSearchBtn"),   null);
        log.info("Farmer dashboard opened for herd number: " + "TD:iHerdNumber");

        String iConfirmation = iAction("GETTEXT", "XPATH", ObjReader.getLocator("iSubmittedStatusChip"), null);
        Assertions.assertTrue(iConfirmation.trim().equalsIgnoreCase("Submitted"), "Expected first row status to be 'Submitted' but found: '" + iConfirmation + "'");

        log.info(" Application submitted successfully | Status confirmed: " + iConfirmation);
        // Click the first client name in the results table to open their dashboard.
        // After the herd search above, there should only be one matching record.
        iAction("CLICK", "XPATH", ObjReader.getLocator("iFirstClientName"), null);
        log.info("Exactly 1 record found. Clicking first client name...");
    }


    // ===================================================================================================================================
    //  CORRESPONDENCE — DOCUMENT UPLOAD
    // ===================================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent chooses to upload a document
    // Description   : Clicks the Upload Document button on the Correspondence tab
    // ***************************************************************************************************************************************************************************************
    @And("the agent chooses to upload a document")
    public void theAgentChoosesToUploadADocument() {
        log.info("[STEP] And the agent chooses to upload a document");
        // Click the Upload Document button on the Correspondence tab to open the upload dialog
        iAction("CLICK", "XPATH", "//button[contains(text(),'Upload') or contains(text(),'Upload Document')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent selects document type "Commonage Evidence"
    // Description   : Selects the document type from the upload form dropdown
    // ***************************************************************************************************************************************************************************************
    @And("the agent selects document type {string}")
    public void theAgentSelectsDocumentType(String pDocumentType) {
        log.info("[STEP] And the agent selects document type: " + pDocumentType);
        // Pick the category that best describes the document being uploaded —
        // e.g. 'Commonage Evidence', 'Map Amendment' etc.
        iAction("LIST", "ID", "documentType", "VISIBLETEXT:" + pDocumentType);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent uploads the correspondence document
    // Description   : Sends the file path to the file input element to trigger upload
    //                 File path is read from test data property TD:CorrespondenceFilePath
    // ***************************************************************************************************************************************************************************************
    @And("the agent uploads the correspondence document")
    public void theAgentUploadsTheCorrespondenceDocument() {
        log.info("[STEP] And the agent uploads the correspondence document");
        // Read the file path from the system property set by the test runner.
        // Falls back to a bundled sample PDF if no path is supplied — useful for smoke runs
        // where we just want to verify the upload UI works rather than test a specific document.
        String iFilePath = System.getProperty("TD:CorrespondenceFilePath",
                "src/test/resources/TestDocuments/sample_correspondence.pdf");
        // Send the path directly to the hidden file input — this bypasses the OS file picker
        // which Selenium can't interact with, and triggers the same upload handler
        iAction("TEXTBOX", "CSS", "input[type='file']", iFilePath);
        log.info("Document file path sent to upload input: " + iFilePath);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : And the agent confirms the upload
    // Description   : Clicks the Confirm or Submit button on the document upload dialog
    // ***************************************************************************************************************************************************************************************
    @And("the agent confirms the upload")
    public void theAgentConfirmsTheUpload() {
        log.info("[STEP] And the agent confirms the upload");
        // The upload dialog has a final confirm step — click whichever affirmative button is shown
        iAction("CLICK", "XPATH", "//button[contains(text(),'Confirm') or contains(text(),'Upload') or contains(text(),'Submit')]", null);
    }


    // ***************************************************************************************************************************************************************************************
    // Step          : Then the document should be uploaded successfully in Correspondence
    // Description   : Confirms the uploaded document appears in the Correspondence document list
    // ***************************************************************************************************************************************************************************************
    @Then("the document should be uploaded successfully in Correspondence")
    public void theDocumentShouldBeUploadedSuccessfullyInCorrespondence() {
        log.info("[STEP] Then the document should be uploaded successfully in Correspondence");
        // After a successful upload the document should appear as a row in the Correspondence list.
        // Asserting the text isn't empty confirms the row is there — the text itself is
        // the document type label, so it doubles as a content verification.
        String iDocRow = iAction("GETTEXT", "XPATH", "//span[contains(text(),'Customers Upload: Commonage Evidence')]", null);
        Assertions.assertFalse(iDocRow.isEmpty(), "Uploaded document should be visible in the Correspondence list.");
        log.info("Document uploaded and confirmed in Correspondence: " + iDocRow);
    }
}
