package com.doranco.project.controllers;

import com.doranco.project.entities.Result;
import com.doranco.project.services.ResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ResultControllerTest {

    @Mock
    private ResultService resultService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ResultController resultController;

    private Result result;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        result = new Result();
        result.setId(1L);
        result.setDescription("Test result");
        result.setDatetime("2025-01-20");
        // Add other necessary fields if needed
    }

    @Test
    void testSaveResult() {
        // Given
        when(resultService.saveUserTestResult(result, authentication)).thenReturn(result);

        // When
        ResponseEntity<Result> response = resultController.saveResult(result, authentication);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetResultsByUserId() {
        // Given
        when(resultService.getResultsByUserId(authentication)).thenReturn(Collections.singletonList(result));

        // When
        ResponseEntity<List<Result>> response = resultController.getResultsByUserId(authentication);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1L, response.getBody().get(0).getId());
    }
}
