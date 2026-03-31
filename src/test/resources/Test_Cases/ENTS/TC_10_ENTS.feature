@Transfers
Feature: Verify the Transfer Application from Individual To ETF Functionality

  @tmslink=ENTSAGL-5961
  Scenario: TC_17_Regression Test Pack_ Individual to ETF_ Lease of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "SOUTHVIEW1" as Individual's Username
    # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # C1741649 Senan Crowley
    # G1861980 Kathleen Deely
    # B1221022 Clare Killilea
    And Agent Fill "txeeHerd" field value as "D1051289"
    And Agent Fill "txeeName" field value as "Browne Exports Ltd"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "211" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    When Agent Select the Lease Year
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "D1051289"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5960
  Scenario: TC_18_Regression Test Pack_ Individual to ETF_ gift of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "TERENCE1" as Individual's Username
    #  MERVSTEP1 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # D127094X Brid Keogh
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "D1190050"
    And Agent Fill "txeeName" field value as "Thomas Treanor"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "202" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "D1190050"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5959
  Scenario: TC_19_Regression Test Pack_ Individual to ETF_ sales of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "DANIELPAUL" as Individual's Username
    #   4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # D1340817 James Heneghan
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "D1190246"
    And Agent Fill "txeeName" field value as "Oliver Griffin"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "212" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "D1190246"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5958
  Scenario: TC_20_Regression Test Pack_ Individual to ETF_inheritance of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "SOUTHVIEW1" as Individual's Username
    #    IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # D1900928 Eugene Bligh
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "J1350309"
    And Agent Fill "txeeName" field value as "Cadden Suppliers"
    Then Agent Click on the Dialog Box "Search" button
#    And Agent select the Start End Date for herd "D1900928"
    Then Agent select "201" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
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

  @tmslink=ENTSAGL-5957
  Scenario: TC_21_Regression Test Pack_ Individual to ETF_ merger of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "TERENCE1" as Individual's Username
    #     PADDYMORGAN TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # D195C519 Sanders Mart Ltd
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "J1360045"
    And Agent Fill "txeeName" field value as "Nestor Exports Ltd"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "203" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
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

  @tmslink=ENTSAGL-5956
# Leased entitlements can only be transferred by Change of Registration, Change of Legal Entity, Merger or Division of Entitlements (Scission)
  Scenario: TC_22_Regression Test Pack_ Individual to ETF_ division of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    # PADDYMORGAN
    When Agent Enter "DANIELPAUL" as Individual's Username
    #      TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # D204062X Seamus Nevin
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "D1051289"
    And Agent Fill "txeeName" field value as "Browne Exports Ltd"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "204" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "D1051289"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5955
  Scenario: TC_23_Regression Test Pack_ Individual to ETF_Change of Registration Details of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "TERENCE1" as Individual's Username
    #      TRAYNORA
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "D1190050"
    And Agent Fill "txeeName" field value as "Thomas Treanor"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "205" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
    When Login with the Partner Username "agr15678"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    When Agent Search for Herd Number Field and Enter Herd as "D1190050"
    Then Agent Click On View Link for Searched Herd
    #Then Click on View button in Transferee Dashboard with Herd Number "J1390696"
    Then AgentClick On ETF Button
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5954
  Scenario: TC_24_Regression Test Pack_ Individual to ETF_Change of Legal Entity of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "DANIELPAUL" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # D2200466 Seamus Duffy
    # G1861980 Kathleen Deely
    And Agent Fill "txeeHerd" field value as "J1360045"
    And Agent Fill "txeeName" field value as "Nestor Exports Ltd"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "206" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on First Add Entitlement Button for Transferor
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
#    Then Agent Click On "Transferor Confirmation Signature Form " Link in Transfer Summary Page
    And Agent Click on the " Upload Document " button
    Then Agent Select from "selectedDocumentType" dropdown the doctype "Companies Registrations Office (Company Printout)" to Upload for Transfers
    And Upload Document for Transfers
    Then Agent Click on the Dialog Box " Upload Document " button
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    # Transferee Process
    And Click on Exit BISS Link
    And Click on Logout Button
    # What is the username ?
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    Given user on partner login page
    When clicks on new agent login button
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

