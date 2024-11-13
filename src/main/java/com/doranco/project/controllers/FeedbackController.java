package com.doranco.project.controllers;

import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.services.FeedbackService;
import com.doranco.project.utils.FileUpload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Autowired
    FileUpload fileUpload;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/save")
    public ResponseEntity<Feedback> saveFeedback(
            @RequestParam("image") MultipartFile file,
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

            // Process and save image if provided
            if (file != null && !file.isEmpty()) {
                byte[] imageData = fileUpload.uploadFile(file);
                feedback.setImage(imageData);
            }

            // Save the feedback
            Feedback savedFeedback = feedbackService.addFeedback(feedback);
            return ResponseEntity.ok(savedFeedback);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Feedback>> getFeedbacks() {
        List<Feedback> listOfAllFeedbacks = feedbackService.getAllFeedbacks();
        for (Feedback feedback : listOfAllFeedbacks) {
            feedback.setImageUrl("http://localhost:8080/feedback/image/" + feedback.getId());
        }
        return ResponseEntity.ok(listOfAllFeedbacks);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Feedback>> getPublicFeedbacks() {
        List<Feedback> listOfAllFeedbacks = feedbackService.getAllFeedbacks();

        // Take only the first two feedbacks
        List<Feedback> limitedFeedbacks = listOfAllFeedbacks.stream()
                .limit(2)
                .collect(Collectors.toList());

        for (Feedback feedback : limitedFeedbacks) {
            feedback.setImageUrl("http://localhost:8080/feedback/image/" + feedback.getId());
        }

        return ResponseEntity.ok(limitedFeedbacks);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id, Authentication authentication) {
        Optional<Feedback> feedbacksById = feedbackService.getFeedbackById(id);

        if (feedbacksById.isPresent()) {
            Feedback feedback = feedbacksById.get();
            feedback.setImageUrl("http://localhost:8080/feedback/image/" + feedback.getId());  // Dynamically set imageUrl
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/sorted/{direction}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Feedback>> getFeedbacksByRating(@PathVariable String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(sortDirection);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);

        if (feedback.isPresent() && feedback.get().getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Assuming JPEG format
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image.jpg\"")
                    .body(feedback.get().getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
