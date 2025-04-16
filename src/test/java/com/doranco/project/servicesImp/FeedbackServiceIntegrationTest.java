package com.doranco.project.servicesImp;
import com.doranco.project.dto.FeedbackDTO;
import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.repositories.IFeedbackRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
@ActiveProfiles("test")
public class FeedbackServiceIntegrationTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Autowired
    private IUserRepository userRepository;

    private User testUser;
    private Feedback testFeedback;
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        feedbackRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setId("1L");
        testUser.setFirstname("Test");
        testUser.setLastname("User");
        testUser.setEmail("testuser" + System.currentTimeMillis() + "@example.com");
        testUser.setPassword("password123");
        testUser.setAvatar("avatar.png");
        testUser.setRole(RoleEnum.USER);

        testUser = userRepository.save(testUser);

        testFeedback = new Feedback();
        testFeedback.setTitle("Test Feedback");
        testFeedback.setContent("This is a test feedback.");
        testFeedback.setUser(testUser);
        testFeedback.setPublicationDate(new Date());
        testFeedback = feedbackRepository.save(testFeedback);

        authentication = new UsernamePasswordAuthenticationToken(testUser, null);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testSaveFeedbackForUser() throws Exception {
        String feedbackJson = "{\"title\":\"New Feedback\",\"id\":\"3\",\"content\":\"Test content\"}";

        FeedbackDTO result = feedbackService.saveFeedbackForUser(feedbackJson, authentication);

        assertNotNull(result);
        assertEquals("New Feedback", result.getTitle());
        assertEquals("Test content", result.getContent());
        assertEquals(testUser.getId(), result.getUser().getId());
    }

    @Test
    public void testGetAllFeedbacks() {
        var feedbacks = feedbackService.getAllFeedbacks();

        assertNotNull(feedbacks);
        assertFalse(feedbacks.isEmpty());
    }

    @Test
    public void testGetFeedbackById() {
        Optional<FeedbackDTO> result = feedbackService.getFeedbackById(testFeedback.getId());

        assertTrue(result.isPresent());
        assertEquals(testFeedback.getId(), result.get().getId());
    }

    @Test
    public void testGetFeedbackByUserId() {
        FeedbackDTO result = feedbackService.getFeedbackByUserId(authentication);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testFeedback.getId(), result.getId());
    }

    @Test
    public void testSaveFeedbackForUser_invalidAuthentication() {
        authentication = null;

        String feedbackJson = "{\"title\":\"Invalid Feedback\",\"content\":\"No authentication\"}";

        assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.saveFeedbackForUser(feedbackJson, authentication);
        });
    }
}
