package com.doranco.project.servicesImp;

import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceImpTest {

    @InjectMocks
    private UserServiceImp userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LoginAttemptServiceImp loginAttemptService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUser_Success() {
        Map<String, String> request = new HashMap<>();
        request.put("firstname", "John");
        request.put("lastname", "Doe");
        request.put("email", "john.doe@example.com");
        request.put("avatar", "avatar.png");
        request.put("password", "password123");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = userService.register(request);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void registerUser_EmailAlreadyExists() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "john.doe@example.com");

        User existingUser = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingUser));

        ResponseEntity<?> response = userService.register(request);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void login_Success() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "john.doe@example.com");
        request.put("password", "password123");

        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(loginAttemptService.isBlocked("john.doe@example.com")).thenReturn(false);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt_token");

        ResponseEntity<?> response = userService.login(request);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void login_UserNotFound() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "john.doe@example.com");
        request.put("password", "password123");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.login(request);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void login_IncorrectPassword() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "john.doe@example.com");
        request.put("password", "incorrect_password");

        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("incorrect_password", "password123")).thenReturn(false);

        ResponseEntity<?> response = userService.login(request);
        assertEquals(400, response.getStatusCode().value());
    }
}
