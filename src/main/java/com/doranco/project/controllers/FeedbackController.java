package com.doranco.project.controllers;

import com.doranco.project.entities.Feedback;
import com.doranco.project.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/save")
    public ResponseEntity<Feedback> saveFeedback(   @RequestParam("feedback")  String feedbackJson, Authentication authentication) {
        try {
            if (feedbackJson == null || feedbackJson.trim().isEmpty()) {
                throw new IllegalArgumentException("Feedback JSON body is required.");
            }

            Feedback savedFeedback = feedbackService.saveFeedbackForUser(feedbackJson, authentication);
            return ResponseEntity.ok(savedFeedback);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST) // Return 400 status code for bad requests
                    .body(null);
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
        List<Feedback> limitedFeedbacks = feedbackService.getPublicFeedbacks();
        return ResponseEntity.ok(limitedFeedbacks);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Optional<Feedback>>getFeedbackById(@PathVariable Long id, Authentication authentication) {
        Optional<Feedback> feedbacksById = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(feedbacksById);
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Feedback> getFeedbackByUserId(Authentication authentication) {

            Feedback feedback = feedbackService.getFeedbackByUserId(authentication);

            return ResponseEntity.ok(feedback);

    }





}
