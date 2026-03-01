package com.schools.logesco.auth.service;

import com.schools.logesco.auth.entity.RefreshToken;
import com.schools.logesco.auth.repository.RefreshTokenRepository;
import com.schools.logesco.user.entity.User;
import com.schools.logesco.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // Durée de validité du refresh token (ex: 7 jours)
    private final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);

    /**
     * Génère un refresh token pour un utilisateur.
     * Si un token existe déjà, il est remplacé (rotation).
     */
    @Transactional
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Supprimer l'ancien token (rotation)
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.flush();

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiration(LocalDateTime.now().plus(REFRESH_TOKEN_DURATION))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(token);
    }

    /**
     * Valide un refresh token reçu.
     */
    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token invalide"));

        System.out.println("Refresh token retrouvé: "+ refreshToken.getToken());

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token révoqué");
        }

        if (refreshToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expiré");
        }

        System.out.println("Refresh token renvoyé: "+ refreshToken.getToken());
        return refreshToken;
    }

    /**
     * Révoque un refresh token (logout).
     */
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    /**
     * Révoque tous les tokens d’un utilisateur.
     */
    public void revokeAllForUser(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            refreshTokenRepository.deleteByUserId(user.getId());
        });
    }
}