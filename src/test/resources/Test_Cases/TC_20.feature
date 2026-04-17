Feature: TC_20 — Validate and Respond to No Agricultural Activity Preliminary Check

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Verifies that an agent can view and respond to the No Agricultural Activity Preliminary Check
  #   for a herd that has a Pending Agricultural Activity notification.
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
  Scenario: TC_20 — Agent responds to No Agricultural Activity Preliminary Check

    When the agent opens a farmer dashboard for preliminary check herd "AGRI_ACTIVITY"
    Then the farmer dashboard should be displayed
    Then the preliminary checks card should be visible on the dashboard
    And the "No Agricultural Activity Checks" preliminary check should show "Response required" on the dashboard
    When the agent clicks View Preliminary Checks
    Then the Preliminary Checks response page should be displayed
    When the agent clicks Submit on the Preliminary Checks page without selecting any response
    Then a validation error should be displayed on the Preliminary Checks page
    When the agent responds to all preliminary check panels row by row
      | Overclaim Checks                | Reject      |
      | Dual Claim Checks               | No, Keep it |
      | No Agricultural Activity Checks | Yes, change |
    When the agent submits the Preliminary Checks response
    Then the agent confirms the Preliminary Checks submission
    Then the Preliminary Checks submission should be confirmed successfully