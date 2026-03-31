@Transfers
Feature: Verify the Transfer Application from ETF To Individual Functionality - agr15512
# Fixed
  Background:
    Given user on partner login page
    When clicks on new agent login button
#    When Agent Enter "agr15512" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    #8714472S agr15512
    When Login with the Partner Username "agr15512"
    And clicks on partner Login button
    And enter password
    And clicks on partner Login button
    Then Partner Enters MS authenticator OTP
    And clicks on partner Login button
#    And Enter the number as "0837291245" after login
#    And clicks on partner Login button
#    Then Partner Enter "111111" as the one time code
#    And clicks on partner Login button
    And Click on the Basic Income Support for Sustainability application

  @tmslink=ENTSAGL-8190
  Scenario: TC_09_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _CLE
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
    # J1360207 B1320063
    When Agent Search for Herd Number Field and Enter Herd as "J1350147"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "V2050144"
#    And Agent Fill "txeeName" field value as "Oliver Carmody"
#    And Agent Fill "txeeAddress" field value as "Carralavin Ave"
    And Agent Fill "txeeHerd" field value as "Y104069X"
    And Agent Fill "txeeName" field value as "Daniel Mulvany"
    And Agent Fill "txeeAddress" field value as "Carrickedmond Heights"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "206" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "8714472S" as Individual's Username
    When Agent Enter "PAUDYFROG" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y104069X"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8189
  Scenario: TC_10_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _Change of Registration
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "J1350457"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "V2040726"
#    And Agent Fill "txeeName" field value as "Brendan Cox"
#    And Agent Fill "txeeAddress" field value as "Churchtown Grove"
    And Agent Fill "txeeHerd" field value as "Y1041344"
    And Agent Fill "txeeName" field value as "Felim Sullivan"
    And Agent Fill "txeeAddress" field value as "Drumhoe Avenue"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "205" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "V2040726" as Individual's Username
    When Agent Enter "TERENCE1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y1041344"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8188
  Scenario: TC_11_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _Lease of Entitlements
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "J1691122"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "K1203872"
#    And Agent Fill "txeeName" field value as "James O/'Riordan"
#    And Agent Fill "txeeAddress" field value as "Clonmore Park"
    And Agent Fill "txeeHerd" field value as "Y1041344"
    And Agent Fill "txeeName" field value as "Felim Sullivan"
    And Agent Fill "txeeAddress" field value as "Drumhoe Avenue"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "211" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Select Lease End Year as "2026" from the Entitlement Type dropdown
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "PEARCEK127" as Individual's Username
    When Agent Enter "TERENCE1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y1041344"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8187
  Scenario: TC_12_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _Gift of Entitlements
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "J1350457"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "V2041129"
#    And Agent Fill "txeeName" field value as "Margt Walsh & Justin Byrne"
#    And Agent Fill "txeeAddress" field value as "Per Lodge"
    And Agent Fill "txeeHerd" field value as "Y1310159"
    And Agent Fill "txeeName" field value as "Seamus Carolan"
    And Agent Fill "txeeAddress" field value as "Unit Grove"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "202" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "MCASEY123" as Individual's Username
    When Agent Enter "DANIELPAUL" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y1310159"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8186
  Scenario: TC_13_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _ Merger of Entitlements
   # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "J1691122"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "E215167X"
#    And Agent Fill "txeeName" field value as "Kieran Daly"
#    And Agent Fill "txeeAddress" field value as "Kilconlea Lawn"
    And Agent Fill "txeeHerd" field value as "Y104069X"
    And Agent Fill "txeeName" field value as "Daniel Mulvany"
    And Agent Fill "txeeAddress" field value as "Carrickedmond Heights"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "203" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "BOYDLAURA" as Individual's Username
    When Agent Enter "PAUDYFROG" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y104069X"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8185
  # Issue with individual side login need to verify the same
  Scenario: TC_14_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _ Division of Entitlements
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "V2631112"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "V2020253"
#    And Agent Fill "txeeName" field value as "Boland Foods Ltd"
#    And Agent Fill "txeeAddress" field value as "Ballynattin Heights"
    And Agent Fill "txeeHerd" field value as "Y1041344"
    And Agent Fill "txeeName" field value as "Felim Sullivan"
    And Agent Fill "txeeAddress" field value as "Drumhoe Avenue"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "204" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "RIICHIEFOGARTY@AGFOOD.IE" as Individual's Username
    When Agent Enter "TERENCE1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y1041344"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8184
  Scenario: TC_15_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _ Inheritance of Entitlements
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "J1691122"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "E2150593"
#    And Agent Fill "txeeName" field value as "Lawrence Hogan"
#    And Agent Fill "txeeAddress" field value as "Kilmacshane"
    And Agent Fill "txeeHerd" field value as "Y1041344"
    And Agent Fill "txeeName" field value as "Felim Sullivan"
    And Agent Fill "txeeAddress" field value as "Drumhoe Avenue"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "201" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "SWORKAN67" as Individual's Username
    When Agent Enter "TERENCE1" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y1041344"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8183
  Scenario: TC_16_Regression_Test_Suite_ETF(Authorized by Agent) to Individual _ Sale of Entitlements
    # Transferor Process
#    When Agent Search for Herd Number Field and Enter Herd as "V2040386"
#    When Agent Search for Herd Number Field and Enter Herd as "B1320063"
    When Agent Search for Herd Number Field and Enter Herd as "V2631112"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "E2150518"
#    And Agent Fill "txeeName" field value as "Edward O/'Leary"
#    And Agent Fill "txeeAddress" field value as "Drumkielvey"
    And Agent Fill "txeeHerd" field value as "Y1310159"
    And Agent Fill "txeeName" field value as "Seamus Carolan"
    And Agent Fill "txeeAddress" field value as "Unit Grove"
    Then Agent Click on the Dialog Box "Search" button
    Then Agent select "212" as Transfer Type
    And Agent Click on the " Next " button
    Then Agent Click on Add Manual Entitlements Button
    Then Agent Select Entitlement Type as "BISS" from the Entitlement Type dropdown
    And Agent Fill "itsNumEntsTx" field value as "0.01"
    And Agent Fill "itsNetUv" field value as "1"
    Then Agent Click on the Dialog Box "Add" button
    And Agent Click on the " Next " button
    And Agent Enter Transfer Notes as "Test Notes"
    When Agent Click on the " Send to Transferee for Acceptance " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Send for Acceptance " button
    Then Agent Capture Transfer Key in Summary Screen
    And Click on Exit BISS Link
    And Click on Logout Button
    Given user on individual login page
    When clicks on new agent login button
#    When Agent Enter "AGFOODRH" as Individual's Username
    When Agent Enter "DANIELPAUL" as Individual's Username
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Individual Enters sms OTP
    And clicks on Continue button
    Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Given Agent is on BISS Agent Home Screen
    Then Click on the Agent BISS "Transfers" Tab
#    Then Click on View Button in Individual Transferee Screen with Herd Number "V204XXXX"
#    Then Click on View Button in Individual Transferee Screen with Herd Number "B132XXXX"
    Then Click on View button in Transferee Dashboard with Herd Number "Y1310159"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button


















