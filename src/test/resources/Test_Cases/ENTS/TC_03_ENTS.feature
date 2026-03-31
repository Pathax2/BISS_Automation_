Feature: TC_03_ENTS - Transfer Application E2E Regression Pack (Different Agent / Cross-Agent)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering Transfer of Entitlements flows where the
  #   Transferor and Transferee are managed by DIFFERENT agents. The Transferee acceptance
  #   requires logging out of the Transferor agent session and re-logging in as the Transferee agent.
  #
  #   This is the critical difference from TC_01_ENTS / TC_02_ENTS (same agent):
  #     Same agent   : Transferee acceptance happens within the same session
  #     Cross-agent  : Requires logout → re-login as Transferee agent → accept → submit
  #
  #   Transfer types covered:
  #     Section 1 : Merger       (203) — H1430795 → A1080981 (David Milligan)
  #     Section 2 : Division     (204) — H1610394 → A1150106 (Geraldine Brady)
  #     Section 3 : Sale         (212) — H1510438 → A1151617 (Joseph Grennan)
  #     Section 4 : Sale         (212) — H1510438 → A1172053 (Sean McGinty)
  #     Section 5 : Lease        (211) — H1510438 → A1211539 (John Flahavan) + lease year
  #     Section 6 : Inheritance  (201) — H1510438 → A1284021 (Dan Kelleher)
  #
  # Migrated from: TC_03_ENTS.feature (legacy 6 separate scenarios)
  #   TC_09  → Section 1     TC_10  → Section 2     TC_11  → Section 3
  #   TC_40  → Section 4     TC_39  → Section 5     TC_38  → Section 6
  #
  # Step reuse:
  #   Transferor flow steps       → TC_01_ENTS.java (creates transfer, uploads doc, sends for acceptance)
  #   Login / portal nav          → TC_03.java
  #   Tab switching               → TC_06.java
  #   NEW steps in this file:
  #     - "the agent logs out and re-logs in as the transferee agent"
  #     - "the agent completes the cross-agent transferee acceptance flow"
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
  Scenario: AT-ENTS-TRANSFERS-E2E-03 - Agent completes all cross-agent transfer types

    # ===========================================
    # SECTION 1 : Merger of Entitlements (203)
    # Covers: TC_09
    # ===========================================

    # --- Transferor (Agent 3) ---
    When the agent creates a transfer application with the following details
      | transferorHerd | H1430795        |
      | transfereeHerd | A1080981        |
      | transfereeName | David Milligan  |
      | transferType   | 203             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (different agent — requires logout + re-login) ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1080981      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Division of Entitlements (204)
    # Covers: TC_10
    # ===========================================

    # --- Transferor (Agent 3 — re-login back) ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | H1610394        |
      | transfereeHerd | A1150106        |
      | transfereeName | Geraldine Brady |
      | transferType   | 204             |
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
    # SECTION 3 : Sale of Entitlements (212)
    # Covers: TC_11
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | H1510438        |
      | transfereeHerd | A1151617        |
      | transfereeName | Joseph Grennan  |
      | transferType   | 212             |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1151617      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Sale of Entitlements (212) — different transferee
    # Covers: TC_40
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | H1510438     |
      | transfereeHerd | A1172053     |
      | transfereeName | Sean McGinty |
      | transferType   | 212          |
      | entitlements   | 0.01         |
      | notes          | Test Notes   |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1172053      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Lease of Entitlements (211)
    # Covers: TC_39
    # NOTE: Lease includes lease year selection
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | H1510438       |
      | transfereeHerd | A1211539       |
      | transfereeName | John Flahavan  |
      | transferType   | 211            |
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1211539      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 6 : Inheritance (201)
    # Covers: TC_38
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-logs in as the transferor agent
    And the agent creates a transfer application with the following details
      | transferorHerd | H1510438      |
      | transfereeHerd | A1284021      |
      | transfereeName | Dan Kelleher  |
      | transferType   | 201           |
      | entitlements   | 0.01          |
      | notes          | Test Notes    |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | A1284021      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
