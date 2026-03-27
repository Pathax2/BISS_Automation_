Feature: TC_09 - BISSAGL-20849 2025 iNet - Agent unable to view payments or land details for client

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Verifies that an agent is able to view payment details for a specific client
  #   without a technical error appearing on screen.
  #
  #   Defect Context:
  #     BISSAGL-20849 reported that agents were unable to view payments or land details
  #     for certain clients — a technical error message was shown instead of the data.
  #
  #   Active Scenario:
  #     Verifies: login with specific agent account → My Clients herd search → Applications /
  #               Payments navigation → scheme year 2023 → Payments tab →
  #               no technical error message on screen
  #
  # Notes:
  #   1. This scenario uses a fixed agent username "agr14385" in the Background as the
  #      defect was reported against that specific account. Update in TestData.xlsx if
  #      the account changes.
  #   2. Herd "D3350968" is the specific client herd referenced in the defect report.
  #   3. The error message text verified is the exact string from the defect report.
  #
  # Author : Aniket Pathare | aniket.pathare@government.ie
  # Created: 26-03-2026
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with the username "agr14385" and valid credentials
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page

  #@tmslink=BISSAGL-1647
  @regression
  Scenario: Agent is able to view payments or land details for client
    And the agent navigates to the "Home" and "My Clients" Left Menu Link
    When the agent searches for herd number "D3350968"
    And the agent clicks on the View Dashboard button
    Then the agent clicks on the "Applications / Payments" link in the side navigation bar
    And the agent selects "2023" from the "schemeYear" dropdown
    And the agent clicks on the "Payments" tab
    Then no technical error is displayed on the page for herd "D3350968"
