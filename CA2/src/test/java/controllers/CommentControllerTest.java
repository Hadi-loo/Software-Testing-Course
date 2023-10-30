package controllers;

import exceptions.NotExistentComment;
import model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static defines.Errors.NOT_EXISTENT_COMMENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentControllerTest {

    CommentController commentController;

    @BeforeEach
    public void setUp() {
        commentController = new CommentController();
    }

    @Test
    @DisplayName("HTTP status should be correct when liking the comment is successful")
    void testLikeCommentStatusOk() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");

        Baloot balootMock = mock(Baloot.class);
        Comment commentMock = mock(Comment.class);
        when(balootMock.getCommentById(1)).thenReturn(commentMock);
        doNothing().when(commentMock).addUserVote("hadi", "like");

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.likeComment("1", input);
        verify(balootMock).getCommentById(1);
        verify(commentMock).addUserVote("hadi", "like");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when liking the comment is successful")
    void testLikeCommentResponseBody() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");

        Baloot balootMock = mock(Baloot.class);
        Comment commentMock = mock(Comment.class);
        when(balootMock.getCommentById(2)).thenReturn(commentMock);
        doNothing().when(commentMock).addUserVote("hadi", "like");

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.likeComment("2", input);
        verify(balootMock).getCommentById(2);
        verify(commentMock).addUserVote("hadi", "like");
        assertEquals("The comment was successfully liked!", response.getBody());
    }

    @Test
    @DisplayName("Http status should be Not found when comment does not exist to like")
    void testLikeCommentErrorNotFound() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");

        Baloot balootMock = mock(Baloot.class);
        when(balootMock.getCommentById(3)).thenThrow(NotExistentComment.class);

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.likeComment("3", input);
        verify(balootMock).getCommentById(3);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when comment does not exist to like")
    void testLikeCommentErrorBodyMessage() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "hadi");

        Baloot balootMock = mock(Baloot.class);
        NotExistentComment errorMock = mock(NotExistentComment.class);
        when(balootMock.getCommentById(4)).thenThrow(errorMock);
        when(errorMock.getMessage()).thenReturn(NOT_EXISTENT_COMMENT);

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.likeComment("4", input);
        verify(balootMock).getCommentById(4);
        assertEquals(NOT_EXISTENT_COMMENT, response.getBody());
    }

    @Test
    @DisplayName("HTTP status should be correct when disliking the comment is successful")
    void testDislikeCommentStatusOk() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");

        Baloot balootMock = mock(Baloot.class);
        Comment commentMock = mock(Comment.class);
        when(balootMock.getCommentById(1)).thenReturn(commentMock);
        doNothing().when(commentMock).addUserVote("sana", "dislike");

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.dislikeComment("1", input);
        verify(balootMock).getCommentById(1);
        verify(commentMock).addUserVote("sana", "dislike");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Response body should be correct when disliking the comment is successful")
    void testDislikeCommentResponseBody() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");

        Baloot balootMock = mock(Baloot.class);
        Comment commentMock = mock(Comment.class);
        when(balootMock.getCommentById(2)).thenReturn(commentMock);
        doNothing().when(commentMock).addUserVote("sana", "dislike");

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.dislikeComment("2", input);
        verify(balootMock).getCommentById(2);
        verify(commentMock).addUserVote("sana", "dislike");
        assertEquals("The comment was successfully disliked!", response.getBody());
    }

    @Test
    @DisplayName("Http status should be Not found when comment does not exist to dislike")
    void testDislikeCommentErrorNotFound() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");

        Baloot balootMock = mock(Baloot.class);
        when(balootMock.getCommentById(3)).thenThrow(NotExistentComment.class);

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.dislikeComment("3", input);
        verify(balootMock).getCommentById(3);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Error message should be correct when comment does not exist to dislike")
    void testDislikeCommentErrorBodyMessage() throws NotExistentComment {
        Map<String, String> input = new HashMap<>();
        input.put("username", "sana");

        Baloot balootMock = mock(Baloot.class);
        NotExistentComment errorMock = mock(NotExistentComment.class);
        when(balootMock.getCommentById(4)).thenThrow(errorMock);
        when(errorMock.getMessage()).thenReturn(NOT_EXISTENT_COMMENT);

        commentController.setBaloot(balootMock);
        ResponseEntity<String> response = commentController.dislikeComment("4", input);
        verify(balootMock).getCommentById(4);
        assertEquals(NOT_EXISTENT_COMMENT, response.getBody());
    }
    
}
