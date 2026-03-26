Feature: Regression Suite for SIM Tick Internal and INET

  @tmslink=BISSAGL-14839
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-7578 | INT: Deselecting SIM and Tick on BISS Application Screen updating
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "D3670245" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Check SIM Tick on BISS Application Summary Screen
#    Then Staff zoom out of screen thrice
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Test Note for 8078"
    Then Staff Click on the "Finish" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "BISS Application" Link in Navbar
    Then Staff Check SIM Tick on BISS Application Summary Screen
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Reset Test for 8078"
    Then Staff Click on the "Finish" button

  @tmslink=BISSAGL-14838
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC1 for BISSAGL-7578 | INT: Selecting SIM and Tick on BISS Application Screen updating
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "D1591313" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Check SIM Tick on BISS Application Summary Screen
#    Then Staff zoom out of screen thrice
    Then Staff Click on "SIM" Link in Navbar
    And Staff Check SIM Calculated Payable Area
    And Staff Toggle Sim option
    And Staff Check SIM Calculated Payable Area
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Test Note for 8079"
    Then Staff Click on the "Finish" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "BISS Application" Link in Navbar
    Then Staff Check SIM Tick on BISS Application Summary Screen
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Reset Test for 8079"
    Then Staff Click on the "Finish" button

  @tmslink=BISSAGL-14782
  @regression
  Scenario: AT-TC-03 - BISS_24.2.Sprint_1 TC3 for BISSAGL-7578 | INT: Deselecting SIM and Tick on BISS Application Screen updating (retest)
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "D3670245" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Check SIM Tick on BISS Application Summary Screen
#    Then Staff zoom out of screen thrice
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Test Note for 8197"
    Then Staff Click on the "Finish" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "BISS Application" Link in Navbar
    Then Staff Check SIM Tick on BISS Application Summary Screen
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Reset Test for 8197"
    Then Staff Click on the "Finish" button

  @tmslink=BISSAGL-14781
  @regression
  Scenario: AT-TC-04 - BISS_24.2.Sprint_1 TC4 for BISSAGL-7578 | INT: Selecting SIM and Tick on BISS Application Screen updating
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "D1591313" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Check SIM Tick on BISS Application Summary Screen
#    Then Staff zoom out of screen thrice
    Then Staff Click on "SIM" Link in Navbar
    And Staff Check SIM Calculated Payable Area
    And Staff Toggle Sim option
    And Staff Check SIM Calculated Payable Area
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Test Note for 8079"
    Then Staff Click on the "Finish" button
    Then Staff Click on "Notes/History" Link in Navbar
    And Staff switch to the "History" tab
    And Staff Check History Tab for Activity
    Then Staff Click on "BISS Application" Link in Navbar
    Then Staff Check SIM Tick on BISS Application Summary Screen
    Then Staff Click on "SIM" Link in Navbar
    And Staff Toggle Sim option
    Then Staff Click on the "Submit" button
    And Staff Enter Sim Notes as "Reset Test for 8079"
    Then Staff Click on the "Finish" button

  @tmslink=BISSAGL-14779
  @regression
  Scenario: AT-TC-05 - BISS_24.2.Sprint_1 TC5 for BISSAGL-7578 | iNet regression (Scheme Selection edit 2024)
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "I122C514" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Check SIM Tick on BISS Application Summary Screen




