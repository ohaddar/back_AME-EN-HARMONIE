package com.doranco.project.servicesImp;

import com.doranco.project.entities.Result;
import com.doranco.project.entities.User;
import com.doranco.project.repositories.IResultRepository;
import com.doranco.project.repositories.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ResultServiceImpTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IResultRepository resultRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ResultServiceImp resultServiceImp;

    private Result result;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        result = new Result();
        result.setId(1L);
        result.setDescription("Test result");
        result.setDatetime("2025-01-20");
        result.setUser(user);
        result.setQuestionnaireId("Q1");
    }

    @Test
    void testSaveUserTestResult() {

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        System.out.println("Result before saving: " + result);

        when(resultRepository.save(any(Result.class))).thenReturn(result);

        Result savedResult = resultServiceImp.saveUserTestResult(result, authentication);

        System.out.println("Saved result: " + savedResult);

        assertNotNull(savedResult);
        Assertions.assertEquals(1L, savedResult.getId());
        Assertions.assertEquals("Test result", savedResult.getDescription());
        verify(resultRepository, times(1)).save(any(Result.class));
    }



    @Test
    void testGetResultsByUserId() {

        when(authentication.getPrincipal()).thenReturn(user);
        when(resultRepository.getResultsByUserId(user.getId())).thenReturn(Collections.singletonList(result));

        var results = resultServiceImp.getResultsByUserId(authentication);

        assertNotNull(results);
        Assertions.assertFalse(results.isEmpty());
        Assertions.assertEquals(1L, results.get(0).getId());
        verify(resultRepository, times(1)).getResultsByUserId(user.getId());
    }

    @Test
    void testGetResultsByUserId_UserNotAuthenticated() {

        when(authentication.getPrincipal()).thenReturn(null);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            resultServiceImp.getResultsByUserId(authentication);
        });

        Assertions.assertEquals("User not authenticated", exception.getMessage());
    }

    @Test
    void testSaveUserTestResult_UserNotFound() {

        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            resultServiceImp.saveUserTestResult(result, authentication);
        });

        Assertions.assertEquals("Authenticated user not found", exception.getMessage());
    }
}
