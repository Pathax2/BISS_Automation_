Feature: TC-03 Entitlements/Usage Button

  @sanity
  @tmslink=ENTSAGL-9414
  Scenario: Entitlements/Usage Button
    Given user on login page
    When clicks on new agent login button
    #When Agent Enters new NRCISYF Agent 1 Username
    When enters "aga6504" as new username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then External User Enters sms OTP
    And clicks on Continue button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "Transfers" Tab in My Clients Page
    When Agent Search for Herd Number Field and Enter Herd as "A1060280"
    #And Agent Search for Herd Number Field and Enter Herd from row 2
    Then Agent Click On View Link for Searched Herd
    And Agent Navigate to "Entitlements / Usage" tab on the SideNavBar
    And Agent Gets OwnerID of herd
    And Agent verifies Entitlement position
