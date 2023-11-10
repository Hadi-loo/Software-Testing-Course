package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    public void setUpWithArgs(int id, int customer, int price, int quantity) {
        order.setId(id);
        order.setCustomer(customer);
        order.setPrice(price);
        order.setQuantity(quantity);
    }

    @Test
    @DisplayName("equals should return true when the id is the same")
    public void testEqualsReturnsTrueWhenIdIsSame() {
        order.setId(1);
        Order order2 = new Order();
        order2.setId(1);
        assertTrue(order.equals(order2));
    }

    @Test
    @DisplayName("equals should return false when the id is different")
    public void testEqualsReturnsFalseWhenIdIsDifferent() {
        order.setId(1);
        Order order2 = new Order();
        order2.setId(2);
        assertFalse(order.equals(order2));
    }

    @Test
    @DisplayName("equals should return false when the object is not an order")
    public void testEqualsReturnsFalseWhenObjectIsNotOrder() {
        assertFalse(order.equals(new Object()));
    }

    @Test
    @DisplayName("id getter should return the correct value")
    public void testIdGetterReturnsCorrectValue() {
        order.setId(1);
        assertEquals(1, order.getId());
    }

    @Test
    @DisplayName("customer getter should return the correct value")
    public void testCustomerGetterReturnsCorrectValue() {
        order.setCustomer(1);
        assertEquals(1, order.getCustomer());
    }

    @Test
    @DisplayName("price getter should return the correct value")
    public void testPriceGetterReturnsCorrectValue() {
        order.setPrice(1);
        assertEquals(1, order.getPrice());
    }

    @Test
    @DisplayName("quantity getter should return the correct value")
    public void testQuantityGetterReturnsCorrectValue() {
        order.setQuantity(1);
        assertEquals(1, order.getQuantity());
    }

}
