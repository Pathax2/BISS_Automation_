@Transfers
Feature: Verify the Transfer Application within same Agent Functionality - aga6327

  Background:
    Given user on login page
    When clicks on new agent login button
#    When Agent Enters NRCISYF Agent 1 Username
    When Agent Enters New Agent 3 Username for Transfers
    And clicks on Continue button
#    And Agent Enters the Pin Number
    And enter password
#    Then Agent Enters 1 as the OTP
#    And clicks on Login button
    And clicks on Continue button
    Then External User Enters sms OTP
    And clicks on Continue button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "Transfers" Tab in My Clients Page

  @tmslink=ENTSAGL-7084
  Scenario: TC_06_Regression_Pack_Same Agent_Merger of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process J1350350 V225069X  V1870644
    When Agent Search for Herd Number Field and Enter Herd as "H114113X"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #V1911022 V2010592 V1920064
    And Agent Fill "txeeHerd" field value as "H1360568"
    #Gearoid Flynn Maria Fleming Martin Egan
    And Agent Fill "txeeName" field value as "Walsh Farm"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "203" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Transferor Signature Confirmation" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
  # Transferee Process J1400195 John Edward Alcorn
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "Transfers" Tab in My Clients Page
    When Agent Search for Herd Number Field and Enter Herd as "H1360568"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "H136XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button


#  Scenario: Negative case for Herd without Entitlements
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V1861254"
#    Then Agent Click On View Link for Searched Herd
#    When Agent Click on the "Create Transfer Application" button
#    Then Agent Click on Transfer Type "Search" Button
#    #K1410720
#    And Agent Fill "txeeHerd" field value as "V1880640"
#    #Patrick Mcsweeney
#    And Agent Fill "txeeName" field value as "Michael Mullin"
#    Then Agent Click on the Dialog Box "Search" button
#    Then Agent select "203" as Transfer Type
#    And Agent Click on the " Next " button
#    Then Agent Check Add Entitlement Button not present
