Feature: Regression Pack for AMS Internal Screen

  @tmslink=BISSAGL-14750
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC2 for BISSAGL-7295 AMS internal screen (Read Only)
    Given user on staff login page
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
    When Staff Filter "AMS" User Role
    Then Staff Select "BISS AMS 1" User Role
    Then Staff Select "BISS AMS 2" User Role
    And Staff click on "arrow_forward" SSO Button
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
    When Staff Filter "RISK" User Role
    Then Staff Select "BISS Risk Management 1 User" User Role
    Then Staff Select "BISS Risk Management 2 User" User Role
    Then Staff Select "BISS Risk Management 3 User" User Role
    And Staff click on "arrow_forward" SSO Button
    When Staff Filter "DIGITISING" User Role
    Then Staff Select "BISS Digitising Supervisor User" User Role
    Then Staff Select "BISS Digitising User" User Role
    And Staff click on "arrow_forward" SSO Button
    And Agent Click on Application Stepper "Save Authorisations" Button
    # Then Staff Click On "Exit Application" Link
    Then Staff Click On SignOut Button
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "IFP10100" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
#    Then Staff Hover over "ams-status-yellow" Info Widget to Check Tooltip
#    Then Staff Hover over "ams-status-green" Info Widget to Check Tooltip
#    Then Staff Hover over "ams-status-red" Info Widget to Check Tooltip
#    Then Staff Hover over "ams-status-grey" Info Widget to Check Tooltip
#    And Staff Check if AMS SchemeYear Dropdown Value populated
    Then Staff Check "Partnership Farmer" Client Details in AMS page
    Then Staff Check "Herd Number" Client Details in AMS page
    Then Staff Switch to Partner Herd "I1330645" Tab
    Then Staff Switch to Partner Herd "I1020085" Tab
    And Staff Check if "Townland" Header Tab present in AMS screen
    And Staff Check if "Parcel no." Header Tab present in AMS screen
    And Staff Check if "AMS status" Header Tab present in AMS screen
    And Staff Check if "Scheme" Header Tab present in AMS screen
    And Staff Check if "AMS finding" Header Tab present in AMS screen
    And Staff Check if "AMS monitoring status" Header Tab present in AMS screen
    And Staff Check if "Claimed area (ha)" Header Tab present in AMS screen
    And Staff Check if "Updated claimed area (ha)" Header Tab present in AMS screen
    And Staff Check if "Parcel use" Header Tab present in AMS screen
    And Staff Check if "Updated parcel use" Header Tab present in AMS screen
    And Staff Check if "Inspected crop found" Header Tab present in AMS screen
    And Staff Check that Pagination Functionality working fine on AMS page
    # Just put in validation cases

  @tmslink=BISSAGL-14748
    @regression
    Scenario: AT-TC-02 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7295 AMS internal screen (Read Only)
    Given user on staff login page
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "IFP10100" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
#    And Staff Check if AMS SchemeYear Dropdown Value populated
    Then Staff Check "Partnership Farmer" Client Details in AMS page
    Then Staff Check "Herd Number" Client Details in AMS page
    Then Staff Switch to Partner Herd "I1330645" Tab
    Then Staff Switch to Partner Herd "I1020085" Tab
    And Staff Check if "Townland" Header Tab present in AMS screen
    And Staff Check if "Parcel no." Header Tab present in AMS screen
    And Staff Check if "AMS status" Header Tab present in AMS screen
    And Staff Check if "Scheme" Header Tab present in AMS screen
    And Staff Check if "AMS Finding" Header Tab present in AMS screen
    And Staff Check if "AMS monitoring status" Header Tab present in AMS screen
    And Staff Check if "Claimed area (ha)" Header Tab present in AMS screen
    And Staff Check if "Updated claimed area (ha)" Header Tab present in AMS screen
    And Staff Check if "Parcel use" Header Tab present in AMS screen
    And Staff Check if "Updated parcel use" Header Tab present in AMS screen
    And Staff Check if "Inspected crop found" Header Tab present in AMS screen
    And Staff Check that Pagination Functionality working fine on AMS page
      # Just put in validation cases

  @tmslink=BISSAGL-14724
  @regression
  Scenario: AT-TC-03 - BISS_24.2.Sprint_1 TC3 for BISSAGL-7295 AMS internal screen (Read Only)
    Given user on staff login page
    When Login with the Username "agr0191"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "IFP10100" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Inspections" Link in Navbar
    And Staff switch to the "AMS" tab
    Then Staff Hover over "ams-status-yellow" Info Widget to Check Tooltip
    Then Staff Hover over "ams-status-green" Info Widget to Check Tooltip
    Then Staff Hover over "ams-status-red" Info Widget to Check Tooltip
    Then Staff Hover over "ams-status-grey" Info Widget to Check Tooltip
    # Validation not possible as the tooltip occurs temporarily can just perform hover action
