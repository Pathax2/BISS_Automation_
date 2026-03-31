Feature: TC_08_ENTS - Transfer Application E2E (Individual to Agent)

  # Migrated from: TC_08_ENTS.feature (legacy 8 scenarios)
  # Transferor: Individual (various usernames) | Transferee: Agent (aga6325)
  # All 8 transfer types covered: 206, 201, 202, 211, 203, 204, 212, 205
  #
  # New steps: individual logs in as transferor, individual creates transfer,
  #            agent logs in as transferee agent
  # Reused: TC_01_ENTS (send/capture), TC_05_ENTS (individual login pattern)
  #
  # Author: Aniket Pathare | Created: 31-03-2026

  @regression @transfers @individual-to-agent @e2e
  Scenario: AT-ENTS-TRANSFERS-E2E-08 - Individual completes all transfer types to Agent

    # ===========================================
    # SECTION 1 : Change of Legal Entity (206)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "FARMBLUE"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z1114042       |
      | transfereeName | Owens Mart Ltd       |
      | transferType   | 206            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z1114042 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 2 : Inheritance (201)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "JAMESSKEHILL"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z2170744       |
      | transfereeName | Hugh Patrick Gore    |
      | transferType   | 201            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z2170744 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 3 : Gift (202)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "EUGENECARR"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z1180657       |
      | transfereeName | Patrick Keenan       |
      | transferType   | 202            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z1180657 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 4 : Lease (211)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "CATHERINEMORAN2"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z1190016       |
      | transfereeName | Brendan Fitzjohn     |
      | transferType   | 211            |
      | entitlements   | 0.01           |
      | leaseYear      | Yes            |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z1190016 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 5 : Merger (203)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "HNOW1280483"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z1270559       |
      | transfereeName | Thomas Cooke         |
      | transferType   | 203            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z1270559 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 6 : Division (204)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "ANGELAMCGLYNN"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z1330942       |
      | transfereeName | Joanne Moroney       |
      | transferType   | 204            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z1330942 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 7 : Sale (212)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "JHARKIN1"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z1334069       |
      | transfereeName | Ronan O'Neill        |
      | transferType   | 212            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z1334069 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully

    # ===========================================
    # SECTION 8 : Change of Registration (205)
    # ===========================================

    # --- Transferor (Individual) ---
    When the individual logs in as transferor "B1090840"
    And the individual creates a transfer application with the following details
      | transfereeHerd | Z2120933       |
      | transfereeName | Richard Keenan       |
      | transferType   | 205            |
      | entitlements   | 0.01           |
      | notes          | Test Notes     |
    And the agent sends the transfer for acceptance
    Then the transfer key should be captured

    # --- Transferee (Agent) ---
    When the agent logs in as transferee agent "aga6325"
    And the agent completes the same agent transferee acceptance flow
      | transfereeHerd | Z2120933 |
      | notes          | Approved Test |
    Then the transfer should be submitted successfully
