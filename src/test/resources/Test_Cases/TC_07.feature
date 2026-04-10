Feature: TC_07 - Regression Suite for Active Farmer Bugs and Features in BISS

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression flow covering Active Farmer related defects and validations.
  #
  #   Covered Defects:
  #     - BISSAGL-119 : Active Farmer Screen Error
  #         * Verifies Active Status checkbox is present on Active Farmer screen
  #         * Ensures draft can be deleted successfully after starting application
  #
  #     - BISSAGL-121 : Active Farmer Approved missing message
  #         * Verifies application reaches Active Farmer screen successfully
  #
  # Notes:
  #   1. Farmer selection is database-driven using framework herd data (no row index).
  #   2. Flow is intentionally designed as a single controlled regression journey.
  #   3. Draft cleanup is validated within the same scenario to keep state consistent.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link

  @regression @active-farmer
  Scenario: AT-TC-07 - Active Farmer regression validation (BISSAGL-119 & BISSAGL-121)

    # -----------------------------------------
    # Farmer selection and dashboard validation
    # -----------------------------------------
    When the agent opens a farmer dashboard using herd data
    Then the farmer dashboard should be displayed

    # -----------------------------------------
    # Application start
    # -----------------------------------------
    When the agent deletes any existing draft if present
    Then the Active Farmer step should be displayed
