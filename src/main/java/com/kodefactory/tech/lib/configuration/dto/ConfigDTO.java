package com.kodefactory.tech.lib.configuration.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConfigDTO {
    private Long id;
    private String configKey;
    private String data;
    private String label;
    private String scopeName;
    private Long scopeId;
    private String dataType;
    private Object rawData;
    private Object defaultValue;
    private Boolean editable;
    private List<String> options;
}
