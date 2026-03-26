
Feature: Regression Suite for Checking Map Generated for HerdNumber
  Background:
    Given user on staff login page
    Then Get 4 Herds from DB

  @tmslink=BISSAGL-14962
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 for BISSAGL-3160 | INT : Generating and Downloading Maps - request map part only
    Given user on staff login page
    When Login with the Username "agr2332"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
#    Then Staff Enter Value as "B1021694" in the "herdNumber" field
    #B1021694
    Then Staff Enter Value as Herd Number 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
#    And Staff check if "name" of herd is "Edward O'Shea"
    And Staff print "name" of the herd
#    And Staff check if "address" of herd is "Curkish Road Killiney Co Cork V91 5ADC Cork"
    And Staff print "address" of the herd
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A4"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button
    And Staff Check the "status" of Generated Map
    And Staff Check the "id" of Generated Map
    And Staff Check the "herdNumber" of Generated Map
    And Staff Check the "date" of Generated Map
    And Staff Check the "status" of Generated Map


  @tmslink=BISSAGL-14960
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_5 TC2 for BISSAGL-3160 | INT : Generating and Downloading Maps - request map part only
    Given user on staff login page
    When Login with the Username "agr2332"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
#    Then Staff Enter Value as "B1021694" in the "herdNumber" field
    #B1021694
    Then Staff Enter Value as Herd Number 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    #    And Staff check if "name" of herd is "Edward O'Shea"
    And Staff print "name" of the herd
#    And Staff check if "address" of herd is "Curkish Road Killiney Co Cork V91 5ADC Cork"
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


