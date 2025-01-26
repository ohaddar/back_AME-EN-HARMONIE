package com.doranco.project.servicesImp;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.repositories.IQuestionnaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class QuestionnaireServiceImpTest {

    @Mock
    private IQuestionnaireRepository questionnaireRepository;

    @InjectMocks
    private QuestionnaireServiceImp questionnaireServiceImp;

    private Questionnaire questionnaire;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questionnaire = new Questionnaire();
        questionnaire.setId("1");
        // Mock other properties like questions or results if needed
    }

    @Test
    void testSaveQuestionnaire() {
        // Given
        when(questionnaireRepository.save(questionnaire)).thenReturn(questionnaire);

        // When
        Questionnaire savedQuestionnaire = questionnaireServiceImp.saveQuestionnaire(questionnaire);

        // Then
        assertNotNull(savedQuestionnaire);
        assertEquals("1", savedQuestionnaire.getId());
        verify(questionnaireRepository, times(1)).save(questionnaire);
    }

    @Test
    void testGetQuestionnaire() {
        // Given
        when(questionnaireRepository.findAll()).thenReturn(List.of(questionnaire));

        // When
        Questionnaire retrievedQuestionnaire = questionnaireServiceImp.getQuestionnaire();

        // Then
        assertNotNull(retrievedQuestionnaire);
        assertEquals("1", retrievedQuestionnaire.getId());
        verify(questionnaireRepository, times(1)).findAll();
    }

    @Test
    void testGetQuestionnaire_Exception() {
        // Given
        when(questionnaireRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            questionnaireServiceImp.getQuestionnaire();
        });
        assertEquals("Error occurred while fetching public feedbacks.", exception.getMessage());
    }
}
