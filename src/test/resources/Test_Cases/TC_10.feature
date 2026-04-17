Feature: TC_10 - BISS_25.3.Sprint_3 TC2 for BISSAGL-20695 Reference Number Agent User

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Verifies that an agent can create a new client record from the No Herd Number tab
  #   and that a reference number is generated correctly for the created client.
  #
  #   Active Scenario:
  #     Covers the full Create Client form flow:
  #       - Navigate to the No Herd Number sub-tab
  #       - Click Create Client button
  #       - Fill in name, address, county, eircode, contact number
  #       - Tick the BISS checkbox
  #       - Enter herd number
  #       - Submit and handle the post-creation dialog flow
  #       - Re-enter name on a second Create Client attempt
  #
  # Notes:
  #   1. The form field identifiers (name, add1, add2, add3, eircode, contactNumber, herdNumber)
  #      map to formcontrolname attributes on the Angular form controls.
  #   2. The test data values used (Kale, DXH1234 etc.) are static fixtures for this scenario.
  #      Move them to TestData.xlsx if they need to vary between environments.
  #   3. The stepper button labels (" Create Client " with spaces, "Create Client" without)
  #      are intentionally different — they map to two separate buttons in the form flow.
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
  Scenario: Reference Number Agent User — Create Client flow
    Given the agent is on the BISS Agent Home Screen
    When the agent switches to the "No Herd Number" tab on the My Client page
    And the agent opens the Create Client form
    And the agent fills in the Create Client form with the following details
      | name          | Kale      |
      | add1          | Address1  |
      | add2          | Address2  |
      | add3          | Address3  |
      | county        | Carlow    |
      | eircode       | D12345    |
      | contactNumber | 123456789 |
      | herdNumber    | DXH1234   |
    And the agent ticks the "BISS" reference type checkbox
    And the agent submits the Create Client form
    Then the agent completes the post creation dialog flow
