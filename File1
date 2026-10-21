Feature: Regression Pack for Scheme Selection - Add BISS, CRISS

  Scenario: AT-TC-00 - Case to get Herds with Payment from DB
    Given user on staff login page
    And Get 8 Herds with Submission and no Amendments from DataBase
    And Get 5 Herds with Submission from DataBase

  @tmslink=BISSAGL-14880
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-5919 INT 2024: Scheme Selection - Add BISS, CRISS
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "J1400098" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Submission from row 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    Then Staff Get Title of Scheme Selection Card "1" and check if it is "BISS"
    And Staff Get Acronym Full Form of Scheme Selection Card "1" and check if it is "Basic Income Support for Sustainability"
    Then Staff Click on the "BISS" Scheme Card View Details Link
    And Staff get "Basic Income Support for Sustainability" View Details Dialog Content
    Then Staff Click on the "Close" button
    Then Staff Get Title of Scheme Selection Card "2" and check if it is "CRISS"
    And Staff Get Acronym Full Form of Scheme Selection Card "2" and check if it is "Complementary Redistributive Income Support for Sustainability"
    Then Staff Click on the "CRISS" Scheme Card View Details Link
    And Staff get "Complementary Redistributive Income Support for Sustainability" View Details Dialog Content
    Then Staff Click on the "Close" button
    Then Staff check if toggle present for "BISS" scheme
    Then Staff check if toggle present for "CRISS" scheme
    And Staff Check if "BISS" scheme is selected or not selected
    And Staff Check if "CRISS" scheme is selected or not selected
    And Staff Check if "Protein Aid Scheme" scheme is selected or not selected
    And Staff Click on "CRISS" Scheme Selection Card
    And Staff Click on "CRISS" Scheme Selection Card

  @tmslink=BISSAGL-14879
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL-5919 INT 2024: Scheme Selection - Add BISS, CRISS
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Click on "CRISS" Scheme Selection Card
    And Staff Click on "Protein Aid Scheme" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button
    And Staff Click on the "OK" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    Then Staff verify Admin Check Entry is "Amendment Submitted" in History Screen
    Then Staff Click on "BISS Amendments" Link in Navbar
    And Staff Check if "Protein Aid Scheme" scheme is selected or not selected
    And Staff Check if "CRISS" scheme is selected or not selected
    # Reset post test case
    And Staff Click on "CRISS" Scheme Selection Card
    And Staff Click on "Protein Aid Scheme" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button

  @tmslink=BISSAGL-14877
  @regression
  Scenario: AT-TC-03 - BISS_24.1.Sprint_6 TC3 for BISSAGL-5919 INT 2024: Scheme Selection - Add BISS, CRISS
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
  #K1410720 P1461522
    Then Staff Enter Value as Herd Number with Submission from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Click on "Protein Aid Scheme" Scheme Selection Card
    Then Staff Click on the "Next" button
    # Reset post test case
    Then Staff Click on the "Back" button
    And Staff Click on "Protein Aid Scheme" Scheme Selection Card

  @tmslink=BISSAGL-14876
  @regression
    # Submit any herd as not active and use here
  Scenario: AT-TC-04 - BISS_24.1.Sprint_6 TC4 for BISSAGL-5919 INT 2024: Scheme Selection - Add BISS, CRISS
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "N1041714" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    And Staff Check if "BISS" scheme is selected or not selected
    And Staff Check if "CRISS" scheme is selected or not selected
    And Staff Check if "ANC" scheme is selected or not selected
    And Staff Check if "Eco" scheme is selected or not selected
    And Staff Check if "Organics" scheme is selected or not selected
    And Staff Check if "SIM" scheme is selected or not selected
    And Staff Check if "Protein Aid Scheme" scheme is selected or not selected
    And Staff Check if "CISYF" scheme is selected or not selected
    And Staff Check if "MSSM" scheme is selected or not selected
    And Staff Check if "RCSM" scheme is selected or not selected


