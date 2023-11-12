package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
        int averageOrderQuantityByCustomer = engine.getAverageOrderQuantityByCustomer(5);
        assertEquals(0, averageOrderQuantityByCustomer);
    }

    @Test
    @DisplayName("average order should be correct when there is one order")
    public void testAverageQuantityIsCorrectWithOneOrder() throws Exception {
        setUpWithArgs(order,1, 5, 10, 5);
        engine.orderHistory.add(order);
        int averageOrderQuantityByCustomer = engine.getAverageOrderQuantityByCustomer(5);
        assertEquals(5, averageOrderQuantityByCustomer );
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
        assertEquals(8, averageOrderQuantityByCustomer);
    }

    @Test
    @DisplayName("an exception should be thrown when the customer id does not exist")
    public void testAverageQuantityThrowExceptionWithNonExistentCustomer() throws Exception {
        setUpWithArgs(order, 1, 5, 10, 6);
        engine.orderHistory.add(order);
        assertThrows(Exception.class, () -> engine.getAverageOrderQuantityByCustomer(2));
    }

    @Test
    @DisplayName("quantity difference should be 0 when there are no orders")
    public void testQuantityDifferenceIsZeroWithNoOrder() {
        int quantityPatternByPrice = engine.getQuantityPatternByPrice(20);
        assertEquals(0, quantityPatternByPrice);
    }

    @Test
    @DisplayName("quantity difference should be 0 when there is one order")
    public void testQuantityDifferenceIsZeroWithOneOrder() {
        setUpWithArgs(order, 1, 5, 10, 6);
        engine.orderHistory.add(order);
        int quantityPatternByPrice = engine.getQuantityPatternByPrice(20);
        assertEquals(0, quantityPatternByPrice);
    }

    @Test
    @DisplayName("quantity difference should be 0 when there are multiple orders with different prices")
    public void testQuantityDifferenceIsZeroWithDifferentPrices() {
        setUpWithArgs(order, 1, 5, 10, 6);
        engine.orderHistory.add(order);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 20, 10);
        engine.orderHistory.add(order2);
        int quantityPatternByPrice = engine.getQuantityPatternByPrice(30);
        assertEquals(0, quantityPatternByPrice);
    }

    @Test
    @DisplayName("quantity difference should be correct when there are multiple orders with the same price")
    public void testQuantityDifferenceIsCorrectWithSamePrices() {
        setUpWithArgs(order, 1, 5, 5, 6);
        engine.orderHistory.add(order);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 10, 10);
        engine.orderHistory.add(order2);
        int quantityPatternByPrice = engine.getQuantityPatternByPrice(10);
        assertEquals(4, quantityPatternByPrice);
    }

    @Test
    @DisplayName("quantity difference should be 0 when there are multiple orders with the same price but different quantities")
    public void testQuantityDifferenceIsZeroWithSamePricesDifferentQuantities() {
        setUpWithArgs(order, 1, 6, 5, 5);
        engine.orderHistory.add(order);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 10, 10);
        engine.orderHistory.add(order2);
        Order order3 = new Order();
        setUpWithArgs(order3, 3, 7, 10, 20);
        engine.orderHistory.add(order3);
        int quantityPatternByPrice = engine.getQuantityPatternByPrice(10);
        assertEquals(0, quantityPatternByPrice);
    }

    @Test
    @DisplayName("quantity difference should be correct when there are multiple orders")
    public void testQualityDifferenceShouldBeCorrectWithMultipleOrder() {
        setUpWithArgs(order, 1, 6, 5, 5);
        engine.orderHistory.add(order);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 10, 10);
        engine.orderHistory.add(order2);
        Order order3 = new Order();
        setUpWithArgs(order3, 3, 7, 10, 15);
        engine.orderHistory.add(order3);
        int quantityPatternByPrice = engine.getQuantityPatternByPrice(10);
        assertEquals(5, quantityPatternByPrice);
    }

    @Test
    @DisplayName("fraudulent quantity should be 0 when the order quantity is less than or equal to the average order quantity")
    public void testFraudulentQuantityIsZeroWithLessEqualOrderQuantity() throws Exception {
        setUpWithArgs(order, 1, 5, 5, 6);
        engine.orderHistory.add(order);
        int fraudulentQuantity = engine.getCustomerFraudulentQuantity(order);
        assertEquals(0, fraudulentQuantity);
    }

    @Test
    @DisplayName("fraudulent quantity should be correct when the order quantity is greater than the average order quantity")
    public void testFraudulentQuantityIsCorrectWithGreaterOrderQuantity() throws Exception {
        setUpWithArgs(order, 1, 5, 5, 10);
        engine.orderHistory.add(order);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 5, 4);
        engine.orderHistory.add(order2);
        int fraudulentQuantity = engine.getCustomerFraudulentQuantity(order);
        assertEquals(3, fraudulentQuantity);
    }

    @Test
    @DisplayName("an exception should be thrown when the customer id does not exist")
    public void testFraudulentQuantityThrowExceptionWithNonExistentCustomer() throws Exception {
        setUpWithArgs(order, 1, 5, 5, 10);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 6, 5, 10);
        engine.orderHistory.add(order2);
        assertThrows(Exception.class, () -> engine.getCustomerFraudulentQuantity(order));
    }

    @Test
    @DisplayName("add order should return 0 when the order already exists")
    public void testAddOrderReturnZeroWithExistingOrder() throws Exception {
        setUpWithArgs(order, 1, 5, 5, 10);
        engine.orderHistory.add(order);
        int fraudulentQuantity = engine.addOrderAndGetFraudulentQuantity(order);
        assertEquals(0, fraudulentQuantity);
    }

    @Test
    @DisplayName("add order should throw an exception when customer id does not exist")
    public void testAddOrderThrowExceptionWithNonExistentCustomer() throws Exception {
        setUpWithArgs(order, 1, 5, 5, 10);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 6, 5, 10);
        engine.orderHistory.add(order2);
        assertThrows(Exception.class, () -> engine.addOrderAndGetFraudulentQuantity(order));
    }

    @Test
    @DisplayName("add order should return fraudulent quantity when the fraudulent quantity is not 0")
    public void testAddOrderReturnFraudulentQuantityWithGreaterOrderQuantity() throws Exception {
        setUpWithArgs(order, 1, 5, 5, 10);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 5, 4);
        engine.orderHistory.add(order2);
        Order order3 = new Order();
        setUpWithArgs(order3, 3, 5, 5, 10);
        engine.orderHistory.add(order3);
        int fraudulentQuantity = engine.addOrderAndGetFraudulentQuantity(order);
        assertEquals(3, fraudulentQuantity);
    }

    @Test
    @DisplayName("add order should return quantity pattern when the fraudulent quantity is 0")
    public void testAddOrderReturnQuantityPatternWithSamePrices() throws Exception {
        setUpWithArgs(order, 1, 5, 10, 4);
        Order order2 = new Order();
        setUpWithArgs(order2, 2, 5, 5, 6);
        engine.orderHistory.add(order2);
        Order order3 = new Order();
        setUpWithArgs(order3, 3, 5, 10, 10);
        engine.orderHistory.add(order3);
        int fraudulentQuantity = engine.addOrderAndGetFraudulentQuantity(order);
        assertEquals(4, fraudulentQuantity);
    }

}
