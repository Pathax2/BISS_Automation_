@Transfers
Feature: Verify the Transfer Application from ETF To Agent Functionality - agr15515
# Fixed
  Background:
    Given user on partner login page
    When clicks on new agent login button
#    When Agent Enter "agr15515" as Trasferee Username
#    And Agent Enters the Pin Number
#    And enter password
#    And clicks on Login button
    #8714472S
    When Login with the Partner Username "agr15678"
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

  @tmslink=ENTSAGL-8673
  Scenario: TC_01_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _CLE
    # Transferor Process
    # V2040386
    # C1050271
    # B1841250
    When Agent Search for Herd Number Field and Enter Herd as "D1391004"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "B1331812"
#    And Agent Fill "txeeName" field value as "Richard Garry"
#    And Agent Fill "txeeAddress" field value as "Toomona"
    # B1331812 G1861980
    And Agent Fill "txeeHerd" field value as "B136017X"
    # Richard Garry
    And Agent Fill "txeeName" field value as "Darragh Staunton"
    # Toomona
    And Agent Fill "txeeAddress" field value as "Knockeevan Park"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "B136017X"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "D139XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8672
  Scenario: TC_02_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Change of Reg
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "D1190050"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
    And Agent Fill "txeeHerd" field value as "B1470189"
    And Agent Fill "txeeName" field value as "Simon Thornton"
    And Agent Fill "txeeAddress" field value as "Tullycleave St"
#    And Agent Fill "txeeHerd" field value as "J1810504"
#    And Agent Fill "txeeName" field value as "Thomas Gill"
#    And Agent Fill "txeeAddress" field value as "Cooga Hill"
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
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "B1470189"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "B147XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8671
  Scenario: TC_03_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Lease_Of_Entitlements
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "J1350309"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "J1980051"
#    And Agent Fill "txeeName" field value as "Michael Bourke"
#    And Agent Fill "txeeAddress" field value as "Kildalton Grove"
    And Agent Fill "txeeHerd" field value as "B1870136"
    And Agent Fill "txeeName" field value as "Michael Mc Cullagh"
    And Agent Fill "txeeAddress" field value as "Oranmore Pk"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "B1870136"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "B187XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8669
  Scenario: TC_04_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Gift_Of_Entitlements
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "D1391004"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "J2040532"
#    And Agent Fill "txeeName" field value as "Anthony O/'Keeffe"
#    And Agent Fill "txeeAddress" field value as "Aughurine Avenue"
    And Agent Fill "txeeHerd" field value as "D1460081"
    And Agent Fill "txeeName" field value as "Timothy Mangan"
    And Agent Fill "txeeAddress" field value as "Ballybuggy Lawn"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "D1460081"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "D146XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8668
  Scenario: TC_05_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Merger of Entitlements
   # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "J1350309"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "J2040656"
#    And Agent Fill "txeeName" field value as "John Tom Grennan"
#    And Agent Fill "txeeAddress" field value as "Culmore Rd"
    And Agent Fill "txeeHerd" field value as "C2057024"
    And Agent Fill "txeeName" field value as "Laoise Murphy"
    And Agent Fill "txeeAddress" field value as "Arcella Road"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "C2057024"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "C205XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8667
  Scenario: TC_06_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Division of Entitlements
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "D1190246"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "J1810334"
#    And Agent Fill "txeeName" field value as "Reps. Of Mary F Farrell"
#    And Agent Fill "txeeAddress" field value as "Annamoe Abbey"
    And Agent Fill "txeeHerd" field value as "B1470189"
    And Agent Fill "txeeName" field value as "Simon Thornton"
    And Agent Fill "txeeAddress" field value as "Tullycleave St"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "B1470189"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "B147XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8664
  Scenario: TC_07_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Inheritance of Entitlements
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "J1360045"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "J2080020"
#    And Agent Fill "txeeName" field value as "John Murphy"
#    And Agent Fill "txeeAddress" field value as "Per Lawn"
    And Agent Fill "txeeHerd" field value as "B1870136"
    And Agent Fill "txeeName" field value as "Michael Mc Cullagh"
    And Agent Fill "txeeAddress" field value as "Oranmore Pk"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "B1870136"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "B187XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button

  @tmslink=ENTSAGL-8663
  Scenario: TC_08_Regression_Test_Suite_ETF(Authorized by Agent) to Agent _Sale of Entitlements
    # Transferor Process
    When Agent Search for Herd Number Field and Enter Herd as "J1350309"
    Then Agent Click On View Link for Searched Herd
    When Agent Click on the "Create Transfer Application" button
    Then Agent Click on Transfer Type "Search" Button
#    And Agent Fill "txeeHerd" field value as "J2080631"
#    And Agent Fill "txeeName" field value as "Reps. Of Paul O/'Brien"
#    And Agent Fill "txeeAddress" field value as "Mayour Grove"
    And Agent Fill "txeeHerd" field value as "D2760615"
    And Agent Fill "txeeName" field value as "Griffin Meats Ltd"
    And Agent Fill "txeeAddress" field value as "Killnadrain Ave"
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
    #And Click on the My Account Button
    And Click on Exit BISS Link
    And Click on Logout Button
    # Transferee Process
    Given user on login page
    When clicks on new agent login button
    When enters "aga6581" as new username
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
    When Agent Search for Herd Number Field and Enter Herd as "D2760615"
    Then Agent Click On View Link for Searched Herd
    Then Click on View button in Transferee Dashboard with Herd Number "D276XXXX"
    And Agent Fill "inputtedTransferKey" field value with Transfer Key
    Then Agent Click on the Dialog Box "View Transfer Application" button
    And Agent Enter Transfer Notes as "Approved Test"
    When Agent Click on the " Submit Application to DAFM " button
    And Agent Click On Terms and Conditions CheckBox
    Then Agent Click on the Dialog Box " Submit Application " button
