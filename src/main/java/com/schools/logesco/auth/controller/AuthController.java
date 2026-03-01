package com.schools.logesco.auth.controller;

import com.schools.logesco.auth.dto.*;
import com.schools.logesco.auth.entity.RefreshToken;
import com.schools.logesco.auth.service.JwtService;
import com.schools.logesco.auth.service.PasswordResetService;
import com.schools.logesco.auth.service.RefreshTokenService;
import com.schools.logesco.common.ApiDoc;
import com.schools.logesco.user.entity.User;
import com.schools.logesco.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordResetService passwordResetService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository; //à mettre dans un service

    @Operation(
            summary = "Connexion d'un utilisateur",
            description = "Accessible à tous les utilisateurs"
    )
    @ApiDoc
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Operation(
            summary = "Rafraîchir son token en cas d'expiration",
            description = "Accessible à tous les utilisateurs"
    )
    @ApiDoc
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {

        RefreshToken rt = refreshTokenService.validateRefreshToken(request.refreshToken());
        User user = rt.getUser();
        System.out.println("User récupérer: "+ user.getUsername());

        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponse(newAccessToken, newRefreshToken.getToken());
    }

    @Operation(
            summary = "Demander un nouveau mot de passe",
            description = "Accessible à tous les utilisateurs"
    )
    @ApiDoc
    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.username());
    }

    @Operation(
            summary = "Réinitialiser son mot de passe",
            description = "Accessible à tous les utilisateurs"
    )
    @ApiDoc
    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshRequest request) {
        refreshTokenService.revokeToken(request.refreshToken());
    }
}
