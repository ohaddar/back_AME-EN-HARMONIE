package com.doranco.project.controllers;

import com.doranco.project.entities.User;
import com.doranco.project.services.UserService;
import com.doranco.project.servicesImp.LoginAttemptServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginAttemptServiceImp loginAttemptService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return userService.register(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        if (loginAttemptService.isBlocked(email)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Too many failed attempts. Try again later.");
        }

        ResponseEntity<?> response = userService.login(email, password);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST || response.getStatusCode() == HttpStatus.NOT_FOUND) {
            loginAttemptService.loginFailed(email);
        } else {
            loginAttemptService.loginSucceeded(email);
        }
        return response;

    }
    @GetMapping("/isBlocked/{email}")
    public ResponseEntity<Boolean> isBlocked(@PathVariable String email) {
        return ResponseEntity.ok(loginAttemptService.isBlocked(email));
    }
}
