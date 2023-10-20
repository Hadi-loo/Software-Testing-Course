package model;

import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;

    @BeforeEach
    public void serUp() {
        user = new User("hadi", "1234", "m.h.babalu@gmail.com", "2000-01-01", "Tehran, Iran");
    }

    @Test
    @DisplayName("Simple Test User Initialization with username")
    public void InitialUsernameTest() {
        assertEquals("hadi", user.getUsername());
    }

    @Test
    @DisplayName("Simple Test User Initialization with password")
    public void InitialPasswordTest() {
        assertEquals("1234", user.getPassword());
    }

    @Test
    @DisplayName("Simple Test User Initialization with Email")
    public void InitialEmailTest() {
        assertEquals("m.h.babalu@gmail.com", user.getEmail());
    }

    @Test
    @DisplayName("Simple Test User Initialization with BirthDate")
    public void InitialBirthTest() {
        assertEquals("2000-01-01", user.getBirthDate());
    }

    @Test
    @DisplayName("Simple Test User Initialization with Address")
    public void InitialAddressTest() {
        assertEquals("Tehran, Iran", user.getAddress());
    }

    @ParameterizedTest
    @ValueSource(floats = { -1.0f, -5.5f, -9.4f })
    @DisplayName("check negative credits in User class")
    public void NegativeCreditUserTest(float amount) {
        assertThrows(InvalidCreditRange.class, () -> user.addCredit(amount));
    }

    @ParameterizedTest
    @ValueSource(floats = { 0f, 5f, 10.0f })
    @DisplayName("check positive credits in User class")
    public void PositiveCreditUserTest(float amount) throws InvalidCreditRange {
        float credit = user.getCredit();
        user.addCredit(amount);
        assertEquals(credit + amount, user.getCredit());
    }

    @ParameterizedTest
    @CsvSource({ "5.0f , 4.5f" })
    @DisplayName("check negative withdraw in User class")
    public void NegativeWithdrawUserTest(float amount, float credit) throws InvalidCreditRange {
        user.addCredit(credit);
        assertThrows(InsufficientCredit.class, () -> user.withdrawCredit(amount));
    }

    @ParameterizedTest
    @CsvSource({ "3f, 4.5f" })
    @DisplayName("check positive withdraw in User class")
    public void PositiveWithdrawUserTest(float amount, float credit) throws InvalidCreditRange, InsufficientCredit {
        user.addCredit(credit);
        user.withdrawCredit(amount);
        assertEquals(credit - amount, user.getCredit());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 500",
            "2, 1000"
    })
    @DisplayName("add New PurchasedItem in User class")
    public void AddNewPurchaseItemTest(String id, int quantity) {
        user.addPurchasedItem(id, quantity);
        assertEquals(quantity, user.getPurchasedList().get(id));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 500, 100",
            "2, 1000, 200"
    })
    @DisplayName("add existing PurchasedItem in User class")
    public void AddPurchaseItemTest(String id, int quantity, int new_quantity) {
        user.addPurchasedItem(id, quantity);
        user.addPurchasedItem(id, new_quantity);
        assertEquals(quantity + new_quantity, user.getPurchasedList().get(id));
    }
}
