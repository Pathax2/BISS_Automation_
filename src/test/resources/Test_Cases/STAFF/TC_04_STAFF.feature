
Feature: Regression Suite for SIM Amendments - History Tab

  Background:
    Given user on staff login page
    Then Get 1 Herds with Submission from DataBase


  @tmslink=BISSAGL-14998
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 for BISSAGL-5444 INT: SIM amendments - History tab
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "G175C038" in the "herdNumber" field
#    Then Staff Enter Value as Herd Number with Submission from row 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff zoom out of screen thrice
    Then Staff Click on "SIM" Link in Navbar
    Then Staff Click on "G17202130" SIM parcel to open side drawer
    And Staff Enter value as "4" in the "simFoundArea" SIM side drawer field
    Then Staff Click on the "Save changes" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "SIM" Link in Navbar
    Then Staff Click on "G22814025" SIM parcel to open side drawer
    And Staff Enter value as "6" in the "amendedClaimedArea" SIM side drawer field
    Then Staff Click on the "Save changes" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "SIM" Link in Navbar
    Then Staff Click on "G22813057" SIM parcel to open side drawer
    Then Staff Click On the "simIndicator" Checkbox in the SIM Parcel Edit Drawer
    Then Staff Click on the "Save changes" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "SIM" Link in Navbar
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Test 7678"
    Then Staff Click on the "Finish" button
    And Staff Toggle Sim option
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "SIM" Link in Navbar
    Then Staff Click on the "Revert" button
    Then Staff Click on "G22813057" SIM parcel to open side drawer
    Then Staff Click On the "simIndicator" Checkbox in the SIM Parcel Edit Drawer
    Then Staff Click on the "Save changes" button
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Set for Test 7678"
    Then Staff Click on the "Finish" button








