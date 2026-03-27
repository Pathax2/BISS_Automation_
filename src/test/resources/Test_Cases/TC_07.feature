Feature: TC_07 - Regression Suite for all Bugs and Features in BISS - 2

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Regression coverage for Active Farmer screen bugs in BISS.
  #
  #   Active Scenarios:
  #     AT-TC-07 : BISSAGL-119 — Active Farmer Screen Error
  #                Verifies the Active Status checkbox exists on the Active Farmer screen
  #                and that the draft can be deleted after the check
  #
  #     AT-TC-13 : BISSAGL-121 — Active Farmer Approved missing message
  #                "May be subject to administrative checks"
  #                Verifies the start application flow reaches the Active Farmer screen
  #                (admin check message validation is currently commented pending investigation)
  #
  # Notes:
  #   1. Both scenarios use dynamic herd selection via row index rather than a hardcoded
  #      herd number — row 10 from the Not Started quick filter is used as the test fixture.
  #   2. AT-TC-13 still has some steps commented inline — kept for traceability.
  #   3. AT-TC-00 is a data query scenario that is commented out and moved to the bottom
  #      of this file pending implementation.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link

  #@tmslink=BISSAGL-7043
  @regression
  Scenario: AT-TC-07 - BISSAGL-119 | Active Farmer Screen Error
    Given the agent is on the BISS Agent Home Screen
    And the agent applies the "Not Started" quick filter
    And the agent picks a herd from the list
    When the agent selects the herd from row 10
      # Then Agent Search for Herd Number "N106036X"
      # N1090812 #N1010991 #N1091932 #N1170280 #N1080655 #N7010276
      # And Agent Click on the Row with the Client "N106036X"
    And the agent clicks on the View Dashboard button
    Then the agent clicks on the "Start Application" farmer dashboard button
    And the active status checkbox is present on the Active Farmer screen
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent selects the herd from row 10
    And the agent clicks on the View Dashboard button
    Then the agent clicks on the "Delete draft" farmer dashboard button
    And the agent clicks on the "Yes, delete" dialog button

  #@tmslink=BISSAGL-7035
  @regression
  Scenario: AT-TC-13 - BISSAGL-121 | Active Farmer Approved missing message "May be subject to administrative checks"
    Given the agent is on the BISS Agent Home Screen
    And the agent applies the "Not Started" quick filter
    And the agent picks a herd from the list
    When the agent selects the herd from row 10
      # Then Agent Search for Herd Number "N106036X"
      # N1090812 #N1010991 #N1091932 #N1170280 #N1080655 #N7010276
      # And Agent Click on the Row with the Client "N106036X"
    And the agent clicks on the View Dashboard button
    Then the agent clicks on the "Start Application" farmer dashboard button
#    Then Check if Admin Check Text is present
#    Then Click on the Agent BISS "My Clients" Tab
#    Then Agent Search Herd Number from row 10
#    Then Agent Click on the View Dashboard Button
#    And Agent Click On "Delete draft" Farmer Dashboard Button
#    Then Agent Click on the "Yes, delete" dialog box button


  # ------------------------------------------------------------------------------------------------------------------
  # Commented Scenarios — kept for traceability and future re-enablement
  # ------------------------------------------------------------------------------------------------------------------

  #  @regression
  #  Scenario: AT-TC-00 - Queries required for BISSAGL-301
  #    Given Agent is on BISS Farmer Dashboard Screen
  #    And Agent Select " Not Started " Quick Filter
  #    And Agent picks herd in list
