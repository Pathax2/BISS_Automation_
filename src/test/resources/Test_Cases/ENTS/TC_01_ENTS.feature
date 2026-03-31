@Transfers
Feature: Verify the Transfer Application within same Agent Functionality - aga6077

  Background:
    Given user on login page
    When clicks on new agent login button
#    When Agent Enters NRCISYF Agent 1 Username
    When Agent Enters new NRCISYF Agent 1 Username
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


  @tmslink=ENTSAGL-7077
  Scenario: TC_01_Regression_Pack_Same Agent_Change of Legal Entity
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1010091"
    # J1350023
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1010148"
    And Agent Fill "txeeName" field value as "Thomas Costelloe"
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
    When Agent Search for Herd Number Field and Enter Herd as "C1010148"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1010148"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7078
  Scenario: TC_02_Regression_Pack_Same Agent_Change of Registration details
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1040578"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1010334"
    And Agent Fill "txeeName" field value as "Nicola Lambe"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "201" as Transfer Type
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
    When Agent Search for Herd Number Field and Enter Herd as "C1010334"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1010334"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7081
  Scenario: TC_03_Regression_Pack_Same Agent_Inheritance of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1040578"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1037011"
    And Agent Fill "txeeName" field value as "Kathleen Doherty"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "201" as Transfer Type
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
    When Agent Search for Herd Number Field and Enter Herd as "C1037011"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1037011"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7082
  Scenario: TC_04_Regression_Pack_Same Agent_Gift of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1040578"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1010121"
    And Agent Fill "txeeName" field value as "Elizabeth Bradfield"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "202" as Transfer Type
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
    When Agent Search for Herd Number Field and Enter Herd as "C1010121"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1010121"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7083
  Scenario: TC_05_Regression_Pack_Same Agent_Lease of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1040578"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1050166"
    And Agent Fill "txeeName" field value as "Peter Kelly"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "211" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    When Agent Select the Lease Year
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
    When Agent Search for Herd Number Field and Enter Herd as "C1050166"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1050166"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7085
  Scenario: TC_07_Regression_Pack_Same Agent_Division of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1010776"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1050212"
    And Agent Fill "txeeName" field value as "Padraig Costello"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "204" as Transfer Type
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
    When Agent Search for Herd Number Field and Enter Herd as "C1050212"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1050212"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7086
  Scenario: TC_08_Regression_Pack_same Agent_Sale of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    #V1570101
    When Agent Search for Herd Number Field and Enter Herd as "C1010776"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "C1070124"
    And Agent Fill "txeeName" field value as "Michael Duignan"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "212" as Transfer Type
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
    When Agent Search for Herd Number Field and Enter Herd as "C1070124"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C1070124"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

