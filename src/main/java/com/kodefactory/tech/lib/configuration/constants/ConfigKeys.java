package com.kodefactory.tech.lib.configuration.constants;

public enum ConfigKeys {
    APPROVE_ROLE, APPROVE_AUTHORITY, APPROVE_USER, APPROVE_CONFIGURATION,
    APPLICATION_MODE,
    ROLE_APPROVAL_LEVEL, AUTHORITY_APPROVAL_LEVEL, USER_APPROVAL_LEVEL, CONFIGURATION_APPROVAL_LEVEL,
    SETUP_COMPLETED;

    public String value(){
        return this.name();
    }
}
