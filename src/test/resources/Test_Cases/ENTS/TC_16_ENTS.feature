@NRCISYF
Feature: NR/CISYF Regression Pack

  Background:
    Given user on login page
    When clicks on new agent login button
    When Agent Enters new NRCISYF Agent 2 Username
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


  Scenario: TC00 Get herds for the cases and store it in a file
    Given  Agent is on ENTS Farmer Dashboard Screen
    And Collect All Herds of that agent which are not submitted save it to excel file


  @tmslink=ENTSAGL-7495
  Scenario: TC01 - Checking important information msg on landing page
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#   # And Get Agent from Database with 80 herds and put in the "username" field in login page
#    And enter password
#    And clicks on Continue button
#    #And Check if Agent is Active in 80 herds in the "username" field
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
    Given Agent Search for Herd Number Field and Enter Herd from row 1
   # When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Then Agent Click On View Link for Searched Herd

  @tmslink=ENTSAGL-7496
  Scenario: TC02 - Closing date Passed alert Warning msg should be displayed
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    #And Get Agent from Database with 80 herds and put in the "username" field in login page
#   # And Get agent of row 1 from Herd List and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    And Agent Check if "15 May 2026" is NRCISYF close date

  @tmslink=ENTSAGL-7497
  Scenario: TC03 - Selecting Category 'A' For Individual Application
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    #And Agent Select "1" "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF Close button

  @tmslink=ENTSAGL-7498
  Scenario: TC04 - Selecting Category 'B' for Individual Application
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
 #   When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    #And Agent Select "2" "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog
    And Agent Check Status of "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF Close button

  @tmslink=ENTSAGL-7499
  Scenario: TC05 - Selecting Category 'C' for Individual Application
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
   # And Agent Select "3" "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog
    Then Agent Click on the NRCISYF Close button

  @tmslink=ENTSAGL-7500
  Scenario: TC06 - Selecting Category 'A' and 'C' for Individual Application
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF Close button

  @tmslink=ENTSAGL-7501
  Scenario: TC07 - Selecting Category 'B' and 'C' for Individual Application
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Agent Check Status of "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF Close button

  @tmslink=ENTSAGL-7510
  Scenario: TC08 - Selecting Category 'A' for Joint Herdowner with 1 Group member
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "1"

  @tmslink=ENTSAGL-7511
  Scenario: TC09 - Selecting Category 'A' for Joint Herdowner with 2 Group member
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "2"

  @tmslink=ENTSAGL-7512
  Scenario: TC10 - Selecting Category 'A' for Joint Herdowner with 3 Group member
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "3"

  @tmslink=ENTSAGL-7513
  Scenario: TC11 - Selecting Category 'A' for Joint Herdowner with 4 Group member
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "4"

  @tmslink=ENTSAGL-7514
  Scenario: TC12 - Selecting Category 'A' for Joint Herdowner with 5 Group member
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "5"

  @tmslink=ENTSAGL-7515
  Scenario: TC13 - Selecting Category 'A' for Joint Herdowner with 6 Group member
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "6"

  @tmslink=ENTSAGL-8627
  Scenario: TC14 - Applying - Category 'A' for an Individual NR Applications
#    When clicks on new agent login button
#    And Get 4 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 4 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
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
  #  Then Agent Click On NRCISYF "Submit Application" Button in Dialog Box

  @tmslink=ENTSAGL-8626
  Scenario: TC15 - Applying - Category 'A' & 'C' for an Individual NR Applications
#    When clicks on new agent login button
#    And Get 6 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 6 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Upload Invoice/Receipt" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Upload Bank Statement Extract with Invoice/Receipt Passing through" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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
  #  Then Agent Click On NRCISYF "Submit Application" Button in Dialog Box

  @tmslink=ENTSAGL-8624
  Scenario: TC16 - Applying - Category 'B' for an Individual NR Applications
