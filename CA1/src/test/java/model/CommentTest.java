package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {
    Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment(1, "sana.sarinavaei@gmail.com", "sana", 10, "meh");
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with Id")
    public void InitialIdTest() {
        assertEquals(1, comment.getId());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with userEmail")
    public void InitialEmailTest() {
        assertEquals("sana.sarinavaei@gmail.com", comment.getUserEmail());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with username")
    public void InitialUsernameTest() {
        assertEquals("sana", comment.getUsername());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with commodityId")
    public void InitialCommodityIdTest() {
        assertEquals(10, comment.getCommodityId());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with text")
    public void InitialTextTest() {
        assertEquals("meh", comment.getText());
    }

    @Test
    @DisplayName("Test Comment date not null")
    public void NotNullDateTest() {
        assertNotNull(comment.getDate());
    }

    @Test
    @DisplayName("Test to check the Comment date")
    public void DateTest() {
        // expected date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expected_date = dateFormat.format(currentDate);

        // actual date
        String[] commentDate = comment.getDate().split(" ");
        String actual_date = commentDate[0];

        assertEquals(expected_date, actual_date);
    }

    @Test
    @DisplayName("Test to liked a comment")
    public void CommentLikeTest() {
        int first_like = comment.getLike();
        comment.addUserVote("hadi", "like");
        assertEquals(first_like + 1, comment.getLike());
    }

    @Test
    @DisplayName("Test to disliked a comment")
    public void CommentDislikeTest() {
        int first_dislike = comment.getDislike();
        comment.addUserVote("hadi", "dislike");
        assertEquals(first_dislike + 1, comment.getDislike());
    }

    @Test
    @DisplayName("Test to check the size of UserVote")
    public void UserVoteLengthTest() {
        int length = comment.getUserVote().size();
        comment.addUserVote("sana", "like");
        comment.addUserVote("hadi", "dislike");
        assertEquals(length + 2, comment.getUserVote().size());
    }

    @Test
    @DisplayName("Test to save the data correctly in UserVote")
    public void UserVoteTest() {
        comment.addUserVote("hadi", "dislike");
        assertEquals("dislike", comment.getUserVote().get("hadi"));
    }
}
