Feature: Regression Pack for SIM Payments

  @tmslink=BISSAGL-14769
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7848 iNet24: Payments - SIM PaymentsTC_151_Regression_INet_BISS24.2Sprint1_BISSAGL7848_SIM_Payments.feature
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
    #Then Agent Enters "1" as the OTP
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