#    When clicks on new agent login button
#    And Get 6 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 6 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Dundalk Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "Teagasc Diploma in Pig Production" for the "qualId" dropdown
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
    #Then Agent Click On NRCISYF "Submit Application" Button in Dialog Box

  @tmslink=ENTSAGL-8619
  Scenario: TC17 - Applying - Category 'B' & 'C' for an Individual NR Applications
#    When clicks on new agent login button
#    And Get 6 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 6 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Upload Invoice/Receipt" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Upload Bank Statement Extract with Invoice/Receipt Passing through" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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
   # Then Agent Click On NRCISYF "Submit Application" Button in Dialog Box

  @tmslink=ENTSAGL-8618
  Scenario: TC18 - Applying - Category 'C' for an Individual NR Applications
#    When clicks on new agent login button
#    And Get 6 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 6 in the "Address, Name or Herd Number" field
 #    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Upload Invoice/Receipt" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Upload Bank Statement Extract with Invoice/Receipt Passing through" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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
   # Then Agent Click On NRCISYF "Submit Application" Button in Dialog Box

  @tmslink=ENTSAGL-8617
  Scenario: TC19 - Msg display after clicking on Info icon for category A
#    When clicks on new agent login button
#    And Get 9 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 9 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    Then Agent Click on Info Button "1" from Category Dialog

  @tmslink=ENTSAGL-8616
  Scenario: TC20 - Msg display after clicking on Info icon for category B
#    When clicks on new agent login button
#    And Get 9 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 9 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    Then Agent Click on Info Button "2" from Category Dialog

  @tmslink=ENTSAGL-8615
  Scenario: TC21 - Msg display after clicking on Info icon for category C
#    When clicks on new agent login button
#    And Get 9 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 9 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    Then Agent Click on Info Button "3" from Category Dialog

  @tmslink=ENTSAGL-8614
  Scenario: TC22 - Closing select category popup dialog box
#    When clicks on new agent login button
#    And Get 9 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 9 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    Then Agent Click on the NRCISYF Close button

  @tmslink=ENTSAGL-8613
  Scenario: TC23 - Error msg display after Selecting invalid Category combination (A then B)
#    When clicks on new agent login button
#    And Get 9 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 9 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Error Message for B

  @tmslink=ENTSAGL-8612
  Scenario: TC24 - Error msg display after Selecting invalid Category combination (B then A)
#    When clicks on new agent login button
#    And Get 10 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 10 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "B. National Reserve (as New Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Error Message for A

  # Clarify last step about clicking Back button
  @tmslink=ENTSAGL-8611
  Scenario: TC25 - Select additional CISYF category
#    When clicks on new agent login button
#    And Get 10 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 10 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Select additional CISYF category and continue" button
    Then Agent Click on the NRCISYF "OK" button

  @tmslink=ENTSAGL-8610
  Scenario: TC26 - Saving current application details
#    When clicks on new agent login button
#    And Get 10 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 10 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
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
    Then Agent Click on the NRCISYF Stepper "Save and Exit" button
    Then Agent Click on the NRCISYF "Save and Exit" button

  @tmslink=ENTSAGL-8609
  Scenario: TC27 - Check on uploaded documents in correspondence
#    When clicks on new agent login button
#    And Get 71 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 71 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Dundalk Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC/QQI Level 6 Advanced Certificate in Stud Management" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Upload Invoice/Receipt" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Upload Bank Statement Extract with Invoice/Receipt Passing through" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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
    And Agent click on "Correspondence" Navbar Button

  # Doc file not being shown so invalid case only PDF being shown for upload
  @tmslink=ENTSAGL-8608
  Scenario: TC28 - Error msg display after uploading incorrect format of the documents
#    When clicks on new agent login button
#    And Get 14 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 14 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 1
    Then Agent Click On View Link for Searched Herd

  @tmslink=ENTSAGL-8607
  Scenario: TC29 - Closing upload popup msg
