package com.doranco.project.services;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.entities.Result;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ResultService {
    Result saveUserTestResult(Result userResult, Authentication authentication);
    List<Result> getResultsByUserId(Authentication authentication);
}
