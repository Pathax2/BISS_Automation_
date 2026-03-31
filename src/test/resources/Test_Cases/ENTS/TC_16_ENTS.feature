Feature: TC_16_ENTS - Entitlements/Usage Button Verification

  # Migrated from: TC_16_ENTS.feature (1 scenario)
  # Agent navigates to Entitlements/Usage tab and verifies position
  # Author: Aniket Pathare | Created: 31-03-2026

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Clients page

  @sanity @entitlements
  Scenario: AT-ENTS-ENTITLEMENTS - Verify Entitlements/Usage button and position

    When the agent searches for herd "A1060280" and opens it
    And the agent navigates to the "Entitlements / Usage" side nav tab
    And the agent captures the OwnerID of the herd
    Then the agent verifies the entitlement position is displayed
