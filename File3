Feature: TC_15_ENTS - NRCISYF End-to-End with Staff ENTSCore Verification

  # Migrated from: TC_15_ENTS.feature (1 scenario)
  # Agent submits NRCISYF Cat A Individual, then staff verifies in ENTSCore
  # Reused: TC_13_ENTS (NRCISYF steps) — most steps from NRCISYF flow
  # New: Staff login + ENTSCore verification steps
  # Author: Aniket Pathare | Created: 31-03-2026

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "NR/CISYF" tab on the My Clients page

  @sanity @nrcisyf @staff-verification @tc13
  Scenario: AT-ENTS-NRCISYF-STAFF - Submit NRCISYF and verify in ENTSCore

    # --- Collect herds and capture owner ID ---
    When the agent searches for the NRCISYF herd and opens the application

    # --- NRCISYF Cat A Individual submission ---
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer) |
    And the agent proceeds past the category selection
    And the agent skips CISYF category if prompted

    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 01/01/2026                      |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
      | Qualifications certificate or Confirmation of Education Form |
      | Personal and Sensitive Documentation                         |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully

    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "NR/CISYF" tab on the My Clients page

    # --- Collect herds and capture owner ID ---
    When the agent searches for the NRCISYF herd and opens the application
    When the agent navigates through the farmer side navigation tabs
      |Entitlements / Usage |
    And the agent captures the OwnerID of the herd
    When the agent navigates through the farmer side navigation tabs
      |NR-CISYF |

    # --- Staff ENTSCore verification ---
    When the staff user logs into ENTSCore as "AGR2214"
    And the staff searches for the herd by OwnerID
    And the staff navigates to year "2026" and "NR / CISYF" tab
    Then the NRCISYF submission should be visible in ENTSCore
