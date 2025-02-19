package com.doranco.project.controllers;

import com.doranco.project.services.UserService;
import com.doranco.project.servicesImp.LoginAttemptServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private LoginAttemptServiceImp loginAttemptService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void registerUser_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("firstname", "John");
        request.put("lastname", "Doe");
        request.put("email", "john.doe@example.com");
        request.put("avatar", "avatar.png");
        request.put("password", "password123");

        when(userService.register(request)).thenAnswer(invocation -> ResponseEntity.ok(request));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void login_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "john.doe@example.com");
        request.put("password", "password123");

        when(loginAttemptService.isBlocked("john.doe@example.com")).thenReturn(false);
        when(userService.login(request)).thenAnswer(invocation -> ResponseEntity.ok("login_success"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void login_Failure_UserNotFound() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "non.existent@example.com");
        request.put("password", "password123");

        when(userService.login(request)).thenAnswer(invocation -> ResponseEntity.status(404).body("User not found"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
