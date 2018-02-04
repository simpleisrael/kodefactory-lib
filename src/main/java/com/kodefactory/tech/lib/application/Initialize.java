package com.kodefactory.tech.lib.application;

import com.kodefactory.tech.lib.configuration.constants.DataType;
import com.kodefactory.tech.lib.configuration.constants.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class Initialize implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(Initialize.class);

    protected ApplicationUtil applicationUtil;

    public Initialize(ApplicationUtil applicationUtil){
        this.applicationUtil = applicationUtil;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextStartedEvent) {
        initializeAuthorities();
        initializeRole();
        initializeConfigurations();
        logger.info("Application Initialized.");
    }


    /**
     * Initialize the base role in the platform
     */
    private void initializeRole() {
        applicationUtil.createRole("Super Admin", "The role have access to all functionality on the platform");
    }

    /**
     * Initialize all the authorities base on the platform
     */
    private void initializeAuthorities() {
        applicationUtil.createAuthority("USER", "Grant permission to ACCESS user resource", "User");
        applicationUtil.createAuthority("USER_READ", "Grant permission to READ user resource", "User");
        applicationUtil.createAuthority("USER_WRITE", "Grant permission to WRITE user resource", "User");
        applicationUtil.createAuthority("USER_READ_WRITE", "Grant permission to READ and WRITE user resource", "User");

        applicationUtil.createAuthority("ROLE", "Grant permission to ACCESS role resource", "Role");
        applicationUtil.createAuthority("READ", "Grant permission to READ role resource", "Role");
        applicationUtil.createAuthority("WRITE", "Grant permission to WRITE role resource", "Role");
        applicationUtil.createAuthority("READ_WRITE", "Grant permission to READ and WRITE role resource", "Role");

        applicationUtil.createAuthority("AUTHORITY", "Grant permission to ACCESS authority resource", "Authority");
        applicationUtil.createAuthority("AUTHORITY_READ", "Grant permission to READ authority resource", "Authority");
        applicationUtil.createAuthority("AUTHORITY_WRITE", "Grant permission to WRITE authority resource", "Authority");
        applicationUtil.createAuthority("AUTHORITY_READ_WRITE", "Grant permission to READ and WRITE authority resource", "Authority");

        applicationUtil.createAuthority("CONFIG", "Grant permission to ACCESS config resource", "Config");
        applicationUtil.createAuthority("CONFIG_READ", "Grant permission to READ config resource", "Config");
        applicationUtil.createAuthority("CONFIG_READ", "Grant permission to READ config resource", "Config");
        applicationUtil.createAuthority("CONFIG_WRITE", "Grant permission to WRITE config resource", "Config");
        applicationUtil.createAuthority("CONFIG_READ_WRITE", "Grant permission to READ and WRITE config resource", "Config");

        applicationUtil.createAuthority("APPROVAL", "Grant permission to ACCESS approval resource", "Approval");
        applicationUtil.createAuthority("APPROVAL_READ", "Grant permission to READ approval resource", "Approval");
        applicationUtil.createAuthority("APPROVAL_APPROVE", "Grant permission to APPROVE approval resource", "Approval");
        applicationUtil.createAuthority("APPROVAL_REJECT", "Grant permission to REJECT approval resource", "Approval");

        applicationUtil.createAuthority("SETTING", "Grant permission to SETTING menu resource", "Setting");

        applicationUtil.createAuthority("TEST2", "Grant permission to READ and WRITE test2 resource", "Test");
    }


    /**
     * Initialize all the base configurations on the platform
     */
    private void initializeConfigurations() {
        applicationUtil.createConfiguration(ConfigKeys.APPROVE_ROLE.value(), true, DataType.BOOLEAN, "system", "Approve Role");
        applicationUtil.createConfiguration(ConfigKeys.APPROVE_AUTHORITY.value(), true, DataType.BOOLEAN, "system", "Approve Authority");
        applicationUtil.createConfiguration(ConfigKeys.APPROVE_USER.value(), false, DataType.BOOLEAN, "system", "Approve User");
        applicationUtil.createConfiguration(ConfigKeys.APPROVE_CONFIGURATION.value(), true, DataType.BOOLEAN, "system", "Approve Configuration");

        applicationUtil.createConfiguration(ConfigKeys.APPLICATION_MODE.value(), "development", DataType.STRING, "system", "Application Mode");

        applicationUtil.createConfiguration(ConfigKeys.ROLE_APPROVAL_LEVEL.value(), 1, DataType.INTEGER, "system", "Role Approval Level");
        applicationUtil.createConfiguration(ConfigKeys.AUTHORITY_APPROVAL_LEVEL.value(), 1, DataType.INTEGER, "system", "Authority Approval Level");
        applicationUtil.createConfiguration(ConfigKeys.USER_APPROVAL_LEVEL.value(), 1, DataType.INTEGER, "system", "User Approval Level");
        applicationUtil.createConfiguration(ConfigKeys.CONFIGURATION_APPROVAL_LEVEL.value(), 1, DataType.INTEGER, "system", "Configuration Approval Level");

        applicationUtil.createConfiguration(ConfigKeys.SETUP_COMPLETED.value(), false, DataType.BOOLEAN, "system", false);
    }
}
