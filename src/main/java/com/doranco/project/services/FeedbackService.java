package com.doranco.project.services;

import com.doranco.project.entities.Blog;
import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface FeedbackService {
    Feedback saveFeedbackForUser(String feedbackJson, Authentication authentication);
    List<Feedback> getAllFeedbacks();
    List<Feedback> getPublicFeedbacks();
    Optional<Feedback> getFeedbackById(Long id);
    Feedback getFeedbackByUserId(Authentication authentication);
}
