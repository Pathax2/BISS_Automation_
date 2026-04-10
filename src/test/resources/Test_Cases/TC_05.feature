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
    # -----------------------------------------
    # Farmer selection and dashboard validation
    # -----------------------------------------
    When the agent opens a farmer dashboard using herd data
    Then the farmer dashboard should be displayed

    # -----------------------------------------
    # Side navigation validation
    # -----------------------------------------
    When the agent navigates through the farmer side navigation tabs
      | Applications / Payments           |
      | Farm Details                      |
      |Entitlements / Usage               |
      | Transfers                         |
      | NR-CISYF                          |
      | My Correspondence                 |
      | Farmer Dashboard                  |
    Then each requested side navigation tab should open successfully

    # -----------------------------------------
    # Application start
    # -----------------------------------------
    When the agent deletes any existing draft if present
    And the agent starts a new BISS application
    Then the Active Farmer step should be displayed
