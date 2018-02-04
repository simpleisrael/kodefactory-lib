package com.kodefactory.tech.lib.security.repository;

import com.kodefactory.tech.lib.security.domain.UserEO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEO, Long> {
    Optional<UserEO> findByUsername(String username);
    Optional<UserEO> findByEmail(String email);
    Optional<UserEO> findByResetToken(String resetToken);
}
