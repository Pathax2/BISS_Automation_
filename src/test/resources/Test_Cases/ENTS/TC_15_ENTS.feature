Feature: TC-02 NR/CISYF end-to-end flow

  @sanity
  @tmslink=ENTSAGL-9920
  Scenario: TC01 - Applying NRCISYF Individual and check in ENTSCore
    Given user on login page
    When clicks on new agent login button
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
    And Agent switch to "NR/CISYF" Tab in My Clients Page
    And Collect All Herds of that agent which are not submitted save it to excel file
    And Agent Search for Herd Number Field and Enter Herd from row 3
    Then Agent Click On View Link for Searched Herd
    And Agent Navigate to "Entitlements / Usage" tab on the SideNavBar
    And Agent Gets OwnerID of herd
    And Agent Navigate to "NR-CISYF" tab on the SideNavBar
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Qualifications certificate or Confirmation of Education Form" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Personal and Sensitive Documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the NRCISYF Stepper "Save and Next" button
    Then Agent Click on the NRCISYF "Save and Next" button
    Then Agent Click on the NRCISYF "Submit Application" button
    And Agent Click on checkbox 1 in the Submission Declaration dialog
    And Agent Click on checkbox 2 in the Submission Declaration dialog
    Then Agent Click On NRCISYF "Submit Application" Button in Dialog Box
    Given user on staff login page
    When Login with the Username "AGR2214"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Click on ENTS link
    And Staff Search for Herd Number Field and Enter Herd from row 3
    And Staff clicks on Search Button
    And Staff clicks on OwnerID link
    And Staff click on year "2026" from left navigation bar
    And Staff click on "NR / CISYF" tab
   # And Staff Verfies "Begin Data Capture" button is present