#    When clicks on new agent login button
#    And Get 14 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 14 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 2
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
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
    #Then Agent Click on the NRCISYF Stepper "Close" button

  @tmslink=ENTSAGL-8605
  Scenario: TC30 - Check on info delaying course completion by Covid19 - Depleted Case to be removed after informing
#    When clicks on new agent login button
#    And Get 15 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 15 in the "Address, Name or Herd Number" field
#    When Agent Search for Herd Number Field and Enter Herd as "J1350511"
    Given Agent Search for Herd Number Field and Enter Herd from row 2
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "No" option for the "selectedValueQualification" Question
#    And Agent Select "Yes" option for the "selectedValuecovid19Delayed" Question - Removed
#    Then Agent Check Covid Message

  @tmslink=ENTSAGL-8604
  Scenario: TC31 - Downloading uploaded documents using links
    #Added code to submit an application
    Given Agent Search for Herd Number Field and Enter Herd from row 3
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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
    Then Click on the Agent BISS "My Clients" Tab
    And Agent switch to "NR/CISYF" Tab in My Clients Page
    Given Agent Search for Herd Number Field and Enter Herd from row 3
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF "View Application" button
    And Agent Click On "Education Documentation" Document Link

  @tmslink=ENTSAGL-8603
  Scenario: TC32 - Education Institution does not appear in the list
#    When clicks on new agent login button
#    And Get 14 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 14 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J197039X"
    Given Agent Search for Herd Number Field and Enter Herd from row 5
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "The educational institution I attended does not appear in this list" for the "colId" dropdown
    Then Agent Enter Value as "Dublin Institute of Technology" in the "educationalInstitute" field
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    And Agent Verify if "Dublin Institute of Technology" is Educational Institution

  @tmslink=ENTSAGL-8602
  Scenario: TC33 - Qualification does not appear in the list
#    When clicks on new agent login button
#    And Get 17 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 17 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1970137"
    Given Agent Search for Herd Number Field and Enter Herd from row 6
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as " My qualification does not appear in this list" for the "qualId" dropdown
    Then Agent Enter Value as "Qualification 1" in the "qualification" field
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    And Agent Verify if "Qualification 1" is Qualification

  # Test Case Title and Test Case Mismatch
  @tmslink=ENTSAGL-8599
  Scenario: TC34 - Edit the application after save and exit
#    When clicks on new agent login button
#    And Get 18 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 18 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1450222"
    Given Agent Search for Herd Number Field and Enter Herd from row 7
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Individual"
    Then Agent Click on the NRCISYF Stepper "Next" button
    Then Agent Click on the NRCISYF Stepper "Save and Exit" button
    Then Agent Click on the NRCISYF "Save and Exit" button

  @tmslink=ENTSAGL-8378
  Scenario: TC35 - Applying - Category 'A' - Company - 1 Group member
#    When clicks on new agent login button
#    And Get 72 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 72 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1390033"
    Given Agent Search for Herd Number Field and Enter Herd from row 8
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8377
  Scenario: TC36 - Applying - Category 'A' - Company - 2 Group member
#    When clicks on new agent login button
#    And Get 20 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 20 in the "Address, Name or Herd Number" field
  #  When Agent Search for Herd Number Field and Enter Herd as "J1370377"
    Given Agent Search for Herd Number Field and Enter Herd from row 9
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8376
  Scenario: TC37 - Applying - Category 'A' - Company - 3 Group member
#    When clicks on new agent login button
#    And Get 21 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 21 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1390033"
    Given Agent Search for Herd Number Field and Enter Herd from row 10
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8375
  Scenario: TC38 - Applying - Category 'A' - Company - 4 Group member
#    When clicks on new agent login button
#    And Get 22 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 22 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1390092"
    Given Agent Search for Herd Number Field and Enter Herd from row 11
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8374
  Scenario: TC39 - Applying - Category 'A' - Company - 5 Group member
