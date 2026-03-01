package com.schools.logesco.admin.service;

import com.schools.logesco.user.entity.Role;
import com.schools.logesco.user.entity.User;
import com.schools.logesco.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.default-admin-email}")
    private String defaultEmail;

    @Bean
    public CommandLineRunner createDefaultAdmin() {
        return args -> {
            if (userRepository.findByUsername(defaultEmail).isEmpty()) {

                User admin = new User();
                admin.setUsername(defaultEmail);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMINISTRATEUR);
                admin.setNom("Administrateur");

                userRepository.save(admin);

                System.out.println("✔ Administrateur par défaut créé : " + defaultEmail);
            } else {
                System.out.println("✔ Administrateur déjà existant, aucune création.");
            }
        };
    }
}
