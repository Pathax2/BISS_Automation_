Feature: Regression Pack for Map Printing Case Sensitive

  Background:
    Given user on staff login page
    Then Get 15 LPIS Parcels from Database

  @tmslink=BISSAGL-14833
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-8080 | Map Printing: Parcel number search should not be case sensitive - Parcel Number Search with lower case
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "o1170800024" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 11 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible

  @tmslink=BISSAGL-14832
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL-8080 | Map Printing: Parcel number search should not be case sensitive - Parcel Number Search with Upper case
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "O1170800024" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 12 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible

  @tmslink=BISSAGL-14831
  @regression
  Scenario: AT-TC-03 - BISS_24.1.Sprint_6 TC3 for BISSAGL-8080 | Map Printing: Parcel number search should not be case sensitive - Parcel Number Search with special characters
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "O1060300013*" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 13 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible

  @tmslink=BISSAGL-14824
  @regression
  Scenario: AT-TC-04 - BISS_24.1.Sprint_6 TC4 for BISSAGL-8080 | Map Printing: Parcel number search should not be case sensitive - Invalid Parcel Search
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as "4354543648894" in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 14 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible
    Then Staff Enter Value as "hdgfhggdfhjkdhj" in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible
    Then Staff Enter Value as "345dhf564gh48" in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible

  @tmslink=BISSAGL-14823
  @regression
  Scenario: AT-TC-05 - BISS_24.1.Sprint_6 TC5 for BISSAGL-8080 | Map Printing: Parcel number search should not be case sensitive - Null Value Parcel Search
    Given user on staff login page
    When Login with the Username "agr11612"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Map Printing" tab
    And Staff Select "Parcel" as Map Printing Type
#    Then Staff Enter Value as " " in the "parcelNumber" field
    Then Staff Enter Value as LPIS Parcel Number from row 15 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    Then Staff check if Generate Map Setting Section visible






