@Transfers
Feature: Verify the Transfer Application from Individual To Agent Functionality

  @tmslink=ENTSAGL-5859
  Scenario: TC_01_Regression_Pack_ Individual_ to_Agent _Change of Legal Entity
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "FARMBLUE" as Individual's Username
    # JAMESSKEHILL EUGENECARR CATHERINEMORAN2 HNOW1280483 ANGELAMCGLYNN JHARKIN1 B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #K1472440 K105146X Z1180657 Z115020X Z1190016 Z1334069 Z2170744 K1021138
    And Agent Fill "txeeHerd" field value as "Z1114042"
    #Liam Kenny, Fay Suppliers, Patrick Scott, Richard O'Riordan, Noel Slattery, Stan Doyle, Nora Meyns, McNamara Consultancy
    And Agent Fill "txeeName" field value as "Owens Mart Ltd"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z1114042"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z1114042"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5858
  Scenario: TC_02_Regression_Pack_ Individual_to_agent_Inheritance of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "JAMESSKEHILL" as Individual's Username
    #  EUGENECARR CATHERINEMORAN2 HNOW1280483 ANGELAMCGLYNN JHARKIN1 B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # K105146X Z1180657 Z115020X Z1190016 Z1334069 Z2170744 K1472440
    And Agent Fill "txeeHerd" field value as "Z2170744"
    #, Fay Suppliers, Patrick Scott, Richard O'Riordan, Noel Slattery, Stan Doyle, Nora Meyns, Liam Kenny
    And Agent Fill "txeeName" field value as "Hugh Patrick Gore"
    Then Agent Click on the Dialog Box "Search" button
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z2170744"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z2170744"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5857
  Scenario: TC_03_Regression_Pack_Individual_ to_Agent_Gift of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "EUGENECARR" as Individual's Username
    #   CATHERINEMORAN2 HNOW1280483 ANGELAMCGLYNN JHARKIN1 B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #  Z1180657 Z115020X Z1190016 Z1334069 Z2170744 K105146X
    And Agent Fill "txeeHerd" field value as "Z1180657"
    #, , Patrick Scott, Richard O'Riordan, Noel Slattery, Stan Doyle, Nora Meyns, Fay Suppliers
    And Agent Fill "txeeName" field value as "Patrick Keenan"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z1180657"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z1180657"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5856
  Scenario: TC_04_Regression_Pack_Individual_ to_Agent _ Lease of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "CATHERINEMORAN2" as Individual's Username
    #    HNOW1280483 ANGELAMCGLYNN JHARKIN1 B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #   Z115020X Z1190016 Z1334069 Z2170744 Z1180657
    And Agent Fill "txeeHerd" field value as "Z1190016"
    #, , , Richard O'Riordan, Noel Slattery, Stan Doyle, Nora Meyns, Patrick Scott
    And Agent Fill "txeeName" field value as "Brendan Fitzjohn"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z1190016"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z1190016"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5855
  Scenario: TC_05_Regression_Pack_Individual_ to_Agent _ Merger of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "HNOW1280483" as Individual's Username
    #     ANGELAMCGLYNN JHARKIN1 B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #    Z1190016 Z1334069 Z2170744 Z115020X
    And Agent Fill "txeeHerd" field value as "Z1270559"
    #, , , , Noel Slattery, Stan Doyle, Nora Meyns, Richard O'Riordan
    And Agent Fill "txeeName" field value as "Thomas Cooke"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z1270559"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z1270559"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5854
  Scenario: TC_06_Regression_Pack_Individual_ to_Agent_ Division of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "ANGELAMCGLYNN" as Individual's Username
    #      JHARKIN1 B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #     Z1334069 Z2170744 Z1190016
    And Agent Fill "txeeHerd" field value as "Z1330942"
    #, , , , , Stan Doyle, Nora Meyns, Noel Slattery
    And Agent Fill "txeeName" field value as "Joanne Moroney"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z1330942"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z1330942"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5853
  Scenario: TC_07_Regression_Pack_Individual_ to_Agent _ Sale of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "JHARKIN1" as Individual's Username
    #       B1090840
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #      Z2170744 Z1334069
    And Agent Fill "txeeHerd" field value as "Z1334069"
    #, , , , , , Nora Meyns, Stan Doyle
    And Agent Fill "txeeName" field value as "Ronan O'Neill"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z1334069"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z1334069"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5845
  Scenario: TC_08_Regression_Pack_ Individual_ to_Agent _Change of Registration details
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "B1090840" as Individual's Username
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
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    # Z2170744
    And Agent Fill "txeeHerd" field value as "Z2120933"
    # Nora Meyns
    And Agent Fill "txeeName" field value as "Richard Keenan"
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
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on login page
    When clicks on new agent login button
    # Transferee Process
    When enters "aga6325" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "Z2120933"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "Z2120933"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button
