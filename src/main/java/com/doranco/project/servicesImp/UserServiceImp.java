package com.doranco.project.servicesImp;

import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.security.JwtService;
import com.doranco.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private LoginAttemptServiceImp loginAttemptService;

    @Override
    public ResponseEntity<?> register(Map<String, String> request) {
        User user = new User("0",
                request.get("firstname"),
                request.get("lastname"),
                request.get("email"),
                request.get("avatar"),
                request.get("password"),
                RoleEnum.USER
        );

        if (user.getPassword() == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        String jwt = jwtService.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("id", registeredUser.getId());
        response.put("firstname", registeredUser.getFirstname());
        response.put("lastname", registeredUser.getLastname());
        response.put("email", registeredUser.getEmail());
        response.put("avatar", registeredUser.getAvatar());
        response.put("role", registeredUser.getRole());
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> login(Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password is missing");
        }
        if (loginAttemptService.isBlocked(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many failed attempts");
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            loginAttemptService.loginFailed(email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            loginAttemptService.loginFailed(email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password");
        }
        loginAttemptService.loginSucceeded(email);

        String jwt = jwtService.generateToken(user);
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("firstname", user.getFirstname());
        response.put("lastname", user.getLastname());
        response.put("email", user.getEmail());
        response.put("avatar", user.getAvatar());
        response.put("role", user.getRole());
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean isBlocked(String email) {
        return loginAttemptService.isBlocked(email);
    }

    @Override
    public ResponseEntity<User> getProfile(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public List<Map<String, String>> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            Map<String, String> map = new HashMap<>();
            map.put("id",(user.getId()));
            map.put("firstname", user.getFirstname());
            map.put("lastname", user.getLastname());
            map.put("email", user.getEmail());
            return map;
        }).collect(Collectors.toList());
    }
}
