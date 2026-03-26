Feature: Regression Suite for all Bugs and Features in BISS

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

  # AT-TC-23 -
#  @tmslink=BISSAGL-1647
  @regression
  Scenario: Sprint BISS_23.1.3 TC1 for BISSAGL-54 | Agents Tools - My Client screen with herd number
    Then Click on the Agent BISS "My Clients" Tab
    And Agent Check Header "Name" With Sorting in My Clients Page
    And Agent Check Header "Herd Number" With Sorting in My Clients Page
    And Agent Check Header " Farmer Status " Without Sorting in My Clients Page
    And Agent Check Header "Herd Expired" Without Sorting in My Clients Page
   # And Agent Check Header "GAEC 8" Without Sorting in My Clients Page
    And Agent Check Header "Eco Space for Nature" Without Sorting in My Clients Page
    And Agent Check Header "BISS Application Status" Without Sorting in My Clients Page
    Then Agent Search Herd Number from row 2
    Then Agent Click on the View Dashboard Button
    Then Agent check for name match

#  @tmslink=BISSAGL-1276
  @regression
  Scenario: Sprint BPS/BISS PI_22.4.6 TC1 for BISSAGL-827 My Clients - ENTS Transfers My Clients
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "Transfers" Tab in My Clients Page
    And Agent Check Header "Name" With Sorting in My Clients Page
    And Agent Check Header "Address" With Sorting in My Clients Page
    And Agent Check Header "Herd No." With Sorting in My Clients Page
    And Agent Check Header "Expired" Without Sorting in My Clients Page
    And Agent Check Header "Transfers" Without Sorting in My Clients Page
    And Agent Chenge Number of rows to "50" in My Clients Page
#    And Agent Chenge Number of rows to "399" in My Clients Page
    And Agent Chenge Number of rows to "20" in My Clients Page
    Then Agent Switch to Last Page using Pagination Arrow

#  @tmslink=BISSAGL-905
  @regression
  Scenario: Sprint BISS_23.1.3 TC2 for BISSAGL-54 | Agents Tools - My Client screen with herd number
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search Herd Number from row 2
    Then Agent Click on the View Dashboard Button
    And Agent Check Name And Herd
    Then Agent Check Personalised Greeting Message
    #And Agent Check BISS Application Deadline Message


