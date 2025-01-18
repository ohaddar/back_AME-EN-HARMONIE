package com.doranco.project.controllers;

import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.services.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/save")
    public ResponseEntity<Feedback> saveFeedback(
            @RequestParam("feedback") String feedbackJson,
            Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            System.out.println("Feedback JSON: " + feedbackJson);
            Feedback feedback = new ObjectMapper().readValue(feedbackJson, Feedback.class);

            // Retrieve the current authenticated user
            User authenticatedUser = (User) authentication.getPrincipal();

            // Set the user on the feedback
            feedback.setUser(authenticatedUser);

            Feedback savedFeedback = feedbackService.addFeedback(feedback);
            System.out.println("Feedback created: " + savedFeedback);

            return ResponseEntity.ok(savedFeedback);

        } catch (Exception e) {

            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Feedback>> getFeedbacks() {
        List<Feedback> listOfAllFeedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(listOfAllFeedbacks);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Feedback>> getPublicFeedbacks() {
        List<Feedback> listOfAllFeedbacks = feedbackService.getAllFeedbacks();

        // Take only the first two feedbacks
        List<Feedback> limitedFeedbacks = listOfAllFeedbacks.stream()
                .limit(2)
                .collect(Collectors.toList());


        return ResponseEntity.ok(limitedFeedbacks);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id, Authentication authentication) {
        Optional<Feedback> feedbacksById = feedbackService.getFeedbackById(id);

        if (feedbacksById.isPresent()) {
            Feedback feedback = feedbacksById.get();
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Feedback> getFeedbackByUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the current authenticated user
            User authenticatedUser = (User) authentication.getPrincipal();

            // Call the service method to get the feedback by user ID
            Optional<Feedback> feedback = feedbackService.getFeedbackByUserId(authenticatedUser.getId());

            if (feedback.isPresent()) {
                return ResponseEntity.ok(feedback.get());
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
