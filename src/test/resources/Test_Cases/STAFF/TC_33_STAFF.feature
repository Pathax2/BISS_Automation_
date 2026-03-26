Feature: Regression Pack for Openshift Amend Application Cases

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Then Get 5 Herds with Submission and SIM Opted In from DataBase

  @tmslink=BISSAGL-18551
  @sanity
  Scenario: AT-TC-01 - BISS_25.1.Sprint_4 TC1 for BISSAGL-12971 Amend Application Regression test (Sim Amend)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission and SIM opted in from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    When Check if Admin Inspection is Completed after clicking On the "application-search-table" table "Amend" link number 1
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button


#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Agent Click on the "Cancel" dialog box button
#    Then Staff Click on "Home" Link in Navbar
#    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
#    Then Staff Select "schemeYear" dropdown value as "2024"
#    Then Staff Click on the "Search" button
#    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Agent Click on the "Cancel" dialog box button
#    Then Staff Click on "Home" Link in Navbar
#    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
#    Then Staff Select "schemeYear" dropdown value as "2023"
#    Then Staff Click on the "Search" button
#    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Agent Click on the "Cancel" dialog box button

  @tmslink=BISSAGL-18553
  @regression
  Scenario: AT-TC-02 - BISS_25.1.Sprint_3 OpenShift BISS Internal Regression Application Summary Force Majeure Save
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission and SIM opted in from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    Then Staff Click on "BISS Application" Link in Navbar
    Then Staff Check if Force Majeure Selected and Click
#    Then Staff Click on the "Force-Majeure-input" CheckBox
    Then Staff Click on "Payments" Link in Navbar

#    Then Staff Click on the "Next" button
#    Then Staff Click on the "Submit Changes" button



#    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Staff Type Text as "Test Save" in "addNoteText" Query Letters TextArea
#    And Agent Click on the "Save" dialog box button
#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Staff Type Text as "Test UnSave" in "addNoteText" Query Letters TextArea
#    And Agent Click on the "Save" dialog box button
#    Then Staff Click on "Home" Link in Navbar
#    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
#    Then Staff Select "schemeYear" dropdown value as "2023"
#    Then Staff Click on the "Search" button
#    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Staff Type Text as "Test Save" in "addNoteText" Query Letters TextArea
#    And Agent Click on the "Save" dialog box button
#    Then Staff Click on the "Force-Majeure-input" CheckBox
#    And Staff Type Text as "Test UnSave" in "addNoteText" Query Letters TextArea
#    And Agent Click on the "Save" dialog box button

