package com.doranco.project.services;

import com.doranco.project.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    ResponseEntity<?> register(Map<String, String> request);
    ResponseEntity<?> login(Map<String, String> request);
    boolean isBlocked(String email);
    ResponseEntity<User> getProfile(String email);
    List<Map<String, String>> getAllUsers();
}
