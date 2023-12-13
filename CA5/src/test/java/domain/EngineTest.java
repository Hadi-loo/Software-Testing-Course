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

}
