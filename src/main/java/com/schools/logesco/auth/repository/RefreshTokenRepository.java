package com.schools.logesco.auth.repository;

import com.schools.logesco.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(final String token);
    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(final Long id);
}
