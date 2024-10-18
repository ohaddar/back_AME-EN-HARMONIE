package com.doranco.project.services;

import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface FeedbackService {
    public User addUserWithFeedback(User user, Feedback feedback);
    public Feedback updateFeedbackById(Long id, Feedback updatedFeedback);
    public List<Feedback> getAllFeedbacks ();
    public List<Feedback> getFeedbacksByRating (Sort.Direction direction);
}
