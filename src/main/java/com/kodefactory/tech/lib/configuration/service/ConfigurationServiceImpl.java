package com.kodefactory.tech.lib.configuration.service;

import com.kodefactory.tech.lib.approval.service.ApprovalService;
import com.kodefactory.tech.lib.configuration.constants.ConfigKeys;
import com.kodefactory.tech.lib.configuration.constants.DataType;
import com.kodefactory.tech.lib.configuration.domain.ConfigEO;
import com.kodefactory.tech.lib.configuration.dto.ConfigDTO;
import com.kodefactory.tech.lib.configuration.event.ConfigCreatedEvent;
import com.kodefactory.tech.lib.configuration.repository.ConfigurationRepository;
import com.kodefactory.tech.lib.core.dto.ResponseMessage;
import com.kodefactory.tech.lib.core.event.EventPublisher;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.exception.RestException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
public class ConfigurationServiceImpl extends BaseServiceImpl implements ConfigurationService {
    private ConfigurationRepository configRepository;
    private EventPublisher eventPublisher;
    private ApprovalService approvalService;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository,
                                    EventPublisher eventPublisher,
                                    ApprovalService approvalService) {
        this.configRepository = configurationRepository;
        this.eventPublisher = eventPublisher;
        this.approvalService = approvalService;
    }


    @Override
    public Optional<ConfigEO> getConfigData(String key) throws RestException {
        validateNotNull(key, "Configuration key is required");
        return configRepository.findByConfigKey(key);
    }


    @Override
    public Optional<ConfigEO> getConfigData(ConfigDTO configDTO) throws RestException {
        validateNotNull(configDTO.getConfigKey(), "Configuration key is required");
        Optional<ConfigEO> configOptional = configRepository.findByConfigKey(configDTO.getConfigKey());
        ConfigEO config = configOptional.orElseGet(ConfigEO::new);

        config.setConfigKey(configDTO.getConfigKey());

        // Update the data with the default value if it does not exist
        if (notNull(configDTO.getDefaultValue()) && isNull(config.getData())) {
            config.setData(configDTO.getDefaultValue().toString());
        }

        // Update the DataType if it is not initially available
        if (notNull(configDTO.getDataType()) && isNull(config.getDataType())) {
            config.setDataType(DataType.valueOf(configDTO.getDataType()));
        }
        config = configRepository.save(config);

        return Optional.of(config);
    }


    @Override
    public <T> T getConfigData(String key, Function<Optional<ConfigEO>, T> postProcessor) throws RestException {
        validateNotNull(key, "Configuration key is required");
        Optional<ConfigEO> configurationEO = configRepository.findByConfigKey(key);
        return Optional.ofNullable(postProcessor.apply(configurationEO)).orElse(null);
    }


    @Override
    public ResponseMessage saveConfig(ConfigDTO configDTO) throws RestException {
        validateNotNull(configDTO.getConfigKey(), "Configuration key is required");
        validateNotNull(configDTO.getData(), "Configuration data is required");
        Optional<ConfigEO> configurationOptional = configRepository.findByConfigKey(configDTO.getConfigKey());
        ConfigEO configurationEO = null;

        Boolean approveConfig = checkConfig(ConfigKeys.APPROVE_CONFIGURATION.value(), (configEO) -> Boolean.valueOf(configEO.getData()));

        if (approveConfig) {
            configurationEO = clone(configurationOptional.orElseGet(ConfigEO::new), ConfigEO.class);
        } else {
            configurationEO = configurationOptional.orElseGet(ConfigEO::new);
        }


        configurationEO.setData(configDTO.getData());
        configurationEO.setConfigKey(configDTO.getConfigKey());
        configurationEO.setDataType(DataType.valueOf(configDTO.getDataType()));
        configurationEO.setLabel(configDTO.getLabel());
        String message = "";

        if (approveConfig) {
            Integer configApprovalLevel = getApprovalLevel(this, ConfigKeys.CONFIGURATION_APPROVAL_LEVEL.value());
            sendForApproval(approvalService, configurationEO, configApprovalLevel, configApprovalLevel, "Approve Configuration");
            message = "Configuration sent for approval";
        } else {
            configurationEO = configRepository.save(configurationEO);
            message = "Configuration saved!";
        }


        eventPublisher.publish(new ConfigCreatedEvent<>(configurationEO));
        return new ResponseMessage(message);
    }


    @Override
    public Boolean checkConfig(String key, Predicate<ConfigEO> predicate) throws RestException {
        Optional<ConfigEO> configOptional = configRepository.findByConfigKey(key);
        if (!configOptional.isPresent()) return false;
        return predicate.test(configOptional.get());
    }

    @Override
    public List<ConfigEO> getConfigByScopeName(String scopeName) {

        return configRepository.findByScopeName(scopeName)
                .stream()
                .peek(configEO -> {
                    switch (configEO.getDataType()) {
                        case BOOLEAN:
                            configEO.setRawData(Boolean.valueOf(configEO.getData()));
                            break;
                        case DOUBLE:
                            configEO.setRawData(Double.valueOf(configEO.getData()));
                            break;
                        case INTEGER:
                            configEO.setRawData(Integer.valueOf(configEO.getData()));
                            break;
                        case STRING:
                            configEO.setRawData(String.valueOf(configEO.getData()));
                            break;
                        case LONG:
                            configEO.setRawData(Long.valueOf(configEO.getData()));
                            break;
                        case DATE:
                            Date d = new Date();
                            d.setTime(Long.valueOf(configEO.getData()));
                            configEO.setRawData(d);
                            break;
                    }
                }).sorted(Comparator.comparing(ConfigEO::getConfigKey))
                .collect(toList());
    }

    @Override
    public List<ConfigEO> getConfigByScopeId(Long id) {
        return configRepository.findByScopeId(id);
    }

    @Override
    public List<ConfigEO> getConfigByScopeNameAndId(String scopeName, Long id) {
        return configRepository.findByScopeNameAndScopeId(scopeName, id);
    }
}
