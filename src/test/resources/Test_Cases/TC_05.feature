Feature: TC_05 - Verify the My Clients Page Functionalities

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Validates the tab structure and core search/navigation functionality
  #   on the BISS Agent My Clients page.
  #
  #   - Sub-tab switching: Transfers, NR/CISYF, BISS Applications
  #   - Herd number search
  #   - Row selection and View Dashboard navigation
  #
  # Notes:
  #   1. Background navigates through Home then My Clients as a combined step.
  #   2. Herd number "N7010276" is used as a stable search fixture — update in
  #      TestData.xlsx if the herd changes between environments.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link

  #@tmslink=BISSAGL-7010
  @regression
  Scenario: To switch between all tabs for agent
    Given the agent is on the BISS Agent Home Screen
    When the agent switches to the "Transfers" tab on the My Clients page
    And the agent switches to the "NR/CISYF" tab on the My Clients page
    And the agent switches to the "BISS Applications" tab on the My Clients page

  @regression
  Scenario: To Check all working functionalities in the My Clients Page
    Given the agent is on the BISS Agent Home Screen
    When the agent searches for herd number "N7010276"
    And the agent clicks on the row for client "N7010276"
    Then the agent clicks on the View Dashboard button