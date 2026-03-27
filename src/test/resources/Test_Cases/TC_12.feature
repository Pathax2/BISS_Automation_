Feature: TC_12 - BISS_25.4_Sprint_2 TC1 for BISSAGL-21680 Login Page

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Full regression coverage of the BISS Login page for both Individual and Agent user types.
  #
  #   Scenarios:
  #     AT-TC-01 : Individual user — positive login flow
  #     AT-TC-02 : Individual user — negative login flow, UI element verification
  #     AT-TC-03 : Agent user — positive login flow
  #     AT-TC-04 : Agent user — negative login flow with incorrect username, UI element verification
  #     AT-TC-05 : Agent Dashboard frame and icon verification after login
  #     AT-TC-06 : Display My Clients screen after login
  #     AT-TC-07 : Sort My Clients by Name and Herd Number columns
  #     AT-TC-08 : Pagination — items per page dropdown verification
  #     AT-TC-09 : Quick filter buttons on My Clients page
  #     AT-TC-10 : Row accordion expansion — Submitted herd
  #     AT-TC-11 : Row accordion expansion — Not Started herd
  #     AT-TC-12 : Search by herd number and clear search
  #     AT-TC-13 : Search by invalid characters — no results shown
  #     AT-TC-14 : Export to Excel from My Clients page
  #
  # Notes:
  #   1. Each scenario manages its own login — no shared Background is used because
  #      individual and agent login flows differ and not all scenarios need the same
  #      entry point.
  #   2. Individual username "MERVSTEP1" is a fixed test account.
  #      # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
  #   3. The "Close" personal details dialog step is currently commented pending
  #      investigation into when the dialog appears.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  @regression
  Scenario: AT-TC-01 - Individual Login page — positive flow
    Given the individual user is on the individual login page
    When the individual user enters "MERVSTEP1" as their username
    # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And the agent logs into the application with valid credentials and OTP
    #Then Agent Click on the "Close" personal details dialog issue
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page

  @regression
  Scenario: AT-TC-02 - Individual Login page — negative flow and UI element verification
    Given the individual user is on the individual login page
    When the individual user enters "MERVST" as their username
    # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And the agent clicks the Login button
    And the agent enters the password
    And the agent clicks the Login button
    Then the login error message is displayed
    And the user clicks the Cancel button
    And the user verifies the "Privacy Statement" link is visible
    And the user verifies the "Need Help" link is visible
    When the individual user enters "6858291D" as their username
    And the agent clicks the Login button
    And the agent enters the password
    And the password field eye icon is visible
    And the user verifies the "Forgot Password" link is visible
    And the user verifies the "Back to Login" element is visible
    And the user verifies the "terms-link" element with label "Terms" is visible
    And the user verifies the "helpdesk-link" element with label "Helpdesk" is visible
    And the agent clicks the Login button
    Then the agent completes OTP verification
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page

  @regression
  Scenario: AT-TC-03 - Agent Login page — positive flow
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page

  @regression
  Scenario: AT-TC-04 - Agent Login page — negative flow and UI element verification
    Given the agent user is on the login page
    When the agent enters an incorrect username
    And the agent clicks the Login button
    And the agent enters the password
    And the agent clicks the Login button
    Then the login error message is displayed
    And the user clicks the Cancel button
    And the user verifies the "Privacy Statement" link is visible
    And the user verifies the "Need Help" link is visible
    When the agent enters the valid username
    And the agent clicks the Login button
    # And Agent Enters the Pin Number
    And the agent enters the password
    And the password field eye icon is visible
    And the user verifies the "Forgot Password" link is visible
    And the user verifies the "Cancel" element is visible
    And the user verifies the "terms-link" element with label "Terms" is visible
    And the user verifies the "helpdesk-link" element with label "Helpdesk" is visible
    And the agent clicks the Login button
    Then the agent completes OTP verification
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page

  @regression
  Scenario: AT-TC-05 - Agent Dashboard frames and icons
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent can see the "BISS Applications 2026" frame on the Agent Dashboard
    And the agent can see the "Quick Links" frame on the Agent Dashboard
    And the agent can see the "News" frame on the Agent Dashboard
    And the agent can see the "Recent Correspondence" frame on the Agent Dashboard
    Then the agent navigates to the "Home" tab
    And the agent verifies the "contact-us" icon is present on the Dashboard
    And the agent verifies the "help" icon is present on the Dashboard
    And the agent verifies the "profile" icon is present on the Dashboard

  @regression
  Scenario: AT-TC-06 - Display My Clients screen
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link

  @regression
  Scenario: AT-TC-07 - Sort My Clients screen by Name and Herd Number
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    Then the agent sorts the "Name" column in ascending and descending order
    And the agent sorts the "Herd Number" column in ascending and descending order

  @regression
  Scenario: AT-TC-08 - Pagination of My Clients screen
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent verifies each page size option from the items per page dropdown

  @regression
  Scenario: AT-TC-09 - Filtering of My Clients screen
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent applies the "Not Started" quick filter
    And the agent applies the "Draft" quick filter
    And the agent applies the "Submitted" quick filter
    And the agent applies the "View all" quick filter
    And the agent applies the "Herd Expired" quick filter

  @regression
  Scenario: AT-TC-10 - Row Accordion — Submitted Herd
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent applies the "Submitted" quick filter
    When the agent expands the accordion for any row using the "keyboard_arrow_down" icon
    Then the agent can see the Payment Details section
    And the agent clicks on the "View payments" farmer dashboard button
    And the agent navigates back in the browser
    And the agent expands the accordion for any row using the "keyboard_arrow_down" icon

  @regression
  Scenario: AT-TC-11 - Row Accordion — Not Started Herd
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    And the agent applies the "Not Started" quick filter
    When the agent expands the accordion for any row using the "keyboard_arrow_down" icon
    Then the agent can see the Payment Details section
    And the agent clicks on the "View payments" farmer dashboard button
    And the agent navigates back in the browser
    And the agent expands the accordion for any row using the "keyboard_arrow_down" icon

  @regression
  Scenario: AT-TC-12 - Search by Herd Number
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent searches for herd number "5"
    Then the agent clears the herd search field

  @regression
  Scenario: AT-TC-13 - Search by invalid characters — no results shown
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent searches for herd number "!£$"
    Then no herd results are shown in the My Clients list
    And the agent clears the herd search field

  @regression
  Scenario: AT-TC-14 - Export to Excel
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent applies the "Export to Excel" quick filter
    Then the Excel file has been downloaded successfully
