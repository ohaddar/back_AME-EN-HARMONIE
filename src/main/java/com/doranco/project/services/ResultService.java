package com.doranco.project.services;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.entities.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ResultService {
    Result saveUserTestResult(Result userResult);
    List<Result> getResultsByUserId(Long userId);
}
