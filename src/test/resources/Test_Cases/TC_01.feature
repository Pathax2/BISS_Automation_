Feature: Staff Login

  Scenario: Verify user can login
    Given user launches login page
    When user enters "Username" into "login.username.id"
    And user enters "Password" into "login.password.id"
    And user clicks on "login.button.id"