package controllers;

import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import exceptions.UsernameAlreadyTaken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static defines.Errors.INCORRECT_PASSWORD;
import static defines.Errors.NOT_EXISTENT_USER;
import static defines.Errors.USERNAME_ALREADY_TAKEN;
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
    public void testLoginStatusOk() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");
        doNothing().when(balootMock).login("hadi", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        verify(balootMock).login("hadi", "1234");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when login is successful")
    public void testLoginResponseBody() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");
        doNothing().when(balootMock).login("hadi", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        verify(balootMock).login("hadi", "1234");
        assertEquals("login successfully!", response.getBody());
    }

    @Test
    @DisplayName("Http status should be Not found when user not exist")
    public void testLoginErrorNotFound() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");
        doThrow(new NotExistentUser()).when(balootMock).login("hadi", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        verify(balootMock).login("hadi", "1234");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when user not exist")
    public void testLoginErrorBodyMessageUserNotFound() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");
        doThrow(new NotExistentUser()).when(balootMock).login("hadi", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        verify(balootMock).login("hadi", "1234");
        assertEquals(NOT_EXISTENT_USER, response.getBody());
    }

    @Test
    @DisplayName("Http status should be Unauthorized when password is incorrect")
    public void testLoginErrorPasswordIncorrect() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");
        doThrow(new IncorrectPassword()).when(balootMock).login("hadi", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        verify(balootMock).login("hadi", "1234");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    @DisplayName("Error message should be correct when password is incorrect")
    public void testLoginErrorBodyMessagePasswordIncorrect() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");
        input.put("password", "1234");
        doThrow(new IncorrectPassword()).when(balootMock).login("hadi", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        verify(balootMock).login("hadi", "1234");
        assertEquals(INCORRECT_PASSWORD, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be correct when signup is successful")
    public void testSignupStatusOk() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();
        input.put("address", "Sari, Iran");
        input.put("birthDate", "2001-11-21");
        input.put("email", "sana.sarinavaei@gmail.com");
        input.put("username", "sana");
        input.put("password", "1234");
        doNothing().when(balootMock).addUser(any());

        ResponseEntity<String> response = authenticationController.signup(input);

        verify(balootMock).addUser(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when signup is successful")
    public void testSignupResponseBody() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();
        input.put("address", "Sari, Iran");
        input.put("birthDate", "2001-11-21");
        input.put("email", "sana.sarinavaei@gmail.com");
        input.put("username", "sana");
        input.put("password", "1234");
        doNothing().when(balootMock).addUser(any());

        ResponseEntity<String> response = authenticationController.signup(input);

        verify(balootMock).addUser(any());
        assertEquals("signup successfully!", response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be bad request when user exists")
    public void testSignupErrorUserExists() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();
        input.put("address", "Sari, Iran");
        input.put("birthDate", "2001-11-21");
        input.put("email", "sana.sarinavaei@gmail.com");
        input.put("username", "sana");
        input.put("password", "1234");
        doThrow(new UsernameAlreadyTaken()).when(balootMock).addUser(argThat(user -> user.getUsername().equals("sana")));

        ResponseEntity<String> response = authenticationController.signup(input);

        verify(balootMock).addUser(argThat(user -> user.getUsername().equals("sana")));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when user exists")
    public void testSignupBodyMessageUserExists() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();
        input.put("address", "Sari, Iran");
        input.put("birthDate", "2001-11-21");
        input.put("email", "sana.sarinavaei@gmail.com");
        input.put("username", "sana");
        input.put("password", "1234");
        doThrow(new UsernameAlreadyTaken()).when(balootMock).addUser(argThat(user -> user.getUsername().equals("sana")));

        ResponseEntity<String> response = authenticationController.signup(input);

        verify(balootMock).addUser(argThat(user -> user.getUsername().equals("sana")));
        assertEquals(USERNAME_ALREADY_TAKEN, response.getBody());
    }

}