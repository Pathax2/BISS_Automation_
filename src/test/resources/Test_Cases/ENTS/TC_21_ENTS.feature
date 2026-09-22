Feature: TC_21_ENTS - Verify Appeal submission for Category A

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering all Transfer of Entitlements flows
  #   within the same agent in the BISS Application Portal and then submitting the APPEAL
  #
  #   Each section performs the full Transferor → Transferee cycle:
  #     Transferor : Search herd → Create Transfer → Search transferee → Select type →
  #                  Add entitlement → Notes → Upload document → Send for acceptance
  #     Transferee : Navigate to Transfers → Search herd → View → Enter transfer key →
  #                  Notes → Submit to DAFM
  #
  #   Transfer types covered:
  #     Section 1 : Change of Legal Entity  (code 206)
  #     Section 2 : Change of Registration  (code 201)
  #     Section 3 : Inheritance             (code 201)
  #     Section 4 : Gift                    (code 202)
  #     Section 5 : Lease                   (code 211) — includes lease year selection
  #     Section 6 : Division                (code 204)
  #     Section 7 : Sale                    (code 212)
  #
  # Migrated from: TC_01_ENTS.feature (legacy 8 separate scenarios)
  #   TC_01 → Section 1    TC_02 → Section 2    TC_03 → Section 3    TC_04 → Section 4
  #   TC_05 → Section 5    TC_07 → Section 6    TC_08 → Section 7
  #
  # Notes:
  #   1. Herd numbers are hardcoded test fixtures — update in TestData.xlsx if they expire.
  #   2. Each transfer uses 0.01 entitlement units as a minimal regression test value.
  #   3. The transferee acceptance uses the transfer key captured during the transferor flow.
  #   4. Steps are designed for reuse across TC_02_ENTS (different agent transfers) if needed.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 31-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "NR/CISYF" tab on the My Clients page

  @regression @transfers @e2e @tc13
  Scenario: AT-ENTS-Appeal-E2E - Verify Appeal submission for Category A


    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
    | A. National Reserve (as Young Farmer) |
    And the agent proceeds past the category selection
    And the agent skips CISYF category if prompted

    And the agent selects farming entity "Company"
    And the agent enters company details
    | croNumber    | 23315673     |
    | companyName  | Company 1    |
    | secretaryName  | Mary Donald  |
    And the agent sets group member count to "1"
    And the agent enters group member details
    | memberIndex | name         | dob        | eligible |
    | 1           | John Smith   | 15/06/1995 | Yes      |
    And the agent confirms group status question as "Yes"
    And the agent proceeds to the qualification step

    When the agent completes the qualification details
    | hasQualification   | Yes                             |
    | dateOfCompletion   | 01/01/2026                      |
    | certificateAwarded | Yes                             |
    | college            | Athlone Institute of Technology |
    | qualification      | FETAC Certificate in Farming    |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
    | Eligible Farmer(s) Birth or Marriage Certificate documentation       |
    | Declaration of Effective Control and Decision Making Power (download |
    | Qualifications certificate or Confirmation of Education Form         |
    | Personal and Sensitive Documentation                                 |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully
    And Validate if Appeal can be made sucessfully
