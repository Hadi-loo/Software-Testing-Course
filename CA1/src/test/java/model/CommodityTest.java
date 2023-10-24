package model;

import exceptions.InvalidScoreRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.NotInStock;

public class CommodityTest {

    private Commodity commodity;

    @BeforeEach
    public void setup() {
        commodity = new Commodity();
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 0, 100})
    @DisplayName("updateInStock should not throw exception")
    public void updateInStockShouldNotThrowException(int amount) {
        commodity.setInStock(100);
        assertDoesNotThrow(() -> commodity.updateInStock(amount));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 0, 10})
    @DisplayName("stock should update correctly")
    public void stockShouldUpdateCorrectly(int amount) throws NotInStock {
        commodity.setInStock(100);
        commodity.updateInStock(amount);
        assertEquals(100 + amount, commodity.getInStock());
    }

    @ParameterizedTest
    @ValueSource(ints = {-11, -1000})
    @DisplayName("stock cannot be negative")
    public void stockCannotBeNegative(int amount) {
        commodity.setInStock(10);
        assertThrows(NotInStock.class, () -> commodity.updateInStock(amount));
    }

    @Test
    @DisplayName("new rate should not throw exception")
    public void newRateDoesNotThrowException() {
        assertDoesNotThrow(() -> commodity.addRate("username", 5));
    }

    @Test
    @DisplayName("new rate added to userRate")
    public void newRateAdded() throws InvalidScoreRange {
        commodity.addRate("username", 5);
        assertTrue(commodity.getUserRate().containsKey("username"));
    }

    @Test
    @DisplayName("count of userRates increases on new rate")
    public void countIncreasesOnNewRate() throws InvalidScoreRange {
        int oldSize = commodity.getUserRate().size();
        commodity.addRate("username", 5);
        assertEquals(oldSize + 1, commodity.getUserRate().size());
    }

    // CHECKME: what's the correct behavior for null username? shouldn't it throw exception?
    @Test
    @DisplayName("new rate added with null username")
    public void rateAddedWithNullUsername() throws InvalidScoreRange {
        commodity.addRate(null, 5);
        assertTrue(commodity.getUserRate().containsKey(null));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0, 11, 100})
    @DisplayName("new rate added with out of bound score")
    public void rateAddedWithOutOfBoundScore(int score) {
        assertThrows(InvalidScoreRange.class, () -> commodity.addRate("username", score));
    }

    @Test
    @DisplayName("new rate added to userRate with correct score")
    public void newRateAddedWithCorrectScore() throws InvalidScoreRange {
        commodity.addRate("username", 5);
        assertEquals(5, commodity.getUserRate().get("username"));
    }

    // CHECKME: instead of using addRate, could directly set the userRate
    @Test
    @DisplayName("score updates on existing user")
    public void scoreUpdatesOnExistingUser() throws InvalidScoreRange {
        commodity.addRate("username", 5);
        commodity.addRate("username", 10);
        assertEquals(10, commodity.getUserRate().get("username"));
    }

    @ParameterizedTest
    @CsvSource({
            "5.0f, 5, 5.0f",
            "2.5f, 3, 2.75f",
            "0.0f, 8, 4.0f"
    })
    @DisplayName("rating updates on new user rating")
    public void ratingUpdatesOnNewUserRating(float init, int score, float expected) throws InvalidScoreRange {
        commodity.setInitRate(init);
        commodity.addRate("username", score);
        assertEquals(expected, commodity.getRating(), 1e-3f);
    }

    // CHECKME: instead of using addRate, could directly set the userRate
    @Test
    @DisplayName("rating updates on existing user rating")
    public void ratingUpdatesOnExistingUserRating() throws InvalidScoreRange {
        commodity.setInitRate(5);
        commodity.addRate("username", 5);
        commodity.addRate("username", 10);
        assertEquals(7.5f, commodity.getRating(), 1e-3f);
    }

    //
    @Test
    @DisplayName("rating updates with 2 new users rating")
    public void ratingUpdatesWith2NewUsersRating() throws InvalidScoreRange {
        commodity.setInitRate(10);
        commodity.addRate("sana", 5);
        commodity.addRate("hadi", 7);
        assertEquals(7.3333f, commodity.getRating(), 1e-3f);
    }

    @Test
    @DisplayName("rating updates with multiple new users rating")
    public void ratingUpdatesWithMultipleNewUsersRating() throws InvalidScoreRange {
        commodity.setInitRate(0);
        commodity.addRate("sana", 4);
        commodity.addRate("hadi", 8);
        commodity.addRate("nesa", 9);
        assertEquals(5.25f, commodity.getRating(), 1e-3f);
    }

    @Test
    @DisplayName("rating updates with multiple existing users rating")
    public void ratingUpdatesWithMultipleExistingUsersRating() throws InvalidScoreRange {
        commodity.setInitRate(10);
        commodity.addRate("sana", 3);
        commodity.addRate("hadi", 4);
        commodity.addRate("nesa", 7);
        commodity.addRate("sana", 4);
        commodity.addRate("hadi", 5);
        commodity.addRate("nesa", 8);
        assertEquals(6.75f, commodity.getRating(), 1e-3f);
    }

}
