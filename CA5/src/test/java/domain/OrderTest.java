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

}
