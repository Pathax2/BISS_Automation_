Feature: Regression Pack for Scheme Selection - Add MSSM, RCSM

  @tmslink=BISSAGL-14893
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-7582 INT 2024: Scheme Selection - Add MSSM and RCSM
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    # A111401X
    Then Staff Enter Value as "J1400098" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    Then Staff Check if card has title as "MSSM" and "Multi Species Sward Measure" is the abbreviation
    Then Staff Check if "MSSM" Scheme Selected
    Then Staff Check if card has title as "RCSM" and "Red Clover Silage Measure" is the abbreviation
    Then Staff Check if "RCSM" Scheme Selected
    Then Staff Check if "Eco" Scheme Selected
    And Staff Click on "MSSM" Scheme Selection Card
    And Staff Click on "RCSM" Scheme Selection Card
    And Staff Click on "MSSM" Scheme Selection Card
    And Staff Click on "RCSM" Scheme Selection Card

  @tmslink=BISSAGL-14889
  #How do we check for inactive farmer
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC4 for BISSAGL-7582 INT 2024: Scheme Selection - Add MSSM and RCSM
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "A1211644" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1

  @tmslink=BISSAGL-14885
  # Need MSSM and RCSM disabled herd
  @regression
  Scenario: AT-TC-03 - BISS_24.1.Sprint_6 TC5 for BISSAGL-7582 INT 2024: Scheme Selection - Add MSSM and RCSM
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1

  @tmslink=BISSAGL-14891
  @regression
  Scenario: AT-TC-04 - BISS_24.1.Sprint_6 TC2 for BISSAGL-7582 INT 2024: Scheme Selection - Add MSSM and RCSM
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "J1400098" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Click on "Eco" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button
    Then Staff Click on "Home" Link in Navbar
    # Reset Case to be reused
    Then Staff Enter Value as "J1400098" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Click on "Eco" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button

 @tmslink=BISSAGL-14890
  # Need herd that will show validation error on Land Details page
  @regression
  Scenario: AT-TC-05 - BISS_24.1.Sprint_6 TC3 for BISSAGL-7582 INT 2024: Scheme Selection - Add MSSM and RCSM
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "J1400098" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Click on "Eco" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button
    Then Staff Click on "Home" Link in Navbar
    # Reset Case to be reused
    Then Staff Enter Value as "J1400098" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Click on "Eco" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button
