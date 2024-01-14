Feature: User can remove item from buyList

    As a user, I want to be able to remove an item from my buyList so that I can use the service.

    Scenario: Removing an item from buyList when there is only one of that item
        Given a user with a buyList of items
        When the user removes an item from the buyList
        Then the item should be removed from user's buyList

    Scenario: Removing an item from buyList when there are multiple of that item
        Given a user with a buyList of items with quantity greater than one
        When the user removes an item from the buyList
        Then the quantity of that item should be decremented by one
        And the item should not be removed from user's buyList

    Scenario: Removing non-existent item from buyList
        Given a user with a buyList of items
        When the user removes an item from the buyList that is not in the buyList
        Then CommodityIsNotInBuyList exception should be thrown