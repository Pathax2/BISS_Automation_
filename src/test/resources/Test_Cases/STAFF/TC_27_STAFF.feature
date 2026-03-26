Feature: Regression Pack for iNet Prelim Checks - Agent Dashboard

  @tmslink=BISSAGL-14727
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_2 TC1 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify Preliminary Checks page
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14603
  @regression
  Scenario: AT-TC-02 - BISS_24.2.Sprint_3 TC2 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify Preliminary Checks page when user does an unsuccessful search
    Given user on staff login page
    Then Get 10 Herds from DB

  @tmslink=BISSAGL-14602
  @regression
  Scenario: AT-TC-03 - BISS_24.2.Sprint_3 TC3 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify Preliminary Checks page, Responded tab is updated with count on quick filter
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14600
  @regression
  Scenario: AT-TC-04 - BISS_24.2.Sprint_3 TC5 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To Verify Preliminary Checks screen when user toggle between the quick filters is results are displaying accordingly
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14601
  @regression
  Scenario: AT-TC-05 - BISS_24.2.Sprint_3 TC4 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify Preliminary Checks screen with no results
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14544
  @regression
  Scenario: AT-TC-06 - BISS_24.2.Sprint_3 TC7 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify quick filters are working as normal while searching
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14545
  @regression
  Scenario: AT-TC-07 - BISS_24.2.Sprint_3 TC6 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify no duplicate Preliminary Checks are displayed on PC page
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14518
  @regression
  Scenario: AT-TC-08 - BISS_24.2.Sprint_3 TC 9 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify 'BISS Applications' text on top of search field is removed on Preliminary Checks page
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  @tmslink=BISSAGL-14519
  @regression
  Scenario: AT-TC-09 - BISS_24.2.Sprint_3 TC 8 for BISSAGL-7898 | iNet24 - PrelimChecks: Agent Dashboard - To verify Preliminary Checks page is loaded back with results when search data is cleared
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  #@tmslink=BISSAGL-8365
  @regression
  Scenario: AT-TC-10 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7482 AMS internal - Filter and search options for screen
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature

  #@tmslink=BISSAGL-8365
  @regression
  Scenario: AT-TC-11 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7482 AMS internal - Filter and search options for screen
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1751946" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    # Change the multi-select Dropdown to suit the dropdowns so value can be passed from Feature
