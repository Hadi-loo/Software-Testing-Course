package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void demoTest() {
        assertEquals(1, 1);
    }

    @Test
    public void testGetUsername() {
        User user = new User("username", "password", "email", "phone", "address");
        assertEquals("username", user.getUsername());
    }
}
