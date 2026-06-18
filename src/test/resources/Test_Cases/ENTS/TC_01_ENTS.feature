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
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page

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
      | transferType   | Change of Legal Entity|
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | C1010148  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Change of Registration (201)
    # Covers: TC_02
    # ===========================================

    # --- Transferor
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | C1010148     |
      | transfereeHerd | C102010X     |
      | transfereeName | John Sweeney |
      | transferType   | Change of Registration Details|
      | entitlements   | 0.01         |
      | notes          | Test Notes   |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | C102010X  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Inheritance of Entitlements (201)
    # Covers: TC_03
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | C2060068         |
      | transfereeHerd | V1421165         |
      | transfereeName | Martin Barrett |
      | transferType   | Inheritance of Entitlements |
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | V1421165  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Gift of Entitlements (202)
    # Covers: TC_04
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | C1820026            |
      | transfereeHerd | C1670423            |
      | transfereeName | Patrick Gilmore |
      | transferType   | Gift of Entitlements|
      | entitlements   | 0.01                |
      | notes          | Test Notes          |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | C1670423  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Lease of Entitlements (211)
    # Covers: TC_05
    # NOTE: Lease requires an additional lease year selection step
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | C1880193    |
      | transfereeHerd | G1831576    |
      | transfereeName | Quish Mart Ltd |
      | transferType   | Lease of Entitlements|
      | entitlements   | 0.01        |
      | leaseYear      | Yes         |
      | notes          | Test Notes  |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | G1831576  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 6 : Division of Entitlements (204)
    # Covers: TC_07
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | C1250483         |
      | transfereeHerd | C1050212         |
      | transfereeName | Padraig Costello |
      | transferType   | Division of Entitlements (Scission)|
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | C1050212 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 7 : Sale of Entitlements (212)
    # Covers: TC_08
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6077"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | C1380352        |
      | transfereeHerd | T1210622        |
      | transfereeName | Seamus Ryder |
      | transferType   | Sale of Entitlements|
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | T1210622 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
