package com.doranco.project.servicesImp;

import com.doranco.project.dto.FeedbackDTO;
import com.doranco.project.dto.UserDTO;
import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IFeedbackRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.FeedbackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class FeedbackServiceImp implements FeedbackService {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    IFeedbackRepository feedbackRepository;




    @Override
    public FeedbackDTO saveFeedbackForUser(String feedbackJson, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
                throw new IllegalArgumentException("Authentication is required.");
        }
        try {
            Feedback feedback = new ObjectMapper().readValue(feedbackJson, Feedback.class);

            User authenticatedUser = (User) authentication.getPrincipal();
            feedback.setUser(authenticatedUser);
            feedback.setPublicationDate(new Date());
            Feedback savedFeedback = feedbackRepository.save(feedback);
            return new FeedbackDTO(
                    savedFeedback.getId(),
                    savedFeedback.getTitle(),
                    savedFeedback.getContent(),
                    savedFeedback.getPublicationDate(),
                    new UserDTO(
                            savedFeedback.getUser().getId(),
                            savedFeedback.getUser().getFirstname(),
                            savedFeedback.getUser().getLastname(),
                            savedFeedback.getUser().getAvatar(),
                            savedFeedback.getUser().getRole(),
                            savedFeedback.getUser().getUsername()));
        }catch (IllegalArgumentException e) {
              throw new RuntimeException("Error occurred while saving feedback.", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks () {
        return feedbackRepository.findAll().stream().map(feedback -> new FeedbackDTO(
                        feedback.getId(),
                        feedback.getTitle(),
                        feedback.getContent(),
                        feedback.getPublicationDate(),
                        new UserDTO(
                                feedback.getUser().getId(),
                                feedback.getUser().getFirstname(),
                                feedback.getUser().getLastname(),
                                feedback.getUser().getAvatar(),
                                feedback.getUser().getRole(),
                                feedback.getUser().getUsername()
                        )
                ))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<FeedbackDTO> getFeedbackById(String id) {
        Optional<Feedback> feedbacksById = feedbackRepository.findById(id);

        if (feedbacksById.isPresent()) {
            return   feedbacksById.map(feedback -> new FeedbackDTO(
                    feedback.getId(),
                    feedback.getTitle(),
                    feedback.getContent(),
                    feedback.getPublicationDate(),
                    new UserDTO(
                            feedback.getUser().getId(),
                            feedback.getUser().getFirstname(),
                            feedback.getUser().getLastname(),
                            feedback.getUser().getAvatar(),
                            feedback.getUser().getRole(),
                            feedback.getUser().getUsername()
                    )
            ));
        } else {
            throw new RuntimeException("Feedback not found with id: " + id);
        }
    }

    @Override
    public FeedbackDTO getFeedbackByUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication is required.");
        }

        try {
            User authenticatedUser = (User) authentication.getPrincipal();
            Optional<Feedback> userFeedback = feedbackRepository.findFeedbackByUserId(authenticatedUser.getId());
            if (userFeedback.isEmpty()) {
                throw new RuntimeException("Feedback not found for user ID: " + authenticatedUser.getId());
            }
            Feedback feedback = userFeedback.get();

            return new FeedbackDTO(
                    feedback.getId(),
                    feedback.getTitle(),
                    feedback.getContent(),
                    feedback.getPublicationDate(),
                    new UserDTO(
                            feedback.getUser().getId(),
                            feedback.getUser().getFirstname(),
                            feedback.getUser().getLastname(),
                            feedback.getUser().getAvatar(),
                            feedback.getUser().getRole(),
                            feedback.getUser().getUsername()
                    ));
         }catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching user feedback.", e);
        }
    }

}
