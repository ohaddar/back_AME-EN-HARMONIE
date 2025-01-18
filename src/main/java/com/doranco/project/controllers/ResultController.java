package com.doranco.project.controllers;

import com.doranco.project.entities.Feedback;
import com.doranco.project.entities.Result;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/results")
@CrossOrigin(origins = "http://localhost:5173")
public class ResultController {
    @Autowired
    ResultService resultService;
    @Autowired
    IUserRepository userRepository;
    @PostMapping("/save")
    public ResponseEntity<Result> saveResult(@RequestBody Result result, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Set the user (from authentication) for the result (userId is passed)
        result.setUser(user); // The user object is set in the result

        // Save the result
        Result savedResult = resultService.saveUserTestResult(result);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Result>> getResultsByUserId( Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the current authenticated user
            User authenticatedUser = (User) authentication.getPrincipal();

            // Call the service method to get the feedback by user ID
            List<Result> results = resultService.getResultsByUserId(authenticatedUser.getId());

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    }

