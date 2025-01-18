package com.doranco.project.services;

import com.doranco.project.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User register(User user);
    Optional<User> login(String email, String password);
}
