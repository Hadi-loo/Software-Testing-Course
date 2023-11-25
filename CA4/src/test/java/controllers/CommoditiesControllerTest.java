package controllers;

import application.BalootApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;
import model.Comment;
import model.Commodity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static defines.Errors.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class CommoditiesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommoditiesController commoditiesController;

    @MockBean
    private Baloot balootMock;

    @BeforeEach
    public void setUp() {
        commoditiesController.setBaloot(balootMock);
    }

    public User setUpWithArgs(){
        return new User("hadi", "1234", "m.h.babalu@gmail.com", "2002-11-03", "Tehran, Iran");
    }

    @Test
    @DisplayName("Test should return the commodities correctly when it is empty")
    public void testReturnCommodityCorrectlyWhenEmpty() throws Exception {
        ArrayList<Commodity> commodities = new ArrayList<>();
        when(balootMock.getCommodities()).thenReturn(commodities);

        mvc.perform(get("/commodities"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Test should return the commodities correctly")
    public void testReturnCommodityCorrectly() throws Exception {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(new Commodity());
        commodities.add(new Commodity());
        when(balootMock.getCommodities()).thenReturn(commodities);

        mvc.perform(get("/commodities"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"));
    }

    @Test
    @DisplayName("Test should return the status code 200 and the commodity info when the commodity exists")
    public void testReturnsCommodityInfoWhenExists() throws Exception {
        Commodity commodity = new Commodity();
        when(balootMock.getCommodityById("1")).thenReturn(commodity);

        mvc.perform(get("/commodities/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    @DisplayName("Test should return the status code 404 when the commodity does not exist")
    public void testReturnsNotFoundWhenCommodityDoesNotExist() throws Exception {
        when(balootMock.getCommodityById("1")).thenThrow(new NotExistentCommodity());

        mvc.perform(get("/commodities/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Rate Commodity should be correct when the commodity exists")
    public void testRateCommodityCorrectly() throws Exception {
        Commodity commodity = new Commodity();
        when(balootMock.getCommodityById("1")).thenReturn(commodity);

        Map<String, String> input = Map.of("rate", "5", "username", "sana");
        mvc.perform(post("/commodities/1/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().string("rate added successfully!"));
    }

    @Test
    @DisplayName("Rate Commodity should return 404 when the commodity does not exist")
    public void testRateCommodityNotFoundWhenCommodityDoesNotExist() throws Exception {
        when(balootMock.getCommodityById("1")).thenThrow(new NotExistentCommodity());

        Map<String, String> input = Map.of("rate", "5", "username", "sana");
        mvc.perform(post("/commodities/1/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_EXISTENT_COMMODITY));
    }

    @ParameterizedTest
    @DisplayName("Rate Commodity should return bad request when the rate range is invalid")
    @ValueSource(strings = {"-1", "0", "20"})
    public void testRateCommodityBadRequestWhenRateRangeIsInvalid(String rate) throws Exception {
        Commodity commodity = new Commodity();
        when(balootMock.getCommodityById("1")).thenReturn(commodity);

        Map<String, String> input = Map.of("rate", rate, "username", "sana");
        mvc.perform(post("/commodities/1/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_RATE_RANGE));
    }

    @Test
    @DisplayName("Rate Commodity should return bad request when rate is null")
    public void testRateCommodityBadRequestWhenRateIsNull() throws Exception {
        Commodity commodity = new Commodity();
        when(balootMock.getCommodityById("1")).thenReturn(commodity);

        Map<String, String> input = new HashMap<>();
        input.put("rate", null);
        input.put("username", "sana");
        mvc.perform(post("/commodities/1/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("rate is null."));
    }

    @Test
    @DisplayName("Add Commodity Comment should be correct")
    public void testAddCommodityCommentCorrectly() throws Exception {
        User user = setUpWithArgs();
        when(balootMock.generateCommentId()).thenReturn(1);
        when(balootMock.getUserById("hadi")).thenReturn(user);
        Map<String, String> input = Map.of("username", "hadi", "comment", "meow");
        mvc.perform(post("/commodities/1/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().string("comment added successfully!"));
    }

    @Test
    @DisplayName("Test should be NotFound when the User does not exist to add comment")
    public void testAddCommodityCommentNotFoundWhenUserDoesNotExist() throws Exception {
        when(balootMock.generateCommentId()).thenReturn(1);
        when(balootMock.getUserById("sana")).thenThrow(new NotExistentUser());
        Map<String, String> input = Map.of("username", "sana", "comment", "meow-meow");
        mvc.perform(post("/commodities/1/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_EXISTENT_USER));
    }

    @Test
    @DisplayName("Test should be NotFound when the User is null")
    public void testAddCommodityCommentNotFoundWhenUserNull() throws Exception {
        when(balootMock.generateCommentId()).thenReturn(1);
        when(balootMock.getUserById(not(eq("sana")))).thenThrow(new NotExistentUser());
        Map<String, String> input = new HashMap<>();
        input.put("username", null);
        input.put("comment", "meow-meow");
        mvc.perform(post("/commodities/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_EXISTENT_USER));
    }

    @Test
    @DisplayName("Test should be correct when getting a commodity's comments is successful")
    public void testGetCommodityCommentCorrectly() throws Exception {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        when(balootMock.getCommentsForCommodity(1)).thenReturn(comments);

        mvc.perform(get("/commodities/1/comment"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    @DisplayName("Search commodity should be bad request when searching commodities with searchValue null")
    public void testSearchCommoditiesWithSearchValueNull() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "whatever");
        input.put("searchValue", null);
        mvc.perform(post("/commodities/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Search commodity should be correct when searching commodities with name is successful")
    public void testSearchCommoditiesWithNameSuccess() throws Exception {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(new Commodity());
        when(balootMock.filterCommoditiesByName("sana")).thenReturn(commodities);

        Map<String, String> input = Map.of("searchOption", "name", "searchValue", "sana");
        mvc.perform(post("/commodities/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    @DisplayName("Search commodity should be correct when searching commodities with category is successful")
    public void testSearchCommoditiesWithCategorySuccess() throws Exception {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(new Commodity());
        when(balootMock.filterCommoditiesByCategory("makeup")).thenReturn(commodities);

        Map<String, String> input = Map.of("searchOption", "category", "searchValue", "makeup");
        mvc.perform(post("/commodities/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    @DisplayName("Search commodity should be correct when searching commodities with provider is successful")
    public void testSearchCommoditiesWithProviderSuccess() throws Exception {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(new Commodity());
        when(balootMock.filterCommoditiesByProviderName("sana")).thenReturn(commodities);

        Map<String, String> input = Map.of("searchOption", "provider", "searchValue", "sana");
        mvc.perform(post("/commodities/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    @DisplayName("Search commodity should be correct when searching commodities with other option is successful")
    public void testSearchCommoditiesWithOtherOptionSuccess() throws Exception {
        Map<String, String> input = Map.of("searchOption", "other", "searchValue", "hadi");
        mvc.perform(post("/commodities/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Test should be correct when getting suggested commodities is successful")
    public void testGetSuggestedCommoditiesSuccess() throws Exception {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(new Commodity());
        when(balootMock.getCommodityById("2")).thenReturn(mock(Commodity.class));
        when(balootMock.suggestSimilarCommodities(any(Commodity.class))).thenReturn(commodities);

        mvc.perform(get("/commodities/2/suggested"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    @DisplayName("Test should return Not found when commodity does not exist to get suggested commodities")
    public void testGetSuggestedCommoditiesErrorNotFoundWhenCommodityDoesNotExist() throws Exception {
        when(balootMock.getCommodityById("3")).thenThrow(new NotExistentCommodity());

        mvc.perform(get("/commodities/3/suggested"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("[]"));
    }
}
