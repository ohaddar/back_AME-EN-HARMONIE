package com.doranco.project.servicesImp;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.repositories.IQuestionnaireRepository;
import com.doranco.project.services.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireServiceImp implements QuestionnaireService {

    @Autowired
    IQuestionnaireRepository questionnaireRepository;

    @Override
    public Questionnaire saveQuestionnaire(Questionnaire questionnaire) {
        if (questionnaire == null) {
            throw new IllegalArgumentException("Error occurred while saving questionnaire.");
        }

        return questionnaireRepository.save(questionnaire);

    }

    @Override
    public Questionnaire getQuestionnaire() {
        try {
            return questionnaireRepository.findAll().getFirst();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching questionnaire.", e);
        }
    }
}
