Feature: TC_20 - Validate and Respond to No Agricultural Activity Preliminary Check

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Verifies that an agent can view and respond to the No Agricultural Activity
  #   Preliminary Check for a herd that has a Pending Agricultural Activity
  #   notification in BISS_DATA.
  #
  #   Flow:
  #     Login as agent (resolved from BISS_INET at runtime) →
  #     My Clients → Search herd (resolved from BISS_DATA at runtime) →
  #     Farmer Dashboard → Assert No Agricultural Activity shows Response Required →
  #     View Preliminary Checks →
  #     Negative test: Submit without selection → assert validation error →
  #     Respond to all panels row by row per DataTable →
  #     Submit → Confirm Yes → Assert submission confirmed
  #
  # Notes:
  #   1. Herd and agent login are resolved at runtime from BISS_DATA / BISS_INET
  #      via the @Before("@preliminary") Hook — no hardcoded values.
  #   2. Response values are DataTable parameters — change them here without
  #      touching step definitions.
  #   3. Overclaim and Dual Claim panels are rejected as this TC targets
  #      No Agricultural Activity specifically.
  #   4. LVS_DESC = 'Pending' filter in the DB query guarantees fresh data.
  #
  # Author  : Aniket Pathare | aniket.pathare@government.ie
  # Created : 17-04-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link

  @regression @preliminary @TC_20
  Scenario: TC_20 - Agent validates and responds to No Agricultural Activity Preliminary Check

    # -----------------------------------------
    # Farmer selection — herd from DB at runtime
    # -----------------------------------------
    When the agent opens a farmer dashboard for preliminary check herd "AGRI_ACTIVITY"
    Then the farmer dashboard should be displayed

    # -----------------------------------------
    # Dashboard — assert Preliminary Checks card
    # -----------------------------------------
    Then the preliminary checks card should be visible on the dashboard
    And the "No Agricultural Activity Checks" preliminary check should show "Response required" on the dashboard

    # -----------------------------------------
    # Navigate to Preliminary Checks page
    # -----------------------------------------
    When the agent clicks View Preliminary Checks
    Then the Preliminary Checks response page should be displayed

    # -----------------------------------------
    # Negative test — submit without selecting
    # -----------------------------------------
    When the agent clicks Submit on the Preliminary Checks page without selecting any response
    Then a validation error should be displayed on the Preliminary Checks page

    # -----------------------------------------
    # Respond to all panels row by row
    # Panel Name must match span.prelim-heading text exactly
    # Response must match the radio button label text exactly
    # Panels with green icon are skipped automatically by the step
    # -----------------------------------------
    When the agent responds to all preliminary check panels row by row
      | Panel Name                      | Response    |
      | Overclaim Checks                | Reject      |
      | Dual Claim Checks               | No, Keep it |
      | No Agricultural Activity Checks | Yes, change |

    # -----------------------------------------
    # Submit and confirm
    # -----------------------------------------
    When the agent submits the Preliminary Checks response
    Then the agent confirms the Preliminary Checks submission
    And the Preliminary Checks submission should be confirmed successfully
