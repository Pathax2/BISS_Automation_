Feature: Regression Pack for Openshift Map Printing Regression Cases

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Then Get 5 Herds with Submission from DataBase
    Then Get 5 Reference Numbers from Database
    Then Get 5 LPIS Parcels from Database

  @tmslink=BISSAGL-18579
  @regression
  Scenario: AT-TC-01 - BISS_25.1.Sprint_4 TC1 for BISSAGL-12728 Openshift Map Printing Regression test
    When Login with the Username "agr11612"
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
    And Staff Click On the "mdc-data-table" table "Ready to download" link number 1

  @tmslink=BISSAGL-18583
  @regression
  Scenario: AT-TC-02 - BISS_25.1.Sprint_4 TC2 for BISSAGL-12728 Openshift Map Printing Regression test
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Enter Value as LPIS Parcel Number from row 3 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Inspection Discovery"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button
    And Staff Check the final status of Generated Map
    And Staff Click On the "mdc-data-table" table "Ready to download" link number 1

  @tmslink=BISSAGL-18586
  @regression
  Scenario: AT-TC-03 - BISS_25.1.Sprint_4 TC3 for BISSAGL-12728 Openshift Map Printing Regression test
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    Then Staff Enter Value as "ABDAAPT" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18589
  @regression
  Scenario: AT-TC-04 - BISS_25.1.Sprint_4 TC4 for BISSAGL-12728 Openshift Map Printing Regression test
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Enter Value as "ABDAAPT" in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Check "Parcel number is invalid" Error Message

  @tmslink=BISSAGL-18591
  @regression
  Scenario: AT-TC-05 - BISS_25.1.Sprint_4 TC5 for BISSAGL-12728 Openshift Map Printing Regression test
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    Then Staff Click on the "Search" button
    And Staff Check "Herd number required." Error Message
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Click on the "Search" button
    And Staff Check "Parcel number required." Error Message
