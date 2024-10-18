package com.doranco.project.servicesImp;

import com.doranco.project.entities.User;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImp implements UserService {
    @Autowired
     IUserRepository userRepository;


    @Override
    public User register(User user) {
        // Chiffrer le mot de passe avant d'enregistrer l'utilisateur

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return user; // Authentification réussie
        }
        return null; // Authentification échouée
    }
}
