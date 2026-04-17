Feature: TC_18 — Validate and Respond to Overclaim Preliminary Check

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Verifies that an agent can view and respond to the Overclaim Preliminary Check
  #   for a herd that has a Pending Overclaim notification.
  #
  #   Flow:
  #     Login as agent → My Clients → Search herd → Farmer Dashboard →
  #     Confirm Overclaim shows Response Required →
  #     View Preliminary Checks → Negative: Submit without selection → assert validation error →
  #     Respond to all panels row by row → Submit → Confirm → Assert success
  #
  # Notes:
  #   1. Herd and agent login are resolved at runtime from BISS_DATA / BISS_INET via @preliminary hook.
  #   2. Response values are passed as DataTable parameters — change them here without touching step defs.
  #   3. If the herd also has other checks showing Response Required, they are rejected as per DataTable.
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

  @regression @preliminary @TC_18
  Scenario: TC_18 — Agent responds to Overclaim Preliminary Check

    # -----------------------------------------
    # Farmer selection using Overclaim herd
    # -----------------------------------------
    When the agent opens a farmer dashboard for preliminary check herd "OVERCLAIM"
    Then the farmer dashboard should be displayed

    # -----------------------------------------
    # Dashboard validation
    # -----------------------------------------
    Then the preliminary checks card should be visible on the dashboard
    And the "Overclaim Checks" preliminary check should show "Response required" on the dashboard

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
    # -----------------------------------------
    When the agent responds to all preliminary check panels row by row
      | Overclaim Checks                | Accept        |
      | Dual Claim Checks               | No, Keep it   |
      | No Agricultural Activity Checks | Reject        |

    # -----------------------------------------
    # Submit and confirm
    # -----------------------------------------
    When the agent submits the Preliminary Checks response
    Then the agent confirms the Preliminary Checks submission
    Then the Preliminary Checks submission should be confirmed successfully