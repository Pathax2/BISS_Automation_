Feature: TC_10_ENTS - Transfer Application E2E (Individual to ETF Partner)

  # Migrated from: TC_10_ENTS.feature (legacy 8 scenarios)
  # Transferor: Individual (various) | Transferee: ETF Partner (agr15678)
  # Reused: TC_08_ENTS (individual transferor), TC_07_ENTS (ETF transferee)
  #
  # Author: Aniket Pathare | Created: 31-03-2026

  @regression @transfers @individual-to-etf @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-10 - Individual completes all transfer types to ETF

    # SECTION 1 : Lease (211)
    When the individual logs in as transferor "SOUTHVIEW1"
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1051289           |
      | transfereeName | Browne Exports Ltd   |
      | transferType   | 211            |
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | D1051289  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 2 : Gift (202)
    When the individual logs in as transferor "TERENCE1"
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1190050           |
      | transfereeName | Thomas Treanor       |
      | transferType   | 202            |
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
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1190246           |
      | transfereeName | Oliver Griffin       |
      | transferType   | 212            |
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
    And the individual creates a transfer application with the following details
      | transfereeHerd | J1350309           |
      | transfereeName | Cadden Suppliers     |
      | transferType   | 201            |
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
    And the individual creates a transfer application with the following details
      | transfereeHerd | J1360045           |
      | transfereeName | Nestor Exports Ltd   |
      | transferType   | 203            |
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
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1051289           |
      | transfereeName | Browne Exports Ltd   |
      | transferType   | 204            |
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
    And the individual creates a transfer application with the following details
      | transfereeHerd | D1190050           |
      | transfereeName | Thomas Treanor       |
      | transferType   | 205            |
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
    And the individual creates a transfer application with the following details
      | transfereeHerd | J1360045           |
      | transfereeName | Nestor Exports Ltd   |
      | transferType   | 206            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the ETF partner "agr15678"
    And the ETF partner completes the transferee acceptance flow
      | transfereeHerd | J1360045  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
