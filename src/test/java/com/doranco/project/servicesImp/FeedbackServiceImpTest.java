package com.doranco.project.servicesImp;

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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FeedbackServiceImpTest {

    @Mock
    private IFeedbackRepository feedbackRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private FeedbackServiceImp feedbackService;

    private Authentication mockAuth;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAuth = mock(Authentication.class);
    }

    @Test
    public void testSaveFeedbackForUser_Success() {

        String feedbackJson = "{\"title\": \"Test Feedback\", \"content\": \"This is a feedback.\"}";

        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setTitle("Test Feedback");
        feedback.setContent("This is a feedback.");
        feedback.setPublicationDate(new java.util.Date());

        String email = "john.defee@example.com";
        String password = "password123";
        User user = new User(3L, "John", "DeFee", email, "avatar.png", password, RoleEnum.USER);
        feedback.setUser(user);

        when(mockAuth.getPrincipal()).thenReturn(user);

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        Feedback result = feedbackService.saveFeedbackForUser(feedbackJson, mockAuth);

        assertNotNull(result, "The result should not be null");
    }


    @Test
    public void testGetAllFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setId(1L);
        feedback1.setTitle("Feedback 1");

        Feedback feedback2 = new Feedback();
        feedback2.setId(2L);
        feedback2.setTitle("Feedback 2");

        when(feedbackRepository.findAll()).thenReturn(List.of(feedback1, feedback2));

        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();

        assertNotNull(feedbacks);
        assertEquals(2, feedbacks.size());
    }

    @Test
    public void testGetFeedbackById_Success() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setTitle("Test Feedback");

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        Optional<Feedback> result = feedbackService.getFeedbackById(1L);

        assertTrue(result.isPresent());
        assertEquals(feedback, result.get());
    }

    @Test
    public void testGetFeedbackById_NotFound() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown;
        thrown = Assertions.<RuntimeException>assertThrows(RuntimeException.class, () -> {
            feedbackService.getFeedbackById(1L);
        });

        assertEquals("Feedback not found with id: 1", thrown.getMessage());
    }

    @Test
    public void testGetPublicFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setId(1L);
        feedback1.setTitle("Public Feedback 1");

        Feedback feedback2 = new Feedback();
        feedback2.setId(2L);
        feedback2.setTitle("Public Feedback 2");

        when(feedbackRepository.findAll()).thenReturn(List.of(feedback1, feedback2));

        List<Feedback> result = feedbackService.getPublicFeedbacks();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetFeedbackByUserId_Success() {
        User user = new User();
        user.setId(1L);

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setId(1L);

        when(mockAuth.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findFeedbackByUserId(1L)).thenReturn(Optional.of(feedback));

        Feedback result = feedbackService.getFeedbackByUserId(mockAuth);

        assertNotNull(result);
        assertEquals(feedback, result);
    }

    @Test
    public void testGetFeedbackByUserId_NotFound() {
        User user = new User();
        user.setId(1L);

        when(mockAuth.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findFeedbackByUserId(1L)).thenReturn(Optional.empty());

        Feedback result = feedbackService.getFeedbackByUserId(mockAuth);

        assertNull(result);
    }
}