#    When clicks on new agent login button
#    And Get 23 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 23 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1390173"
    Given Agent Search for Herd Number Field and Enter Herd from row 12
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "8"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8373
  Scenario: TC40 - Applying - Category 'A' - Company - 6 Group member
#    When clicks on new agent login button
#    And Get 24 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 24 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1390262"
    Given Agent Search for Herd Number Field and Enter Herd from row 13
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "8"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member6" in field "9"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8372
  Scenario: TC41 - Applying - Category 'A' for SHRFP with Individual Group Type
#    When clicks on new agent login button
#    And Get 24 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 24 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1390637"
    Given Agent Search for Herd Number Field and Enter Herd from row 14
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Individual"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8371
  Scenario: TC42 - Applying - Category 'A' - SHRFP - JH - 1 Group Member
#    When clicks on new agent login button
#    And Get 26 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 26 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1410107"
    Given Agent Search for Herd Number Field and Enter Herd from row 15
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8370
  Scenario: TC43 - Applying - Category 'A' - SHRFP - JH - 2 Group Member
#    When clicks on new agent login button
#    And Get 24 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 24 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J140033X"
    Given Agent Search for Herd Number Field and Enter Herd from row 16
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8369
  Scenario: TC44 - Applying - Category 'A' - SHRFP - JH - 3 Group Member
#    When clicks on new agent login button
#    And Get 74 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 74 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1400519"
    Given Agent Search for Herd Number Field and Enter Herd from row 17
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8368
  Scenario: TC45 - Applying - Category 'A' - SHRFP - JH - 4 Group Member
#    When clicks on new agent login button
#    And Get 75 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 75 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1360169"
    Given Agent Search for Herd Number Field and Enter Herd from row 18
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8367
  Scenario: TC46 - Applying - Category 'A' - SHRFP - JH - 5 Group Member
#    When clicks on new agent login button
#    And Get 30 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 30 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1360096"
    Given Agent Search for Herd Number Field and Enter Herd from row 19
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member4" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8366
  Scenario: TC47 - Applying - Category 'A' - SHRFP - JH - 6 Group Member
#    When clicks on new agent login button
#    And Get 32 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 32 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1360517"
    Given Agent Search for Herd Number Field and Enter Herd from row 20
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member4" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member4" in field "6"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8365
  Scenario: TC48 - Applying - Category 'A' - SHRFP - Company - 1 Group Member
#    When clicks on new agent login button
#    And Get 76 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 76 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1480326"
    Given Agent Search for Herd Number Field and Enter Herd from row 21
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8364
  Scenario: TC49 - Applying - Category 'A' - SHRFP - Company - 2 Group Member
#    When clicks on new agent login button
#    And Get 36 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 36 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1480814"
    Given Agent Search for Herd Number Field and Enter Herd from row 22
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8363
  Scenario: TC50 - Applying - Category 'A' - SHRFP - Company - 3 Group Member
#    When clicks on new agent login button
#    And Get 37 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 37 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1481004"
    Given Agent Search for Herd Number Field and Enter Herd from row 23
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8362
  Scenario: TC51 - Applying - Category 'A' - SHRFP - Company - 4 Group Member
#    When clicks on new agent login button
#    And Get 38 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 38 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1481101"
    Given Agent Search for Herd Number Field and Enter Herd from row 24
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8361
  Scenario: TC52 - Applying - Category 'A' - SHRFP - Company - 5 Group Member
#    When clicks on new agent login button
#    And Get 77 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 77 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1481128"
    Given Agent Search for Herd Number Field and Enter Herd from row 25
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "8"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8360
  Scenario: TC53 - Applying - Category 'A' - SHRFP - Company - 6 Group Member
