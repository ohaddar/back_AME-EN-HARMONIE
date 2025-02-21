package com.doranco.project.seeder;

import com.doranco.project.entities.Questionnaire;
import com.doranco.project.entities.User;
import com.doranco.project.enums.RoleEnum;
import com.doranco.project.repositories.IQuestionnaireRepository;
import com.doranco.project.repositories.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IQuestionnaireRepository questionnaireRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${AEH_ADMIN_EMAIL}")
    private String email;

    @Value("${AEH_ADMIN_PASSWORD}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail(email).isEmpty()) {
            User admin = new User();
            admin.setRole(RoleEnum.ADMIN);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setFirstname("Admin");
            admin.setLastname("AEH");
            admin.setAvatar("src/assets/images/admin.webp");

            userRepository.save(admin);
            System.out.println("Admin seeded successfully!");
        } else {
            System.out.println("Admin already seeded. Skipping...");
        }

        if (questionnaireRepository.count() == 0) {
            System.out.println("Seeding questionnaires from JSON file...");

            InputStream inputStream = new ClassPathResource("seed-data/questionnaires.json").getInputStream();
            Questionnaire questionnaire = objectMapper.readValue(inputStream, Questionnaire.class);

            questionnaireRepository.save(questionnaire);

            System.out.println("Questionnaires seeded successfully!");
        } else {
            System.out.println("Questionnaires already exist. Skipping seeding.");
        }

    }
}
