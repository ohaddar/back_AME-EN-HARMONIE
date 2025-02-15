package com.doranco.project.services;

import com.doranco.project.dto.FeedbackDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface FeedbackService {
    FeedbackDTO saveFeedbackForUser(String feedbackJson, Authentication authentication);

    List<FeedbackDTO> getAllFeedbacks();

    List<FeedbackDTO> getPublicFeedbacks();

    Optional<FeedbackDTO> getFeedbackById(Long id);

    FeedbackDTO getFeedbackByUserId(Authentication authentication);
}
