package com.doranco.project.services;

import com.doranco.project.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User register(User user);
    public User login(String email, String password);
}
