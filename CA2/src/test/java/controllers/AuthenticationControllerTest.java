package controllers;

import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static defines.Errors.NOT_EXISTENT_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private Baloot balootMock;

    @BeforeEach
    public void setUp() {
        authenticationController = new AuthenticationController();
        balootMock = mock(Baloot.class);
        authenticationController.setBaloot(balootMock);
    }

    @Test
    @DisplayName("HTTP status should be correct when login is successful")
    public void testLoginStatusOk() {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");

        ResponseEntity<String> response = authenticationController.login(input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when login is successful")
    public void testLoginResponseBody() {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");

        ResponseEntity<String> response = authenticationController.login(input);
        assertEquals("login successfully!", response.getBody());
    }

    @Test
    @DisplayName("Http status should be Not found when user not exist")
    public void testLoginErrorNotFound() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");

        doThrow(new NotExistentUser()).when(balootMock).login(argThat((List args) -> args.get(0).equals("hadi")));
        ResponseEntity<String> response = authenticationController.login(input);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when user not exist")
    public void testLoginErrorNotFound() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");

        doThrow(new NotExistentUser()).when(balootMock).login(argThat((List args) -> args.get(0).equals("hadi")));
        ResponseEntity<String> response = authenticationController.login(input);
        assertEquals(NOT_EXISTENT_USER, response.getBody());
    }
}
