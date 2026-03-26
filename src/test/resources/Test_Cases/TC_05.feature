Feature: Verify the My Clients Page Functionalities

  #Background updated
  Background:
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab

  #@tmslink=BISSAGL-7010
  @regression
  Scenario: To switch between all tabs for agent
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "Transfers" Tab in My Clients Page
    And Agent switch to "NR/CISYF" Tab in My Clients Page
    And Agent switch to "BISS Applications" Tab in My Clients Page

  @regression
  Scenario: To Check all working functionalities in the My Clients Page
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search for Herd Number "N7010276"
    And Agent Click on the Row with the Client "N7010276"
    Then Agent Click on the View Dashboard Button





























