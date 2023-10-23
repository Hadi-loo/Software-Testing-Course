package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentTest {
    Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment();
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with Id")
    public void InitialIdTest() {
        comment.setId(1);
        assertEquals(1, comment.getId());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with userEmail")
    public void InitialEmailTest() {
        comment.setUserEmail("sana.sarinavaei@gmail.com");
        assertEquals("sana.sarinavaei@gmail.com", comment.getUserEmail());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with username")
    public void InitialUsernameTest() {
        comment.setUsername("sana");
        assertEquals("sana", comment.getUsername());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with commodityId")
    public void InitialCommodityIdTest() {
        comment.setCommodityId(10);
        assertEquals(10, comment.getCommodityId());
    }

    @Test
    @DisplayName("Simple Test Comment Initialization with text")
    public void InitialTextTest() {
        comment.setText("meh");
        assertEquals("meh", comment.getText());
    }

    @Test
    @DisplayName("Test Comment date not null")
    public void NotNullDateTest() {
        comment = new Comment(1, "sana.sarinavaei@gmail.com", "sana", 10, "meh");
        assertNotNull(comment.getDate());
    }

    @Test
    @DisplayName("Test to check the Comment date")
    public void DateTest() {
        comment = new Comment(1, "sana.sarinavaei@gmail.com", "sana", 10, "meh");
        // expected date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expected_date = dateFormat.format(currentDate);

        // actual date
        String[] commentDate = comment.getDate().split(" ");
        String actual_date = commentDate[0];

        assertEquals(expected_date, actual_date);
    }

    @ParameterizedTest
    @CsvSource({ "hadi, like" })
    @DisplayName("Test to liked a comment")
    public void CommentLikeTest(String username, String vote) {
        int first_like = comment.getLike();
        comment.addUserVote(username, vote);
        assertEquals(first_like + 1, comment.getLike());
    }

    @ParameterizedTest
    @CsvSource({ "hadi, dislike" })
    @DisplayName("Test to disliked a comment")
    public void CommentDislikeTest(String username, String vote) {
        int first_dislike = comment.getDislike();
        comment.addUserVote(username, vote);
        assertEquals(first_dislike + 1, comment.getDislike());
    }

    @ParameterizedTest
    @CsvSource({
            "sana, like",
            "hadi, dislike"
    })
    @DisplayName("Test to check the size of UserVote")
    public void UserVoteLengthTest(String username, String vote) {
        int length = comment.getUserVote().size();
        comment.addUserVote(username, vote);
        assertEquals(length + 1, comment.getUserVote().size());
    }

    @ParameterizedTest
    @CsvSource({ "hadi, dislike" })
    @DisplayName("Test to save the data correctly in UserVote")
    public void UserVoteTest(String username, String vote) {
        comment.addUserVote(username, vote);
        assertEquals("dislike", comment.getUserVote().get("hadi"));
    }
}
