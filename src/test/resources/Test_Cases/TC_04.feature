Feature: TC_04 - Verify the Dashboard Page Functionalities

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Validates all key interactive elements on the BISS Agent Dashboard (Home) page.
  #
  #   - Quick Links section: Transfer of Entitlements and CISYF hyperlinks
  #   - BISS Applications section: View Clients button
  #   - News section: Read More button
  #   - Contact Us button
  #
  # Notes:
  #   1. Background handles login and lands the agent on the BISS Home page.
  #   2. Each interaction navigates away from Home; the scenario returns to Home
  #      between each click to verify independent navigation.
  #   3. Locators for dashboard elements must be defined in ObjectRepository.properties.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page

  @regression
  Scenario: To Check all working functionalities in the Dashboard Page
    Given the agent is on the BISS Agent Home Screen
    When the agent clicks on the "Transfer of Entitlements" hyperlink in the Quick Links section
    Then the agent navigates to the "Home" tab
    When the agent clicks on the "CISYF" hyperlink in the Quick Links section
    Then the agent navigates to the "Home" tab
    And the agent clicks on the View Clients button in the BISS Applications section
    Then the agent navigates to the "Home" tab
    Then the agent clicks on the Read More button in the News section
    And the agent clicks on the Contact Us button