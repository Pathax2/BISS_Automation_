Feature: TC_06 - Regression Suite for all Bugs and Features in BISS - 1

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Regression coverage for iNet correspondence bugs and fixes in BISS.
  #
  #   Active Scenarios:
  #     AT-TC-01 : Verify 2023 Uploaded / Response Correspondence is visible in iNet / Internal
  #                Covers: login → My Clients → herd search → Correspondence tab →
  #                         document type selection → upload → submit
  #
  #     AT-TC-18 : Verify the View Correspondence hyperlink is functional in iNet 2023
  #                Covers: login → Transfers tab → herd search → View link → View Correspondence link
  #
  # Notes:
  #   1. TC_06 does not use a shared Background because each scenario performs its own
  #      full login with a specific herd — the herds and navigation paths differ between them.
  #   2. Commented scenarios (AT-TC-02 through AT-TC-21) are kept below for historical
  #      traceability and re-enablement when the underlying defects are resolved.
  #   3. Herd numbers used in active scenarios are hardcoded test fixtures — update them
  #      in TestData.xlsx if they become invalid in the target environment.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  @regression
  Scenario: AT-TC-01 - iNet 2023 - Uploaded/Response Correspondence not showing in iNet/Internal
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent searches for herd number "N1090944"
      # N1090812 #N1010991 #N1091932 #N1170280 #N1080655 #N7010276
    And the agent clicks on the row for client "N1090944"
    Then the agent clicks on the View Dashboard button
    And the agent navigates to the "Correspondence" tab on the Side Navigation bar
    Then the agent clicks on the " Upload a document" stepper button
      #When Agent expand Upload Documents Accordion
    And the agent selects "Commonage Evidence" from the "doc-type" dropdown
    And the agent uploads a document in Correspondence
    Then the agent clicks on the "Upload" stepper button
