package com.kodefactory.tech.lib.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodefactory.tech.lib.approval.service.ApprovalService;
import com.kodefactory.tech.lib.configuration.constants.ConfigKeys;
import com.kodefactory.tech.lib.configuration.service.ConfigurationService;
import com.kodefactory.tech.lib.core.dto.BaseDTO;
import com.kodefactory.tech.lib.core.util.CopyUtil;
import com.kodefactory.tech.lib.core.util.ValidationUtil;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.exception.error.ApiError;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class BaseServiceImpl {
    public void validateIsRequired(Object source, String... fields) throws RestException {
        ApiError apiError = ValidationUtil.buildValidateIsRequiredObject(source, fields);
        if(apiError != null){
            throwException(apiError);
        }
    }

    public void validateNotNull(Object object, String message) throws RestException{
        validateImpl(object, message, (obj) -> (obj != null));
    }

    public void validateEquals(Object object1, Object object2, String message) throws RestException{
        if(!Objects.equals(object1, object2)){
            throwException(message);
        }
    }

    public void validateListNotEmpty(List<? extends BaseDTO> object, String message) throws RestException{
        validateImpl(object, message, (obj) -> (obj != null && ((List) obj).size() > 0));
    }

    public void copyRestObjects(Object source, Object destination) throws RestException {
        try {
            CopyUtil.copy(source, destination);
        }catch (Exception ex) {
            throwException(ex.getMessage());
        }
    }

    public boolean notNull(Object object) {
        return Objects.nonNull(object);
    }

    public boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public boolean isEqual(Object object, Object object2){
        return Objects.equals(object, object2);
    }

    private void validateImpl(Object object, String message, Predicate<Object> condition) throws RestException{
        if(!condition.test(object)){
            throwException(message);
        }
    }

    public void throwException(String message) throws RestException {
        throw new RestException(new ApiError(message));
    }

    public void throwException(ApiError apiError) throws RestException {
        throw new RestException(apiError);
    }

    public void sendForApproval(ApprovalService approvalService, Object entity, Integer minApprovalLevel, Integer maxApprovalLevel, String title) throws RestException{
        approvalService.sendForApproval(entity, minApprovalLevel, maxApprovalLevel, title);
    }

    public <T> T clone(T object, Class<T> klass) throws RestException{
        if(isNull(object)) return null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objString = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(objString, klass);
        } catch (Exception e) {
            throwException(e.getMessage());
        }
        throwException("Unable to clone object");
        return null;
    }

    public <T> Object toJsonObject(String jsonString, Class<T> klass){
        try{
            return new ObjectMapper().readValue(jsonString, klass);
        }catch (Exception e){}
        return null;
    }


    public Integer getApprovalLevel(ConfigurationService configurationService, String configKey) throws RestException {
        return configurationService
                .getConfigData(configKey,
                        (configEO -> {
                            if(configEO.isPresent()){
                                return Integer.valueOf(configEO.get().getData().toString());
                            }
                            return 1;
                        })
                );
    }
}
