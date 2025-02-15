package com.doranco.project.servicesImp;

import com.doranco.project.dto.ResultDto;
import com.doranco.project.dto.UserDTO;
import com.doranco.project.entities.Result;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IResultRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ResultServiceImp implements ResultService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IResultRepository resultRepository;
    @Override
    public ResultDto saveUserTestResult(ResultDto userResult, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Result result = new Result();
        result.setDescription(userResult.getDescription());
        result.setDatetime(userResult.getDatetime());
        result.setUser(user);
        result.setQuestionnaireId(userResult.getQuestionnaireId());
        Result savedResult = resultRepository.save(result);
        return new ResultDto(
                savedResult.getId(),
                savedResult.getDescription(),
                savedResult.getDatetime(),
                new UserDTO(
                        savedResult.getUser().getId(),
                        savedResult.getUser().getFirstname(),
                        savedResult.getUser().getLastname(),
                        savedResult.getUser().getAvatar(),
                        savedResult.getUser().getRole(),
                        savedResult.getUser().getUsername()
                ),
                savedResult.getQuestionnaireId()
        );
    }

    @Override
    public List<ResultDto> getResultsByUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        try {
            User authenticatedUser = (User) authentication.getPrincipal();

            return resultRepository.getResultsByUserId(authenticatedUser.getId()).stream()
                    .map(result -> new ResultDto(
                            result.getId(),
                            result.getDescription(),
                            result.getDatetime(),
                            new UserDTO(
                                    result.getUser().getId(),
                                    result.getUser().getFirstname(),
                                    result.getUser().getLastname(),
                                    result.getUser().getAvatar(),
                                    result.getUser().getRole(),
                                    result.getUser().getUsername()
                            ),
                            result.getQuestionnaireId()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving results",e);
        }
    }

    @Override
    public List<ResultDto> getAllResults() {
        return resultRepository.findAll().stream()
                .map(result -> new ResultDto(
                        result.getId(),
                        result.getDescription(),
                        result.getDatetime(),
                        new UserDTO(
                                result.getUser().getId(),
                                result.getUser().getFirstname(),
                                result.getUser().getLastname(),
                                result.getUser().getAvatar(),
                                result.getUser().getRole(),
                                result.getUser().getUsername()
                        ),
                        result.getQuestionnaireId()
                ))
                .collect(Collectors.toList());
    }
}
