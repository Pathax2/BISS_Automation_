Feature: TC_04_ENTS - Transfer Application E2E Regression Pack (Cross-Agent)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   End-to-end regression journey where the transferor and the transferee are managed by DIFFERENT agents. The
  #   transferor agent creates the transfer, the session logs out and logs back in as the transferee agent, and that
  #   agent accepts it.
  #
  #     Section 1 : Lease of Entitlements      - aga6060 to aga6325  (with lease year)
  #     Section 2 : Inheritance of Entitlements- aga6060 to agr17724
  #     Section 3 : Change of Registration     - aga6060 to aga6352
  #     Section 4 : Change of Legal Entity     - aga6060 to aga6306
  #     Section 5 : Gift of Entitlements       - aga6060 to agr22612
  #
  # Runtime data (22-09-2026):
  #   Every herd cell is a token. The transfereeAgent row is what makes a section cross-agent: the transferor herd is
  #   taken from the logged-in agent's pool and the transferee herd from that other agent's pool
  #   (EntsTestData.nextCrossAgentPair). Both are recorded in runtime-data/ents_used_herds.csv and never reused.
  #     {transferor.herd}  {transferee.herd}  {transferee.name}  {transferee.agent}
  #   The login step uses {transferee.agent}, so it is always the agent named in the create table - the two cannot
  #   drift apart. Put a herd number or a login back in any cell to pin it for debugging.
  #
  #   Because the transferee herd belongs to another agent, it is NOT in the transferor agent's Transfer Out list. The
  #   create step therefore checks only the transferor on screen; the transferee herd is checked after the login swap,
  #   by the acceptance step. See guide/STEP_06_ENTS_TC01.md.
  #
  #   Each transferee agent's pool is fetched once per run (one Agent Login query each, a few seconds), so this test
  #   case runs six queries in total: one per agent.
  #
  # Step reuse - no new step definitions:
  #   Login / portal navigation      -> stepdefinitions.TC_03
  #   Tab switching                  -> stepdefinitions.TC_06
  #   Login as a named user          -> stepdefinitions.ENTS.TC_08_ENTS
  #   Create / upload / send / key   -> stepdefinitions.ENTS.TC_01_ENTS
  #   Cross-agent logout / re-login  -> stepdefinitions.ENTS.TC_03_ENTS
  #   Cross-agent acceptance         -> stepdefinitions.ENTS.TC_03_ENTS
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6060"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page

  @regression @transfers @cross-agent @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-04 - Agent 4 completes all cross-agent transfer types

    # ===========================================
    # SECTION 1 : Lease of Entitlements - aga6060 to aga6325
    # NOTE: Lease includes lease year selection
    # ===========================================

    # --- Transferor (aga6060) ---
    When the agent creates a transfer application with the following details
      | transferorHerd  | {transferor.herd} |
      | transfereeHerd  | {transferee.herd} |
      | transfereeName  | {transferee.name} |
      | transfereeAgent | aga6325           |
      | transferType    | Lease of Entitlements|
      | entitlements    | 0.01           |
      | leaseYear       | Yes            |
      | notes           | Test Notes     |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (different agent) ---
    When the agent logs out and re-logs in as the transferee agent "{transferee.agent}"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Inheritance - aga6060 to agr17724
    # ===========================================

    # --- Transferor ---
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6060"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd  | {transferor.herd} |
      | transfereeHerd  | {transferee.herd} |
      | transfereeName  | {transferee.name} |
      | transfereeAgent | agr17724          |
      | transferType    | Inheritance of Entitlements|
      | entitlements    | 0.01            |
      | notes           | Test Notes      |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent "{transferee.agent}"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Change of Registration - aga6060 to aga6352
    # ===========================================

    # --- Transferor ---
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6060"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd  | {transferor.herd} |
      | transfereeHerd  | {transferee.herd} |
      | transfereeName  | {transferee.name} |
      | transfereeAgent | aga6352           |
      | transferType    | Change of Registration Details|
      | entitlements    | 0.01           |
      | notes           | Test Notes     |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent "{transferee.agent}"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Change of Legal Entity - aga6060 to aga6306
    # ===========================================

    # --- Transferor ---
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6060"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd  | {transferor.herd} |
      | transfereeHerd  | {transferee.herd} |
      | transfereeName  | {transferee.name} |
      | transfereeAgent | aga6306           |
      | transferType    | Change of Legal Entity|
      | entitlements    | 0.01             |
      | notes           | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent "{transferee.agent}"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Gift of Entitlements - aga6060 to agr22612
    # ===========================================

    # --- Transferor ---
    Given the agent user is on the login page
    When the individual logs in as transferor "aga6060"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent creates a transfer application with the following details
      | transferorHerd  | {transferor.herd} |
      | transfereeHerd  | {transferee.herd} |
      | transfereeName  | {transferee.name} |
      | transfereeAgent | agr22612          |
      | transferType    | Gift of Entitlements|
      | entitlements    | 0.01             |
      | notes           | Test Notes       |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee ---
    When the agent logs out and re-logs in as the transferee agent "{transferee.agent}"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the cross-agent transferee acceptance flow
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
