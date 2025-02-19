package com.doranco.project.services;

import com.doranco.project.entities.Questionnaire;
import org.springframework.stereotype.Service;

@Service

public interface QuestionnaireService {
    Questionnaire saveQuestionnaire(Questionnaire questionnaire);
    Questionnaire getQuestionnaire();
}
