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
import java.util.Map;
import java.util.Optional;

@Service

public class UserServiceImp implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
 @Autowired
 IUserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Override
    public ResponseEntity<?> register(User user) {
        if (user.getPassword() == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if (user.getRole() == null) {
            user.setRole(RoleEnum.USER);
        }

        User registeredUser = userRepository.save(user);
        return ResponseEntity.ok(registeredUser);

    }



    @Override
    public ResponseEntity<?> login(String email, String password) {
        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password is missing");
        }
        Optional<User> loggedInUser = userRepository.findByEmail(email);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = loggedInUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password");
        }
        String jwt = jwtService.generateToken(user);
        Map<String ,Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("firstname", user.getFirstname());
        response.put("lastname",user.getLastname());
        response.put("email", user.getEmail());
        response.put("avatar", user.getAvatar());
        response.put("role", user.getRole());
        response.put("token", jwt);
        return ResponseEntity.ok(response);
    }
}
