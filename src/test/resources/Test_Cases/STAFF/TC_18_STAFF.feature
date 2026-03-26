Feature: Regression Pack for Requesting Supplementary Payments

  Scenario: At-TC-00 - Case to get Herds with Paymet from DB
    Given user on staff login page
    And Get 10 Herds with Payments from DataBase

  @tmslink=BISSAGL-14868
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_6 TC1 for BISSAGL-5743 INT: BISS 2023 Roles for Requesting Supplementary Payments
    Given user on staff login page
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
    Then Staff Select "BISS Processing 3" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "INSPECTIONS" User Role
    Then Staff Select "BISS Inspections 3" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 3 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
   # Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    Given user on staff login page
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "A1110015" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 1 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Check "Payments" Link not Present in Navbar
    Then Staff Click on "Payments" Link in Navbar
    Then Staff Click on the "Request Supplementary Payment" button
    And Staff Check if dropdown "paymentReason" is present
    And Staff Check if element "irregularitiesDate" is present
    And Staff Check if element "adminControlDate" is present
    And Staff Check if element "conditionalityControlDate" is present

  @tmslink=BISSAGL-14825
  #Changed account to see payments
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_6 TC2 for BISSAGL-5743 INT: BISS 2023 Roles for Requesting Supplementary Payments
    Given user on staff login page
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
    When Staff Filter "PROCESSING" User Role
    Then Staff Select "BISS Processing 1" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "INSPECTIONS" User Role
    Then Staff Select "BISS Inspections 1" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "PAYMENT" User Role
    Then Staff Select "BISS Payment QA 1 User" User Role
    Then Staff Select "BISS Payment QA 2 User" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "ENTS" User Role
    Then Staff Select "BISS ENTS User" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "EMS" User Role
    Then Staff Select "BISS EMS User" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 1 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    # Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    Given user on staff login page
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
    Then Staff Click on the "Request Supplementary Payment" button
    And Staff Check if dropdown "paymentReason" is present
    And Staff Check if element "irregularitiesDate" is present
    And Staff Check if element "adminControlDate" is present
    And Staff Check if element "conditionalityControlDate" is present

  @tmslink=BISSAGL-14829
  @regression
  Scenario: AT-TC-03 - BISS_24.1.Sprint_6 TC3 for BISSAGL-5743 INT: BISS 2023 Roles for Requesting Supplementary Payments
    Given user on staff login page
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
    When Staff Filter "READ" User Role
    Then Staff Select "BISS Read Only User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    # Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    Given user on staff login page
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "P1461522" in the "herdNumber" field
    Then Staff Enter Value as Herd Number with Payments from row 3 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Check "Payments" Link not Present in Navbar

  @tmslink=BISSAGL-14826
  @regression
  Scenario: AT-TC-04 - BISS_24.1.Sprint_6 TC4 for BISSAGL-5743 INT: BISS 2023 Roles for Requesting Supplementary Payments
    Given user on staff login page
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
    When Staff Filter "PROCESSING" User Role
    Then Staff Select "BISS Processing 2" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 2 User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    # Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    Given user on staff login page
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
    Then Staff Click on the "Request Supplementary Payment" button
    And Staff Check if dropdown "paymentReason" is present
    And Staff Check if element "irregularitiesDate" is present
    And Staff Check if element "adminControlDate" is present
    And Staff Check if element "conditionalityControlDate" is present

  @tmslink=BISSAGL-14825
  @regression
  Scenario: AT-TC-05 - BISS_24.1.Sprint_6 TC5 for BISSAGL-5743 INT: BISS 2023 Roles for Requesting Supplementary Payments
    Given user on staff login page
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
    When Staff Filter "INSPECTIONS" User Role
    Then Staff Select "BISS Inspections 2" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    # Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    Given user on staff login page
    When Login with the Username "agr4442"
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
    Then Staff Click on the "Request Supplementary Payment" button
    And Staff Check if dropdown "paymentReason" is present
    And Staff Check if element "irregularitiesDate" is present
    And Staff Check if element "adminControlDate" is present
    And Staff Check if element "conditionalityControlDate" is present

