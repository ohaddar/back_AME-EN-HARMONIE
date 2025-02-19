package com.doranco.project.controllers;

import com.doranco.project.entities.User;
import com.doranco.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return userService.login(request);
    }

    @GetMapping("/isBlocked/{email}")
    public ResponseEntity<Boolean> isBlocked(@PathVariable String email) {
        return ResponseEntity.ok(userService.isBlocked(email));
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<User> profile(@PathVariable String email) {
        return userService.getProfile(email);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, String>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
