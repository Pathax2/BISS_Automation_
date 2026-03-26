Feature: Regression Pack for Openshift Notes and History Cases

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Then Get 15 Herds with Submission from DataBase
    Then Get 5 Reference Numbers from Database
    Then Get 5 LPIS Parcels from Database

  @tmslink=BISSAGL-18477
  @regression
  Scenario: AT-TC-01 - BISS_25.1.Sprint_3 TC1 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar
    Then Staff Click on the "Add Note" button

  @tmslink=BISSAGL-18479
  @regression
  Scenario: AT-TC-02 - BISS_25.1.Sprint_3 TC2 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar

  @tmslink=BISSAGL-18481
  @regression
  Scenario: AT-TC-03 - BISS_25.1.Sprint_3 TC3 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar

  @tmslink=BISSAGL-18483
  @regression
  Scenario: AT-TC-04 - BISS_25.1.Sprint_3 TC4 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar

  @tmslink=BISSAGL-18486
  @regression
  Scenario: AT-TC-05 - BISS_25.1.Sprint_3 TC5 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar

  @tmslink=BISSAGL-18492
  @regression
  Scenario: AT-TC-06 - BISS_25.1.Sprint_3 TC6 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar

  @tmslink=BISSAGL-18495
  @regression
  Scenario: AT-TC-07 - BISS_25.1.Sprint_3 TC7 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar

  @tmslink=BISSAGL-18498
  @regression
  Scenario: AT-TC-08 - BISS_25.1.Sprint_3 TC8 for BISSAGL-12977 Notes History screen Regression test
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
    Then Staff Click on "Notes/History" Link in Navbar
