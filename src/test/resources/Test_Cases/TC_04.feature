Feature: Verify the Dashboard Page Functionalities

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

  @regression
  Scenario: To Check all working functionalities in the Dashboard Page
    Given Agent is on BISS Agent Home Screen
    When Agent Click on the "Transfer of Entitlements" hyperlink in the Quick Links Section
    Then Click on the Agent BISS "Home" Tab
    When Agent Click on the "CISYF" hyperlink in the Quick Links Section
    Then Click on the Agent BISS "Home" Tab
    And Agent Click on View Clients Button in BISS Applications Section
    Then Click on the Agent BISS "Home" Tab
    Then Agent Click On Read More Button in News Section
    And Click on Contact US Button




























