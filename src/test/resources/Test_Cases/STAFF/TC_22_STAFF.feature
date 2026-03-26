Feature: Regression Pack for Free Text Commonage Evidence Query Letter

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Given user on staff login page
    Then Get 5 Herds with Query Letter Not Started from Database

  @tmslink=BISSAGL-14857
  # Change the herd for generating BISS Letter
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-7518 Add Free Text section to the Commonage Evidence Query Letter
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Query Letter Not Started from row 2 in the "herdNumber" field
   # Then Staff Enter Value as "G1060529" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Correspondence" Link in Navbar
    And Staff switch to the " Query Letter" tab in Correspondence Header
    #And Staff switch to the " Query Letter" tab
#    Then Staff Change the "Available application letters" no formcontrolname dropdown value as "BISS Commonage Evidence Letter"
    Then Staff Change the "docType" query letter dropdown value as "BISS Commonage Evidence Letter"
    And Staff Type Text as "Test Comment" in "comment" Query Letters TextArea
    Then Staff Click on the "Preview" button


  @tmslink=BISSAGL-14856
  # Change the herd for generating BISS Letter
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL-7518 Add Free Text section to the Commonage Evidence Query Letter
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Admin Check Not Started from row 2 in the "herdNumber" field
    #Then Staff Enter Value as "G1060529" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Correspondence" Link in Navbar
    And Staff switch to the " Query Letter" tab in Correspondence Header
    #And Staff switch to the " Query Letter" tab
#    Then Staff Change the "Available application letters" no formcontrolname dropdown value as "BISS Commonage Evidence Letter"
    Then Staff Change the "docType" query letter dropdown value as "BISS Commonage Evidence Letter"
    And Staff Type Text as "Test Comment" in "comment" Query Letters TextArea
    Then Staff Click on the "Preview" button



