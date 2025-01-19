package com.doranco.project.controllers;

import com.doranco.project.entities.User;
import com.doranco.project.security.JwtService;
import com.doranco.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return userService.register(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return userService.login(email, password);
    }
}
