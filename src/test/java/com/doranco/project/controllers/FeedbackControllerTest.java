package com.doranco.project.controllers;

import com.doranco.project.dto.FeedbackDTO;
import com.doranco.project.dto.UserDTO;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private  FeedbackController feedbackController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();
    }


    @Test
    void testSaveFeedback() throws Exception {
        FeedbackDTO feedback = new FeedbackDTO();
        UserDTO user = new UserDTO(3L, "John", "DeFee", "avatar.png", RoleEnum.USER, "john.defee@example.com");

        feedback.setTitle("Test Title");
        feedback.setId(1L);
        feedback.setContent("Test Content");
        feedback.setPublicationDate(null);
        feedback.setUser(user);

        when(feedbackService.saveFeedbackForUser(any(String.class), any())).thenReturn(feedback);

        mockMvc.perform(post("/feedback/save")
                        .param("feedback", "{\"title\": \"Test Title\", \"content\": \"Test Content\"}")
                        .header("Authorization", "Bearer someToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    void testGetFeedbacks() throws Exception {
        FeedbackDTO feedback1 = new FeedbackDTO();
        feedback1.setTitle("Title 1");
        feedback1.setContent("Content 1");

        FeedbackDTO feedback2 = new FeedbackDTO();
        feedback2.setTitle("Title 2");
        feedback2.setContent("Content 2");

        List<FeedbackDTO> feedbacks = Arrays.asList(feedback1, feedback2);

        when(feedbackService.getAllFeedbacks()).thenReturn(feedbacks);

        mockMvc.perform(get("/feedback/all")
                        .header("Authorization", "Bearer someToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));
    }
    @Test
    void testGetFeedbackById() throws Exception {
        FeedbackDTO feedback = new FeedbackDTO();
        feedback.setId(1L);
        feedback.setTitle("Test Title");
        feedback.setContent("Test Content");

        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));

        mockMvc.perform(get("/feedback/1")
                        .header("Authorization", "Bearer someToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }
    @Test
    void testGetFeedbackByUserId() throws Exception {
        UserDTO user = new UserDTO(3L, "John", "DeFee","avatar.png", RoleEnum.USER, "john.defee@example.com" );

        FeedbackDTO feedback = new FeedbackDTO();
        feedback.setId(1L);
        feedback.setTitle("User Feedback");
        feedback.setContent("Content by John");
        feedback.setUser(user);


        when(feedbackService.getFeedbackByUserId(any())).thenReturn(feedback);

        mockMvc.perform(get("/feedback/user")
                        .header("Authorization", "Bearer someToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("User Feedback"))
                .andExpect(jsonPath("$.content").value("Content by John"));
    }
    @Test
    void testGetPublicFeedbacks() throws Exception {
        FeedbackDTO feedback1 = new FeedbackDTO();
        feedback1.setTitle("Public Feedback 1");
        feedback1.setContent("Public Content 1");

        FeedbackDTO feedback2 = new FeedbackDTO();
        feedback2.setTitle("Public Feedback 2");
        feedback2.setContent("Public Content 2");

        List<FeedbackDTO> feedbacks = Arrays.asList(feedback1, feedback2);

        when(feedbackService.getPublicFeedbacks()).thenReturn(feedbacks);

        mockMvc.perform(get("/feedback/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Public Feedback 1"))
                .andExpect(jsonPath("$[1].title").value("Public Feedback 2"));
    }
    @Test
    void testSaveFeedback_InvalidJson() throws Exception {
        mockMvc.perform(post("/feedback/save")
                        .param("feedback", "")
                        .header("Authorization", "Bearer someToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) ;
    }

}
