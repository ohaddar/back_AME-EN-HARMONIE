package com.doranco.project.controllers;

import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.User;
import com.doranco.project.services.FeedbackService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
    @PostMapping("/save")
    public ResponseEntity<User> saveFeedback (@RequestBody User user , @RequestBody Feedback feedback) {

        User savedUserFeedback = feedbackService.addUserWithFeedback(user, feedback);
        return ResponseEntity.ok(savedUserFeedback);

    }
    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getFeedbacks()  {
        List<Feedback> listOfAllFeedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(listOfAllFeedbacks);

    }
    @GetMapping("/sorted/{direction}")
    public ResponseEntity<List<Feedback>> getFeedbacksByRating(@PathVariable String Direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(Direction);

        List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(sortDirection);
        return ResponseEntity.ok(feedbacks);
    }
}