Feature: TC_03_ENTS - Transfer Application E2E Regression Pack (One agent per section)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   End-to-end regression journey covering six transfer types. Each section logs in as a different agent and runs the
  #   whole cycle - create, send, accept, submit - inside that one session.
  #
  #     Section 1 : Merger of Entitlements     - agent aga6509
  #     Section 2 : Division (Scission)        - agent aga6581
  #     Section 3 : Sale of Entitlements       - agent agr25315
  #     Section 4 : Sale of Entitlements       - agent aga6555
  #     Section 5 : Lease of Entitlements      - agent aga6701 (with lease year)
  #     Section 6 : Inheritance of Entitlements- agent aga6743
  #
  # NOTE on the name of this test case (22-09-2026):
  #   The header used to call this cross-agent, but the steps never log out and back in as a second agent: the
  #   acceptance runs in the same session that created the transfer, so both herds belong to the agent of that section.
  #   The herd tokens are therefore picked as a same-agent pair. TC_04_ENTS is the real cross-agent journey.
  #   If a section is meant to be cross-agent, add a transfereeAgent row to its create table and a
  #   "the agent logs out and re-logs in as the transferee agent" step, exactly as TC_04_ENTS does.
  #
  # Runtime data (22-09-2026):
  #   Every herd cell is a token. The herds come from the ENTS Agent Login query for the agent logged in at that point,
  #   both herds come from the same agent, and a herd is never reused across runs
  #   (runtime-data/ents_used_herds.csv). See guide/STEP_06_ENTS_TC01.md.
  #     {transferor.herd}  {transferee.herd}  {transferee.name}
  #   Put a herd number back in any cell to pin it for debugging - a value that is not a token is used as it is.
  #   Each section picks a fresh pair, so the six sections never collide.
  #
  # Step reuse:
  #   Login / portal navigation    -> stepdefinitions.TC_03
  #   Tab switching                -> stepdefinitions.TC_06
  #   Login as a named user        -> stepdefinitions.ENTS.TC_08_ENTS
  #   Create / upload / send / key -> stepdefinitions.ENTS.TC_01_ENTS
  #   Transferee acceptance        -> stepdefinitions.ENTS.TC_03_ENTS
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6509"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page

  @regression @transfers @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-03 - Agent completes all transfer types

    # ===========================================
    # SECTION 1 : Merger of Entitlements - aga6509
    # ===========================================

    # --- Transferor ---
    When the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
      | transferType   | Merger of 2 or more holdings (forming an unregistered Farm Partnership) |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Division of Entitlements - aga6581
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6581"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
      | transferType   | Division of Entitlements (Scission) |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Sale of Entitlements - agr25315
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "agr25315"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
      | transferType   |  Sale of Entitlements |
      | entitlements   | 0.01            |
      | notes          | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Sale of Entitlements - aga6555
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6555"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
      | transferType   |  Sale of Entitlements|
      | entitlements   | 0.01         |
      | notes          | Test Notes   |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Lease of Entitlements - aga6701
    # NOTE: Lease includes lease year selection
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6701"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
      | transferType   |  Lease of Entitlements|
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 6 : Inheritance - aga6743
    # ===========================================

    # --- Transferor ---
    When the individual logs in as transferor "aga6743"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
      | transferType   | Inheritance of Entitlements|
      | entitlements   | 0.01          |
      | notes          | Test Notes    |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
