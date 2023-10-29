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

    public void setUpWithArgs() {
        comment = new Comment(1, "sana.sarinavaei@gmail.com", "sana", 10, "meh");
    }

    @Test
    @DisplayName("comment id initialed correctly")
    public void InitialIdTest() {
        setUpWithArgs();
        assertEquals(1, comment.getId());
    }

    @Test
    @DisplayName("comment email initialed correctly")
    public void InitialEmailTest() {
        setUpWithArgs();
        assertEquals("sana.sarinavaei@gmail.com", comment.getUserEmail());
    }

    @Test
    @DisplayName("comment username initialed correctly")
    public void InitialUsernameTest() {
        setUpWithArgs();
        assertEquals("sana", comment.getUsername());
    }

    @Test
    @DisplayName("comment commodityId initialed correctly")
    public void InitialCommodityIdTest() {
        setUpWithArgs();
        assertEquals(10, comment.getCommodityId());
    }

    @Test
    @DisplayName("comment text initialed correctly")
    public void InitialTextTest() {
        setUpWithArgs();
        assertEquals("meh", comment.getText());
    }

    @Test
    @DisplayName("comment date should not be null")
    public void NotNullDateTest() {
        comment = new Comment(1, "sana.sarinavaei@gmail.com", "sana", 10, "meh");
        assertNotNull(comment.getDate());
    }

    @Test
    @DisplayName("comment date format should be correct")
    public void CorrectDateFormatTest() {
        setUpWithArgs();
        assertTrue(comment.getDate().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Test to check the Comment date")
    public void DateTest() {
        comment = new Comment(1, "sana.sarinavaei@gmail.com", "sana", 10, "meh");

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expected_date = dateFormat.format(currentDate);

        String[] commentDate = comment.getDate().split(" ");
        String actual_date = commentDate[0];

        assertEquals(expected_date, actual_date);
    }

    @ParameterizedTest
    @CsvSource({
            "sana, like",
            "hadi, dislike"
    })
    @DisplayName("record added to UserVote")
    public void recordAddedToUserVoteTest(String username, String vote) {
        comment.addUserVote(username, vote);
        assertTrue(comment.getUserVote().containsKey(username));
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
    @CsvSource({ "hadi, dislike" })
    @DisplayName("Test to save the data correctly in UserVote")
    public void UserVoteTest(String username, String vote) {
        comment.addUserVote(username, vote);
        assertEquals("dislike", comment.getUserVote().get("hadi"));
    }
}
