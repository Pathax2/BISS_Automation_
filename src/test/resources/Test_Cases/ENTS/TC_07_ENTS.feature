Feature: TC_07_ENTS - Transfer Application E2E Regression Pack (Agent to ETF Partner)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering Transfer of Entitlements flows where the
  #   Transferor is an Agent and the Transferee is an ETF Partner user.
  #
  #   Key differences from Agent-to-Agent (TC_03/TC_04) and Agent-to-Individual (TC_05/TC_06):
  #     - Transferee logs in via the PARTNER login page (different URL from agent and individual)
  #     - Partner uses MS Authenticator OTP (not SMS OTP)
  #     - Partner login flow: username → Login → password → Login → MS Auth OTP → Login
  #     - Partner clicks the "ETF" button on the transferee dashboard (not standard View)
  #     - Partner searches for the transferee herd first, then clicks ETF
  #
  #   Transfer types covered:
  #     Section 1 : Change of Registration  (205) — J1390696 → J1314060 (Teresa Noone / agr15594)
  #     Section 2 : Inheritance             (201) — J1400039 → J1350023 (Frehill Suppliers / agr15594)
  #     Section 3 : Gift                    (202) — J1410417 → J1360045 (Nestor Exports Ltd / agr15678)
  #     Section 4 : Lease                   (211) — J1400195 → J1350309 (Cadden Suppliers / agr15678) + lease year
  #     Section 5 : Merger                  (203) — J1400217 → J1350147 (Sean Lally / agr15512)
  #     Section 6 : Division               (204) — J140033X → V2631112 (Nora White / agr15512)
  #     Section 7 : Sale                    (212) — J1400519 → J1350147 (Sean Lally / agr15512)
  #     Section 8 : Change of Legal Entity  (206) — J1350350 → J1350457 (Michael Gerard Gargan / agr15512)
  #
  # Migrated from: TC_07_ENTS.feature (legacy 8 separate scenarios)
  #   TC_25 → Section 1     TC_26 → Section 2     TC_28 → Section 3     TC_29 → Section 4
  #   TC_30 → Section 5     TC_31 → Section 6     TC_32 → Section 7     TC_33 → Section 8
  #
  # Step reuse:
  #   Transferor flow (create/upload/send/capture)  → TC_01_ENTS.java
  #   Background login / nav                        → TC_03.java
  #   Tab switching                                 → TC_06.java
  #   Transferor re-login                           → TC_03_ENTS.java
  #   Submission verification                       → TC_01_ENTS.java
  #
  #   NEW steps in TC_07_ENTS.java (2):
  #     - "the agent logs out and re-logs in as the ETF partner {string}"
  #     - "the ETF partner completes the transferee acceptance flow" (DataTable)
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page

  @regression @transfers @agent-to-etf @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-07 - Agent completes all Agent-to-ETF Partner transfer types

    # ===========================================
    # SECTION 1 : Change of Registration (205)
    # Covers: TC_25
    # ETF Partner: agr15594
    # ===========================================

    # --- Transferor (Agent) ---
    When the agent creates a transfer application with the following details
      | transferorHerd | J1390696      |
      | transfereeHerd | J1314060      |
      | transfereeName | Teresa Noone  |
      | transferType   | Change of Registration Details|
      | entitlements   | 0.01          |
      | notes          | Test Notes    |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6322"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1314060      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Inheritance (201)
    # Covers: TC_26
    # ETF Partner: agr15594
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J1400039          |
      | transfereeHerd | J1350023          |
      | transfereeName | Frehill Suppliers |
      | transferType   | Inheritance of Entitlements|
      | entitlements   | 0.01              |
      | notes          | Test Notes        |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6322"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | Frehill Suppliers      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Gift of Entitlements (202)
    # Covers: TC_28
    # ETF Partner: agr15678
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J1410417           |
      | transfereeHerd | J1360045           |
      | transfereeName | Nestor Exports Ltd |
      | transferType   | Gift of Entitlements|
      | entitlements   | 0.01               |
      | notes          | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner — different partner account) ---
    When the agent logs out and re-logs in as the ETF partner "aga6454"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | Nestor Exports Ltd      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Lease of Entitlements (211)
    # Covers: TC_29
    # ETF Partner: agr15678
    # NOTE: Lease includes lease year selection
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6365"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J1400195          |
      | transfereeHerd | J1350309          |
      | transfereeName | Cadden Suppliers  |
      | transferType   | Lease of Entitlements|
      | entitlements   | 0.01              |
      | leaseYear      | Yes               |
      | notes          | Test Notes        |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6454"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1350309      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Merger of Entitlements (203)
    # Covers: TC_30
    # ETF Partner: agr15512
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J1400217    |
      | transfereeHerd | J1350147    |
      | transfereeName | Sean Lally  |
      | transferType   | Merger of 2 or more holdings (forming an unregistered Farm Partnership) |
      | entitlements   | 0.01        |
      | notes          | Test Notes  |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6066"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1350147      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 6 : Division of Entitlements (204)
    # Covers: TC_31
    # ETF Partner: agr15512
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J140033X    |
      | transfereeHerd | V2631112    |
      | transfereeName | Nora White  |
      | transferType   | Division of Entitlements (Scission) |
      | entitlements   | 0.01        |
      | notes          | Test Notes  |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6322"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | V2631112      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 7 : Sale of Entitlements (212)
    # Covers: TC_32
    # ETF Partner: agr15512
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J1400519    |
      | transfereeHerd | J1350147    |
      | transfereeName | Sean Lally  |
      | transferType   |  Sale of Entitlements |
      | entitlements   | 0.01        |
      | notes          | Test Notes  |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6066"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1350147      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 8 : Change of Legal Entity (206)
    # Covers: TC_33
    # ETF Partner: agr15512
    # ===========================================

    # --- Transferor ---
    When the agent logs out and re-log in as the transferee agent "aga6322"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | J1350350              |
      | transfereeHerd | J1350457              |
      | transfereeName | Michael Gerard Gargan |
      | transferType   | Change of Legal Entity|
      | entitlements   | 0.01                  |
      | notes          | Test Notes            |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (ETF Partner) ---
    When the agent logs out and re-logs in as the ETF partner "aga6505"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1350457      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
