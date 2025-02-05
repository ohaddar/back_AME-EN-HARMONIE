package com.doranco.project.controllers;

import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void registerUser_Success() throws Exception {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);

        when(userService.register(user)).thenAnswer(invocation -> ResponseEntity.ok(user));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void login_Success() throws Exception {
        String email = "john.doe@example.com";
        String password = "password123";
        User user = new User(1L, "John", "Doe", email, "avatar.png", password, RoleEnum.USER);

        when(userService.login(email, password)).thenAnswer(invocation ->  ResponseEntity.ok("login_success"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void login_Failure_UserNotFound() throws Exception {
        String email = "non.existent@example.com";
        String password = "password123";

        when(userService.login(email, password)).thenAnswer(invocation ->  ResponseEntity.status(404).body("User not found"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"non.existent@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isNotFound());
    }
}
