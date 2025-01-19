package com.doranco.project.servicesImp;

import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IFeedbackRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.FeedbackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class FeedbackServiceImp implements FeedbackService {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    IFeedbackRepository feedbackRepository;

    @Override
    public Feedback saveFeedbackForUser(String feedbackJson, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
                throw new IllegalArgumentException("Authentication is required.");
        }
        try {
            Feedback feedback = new ObjectMapper().readValue(feedbackJson, Feedback.class);

            User authenticatedUser = (User) authentication.getPrincipal();
            feedback.setUser(authenticatedUser);
            feedback.setPublicationDate(new Date());

            return feedbackRepository.save(feedback);
        }catch (IllegalArgumentException e) {
              throw new RuntimeException("Error occurred while saving feedback.", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Feedback>   getAllFeedbacks () {
        return feedbackRepository.findAll();

    }

    @Override
    public Optional<Feedback> getFeedbackById(Long id) {
        Optional<Feedback> feedbacksById = feedbackRepository.findById(id);

        if (feedbacksById.isPresent()) {
            return feedbacksById;
        } else {
            throw new RuntimeException("Feedback not found with id: " + id);
        }
    }

    @Override
    public Feedback getFeedbackByUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication is required.");
        }

        try {
            User authenticatedUser = (User) authentication.getPrincipal();
            Optional<Feedback> feedback = feedbackRepository.findFeedbackByUserId(authenticatedUser.getId());
            return feedback.orElse(null);
         }catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching user feedback.", e);
        }
    }

    @Override
    public List<Feedback> getPublicFeedbacks() {
        try {
            List<Feedback> allFeedbacks = feedbackRepository.findAll();
            return allFeedbacks.size() > 2 ? allFeedbacks.subList(0, 2) : allFeedbacks;
        }catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching public feedbacks.", e);
        }
    }

}
