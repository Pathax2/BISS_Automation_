Feature: Regression Pack for Deleting Re-Adding Parcels/Plots on Multi Herd RFPs

  @tmslink=BISSAGL-14687
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_2 TC1 for BISSAGL-4362 iNet 2024 - Deleting and re-adding parcels / plots on multi herd RFPs
    Given user on login page
    When clicks on new agent login button
#    When Agent Enters NRCISYF Agent 1 Username
    When Enters "aga6306" as new username
    And clicks on Login button
#    And Agent Enters the Pin Number
    And enter password
#    Then Agent Enters 1 as the OTP
#    And clicks on Login button
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    When Agent Search for Herd Number Field and Enter Herd as "A1230614"
    And Agent Click on the Row with the Client "A1230614"
    # N1100559
    Then Agent Click on the View Dashboard Button
    #And Agent Click On " Start Application " Farmer Dashboard Button
    And Agent Click On Start or Continue Application Farmer Dashboard Button
#    And Staff Click on "Eco" Scheme Selection Card
#    Then Staff Click on the "Next" button
    # Just put in validation cases

#  @tmslink=BISSAGL-14763
#    @regression
#    Scenario: AT-TC-02 - BISS_24.2.Sprint_1 TC1 for BISSAGL-8126 INT:- Select/Unselect the ECO scheme on the BISS Internal Amendments, which is not reflecting on the scheme panel summary screen for 2023.
#    Given user on login page
#    When clicks on new agent login button
##    When Agent Enters NRCISYF Agent 1 Username
#    When Enters "aga6306" as new username
#    And clicks on Login button
##    And Agent Enters the Pin Number
#    And enter password
##    Then Agent Enters 1 as the OTP
##    And clicks on Login button
#    And clicks on Login button
#    Then Agent Enters "1" as the OTP
#    And clicks on Login button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    When Agent Search for Herd Number Field and Enter Herd as "A1230614"
#    And Agent Click on the Row with the Client "A1230614"
#    # N1100559
#    Then Agent Click on the View Dashboard Button
#    And Agent Click On " Start application " Farmer Dashboard Button
#    And Staff Click on "Eco" Scheme Selection Card
#    Then Staff Click on the "Next" button

      # Just put in validation cases
