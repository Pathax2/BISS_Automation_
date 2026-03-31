Feature: TC_17_ENTS - Transfer End-to-End with Staff ENTSCore Verification

  # Migrated from: TC_17_ENTS.feature (1 scenario)
  # Same-agent transfer (206) then staff verifies in ENTSCore
  # Reused: TC_01_ENTS (transfer steps), TC_15_ENTS (staff verification)
  # Author: Aniket Pathare | Created: 31-03-2026

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Clients page

  @sanity @transfers @staff-verification
  Scenario: AT-ENTS-TRANSFER-STAFF - Complete transfer and verify in ENTSCore

    # --- Capture OwnerID first ---
    When the agent searches for herd "A1020300" and opens it
    And the agent navigates to the "Entitlements / Usage" side nav tab
    And the agent captures the OwnerID of the herd
    And the agent navigates to the "Transfers" side nav tab

    # --- Same-agent transfer (206) ---
    When the agent creates a transfer application with the following details
      | transferorHerd | A1020300      |
      | transfereeHerd | A1010126      |
      | transfereeName | Maureen Wynne |
      | transferType   | 206           |
      | entitlements   | 0.01          |
      | notes          | Test Notes    |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (same agent) ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | A1010126 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # --- Staff ENTSCore verification ---
    When the staff user logs into ENTSCore as "AGR2214"
    And the staff searches for the herd by OwnerID
    And the staff navigates to year "2026" and "Transfers" tab
    Then the transfer submission should be visible in ENTSCore
