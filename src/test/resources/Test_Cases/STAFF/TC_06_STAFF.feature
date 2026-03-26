
Feature: Regression Suite for BISS Letters

  @regression
  Scenario: AT-TC-00 - Queries required for BISSAGL-301
    Given user on staff login page
    Then Get 5 Herds with Admin Check Not Started from Database

  @tmslink=BISSAGL-14914
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 for BISSAGL-7301 2023 BISS Letters issued in the name of the previous herd owner
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Reference Number from row 2 in the "referenceNumber" field
    #Then Staff Enter Value as "23C4304B" in the "referenceNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff zoom out of screen thrice
    Then Staff Click on "Coordination Amendments" Link in Navbar
    Then Staff Enter Value as "U1222127" in the "newHerdNo" field
    Then Staff Click on the "Start" button
    Then Staff Click on the "Move all to new herd" button
    Then Staff Click on the "Save" button

  @tmslink=BISSAGL-14913
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_5 TC1 for BISSAGL-7301 2023 BISS Letters issued in the name of the previous herd owner
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Reference Number from row 2 in the "referenceNumber" field
    #Then Staff Enter Value as "23C4304B" in the "referenceNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff zoom out of screen thrice
    Then Staff Click on "Coordination Amendments" Link in Navbar
    Then Staff Enter Value as "U1222127" in the "newHerdNo" field
    Then Staff Click on the "Start" button
    Then Staff Click on the "Move all to new herd" button
    Then Staff Click on the "Save" button




