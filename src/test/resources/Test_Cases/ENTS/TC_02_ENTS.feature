Feature: TC_02_ENTS - Transfer Application E2E Regression Pack (Different Agent)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   End-to-end regression journey for Transfer of Entitlements flows using a different
  #   agent (Agent 3 / aga6327) than TC_01_ENTS (Agent 1 / aga6077).
  #
  #   Covers:
  #     Section 1 : Merger of Entitlements (code 203) — full Transferor → Transferee cycle
  #     Section 2 : Negative case — herd without entitlements (Add Entitlement button absent)
  #
  # Migrated from: TC_02_ENTS.feature (legacy)
  #   TC_06 → Section 1  (Merger)
  #   Commented negative case → Section 2  (No entitlements)
  #
  # Step reuse:
  #   All Transferor/Transferee steps are defined in TC_01_ENTS.java.
  #   Background login steps are from TC_03.java.
  #   Tab switching is from TC_06.java.
  #   The only NEW step in this file is the negative entitlement validation (Section 2).
  #
  # Notes:
  #   1. This agent uses different credentials from TC_01_ENTS — set via TestData.xlsx
  #      or -DusernameOverride for Agent 3.
  #   2. Herd numbers are hardcoded test fixtures — update in TestData.xlsx if they expire.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6535"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page

  @regression @transfers @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-02 - Agent completes merger transfer and validates no-entitlement herd

    # ===========================================
    # SECTION 1 : Merger of Entitlements (203)
    # Covers: TC_06
    # ===========================================

    # --- Transferor ---
    When the agent creates a transfer application with the following details
      | transferorHerd | H114113X         |
      | transfereeHerd | H2454086         |
      | transfereeName | Kathleen Mahon   |
      | transferType   | Merger of 2 or more holdings (forming an unregistered Farm Partnership)|
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | H2454086  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # ===========================================
    # SECTION 2 : Negative — herd without entitlements
    # Covers: Commented legacy negative scenario
    # The Add Entitlement button should NOT be present
    # ===========================================
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent initiates a transfer search for herd without entitlements
      | transferorHerd | A1374039       |
      | transfereeHerd | C1150624       |
      | transfereeName | Annette O'Grady |
      | transferType   | Inheritance of Entitlements|
    Then the Add Entitlement button should not be present
