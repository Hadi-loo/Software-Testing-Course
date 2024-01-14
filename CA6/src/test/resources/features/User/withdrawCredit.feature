Feature: User can withdraw credit from their account

    As a user, I want to withdraw credit from my account so that I can use the service.

    Scenario Outline: Withdrawing valid credit from the user account
        Given a user with a balance of <balance>
        When the user withdraws credit of <withdraw> from their account
        Then the user's new balance should be <new_balance>

        Examples:
        | balance | withdraw | new_balance |
        | 7.00    | 5.50     | 1.50        |
        | 7.00    | 7.00     | 0.00        |

    Scenario Outline: Withdrawing invalid credit from the user account
        Given a user with a balance of <balance>
        When the user withdraws credit of <withdraw> from their account
        Then the user's new balance should be <new_balance>
        And An InsufficientCredit exception should be thrown

        Examples:
        | balance | withdraw | new_balance |
        | 7.00    | 10.50    | 7.00        |
        | 0.00    | 5.50     | 0.00        |