#    When clicks on new agent login button
#    And Get 41 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 41 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1470045"
    Given Agent Search for Herd Number Field and Enter Herd from row 26
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Single-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "8"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member6" in field "9"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8359
  Scenario: TC54 - Applying - Category 'A' for MHRFP with Individual Group Type
#    When clicks on new agent login button
#    And Get 43 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 43 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2020183"
    Given Agent Search for Herd Number Field and Enter Herd from row 27
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Individual"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8358
  Scenario: TC55 - Applying - Category 'A' - MHRFP - JH - 1 Group Member
#    When clicks on new agent login button
#    And Get 44 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 44 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J202031X"
    Given Agent Search for Herd Number Field and Enter Herd from row 28
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8357
  Scenario: TC56 - Applying - Category 'A' - MHRFP - JH - 2 Group Member
#    When clicks on new agent login button
#    And Get 46 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 46 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2020345"
    Given Agent Search for Herd Number Field and Enter Herd from row 29
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8356
  Scenario: TC57 - Applying - Category 'A' - MHRFP - JH - 3 Group Member
#    When clicks on new agent login button
#    And Get 47 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 47 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2020353"
    Given Agent Search for Herd Number Field and Enter Herd from row 30
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8355
  Scenario: TC58 - Applying - Category 'A' - MHRFP - JH - 4 Group Member
#    When clicks on new agent login button
#    And Get 49 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 49 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2020370"
    Given Agent Search for Herd Number Field and Enter Herd from row 31
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8354
  Scenario: TC59 - Applying - Category 'A' - MHRFP - JH - 5 Group Member
#    When clicks on new agent login button
#    And Get 50 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 50 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2060029"
    Given Agent Search for Herd Number Field and Enter Herd from row 32
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member4" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8353
  Scenario: TC60 - Applying - Category 'A' - MHRFP - JH - 6 Group Member
#    When clicks on new agent login button
#    And Get 51 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 51 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2060037"
    Given Agent Search for Herd Number Field and Enter Herd from row 33
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Joint herdowner"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member4" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member4" in field "6"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8352
  Scenario: TC61 - Applying - Category 'A' - MHRFP - Company - 1 Group Member
#    When clicks on new agent login button
#    And Get 52 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 52 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2060193"
    Given Agent Search for Herd Number Field and Enter Herd from row 34
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8351
  Scenario: TC62 - Applying - Category 'A' - MHRFP - Company - 2 Group Member
#    When clicks on new agent login button
#    And Get 53 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 53 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2060207"
    Given Agent Search for Herd Number Field and Enter Herd from row 35
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8350
  Scenario: TC63 - Applying - Category 'A' - MHRFP - Company - 3 Group Member
#    When clicks on new agent login button
#    And Get 54 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 54 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2060223"
    Given Agent Search for Herd Number Field and Enter Herd from row 36
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8349
  Scenario: TC64 - Applying - Category 'A' - MHRFP - Company - 4 Group Member
#    When clicks on new agent login button
#    And Get 55 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 55 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2070016"
    Given Agent Search for Herd Number Field and Enter Herd from row 37
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8348
  Scenario: TC65 - Applying - Category 'A' - MHRFP - Company - 5 Group Member
#    When clicks on new agent login button
#    And Get 56 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 56 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J2070245"
    Given Agent Search for Herd Number Field and Enter Herd from row 38
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "8"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8347
  Scenario: TC66 - Applying - Category 'A' - MHRFP - Company - 6 Group Member
#    When clicks on new agent login button
#    And Get 57 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 57 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J2100063"
    Given Agent Search for Herd Number Field and Enter Herd from row 39
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "23315673" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "6"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "7"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "8"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member6" in field "9"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8345
  Scenario: TC67 - Applying - Category 'A' - JH - 1 Group member
#    When clicks on new agent login button
#    And Get 58 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 58 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1500289"
    Given Agent Search for Herd Number Field and Enter Herd from row 40
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Upload Bank Statements" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8344
  Scenario: TC68 - Applying - Category 'A' - JH - 2 Group member
