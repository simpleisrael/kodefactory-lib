package com.kodefactory.tech.lib.configuration.rest;

import com.kodefactory.tech.lib.configuration.domain.ConfigEO;
import com.kodefactory.tech.lib.configuration.dto.ConfigByScopeRequestDTO;
import com.kodefactory.tech.lib.configuration.dto.ConfigDTO;
import com.kodefactory.tech.lib.configuration.service.ConfigurationService;
import com.kodefactory.tech.lib.core.rest.BaseREST;
import com.kodefactory.tech.lib.exception.RestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("config-service")
public class ConfigurationREST extends BaseREST {

    private ConfigurationService configurationService;

    public ConfigurationREST(ConfigurationService configurationService){
        this.configurationService = configurationService;
    }



    @PostMapping("save-config")
    @PreAuthorize("hasAnyRole('CONFIG_WRITE', 'CONFIG_READ_WRITE')")
    public ResponseEntity<Object> saveConfig(@RequestBody ConfigDTO configDTO) throws RestException {
        return buildSuccessResponse(configurationService.saveConfig(configDTO));
    }



    @PostMapping("get-config")
    @PreAuthorize("hasAnyRole('CONFIG_READ', 'CONFIG_READ_WRITE')")
    public ResponseEntity<Object> getConfig(@RequestBody ConfigDTO configDTO) throws RestException {
        return buildSuccessResponse(configurationService.getConfigData(configDTO).orElse(null));
    }



    @PostMapping("get-config-by-scope-name")
    @PreAuthorize("hasAnyRole('CONFIG_READ', 'CONFIG_READ_WRITE')")
    public ResponseEntity<Object> getConfigByScope(@RequestBody ConfigByScopeRequestDTO requestDTO) throws RestException {
        return buildSuccessResponse(configurationService.getConfigByScopeName(requestDTO.getName()));
    }



    @PostMapping("get-config-by-scope-id")
    @PreAuthorize("hasAnyRole('CONFIG_READ', 'CONFIG_READ_WRITE')")
    public ResponseEntity<Object> getConfigByScopeId(@RequestBody ConfigByScopeRequestDTO requestDTO) throws RestException {
        return buildSuccessResponse(configurationService.getConfigByScopeId(requestDTO.getId()));
    }



    @PostMapping("get-config-by-scope-name-id")
    @PreAuthorize("hasAnyRole('CONFIG_READ', 'CONFIG_READ_WRITE')")
    public ResponseEntity<Object> getConfigByScopeNameAndId(@RequestBody ConfigByScopeRequestDTO requestDTO) throws RestException {
        return buildSuccessResponse(configurationService.getConfigByScopeNameAndId(requestDTO.getName(), requestDTO.getId()));
    }



    @PostMapping("get-typed-config")
    @PreAuthorize("hasAnyRole('CONFIG_READ', 'CONFIG_READ_WRITE')")
    public ResponseEntity<Object> getTypedConfig(@RequestBody ConfigDTO configDTO) throws RestException {
        return buildSuccessResponse(configurationService.getConfigData(configDTO.getConfigKey(), configOptional -> {
            if(configOptional.isPresent() && notNull(configOptional.get().getData())){
                ConfigEO config = configOptional.get();
                switch (config.getDataType()){
                    case DATE:
                        Date res = new Date();
                        res.setTime(Long.valueOf(config.getData()));
                        return res;

                    case LONG:
                        return Long.valueOf(config.getData());

                    case INTEGER:
                        return Integer.valueOf(config.getData());

                    case DOUBLE:
                        return Double.valueOf(config.getData());

                    case BOOLEAN:
                        return Boolean.valueOf(config.getData());

                    case STRING:
                        return String.valueOf(config.getData());

                    default: return config.getData();
                }
            }

            return null;
        }));
    }


}
