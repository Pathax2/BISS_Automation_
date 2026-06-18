Feature: TC_10_ENTS - Transfer Application E2E (Individual to ETF Partner)

  # Migrated from: TC_10_ENTS.feature (legacy 8 scenarios)
  # Transferor: Individual (various) | Transferee: ETF Partner (agr15678)
  # Reused: TC_08_ENTS (individual transferor), TC_07_ENTS (ETF transferee)
  #
  # Author: Aniket Pathare | Created: 31-03-2026

  Background:
    Given the agent user is on the login page
    When the individual logs in as transferor "SOUTHVIEW1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |


  @regression @transfers @individual-to-etf @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-10 - Individual completes all transfer types to ETF

    # SECTION 1 : Lease (211)
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1460081            |
      | transfereeName | Timothy Mangan  |
      | transferType   |Lease of Entitlements|
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | D1460081  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 2 : Gift (202)
    When the individual logs in as transferor "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1190050           |
      | transfereeName | Thomas Treanor       |
      | transferType   | Merger of 2 or more holdings (forming an unregistered Farm Partnership)|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | D1190050  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 3 : Sale (212)
    When the individual logs in as transferor "DANIELPAUL"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1190246           |
      | transfereeName | Oliver Griffin       |
      | transferType   | Sale of Entitlements|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | D1190246  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 4 : Inheritance (201)
    When the individual logs in as transferor "SOUTHVIEW1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | J1350309           |
      | transfereeName | Cadden Suppliers     |
      | transferType   | Inheritance of Entitlements|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1350309  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 5 : Merger (203)
    When the individual logs in as transferor "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | J1360045           |
      | transfereeName | Nestor Exports Ltd   |
      | transferType   | Merger of 2 or more holdings (forming an unregistered Farm Partnership)|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1360045  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 6 : Division (204)
    When the individual logs in as transferor "DANIELPAUL"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1051289           |
      | transfereeName | Browne Exports Ltd   |
      | transferType   | Division of Entitlements (Scission)|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | D1051289  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 7 : Change of Registration (205)
    When the individual logs in as transferor "TERENCE1"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1190050           |
      | transfereeName | Thomas Treanor       |
      | transferType   | Change of Registration Details|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | D1190050  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 8 : Change of Legal Entity (206)
    When the individual logs in as transferor "DANIELPAUL"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    And the individual creates a transfer application with the following details
      | transfereeHerd | J1360045           |
      | transfereeName | Nestor Exports Ltd   |
      | transferType   | Change of Legal Entity|
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1360045  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
