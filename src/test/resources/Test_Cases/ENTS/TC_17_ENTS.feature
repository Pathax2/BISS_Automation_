Feature: TC-04 Verify Transfer end-to-end Flow

  Background:
    Given user on login page
    When clicks on new agent login button
#    When Agent Enters NRCISYF Agent 1 Username
    When enters "aga6352" as new username
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


  @sanity
  @tmslink=ENTSAGL-9994
  Scenario: Transfer Flow
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "A1020300"
    # J1350023
    Then Agent Click On View Link for Searched Herd
    And Agent Navigate to "Entitlements / Usage" tab on the SideNavBar
    And Agent Gets OwnerID of herd
    And Agent Navigate to "Transfers" tab on the SideNavBar
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "A1010126"
    And Agent Fill "txeeName" field value as "Maureen Wynne"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "206" as Transfer Type
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
    # Transferee Process
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "Transfers" Tab in My Clients Page
    When Agent Search for Herd Number Field and Enter Herd as "A1010126"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "A1020300"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button
    Given user on staff login page
    When Login with the Username "AGR2214"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on ENTS link
    When Staff Search for Herd Number Field and Enter Herd "A1020300"
    And Staff clicks on Search Button
    And Staff clicks on OwnerID link
    And Staff click on year "2026" from left navigation bar
    And Staff click on "Transfers" tab
    #And Staff Verfies "Begin Data Capture" button is present

