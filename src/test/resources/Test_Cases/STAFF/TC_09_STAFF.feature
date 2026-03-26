Feature: Regression Pack for GAEC7

  @tmslink=BISSAGL-14900
  # Find a new herd and continue the application to check GAEC 7 details
  @regression
  Scenario: AT-TC-01 - BISS_24.1.Sprint_5 TC1 for BISSAGL-6210 | iNet 2024 - GAEC 7 - Screen Improvements for Crop Diversification Totals (Start app)
    Given user on login page
    When clicks on new agent login button
    When Enters "aga6550" as new username
    And clicks on Login button
#    Clicks On Agent Logon Button
#    And Agent Enters the Pin Number
#    And enters agent password
    When enter password
#    And Clicks On Agent Logon Button
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
#    And Clicks On Agent Logon Button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    # D1010434
    Then Agent Search for Herd Number "D2611067"
    And Agent Click on the Row with the Client "D2611067"
    #Y1031128
    Then Agent Click on the View Dashboard Button
    Given  Agent is on BISS Farmer Dashboard Screen
    And Delete Draft if exists
    And Agent Click On Start or Continue Application Farmer Dashboard Button
    And Agent Click on the "Making Hay/Silage/Haylage" CheckBox
   # And Agent Click On "Continue application" Farmer Dashboard Button
    Then Agent Click on Application Stepper "Next" Button
    Then Agent Click on Application Stepper "Next" Button
    Then Agent Click on the "I understand" dialog box button
    Then Agent Click on Application Stepper "Next" Button
    Then Agent Check GAEC 7 Status
    And Agent Check if Total Eligible Hectare Values Displayed
    Then Agent Check if Crop Diversification Summary Table has data populated
   # And Agent Check Total Other Eligible Crop Area Value
    And Agent Check Total Eligible ha values in Breakdown Table
    Then Agent Check Total Arable Land Percentage values in Breakdown Table


#    And Agent Click on the "Other" CheckBox
#    Then Agent Enter Note as "Test Note" for Other Option in Active Farmer
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on the "Eco" scheme card
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on the "I understand" dialog box button
#    And Agent Select "Action Required" Quick Filter
#    Then Change the number of rows Displayed in the page to "50"
#    Then Agent Fill in Mandatory Agricultural Activity Information
#    And Agent Select "View all" Quick Filter
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on Application Stepper "Next" Button
#    And Agent Click on Next Button in Review and Submit Page
#    Then Agent Click on Application Stepper "Submit" Button
#    Then Agent Click on Terms & Conditions CheckBox
#    Then Agent Click on Application Stepper "Submit" Button
#    And Agent Click on the "Yes, I confirm" dialog box button
#    Given user on staff login page
#    When Login with the Username "agr11612"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "Y1010686" in the "herdNumber" field
#    Then Staff Click on the "Search" button
#    And Staff Click On the "application-search-table" table "View" link
#    # Agricultural Activity Sorting not working


  @tmslink=BISSAGL-14899
  @regression
  Scenario: AT-TC-02 - BISS_24.1.Sprint_5 TC2 for BISSAGL-6210 | iNet 2024 - GAEC 7 - Screen Improvements for Crop Diversification Totals (Amend app)
    Given user on login page
    When clicks on new agent login button
    When Enters "aga6040" as new username
    And clicks on Login button
#    Clicks On Agent Logon Button
#    And Agent Enters the Pin Number
#    And enters agent password
    When enter password
#    And Clicks On Agent Logon Button
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
#    And Clicks On Agent Logon Button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search for Herd Number "D3241018"
    And Agent Click on the Row with the Client "D3241018"
    #Y1031128
    Then Agent Click on the View Dashboard Button
    Given  Agent is on BISS Farmer Dashboard Screen
    And Delete Draft if exists
#    And Agent Click On Start or Continue amendment Farmer Dashboard Button
#    And Agent gives reason for late amendment
  #*****Commented out as application amendment date is closed*****
#    Then Agent Click on Application Stepper "Submit" Button
#    #And Agent Click On "Continue amendment" Farmer Dashboard Button
#    #And Agent Click on the "Making Hay/Silage/Haylage" CheckBox
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on the "I understand" dialog box button
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Check GAEC 7 Status
#    And Agent Check if Total Eligible Hectare Values Displayed
#    Then Agent Check if Crop Diversification Summary Table has data populated
#    #And Agent Check Total Other Eligible Crop Area Value
#    And Agent Check Total Eligible ha values in Breakdown Table
#    Then Agent Check Total Arable Land Percentage values in Breakdown Table

#    And Agent Click on the "Other" CheckBox
#    Then Agent Enter Note as "Test Note" for Other Option in Active Farmer
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on the "Eco" scheme card
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on the "I understand" dialog box button
#    And Agent Select "Action Required" Quick Filter
#    Then Change the number of rows Displayed in the page to "50"
#    Then Agent Fill in Mandatory Agricultural Activity Information
#    And Agent Select "View all" Quick Filter
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on Application Stepper "Next" Button
#    Then Agent Click on Application Stepper "Next" Button
#    And Agent Click on Next Button in Review and Submit Page
#    Then Agent Click on Application Stepper "Submit" Button
#    Then Agent Click on Terms & Conditions CheckBox
#    Then Agent Click on Application Stepper "Submit" Button
#    And Agent Click on the "Yes, I confirm" dialog box button
#    Given user on staff login page
#    When Login with the Username "agr11612"
#    And enter password
#    Then Staff Select Data Protection CheckBox
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    Then Staff Enter Value as "Y1010686" in the "herdNumber" field
#    Then Staff Click on the "Search" button
#    And Staff Click On the "application-search-table" table "View" link
#    # Agricultural Activity Sorting not working







