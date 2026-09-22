Feature: TC_02_ENTS - Transfer Application E2E Regression Pack (Different Agent)

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   End-to-end regression journey for Transfer of Entitlements using a different agent from TC_01_ENTS.
  #
  #   Section 1 : Merger of Entitlements - full Transferor to Transferee cycle (same agent, same session)
  #   Section 2 : Negative case - herd without entitlements (Add Entitlement button absent)
  #
  # Runtime data (22-09-2026):
  #   Section 1 herd cells are tokens. The herds are picked at runtime from the ENTS Agent Login query for whoever is
  #   logged in, both herds come from the same agent, and a herd is never reused across runs
  #   (runtime-data/ents_used_herds.csv). See guide/STEP_06_ENTS_TC01.md.
  #     {transferor.herd}  {transferee.herd}  {transferee.name}
  #   Put a herd number back in any cell to pin it for debugging - a value that is not a token is used as it is.
  #
  #   Section 2 herds stay hard-coded ON PURPOSE. The Agent Login query only returns herds holding more than 10
  #   entitlements, so it can never supply a herd with none. A1374039 is the fixture from the Selenium version; if it
  #   stops working, swap in another herd with no entitlements for aga6077 (or ask for a "no entitlements" query and a
  #   token of its own). The transferee name must match the portal exactly, Jnr and middle names included.
  #
  # Step reuse:
  #   Login / portal navigation       -> stepdefinitions.TC_03
  #   Tab switching                   -> stepdefinitions.TC_06
  #   Login as a named user           -> stepdefinitions.ENTS.TC_08_ENTS
  #   Create / upload / send / key    -> stepdefinitions.ENTS.TC_01_ENTS
  #   Transferee acceptance           -> stepdefinitions.ENTS.TC_07_ENTS
  #   Negative entitlement validation -> stepdefinitions.ENTS.TC_02_ENTS
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026 | Updated: 22-09-2026 (Playwright + runtime ENTS data)
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
    # Herds picked at runtime from aga6535's pool
    # ===========================================

    # --- Transferor ---
    When the agent creates a transfer application with the following details
      | transferorHerd | {transferor.herd} |
      | transfereeHerd | {transferee.herd} |
      | transfereeName | {transferee.name} |
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
      | transfereeHerd | {transferee.herd} |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Negative - herd without entitlements
    # Hard-coded herds - see the note in the header
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
