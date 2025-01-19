package com.doranco.project.services;

import com.doranco.project.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    ResponseEntity<?> register(User user);
    ResponseEntity<?> login(String email, String password);
}
