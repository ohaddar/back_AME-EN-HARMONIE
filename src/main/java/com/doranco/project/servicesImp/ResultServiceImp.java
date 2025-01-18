package com.doranco.project.servicesImp;

import com.doranco.project.entities.Result;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IResultRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ResultServiceImp implements ResultService {
    @Autowired
    IUserRepository userRepository;
@Autowired
    IResultRepository resultRepository;
    @Override
    public Result saveUserTestResult(Result userResult) {
        User user = userRepository.findById(userResult.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Result result = new Result();
        result.setDescription(userResult.getDescription());
        result.setDatetime(userResult.getDatetime());
        result.setUser(user);
        result.setQuestionnaireId(userResult.getQuestionnaireId());
        return resultRepository.save(result);

    }

    @Override
    public List<Result> getResultsByUserId(Long userId) {
        return resultRepository.getResultsByUserId(userId);
    }
}
