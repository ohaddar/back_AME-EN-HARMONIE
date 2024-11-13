package com.doranco.project.controllers;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.services.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }
    @PostMapping("/save")
    public ResponseEntity<Questionnaire>   saveQuestionnaire(@RequestBody Questionnaire questionnaire) {
        Questionnaire addedQuestionnaire = questionnaireService.saveQuestionnaire(questionnaire);
        if (questionnaire == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(addedQuestionnaire);

    }
    @GetMapping("/show")

    public ResponseEntity<Questionnaire> getQuestionnaire() {
        Questionnaire listOfQuestions = questionnaireService.getQuestionnaire();
        return ResponseEntity.ok(listOfQuestions);
    }

}