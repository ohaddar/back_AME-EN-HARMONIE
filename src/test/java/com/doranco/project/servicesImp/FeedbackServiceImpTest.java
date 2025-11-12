package com.doranco.project.servicesImp;

import com.doranco.project.dto.FeedbackDTO;
import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.repositories.IFeedbackRepository;
import com.doranco.project.repositories.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FeedbackServiceImpTest {

    @Mock
    private IFeedbackRepository feedbackRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FeedbackServiceImp feedbackService;

    private User user;
    private Feedback feedback;
    private Feedback savedFeedback;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId("1L");
        user.setFirstname("John");
        user.setLastname("DeFee");
        user.setAvatar("avatar.png");
        user.setRole(RoleEnum.USER);
        user.setEmail("john.defee@example.com");

        feedback = new Feedback();
        feedback.setId("1L");
        feedback.setTitle("Test Feedback");
        feedback.setContent("This is a feedback.");
        feedback.setPublicationDate(new Date());
        feedback.setUser(user);
        savedFeedback = new Feedback();
        savedFeedback.setId("2L");
        savedFeedback.setTitle("Great Service");
        savedFeedback.setContent("Very helpful");
        savedFeedback.setPublicationDate(new Date());
        savedFeedback.setUser(user);
    }

    private Feedback createTestFeedback(String title, String content) {
        Feedback feedback = new Feedback();
        feedback.setTitle(title);
        feedback.setContent(content);
        return feedback;
    }

    private Feedback createTestFeedbackWithUser(String title, String content, User user) {
        Feedback feedback = new Feedback();
        feedback.setTitle(title);
        feedback.setContent(content);
        feedback.setUser(user);
        feedback.setPublicationDate(new Date());
        return feedback;
    }

    @Test
    public void testSaveFeedbackForUser_success() {
        Feedback feedbackToSave = createTestFeedback("Great Service", "Very helpful");

        when(authentication.getPrincipal()).thenReturn(user);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);

        FeedbackDTO result = feedbackService.saveFeedbackForUser(feedbackToSave, authentication);
        assertNotNull(result);
        assertEquals("Great Service", result.getTitle());
        assertEquals("Very helpful", result.getContent());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    public void testSaveFeedbackForUser_invalidAuthentication() {
        authentication = null;
        Feedback feedbackToSave = createTestFeedback("Invalid Feedback", "No authentication");

        assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.saveFeedbackForUser(feedbackToSave, authentication);
        });
    }

    @Test
    public void testSaveFeedbackForUser_nullFeedback() {
        when(authentication.getPrincipal()).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.saveFeedbackForUser(null, authentication);
        });
    }

    @Test
    public void testGetAllFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setId("1L");
        feedback1.setTitle("Feedback 1");
        feedback1.setContent("Content 1");
        feedback1.setPublicationDate(new Date());
        feedback1.setUser(user);

        Feedback feedback2 = new Feedback();
        feedback2.setId("2L");
        feedback2.setTitle("Feedback 2");
        feedback2.setContent("Content 2");
        feedback2.setPublicationDate(new Date());
        feedback2.setUser(user);

        when(feedbackRepository.findAll()).thenReturn(List.of(feedback1, feedback2));

        List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbacks();

        assertEquals(2, feedbacks.size());
        assertEquals("Feedback 1", feedbacks.get(0).getTitle());
        assertEquals("Feedback 2", feedbacks.get(1).getTitle());
    }

    @Test
    public void testGetFeedbackById_Success() {
        when(feedbackRepository.findById("1L")).thenReturn(Optional.of(feedback));

        Optional<FeedbackDTO> result = feedbackService.getFeedbackById("1L");

        assertTrue(result.isPresent());
        assertEquals(feedback.getId(), result.get().getId());
        assertEquals(feedback.getTitle(), result.get().getTitle());
        assertEquals(feedback.getContent(), result.get().getContent());
        assertEquals(feedback.getUser().getId(), result.get().getUser().getId());
    }

    @Test
    public void testGetFeedbackById_NotFound() {
        when(feedbackRepository.findById("1L")).thenReturn(Optional.empty());

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            feedbackService.getFeedbackById("1L");
        });

        assertEquals("Feedback not found with id: 1L", thrown.getMessage());
    }

    @Test
    public void testGetFeedbackByUserId_Success() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findFeedbackByUserId("1L")).thenReturn(Optional.of(feedback));

        FeedbackDTO result = feedbackService.getFeedbackByUserId(authentication);

        assertNotNull(result);
        assertEquals("Test Feedback", result.getTitle());
        assertEquals("This is a feedback.", result.getContent());
        assertEquals(user.getId(), result.getUser().getId());
    }

    @Test
    public void testGetFeedbackByUserId_NotFound() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findFeedbackByUserId("1L")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            feedbackService.getFeedbackByUserId(authentication);
        });
        assertEquals("Error occurred while fetching user feedback.", exception.getMessage());
    }

    @Test
    public void testGetFeedbackByUserId_nullAuthentication() {
        authentication = null;

        assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.getFeedbackByUserId(authentication);
        });
    }
}
