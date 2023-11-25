package controllers;

import application.BalootApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.Baloot;

import java.util.Collections;
import java.util.Map;

import static defines.Errors.INVALID_CREDIT_RANGE;
import static defines.Errors.NOT_EXISTENT_USER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @MockBean
    private Baloot balootMock;

    @BeforeEach
    public void setUp() {
        userController.setBaloot(balootMock);
    }

    public User setUpWithArgs(){
        return new User("hadi", "1234", "m.h.babalu@gmail.com", "2002-11-03", "Tehran, Iran");
    }

    @Test
    @DisplayName("Test should return the status code 200 and the user info when the user exists")
    public void testReturnsUserInfoWhenExists() throws Exception {
        User user = setUpWithArgs();
        when(balootMock.getUserById("hadi")).thenReturn(user);

        mvc.perform(get("/users/hadi"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\":\"hadi\"}"))
                .andExpect(content().json("{\"password\":\"1234\"}"))
                .andExpect(content().json("{\"email\":\"m.h.babalu@gmail.com\"}"))
                .andExpect(content().json("{\"birthDate\":\"2002-11-03\"}"))
                .andExpect(content().json("{\"address\":\"Tehran, Iran\"}"));
    }

    @Test
    @DisplayName("Test should return the status code 404 when the user does not exist")
    public void testReturns404WhenUserDoesNotExist() throws Exception {
        when(balootMock.getUserById("sana")).thenThrow(new NotExistentUser());

        mvc.perform(get("/users/sana"))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "10.0", "50.005"})
    @DisplayName("Test should work correctly when the credit is valid and the user exists")
    public void testAddCreditCorrectly(String credit) throws Exception {
        User user = setUpWithArgs();
        when(balootMock.getUserById("hadi")).thenReturn(user);

        Map<String, String> validCreditInput = Collections.singletonMap("credit", credit);
        mvc.perform(post("/users/hadi/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(validCreditInput)))
                .andExpect(status().isOk())
                .andExpect(content().string("credit added successfully!"));
    }

    @Test
    @DisplayName("Test should return bad request when the credit is invalid")
    public void testAddCreditWithInvalidCredit() throws Exception {
        User user = setUpWithArgs();
        when(balootMock.getUserById("hadi")).thenReturn(user);

        Map<String, String> invalidCreditInput = Collections.singletonMap("credit", "-50");
        mvc.perform(post("/users/hadi/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidCreditInput)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_CREDIT_RANGE));
    }

    @Test
    @DisplayName("Test should return status code 404 in addCredit when the user does not exist")
    public void testAddCreditWhenUserDoesNotExist() throws Exception {
        when(balootMock.getUserById("sana")).thenThrow(new NotExistentUser());

        Map<String, String> validCreditInput = Collections.singletonMap("credit", "50");
        mvc.perform(post("/users/sana/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(validCreditInput)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_EXISTENT_USER));
    }

    @ParameterizedTest
    @DisplayName("Test should return bad request when the credit is not a number")
    @ValueSource(strings = {"fifty", "ten dollars", "50$"})
    public void testAddCreditWithInvalidCreditFormat(String invalidCredit) throws Exception {
        User user = setUpWithArgs();
        when(balootMock.getUserById("hadi")).thenReturn(user);

        Map<String, String> invalidCreditInput = Collections.singletonMap("credit",invalidCredit);
        mvc.perform(post("/users/hadi/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidCreditInput)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please enter a valid number for the credit amount."));
    }

    @Test
    @DisplayName("Test should return bad request when the credit is null")
    public void testAddCreditWithNullCredit() throws Exception {
        User user = setUpWithArgs();
        when(balootMock.getUserById("hadi")).thenReturn(user);

        Map<String, String> invalidCreditInput = Collections.singletonMap("credit",null);
        mvc.perform(post("/users/hadi/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidCreditInput)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Credit is null."));
    }
}
