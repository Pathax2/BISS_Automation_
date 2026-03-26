
#Feature: Regression Suite for Admin Inspection
#
#  @tmslink=BISSAGL-14933
#  @regression
#  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 for BISSAGL-7637 Admin Inspection - Access Permissions
#    Given user on staff login page
#    # Helpdesk User
#    When Login with the Username "agr0043"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And click on the SSO Administration application
#    Then Staff Enter Value as "agr5033" the Helpdesk "search-form-username" Field
#    And Agent Click on Application Stepper "Search" Button
#    Then Staff Click on Agent Row in SSO Admin Search
#    And Staff Switch To "authorisations" Tab for user
#    Then Staff Select "BISS" as Application value
#    Then Staff Switch To "Roles" Authorisation Tab
#    When Staff Filter "CO-ORDINATION 1" User Role
#    Then Staff Select "BISS Co-ordination 1" User Role
#    And Staff click on "arrow_forward" SSO Button
#    When Staff Filter "CO-ORDINATION 2" User Role
#    Then Staff Select "BISS Co-ordination 2" User Role
#    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "EMS User" User Role
##    Then Staff Select "BISS EMS User" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Acres User" User Role
##    Then Staff Select "BISS ACRES User" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Inspections 1" User Role
##    Then Staff Select "BISS Inspections 1" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Inspections 2" User Role
##    Then Staff Select "BISS Inspections 2" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Inspections 3" User Role
##    Then Staff Select "BISS Inspections 3" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Processing 1" User Role
##    Then Staff Select "BISS Processing 1" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Processing 2" User Role
##    Then Staff Select "BISS Processing 2" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Processing 3" User Role
##    Then Staff Select "BISS Processing 3" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Risk Management 1" User Role
##    Then Staff Select "BISS Risk Management 1" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Risk Management 2" User Role
##    Then Staff Select "BISS Risk Management 2" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Risk Management 3" User Role
##    Then Staff Select "BISS Risk Management 3" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Read Only Doc" User Role
##    Then Staff Select "BISS Read Only Documents Payments User" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Active Farmer" User Role
##    Then Staff Select "BISS Active Farmer Amend" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "GAEC 8" User Role
##    Then Staff Select "BISS GAEC 8 Amend" User Role
##    And Staff click on "arrow_forward" SSO Button
##    When Staff Filter "Read Only Doc" User Role
##    Then Staff Select "BISS Read only Documents User" User Role
##    And Staff click on "arrow_forward" SSO Button
#    And Agent Click on Application Stepper "Save Authorisations" Button
#    Then Staff Click On "Exit Application" Link
#    Then Staff Click On "Sign Out" Link
#    When Login with the Username "agr5033"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "S1450774" in the "herdNumber" field
#    Then Staff Select "schemeYear" dropdown value as "2023"
#    Then Staff Click on the "Search" button
#    And Staff Click On the "application-search-table" table "View" link number 1
#    Then Staff Click on "Inspections" Link in Navbar
#    Then Staff Check Admin Inspection Status
#    And Click on the My Account Button
#    And Click on Exit BISS Link
#    And Click on Logout Button
#    When Login with the Username "agr0043"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And click on the SSO Administration application
#    Then Staff Enter Value as "agr5033" the Helpdesk "search-form-username" Field
#    And Agent Click on Application Stepper "Search" Button
#    Then Staff Click on Agent Row in SSO Admin Search
#    And Staff Switch To "Authorisations" Tab for user
#    Then Staff Select "BISS" as Application value
#    Then Staff Switch To "Roles" Authorisation Tab
#   # Then Staff Select "BISS Co-ordination 1" User Role
#    Then Staff Select "BISS Co-ordination 2" User Role
##    Then Staff Select "BISS EMS User" User Role
##    Then Staff Select "BISS ACRES User" User Role
##    Then Staff Select "BISS Inspections 1" User Role
##    Then Staff Select "BISS Inspections 2" User Role
##    Then Staff Select "BISS Inspections 3" User Role
##    Then Staff Select "BISS Processing 1" User Role
##    Then Staff Select "BISS Processing 2" User Role
##    Then Staff Select "BISS Processing 3" User Role
##    Then Staff Select "BISS Risk Management 1" User Role
##    Then Staff Select "BISS Risk Management 2" User Role
##    Then Staff Select "BISS Risk Management 3" User Role
##    Then Staff Select "BISS Read Only Documents Payments User" User Role
##    Then Staff Select "BISS Active Farmer Amend" User Role
##    Then Staff Select "BISS GAEC 8 Amend" User Role
##    Then Staff Select "BISS Read only Documents User" User Role
#    And Staff click on "arrow_back" SSO Button
#    And Agent Click on Application Stepper "Save Authorisations" Button
#    Then Staff Click On "Exit Application" Link
#
#
#
#
