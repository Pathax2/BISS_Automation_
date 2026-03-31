Feature: TC_12_ENTS - Transfer Application E2E (ETF Partner to Individual)

  # Migrated from: TC_12_ENTS.feature (legacy 8 scenarios)
  # Transferor: ETF Partner (agr15512) | Transferee: Individual (various)
  # Uses MANUAL entitlements same as TC_11
  # Reused: TC_11_ENTS (ETF manual create), TC_05_ENTS (individual transferee)
  #
  # Author: Aniket Pathare | Created: 31-03-2026

  Background:
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application

  @regression @transfers @etf-to-individual @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-12 - ETF Partner completes all transfer types to Individual

    # SECTION 1 : CLE (206)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1350147                 |
      | transfereeHerd   | Y104069X                 |
      | transfereeName   | Daniel Mulvany           |
      | transfereeAddress| Carrickedmond Heights    |
      | transferType     | 206                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 2 : Change of Registration (205)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1350457                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | 205                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 3 : Lease (211)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1691122                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | 211                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 4 : Gift (202)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1350457                 |
      | transfereeHerd   | Y1310159                 |
      | transfereeName   | Seamus Carolan           |
      | transfereeAddress| Unit Grove               |
      | transferType     | 202                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "DANIELPAUL"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1310159 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 5 : Merger (203)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1691122                 |
      | transfereeHerd   | Y104069X                 |
      | transfereeName   | Daniel Mulvany           |
      | transfereeAddress| Carrickedmond Heights    |
      | transferType     | 203                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 6 : Division (204)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | V2631112                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | 204                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 7 : Inheritance (201)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | J1691122                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | 201                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor

    # SECTION 8 : Sale (212)
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | V2631112                 |
      | transfereeHerd   | Y1310159                 |
      | transfereeName   | Seamus Carolan           |
      | transfereeAddress| Unit Grove               |
      | transferType     | 212                      |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "DANIELPAUL"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1310159 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
    When the ETF partner logs back in as transferor
