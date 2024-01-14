Feature: User can withdraw credit from their account

  As a user, I want to withdraw credit from my account so that I can use the service.

  Scenario: Withdrawing valid credit from the user account
    Given a user with a balance of 7.00
    When the user withdraws credit of 5.50 from their account
    Then the user's new balance should be 1.50

  Scenario: Withdrawing invalid credit from the user account
    Given a user with a balance of 7.00
    When the user withdraws credit of 10.50 from their account
    Then An InsufficientCredit exception should be thrown
    And the user's balance should remain 7.00