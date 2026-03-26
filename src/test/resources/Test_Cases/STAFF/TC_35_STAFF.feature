Feature: Regression Pack for Select/Unselect ECO Scheme on the BISS Internal Amendments

  # NeedHErd to Select and Deselect
  @tmslink=BISSAGL-14762
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC2 for BISSAGL-8126 INT:- Select/Unselect the ECO scheme on the BISS Internal Amendments, which is not reflecting on the scheme panel summary screen for 2023.
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

  @tmslink=BISSAGL-14763
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
