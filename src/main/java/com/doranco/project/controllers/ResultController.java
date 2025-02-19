package com.doranco.project.controllers;

import com.doranco.project.dto.ResultDto;
import com.doranco.project.entities.Result;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/results")
public class ResultController {
    @Autowired
    ResultService resultService;
    @Autowired
    IUserRepository userRepository;
    @PostMapping("/save")
    public ResponseEntity<ResultDto> saveResult(@RequestBody ResultDto result, Authentication authentication) {
        ResultDto savedResult = resultService.saveUserTestResult(result,authentication);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ResultDto>> getResultsByUserId(Authentication authentication) {

            List<ResultDto> results = resultService.getResultsByUserId(authentication);

            return ResponseEntity.ok(results);

    }
    @GetMapping("/all")
    public ResponseEntity<List<ResultDto>> getAllResults() {
        List<ResultDto> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    }

