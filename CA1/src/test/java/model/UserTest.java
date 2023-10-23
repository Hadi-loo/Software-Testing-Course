package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @CsvSource({ "5.0f, 4.5f" })
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

    @Test
    @DisplayName("add new BuyItem adds to buy list")
    public void addBuyItemForNewItemAddsToBuyList() {
        Commodity commodityMock = mock(Commodity.class);
        when(commodityMock.getId()).thenReturn("7");
        user.addBuyItem(commodityMock);
        assertTrue(user.getBuyList().containsKey("7"));
    }

    @Test
    @DisplayName("add new BuyItem inserts correct quantity")
    public void addBuyItemForNewItemInsertsCorrectQuantity() {
        Commodity commodityMock = mock(Commodity.class);
        when(commodityMock.getId()).thenReturn("7");
        user.addBuyItem(commodityMock);
        assertEquals(1, user.getBuyList().get("7"));
    }

    // CHECKME: this could be implemented simpler (now we are using delta assertion)
    // CHECKME: should we use addBuyItem() or just directly set the buyList?
    @Test
    @DisplayName("add existing BuyItem increases quantity")
    public void addBuyItemForExistingItemIncreasesQuantity() {
        Commodity commodityMock = mock(Commodity.class);
        when(commodityMock.getId()).thenReturn("7");
        user.addBuyItem(commodityMock);
        Integer old_quantity = user.getBuyList().get("7");
        user.addBuyItem(commodityMock);
        assertEquals(old_quantity+1 , user.getBuyList().get("7"));
    }

    @Test
    @DisplayName("throws exception when removing non-existing item from buy list")
    public void removeItemFromBuyListThrowsExceptionWhenItemDoesNotExist() {
        Commodity commodityMock = mock(Commodity.class);
        when(commodityMock.getId()).thenReturn("7");
        assertThrows(CommodityIsNotInBuyList.class, () -> user.removeItemFromBuyList(commodityMock));
    }

    @Test
    @DisplayName("removes item from buy list when quantity is 1")
    public void removeItemFromBuyListRemovesItemWhenQuantityIs1() throws CommodityIsNotInBuyList {
        Commodity commodityMock = mock(Commodity.class);
        when(commodityMock.getId()).thenReturn("7");
        user.addBuyItem(commodityMock);
        user.removeItemFromBuyList(commodityMock);
        assertFalse(user.getBuyList().containsKey("7"));
    }

    // CHECKME: this could be implemented simpler (now we are using delta assertion)
    @Test
    @DisplayName("decreases quantity when removing item from buy list")
    public void removeItemFromBuyListDecreasesQuantity() throws CommodityIsNotInBuyList {
        Commodity commodityMock = mock(Commodity.class);
        when(commodityMock.getId()).thenReturn("7");
        user.addBuyItem(commodityMock);
        user.addBuyItem(commodityMock);
        Integer old_quantity = user.getBuyList().get("7");
        user.removeItemFromBuyList(commodityMock);
        assertEquals(old_quantity-1, user.getBuyList().get("7"));
    }

    // CHECKME: what happens if commodity is null?
    // CHECKME: what happens if commodity_id is null?


}
