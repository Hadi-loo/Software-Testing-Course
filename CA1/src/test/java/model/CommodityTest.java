package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.NotInStock;

public class CommodityTest {

    private Commodity commodity;

    @BeforeEach
    public void setup() {
        commodity = new Commodity();
    }

    @Test
    public void demoTest() {
        assertTrue(true);
    }

    // FIXME: is it necessary?
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 0, 1, 10, 100})
    @DisplayName("updateInStock should not throw exception")
    public void updateInStockShouldNotThrowException(int amount) {
        commodity.setInStock(100);
        assertDoesNotThrow(() -> commodity.updateInStock(amount));
    }

    // FIXME: should name it better
    // FIXME: should it be parameterized?
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 0, 1, 10, 100})
    @DisplayName("stock should update correctly")
    public void stockShouldUpdateCorrectly(int amount) throws NotInStock {
        commodity.setInStock(100);
        commodity.updateInStock(amount);
        assertEquals(100 + amount, commodity.getInStock());
    }

    // FIXME: should we check for the exception message?
    @ParameterizedTest
    @ValueSource(ints = {-11, -100, -1000})
    @DisplayName("stock cannot be negative")
    public void stockCannotBeNegative(int amount) {
        commodity.setInStock(10);
        assertThrows(NotInStock.class, () -> commodity.updateInStock(amount));
    }

    @Test
    @DisplayName("new rate added to userRate")
    public void rateAddedCorrectly() {
        commodity.addRate("username", 5);
        assertTrue(commodity.getUserRate().containsKey("username"));
    }

    @Test
    @DisplayName("count of userRate increases on new rate")
    public void countIncreasesOnNewRate() {
        int oldSize = commodity.getUserRate().size();
        commodity.addRate("username", 5);
        assertEquals(oldSize + 1, commodity.getUserRate().size());
    }

    // FIXME: should't it throw exception?
    @Test
    @DisplayName("new rate added with null username")
    public void rateAddedWithNullUsername() {
        commodity.addRate(null, 5);
        assertTrue(commodity.getUserRate().containsKey(null));
    }

    // FIXME: shouldn't it throw exception?
    @Test
    @DisplayName("new rate added with out of bound score")
    public void rateAddedWithOutOfBoundScore() {
        commodity.addRate("username", 100);
        assertEquals(100, commodity.getUserRate().get("username"));
    }

    // FIXME: shouldn't it throw exception?
    @Test
    @DisplayName("new rate added with negative score")
    public void rateAddedWithNullScore() {
        commodity.addRate("username", -5);
        assertEquals(-5, commodity.getUserRate().get("username"));
    }

    @Test
    @DisplayName("new rate added to userRate with correct score")
    public void rateAddedCorrectlyWithCorrectScore() {
        commodity.addRate("username", 5);
        assertEquals(5, commodity.getUserRate().get("username"));
    }

    @Test
    @DisplayName("score updates on existing user")
    public void scoreUpdatesOnExistingUser() {
        commodity.addRate("username", 5);
        commodity.addRate("username", 10);
        assertEquals(10, commodity.getUserRate().get("username"));
    }

    @Test
    @DisplayName("rating updates on new user rating")
    public void ratingUpdatesOnNewUserRating() {
        commodity.setInitRate(5);
        commodity.addRate("username", 5);
        assertEquals(5, commodity.getRating());
    }

    @Test
    @DisplayName("rating updates on new user rating")
    public void ratingUpdatesOnNewUserRating2() {
        commodity.setInitRate(0);
        commodity.addRate("username", 10);
        assertEquals(5, commodity.getRating());
    }

    @Test
    @DisplayName("rating updates on new user rating")
    public void ratingUpdatesOnNewUserRating3() {
        commodity.setInitRate(5);
        commodity.addRate("username", -5);
        assertEquals(0, commodity.getRating());
    }

    @Test
    @DisplayName("rating updates on existing user rating")
    public void ratingUpdatesOnExistingUserRating() {
        commodity.setInitRate(5);
        commodity.addRate("username", 5);
        commodity.addRate("username", 10);
        assertEquals(7.5, commodity.getRating());
    }

    @Test
    @DisplayName("rating updates in multiple new users rating")
    public void ratingUpdatesInMultipleNewUsersRating() {
        commodity.setInitRate(10);
        commodity.addRate("sana", 20);
        commodity.addRate("hadi", 60);
        assertEquals(30, commodity.getRating());
    }

    @Test
    @DisplayName("rating updates in multiple new users rating")
    public void ratingUpdatesInMultipleNewUsersRating2() {
        commodity.setInitRate(0);
        commodity.addRate("sana", 20);
        commodity.addRate("hadi", 60);
        commodity.addRate("nesa", 30);
        assertEquals(27.5, commodity.getRating());
    }

    @Test
    @DisplayName("rating updates in multiple existing users rating")
    public void ratingUpdatesInMultipleExistingUsersRating() {
        commodity.setInitRate(10);
        commodity.addRate("sana", 20);
        commodity.addRate("hadi", 60);
        commodity.addRate("nesa", 30);
        commodity.addRate("sana", 30);
        commodity.addRate("hadi", 70);
        commodity.addRate("nesa", 40);
        assertEquals(37.5, commodity.getRating());
    }

}
