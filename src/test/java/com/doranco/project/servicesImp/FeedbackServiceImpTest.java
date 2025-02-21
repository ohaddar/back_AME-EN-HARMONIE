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

    private Authentication mockAuth;
    private User user;
    private Feedback feedback;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAuth = mock(Authentication.class);
        user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("DeFee");
        user.setAvatar("avatar.png");
        user.setRole(RoleEnum.USER);
        user.setEmail("john.defee@example.com");

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setTitle("Test Feedback");
        feedback.setContent("This is a feedback.");
        feedback.setPublicationDate(new Date());
        feedback.setUser(user);
    }

    @Test
    public void testSaveFeedbackForUser_Success() {

        String feedbackJson = "{\"title\": \"Test Feedback\", \"content\": \"This is a feedback.\"}";
        when(authentication.getPrincipal()).thenReturn(user);
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> {
            Feedback savedFeedback = invocation.getArgument(0);
            savedFeedback.setId(1L);
            return savedFeedback;
        });

        FeedbackDTO result = feedbackService.saveFeedbackForUser(feedbackJson, authentication);

        assertNotNull(result, "The result should not be null");
        assertEquals("Test Feedback", result.getTitle());
        assertEquals("This is a feedback.", result.getContent());
    }


    @Test
    public void testGetAllFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setId(1L);
        feedback1.setTitle("Feedback 1");
        feedback1.setUser(user);

        Feedback feedback2 = new Feedback();
        feedback2.setId(2L);
        feedback2.setTitle("Feedback 2");
        feedback2.setUser(user);

        when(feedbackRepository.findAll()).thenReturn(List.of(feedback1, feedback2));

        List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbacks();

        assertNotNull(feedbacks);
        assertEquals(2, feedbacks.size());
    }

    @Test
    public void testGetFeedbackById_Success() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        Optional<FeedbackDTO> result = feedbackService.getFeedbackById(1L);

        assertTrue(result.isPresent());
        assertEquals(feedback.getId(), result.get().getId());
        assertEquals(feedback.getTitle(), result.get().getTitle());
        assertEquals(feedback.getContent(), result.get().getContent());
        assertEquals(feedback.getUser().getId(), result.get().getUser().getId());
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
    public void testGetFeedbackByUserId_Success() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findFeedbackByUserId(1L)).thenReturn(Optional.of(feedback));

        FeedbackDTO result = feedbackService.getFeedbackByUserId(authentication);

        assertNotNull(result);
        assertEquals("Test Feedback", result.getTitle());
    }

    @Test
    public void testGetFeedbackByUserId_NotFound() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(feedbackRepository.findFeedbackByUserId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            feedbackService.getFeedbackByUserId(authentication);
        });
        assertEquals("Error occurred while fetching user feedback.", exception.getMessage());

    }
}
