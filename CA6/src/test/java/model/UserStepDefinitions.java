package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
public class UserStepDefinitions {
    private User user;
    private Commodity commodity;
    private Exception exception;

    @Given("a user with a balance of {float}")
    public void aUserWithABalanceOfExists(float balance) {
        user = new User();
        user.setCredit(balance);
    }

    @Given("a user with a buyList of items")
    public void aUserWithABuyListOfItems() {
        user = new User();
        commodity = new Commodity();
        commodity.setId("1");
        user.addBuyItem(commodity);
    }

    @Given("a user with a buyList of items with quantity greater than one")
    public void aUserWithABuyListOfItemsWithQuantityGreaterThanOne() {
        user = new User();
        commodity = new Commodity();
        commodity.setId("1");
        user.addBuyItem(commodity);
        user.addBuyItem(commodity);
    }

    @When("the user adds credit of {float} to their account")
    public void theUserAddsCreditOfToTheirAccount(float amount) {
        try {
            user.addCredit(amount);
        } catch (InvalidCreditRange e) {
            exception = e;
        }
    }

    @When("the user withdraws credit of {float} from their account")
    public void theUserWithdrawsCreditOfFromTheirAccount(float amount) {
        try {
            user.withdrawCredit(amount);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the user removes an item from the buyList")
    public void theUserRemovesAnItemFromTheBuyList() {
        try {
            user.removeItemFromBuyList(commodity);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("the user removes an item from the buyList that is not in the buyList")
    public void theUserRemovesAnItemFromTheBuyListThatIsNotInTheBuyList() {
        commodity = new Commodity();
        commodity.setId("2");
        try {
            user.removeItemFromBuyList(commodity);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the user's new balance should be {float}")
    public void theUserSNewBalanceShouldBe(float newBalance) {
        assertEquals(newBalance, user.getCredit());
    }

    @Then("the item should be removed from user's buyList")
    public void theItemShouldBeRemovedFromUserSBuyList() {
        assertFalse(user.getBuyList().containsKey(commodity.getId()));
    }

    @Then("the quantity of that item should be decremented by one")
    public void theQuantityOfThatItemShouldBeDecrementedByOne() {
        assertEquals(1, user.getBuyList().get(commodity.getId()));
    }

    @Then("CommodityIsNotInBuyList exception should be thrown")
    public void commodityIsNotInBuyListExceptionShouldBeThrown() {
        assertInstanceOf(CommodityIsNotInBuyList.class, exception);
    }

    @And("An InvalidCreditRange exception should be thrown")
    public void anInvalidCreditRangeExceptionShouldBeThrown() {
        assertInstanceOf(InvalidCreditRange.class, exception);
    }

    @And("An InsufficientCredit exception should be thrown")
    public void anInsufficientCreditExceptionShouldBeThrown() {
        assertInstanceOf(InsufficientCredit.class, exception);
    }

    @And("the item should not be removed from user's buyList")
    public void theItemShouldNotBeRemovedFromUserSBuyList() {
        assertTrue(user.getBuyList().containsKey(commodity.getId()));
    }
}