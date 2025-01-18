package com.doranco.project.services;

import com.doranco.project.entities.Blog;
import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface FeedbackService {
    Feedback addFeedback(Feedback feedback);
    Feedback updateFeedbackById(Long id, Feedback updatedFeedback);
    List<Feedback> getAllFeedbacks();
    Optional<Feedback>getFeedbackById(Long id);
    Optional<Feedback>getFeedbackByUserId(Long userId);

}
