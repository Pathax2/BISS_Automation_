Feature: TC_04_ENTS - Transfer Application E2E Regression Pack (Different Agent 4 / Cross-Agent)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering Transfer of Entitlements flows where the
  #   Transferor agent (Agent 4 / aga6060) and Transferee agent are different users.
  #   Same cross-agent logout → re-login pattern as TC_03_ENTS.
  #
  #   Transfer types covered:
  #     Section 1 : Lease                      (211) — G1331310 → A1080981 (David Milligan) + lease year
  #     Section 2 : Inheritance                (201) — G1371095 → A1150106 (Geraldine Brady)
  #     Section 3 : Change of Registration     (205) — G1610022 → A1090421 (Patrick Healy)
  #     Section 4 : Change of Legal Entity     (206) — G1861980 → A1314036 (John F O'Leary)
  #     Section 5 : Gift                       (202) — G1910786 → A1410400 (Luke O'Sullivan)
  #
  # Migrated from: TC_04_ENTS.feature (legacy 5 separate scenarios)
  #   TC_12 → Section 1     TC_13 → Section 2     TC_14 → Section 3
  #   TC_15 → Section 4     TC_16 → Section 5
  #
  # Step reuse — ZERO new step definitions required:
  #   Background login / nav             → TC_03.java
  #   Tab switching                      → TC_06.java
  #   Transferor flow (create/upload/send) → TC_01_ENTS.java
  #   Transfer key capture               → TC_01_ENTS.java
  #   Cross-agent logout / re-login      → TC_03_ENTS.java
  #   Cross-agent transferee acceptance  → TC_03_ENTS.java
  #   Submission verification            → TC_01_ENTS.java
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

  @regression @transfers @cross-agent @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-04 - Agent 4 completes all cross-agent transfer types

    # ===========================================
    # SECTION 1 : Lease of Entitlements (211)
    # Covers: TC_12
    # NOTE: Lease includes lease year selection
    # ===========================================

    # --- Transferor (Agent 4) ---
    When the agent creates a transfer application with the following details
      | transferorHerd | G1331310       |
      | transfereeHerd | A1080981       |
      | transfereeName | David Milligan |
      | transferType   | 211            |
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (different agent) ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1080981      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Inheritance (201)
    # Covers: TC_13
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | G1371095        |
      | transfereeHerd | A1150106        |
      | transfereeName | Geraldine Brady |
      | transferType   | 201             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1150106      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Change of Registration (205)
    # Covers: TC_14
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | G1610022       |
      | transfereeHerd | A1090421       |
      | transfereeName | Patrick Healy  |
      | transferType   | 205            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1090421      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Change of Legal Entity (206)
    # Covers: TC_15
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | G1861980         |
      | transfereeHerd | A1314036         |
      | transfereeName | John F O'Leary   |
      | transferType   | 206              |
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1314036      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Gift of Entitlements (202)
    # Covers: TC_16
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | G1910786         |
      | transfereeHerd | A1410400         |
      | transfereeName | Luke O'Sullivan  |
      | transferType   | 202              |
      | entitlements   | 0.01             |
      | notes          | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1410400      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
