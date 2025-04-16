package com.doranco.project.controllers;

import com.doranco.project.dto.FeedbackDTO;
import com.doranco.project.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/save")
    public ResponseEntity<FeedbackDTO> saveFeedback(   @RequestParam("feedback")  String feedbackJson, Authentication authentication) {
        try {
            if (feedbackJson == null || feedbackJson.trim().isEmpty()) {
                throw new IllegalArgumentException("Feedback JSON body is required.");
            }

            FeedbackDTO savedFeedback = feedbackService.saveFeedbackForUser(feedbackJson, authentication);
            return ResponseEntity.ok(savedFeedback);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacks() {
        List<FeedbackDTO> listOfAllFeedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(listOfAllFeedbacks);
    }




    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable String id, Authentication authentication) {
        return feedbackService.getFeedbackById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FeedbackDTO> getFeedbackByUserId(Authentication authentication) {

        FeedbackDTO feedback = feedbackService.getFeedbackByUserId(authentication);

            return ResponseEntity.ok(feedback);

    }


}
