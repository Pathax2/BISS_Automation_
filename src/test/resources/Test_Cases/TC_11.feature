Feature:  BISS_25.3.Sprint_5 TC1 for BISSAGL-21062 BISS iNET - Shift Document Retrieval Logic to document service

  @regression
  Scenario: AT-TC-00 - Queries required for BISSAGL-301
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And Agent Select " Submitted " Quick Filter
    And Agent picks submitted herd in list

  #@tmslink=BISSAGL-7010
  @regression
  Scenario: Shift Document Retrieval Logic to document service
    Given user on staff login page
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
    And Staff Click on the Basic Income Support for Sustainability application
    Then Staff Search Herd Number from row 1
    Then Staff Select "schemeYear" dropdown value as "2025"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1
    Then Staff Click on "Correspondence" Link in Navbar
    And Staff switch to the " Query Letter" tab in Correspondence Header
    And Staff Checks if Inspection error
    Then Staff Change the "docType" query letter dropdown value as " BISS Free Text Letter "
    And Staff Type Text as "Test Heading" in "Enter letter heading" Query Letters TextArea
    And Staff Type Text as "Test Body" in "Enter letter body" Query Letters TextArea
    Then Staff Click on the "Preview" button
    Then Staff Click on the " Submit " button
    Then Staff Click on the "Yes, Confirm" button
    #Letter 2nd time
    Then Staff Change the "docType" query letter dropdown value as " BISS Free Text Letter "
    And Staff Type Text as "Test Heading" in "Enter letter heading" Query Letters TextArea
    And Staff Type Text as "Test Body" in "Enter letter body" Query Letters TextArea
    Then Staff Click on the "Preview" button
    Then Staff Click on the " Submit " button
    Then Staff Click on the "Yes, Confirm" button
    #Letter 3rd time
    Then Staff Change the "docType" query letter dropdown value as " BISS Free Text Letter "
    And Staff Type Text as "Test Heading" in "Enter letter heading" Query Letters TextArea
    And Staff Type Text as "Test Body" in "Enter letter body" Query Letters TextArea
    Then Staff Click on the "Preview" button
    Then Staff Click on the " Submit " button
    Then Staff Click on the "Yes, Confirm" button
    #Letter 4th time
    Then Staff Change the "docType" query letter dropdown value as " BISS Free Text Letter "
    And Staff Type Text as "Test Heading" in "Enter letter heading" Query Letters TextArea
    And Staff Type Text as "Test Body" in "Enter letter body" Query Letters TextArea
    Then Staff Click on the "Preview" button
    Then Staff Click on the " Submit " button
    Then Staff Click on the "Yes, Confirm" button
    #Letter 5th time
    Then Staff Change the "docType" query letter dropdown value as " BISS Free Text Letter "
    And Staff Type Text as "Test Heading" in "Enter letter heading" Query Letters TextArea
    And Staff Type Text as "Test Body" in "Enter letter body" Query Letters TextArea
    Then Staff Click on the "Preview" button
    Then Staff Click on the " Submit " button
    Then Staff Click on the "Yes, Confirm" button
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    And Agent can see "Recent Correspondence" frame on the Agent Dashboard
    And Agent verifies latest 5 letters received by herds
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search Submitted Herd Number from row 1
    Then Agent Click on the View Dashboard Button
    Then Staff Click on "Correspondence" Link in Navbar
#    And Agent verifies latest 5 letters received by herds
