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
    And the agent switches to the "NR/CISYF" tab on the My Clients page

  @sanity @nrcisyf @staff-verification @tc13
  Scenario: AT-ENTS-NRCISYF-STAFF - Submit NRCISYF and verify in ENTSCore

    # --- Collect herds and capture owner ID ---
    When the agent searches for the NRCISYF herd and opens the application
    When the agent navigates through the farmer side navigation tabs
      |Entitlements / Usage |
    And the agent captures the OwnerID of the herd
