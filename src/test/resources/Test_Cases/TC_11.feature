Feature: TC_11 - BISS_25.3.Sprint_5 TC1 for BISSAGL-21062 BISS iNET - Shift Document Retrieval Logic to document service

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Validates that when a staff user sends multiple correspondence letters to a herd,
  #   the agent is able to see the latest 5 letters in the Recent Correspondence frame
  #   on the Agent Dashboard — confirming the document retrieval logic routes correctly
  #   through the document service.
  #
  #   Active Scenarios:
  #     AT-TC-00 : Agent-side precondition
  #                Navigates to My Clients, applies the Submitted filter, and selects
  #                a submitted herd as the test fixture for the main scenario.
  #
  #     Main     : Staff sends 5 Free Text Letters via the Query Letters flow,
  #                then the agent verifies those letters appear in Recent Correspondence
  #                and can be viewed from the farmer dashboard.
  #
  # Notes:
  #   1. TC_11 has no shared Background — the two scenarios use different login flows
  #      (agent for AT-TC-00, staff + agent for the main scenario).
  #   2. The staff user "agr4442" is a fixed test account. Update in TestData.xlsx if needed.
  #   3. The letter is submitted 5 times in sequence to trigger the volume check in the
  #      Recent Correspondence frame. Comments mark each repetition for clarity.
  #   4. The last step verifying letters from the Correspondence tab is currently
  #      commented pending investigation.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  #@tmslink=BISSAGL-7010
  @regression
  Scenario: AT-TC-00 - Agent selects a submitted herd as the test fixture
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent applies the "Submitted" quick filter
    And the agent picks a herd from the list

  @regression
  Scenario: Shift Document Retrieval Logic to document service
    Given the staff user is on the staff login page
    When the staff user logs in with username "agr4442"
    And the staff user selects the data protection checkbox
    And the staff user opens the "Basic Income Support for Sustainability" application
    And the staff user searches for a herd from row 1 with scheme year "2025"
    And the staff user clicks on the "View" link in the application table at position 1
    Then the staff user navigates to "Correspondence" in the side navigation bar
    And the staff user switches to the "Query Letter" tab in Correspondence
    And the staff user checks for any inspection error
    # Letter 1
    And the staff user selects "BISS Free Text Letter" from the "docType" query letter dropdown
    And the staff user types "Test Heading" in the "Enter letter heading" query letter text area
    And the staff user types "Test Body" in the "Enter letter body" query letter text area
    And the staff user clicks the "Preview" button
    And the staff user clicks the "Submit" button
    And the staff user confirms by clicking the "Yes, Confirm" button
    # Letter 2
    And the staff user selects "BISS Free Text Letter" from the "docType" query letter dropdown
    And the staff user types "Test Heading" in the "Enter letter heading" query letter text area
    And the staff user types "Test Body" in the "Enter letter body" query letter text area
    And the staff user clicks the "Preview" button
    And the staff user clicks the "Submit" button
    And the staff user confirms by clicking the "Yes, Confirm" button
    # Letter 3
    And the staff user selects "BISS Free Text Letter" from the "docType" query letter dropdown
    And the staff user types "Test Heading" in the "Enter letter heading" query letter text area
    And the staff user types "Test Body" in the "Enter letter body" query letter text area
    And the staff user clicks the "Preview" button
    And the staff user clicks the "Submit" button
    And the staff user confirms by clicking the "Yes, Confirm" button
    # Letter 4
    And the staff user selects "BISS Free Text Letter" from the "docType" query letter dropdown
    And the staff user types "Test Heading" in the "Enter letter heading" query letter text area
    And the staff user types "Test Body" in the "Enter letter body" query letter text area
    And the staff user clicks the "Preview" button
    And the staff user clicks the "Submit" button
    And the staff user confirms by clicking the "Yes, Confirm" button
    # Letter 5
    And the staff user selects "BISS Free Text Letter" from the "docType" query letter dropdown
    And the staff user types "Test Heading" in the "Enter letter heading" query letter text area
    And the staff user types "Test Body" in the "Enter letter body" query letter text area
    And the staff user clicks the "Preview" button
    And the staff user clicks the "Submit" button
    And the staff user confirms by clicking the "Yes, Confirm" button

    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent can see the "Recent Correspondence" frame on the Agent Dashboard
    And the agent verifies the latest 5 letters are visible in Recent Correspondence
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent selects the submitted herd from row 1
    And the agent clicks on the View Dashboard button
    Then the agent navigates to the "Correspondence" tab on the Side Navigation bar
#   And the agent verifies latest 5 letters received by herds
