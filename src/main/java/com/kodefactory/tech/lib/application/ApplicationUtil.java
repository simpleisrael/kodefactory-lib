package com.kodefactory.tech.lib.application;

import com.kodefactory.tech.lib.configuration.constants.DataType;
import com.kodefactory.tech.lib.configuration.domain.ConfigEO;
import com.kodefactory.tech.lib.configuration.repository.ConfigurationRepository;
import com.kodefactory.tech.lib.security.domain.AuthorityEO;
import com.kodefactory.tech.lib.security.domain.RoleEO;
import com.kodefactory.tech.lib.security.repository.AuthorityRepository;
import com.kodefactory.tech.lib.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * This class is generally used to bootstrap system specific roles, authorities and configuration
 */

@Service
@Transactional
public class ApplicationUtil {
    private Logger logger = LoggerFactory.getLogger(ApplicationUtil.class);

    private AuthorityRepository authorityRepository;
    private RoleRepository roleRepository;
    private ConfigurationRepository configurationRepository;

    public ApplicationUtil(AuthorityRepository authorityRepository,
                           RoleRepository roleRepository,
                           ConfigurationRepository configurationRepository){
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.configurationRepository = configurationRepository;
    }


    /**
     * Create system specific role
     * @param name
     * @param description
     */
    public void createRole(String name, String description) {
        createRoleImpl(name, description, authorityRepository.findAll());
    }


    /**
     * Create system specific role
     * @param name
     * @param description
     * @param authorities
     */
    public void createRole(String name, String description, List<AuthorityEO> authorities) {
        createRoleImpl(name, description, authorities);
    }


    /**
     * Create system specific role
     * @param name
     * @param description
     */
    public void createRoleImpl(String name, String description, List<AuthorityEO> authorities) {
        Optional<RoleEO> roleOptional = roleRepository.findByName(name);
        if(!roleOptional.isPresent()){
            RoleEO role = new RoleEO();
            role.setName(name);
            role.setDescription(description);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
    }

    /**
     * Create system specific authority
     * @param name
     * @param description
     */
    public void createAuthority(String name, String description){
        createAuthorityImpl(name, description, null,null);
    }

    /**
     * Create system specific authority
     * @param name
     * @param description
     */
    public void createAuthority(String name, String description, String groupName){
        createAuthorityImpl(name, description, null, groupName);
    }

    /**
     *
     * @param name
     * @param description
     * @param label
     * @param groupName
     */
    public void createAuthority(String name, String description, String groupName, String label){
        createAuthorityImpl(name, description, label, groupName);
    }


    private void createAuthorityImpl(String name, String description, String label, String groupName){
        Optional<AuthorityEO> authorityOptional = authorityRepository.findByName("ROLE_"+name);
        if(!authorityOptional.isPresent()){
            AuthorityEO aeo = new AuthorityEO();
            aeo.setName("ROLE_"+name);
            aeo.setDescription(description);
            aeo.setGroupName(groupName);
            aeo.setLabel(label);
            authorityRepository.save(aeo);
            logger.info("Authority with name {} created", name);
        }else {
            AuthorityEO authorityEO = authorityOptional.get();
            if(authorityEO.getGroupName() == null && groupName != null) {
                authorityEO.setGroupName(groupName);
                authorityRepository.save(authorityEO);
            }
        }
    }


    /**
     * Create system specific configuration
     * @param configKey
     * @param data
     * @param dataType
     * @param scopeName
     * @Param editable
     */
    public void createConfiguration(String configKey, Object data, DataType dataType, String scopeName, boolean editable) {
        createConfigImpl(configKey, data, dataType, scopeName, editable, null,null);
    }

    /**
     * Create system specific configuration
     * @param configKey
     * @param data
     * @param dataType
     * @param scopeName
     * @Param editable
     */
    public void createConfiguration(String configKey, Object data, DataType dataType, String scopeName) {
        createConfigImpl(configKey, data, dataType, scopeName, true,  null,null);
    }

    /**
     * Create system specific configuration
     * @param configKey
     * @param data
     * @param dataType
     * @param scopeName
     * @Param editable
     * @Param options
     */
    public void createConfiguration(String configKey, Object data, DataType dataType, String scopeName, List<String> options) {
        createConfigImpl(configKey, data, dataType, scopeName, true, null, options);
    }


    public void createConfiguration(String configKey, Object data, DataType dataType, String scopeName, String label) {
        createConfigImpl(configKey, data, dataType, scopeName, true, label, null);
    }


    /**
     *
     * @param configKey
     * @param data
     * @param dataType
     * @param scopeName
     * @param label
     * @param options
     */
    public void createConfiguration(String configKey, Object data, DataType dataType, String scopeName, String label, List<String> options) {
        createConfigImpl(configKey, data, dataType, scopeName, true, label, options);
    }


    private void createConfigImpl(String configKey, Object data, DataType dataType, String scopeName, boolean editable, String label, List<String> options) {
        Optional<ConfigEO> config = configurationRepository.findByConfigKey(configKey);
        if(!config.isPresent()){
            ConfigEO configEO = new ConfigEO();
            configEO.setConfigKey(configKey);
            configEO.setData(data.toString());
            configEO.setDataType(dataType);
            configEO.setScopeName(scopeName);
            configEO.setEditable(editable);
            configEO.setOptions(options);
            configEO.setLabel(label);
            configurationRepository.save(configEO);
            logger.info("Configuration with key {} created", configKey);
        }else {
            boolean updated = false;
            ConfigEO configEO = config.get();
            if(configEO.getScopeName() == null && scopeName != null){
                configEO.setScopeName(scopeName);
                updated = true;
            }
            if(configEO.getLabel() == null && label != null) {
                configEO.setLabel(label);
                updated = true;
            }
            if(configEO.getOptions() == null && options != null) {
                configEO.setOptions(options);
                updated = true;
            }
            if(updated){
                configurationRepository.save(configEO);
            }
        }
    }
}
