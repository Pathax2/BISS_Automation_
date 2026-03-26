Feature: Regression Suite for all Bugs and Features in BISS - 2


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
    Then Click on the Agent BISS "My Clients" Tab



#  @regression
#  Scenario: AT-TC-00 - Queries required for BISSAGL-301
#    Given Agent is on BISS Farmer Dashboard Screen
#    And Agent Select " Not Started " Quick Filter
#    And Agent picks herd in list


#  @tmslink=BISSAGL-7043
  @regression
  Scenario: AT-TC-07 - BISSAGL-119 | Active Farmer Screen Error
    Given Agent is on BISS Farmer Dashboard Screen
    And Agent Select " Not Started " Quick Filter
    And Agent picks herd in list
    Then Agent Search Herd Number from row 10
   # Then Agent Search for Herd Number "N106036X"
    # N1090812 #N1010991 #N1091932 #N1170280 #N1080655 #N7010276
    #And Agent Click on the Row with the Client "N106036X"
    Then Agent Click on the View Dashboard Button
    Given  Agent is on BISS Farmer Dashboard Screen
    And Agent Click On " Start Application " Farmer Dashboard Button
    Then Check if Active Status CheckBox Exists
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search Herd Number from row 10
    Then Agent Click on the View Dashboard Button
    And Agent Click On "Delete draft" Farmer Dashboard Button
    Then Agent Click on the "Yes, delete" dialog box button

#  @tmslink=BISSAGL-7035
  @regression
  Scenario: AT-TC-13 - BISSAGL-121 | Active Farmer Approved missing message "May be subject to administrative checks"
    Given Agent is on BISS Farmer Dashboard Screen
    And Agent Select " Not Started " Quick Filter
    And Agent picks herd in list
    Then Agent Search Herd Number from row 10
    #Then Agent Search for Herd Number "N106036X"
    # N1090812 #N1010991 #N1091932 #N1170280 #N1080655 #N7010276
   # And Agent Click on the Row with the Client "N106036X"
    Then Agent Click on the View Dashboard Button
    Given  Agent is on BISS Farmer Dashboard Screen
    And Agent Click On " Start Application " Farmer Dashboard Button
#    Then Check if Admin Check Text is present
#    Then Click on the Agent BISS "My Clients" Tab
#    Then Agent Search Herd Number from row 10
#    Then Agent Click on the View Dashboard Button
#    And Agent Click On "Delete draft" Farmer Dashboard Button
#    Then Agent Click on the "Yes, delete" dialog box button

