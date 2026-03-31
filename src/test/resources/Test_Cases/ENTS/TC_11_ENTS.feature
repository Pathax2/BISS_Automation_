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
    Given the ETF partner logs in as transferor "agr15678"
    And the agent opens the "Basic Income Support for Sustainability" application

  @regression @transfers @etf-to-agent @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-11 - ETF Partner completes all transfer types to Agent

    # SECTION 1 : CLE (206)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | D1391004           |
      | transfereeHerd   | B136017X           |
      | transfereeName   | Darragh Staunton   |
      | transfereeAddress| Knockeevan Park    |
      | transferType     | 206                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B136017X |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 2 : Change of Registration (205)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | D1190050           |
      | transfereeHerd   | B1470189           |
      | transfereeName   | Simon Thornton     |
      | transfereeAddress| Tullycleave St     |
      | transferType     | 205                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1470189 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 3 : Lease (211)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1350309           |
      | transfereeHerd   | B1870136           |
      | transfereeName   | Michael Mc Cullagh |
      | transfereeAddress| Oranmore Pk        |
      | transferType     | 211                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | leaseYear        | Yes                |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1870136 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 4 : Gift (202)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | D1391004           |
      | transfereeHerd   | D1460081           |
      | transfereeName   | Timothy Mangan     |
      | transfereeAddress| Ballybuggy Lawn    |
      | transferType     | 202                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | D1460081 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 5 : Merger (203)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1350309           |
      | transfereeHerd   | C2057024           |
      | transfereeName   | Laoise Murphy      |
      | transfereeAddress| Arcella Road       |
      | transferType     | 203                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | C2057024 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 6 : Division (204)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | D1190246           |
      | transfereeHerd   | B1470189           |
      | transfereeName   | Simon Thornton     |
      | transfereeAddress| Tullycleave St     |
      | transferType     | 204                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1470189 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 7 : Inheritance (201)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1360045           |
      | transfereeHerd   | B1870136           |
      | transfereeName   | Michael Mc Cullagh |
      | transfereeAddress| Oranmore Pk        |
      | transferType     | 201                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | B1870136 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 8 : Sale (212)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1350309           |
      | transfereeHerd   | D2760615           |
      | transfereeName   | Griffin Meats Ltd  |
      | transfereeAddress| Killnadrain Ave    |
      | transferType     | 212                |
      | entitlements     | 0.01               |
      | entitlementType  | BISS               |
      | netUV            | 1                  |
      | notes            | Test Notes         |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs in as transferee agent "aga6581"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | D2760615 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor
