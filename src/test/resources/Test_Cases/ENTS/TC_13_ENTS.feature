Feature: TC_13 - NRCISYF End-to-End Regression Pack

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering the entire NRCISYF (National Reserve /
  #   Complementary Income Support for Young Farmers) business flow in the BISS Application Portal.
  #
  #   - Login and NRCISYF tab navigation
  #   - Landing page validation (close date, info icons, dialog behaviour)
  #   - Category selection validations (A, B, C, A+B invalid, A+C, B+C)
  #   - Additional CISYF category prompt
  #   - Joint Herdowner — group member count validation
  #   - Individual applications — full submit for all category combos (A, B, C, A+C, B+C)
  #   - Company application — Cat A only (1 member), Cat A&C (2 members), Cat A (3 members)
  #   - No qualification / Covid delay path
  #   - Save and Exit mid-application
  #   - Custom education institution not in list
  #   - Custom qualification not in list
  #   - Invalid file format upload error
  #   - Close upload popup mid-flow
  #   - Post-submission — view application + download documents + correspondence
  #
  # Notes:
  #   1. This feature is intentionally designed as one controlled business journey.
  #   2. Herd data is resolved at runtime via Hooks.RUNTIME_HERD (DB-driven).
  #   3. Document upload uses a DataTable — the step def loops and uploads the same sample PDF for each.
  #   4. Qualification details use a reusable DataTable step shared across Individual and Company flows.
  #
  # Migrated from: TC_13_ENTS.feature (legacy 80+ scenario file)
  #   TC01/TC02/TC19–TC22  → Section 1–3  (landing page, info icons, dialog close)
  #   TC03–TC07            → Section 4    (category selections)
  #   TC23–TC24            → Section 5    (invalid combo)
  #   TC25                 → Section 6    (additional CISYF prompt)
  #   TC08–TC13            → Section 7    (Joint Herdowner member counts)
  #   TC14                 → Section 8    (Individual Cat A full submit)
  #   TC15                 → Section 9    (Individual Cat A&C full submit)
  #   TC16                 → Section 10   (Individual Cat B full submit)
  #   TC17                 → Section 11   (Individual Cat B&C full submit)
  #   TC18                 → Section 12   (Individual Cat C full submit)
  #   TC35                 → Section 13   (Company Cat A, 1 member full submit)
  #   TC36                 → Section 14   (Company Cat A, 2 members full submit)
  #   TC37                 → Section 15   (Company Cat A, 3 members full submit)
  #   TC80                 → Section 16   (Company Cat A&C, 2 members full submit)
  #   TC30                 → Section 17   (No qualification / Covid delay path)
  #   TC26                 → Section 18   (Save and Exit)
  #   TC32                 → Section 19   (Custom institution not in list)
  #   TC33                 → Section 20   (Custom qualification not in list)
  #   TC28                 → Section 21   (Invalid file format upload)
  #   TC29                 → Section 22   (Close upload popup mid-flow)
  #   TC27/TC31            → Section 23   (Post-submission + correspondence)
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

  @regression @nrcisyf @e2e
  Scenario: AT-ENTS-E2E - Agent completes the end-to-end NRCISYF regression flow

    # ===========================================
    # SECTION 1 : Herd search and landing page
    # Covers: TC01, TC02
    # ===========================================
    When the agent searches for the NRCISYF herd and opens the application
    Then the NRCISYF landing page should be displayed
    And the NRCISYF closing date should display "15 May 2026"

    # ===========================================
    # SECTION 2 : Category dialog — info icons
    # Covers: TC19, TC20, TC21
    # ===========================================
    When the agent opens the NRCISYF Apply or Edit dialog
    Then the info icon for category "A" should display the correct description
    And the info icon for category "B" should display the correct description
    And the info icon for category "C" should display the correct description

    # ===========================================
    # SECTION 3 : Dialog close behaviour
    # Covers: TC22
    # ===========================================
    When the agent closes the NRCISYF category dialog
    Then the dialog should be dismissed

    # ===========================================
    # SECTION 4 : Individual category selections
    # Covers: TC03, TC04, TC05, TC06, TC07
    # ===========================================

    # --- Category A only ---
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer) |
    Then the selected categories should be highlighted
    And the agent closes the NRCISYF category dialog

    # --- Category B only ---
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | B. National Reserve (as New Farmer) |
    Then the selected categories should be highlighted
    And the agent closes the NRCISYF category dialog

    # --- Category C only ---
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | C. Complementary income Support for Young Farmers |
    Then the selected categories should be highlighted
    And the agent closes the NRCISYF category dialog

    # --- Category A + C combined ---
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer)             |
      | C. Complementary income Support for Young Farmers |
    Then the selected categories should be highlighted
    And the agent closes the NRCISYF category dialog

    # --- Category B + C combined ---
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | B. National Reserve (as New Farmer)               |
      | C. Complementary income Support for Young Farmers |
    Then the selected categories should be highlighted
    And the agent closes the NRCISYF category dialog

    # ===========================================
    # SECTION 5 : Invalid category combination
    # Covers: TC23, TC24
    # ===========================================

    # --- A then B — mutual exclusion error ---
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer) |
      | B. National Reserve (as New Farmer)   |
    Then the invalid category combination error should be displayed for "B"

    # --- B then A — mutual exclusion error ---
    When the agent resets all category selections
    And the agent selects NRCISYF categories
      | B. National Reserve (as New Farmer)   |
      | A. National Reserve (as Young Farmer) |
    Then the invalid category combination error should be displayed for "A"
    And the agent closes the NRCISYF category dialog

    # ===========================================
    # SECTION 6 : Additional CISYF category prompt
    # Covers: TC25
    # ===========================================
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer) |
    And the agent proceeds past the category selection
    Then the agent should see the CISYF category prompt
    And the agent selects additional CISYF category if prompted
    And the agent closes the NRCISYF category dialog

    # ===========================================
    # SECTION 7 : Joint Herdowner — member count
    # Covers: TC08, TC09, TC10, TC11, TC12, TC13
    # ===========================================
    When the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer) |
    And the agent proceeds past the category selection
    And the agent skips CISYF category if prompted
    And the agent selects farming entity "Joint herdowner"
    Then the group member count dropdown should offer values
      | 1 |
      | 2 |
      | 3 |
      | 4 |
      | 5 |
      | 6 |

    # --- Navigate back for the next flow ---
    When the agent navigates back to the NRCISYF client list

    # ===========================================
    # SECTION 8 : Individual Cat A only — full submit
    # Covers: TC14
    # ===========================================
    When the agent searches for the NRCISYF herd and opens the application
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
      | dateOfCompletion   | 1                               |
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

    # ===========================================
    # SECTION 9 : Individual Cat A & C — full submit
    # Covers: TC06, TC15
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer)             |
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection

    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
      | Upload Invoice/Receipt                                             |
      | Upload Bank Statement Extract with Invoice/Receipt Passing through |
      | Qualifications certificate or Confirmation of Education Form       |
      | Personal and Sensitive Documentation                               |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully

    # ===========================================
    # SECTION 10 : Individual Cat B only — full submit
    # Covers: TC04, TC16
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | B. National Reserve (as New Farmer) |
    And the agent proceeds past the category selection

    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | Yes                                |
      | dateOfCompletion   | 1                                  |
      | certificateAwarded | Yes                                |
      | college            | Dundalk Institute of Technology    |
      | qualification      | Teagasc Diploma in Pig Production  |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
      | Qualifications certificate or Confirmation of Education Form |
      | Personal and Sensitive Documentation                         |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully

    # ===========================================
    # SECTION 11 : Individual Cat B & C — full submit
    # Covers: TC07, TC17
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | B. National Reserve (as New Farmer)               |
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection

    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
      | Upload Invoice/Receipt                                             |
      | Upload Bank Statement Extract with Invoice/Receipt Passing through |
      | Qualifications certificate or Confirmation of Education Form       |
      | Personal and Sensitive Documentation                               |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully

    # ===========================================
    # SECTION 12 : Individual Cat C only — full submit
    # Covers: TC05, TC18
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection

    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
      | Upload Invoice/Receipt                                             |
      | Upload Bank Statement Extract with Invoice/Receipt Passing through |
      | Qualifications certificate or Confirmation of Education Form       |
      | Personal and Sensitive Documentation                               |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully

    # ===========================================
    # SECTION 13 : Company Cat A — 1 member full submit
    # Covers: TC35
    # ===========================================
    When the agent navigates back to the NRCISYF client list
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
      | directorName | Mary Donald  |
    And the agent sets group member count to "1"
    And the agent enters group member details
      | memberIndex | name    | dobDay | dobMonth | dobYear | eligible |
      | 1           | Member1 | 1      | FEB      | 2003    | Yes      |
    And the agent confirms group status question as "Yes"
    And the agent proceeds to the qualification step

    When the agent completes the qualification details
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
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

    # ===========================================
    # SECTION 14 : Company Cat A — 2 members full submit
    # Covers: TC36
    # ===========================================
    When the agent navigates back to the NRCISYF client list
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
      | directorName | Mary Donald  |
    And the agent sets group member count to "2"
    And the agent enters group member details
      | memberIndex | name    | dobDay | dobMonth | dobYear | eligible |
      | 1           | Member1 | 1      | FEB      | 2003    | Yes      |
      | 2           | Member2 | 1      | FEB      | 2003    | Yes      |
    And the agent confirms group status question as "Yes"
    And the agent proceeds to the qualification step

    When the agent completes the qualification details for each member
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
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

    # ===========================================
    # SECTION 15 : Company Cat A — 3 members full submit
    # Covers: TC37
    # ===========================================
    When the agent navigates back to the NRCISYF client list
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
      | directorName | Mary Donald  |
    And the agent sets group member count to "3"
    And the agent enters group member details
      | memberIndex | name    | dobDay | dobMonth | dobYear | eligible |
      | 1           | Member1 | 1      | FEB      | 2003    | Yes      |
      | 2           | Member2 | 1      | FEB      | 2003    | Yes      |
      | 3           | Member3 | 1      | FEB      | 2003    | Yes      |
    And the agent confirms group status question as "Yes"
    And the agent proceeds to the qualification step

    When the agent completes the qualification details for each member
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
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

    # ===========================================
    # SECTION 16 : Company Cat A & C — 2 members full submit
    # Covers: TC80
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer)             |
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection

    And the agent selects farming entity "Multi-Herd Registered Farm Partnership"
    And the agent selects herd group type "Company"
    And the agent enters company details
      | croNumber    | 324556789    |
      | companyName  | Company 1    |
      | directorName | Mary Donald  |
    And the agent sets group member count to "2"
    And the agent enters group member details
      | memberIndex | name    | dobDay | dobMonth | dobYear | eligible |
      | 1           | Member1 | 1      | FEB      | 2003    | Yes      |
      | 2           | Member2 | 1      | FEB      | 2003    | Yes      |
    And the agent confirms group status question as "Yes"
    And the agent proceeds to the qualification step

    When the agent completes the qualification details for each member
      | hasQualification   | Yes                             |
      | dateOfCompletion   | 1                               |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |

    And the agent proceeds to the summary step
    When the agent uploads NRCISYF documents
      | Eligible Farmer(s) Birth or Marriage Certificate documentation                                                                                                          |
      | Declaration of Effective Control and Decision Making Power (download                                                                                                    |
      | Upload Invoice/Receipt                                                                                                                                                  |
      | Upload Bank Statement Extract with Invoice/Receipt Passing through                                                                                                      |
      | Please upload the relevant Agricultural Qualification at Level 6 or equivalent on the National Framework of Qualifications certificate or Confirmation of Education Form |
      | Qualifications certificate or Confirmation of Education Form                                                                                                            |
      | Personal and Sensitive Documentation                                                                                                                                    |

    And the agent saves and proceeds to the declaration step
    And the agent submits the NRCISYF application with declaration
    Then the NRCISYF application should be submitted successfully

    # ===========================================
    # SECTION 17 : No qualification path
    # Covers: TC30
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | A. National Reserve (as Young Farmer)             |
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection

    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | No |
    Then the no-qualification information message should be displayed if applicable

    # --- Navigate back to reset for next flow ---
    When the agent navigates back to the NRCISYF client list

    # ===========================================
    # SECTION 18 : Save and Exit mid-application
    # Covers: TC26
    # ===========================================
    When the agent searches for the NRCISYF herd and opens the application
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
      | dateOfCompletion   | 1                               |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |
    And the agent saves and exits the NRCISYF application
    Then the application should be saved successfully

    # ===========================================
    # SECTION 19 : Custom education institution not in list
    # Covers: TC32
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection
    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification   | Yes                                                                |
      | dateOfCompletion   | 1                                                                  |
      | certificateAwarded | Yes                                                                |
      | college            | The educational institution I attended does not appear in this list |
      | customCollege      | Dublin Institute of Technology                                     |
      | qualification      | FETAC Certificate in Farming                                       |
    And the agent proceeds to the summary step
    Then the education institution should display "Dublin Institute of Technology"

    # ===========================================
    # SECTION 20 : Custom qualification not in list
    # Covers: TC33
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent opens the NRCISYF Apply or Edit dialog
    And the agent resets all category selections
    And the agent selects NRCISYF categories
      | C. Complementary income Support for Young Farmers |
    And the agent proceeds past the category selection
    And the agent selects farming entity "Individual"
    And the agent proceeds to the qualification step
    When the agent completes the qualification details
      | hasQualification      | Yes                                                                       |
      | dateOfCompletion      | 1                                                                         |
      | certificateAwarded    | Yes                                                                       |
      | college               | Athlone Institute of Technology                                           |
      | qualification         | The qualification I have achieved does not appear in this list             |
      | customQualification   | Advanced Certificate in Sustainable Agriculture                           |
    And the agent proceeds to the summary step
    Then the qualification should display "Advanced Certificate in Sustainable Agriculture"

    # ===========================================
    # SECTION 21 : Invalid file format upload error
    # Covers: TC28
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    Then the agent verifies the upload control only accepts PDF format

    # ===========================================
    # SECTION 22 : Close upload popup mid-flow
    # Covers: TC29
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
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
      | dateOfCompletion   | 1                               |
      | certificateAwarded | Yes                             |
      | college            | Athlone Institute of Technology |
      | qualification      | FETAC Certificate in Farming    |
    And the agent proceeds to the summary step
    When the agent opens the upload dialog for "Qualifications certificate or Confirmation of Education Form"
    And the agent closes the upload dialog without uploading
    Then the upload dialog should be dismissed

    # ===========================================
    # SECTION 23 : Post-submission verification
    # Covers: TC27, TC31
    # ===========================================
    When the agent navigates back to the NRCISYF client list
    And the agent searches for the NRCISYF herd and opens the application
    And the agent views the submitted NRCISYF application
    Then the uploaded documents should be accessible in correspondence
    And the agent clicks on the "Education Documentation" document link
    Then the document should open or download successfully
