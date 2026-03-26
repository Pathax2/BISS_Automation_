Feature: Regression Pack for AMS Internal Screen - Filter and Search Option

  @tmslink=BISSAGL-14725
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7482 AMS internal - Filter and search options for screen
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "IFP10100" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

##@tmslink=BISSAGL-8365
#  @regression
#  Scenario: AT-TC-02 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7482 AMS internal - Filter and search options for screen
#    Given user on staff login page
#    Then Get 10 Herds from DB
