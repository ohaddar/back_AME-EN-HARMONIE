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
    public Feedback addFeedback(Feedback feedback);
    public Feedback updateFeedbackById(Long id, Feedback updatedFeedback);
    public List<Feedback> getAllFeedbacks ();
    public Optional<Feedback>getFeedbackById(Long id);
    public List<Feedback> getFeedbacksByRating (Sort.Direction direction);
}
