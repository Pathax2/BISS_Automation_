Feature: TC_11_ENTS - Transfer Application E2E (ETF Partner to Agent)

  # Migrated from: TC_11_ENTS.feature (legacy 8 scenarios)
  # Transferor: ETF Partner (agr15678) | Transferee: Agent (aga6581)
  # KEY DIFFERENCE: Uses MANUAL entitlements (Add Manual + Type + Net UV) instead of standard Add
  # Also includes txeeAddress field in transferee search
  #
  # New steps: ETF partner creates transfer with manual entitlements
  # Reused: TC_07_ENTS (ETF login), TC_03_ENTS (agent transferee)
  #
  # Author: Aniket Pathare | Created: 31-03-2026

  Background:
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application


  @regression @transfers @etf-to-agent @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-11 - ETF Partner completes all transfer types to Agent

    #-------------------------------------------- SECTION 1 : CLE (206)----------------------------------------------------
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | B136017X           |
      | transfereeName   | Darragh Staunton   |
      | transfereeAddress| Knockeevan Park    |
      | transferType     | Change of Legal Entity|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B136017X |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    #---------------------------------------------------- SECTION 2 : Change of Registration (205)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | B1470189           |
      | transfereeName   | Simon Thornton     |
      | transfereeAddress| Tullycleave St     |
      | transferType     | Change of Registration Details|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1470189 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # ----------------------------------------------------SECTION 3 : Lease (211)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | B1870136           |
      | transfereeName   | Michael Mc Cullagh |
      | transfereeAddress| Oranmore Pk        |
      | transferType     | Lease of Entitlements|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | leaseYear        | Yes                |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1870136 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # ----------------------------------------------------SECTION 4 : Gift (202)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | D1460081           |
      | transfereeName   | Timothy Mangan     |
      | transfereeAddress| Ballybuggy Lawn    |
      | transferType     | Gift of Entitlements|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | D1460081 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    #---------------------------------------------------- SECTION 5 : Merger (203)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | D3320139           |
      | transfereeName   | Felix Mc Donagh     |
      | transfereeAddress| Ballyine Abbey       |
      | transferType     | Merger of 2 or more holdings (forming an unregistered Farm Partnership)|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | D3320139 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    #---------------------------------------------------- SECTION 6 : Division (204)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | B1470189           |
      | transfereeName   | Simon Thornton     |
      | transfereeAddress| Tullycleave St     |
      | transferType     | Division of Entitlements (Scission)|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1470189 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # ----------------------------------------------------SECTION 7 : Inheritance (201)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | B1870136           |
      | transfereeName   | Michael Mc Cullagh |
      | transfereeAddress| Oranmore Pk        |
      | transferType     | Inheritance of Entitlements|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1870136 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # ----------------------------------------------------SECTION 8 : Sale (212)----------------------------------------------------
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024           |
      | transfereeHerd   | D2760615           |
      | transfereeName   | Griffin Meats Ltd  |
      | transfereeAddress| Killnadrain Ave    |
      | transferType     | Sale of Entitlements|
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent uploads the transferor signature document
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs in as transferee agent "aga6581"
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Client page
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | D2760615 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
