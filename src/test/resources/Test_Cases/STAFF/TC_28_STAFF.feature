Feature: Regression Pack for Openshift Cases

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Then Get 5 Herds with Submission from DataBase
    Then Get 5 Reference Numbers from Database
    Then Get 5 LPIS Parcels from Database

  @regression
  Scenario: AT-TC-01 - BISSAGL-12669 | Switch Tabs on Dashboard Side Menu
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Click on "Coordination" Link in Navbar
#    Then Staff Click on "Payment QA" Link in Navbar
    Then Staff Click on "Home" Link in Navbar

  @regression
  Scenario: AT-TC-02 - BISSAGL-12669 | Search with Reference Number
#    Then Get 5 Reference Numbers from Database
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Reference Number from row 1 in the "referenceNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button

#    And Staff Click On the "application-search-table" table "View" link
#    Then Staff Click on "BISS Amendments" Link in Navbar
#    Then Staff Click on "Payments" Link in Navbar
#    Then Staff Click on "Correspondence" Link in Navbar
#    Then Staff Click on "Errors/Stops" Link in Navbar
#    Then Staff Click on "Inspections" Link in Navbar
#    Then Staff Click on "Notes/History" Link in Navbar
  #Then Staff Enter Value as "P1751946" in the "herdNumber" field


  @regression
  Scenario: AT-TC-03 - BISSAGL-12669 | Search with Herd Number
#    Then Get 5 Herds with Submission from DataBase
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button

  @regression
  Scenario: AT-TC-04 - BISSAGL-12669 | Parcel Search
#    Then Get 5 LPIS Parcels from Database
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Parcels" tab
    Then Staff Enter Value as LPIS Parcel Number from row 1 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button

  @regression
  Scenario: AT-TC-05 - BISSAGL-12669 | INT : Generating Maps by Parcel Number Search
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Enter Value as LPIS Parcel Number from row 2 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Inspection Discovery"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button

  @regression
  Scenario: AT-TC-06 - BISSAGL-12669 | INT : Generating and Downloading Maps Herd Number Search
    When Login with the Username "agr2332"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    Then Staff Enter Value as Herd Number with Submission from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff print "name" of the herd
    And Staff print "address" of the herd
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A4"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button
    And Staff Check the "status" of Generated Map
    And Staff Check the "id" of Generated Map
    And Staff Check the "herdNumber" of Generated Map
    And Staff Check the "date" of Generated Map
    And Staff Check the final status of Generated Map
    Then Staff Click On "Next page" Pagination Button
    Then Staff Click On "Previous page" Pagination Button

  @regression
  Scenario: AT-TC-07 - BISSAGL-12669 | Online Response Activities
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Online Response" tab
    Then Staff Enter Value as Herd Number with Submission from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
#    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Click on the "Search" button

  @regression
  Scenario: AT-TC-08 - BISSAGL-12669 | Internal Amendments
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 4 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    When Check if Admin Inspection is Completed after clicking On the "application-search-table" table "Amend" link number 1
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button


  @regression
  Scenario: AT-TC-09 - BISSAGL-12669 | Switch Tabs in Herd
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 5 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on "Active Farmer" Link in Navbar
    Then Staff Click on "Payments" Link in Navbar
    Then Staff Click on "Correspondence" Link in Navbar
    Then Staff Click on "Errors/Stops" Link in Navbar
    Then Staff Click on "Inspections" Link in Navbar
    Then Staff Click on "Notes/History" Link in Navbar
    Then Staff Click on "BISS Application" Link in Navbar
    Then Staff Click on "SIM" Link in Navbar
#    Then Staff Click on "Coordination Amendments" Link in Navbar

  @regression
  Scenario: AT-TC-10 - BISSAGL-12669 | Active Farmer and start Admin Check
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Active Farmer" Link in Navbar
    Then Staff Click on the "Start admin check" button
