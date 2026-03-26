Feature: Regression Suite for Map Printing with Parcel

  Background:
    Given user on staff login page
   Then Get 14 LPIS Parcels from Database

  @tmslink=BISSAGL-14997
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 for BISSAGL-3159 | INT : Generating Maps by Parcel Number Search
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "A1120600028" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 1 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Inspection Discovery"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button

  @tmslink=BISSAGL-14996
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_5 TC2 for BISSAGL-3159 | Maps Custom map layouts
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "A1120600028" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 3 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Custom Layer A4"
    Then Staff Select "imagerySelect" dropdown value as "2009 Low Flown Ortho"
    Then Staff Select "rasterSelect" dropdown value as "Raster 1:5000"
    And Staff Change the "vectorSelect" Multi Option Dropdown Value
    Then Staff Click on the "Generate map" button

  #@tmslink=BISSAGL-7690
  # Depleted Case
#  @regression
#  Scenario: AT-TC-03 - BISS_24.1.Sprint_5 TC3 for BISSAGL-3159 | Maps Error Handling:
#    Given user on staff login page
#    When Login with the Username "agr11612"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    And Staff switch to the "Map Printing" tab
#    Then Staff Click on the "Search" button
#    And Staff Check "Scheme year required." Error Message

  @tmslink=BISSAGL-14993
  @regression
  Scenario: AT-TC-04 - BISS_24.1.Sprint_5 TC4 for BISSAGL-3159 | Maps Single Parcel Generation
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "O1060300020" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 4 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A4"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button

  @tmslink=BISSAGL-14988
  @regression
  Scenario: AT-TC-05 - BISS_24.1.Sprint_5 TC5 for BISSAGL-3159 | Maps Error - parcel number required
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Click on the "Search" button
    And Staff Check "Parcel number required." Error Message

  #@tmslink=BISSAGL-7707
  # Depleted Case
#  @regression
#  Scenario: AT-TC-06 - BISS_24.1.Sprint_4 TC6 for BISSAGL-3159 | Maps Error - scheme year required
#    Given user on staff login page
#    When Login with the Username "agr11612"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    And Staff switch to the "Map Printing" tab
#    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Click on the "Search" button
#    And Staff Check "Scheme year required." Error Message

  @tmslink=BISSAGL-14986
  @regression
  Scenario: AT-TC-07 - BISS_24.1.Sprint_3 TC7 for BISSAGL-3159 | Maps Multiple Parcel generation
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "B1042500038" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 5 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Select All the "parcelSelect" Multi Option Dropdown Values
    Then Staff Select "mapTypeSelect" dropdown value as "Full Color Ortho A3"
    And Staff Click On Legend Indicator CheckBox
    Then Staff Click on the "Generate map" button

  @tmslink=BISSAGL-14983
  @regression
  Scenario: AT-TC-08 - BISS_24.1.Sprint_5 TC8 for BISSAGL-3159 | Custom Map generation
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "Q1644100004" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 6 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Select All the "parcelSelect" Multi Option Dropdown Values
    Then Staff Select "mapTypeSelect" dropdown value as "Custom Layer A4"
    Then Staff Select "imagerySelect" dropdown value as "2008 Low Flown Ortho"
    Then Staff Select "rasterSelect" dropdown value as "Raster 1:5000"
    And Staff Change the "vectorSelect" Multi Option Dropdown Value
    Then Staff Click on the "Generate map" button

  @tmslink=BISSAGL-14982
  @regression
    # Fix case by next week
  Scenario: AT-TC-09 - BISS_24.1.Sprint_5 TC9 for BISSAGL-3159 | Maps Only LPIS Parcels
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "G2182100056" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 6 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
#    And Staff Check No Results Warning Message

  @tmslink=BISSAGL-14981
  @regression
  Scenario: AT-TC-10 - BISS_24.1.Sprint_4 TC10 for BISSAGL-3159 | Maps Parcel not selected
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "K1360100094" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 7 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Open and Close the "parcelSelect" Multi Option Dropdown Value
    And Staff Check "select a parcel required" Error Message

  @tmslink=BISSAGL-14980
  @regression
  Scenario: AT-TC-11 - BISS_24.1.Sprint_4 TC11 for BISSAGL-3159 | Map Type not selected
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "K1360100094" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 8 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    And Staff Open and Close the "mapTypeSelect" Multi Option Dropdown Value
    And Staff Check "select a map type required" Error Message

  @tmslink=BISSAGL-14979
  @regression
  Scenario: AT-TC-12 - BISS_24.1.Sprint_4 TC12 for BISSAGL-3159 | Map Image Layer not selected
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "K1360100094" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 9 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Custom Layer A4"
    And Staff Open and Close the "imagerySelect" Multi Option Dropdown Value
    And Staff Check "select an imagery layer required" Error Message

  @tmslink=BISSAGL-14978
  @regression
  Scenario: AT-TC-13 - BISS_24.1.Sprint_4 TC13 for BISSAGL-3159 | Map Vector Layer not selected
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "K1360100094" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 10 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Change the "parcelSelect" Multi Option Dropdown Value
    Then Staff Select "mapTypeSelect" dropdown value as "Custom Layer A4"
    Then Staff Select "imagerySelect" dropdown value as "2009 Low Flown Ortho"
    And Staff Open and Close the "vectorSelect" Multi Option Dropdown Value
    And Staff Check "select a vector layer required" Error Message

  @tmslink=BISSAGL-14977
  @regression
  Scenario: AT-TC-14 - BISS_24.1.Sprint_5 TC14 for BISSAGL-3159 | Map non-internal user
    Given user on staff login page
    When Login with the Username "agr5033"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Check "Map Printing" Tab Visibility


