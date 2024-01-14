Feature: User can add credit to their account

    As a user, I want to add credit to my account so that I can use the service.

    Scenario: Adding valid credit to the user account
        Given a user with a balance of 7.00
        When the user adds credit of 5.50 to their account
        Then the user's new balance should be 12.50

    Scenario: Adding invalid credit to the user account
        Given a user with a balance of 7.00
        When the user adds credit of -5.50 to their account
        Then An InvalidCreditRange exception should be thrown
        And the user's balance should remain 7.00