#    When clicks on new agent login button
#    And Get 59 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 59 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1420820"
    Given Agent Search for Herd Number Field and Enter Herd from row 41
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
     ##**Commented out cauz there's no Next Farmer button**##
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8343
  Scenario: TC69- Applying - Category 'A' - JH - 3 Group member
#    When clicks on new agent login button
#    And Get 60 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 60 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1430019"
    Given Agent Search for Herd Number Field and Enter Herd from row 42
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    ##**Commented out cauz there's no Next Farmer button**##
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8342
  Scenario: TC70 - Applying - Category 'A' - JH - 4 Group member
#    When clicks on new agent login button
#    And Get 61 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 61 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1430094"
    Given Agent Search for Herd Number Field and Enter Herd from row 43
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    ##**Commented out cauz there's no Next Farmer button**##
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8341
  Scenario: TC71 - Applying - Category 'A' - JH - 5 Group member
#    When clicks on new agent login button
#    And Get 78 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 78 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1430175"
    Given Agent Search for Herd Number Field and Enter Herd from row 44
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8342
  Scenario: TC72 - Applying - Category 'A' - JH - 6 Group member
#    When clicks on new agent login button
#    And Get 79 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 79 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J143028X"
    Given Agent Search for Herd Number Field and Enter Herd from row 45
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "Do not select CISYF category and continue" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member6" in field "6"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8324
  Scenario: TC74 - Applying - Category 'A' & 'C' - JH - 1 Group member
#    When clicks on new agent login button
#    And Get 64 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 64 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1570414"
    Given Agent Search for Herd Number Field and Enter Herd from row 46
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "1"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Upload Bank Statements" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8323
  Scenario: TC75 - Applying - Category 'A' & 'C' - JH - 2 Group member
#    When clicks on new agent login button
#    And Get 80 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 80 in the "Address, Name or Herd Number" field
   # When Agent Search for Herd Number Field and Enter Herd as "J1640072"
    Given Agent Search for Herd Number Field and Enter Herd from row 47
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8322
  Scenario: TC76 - Applying - Category 'A' & 'C' - JH - 3 Group member
#    When clicks on new agent login button
#    And Get 66 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 66 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J1910231"
    Given Agent Search for Herd Number Field and Enter Herd from row 48
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "3"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8321
  Scenario: TC77 - Applying - Category 'A' & 'C' - JH - 4 Group member
#    When clicks on new agent login button
#    And Get 67 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 67 in the "Address, Name or Herd Number" field
#    #When Agent Search for Herd Number Field and Enter Herd as "J191032X"
    Given Agent Search for Herd Number Field and Enter Herd from row 49
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "4"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
#    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
#    And Agent Select "Yes" option for the "selectedValueQualification" Question
#    Then Agent Enter Date of completion as "1"
#    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
#    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
#    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8320
  Scenario: TC78 - Applying - Category 'A' & 'C' - JH - 5 Group member
#    When clicks on new agent login button
#    And Get 68 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 68 in the "Address, Name or Herd Number" field
#   # When Agent Search for Herd Number Field and Enter Herd as "J1920091"
    Given Agent Search for Herd Number Field and Enter Herd from row 50
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "5"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8319
  Scenario: TC79 - Applying - Category 'A' & 'C' - JH - 6 Group member
#    When clicks on new agent login button
#    And Get 69 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 69 in the "Address, Name or Herd Number" field
    #When Agent Search for Herd Number Field and Enter Herd as "J192052X"
    Given Agent Search for Herd Number Field and Enter Herd from row 51
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Joint herdowner"
    And Agent Select the Number of Members dropdown value as "6"
    Then Agent Enter Value as "Member1" in field "1"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "2"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    Then Agent Enter Value as "Member3" in field "3"
    Then Agent Enter Date of Birth in DatePicker 3 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "3" and Tick
    Then Agent Enter Value as "Member4" in field "4"
    Then Agent Enter Date of Birth in DatePicker 4 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "4" and Tick
    Then Agent Enter Value as "Member5" in field "5"
    Then Agent Enter Date of Birth in DatePicker 5 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "5" and Tick
    Then Agent Enter Value as "Member6" in field "6"
    Then Agent Enter Date of Birth in DatePicker 6 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "6" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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

  @tmslink=ENTSAGL-8315
  Scenario: TC80 - Applying - Category 'A' & 'C'- Company - 2 Group Member
