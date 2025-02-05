package com.doranco.project.controllers;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.services.QuestionnaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class QuestionnaireControllerTest {

    @Mock
    private QuestionnaireService questionnaireService;

    @InjectMocks
    private QuestionnaireController questionnaireController;

    private Questionnaire questionnaire;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questionnaire = new Questionnaire();
        questionnaire.setId("1");
    }

    @Test
    void testSaveQuestionnaire() {
        when(questionnaireService.saveQuestionnaire(questionnaire)).thenReturn(questionnaire);

        ResponseEntity<Questionnaire> response = questionnaireController.saveQuestionnaire(questionnaire);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
    }

    @Test
    void testGetQuestionnaire() {
        when(questionnaireService.getQuestionnaire()).thenReturn(questionnaire);

        ResponseEntity<Questionnaire> response = questionnaireController.getQuestionnaire();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
    }
}