#     Then Check Doc Upload Success

  #@tmslink=BISSAGL-6987
  @regression
  Scenario: AT-TC-18 - BISSAGL-6987 iNet 2023 View Correspondence hyperlink
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent switches to the "Transfers" tab on the My Clients page
    When the agent searches for a transfer herd number "B1410500"
    Then the agent clicks on the View link for the searched herd
    And the agent clicks on the View Correspondence link in the dashboard


  # ------------------------------------------------------------------------------------------------------------------
  # Commented Scenarios — kept for traceability and future re-enablement
  # ------------------------------------------------------------------------------------------------------------------

  #   @regression
  #   Scenario: Convert Data Table to User Defined Type
  #     Given Agent Numbers are taken from Excel File and used as UserName
  #       | Excel            | Location                                       | Sheet  |
  #       | AgentsBatch.xlsx | src/main/resources/TestData/AgentsBatch.xlsx   | Sheet1 |

  #  @tmslink=BISSAGL-7056
  #  # How do I validate Commonage field not present ?
  #  @regression
  #  Scenario: AT-TC-02 - BISSAGL-400 Land Details: Drawer validation: All Parcels require a commonage fraction
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer

  #  @tmslink=BISSAGL-7055
  #  # Need Commonage Parcel to check the same
  #  @regression
  #  Scenario: AT-TC-03 - BISSAGL-400 Land Details: Drawer validation: Parcels commonage fraction Field Tool tip is displaying todays date time
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer

  #  @tmslink=BISSAGL-7054
  #  @regression
  #  Scenario: AT-TC-04 - BISSAGL-400 Land Details: Drawer : displaying [ ) Brackets arround units needs to be ( )
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer
  #    Then Check if round bracket is around unit in Land Details Drawer Parcel

  #  @tmslink=BISSAGL-7053
  #  # Solution - Get Claimed area and compare it with whatever is there in the field in the edit drawer
  #  # Logic still under discussion how to add content to clipboard using automation
  #  @regression
  #  Scenario: AT-TC-05 - BISSAGL-400 Land Details: Drawer : Claimed Area Input field Popup displaying Clipboard contents
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer

  #  # Clarify - No X mark in the warning message, so how do we check this what happens ?
  #  @tmslink=BISSAGL-7052
  #  @regression
  #  Scenario: AT-TC-06 - BISSAGL-400 Land Details: Drawer : Warning msg Displayed cannot be closed with X click
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer

  #  # No editable field for Eligible Hectare how to verify ?
  #  @tmslink=BISSAGL-7042
  #  @regression
  #  Scenario: AT-TC-08 - BISSAGL-400 Land Details: Drawer : displaying No Input field for Eligible Hectare
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer
  #    Then Check if round bracket is around unit in Land Details Drawer Parcel

  #  # Where is the agricultural activity field ?
  #  @tmslink=BISSAGL-7038
  #  @regression
  #  Scenario: AT-TC-09 - BISSAGL-400 Land Details: Drawer validation: Parcels not displaying input field for Ag Activity
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer
  #    Then Check if round bracket is around unit in Land Details Drawer Parcel

  #  # Clarity needed - Commonage Parcel ?
  #  @tmslink=BISSAGL-7037
  #  @regression
  #  Scenario: AT-TC-10 - BISSAGL-400 Land Details: Drawer validation: Parcel commonage fraction of 200/18 should not pass validation
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer
  #    Then Check if round bracket is around unit in Land Details Drawer Parcel

  #  @tmslink=BISSAGL-7031
  #  @regression
  #  Scenario: AT-TC-14 - BISSAGL-395 Land Details: Load list of lands: Organic Column is not displayed although edit parcel drawer shows the parcel to be organic
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on Application Stepper "Back" Button
  #    Then Agent Click on the "Organics" scheme card
  #    Then Agent Click on Application Stepper "Next" Button
  #    Then Agent Click on the "I understand" dialog box button
  #    Then Check if "Organic Status" column is present
  #    Then Agent Click on Application Stepper "Back" Button
  #    Then Agent Click on the "Organics" scheme card
  #    Then Agent Click on Application Stepper "Next" Button
  #    Then Agent Click on the "I understand" dialog box button

  #  @tmslink=BISSAGL-7029
  #  @regression
  #  Scenario: AT-TC-15 - BISSAGL-395 Land Details: Load list of lands: Columns Claimed Area and Ownership status are merged and need to be seperated
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Check if "Claimed Area" column is present
  #    Then Check if "Ownership Status" column is present

  #  @tmslink=BISSAGL-7014
  #  @regression
  #  Scenario: AT-TC-16 - BISSAGL-1365 Side Drawer - Save button is not working unless all missing mandatory information is entered
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    #B1670536
  #    Then Agent Search for Herd Number "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    Then Agent is on BISS Farmer Dashboard Screen
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on Plot Reference "N1050800011" to open Side Drawer
  #    And Agent Clear Value in Side Drawer Claimed Area Field
  #    Then Agent Click On Toggle Button "Previous land" in Side Drawer
  #    And Agent Click on the "Save changes" dialog box button
  #    Then Agent Check Mandatory Info Toast Message Visibility

  #  @tmslink=BISSAGL-7011
  #  @regression
  #  Scenario: AT-TC-17 - BISSAGL-1518 Entitlement Position button missing from Menu Bar
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    And Agent switch to "Transfers" Tab in My Clients Page
  #    When Agent Search for Transfers Herd Number Field and Enter Herd as "B1410500"
  #    Then Agent Click On View Link for Searched Herd
  #    And Agent Check for "Entitlement Position" tab Side Nav Menu

  #  #N1010606
  #  @tmslink=BISSAGL-6976
  #  @regression
  #  Scenario: AT-TC-19 - BISSAGL-6976 iNet 2024 - Open Map Button on Add Parcel Modal
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    When Agent Search for Herd Number Field and Enter Herd as "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    And Agent Click on the "Add parcel" Button in land details page
  #    And Agent Select Value "Clare" in the "county" Add Parcel Or Plot Dialog Dropdown
  #    # Affick - C23901
  #    And Agent Select Value "Addergoole - C11501" in the "townland" Add Parcel Or Plot Dialog Dropdown
  #    Then Agent Click on the "Open Map" dialog box button
  #    Then Agent Click on the Return to Land Details Page Button

  #  @tmslink=BISSAGL-7036
  #  @regression
  #  Scenario: AT-TC-20 - BISSAGL-30 Serial Number warming message remains on screen after its entered & save
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    When Agent Search for Herd Number Field and Enter Herd as "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    Then Agent Click on Application Stepper "Next" Button
  #    Then Agent Click on Application Stepper "Next" Button
  #    Then Agent Click on Application Stepper "Next" Button
  #    And Agent Select the "AP5" Scheme Option in Eco Page
  #    And Agent Selects the "Agri-Spread" Approved Spreader Type from the "selectedSpreaderManufacturer" dropdown
  #    And Agent Selects the "AS65with GPS Control and Isobus" Approved Spreader Type from the "spreaderModel" dropdown
  #    And Agent enters data "A735B78346" in the "spreaderSerialNo" textbox
  #    And Agent Click on Save & Select for "ap5" Scheme in Eco Page
  #    And Agent Select the "AP5" Scheme Option in Eco Page

  #  @tmslink=BISSAGL-7008
  #  @regression
  #  Scenario: AT-TC-21 - CAP 2023 - Eco-Scheme - AP1 issues
  #    Given user on login page
  #    When enters username
  #    And Agent Enters the Pin Number
  #    And enter password
  #    And clicks on Login button
  #    And Click on the Basic Income Support for Sustainability application
  #    Given Agent is on BISS Agent Home Screen
  #    Then Click on the Agent BISS "My Clients" Tab
  #    When Agent Search for Herd Number Field and Enter Herd as "N1050519"
  #    And Agent Click on the Row with the Client "N1050519"
  #    Then Agent Click on the View Dashboard Button
  #    And Agent Click On "Continue application" Farmer Dashboard Button
  #    Then Agent Click on "Land Details" Stepper
  #    Then Agent Click on Application Stepper "Next" Button
  #    Then Agent Click on Application Stepper "Next" Button
  #    Then Agent Click on Application Stepper "Next" Button
  #    And Agent Check Green Tick for Parcel "N1042300011" wich is exempt
