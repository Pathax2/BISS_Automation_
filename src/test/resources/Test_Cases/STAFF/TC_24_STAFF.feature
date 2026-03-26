Feature: Regression Pack for Add Payments Tab

  @tmslink=BISSAGL-14785
  @regression
  Scenario: AT-TC-01 - BISS_24.2.Sprint_1 TC1 for BISSAGL-7245 iNet 2024 - Add Payments tab to Applications Menu and Scheme Year drop-down
    Given user on login page
    When clicks on new agent login button
    When Enters "aga6128" as new username
    And clicks on Login button
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search for Herd Number "N1142057"
    And Agent Click on the Row with the Client "N1142057"
    Then Agent Click on the View Dashboard Button
    Given  Agent is on BISS Farmer Dashboard Screen
    Then Click on the Agent BISS "Applications / Payments" Tab
    And Agent Check if "schemeYear" Dropdown is Present
    Then Agent Check if "Land Details" tab is present
    Then Agent Check if "Payments" tab is present
    And Agent Click on the "Payments" Tab
    Then Agent Check if "There are no records to display" message displayed





