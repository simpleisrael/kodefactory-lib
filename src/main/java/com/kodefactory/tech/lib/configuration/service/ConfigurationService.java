package com.kodefactory.tech.lib.configuration.service;

import com.kodefactory.tech.lib.configuration.domain.ConfigEO;
import com.kodefactory.tech.lib.configuration.dto.ConfigDTO;
import com.kodefactory.tech.lib.core.dto.ResponseMessage;
import com.kodefactory.tech.lib.exception.RestException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ConfigurationService {
    <T> Optional<ConfigEO> getConfigData(String key) throws RestException;

    <T> Optional<ConfigEO> getConfigData(ConfigDTO configDTO) throws RestException;

    <T> T getConfigData(String key, Function<Optional<ConfigEO>, T> postProcessor) throws RestException;

    ResponseMessage saveConfig(ConfigDTO configDTO) throws RestException;

    Boolean checkConfig(String key, Predicate<ConfigEO> predicate) throws RestException;

    List<ConfigEO> getConfigByScopeName(String scopeName);
    List<ConfigEO> getConfigByScopeId(Long id);
    List<ConfigEO> getConfigByScopeNameAndId(String scopeName, Long id);
}
