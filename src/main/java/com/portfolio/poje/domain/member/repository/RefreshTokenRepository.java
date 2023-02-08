package com.portfolio.poje.domain.member.repository;

import com.portfolio.poje.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByLoginId(String loginId);

    void deleteAllByLoginId(String loginId);
}
