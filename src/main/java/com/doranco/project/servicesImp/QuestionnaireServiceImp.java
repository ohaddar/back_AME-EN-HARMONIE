package com.doranco.project.servicesImp;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.repositories.IQuestionnaireRepository;
import com.doranco.project.services.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class QuestionnaireServiceImp implements QuestionnaireService {

    @Autowired
    IQuestionnaireRepository questionnaireRepository;
    @Override
    public Questionnaire saveQuestionnaire(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);


    }
    @Override
    public List<Questionnaire> getAllQuestionnaire () {
        return questionnaireRepository.findAll();
    }
}
