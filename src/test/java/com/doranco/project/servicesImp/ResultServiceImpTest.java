package com.doranco.project.servicesImp;

import com.doranco.project.dto.ResultDto;
import com.doranco.project.dto.UserDTO;
import com.doranco.project.entities.Result;
import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
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

    private ResultDto result;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("1L");
        user.setFirstname("John");
        user.setLastname("DeFee");
        user.setAvatar("avatar.png");
        user.setRole(RoleEnum.USER);
        user.setEmail("email@hell.com");
        result = new ResultDto(
                "1L",
                "Test result",
                "2025-01-20",
                new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getAvatar(), user.getRole(), user.getUsername()),
                "Q1"
        );

    }

    @Test
    void testSaveUserTestResult() {

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        ResultDto resultDto = new ResultDto(
                null,
                "Test result",
                "2024-02-15T12:00:00",
                new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getAvatar(), user.getRole(),user.getUsername()),
                "12345"
        );
        System.out.println("Result before saving: " + result);
        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> {
            Result savedResult = invocation.getArgument(0);
            savedResult.setId("1L");
            return savedResult;
        });
        ResultDto savedResult = resultServiceImp.saveUserTestResult(resultDto, authentication);



        System.out.println("Saved result: " + savedResult);

        assertNotNull(savedResult);
        Assertions.assertEquals("1L", savedResult.getId());
        Assertions.assertEquals("Test result", savedResult.getDescription());
        verify(resultRepository, times(1)).save(any(Result.class));
    }



    @Test
    void testGetResultsByUserId() {

        when(authentication.getPrincipal()).thenReturn(user);
        when(resultRepository.getResultsByUserId(user.getId())).thenReturn(Collections.singletonList(new Result(
                "1L", "Test result", "2025-01-20",  "Q1", user
        )));
        var results = resultServiceImp.getResultsByUserId(authentication);

        assertNotNull(results);
        Assertions.assertFalse(results.isEmpty());
        Assertions.assertEquals("1L", results.getFirst().getId());
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
