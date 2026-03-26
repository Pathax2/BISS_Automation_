Feature: Regression Pack for INT: Display Late penalty percentage for CISYF late penalty

  Background:
    Given user on staff login page

  @regression
  Scenario: AT-TC-00 - JDBC Queries for Data
    Then Get 20 Herds with Payments from DataBase

  @tmslink=BISSAGL-14747
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC1 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed -  BISS Coordination 1 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "CO-ORDINATION 1" User Role
    Then Staff Select "BISS Co-ordination 1" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but doesn't exist so writing for euro

  @tmslink=BISSAGL-14746
  @regression
  Scenario: AT-TC-02 - BISS_24.2.Sprint_1 TC2 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed -  BISS Coordination 2 User
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "CO-ORDINATION 2" User Role
    Then Staff Select "BISS Co-ordination 2" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "C1251161" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 2 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14743
  @regression
  Scenario: AT-TC-03 - BISS_24.2.Sprint_1 TC3 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Processing 1 User
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "PROCESSING 1" User Role
    Then Staff Select "BISS Processing 1" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14742
  @regression
  Scenario: AT-TC-04 - BISS_24.2.Sprint_1 TC4 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Processing 2 User
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "PROCESSING 2" User Role
    Then Staff Select "BISS Processing 2" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 4 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14741
  @regression
  Scenario: AT-TC-05 - BISS_24.2.Sprint_1 TC5 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Processing 3 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "PROCESSING 3" User Role
    Then Staff Select "BISS Processing 3" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 5 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14740
  @regression
  Scenario: AT-TC-06 - BISS_24.2.Sprint_1 TC 6 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Inspections 1 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "INSPECTIONS 1" User Role
    Then Staff Select "BISS Inspections 1" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 6 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14739
  @regression
  Scenario: AT-TC-07 - BISS_24.2.Sprint_1 TC 7 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Inspections 2 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "INSPECTIONS 2" User Role
    Then Staff Select "BISS Inspections 2" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 7 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14738
  @regression
  Scenario: AT-TC-08 - BISS_24.2.Sprint_1 TC 8 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Inspections 3 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "INSPECTIONS 3" User Role
    Then Staff Select "BISS Inspections 3" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 8 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14737
  @regression
  Scenario: AT-TC-09 - BISS_24.2.Sprint_1 TC 9 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - EMS User
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "EMS" User Role
    Then Staff Select "BISS EMS User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 9 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading

    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14736
  @regression
  Scenario: AT-TC-10 - BISS_24.2.Sprint_1 TC 10 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Risk Management 1 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 1 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 10 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
     # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14735
  @regression
  Scenario: AT-TC-11 - BISS_24.2.Sprint_1 TC 11 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Risk Management 2 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 2 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 11 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14734
  @regression
  Scenario: AT-TC-12 - BISS_24.2.Sprint_1 TC 12 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify Late penalty percentage for CISYF late penalty is displayed - BISS Risk Management 3 user
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 2 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 12 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    # Write steps for amounts but it is not in small text

  @tmslink=BISSAGL-14733
  @regression
  Scenario: AT-TC-13 - BISS_24.2.Sprint_1 TC 13 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify total payment amount is displayed with a row name 'Total DPS Payment'  in payment summary table
    # Helpdesk User
    When Login with the Username "agr0043"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And click on the SSO Administration application
    Then Staff Enter Value as "agr0191" the Helpdesk "search-form-username" Field
    And Agent Click on Application Stepper "Search" Button
    Then Staff Click on Agent Row in SSO Admin Search
    And Staff Switch To "authorisations" Tab for user
    Then Staff Select "BISS" as Application value
    Then Staff Switch To "Roles" Authorisation Tab
    And Staff Select alll Roles on "right" side box
    And Staff click on "arrow_back" SSO Button
    When Staff Filter "CO-ORDINATION" User Role
    Then Staff Select "BISS Co-ordination 1" User Role
    Then Staff Select "BISS Co-ordination 2" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "PROCESSING" User Role
    Then Staff Select "BISS Processing 1" User Role
    Then Staff Select "BISS Processing 2" User Role
    Then Staff Select "BISS Processing 3" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "INSPECTIONS" User Role
    Then Staff Select "BISS Inspections 1" User Role
    Then Staff Select "BISS Inspections 2" User Role
    Then Staff Select "BISS Inspections 3" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "EMS" User Role
    Then Staff Select "BISS EMS User" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 1 User" User Role
    Then Staff Select "BISS Risk Management 2 User" User Role
    Then Staff Select "BISS Risk Management 3 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    #Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 7 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check for "bissAmount" header below respective heading
    Then Staff Check for "crissAmount" header below respective heading
    Then Staff Check for "cisyfAmount" header below respective heading
    And Staff Check Total DPS Payment for Herd

  @tmslink=BISSAGL-14732
  @regression
  Scenario: AT-TC-14 - BISS_24.2.Sprint_1 TC 14 for BISSAGL-5517 | INT: Display Late penalty percentage for CISYF late penalty | To verify each row header in payment summary table  under 'Reduction Type'
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    #P1461522
    Then Staff Enter Value as Herd Number with Payments from row 7 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Payments" Link in Navbar
    And Staff Check if "BISS" header exists in Payment summary Table
    And Staff Check if "CRISS" header exists in Payment summary Table
    And Staff Check if "CISYF" header exists in Payment summary Table
    Then Staff Check if 2 Reduction Type header exists in Payment summary Table
    And Staff Check if "Gross Claim" header type 1 exists in Payments Page
    And Staff Check if "Overclaim" header type 1 exists in Payments Page
    And Staff Check if "Overclaim Penalty" header type 1 exists in Payments Page
    And Staff Check if "Gross Balance Held" header type 1 exists in Payments Page
    And Staff Check if "Interim Payment" header type 1 exists in Payments Page
    And Staff Check if "Late Penalty" header type 2 exists in Payments Page
    And Staff Check if "Non Declared Parcels" header type 1 exists in Payments Page
    And Staff Check if "Between €60,000-€100,000" header type 3 exists in Payments Page
    And Staff Check if "Amount Exceeding €100,000" header type 3 exists in Payments Page
    And Staff Check if "Financial Discipline" header type 1 exists in Payments Page
    And Staff Check if "CISYF Inspection Penalty" header type 1 exists in Payments Page
    And Staff Check if "Conditionality Penalty" header type 4 exists in Payments Page
    And Staff Check if "Net Payment Amount" header type 1 exists in Payments Page
    # SQL to be checked to suit validations






