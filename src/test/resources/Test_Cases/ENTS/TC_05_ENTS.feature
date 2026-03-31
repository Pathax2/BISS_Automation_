@Transfers
Feature: Verify the Transfer Application from Agent To Individual Functionality - aga6532

  Background:
    Given user on login page
    When clicks on new agent login button
    When Agent Enters new NRCISYF Agent 1 Username
    #When Agent Enters New Agent 5 Username for Transfers
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

  @tmslink=ENTSAGL-7095
  Scenario: TC_17_Regression_Pack_ Agent to Individual_Change of Registration details
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process G1570420 G1311319
    When Agent Search for Herd Number Field and Enter Herd as "C1120016"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "Y104069X"
    # Patrick Bonner Padraig Gleeson
    And Agent Fill "txeeName" field value as "Daniel Mulvany"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "205" as Transfer Type
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "PAUDYFROG" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
#    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
    Then Click on View button in Transferee Dashboard with Herd Number "Y104069X"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7096
  Scenario: TC_18_Regression_Pack_Agent to Individual_Inheritance of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1120016"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "Y104069X"
    #Norman Lehane
    And Agent Fill "txeeName" field value as "Daniel Mulvany"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "PAUDYFROG" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
#    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
    Then Click on View button in Transferee Dashboard with Herd Number "Y104069X"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7097
  Scenario: TC_19_Regression_Pack_Agent to Individual_Gift of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1120016"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "Y1041344"
    # John Duggan Bridie McCormack
    And Agent Fill "txeeName" field value as "Felim Sullivan"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "TERENCE1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
#    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
    Then Click on View button in Transferee Dashboard with Herd Number "Y1041344"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7098
  Scenario: TC_20_Regression_Pack_Agent to Individual _ Lease of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "C1120016"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "Y1041344"
    # Bgt Mahony Mel O/'Brien
    And Agent Fill "txeeName" field value as "Felim Sullivan"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "TERENCE1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
#    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
    Then Click on View button in Transferee Dashboard with Herd Number "Y1041344"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

