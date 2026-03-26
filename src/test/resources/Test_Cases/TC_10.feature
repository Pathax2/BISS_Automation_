Feature:  BISS_25.3.Sprint_3 TC2 for BISSAGL-20695 reference number Agent User

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
  Scenario: reference number Agent User
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "No Herd Number" Tab in My Clients Page
    Then Agent Click on Application Stepper " Create Client " Button
    And Agent enter text "Kale" in the "name" field
    And Agent enter text "Address1" in the "add1" field
    And Agent enter text "Address2" in the "add2" field
    And Agent enter text "Address3" in the "add3" field
    And Agent Select "county" dropdown value as " Carlow "
    And Agent enter text "D12345" in the "eircode" field
    And Agent enter text "123456789" in the "contactNumber" field
    And Agent tick " BISS " checkbox
    And Agent enter text "DXH1234" in the "herdNumber" field
    Then Agent Click on Application Stepper "Create Client" Button
    Then Agent Click on Application Stepper "I understand" Button
    Then Agent Click on Application Stepper "Edit " Button
    Then Agent Click on Application Stepper "Close" Button
    Then Agent Click on Application Stepper " Create Client " Button
    And Agent enter text "Kale" in the "name" field
