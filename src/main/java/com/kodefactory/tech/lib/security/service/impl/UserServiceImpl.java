package com.kodefactory.tech.lib.security.service.impl;

import com.kodefactory.tech.lib.approval.service.ApprovalService;
import com.kodefactory.tech.lib.configuration.constants.ConfigKeys;
import com.kodefactory.tech.lib.configuration.service.ConfigurationService;
import com.kodefactory.tech.lib.core.dto.ResponseMessage;
import com.kodefactory.tech.lib.core.event.EventPublisher;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.exception.error.ApiError;
import com.kodefactory.tech.lib.security.domain.AuthorityEO;
import com.kodefactory.tech.lib.security.domain.RoleEO;
import com.kodefactory.tech.lib.security.domain.UserEO;
import com.kodefactory.tech.lib.security.dto.*;
import com.kodefactory.tech.lib.security.event.PasswordChangedEvent;
import com.kodefactory.tech.lib.security.event.PasswordResetInitiatedEvent;
import com.kodefactory.tech.lib.security.event.UserCreatedEvent;
import com.kodefactory.tech.lib.security.repository.AuthorityRepository;
import com.kodefactory.tech.lib.security.repository.RoleRepository;
import com.kodefactory.tech.lib.security.repository.UserRepository;
import com.kodefactory.tech.lib.security.event.UserUpdatedEvent;
import com.kodefactory.tech.lib.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;
    private EventPublisher eventPublisher;
    private ApprovalService approvalService;
    private ConfigurationService configurationService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AuthorityRepository authorityRepository,
                           PasswordEncoder passwordEncoder,
                           EventPublisher eventPublisher,
                           ConfigurationService configurationService,
                           ApprovalService approvalService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.configurationService = configurationService;
        this.approvalService = approvalService;
    }


    /**
     * Save or Update a user
     * @param userDTO
     * @return
     * @throws RestException
     */
    @Override
    public ResponseMessage saveUser(UserDTO userDTO) throws RestException {
        validateIsRequired(userDTO, "firstname", "lastname", "email", "password");

        boolean isNew = false;
        UserEO user = null;

        Boolean approveUser = configurationService.checkConfig(ConfigKeys.APPROVE_USER.value(), (configEO) -> Boolean.valueOf(configEO.getData()));

        if(Objects.nonNull(userDTO.getId())){
            if(approveUser) {
                user = clone(userRepository.findOne(userDTO.getId()), UserEO.class);
            }else {
                user = userRepository.findOne(userDTO.getId());
            }
        }

        if(Objects.isNull(user)) {
            user = new UserEO();
            isNew = true;
        }

        copyRestObjects(userDTO, user);
        if(isNew && user.getUsername()==null){
            user.setUsername(user.getEmail());
            user.setEnabled(true);
            user.setDeleted(false);
            user.setLastPasswordResetDate(new Date());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        String message = "";

        if(approveUser){
            Integer userApprovalLevel = getApprovalLevel(configurationService, ConfigKeys.USER_APPROVAL_LEVEL.value());
            sendForApproval(approvalService, user, userApprovalLevel, userApprovalLevel, "Create/Edit User");
            message = "User sent for approval";
        }else {
            user = userRepository.save(user);
            message = "User Saved!";
        }

        if(isNew){
            eventPublisher.publish(new UserCreatedEvent<>(user));
        }else{
            eventPublisher.publish(new UserUpdatedEvent<>(user));
        }
        return new ResponseMessage(message);
    }


    /**
     * Save or Update a role
     * @param roleDTO
     * @return
     * @throws RestException
     */
    @Override
    public ResponseMessage saveRole(RoleDTO roleDTO) throws RestException {
        validateIsRequired(roleDTO, "name", "description");

        RoleEO role = null;
        Boolean approveRole = configurationService.checkConfig(ConfigKeys.APPROVE_ROLE.value(), (configEO) -> Boolean.valueOf(configEO.getData()));


        if(Objects.nonNull(roleDTO.getId())) {
            if(approveRole) {
                //The role is cloned because the role object retrieved is still attached to the entity manager
                //If any changes are made on the role object, it would persist it automatically and that isn't desirable
                role = clone(roleRepository.findOne(roleDTO.getId()), RoleEO.class);
            }else {
                role = roleRepository.findOne(roleDTO.getId());
            }
        }

        if(Objects.isNull(role)){
            role = new RoleEO();
            role.setDeleted(false);
        }

        copyRestObjects(roleDTO, role);

        List<AuthorityEO> authorities = roleDTO.getAuthorities().stream().map(authorityDTO -> {
            AuthorityEO authorityEO = authorityRepository.findOne(authorityDTO.getId());
            try{copyRestObjects(authorityDTO, authorityEO);}catch(Exception ex){ex.printStackTrace();}
            return authorityEO;
        }).collect(toList());
        role.setAuthorities(authorities);

        String messsage = "";

        if(approveRole){
            Integer userApprovalLevel = getApprovalLevel(configurationService, ConfigKeys.USER_APPROVAL_LEVEL.value());
            sendForApproval(approvalService, role, userApprovalLevel, userApprovalLevel, "Create/Edit Role");
            messsage = "Role sent for approval";
        }else {
            role = roleRepository.save(role);
            messsage = "Role Saved!";
        }

        return new ResponseMessage(messsage);
    }

    @Override
    public UserDTO findUserByEmail(String email) throws RestException {
        Optional<UserEO> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            return userOptional.map(UserEO::toPrivateDto).orElse(null);
        }else {
            throwException(String.format("User with email %s does not exist", email));
        }
        return null;
    }


    /**
     * Save or Update a authority
     * @param authorityDTO
     * @return
     * @throws RestException
     */
    @Override
    public ResponseMessage saveAuthority(AuthorityDTO authorityDTO) throws RestException {
        validateIsRequired(authorityDTO, "name", "description");
        AuthorityEO authority = null;
        Boolean approveAuthority = configurationService.checkConfig(ConfigKeys.APPROVE_AUTHORITY.value(), (configEO) -> Boolean.valueOf(configEO.getData()));

        if(Objects.nonNull(authorityDTO.getId())) {
            if(approveAuthority) {
                authority = clone(authorityRepository.findOne(authorityDTO.getId()), AuthorityEO.class);
            }else {
                authority = authorityRepository.findOne(authorityDTO.getId());
            }
        }

        if(Objects.isNull(authority)){
            authority = new AuthorityEO();
        }

        copyRestObjects(authorityDTO, authority);
        if(!authority.getName().contains("ROLE_")) {
            authority.setName("ROLE_"+authority.getName());
        }

        String message = "";

        if(approveAuthority){
            Integer authorityApprovalLevel = getApprovalLevel(configurationService, ConfigKeys.USER_APPROVAL_LEVEL.value());
            sendForApproval(approvalService, authority, authorityApprovalLevel, authorityApprovalLevel, "Create/Edit Authority");
            message = "Authority sent for approval";
        }else {
            authorityRepository.save(authority);
            message = "Authority Saved";
        }

        return new ResponseMessage(message);
    }

    /**
     * Soft delete an existing role
     * @param roleDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean removeRole(RoleDTO roleDTO) throws RestException {
        validateIsRequired(roleDTO, "id");
        RoleEO role = roleRepository.findOne(roleDTO.getId());
        validateNotNull(role, String.format("Role with ID %d does not exist", roleDTO.getId()));
        role.setDeleted(true);
        roleRepository.save(role);
        return true;
    }

    /**
     * Assign role to a user
     * @param assignRoleDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean assignRoleToUser(UserRoleDTO assignRoleDTO) throws RestException{
        validateIsRequired(assignRoleDTO, "userId", "roleId");
        Map<String, Object> response = this.validateUserAndRole(assignRoleDTO.getUserId(), assignRoleDTO.getRoleId());
        UserEO user = (UserEO)response.get("user");
        RoleEO role = (RoleEO)response.get("role");

        if(user.hasRole(assignRoleDTO.getRoleId())){
            throw new RestException(new ApiError("Role already assigned to user"));
        }

        user.getRoles().add(role);
        userRepository.save(user);
        return true;
    }


    /**
     * Check if the user have a role with roleId
     * @param userRoleDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean userHasRole(UserRoleDTO userRoleDTO) throws RestException {
        validateIsRequired(userRoleDTO, "userId", "roleId");
        Map<String, Object> response = this.validateUserAndRole(userRoleDTO.getUserId(), userRoleDTO.getRoleId());
        UserEO user = (UserEO)response.get("user");
        return user.hasRole(userRoleDTO.getRoleId());
    }


    /**
     * Assign authority to a role
     * @param assignAuthoritiesDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean assignAuthoritiesToRole(AssignAuthoritiesDTO assignAuthoritiesDTO) throws RestException {
        RoleEO role = roleRepository.findOne(assignAuthoritiesDTO.getRoleId());
        validateNotNull(role, String.format("Role with ID %d does not exist", assignAuthoritiesDTO.getRoleId()));

        assignAuthoritiesDTO.getAuthorityIds().forEach(authorityId -> {
            AuthorityEO authority = authorityRepository.findOne(authorityId);
            if(Objects.nonNull(authority)){
//                role.getAuthorities().add(authority);
            }
        });
        roleRepository.save(role);
        return true;
    }


    /**
     * Checks if role have authority with Id
     * @param roleAuthorityDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean roleHasAuthority(RoleAuthorityDTO roleAuthorityDTO) throws RestException {
        Map<String, Object> response = this.validateUserAndRole(roleAuthorityDTO.getRoleId(), roleAuthorityDTO.getAuthorityId());
        RoleEO role = (RoleEO)response.get("role");
        return role.hasAuthority(roleAuthorityDTO.getAuthorityId());
    }


    /**
     * List all existing roles
     * @return
     */
    @Override
    public List<RoleEO> listRoles() {
        return roleRepository.findAll()
                .stream()
                .filter(role ->  !role.getDeleted())
                .collect(toList());
    }



    /**
     * List all existing authorities
     * @return
     */
    @Override
    public List<AuthorityEO> listAuthorities() {
        return authorityRepository.findAll().stream().sorted(Comparator.comparing(AuthorityEO::getGroupName)).collect(toList());
    }

    @Override
    public List<AuthorityEO> listAuthorities(String[] names){
        if(names == null) return new ArrayList<>();
        List<AuthorityEO> authorities = new ArrayList<>();
        authorities = Arrays.stream(names).map(name -> {
            Optional<AuthorityEO> authorityOptional = authorityRepository.findByName(name);
            return authorityOptional.orElseGet(() -> null);
        }).filter(Objects::nonNull)
          .sorted(Comparator.comparing(AuthorityEO::getGroupName)).collect(toList());
        return authorities;
    }


    @Override
    public List<UserPublicDTO> listUsers() throws RestException {
        List<UserEO> users = userRepository.findAll();
        return users.stream()
                .filter(user -> !user.getDeleted())
                .map(UserEO::toPublicDto)
                .collect(toList());
    }



    @Override
    public Boolean resetPassword(PasswordDTO passwordDTO) throws RestException {
        validateIsRequired(passwordDTO, "email", "newPassword", "confirmNewPassword", "currentPassword");
        Optional<UserEO> userOptional = userRepository.findByEmail(passwordDTO.getEmail());
        if(!userOptional.isPresent()){
            throwException(String.format("User with email %s does not exist", passwordDTO.getEmail()));
        }
        UserEO user = userOptional.get();
        validateEquals(passwordDTO.getNewPassword(), passwordDTO.getConfirmNewPassword(), "Password does not match");
        user.setResetToken(null);
        userRepository.save(user);
        eventPublisher.publish(new PasswordChangedEvent<>(user));
        return true;
    }



    @Override
    public Boolean changePassword(PasswordDTO passwordDTO) throws RestException {
        validateIsRequired(passwordDTO, "email", "newPassword", "confirmNewPassword", "currentPassword");
        Optional<UserEO> userOptional = userRepository.findByEmail(passwordDTO.getEmail());
        if(!userOptional.isPresent()){
            throwException(String.format("User with email %s does not exist", passwordDTO.getEmail()));
        }
        UserEO user = userOptional.get();
        validateEquals(passwordDTO.getNewPassword(), passwordDTO.getConfirmNewPassword(), "Password does not match");
        validateEquals(passwordEncoder.encode(passwordDTO.getNewPassword()),
                passwordEncoder.encode(user.getPassword()), "Current password is invalid");
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        user.setResetToken(null);
        userRepository.save(user);
        eventPublisher.publish(new PasswordChangedEvent<>(user));
        return true;
    }


    /**
     * Initiate Password Reset Process
     * @param passwordResetDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean initiatePasswordReset(PasswordResetDTO passwordResetDTO) throws RestException {
        Optional<UserEO> userOptional = userRepository.findByEmail(passwordResetDTO.getEmail());
        if(!userOptional.isPresent()){
            throwException(String.format("User with email %s does not exist", passwordResetDTO.getEmail()));
        }
        UserEO user = userOptional.get();
        user.setResetToken(UUID.randomUUID().toString());
        userRepository.save(user);
        eventPublisher.publish(new PasswordResetInitiatedEvent<>(user));
        return null;
    }


    /**
     * Check if the token passed is valid
     * @param passwordResetDTO
     * @return
     * @throws RestException
     */
    @Override
    public Boolean validateToken(PasswordResetDTO passwordResetDTO) throws RestException {
        Optional<UserEO> userOptional = userRepository.findByResetToken(passwordResetDTO.getToken());
        if(!userOptional.isPresent()){
            throwException(String.format("Invalid Reset Token"));
        }
        return true;
    }

    @Override
    public Boolean emailExist(String email) {
        Optional<UserEO> user = userRepository.findByEmail(email);
        return user.isPresent();
    }


    private Map<String, Object> validateUserAndRole(Long userId, Long roleId) throws RestException{
        Map<String, Object> response = new HashMap<>();
        UserEO user = userRepository.findOne(userId);
        RoleEO role = roleRepository.findOne(roleId);
        validateNotNull(user, String.format("User with ID %s does not exist", userId));
        validateNotNull(role, String.format("Role with ID %s does not exist", roleId));
        response.put("user", user);
        response.put("role", role);
        return response;
    }



    private Map<String, Object> validateRoleAndAuthority(Long roleId, Long authorityId) throws RestException{
        Map<String, Object> response = new HashMap<>();
        AuthorityEO authority = authorityRepository.findOne(authorityId);
        RoleEO role = roleRepository.findOne(roleId);
        validateNotNull(role, String.format("Role with ID %s does not exist", roleId));
        validateNotNull(authority, String.format("Authority with ID %s does not exist", authorityId));
        response.put("authority", authority);
        response.put("role", role);
        return response;
    }
}
