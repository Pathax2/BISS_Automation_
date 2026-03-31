Feature: TC_06_ENTS - Transfer Application E2E Regression Pack (Agent 6 to Individual)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering Transfer of Entitlements flows where the
  #   Transferor is Agent 6 (aga6504) and the Transferee is an Individual user.
  #   Same Agent-to-Individual pattern as TC_05_ENTS but with different agent and herds.
  #
  #   Transfer types covered:
  #     Section 1 : Merger                   (203) — A1090502 → Y1310159 (Seamus Carolan / DANIELPAUL)
  #     Section 2 : Division                 (204) — A1090588 → Y1310159 (Seamus Carolan / DANIELPAUL)
  #     Section 3 : Sale                     (212) — A1100087 → Y1041344 (Felim Sullivan / TERENCE1)
  #     Section 4 : Change of Legal Entity   (206) — A1100478 → Y104069X (Daniel Mulvany / PAUDYFROG)
  #
  # Migrated from: TC_06_ENTS.feature (legacy 4 separate scenarios)
  #   TC_21 → Section 1     TC_22 → Section 2     TC_23 → Section 3     TC_24 → Section 4
  #
  # Step reuse — ZERO new step definitions required:
  #   Background login / nav                         → TC_03.java
  #   Tab switching                                  → TC_06.java
  #   Transferor flow (create/upload/send/capture)   → TC_01_ENTS.java
  #   Transfer key capture                           → TC_01_ENTS.java
  #   Submission verification                        → TC_01_ENTS.java
  #   Transferor re-login (between sections)         → TC_03_ENTS.java
  #   Individual logout + re-login                   → TC_05_ENTS.java
  #   Individual transferee acceptance               → TC_05_ENTS.java
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
  Scenario: AT-ENTS-TRANSFERS-E2E-06 - Agent 6 completes all Agent-to-Individual transfer types

    # ===========================================
    # SECTION 1 : Merger of Entitlements (203)
    # Covers: TC_21
    # Transferee: DANIELPAUL (Individual)
    # ===========================================

    # --- Transferor (Agent 6) ---
    When the agent creates a transfer application with the following details
      | transferorHerd | A1090502        |
      | transfereeHerd | Y1310159        |
      | transfereeName | Seamus Carolan  |
      | transferType   | 203             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual — DANIELPAUL) ---
    When the agent logs out and re-logs in as the individual transferee "DANIELPAUL"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1310159      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Division of Entitlements (204)
    # Covers: TC_22
    # Transferee: DANIELPAUL (Individual)
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | A1090588        |
      | transfereeHerd | Y1310159        |
      | transfereeName | Seamus Carolan  |
      | transferType   | 204             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual — DANIELPAUL) ---
    When the agent logs out and re-logs in as the individual transferee "DANIELPAUL"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1310159      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Sale of Entitlements (212)
    # Covers: TC_23
    # Transferee: TERENCE1 (Individual — different user)
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | A1100087        |
      | transfereeHerd | Y1041344        |
      | transfereeName | Felim Sullivan  |
      | transferType   | 212             |
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
    # SECTION 4 : Change of Legal Entity (206)
    # Covers: TC_24
    # Transferee: PAUDYFROG (Individual)
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | A1100478        |
      | transfereeHerd | Y104069X        |
      | transfereeName | Daniel Mulvany  |
      | transferType   | 206             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Individual — PAUDYFROG) ---
    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
