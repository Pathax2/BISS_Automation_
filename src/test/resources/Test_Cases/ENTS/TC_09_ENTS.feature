Feature: TC_09_ENTS - Transfer Application E2E (Individual to Individual)

  # Migrated from: TC_09_ENTS.feature (legacy 8 scenarios)
  # Transferor: Individual (various) | Transferee: Individual (various)
  # All 8 transfer types covered
  # Reused: TC_08_ENTS (individual transferor login/create), TC_05_ENTS (individual transferee)
  #
  # Author: Aniket Pathare | Created: 31-03-2026

  @regression @transfers @individual-to-individual @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-09 - Individual completes all transfer types to Individual

    # SECTION 1 : Lease (211)
    When the individual logs in as transferor "SOUTHVIEW1"
    And the individual creates a transfer application with the following details
      | transfereeHerd | T1060418       |
      | transfereeName | Patrick Hoban                            |
      | transferType   | 211            |
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "3970723H"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | T1060418  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 2 : Sale (212)
    When the individual logs in as transferor "RENATENOHL@HOTMAIL.COM"
    And the individual creates a transfer application with the following details
      | transfereeHerd | M2081822       |
      | transfereeName | Mary Ann Ryan & P J Quinn                |
      | transferType   | 212            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "4589015L"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | M2081822  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 3 : Gift (202)
    When the individual logs in as transferor "3029508B"
    And the individual creates a transfer application with the following details
      | transfereeHerd | O119072X       |
      | transfereeName | Louise Cunningham                        |
      | transferType   | 202            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "JOHNBALLYORAN"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | O119072X  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 4 : Division (204)
    When the individual logs in as transferor "H2200068"
    And the individual creates a transfer application with the following details
      | transfereeHerd | R126120X       |
      | transfereeName | Margt Reid                               |
      | transferType   | 204            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "6838713O"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | R126120X  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 5 : Merger (203)
    When the individual logs in as transferor "EILEEN.KEENAN"
    And the individual creates a transfer application with the following details
      | transfereeHerd | D2560055       |
      | transfereeName | Gerard Hazelwood & Timothy O'Sullivan    |
      | transferType   | 203            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "TIMOTHY.MURPHY"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | D2560055  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 6 : Change of Registration (205)
    When the individual logs in as transferor "KARENKELLYWH"
    And the individual creates a transfer application with the following details
      | transfereeHerd | X1370418       |
      | transfereeName | John Burke                               |
      | transferType   | 205            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "KADAGH.GEOGHEGAN"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | X1370418  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 7 : Change of Legal Entity (206)
    When the individual logs in as transferor "BRENDANCLARKE"
    And the individual creates a transfer application with the following details
      | transfereeHerd | N1211270       |
      | transfereeName | Michael Kelleher Jnr                     |
      | transferType   | 206            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "JOHNSHERIDAN89"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | N1211270  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # SECTION 8 : Inheritance (201)
    When the individual logs in as transferor "BRENDANCLARKE"
    And the individual creates a transfer application with the following details
      | transfereeHerd | N1211270       |
      | transfereeName | Michael Kelleher Jnr                     |
      | transferType   | 201            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured
    When the agent logs out and re-logs in as the individual transferee "JOHNSHERIDAN89"
    And the individual completes the transferee acceptance flow
      | transfereeHerd | N1211270  |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
