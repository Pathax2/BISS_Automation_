Feature:  BISSAGL-20849 2025 iNet - Agent unable to view payments or land details for client

  Background:
    Given user on login page
    When clicks on new agent login button
    Then Login with the Specified Username "agr14385"
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab


#  @tmslink=BISSAGL-1647
  @regression
  Scenario:  Agent able to view payments or land details for client
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search for Herd Number Field and Enter Herd as "D3350968"
    Then Agent Click on the View Dashboard Button
    Then Agent Click on "Applications / Payments" Link in Navbar
    And Agent Select "schemeYear" dropdown value as " 2023 "
    And Agent Click on "Payments" tab
    Then Agent Verifes No "Techinal error getting information extra information for herd number" Error shown on page.

