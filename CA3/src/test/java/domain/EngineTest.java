package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {
    private Order order;
    private Engine engine;
    @BeforeEach
    public void setUp() {
        engine = new Engine();
        order = new Order();
    }

    public void setUpWithArgs(Order order, int id, int customer, int price, int quantity) {
        order.setId(id);
        order.setCustomer(customer);
        order.setPrice(price);
        order.setQuantity(quantity);
    }

    @Test
    @DisplayName("average order should be 0 when there are no orders")
    public void testAverageQuantityIsZeroWithNoOrder() throws Exception {
        int customer_id = 5;
        int averageOrderQuantityByCustomer = engine.getAverageOrderQuantityByCustomer(customer_id);
        assertEquals(averageOrderQuantityByCustomer, 0);
    }

    @Test
    @DisplayName("average order should be correct when there is one order")
    public void testAverageQuantityIsCorrectWithOneOrder() throws Exception {
        setUpWithArgs(order,1, 5, 10, 5);
        engine.orderHistory.add(order);
        int averageOrderQuantityByCustomer = engine.getAverageOrderQuantityByCustomer(5);
        assertEquals(averageOrderQuantityByCustomer, 5);
    }

    @Test
    @DisplayName("average order should be correct when there are multiple orders")
    public void testAverageQuantityIsCorrectWithMultipleOrders() throws Exception {
        setUpWithArgs(order, 1, 5, 10, 6);
        engine.orderHistory.add(order);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 10, 10);
        engine.orderHistory.add(order2);
        int averageOrderQuantityByCustomer = engine.getAverageOrderQuantityByCustomer(5);
        assertEquals(averageOrderQuantityByCustomer, 8);
    }

    @Test
    @DisplayName("an exception should be thrown when the customer id does not exist")
    public void testAverageQuantityThrowExceptionWithNonExistentCustomer() throws Exception {
        setUpWithArgs(order, 1, 5, 10, 6);
        engine.orderHistory.add(order);
        assertThrows(Exception.class, () -> engine.getAverageOrderQuantityByCustomer(2));
    }
}