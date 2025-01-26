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

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUser_Success() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(userRepository.findByEmail("john.doe@example.com")).thenAnswer(invocation -> Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.register(user);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void registerUser_EmailAlreadyExists() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.register(user);
        assertEquals(409, response.getStatusCode().value());  // Conflict
    }

    @Test
    public void login_Success() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt_token");

        ResponseEntity<?> response = userService.login("john.doe@example.com", "password123");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void login_UserNotFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.login("john.doe@example.com", "password123");
        assertEquals(404, response.getStatusCode().value()); // Not Found
    }

    @Test
    public void login_IncorrectPassword() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "avatar.png", "password123", RoleEnum.USER);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("incorrect_password", "password123")).thenReturn(false);

        ResponseEntity<?> response = userService.login("john.doe@example.com", "incorrect_password");
        assertEquals(400, response.getStatusCode().value()); // Bad Request
    }
}
