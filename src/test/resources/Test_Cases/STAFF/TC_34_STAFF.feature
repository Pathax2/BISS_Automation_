Feature: Regression Pack for Openshift Active Farmer screen Regression test - Approve & Reject

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
  Then Get 5 Herds with Admin Check Not Started from Database

  @tmslink=BISSAGL-18613
  @regression
  Scenario: AT-TC-01 - BISS_25.1.Sprint_4 TC1 for BISSAGL-12972 Openshift Active Farmer screen Regression test (Reject and appeal)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Admin Check Not Started from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Active Farmer" Link in Navbar
    Then Staff Click on the "Start admin check" button
    And Agent Click on the "Send" dialog box button

  @tmslink=BISSAGL-18616
  @regression
  Scenario: AT-TC-02 - BISS_25.1.Sprint_4 TC2 for BISSAGL-12972 Openshift Active Farmer screen Regression test (approved)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Admin Check Not Started from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Active Farmer" Link in Navbar
    Then Staff Click on the "Start admin check" button
    And Agent Click on the "Send" dialog box button



