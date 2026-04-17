Feature: Login Into the application

  Scenario: Verify user can login with valid credentials
    Given User Launches the application
    When user enters valid "Username"
    And user enters valid "Password"
    And user clicks on Login button
    Then User should be successfully able to login
    Then A user logging out of an application


