package com.schools.logesco.auth.config;

import com.schools.logesco.auth.service.JwtService;
import com.schools.logesco.user.entity.Role;
import com.schools.logesco.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @Value("${app.base-url}")
    private String frontUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Auth public
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login", "/auth/forgot-password", "/auth/refresh", "/auth/reset-password").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // --- Élèves ---
                        .requestMatchers(HttpMethod.POST, "/eleves").hasAnyRole(Role.SECRETAIRE.name(), Role.ADMINISTRATEUR.name())
                        .requestMatchers(HttpMethod.PUT, "/eleves/*").hasAnyRole(Role.SECRETAIRE.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())
                        .requestMatchers(HttpMethod.PUT, "/eleves/*/archive").hasAnyRole(Role.SECRETAIRE.name(), Role.ADMINISTRATEUR.name())
                        .requestMatchers(HttpMethod.GET, "/eleves/archives").hasAnyRole(Role.SECRETAIRE.name(), Role.ECONOME.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())
                        .requestMatchers(HttpMethod.POST, "/eleves/search").hasAnyRole(Role.SECRETAIRE.name(), Role.ECONOME.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())

                        // --- Utilisateurs ---
                        .requestMatchers(HttpMethod.GET, "/profil").hasAnyRole(Role.SECRETAIRE.name(), Role.ECONOME.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())

                        // --- Paiements ---
                        .requestMatchers(HttpMethod.PUT, "/paiements/*").hasAnyRole(Role.ECONOME.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())
                        .requestMatchers(HttpMethod.PUT, "/paiements/*/statut").hasAnyRole(Role.ECONOME.name(), Role.ADMINISTRATEUR.name())

                        // --- Listes ---
                        .requestMatchers(HttpMethod.GET, "/eleves/liste-appel").hasAnyRole(Role.SECRETAIRE.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())
                        .requestMatchers(HttpMethod.POST, "/eleves/liste").hasAnyRole(Role.SECRETAIRE.name(), Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())

                        // --- Gestion utilisateurs ---
                        .requestMatchers("/users/**").hasRole(Role.ADMINISTRATEUR.name())

                        // --- Logs ---
                        .requestMatchers("/logs/**").hasAnyRole(Role.ADMINISTRATEUR.name(), Role.DIRECTEUR.name())

                        // Toute autre requête doit être authentifiée
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService, userDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
