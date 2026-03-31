@Transfers
Feature: Verify the Transfer Application from Individual To Individual Functionality - aga6322

  @tmslink=ENTSAGL-5836
  Scenario: TC_09_Regression_Pack_INDIVIDUAL_ to Individual_Lease of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "SOUTHVIEW1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
#    Then External User Enters sms OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    # RENATENOHL@HOTMAIL.COM 3029508B H2200068 G1660844 2215679I BRENDANCLARKE JOHNPFINN
    # D1760654
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
    # Transferor Process
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    #M2081822 O119072X R126120X D2560055 X1370418 N1211270 I1480302
    And Agent Fill "txeeHerd" field value as "T1060418"
    #Samuel Connolly & Marty Mcgovern, Donal Tiernan, William Hand, Thomas Barton & Gerard Mc Carthy, Michael Reynolds, Bernard O'Rahilly Jnr, Patrick Horan, Jimmy Walsh
    And Agent Fill "txeeName" field value as "Patrick Hoban"
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
    Given user on individual login page
    When clicks on new agent login button
    # 4589015L JOHNBALLYORAN 6838713O TIMOTHY.MURPHY KADAGH.GEOGHEGAN JOHNSHERIDAN89 THOMASMCG
    When Agent Enter "3970723H" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then External User Enters sms OTP
#    Then External User Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
    Then Click on View button in Transferee Dashboard with Herd Number "T1060418"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5835
  Scenario: TC_10_Regression_Pack_INDIVIDUAL_ to Individual_sale of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "RENATENOHL@HOTMAIL.COM" as Individual's Username
    #  3029508B H2200068 G1660844 2215679I BRENDANCLARKE JOHNPFINN
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
    # O119072X R126120X D2560055 X1370418 N1211270 I1480302
    And Agent Fill "txeeHerd" field value as "M2081822"
    # Donal Tiernan, William Hand, Thomas Barton & Gerard Mc Carthy, Michael Reynolds, Bernard O'Rahilly Jnr, Patrick Horan
    And Agent Fill "txeeName" field value as "Mary Ann Ryan & P J Quinn"
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
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "4589015L" as Individual's Username
    #  JOHNBALLYORAN 6838713O TIMOTHY.MURPHY KADAGH.GEOGHEGAN JOHNSHERIDAN89 THOMASMCG
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
    Then Click on View button in Transferee Dashboard with Herd Number "M2081822"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5967
  Scenario: TC_11_Regression_Pack_INDIVIDUAL_ to Individual_gift of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "3029508B" as Individual's Username
    #   H2200068 G1660844 2215679I BRENDANCLARKE JOHNPFINN
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
    #  R126120X D2560055 X1370418 N1211270 I1480302
    And Agent Fill "txeeHerd" field value as "O119072X"
    #  William Hand, Thomas Barton & Gerard Mc Carthy, Michael Reynolds, Bernard O'Rahilly Jnr, Patrick Horan
    And Agent Fill "txeeName" field value as "Louise Cunningham"
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
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "JOHNBALLYORAN" as Individual's Username
    #   6838713O TIMOTHY.MURPHY KADAGH.GEOGHEGAN JOHNSHERIDAN89 THOMASMCG
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
    Then Click on View button in Transferee Dashboard with Herd Number "O119072X"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5966
  Scenario: TC_12_Regression_Pack_INDIVIDUAL_ to Individual_Division of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "H2200068" as Individual's Username
    #    G1660844 2215679I BRENDANCLARKE JOHNPFINN
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
    #   D2560055 X1370418 N1211270 I1480302
    And Agent Fill "txeeHerd" field value as "R126120X"
    #   Thomas Barton & Gerard Mc Carthy, Michael Reynolds, Bernard O'Rahilly Jnr, Patrick Horan
    And Agent Fill "txeeName" field value as "Margt Reid"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "6838713O" as Individual's Username
    #    TIMOTHY.MURPHY KADAGH.GEOGHEGAN JOHNSHERIDAN89 THOMASMCG
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
    Then Click on View button in Transferee Dashboard with Herd Number "R126120X"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5965
  Scenario: TC_13_Regression_Pack_INDIVIDUAL_ to Individual_Merger_of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    #     2215679I BRENDANCLARKE JOHNPFINN
    #BRENDANDOLAN  G1660844
    When Agent Enter "EILEEN.KEENAN" as Individual's Username
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
    #    X1370418 N1211270 I1480302
    And Agent Fill "txeeHerd" field value as "D2560055"
    #    Michael Reynolds, Bernard O'Rahilly Jnr, Patrick Horan
    And Agent Fill "txeeName" field value as "Gerard Hazelwood & Timothy O'Sullivan"
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
    Given user on individual login page
    When clicks on new agent login button
    #     KADAGH.GEOGHEGAN JOHNSHERIDAN89 THOMASMCG
    When Agent Enter "TIMOTHY.MURPHY" as Individual's Username
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
    Then Click on View button in Transferee Dashboard with Herd Number "D2560055"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5964
  Scenario: TC_14_Regression_Pack_INDIVIDUAL_ to Individual_Change of Registration details
    Given user on individual login page
    When clicks on new agent login button
    #      BRENDANCLARKE JOHNPFINN
    When Agent Enter "KARENKELLYWH" as Individual's Username
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
    #     N1211270 I1480302
    And Agent Fill "txeeHerd" field value as "X1370418"
    #     Bernard O'Rahilly Jnr, Patrick Horan
    And Agent Fill "txeeName" field value as "John Burke"
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
    Given user on individual login page
    When clicks on new agent login button
    #      JOHNSHERIDAN89 THOMASMCG
    When Agent Enter "KADAGH.GEOGHEGAN" as Individual's Username
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
    Then Click on View button in Transferee Dashboard with Herd Number "X1370418"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-5963
  Scenario: TC_15_Regression_Pack_INDIVIDUAL_ to Individual_Change of Legal Entity
    Given user on individual login page
    When clicks on new agent login button
    #       JOHNPFINN
    When Agent Enter "BRENDANCLARKE" as Individual's Username
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
    #      I1480302
    And Agent Fill "txeeHerd" field value as "N1211270"
    #      Patrick Horan
    And Agent Fill "txeeName" field value as "Michael Kelleher Jnr"
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
    Given user on individual login page
    When clicks on new agent login button
    #       THOMASMCG
    When Agent Enter "JOHNSHERIDAN89" as Individual's Username
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
    Then Click on View button in Transferee Dashboard with Herd Number "N1211270"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

    # Shows old OTP page
  @tmslink=ENTSAGL-5962
  Scenario: TC_16_Regression_Pack_INDIVIDUAL_ to Individual_Inheritance of Entitlements
    Given user on individual login page
    When clicks on new agent login button
    #JOHNPFINN
    When Agent Enter "BRENDANCLARKE" as Individual's Username
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
    And Agent Fill "txeeHerd" field value as "N1211270"
    And Agent Fill "txeeName" field value as "Michael Kelleher Jnr"
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
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "JOHNSHERIDAN89" as Individual's Username
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
    Then Click on View button in Transferee Dashboard with Herd Number "N1211270"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button
