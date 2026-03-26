Feature: Regression Pack for Openshift View Application Cases

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Then Get 15 Herds with Submission from DataBase
    Then Get 5 Reference Numbers from Database
    Then Get 5 LPIS Parcels from Database

  @tmslink=BISSAGL-12927
  @sanity
  Scenario: AT-TC-01 - BISS_25.1.Sprint_3 OpenShift Regression BISS Internal Application Summary Force Majeure Cancel
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Agent Click on the "Cancel" dialog box button
    Then Staff Click on "Home" Link in Navbar
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Agent Click on the "Cancel" dialog box button
    Then Staff Click on "Home" Link in Navbar
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Agent Click on the "Cancel" dialog box button

  @tmslink=BISSAGL-12928
  @regression
  Scenario: AT-TC-02 - BISS_25.1.Sprint_3 OpenShift BISS Internal Regression Application Summary Force Majeure Save
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Staff Type Text as "Test Save" in "addNoteText" Query Letters TextArea
    And Agent Click on the "Save" dialog box button
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Staff Type Text as "Test UnSave" in "addNoteText" Query Letters TextArea
    And Agent Click on the "Save" dialog box button
    Then Staff Click on "Home" Link in Navbar
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Staff Type Text as "Test Save" in "addNoteText" Query Letters TextArea
    And Agent Click on the "Save" dialog box button
    Then Staff Click on the "Force-Majeure-input" CheckBox
    And Staff Type Text as "Test UnSave" in "addNoteText" Query Letters TextArea
    And Agent Click on the "Save" dialog box button

  @tmslink=BISSAGL-12929
  @regression
  Scenario: AT-TC-03 - BISS_25.1.Sprint_3 OpenShift Regression BISS Internal Dialog integration - Application Summary
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    And Staff Click On the "mdc-data-table" table "View details" link number 2
    And Agent Click on the "Close" dialog box button

  @tmslink=BISSAGL-12930
  @regression
  Scenario: AT-TC-04 - BISS_25.1.Sprint_3 OpenShift Regression Dialog integration - Application Summary Land Details panel
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 7 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Hover over the "Determined area with compensation" info icon

  @tmslink=BISSAGL-13008
  @regression
  Scenario: AT-TC-05 - BISS_25.1.Sprint_3 OpenShift Regression BISS Internal View Active Farmer screen
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-13009
  @regression
  Scenario: AT-TC-06 - BISS_25.1.Sprint_3 OpenShift Regression BISS Internal View Land details screen
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application

  @tmslink=BISSAGL-13010
  @regression
  Scenario: AT-TC-07 - BISS_25.1.Sprint_3 OpenShift Regression BISS Internal View GAEC8 screen
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application

  @tmslink=BISSAGL-13011
  @regression
  Scenario: AT-TC-08 - BISS_25.1.Sprint_4 OpenShift Regression BISS Internal View Digitisation Task screen
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
