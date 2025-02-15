package com.doranco.project.services;

import com.doranco.project.dto.ResultDto;
import com.doranco.project.entities.Result;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ResultService {
    ResultDto saveUserTestResult(ResultDto userResult, Authentication authentication);
    List<ResultDto> getResultsByUserId(Authentication authentication);
    List<ResultDto> getAllResults();
}
