Feature: Regression Pack for Generating Map

  @tmslink=BISSAGL-14866
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-3161 | INT : Generating Maps for Herds in Partnership - Herd Number
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    Then Staff Enter Value as "K1361753" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Select All the "parcelSelect" Multi Option Dropdown Values
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A3"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button
    And Staff Check the "status" of Generated Map
    And Staff Check the "id" of Generated Map
    And Staff Check the "herdNumber" of Generated Map
    And Staff Check the "date" of Generated Map
    And Staff Check the final status of Generated Map
    Then Staff Click On First Ready to Download Link

  @tmslink=BISSAGL-14865
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL-3161 | INT : Generating Maps for Herds in Partnership - Multi Herd Parcel Generation
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Enter Value as "K1360100094" in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Select All the "parcelSelect" Multi Option Dropdown Values
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A3"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button
    And Staff Check the "status" of Generated Map
    And Staff Check the "id" of Generated Map
    And Staff Check the "herdNumber" of Generated Map
    And Staff Check the "date" of Generated Map
    And Staff Check the final status of Generated Map
    Then Staff Click On First Ready to Download Link