#    When clicks on new agent login button
#    And Get 70 Agent for Herd from DataBase and put in the "username" field in login page
#    And clicks on Continue button
#    And enter password
#    And clicks on Continue button
#    Then External User Enters sms OTP
#    And clicks on Continue button
#    And Click on the Basic Income Support for Sustainability application
#    Given Agent is on BISS Agent Home Screen
#    Then Click on the Agent BISS "My Clients" Tab
#    And Agent switch to "NR/CISYF" Tab in My Clients Page
#    Given  Agent is on ENTS Farmer Dashboard Screen
#    When Agent Enter Value as Herd Number with no NR_CISYF submission from row 70 in the "Address, Name or Herd Number" field
#    #When Agent Search for Herd Number Field and Enter Herd as "J2060207"
    Given Agent Search for Herd Number Field and Enter Herd from row 52
    Then Agent Click On View Link for Searched Herd
    Then Agent Click on the NRCISYF Apply or Edit button
    And Uncheck all options in the Dialog box
    And Agent Check Status of "A. National Reserve (as Young Farmer)" CheckBox Option in the Dialog and Select
    And Agent Check Status of "C. Complementary income Support for Young Farmers" CheckBox Option in the Dialog and Select
    Then Agent Click on the NRCISYF " Next " button
    Then Agent Click on the NRCISYF "OK" button
    And Agent Select the Farming Entity dropdown value as "Multi-Herd Registered Farm Partnership"
    And Agent Select the Herd Group Type dropdown value as "Company"
    Then Agent Enter Value as "324556789" in field "1"
    Then Agent Enter Value as "Company 1" in field "2"
    Then Agent Enter Value as "Mary Donald" in field "3"
    And Agent Select the Number of Members for Joint HerdOwner dropdown value as "2"
    Then Agent Enter Value as "Member1" in field "4"
    Then Agent Enter Date of Birth in DatePicker 1 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "1" and Tick
    Then Agent Enter Value as "Member2" in field "5"
    Then Agent Enter Date of Birth in DatePicker 2 as "1" "FEB" "2003"
    And Agent Check Status of Eligible Framer CheckBox "2" and Tick
    And Agent Select "Yes" option on Status Page for the "radiogroup" Question
    Then Agent Click on the NRCISYF Stepper "Next" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next Farmer" button
    And Agent Select "Yes" option for the "selectedValueQualification" Question
    Then Agent Enter Date of completion as "1"
    And Agent Select "Yes" option for the "certificateHasBeenAwarded" Question
    And Agent Select Value as "Athlone Institute of Technology" for the "colId" dropdown
    And Agent Select Value as "FETAC Certificate in Farming" for the "qualId" dropdown
    Then Agent Click on the NRCISYF Stepper "Next to Summary" button
    Then Agent Click on the "Eligible Farmer(s) Birth or Marriage Certificate documentation" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the "Declaration of Effective Control and Decision Making Power (download" Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the " Upload Invoice/Receipt " Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the " Upload Bank Statement Extract with Invoice/Receipt Passing through " Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
    Then Agent Click on the " Please upload the relevant Agricultural Qualification at Level 6 or equivalent on the National Framework of Qualifications certificate or Confirmation of Education Form " Upload Button
    And Upload Document for NRCISYF
    Then Agent Click on the NRCISYF "Upload Document" button
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
