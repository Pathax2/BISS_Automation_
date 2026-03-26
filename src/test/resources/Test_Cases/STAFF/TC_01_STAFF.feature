Feature: Regression Suite for Map Printing with Herd

  Background:
    Given user on staff login page
    Then Get 4 Herds from DB
    #and print value of row number 1

  @tmslink=BISSAGL-14969
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 FOR BISSAGL-3158 INT : Generating maps by Herd Number Search
#    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    #N106036X
    Then Staff Enter Value as Herd Number 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A4"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button

  @tmslink=BISSAGL-14968
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_5 TC2 FOR BISSAGL-3158 INT : Generating maps by Herd Number Search
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    Then Staff Click on the "Search" button
    And Staff Check "Herd number required." Error Message

#  #@tmslink=BISSAGL-7752
#  # Depleted Case
#  @regression
#  Scenario: AT-TC-03 - BISS_24.1.Sprint_5 TC3 FOR BISSAGL-3158 INT : Generating maps by Herd Number Search
#    Given user on staff login page
#    When Login with the Username "agr11612"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    And Staff switch to the "Map Printing" tab
#    Then Staff Click on the "Search" button
#    And Staff Check "Scheme year required." Error Message

  @tmslink=BISSAGL-14966
  @regression
  Scenario: AT-TC-04 - BISS_24.1.Sprint_5 TC4 FOR BISSAGL-3158 INT : Generating maps by Herd Number Search
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    #N106036X
    Then Staff Enter Value as Herd Number 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Select All the "parcelSelect" Multi Option Dropdown Values
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A4"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button

