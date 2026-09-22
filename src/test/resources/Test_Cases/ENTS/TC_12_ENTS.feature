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
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y104069X                 |
      | transfereeName   | Daniel Mulvany           |
      | transfereeAddress| Carrickedmond Heights    |
      | transferType     | Change of Legal Entity|
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 2 : Change of Registration (205)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | Change of Registration Details|
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 3 : Lease (211)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | Lease of Entitlements|
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured


    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 4 : Gift (202)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y1310159                 |
      | transfereeName   | Seamus Carolan           |
      | transfereeAddress| Unit Grove               |
      | transferType     | Gift of Entitlements     |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "DANIELPAUL"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1310159 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # SECTION 5 : Merger (203)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y104069X                 |
      | transfereeName   | Daniel Mulvany           |
      | transfereeAddress| Carrickedmond Heights    |
      | transferType     | Merger of 2 or more holdings (forming an unregistered Farm Partnership)|
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "PAUDYFROG"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y104069X      |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 6 : Division (204)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | Division of Entitlements (Scission)|
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # SECTION 7 : Inheritance (201)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y1041344                 |
      | transfereeName   | Felim Sullivan           |
      | transfereeAddress| Drumhoe Avenue           |
      | transferType     | Inheritance of Entitlements|
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1041344 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully


    # SECTION 8 : Sale (212)
    Given the ETF partner logs in as transferor "agr15512"
    And the agent opens the "Basic Income Support for Sustainability" application
    When the ETF partner creates a transfer with manual entitlements
      | transferorHerd   | C2057024                 |
      | transfereeHerd   | Y1310159                 |
      | transfereeName   | Seamus Carolan           |
      | transfereeAddress| Unit Grove               |
      | transferType     | Sale of Entitlements     |
      | entitlements     | 0.01                     |
      | entitlementType  | BISS                     |
      | netUV            | 1                        |
      | notes            | Test Notes               |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    When the agent logs out and re-logs in as the individual transferee "DANIELPAUL"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual completes the transferee acceptance flow
      | transfereeHerd | Y1310159 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
