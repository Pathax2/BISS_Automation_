Feature: Login Into the application

  Scenario: Verify user can login
    Given User Launches the application
    When user enters username "Username"
    And user enters password "Password"
    And user clicks on Login button
    Then User should be successfully able to login