package com.doranco.project.servicesImp;

import com.doranco.project.entities.Question;
import com.doranco.project.entities.Questionnaire;
import com.doranco.project.repositories.IQuestionnaireRepository;
import com.doranco.project.services.QuestionnaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
@ActiveProfiles("test")
public class QuestionnaireServiceIntegrationTest {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private IQuestionnaireRepository questionnaireRepository;

    @BeforeEach
    public void setup() {
        questionnaireRepository.deleteAll();
    }

    @Test
    public void testSaveQuestionnaire_valid() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDefaultMessage("Welcome to the survey!");
        questionnaire.setResults(Map.of("q1", "Yes", "q2", "No"));

        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setText("What is your favorite color?");
        question.setResponses(Arrays.asList("Red", "Blue", "Green"));
        question.setNext(Map.of("Red", "q2", "Blue", "q3"));
        questions.add(question);
        questionnaire.setQuestions(questions);

        Questionnaire saved = questionnaireService.saveQuestionnaire(questionnaire);

        assertNotNull(saved, "The saved questionnaire should not be null");
        assertNotNull(saved.getId(), "The questionnaire ID should be generated");
        assertEquals("Welcome to the survey!", saved.getDefaultMessage());
        assertEquals(2, saved.getResults().size());
        assertEquals(1, saved.getQuestions().size());
    }

    @Test
    public void testSaveQuestionnaire_null() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                questionnaireService.saveQuestionnaire(null)
        );
        assertEquals("Error occurred while saving questionnaire.", exception.getMessage());
    }

    @Test
    public void testGetQuestionnaire_success() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDefaultMessage("Hello, world!");
        questionnaire.setResults(Map.of("key", "value"));

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(null, "What is your name?", Arrays.asList("Alice", "Bob"), Map.of()));
        questionnaire.setQuestions(questions);

        Questionnaire saved = questionnaireService.saveQuestionnaire(questionnaire);

        Questionnaire retrieved = questionnaireService.getQuestionnaire();

        assertNotNull(retrieved, "Retrieved questionnaire should not be null");
        assertEquals(saved.getId(), retrieved.getId(), "The IDs should match");
        assertEquals("Hello, world!", retrieved.getDefaultMessage());
    }

    @Test
    public void testGetQuestionnaire_empty() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                questionnaireService.getQuestionnaire()
        );
        assertTrue(exception.getMessage().contains("Error occurred while fetching questionnaire."));
    }
}
