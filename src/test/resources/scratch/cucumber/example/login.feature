Feature: It is possible for a user to sign in

  Scenario Outline: A user can sign in
    Given a user with the username "<username>" and password "<password>" exists
    And the user is on the login page
    And the user enters a username of "<username>"
    And the user enters a password of "<password>"
    When the user signs in
    Then the user should be on the home page
    And the user should be signed in as "<username>"

  Examples:
    | username | password      |
    | TestUser | test password |
