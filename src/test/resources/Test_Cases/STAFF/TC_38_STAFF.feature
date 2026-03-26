Feature: Regression Pack for Prevent_SIM_Indicator_Selected_when_Crop_not_SIM

  @tmslink=BISSAGL-14676
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_2 TC1 for BISSAGL-7472 | SIM. Prevent SIM indicator selected when crop is not SIM
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "L1161200" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    # N1100559
    Then Staff Click on "BISS Amendments" Link in Navbar
    And Staff Click on "Eco" Scheme Selection Card

  @tmslink=BISSAGL-14675
    @regression
    Scenario: AT-TC-02 - BISS_24.2.Sprint_1 TC1 for BISSAGL-8126 INT:- Select/Unselect the ECO scheme on the BISS Internal Amendments, which is not reflecting on the scheme panel summary screen for 2023.
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "L1161200" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "BISS Amendments" Link in Navbar
    And Staff Click on "Eco" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button
    Then Staff Click on "Home" Link in Navbar
    Then Staff Enter Value as "A1140364" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "BISS Amendments" Link in Navbar
    And Staff Click on "Eco" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button

      # Just put in validation cases
