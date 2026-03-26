Feature: Eco-Scheme - iNet - TRNs with parcels & >50% Grassland applying for AP2

  @tmslink=BISSAGL-14776
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC2 for BISSAGL-7734 Eco-Scheme - iNet - TRNs with parcels & >50% Grassland applying for AP2
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
    Then Agent Search for Herd Number "A1360089"
    And Agent Click on the Row with the Client "A1360089"
    # N1100559 A1360089
    Then Agent Click on the View Dashboard Button
    #And Agent Click On " Start Application " Farmer Dashboard Button
    And Agent Click On Start or Continue Application Farmer Dashboard Button


  @tmslink=BISSAGL-14777
    @regression
    Scenario: AT-TC-02 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7734 Eco-Scheme - iNet - TRNs with parcels & >50% Grassland applying for AP2
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
    Then Agent Search for Herd Number "A1131667"
    And Agent Click on the Row with the Client "A1131667"
    # N1100559 A1131667
    Then Agent Click on the View Dashboard Button
    #And Agent Click On " Start Application " Farmer Dashboard Button
    And Agent Click On Start or Continue Application Farmer Dashboard Button
      # Just put in validation cases
