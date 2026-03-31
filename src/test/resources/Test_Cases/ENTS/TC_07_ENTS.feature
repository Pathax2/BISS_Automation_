@Transfers
Feature: Verify the Transfer Application from Agent To ETF Functionality - aga6322

  Background:
    Given user on login page
    When clicks on new agent login button
    When Agent Enters New Agent 1 Username for Transfers
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


  @tmslink=ENTSAGL-7104
  Scenario: TC_25_Regression_Pack_ Agent to ETF_Change of Registration details
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O1321027
    When Agent Search for Herd Number Field and Enter Herd as "J1390696"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1314060"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Teresa Noone"
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
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
    When Login with the Partner Username "agr15594"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1314060"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7105
  Scenario: TC_26_Regression_Pack_Agent to ETF_Inheritance of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O1321035
    When Agent Search for Herd Number Field and Enter Herd as "J1400039"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1350023"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Frehill Suppliers"
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
    Given user on partner login page
    When clicks on new agent login button
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15594"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1350023"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7106
  Scenario: TC_28_Regression_Pack_Agent to ETF_Gift of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O132405X J1400101
    When Agent Search for Herd Number Field and Enter Herd as "J1410417"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1360045"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Nestor Exports Ltd"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "202" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
#    And Agent Enter Transfer Notes as "Test Notes"
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
    Given user on partner login page
    When clicks on new agent login button
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1360045"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7107
  Scenario: TC_29_Regression_Pack_Agent to ETF _ Lease of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O1324115
    When Agent Search for Herd Number Field and Enter Herd as "J1400195"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1350309"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Cadden Suppliers"
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
    Given user on partner login page
    When clicks on new agent login button
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1350309"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7108
  Scenario: TC_30_Regression_Pack_Agent to ETF _ Merger of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O1324131
    When Agent Search for Herd Number Field and Enter Herd as "J1400217"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1350147"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Sean Lally"
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
    # Transferee Process
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on partner login page
    When clicks on new agent login button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15512"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1350147"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7109
  Scenario: TC_31_Regression_Pack_Agent to ETF _ Division of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O1324158
    When Agent Search for Herd Number Field and Enter Herd as "J140033X"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "V2631112"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Nora White"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on partner login page
    When clicks on new agent login button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15512"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "V2631112"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7110
  Scenario: TC_32_Regression_Pack_Agent to ETF _ Sale of Entitlements
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O1330247
    When Agent Search for Herd Number Field and Enter Herd as "J1400519"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1350147"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Sean Lally"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on partner login page
    When clicks on new agent login button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15512"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1350147"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-7919
  Scenario: TC_33_Regression_Pack_Agent to ETF_Change of Legal Entity
    Given  Agent is on ENTS Farmer Dashboard Screen
    # Transferor Process O132078X
    When Agent Search for Herd Number Field and Enter Herd as "J1350350"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #B1220255 G1861980
    And Agent Fill "txeeHerd" field value as "J1350457"
    #Kathleen Deely
    And Agent Fill "txeeName" field value as "Michael Gerard Gargan"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on partner login page
    When clicks on new agent login button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    When Login with the Partner Username "agr15512"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "J1350457"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button


