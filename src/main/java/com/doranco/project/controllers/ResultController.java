package com.doranco.project.controllers;

import com.doranco.project.entities.Result;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        Result savedResult = resultService.saveUserTestResult(result,authentication);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Result>> getResultsByUserId( Authentication authentication) {

            List<Result> results = resultService.getResultsByUserId(authentication);

            return ResponseEntity.ok(results);

    }
    }

