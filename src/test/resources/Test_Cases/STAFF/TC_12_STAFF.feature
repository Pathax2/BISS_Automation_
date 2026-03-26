Feature: Regression Pack for Maps in Correspondence

  Background:
    Given user on staff login page
    Then Get 15 LPIS Parcels from Database

  #@tmslink=BISSAGL-14866
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-3161 | INT : Generating Maps for Herds in Partnership - Herd Number
    Given user on staff login page
    When Login with the Username "agr2332"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "O1020191" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
#    And Staff Scroll to Documents Section
    # Expected Link not found
    # Clicking on Links throws issues



  #@tmslink=BISSAGL-14873
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL-3162 | INT : Save Generated Maps on the Correspondence screen- View map from Correspondence
    Given user on staff login page
    When Login with the Username "agr2332"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
    Then Staff Enter Value as LPIS Parcel Number from row 11 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    #Then Staff Enter Value as "O1020191" in the "herdNumber" field
    #Then Staff Select "schemeYear" dropdown value as "2023"
    #Then Staff Click on the "Search" button
    #And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on "Correspondence" Quick Link in Navbar



