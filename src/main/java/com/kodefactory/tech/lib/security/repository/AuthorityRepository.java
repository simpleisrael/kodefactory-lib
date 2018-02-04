package com.kodefactory.tech.lib.security.repository;

import com.kodefactory.tech.lib.security.domain.AuthorityEO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthorityEO, Long> {
    Optional<AuthorityEO> findByName(String name);
}
