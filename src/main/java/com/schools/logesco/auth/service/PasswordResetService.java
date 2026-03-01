package com.schools.logesco.auth.service;

import com.schools.logesco.admin.repository.AdminRepository;
import com.schools.logesco.auth.entity.PasswordResetToken;
import com.schools.logesco.auth.repository.PasswordResetTokenRepository;
import com.schools.logesco.mail.EmailTemplateService;
import com.schools.logesco.mail.MailService;
import com.schools.logesco.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final AdminRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailTemplateService emailTemplateService;
    private final MailService mailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public void requestReset(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .username(user.getUsername())
                .expiration(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        tokenRepository.save(token);

        String resetLink = baseUrl+"/reset-password?token=" + token.getToken();

        String html = emailTemplateService.resetPassword(
                user.getUsername(),
                resetLink
        );

        mailService.send(user.getUsername(), "Réinitialisation de votre mot de passe", html);
    }

    public void resetPassword(String tokenValue, String newPassword) {

        PasswordResetToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Token invalide"));

        if (token.isUsed() || token.getExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expiré ou déjà utilisé");
        }

        User user = userRepository.findByUsername(token.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);

    }
}

