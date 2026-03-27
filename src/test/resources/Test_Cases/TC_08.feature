Feature: TC_08 - Regression Suite for My Clients Table — Headers, Sorting and Pagination

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Validates the structure, sorting, and pagination behaviour of the My Clients page
  #   and its Transfers sub-tab, as well as farmer dashboard name and greeting verification.
  #
  #   Active Scenarios:
  #     Sprint BISS_23.1.3 TC1 : My Clients table column headers and sorting controls
  #                               Verifies sortable vs non-sortable columns, row selection
  #                               and name match on farmer dashboard
  #
  #     Sprint BPS/BISS PI_22.4.6 TC1 : Transfers sub-tab column headers and pagination
  #                               Verifies Transfers table headers, row count changes,
  #                               and last page navigation via pagination arrow
  #
  #     Sprint BISS_23.1.3 TC2 : My Clients herd selection and dashboard verification
  #                               Verifies name/herd match on dashboard and
  #                               personalised greeting message is displayed
  #
  # Notes:
  #   1. Row 2 is used as the dynamic herd fixture to avoid dependency on a specific herd number.
  #   2. The "GAEC 8" header check is intentionally commented — column was removed from the UI.
  #   3. The "Herd Expired" column does not have a sort control by design.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "Home" and "My Clients" Left Menu Link

  #@tmslink=BISSAGL-1647
  @regression
  Scenario: Sprint BISS_23.1.3 TC1 for BISSAGL-54 | Agents Tools - My Client screen with herd number
    Given the agent is on the BISS Agent Home Screen
    And the agent verifies the "Name" column header has sorting
    And the agent verifies the "Herd Number" column header has sorting
    And the agent verifies the "Farmer Status" column header has no sorting
    And the agent verifies the "Herd Expired" column header has no sorting
    # And the agent verifies the "GAEC 8" column header has no sorting
    And the agent verifies the "Eco Space for Nature" column header has no sorting
    And the agent verifies the "BISS Application Status" column header has no sorting
    When the agent selects the herd from row 2
    And the agent clicks on the View Dashboard button
    Then the agent verifies the name on the dashboard matches the selected row

  #@tmslink=BISSAGL-1276
  @regression
  Scenario: Sprint BPS/BISS PI_22.4.6 TC1 for BISSAGL-827 My Clients - ENTS Transfers My Clients
    Given the agent is on the BISS Agent Home Screen
    When the agent switches to the "Transfers" tab on the My Clients page
    And the agent verifies the "Name" column header has sorting
    And the agent verifies the "Address" column header has sorting
    And the agent verifies the "Herd No." column header has sorting
    And the agent verifies the "Expired" column header has no sorting
    And the agent verifies the "Transfers" column header has no sorting
    And the agent changes the number of rows per page to "50"
    # And the agent changes the number of rows per page to "399"
    And the agent changes the number of rows per page to "20"
    Then the agent navigates to the last page using the pagination arrow

  #@tmslink=BISSAGL-905
  @regression
  Scenario: Sprint BISS_23.1.3 TC2 for BISSAGL-54 | Agents Tools - My Client screen with herd number
    Given the agent is on the BISS Agent Home Screen
    When the agent selects the herd from row 2
    And the agent clicks on the View Dashboard button
    Then the agent verifies the name and herd number on the dashboard
    And the agent verifies the personalised greeting message is displayed
    #And Agent Check BISS Application Deadline Message
