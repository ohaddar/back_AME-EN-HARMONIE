package com.doranco.project.controllers;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.services.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {
   @Autowired
    QuestionnaireService questionnaireService;


    @PostMapping("/save")
    public ResponseEntity<Questionnaire>   saveQuestionnaire(@RequestBody Questionnaire questionnaire) {
        Questionnaire addedQuestionnaire = questionnaireService.saveQuestionnaire(questionnaire);
        return ResponseEntity.ok(addedQuestionnaire);

    }
    @GetMapping("/show")

    public ResponseEntity<Questionnaire> getQuestionnaire() {
        Questionnaire listOfQuestions = questionnaireService.getQuestionnaire();
        return ResponseEntity.ok(listOfQuestions);
    }

}