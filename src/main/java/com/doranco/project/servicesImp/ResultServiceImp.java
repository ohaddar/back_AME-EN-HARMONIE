package com.doranco.project.servicesImp;

import com.doranco.project.entities.Result;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IResultRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ResultServiceImp implements ResultService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IResultRepository resultRepository;
    @Override
    public Result saveUserTestResult(Result userResult, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Result result = new Result();
        result.setDescription(userResult.getDescription());
        result.setDatetime(userResult.getDatetime());
        result.setUser(user);
        result.setQuestionnaireId(userResult.getQuestionnaireId());
        return resultRepository.save(result);

    }

    @Override
    public List<Result> getResultsByUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        try {
            User authenticatedUser = (User) authentication.getPrincipal();

            return resultRepository.getResultsByUserId(authenticatedUser.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving results",e);
        }
    }
}
