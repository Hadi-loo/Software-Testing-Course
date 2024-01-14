package model;

import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
public class UserStepDefinitions {
    private User user;
    private Exception exception;

    @Given("a user with a balance of {float}")
    public void aUserWithABalanceOfExists(float balance) {
        user = new User();
        user.setCredit(balance);
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

    @Then("the user's new balance should be {float}")
    public void theUserSNewBalanceShouldBe(float newBalance) {
        assertEquals(newBalance, user.getCredit());
    }

    @Then("An InvalidCreditRange exception should be thrown")
    public void anInvalidCreditRangeExceptionShouldBeThrown() {
        assertInstanceOf(InvalidCreditRange.class, exception);
    }

    @Then("An InsufficientCredit exception should be thrown")
    public void anInsufficientCreditExceptionShouldBeThrown() {
        assertInstanceOf(InsufficientCredit.class, exception);
    }

    @And("the user's balance should remain {float}")
    public void theUserSBalanceShouldRemain(float balance) {
        assertEquals(balance, user.getCredit());
    }
}