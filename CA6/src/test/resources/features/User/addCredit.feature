Feature: User can add credit to their account

    As a user, I want to add credit to my account so that I can use the service.

    Scenario Outline: Adding valid credit to the user account
        Given a user with a balance of <balance>
        When the user adds credit of <credit> to their account
        Then the user's new balance should be <new_balance>

        Examples:
        | balance | credit | new_balance |
        | 7.00    | 5.50   | 12.50       |
        | 0.00    | 10.0   | 10.0        |

    Scenario Outline: Adding invalid credit to the user account
        Given a user with a balance of <balance>
        When the user adds credit of <credit> to their account
        Then the user's new balance should be <new_balance>
        And An InvalidCreditRange exception should be thrown

        Examples:
        | balance | credit | new_balance |
        | 7.00    | -5.50  | 7.00        |
        | 0.00    | -10.00 | 0.00        |
