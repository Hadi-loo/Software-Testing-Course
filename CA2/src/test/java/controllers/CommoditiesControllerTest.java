package controllers;

import defines.Errors;
import exceptions.InvalidScoreRange;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;

import service.Baloot;
import model.Comment;
import model.Commodity;
import model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommoditiesControllerTest {

    private CommoditiesController commoditiesController;
    private Baloot balootMock;

    @BeforeEach
    public void setUp() {
        commoditiesController = new CommoditiesController();
        balootMock = mock(Baloot.class);
        commoditiesController.setBaloot(balootMock);
    }

    @Test
    @DisplayName("HTTP status should be correct when getting commodities is successful")
    void testGetCommoditiesStatusOk() {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.getCommodities()).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getCommodities();

        verify(balootMock).getCommodities();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when getting commodities is successful")
    void testGetCommoditiesResponseBody() {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.getCommodities()).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getCommodities();

        verify(balootMock).getCommodities();
        assertEquals(commodities, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be correct when getting a commodity is successful")
    void testGetCommodityStatusOkOnSuccess() throws NotExistentCommodity {
        Commodity commodityMock = mock(Commodity.class);
        when(balootMock.getCommodityById("1")).thenReturn(commodityMock);

        ResponseEntity<Commodity> response = commoditiesController.getCommodity("1");

        verify(balootMock).getCommodityById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when getting a commodity is successful")
    public void testGetCommodityResponseBodyOnSuccess() throws NotExistentCommodity {
        Commodity commodityMock = mock(Commodity.class);
        when(balootMock.getCommodityById("2")).thenReturn(commodityMock);

        ResponseEntity<Commodity> response = commoditiesController.getCommodity("2");

        verify(balootMock).getCommodityById("2");
        assertEquals(commodityMock, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Not found when commodity does not exist")
    public void testGetCommodityErrorNotFoundWhenCommodityDoesNotExist() throws NotExistentCommodity {
        when(balootMock.getCommodityById("3")).thenThrow(new NotExistentCommodity());

        ResponseEntity<Commodity> response = commoditiesController.getCommodity("3");

        verify(balootMock).getCommodityById("3");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be null when commodity does not exist")
    public void testGetCommodityResponseBodyNullWhenCommodityDoesNotExist() throws NotExistentCommodity {
        when(balootMock.getCommodityById("4")).thenThrow(new NotExistentCommodity());

        ResponseEntity<Commodity> response = commoditiesController.getCommodity("4");

        verify(balootMock).getCommodityById("4");
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be correct when rating a commodity is successful")
    public void testRateCommodityStatusOkOnSuccess() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "5");
        Commodity commodityMock = mock(Commodity.class);
        when(balootMock.getCommodityById("1")).thenReturn(commodityMock);
        doNothing().when(commodityMock).addRate("sana", 5);

        ResponseEntity<String> response = commoditiesController.rateCommodity("1", input);

        verify(balootMock).getCommodityById("1");
        verify(commodityMock).addRate("sana", 5);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when rating a commodity is successful")
    public void testRateCommodityResponseBodyOnSuccess() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "5");
        Commodity commodityMock = mock(Commodity.class);
        when(balootMock.getCommodityById("2")).thenReturn(commodityMock);
        doNothing().when(commodityMock).addRate("sana", 5);

        ResponseEntity<String> response = commoditiesController.rateCommodity("2", input);

        verify(balootMock).getCommodityById("2");
        verify(commodityMock).addRate("sana", 5);
        assertEquals("rate added successfully!", response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Not found when commodity does not exist to rate")
    public void testRateCommodityErrorNotFoundWhenCommodityDoesNotExist() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "5");
        when(balootMock.getCommodityById("3")).thenThrow(new NotExistentCommodity());

        ResponseEntity<String> response = commoditiesController.rateCommodity("3", input);

        verify(balootMock).getCommodityById("3");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when commodity does not exist to rate")
    public void testRateCommodityErrorBodyMessageWhenCommodityDoesNotExist() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "5");
        when(balootMock.getCommodityById("4")).thenThrow(new NotExistentCommodity());

        ResponseEntity<String> response = commoditiesController.rateCommodity("4", input);

        verify(balootMock).getCommodityById("4");
        assertEquals(Errors.NOT_EXISTENT_COMMODITY, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Bad request when rate is not a number")
    public void testRateCommodityErrorBadRequestWhenRateIsNotANumber() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "five");

        ResponseEntity<String> response = commoditiesController.rateCommodity("5", input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when rate is not a number")
    public void testRateCommodityErrorBodyMessageWhenRateIsNotANumber() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "five");

        ResponseEntity<String> response = commoditiesController.rateCommodity("6", input);

        assertEquals("For input string: \"five\"", response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Bad request when rate is out of bounds")
    public void testRateCommodityErrorBadRequestWhenRateIsOutOfBounds() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "11");
        Commodity commodityMock = mock(Commodity.class);
        when(balootMock.getCommodityById("7")).thenReturn(commodityMock);
        doThrow(new InvalidScoreRange()).when(commodityMock).addRate("sana", 11);

        ResponseEntity<String> response = commoditiesController.rateCommodity("7", input);

        verify(balootMock).getCommodityById("7");
        verify(commodityMock).addRate("sana", 11);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when rate is out of bounds")
    public void testRateCommodityErrorBodyMessageWhenRateIsOutOfBounds() throws NotExistentCommodity, NumberFormatException, InvalidScoreRange {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("rate", "11");
        Commodity commodityMock = mock(Commodity.class);
        when(balootMock.getCommodityById("8")).thenReturn(commodityMock);
        doThrow(new InvalidScoreRange()).when(commodityMock).addRate("sana", 11);

        ResponseEntity<String> response = commoditiesController.rateCommodity("8", input);

        verify(balootMock).getCommodityById("8");
        verify(commodityMock).addRate("sana", 11);
        assertEquals(Errors.INVALID_RATE_RANGE, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when adding a comment is successful")
    public void testAddCommodityCommentStatusOkOnSuccess() throws NotExistentUser {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("comment", "this is a comment");
        User userMock = mock(User.class);

        when(balootMock.generateCommentId()).thenReturn(1);
        when(balootMock.getUserById("sana")).thenReturn(userMock);
        when(userMock.getEmail()).thenReturn("sana.sarinavaei@gmail.com");
        when(userMock.getUsername()).thenReturn("sana");
        doNothing().when(balootMock).addComment(argThat(comment -> comment.getId() == 1));

        ResponseEntity<String> response = commoditiesController.addCommodityComment("1", input);

        verify(balootMock).generateCommentId();
        verify(balootMock).getUserById("sana");
        verify(userMock).getEmail();
        verify(userMock).getUsername();
        verify(balootMock).addComment(argThat(comment -> comment.getId() == 1));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when adding a comment is successful")
    public void testAddCommodityCommentResponseBodyOnSuccess() throws NotExistentUser {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("comment", "this is a comment");
        User userMock = mock(User.class);

        when(balootMock.generateCommentId()).thenReturn(2);
        when(balootMock.getUserById("sana")).thenReturn(userMock);
        when(userMock.getEmail()).thenReturn("sana.sarinavaei@gmail.com");
        when(userMock.getUsername()).thenReturn("sana");
        doNothing().when(balootMock).addComment(argThat(comment -> comment.getId() == 2));

        ResponseEntity<String> response = commoditiesController.addCommodityComment("2", input);

        verify(balootMock).generateCommentId();
        verify(balootMock).getUserById("sana");
        verify(userMock).getEmail();
        verify(userMock).getUsername();
        verify(balootMock).addComment(argThat(comment -> comment.getId() == 2));
        assertEquals("comment added successfully!", response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Not found when user does not exist to add comment")
    public void testAddCommodityCommentErrorNotFoundWhenUserDoesNotExist() throws NotExistentUser {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("comment", "this is a comment");

        when(balootMock.generateCommentId()).thenReturn(3);
        when(balootMock.getUserById("sana")).thenThrow(new NotExistentUser());

        ResponseEntity<String> response = commoditiesController.addCommodityComment("3", input);

        verify(balootMock).generateCommentId();
        verify(balootMock).getUserById("sana");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when user does not exist to add comment")
    public void testAddCommodityCommentErrorBodyMessageWhenUserDoesNotExist() throws NotExistentUser {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");
        input.put("comment", "this is a comment");

        when(balootMock.generateCommentId()).thenReturn(4);
        when(balootMock.getUserById("sana")).thenThrow(new NotExistentUser());

        ResponseEntity<String> response = commoditiesController.addCommodityComment("4", input);

        verify(balootMock).generateCommentId();
        verify(balootMock).getUserById("sana");
        assertEquals(Errors.NOT_EXISTENT_USER, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when getting a commodity's comments is successful")
    public void testGetCommodityCommentStatusOkOnSuccess() {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(mock(Comment.class));
        comments.add(mock(Comment.class));
        when(balootMock.getCommentsForCommodity(1)).thenReturn(comments);

        ResponseEntity<ArrayList<Comment>> response = commoditiesController.getCommodityComment("1");

        verify(balootMock).getCommentsForCommodity(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when getting a commodity's comments is successful")
    public void testGetCommodityCommentResponseBodyOnSuccess() {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(mock(Comment.class));
        comments.add(mock(Comment.class));
        when(balootMock.getCommentsForCommodity(2)).thenReturn(comments);

        ResponseEntity<ArrayList<Comment>> response = commoditiesController.getCommodityComment("2");

        verify(balootMock).getCommentsForCommodity(2);
        assertEquals(comments, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Bad Request when id is not a number")
    public void testGetCommodityCommentErrorBadRequestWhenIdIsNotANumber() {
        ResponseEntity<ArrayList<Comment>> response = commoditiesController.getCommodityComment("three");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("response body should be null when id is not a number")
    public void testGetCommodityCommentResponseBodyNullWhenIdIsNotANumber() {
        ResponseEntity<ArrayList<Comment>> response = commoditiesController.getCommodityComment("four");

        assertNull(response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when searching commodities with name is successful")
    public void testSearchCommoditiesWithNameStatusOkOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "name");
        input.put("searchValue", "sana");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.filterCommoditiesByName("sana")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        verify(balootMock).filterCommoditiesByName("sana");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when searching commodities with name is successful")
    public void testSearchCommoditiesWithNameResponseBodyOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "name");
        input.put("searchValue", "sana");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.filterCommoditiesByName("sana")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        verify(balootMock).filterCommoditiesByName("sana");
        assertEquals(commodities, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when searching commodities with category is successful")
    public void testSearchCommoditiesWithCategoryStatusOkOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "category");
        input.put("searchValue", "makeup");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.filterCommoditiesByCategory("makeup")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        verify(balootMock).filterCommoditiesByCategory("makeup");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when searching commodities with category is successful")
    public void testSearchCommoditiesWithCategoryResponseBodyOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "category");
        input.put("searchValue", "makeup");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.filterCommoditiesByCategory("makeup")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        verify(balootMock).filterCommoditiesByCategory("makeup");
        assertEquals(commodities, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when searching commodities with provider is successful")
    public void testSearchCommoditiesWithProviderStatusOkOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "provider");
        input.put("searchValue", "sana");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.filterCommoditiesByProviderName("sana")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        verify(balootMock).filterCommoditiesByProviderName("sana");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when searching commodities with provider is successful")
    public void testSearchCommoditiesWithProviderResponseBodyOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "provider");
        input.put("searchValue", "sana");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.filterCommoditiesByProviderName("sana")).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        verify(balootMock).filterCommoditiesByProviderName("sana");
        assertEquals(commodities, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when searching commodities with other options is successful")
    public void testSearchCommoditiesWithOtherOptionsStatusOkOnSuccess() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "other");
        input.put("searchValue", "sana");
        ArrayList<Commodity> commodities = new ArrayList<>();

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be empty list when searching commodities with other options is successful")
    public void testSearchCommoditiesWithOtherOptionsResponseBody() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "other");
        input.put("searchValue", "sana");
        ArrayList<Commodity> commodities = new ArrayList<>();

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        assertEquals(commodities, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be OK when getting suggested commodities is successful")
    public void testGetSuggestedCommoditiesStatusOkOnSuccess() throws NotExistentCommodity {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.getCommodityById("1")).thenReturn(mock(Commodity.class));
        when(balootMock.suggestSimilarCommodities(any(Commodity.class))).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities("1");

        verify(balootMock).getCommodityById("1");
        verify(balootMock).suggestSimilarCommodities(any(Commodity.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when getting suggested commodities is successful")
    public void testGetSuggestedCommoditiesResponseBodyOnSuccess() throws NotExistentCommodity {
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(mock(Commodity.class));
        commodities.add(mock(Commodity.class));
        when(balootMock.getCommodityById("2")).thenReturn(mock(Commodity.class));
        when(balootMock.suggestSimilarCommodities(any(Commodity.class))).thenReturn(commodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities("2");

        verify(balootMock).getCommodityById("2");
        verify(balootMock).suggestSimilarCommodities(any(Commodity.class));
        assertEquals(commodities, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be Not found when commodity does not exist to get suggested commodities")
    public void testGetSuggestedCommoditiesErrorNotFoundWhenCommodityDoesNotExist() throws NotExistentCommodity {
        when(balootMock.getCommodityById("3")).thenThrow(new NotExistentCommodity());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities("3");

        verify(balootMock).getCommodityById("3");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be empty list when commodity does not exist to get suggested commodities")
    public void testGetSuggestedCommoditiesResponseBodyEmptyListWhenCommodityDoesNotExist() throws NotExistentCommodity {
        when(balootMock.getCommodityById("4")).thenThrow(new NotExistentCommodity());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities("4");

        verify(balootMock).getCommodityById("4");
        assertEquals(new ArrayList<>(), response.getBody());
    }

}