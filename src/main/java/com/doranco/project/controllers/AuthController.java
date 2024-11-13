package com.doranco.project.controllers;

import com.doranco.project.entities.User;
import com.doranco.project.security.JwtService;
import com.doranco.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
     User registredUser =   userService.register(user);
     if(registredUser !=null) {
         Map<String,Object> response = new HashMap<>();
         response.put("id", registredUser.getId());
         response.put("firstname", registredUser.getFirstname());
         response.put("lastname", registredUser.getLastname());
         response.put("email", registredUser.getEmail());
         response.put("role", registredUser.getRole());

         return ResponseEntity.ok(response);
     }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid parameter");

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Optional<User> loggedInUser = userService.login(email, password);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!passwordEncoder.matches(password, loggedInUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password");
        }

            Map<String ,Object> response = new HashMap<>();
            response.put("id", loggedInUser.get().getId());
            response.put("firstname", loggedInUser.get().getFirstname());
            response.put("lastname", loggedInUser.get().getLastname());
            response.put("email", loggedInUser.get().getEmail());
            response.put("role", loggedInUser.get().getRole());
            String jwt =  jwtService.generateToken(loggedInUser.get());
        response.put("token", jwt);
        return ResponseEntity.ok(response);

    }
}
