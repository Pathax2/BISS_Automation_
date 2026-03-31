Feature: TC_05_ENTS - Transfer Application E2E Regression Pack (Agent to Individual)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering Transfer of Entitlements flows where the
  #   Transferor is an Agent and the Transferee is an Individual user (farmer login).
  #
  #   Key difference from TC_03_ENTS / TC_04_ENTS (Agent-to-Agent cross-agent):
  #     - Transferee logs in via the INDIVIDUAL login page (different URL)
  #     - Individual username format differs (e.g. "PAUDYFROG", "TERENCE1")
  #     - A "Close personal details" dialog must be dismissed after Individual login
  #     - Individual navigates directly to "Transfers" tab (no My Clients → tab switch)
  #     - Individual sees the transfer directly on their dashboard (no herd search needed)
  #
  #   Transfer types covered:
  #     Section 1 : Change of Registration  (205) — C1120016 → Y104069X (Daniel Mulvany / PAUDYFROG)
  #     Section 2 : Inheritance             (201) — C1120016 → Y104069X (Daniel Mulvany / PAUDYFROG)
  #     Section 3 : Gift                    (202) — C1120016 → Y1041344 (Felim Sullivan / TERENCE1)
  #     Section 4 : Lease                   (211) — C1120016 → Y1041344 (Felim Sullivan / TERENCE1) + lease year
  #
  # Migrated from: TC_05_ENTS.feature (legacy 4 separate scenarios)
  #   TC_17 → Section 1     TC_18 → Section 2     TC_19 → Section 3     TC_20 → Section 4
  #
  # Step reuse:
  #   Transferor flow (create/upload/send/capture)  → TC_01_ENTS.java
  #   Background login / nav                        → TC_03.java
  #   Tab switching                                 → TC_06.java
  #   Transferor re-login                           → TC_03_ENTS.java
  #   Submission verification                       → TC_01_ENTS.java
  #
  #   NEW steps in TC_05_ENTS.java (2):
  #     - "the agent logs out and re-logs in as the individual transferee {string}"
  #     - "the individual completes the transferee acceptance flow" (DataTable)
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

  @regression @transfers @agent-to-individual @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-05 - Agent completes all Agent-to-Individual transfer types

    # ===========================================
    # SECTION 1 : Change of Registration (205)
    # Covers: TC_17
    # Transferee: PAUDYFROG (Individual)
    # ===========================================

    # --- Transferor (Agent) ---
    When the agent creates a transfer application with the following details
      | transferorHerd | C1120016        |
      | transfereeHerd | Y104069X        |
      | transfereeName | Daniel Mulvany  |
      | transferType   | 205             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual — different login flow) ---
    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Inheritance (201)
    # Covers: TC_18
    # Transferee: PAUDYFROG (Individual)
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | C1120016        |
      | transfereeHerd | Y104069X        |
      | transfereeName | Daniel Mulvany  |
      | transferType   | 201             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual) ---
    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Gift of Entitlements (202)
    # Covers: TC_19
    # Transferee: TERENCE1 (Individual — different user)
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | C1120016        |
      | transfereeHerd | Y1041344        |
      | transfereeName | Felim Sullivan  |
      | transferType   | 202             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual — TERENCE1) ---
    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Lease of Entitlements (211)
    # Covers: TC_20
    # Transferee: TERENCE1 (Individual)
    # NOTE: Lease includes lease year selection
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | C1120016        |
      | transfereeHerd | Y1041344        |
      | transfereeName | Felim Sullivan  |
      | transferType   | 211             |
      | entitlements   | 0.01            |
      | leaseYear      | Yes             |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual — TERENCE1) ---
    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
