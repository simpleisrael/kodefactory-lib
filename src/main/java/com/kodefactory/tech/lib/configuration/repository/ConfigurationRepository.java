package com.kodefactory.tech.lib.configuration.repository;

import com.kodefactory.tech.lib.configuration.domain.ConfigEO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<ConfigEO, Long> {
    Optional<ConfigEO> findByConfigKey(String configKey);
    List<ConfigEO> findByScopeName(String scopeName);
    List<ConfigEO> findByScopeId(Long scopeId);
    List<ConfigEO> findByScopeNameAndScopeId(String scopeName, Long scopeId);
}
