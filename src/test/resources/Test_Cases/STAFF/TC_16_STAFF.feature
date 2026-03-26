Feature: Regression Pack for Show CISYF and NR late penalty % on Summary Screen - Submission Details

  Scenario: At-TC-00 - Case to get Herds with Paymet from DB
    Given user on staff login page
    And Get 5 Herds with Submission and no Amendments from DataBase
    And Get 10 Herds with Submission from DataBase
    And Get 6 Herds with Submission And Penalty from DataBase
#
#
#
  @tmslink=BISSAGL-14767
  # Amended date will not be updated it will be the submitted date
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL- 7547 | INT: Show CISYF and NR late penalty % on Summary Screen - Submission Details - To verify CISYF and NR Late Penalty charges are reflecting
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission and Penalty from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff capture and print "Customer lookup date" value
    Then Staff capture and print "Received date" value
    Then Staff capture and verify error "Late BISS application %" value
    Then Staff capture and verify no error "Late CISYF application %" value
    Then Staff capture and verify no error "Late NR application %" value
    Then Staff capture and print "Submitted by" value
    Then Staff capture and print "Amendment date" value
    Then Staff capture and print "Late Amendment date" value
    Then Staff capture and print "Amendment" value
    Then Staff capture and print "Parcels (no. of)" value
    Then Staff capture and print "Undigitised Plots" value
    Then Staff capture and print "Sequence No" value

  @tmslink=BISSAGL-14766
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL- 7547 | INT: Show CISYF and NR late penalty % on Summary Screen - Submission Details - To verify No CISYF and No NR Late Penalty charges are displayed
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff capture and print "Customer lookup date" value
    Then Staff capture and print "Received date" value
    Then Staff capture and verify no error "Late BISS application %" value
    Then Staff capture and verify no error "Late CISYF application %" value
    Then Staff capture and verify no error "Late NR application %" value
    Then Staff capture and print "Submitted by" value
    Then Staff capture and print "Amendment date" value
    Then Staff capture and print "Late Amendment date" value
    Then Staff capture and print "Amendment" value
    Then Staff capture and print "Parcels (no. of)" value
    Then Staff capture and print "Undigitised Plots" value
    Then Staff capture and print "Sequence No" value

  @tmslink=BISSAGL-14765
  @regression
  Scenario: AT-TC-03 - BISS_24.1.Sprint_6 TC3 for BISSAGL- 7547 | INT: Show CISYF and NR late penalty % on Summary Screen - Submission Details - To verify  Amendment  field is displayed with none when application is not amended
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission and no Amendment from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff capture and verify "Amendment" value is "None"

  @tmslink=BISSAGL-14764
  @regression
  # Amendment Date Value does not change
  Scenario: AT-TC-04 - BISS_24.1.Sprint_6 TC4 for BISSAGL- 7547 | INT: Show CISYF and NR late penalty % on Summary Screen - Submission Details - To verify Last Amended date is displayed
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff capture and print "Amendment date" value
    Then Staff Click on "BISS Amendments" Link in Navbar
    When Check if Admin Inspection is Completed after clicking On the "application-search-table" table "Amend" link number 1
    Then Staff Click on "BISS Amendments" Link in Navbar
    And Staff Click on "CRISS" Scheme Selection Card
    Then Staff Click on the "Next" button
    Then Staff Click on the "Submit Changes" button
    Then Staff capture and print "Amendment date" value

  @tmslink=BISSAGL-14761
  @regression
  Scenario: AT-TC-05 - BISS_24.1.Sprint_6 TC5 for BISSAGL- 7547 | INT: Show CISYF and NR late penalty % on Summary Screen - Submission Details - To verify Amendment  field details are updated
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission and no Amendment from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff capture and print "Amendment" value
    Then Staff Click on "Home" Link in Navbar
    Then Staff Enter Value as Herd Number with Submission and no Amendment from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "Amend" link number 1
    When Check if Admin Inspection is Completed after clicking On the "application-search-table" table "Amend" link number 1
    Then Staff Click on the "Next" button
    Then Agent Click on First Plot Reference to open Side Drawer
    And Agent Click on CheckBox to request Eh Change
    Then Agent do Request for EhChange in Side Drawer with reason as "Test 1"
    And Agent Click on Application Stepper "Save changes" Button
    And Agent Click on Application Stepper "Cancel" Button
    Then Staff Click on the "Submit Changes" button
    Then Staff capture and print "Amendment" value
    # Assertion pending



