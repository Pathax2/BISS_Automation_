Feature: Regression Pack for Prevent_SIM_Indicator_Selected_when_Crop_not_SIM

  @tmslink=BISSAGL-14757
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_2 TC1 for BISSAGL-7832 iNet24: Payments - add Payment Page and paid status and date for Agents
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

  @tmslink=BISSAGL-14691
  @regression
  Scenario: AT-TC-02 - BISS_24.2.Sprint_2 TC3 for BISSAGL-7832 iNet24: Payments - add Payment Page and paid status and date for Agents
    Given user on login page
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

  @tmslink=BISSAGL-14692
  @regression
  Scenario: AT-TC-03 - BISS_24.2.Sprint_2 TC2 for BISSAGL-7832 iNet24: Payments - add Payment Page and paid status and date for Agents
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
