package com.doranco.project.services;

import com.doranco.project.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public User register(User user);
    public Optional<User> login(String email, String password);
}
