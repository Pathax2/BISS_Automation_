Feature: Regression Pack for Active Farmer and Admin Process History Notes - In Progress to be cleared

  @tmslink=BISSAGL-14837
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-7779 Active Farmer - GAA for Active Farmer & Admin Process
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "A1220864" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Active Farmer" Link in Navbar
#    Then Staff Click on the "Start admin check" button
#    And Agent Click on the "Send" dialog box button
#    And Staff Select "REJECTED" as Admin Check Result
#    Then Staff Enter "Test Note" as Admin Notes
#    Then Staff Click on the "Save changes" button
#    Then Staff Click on the "Start appeal process" button
#    And Agent Click on the "Yes,Appeal" dialog box button
    #Upload Files and approve




