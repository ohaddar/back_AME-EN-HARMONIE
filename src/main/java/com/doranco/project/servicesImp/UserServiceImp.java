package com.doranco.project.servicesImp;

import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserServiceImp implements UserService {
    @Autowired
     IUserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public User register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if (user.getRole() == null) {
            user.setRole(RoleEnum.USER);
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String email, String password) {
     return userRepository.findByEmail(email);

    }
}
