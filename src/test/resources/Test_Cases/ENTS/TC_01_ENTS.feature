Feature: TC_01_ENTS - Transfer Application End-to-End Regression Pack (Same Agent)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering all Transfer of Entitlements flows
  #   within the same agent in the BISS Application Portal.
  #
  #   Each section performs the full Transferor → Transferee cycle:
  #     Transferor : Search herd → Create Transfer → Search transferee → Select type →
  #                  Add entitlement → Notes → Upload document → Send for acceptance
  #     Transferee : Navigate to Transfers → Search herd → View → Enter transfer key →
  #                  Notes → Submit to DAFM
  #
  #   Transfer types covered:
  #     Section 1 : Change of Legal Entity  (code 206)
  #     Section 2 : Change of Registration  (code 201)
  #     Section 3 : Inheritance             (code 201)
  #     Section 4 : Gift                    (code 202)
  #     Section 5 : Lease                   (code 211) — includes lease year selection
  #     Section 6 : Division                (code 204)
  #     Section 7 : Sale                    (code 212)
  #
  # Migrated from: TC_01_ENTS.feature (legacy 8 separate scenarios)
  #   TC_01 → Section 1    TC_02 → Section 2    TC_03 → Section 3    TC_04 → Section 4
  #   TC_05 → Section 5    TC_07 → Section 6    TC_08 → Section 7
  #
  # Notes:
  #   1. Herd numbers are hardcoded test fixtures — update in TestData.xlsx if they expire.
  #   2. Each transfer uses 0.01 entitlement units as a minimal regression test value.
  #   3. The transferee acceptance uses the transfer key captured during the transferor flow.
  #   4. Steps are designed for reuse across TC_02_ENTS (different agent transfers) if needed.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Clients page

  @regression @transfers @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E - Agent completes all transfer types within the same agent

    # ===========================================
    # SECTION 1 : Change of Legal Entity (206)
    # Covers: TC_01
    # ===========================================

    # --- Transferor ---
    When the agent creates a transfer application with the following details
      | transferorHerd | C1010091         |
      | transfereeHerd | C1010148         |
      | transfereeName | Thomas Costelloe |
      | transferType   | 206              |
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1010148 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Change of Registration (201)
    # Covers: TC_02
    # ===========================================

    # --- Transferor ---
    When the agent navigates back to the Transfers client list
    And the agent creates a transfer application with the following details
      | transferorHerd | C1040578     |
      | transfereeHerd | C1010334     |
      | transfereeName | Nicola Lambe |
      | transferType   | 201          |
      | entitlements   | 0.01         |
      | notes          | Test Notes   |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1010334 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Inheritance of Entitlements (201)
    # Covers: TC_03
    # ===========================================

    # --- Transferor ---
    When the agent navigates back to the Transfers client list
    And the agent creates a transfer application with the following details
      | transferorHerd | C1040578         |
      | transfereeHerd | C1037011         |
      | transfereeName | Kathleen Doherty |
      | transferType   | 201              |
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1037011 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Gift of Entitlements (202)
    # Covers: TC_04
    # ===========================================

    # --- Transferor ---
    When the agent navigates back to the Transfers client list
    And the agent creates a transfer application with the following details
      | transferorHerd | C1040578            |
      | transfereeHerd | C1010121            |
      | transfereeName | Elizabeth Bradfield |
      | transferType   | 202                 |
      | entitlements   | 0.01                |
      | notes          | Test Notes          |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1010121 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Lease of Entitlements (211)
    # Covers: TC_05
    # NOTE: Lease requires an additional lease year selection step
    # ===========================================

    # --- Transferor ---
    When the agent navigates back to the Transfers client list
    And the agent creates a transfer application with the following details
      | transferorHerd | C1040578    |
      | transfereeHerd | C1050166    |
      | transfereeName | Peter Kelly |
      | transferType   | 211         |
      | entitlements   | 0.01        |
      | leaseYear      | Yes         |
      | notes          | Test Notes  |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1050166 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 6 : Division of Entitlements (204)
    # Covers: TC_07
    # ===========================================

    # --- Transferor ---
    When the agent navigates back to the Transfers client list
    And the agent creates a transfer application with the following details
      | transferorHerd | C1010776         |
      | transfereeHerd | C1050212         |
      | transfereeName | Padraig Costello |
      | transferType   | 204              |
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1050212 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 7 : Sale of Entitlements (212)
    # Covers: TC_08
    # ===========================================

    # --- Transferor ---
    When the agent navigates back to the Transfers client list
    And the agent creates a transfer application with the following details
      | transferorHerd | C1010776        |
      | transfereeHerd | C1070124        |
      | transfereeName | Michael Duignan |
      | transferType   | 212             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent navigates to the transferee acceptance flow
      | transfereeHerd | C1070124 |
    And the agent enters the transfer key and views the application
    And the agent submits the transfer to DAFM with notes "Approved Test"
    Then the transfer should be submitted successfully
