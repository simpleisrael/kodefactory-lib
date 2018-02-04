package com.kodefactory.tech.lib.security.repository;

import com.kodefactory.tech.lib.security.domain.RoleEO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEO, Long> {
    Optional<RoleEO> findByName(String name);
}
