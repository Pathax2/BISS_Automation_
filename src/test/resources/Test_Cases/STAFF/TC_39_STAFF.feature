Feature: Regression Pack for AMS internal - Rules for display of AMS status on internal screen

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Given user on staff login page
    Then Get 10 Herds with Submission and SIM Opted In from DataBase

  @tmslink=BISSAGL-14749
  @regression
  Scenario: BISS_24.2.Sprint_1 TC1 for BISSAGL-7540 AMS internal - Rules for display of AMS status on internal screen
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission and SIM opted in from row 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    # N1100559
    Then Staff Click on "Inspections" Link in Navbar
    And Staff CLick on Parcel No Header in AMS Screen
    And Staff CLick on Parcel No Header in AMS Screen


