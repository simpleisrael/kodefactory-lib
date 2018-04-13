package com.kodefactory.tech.lib.security.service;

import com.kodefactory.tech.lib.core.dto.ResponseMessage;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.security.domain.AuthorityEO;
import com.kodefactory.tech.lib.security.domain.RoleEO;
import com.kodefactory.tech.lib.security.dto.*;

import java.util.List;

public interface UserService {

    ResponseMessage saveUser(UserDTO userDTO) throws RestException;

    ResponseMessage saveRole(RoleDTO roleDTO) throws RestException;

    UserDTO findUserByEmail(String email) throws RestException;

    ResponseMessage saveAuthority(AuthorityDTO authorityDTO) throws RestException;

    Boolean removeRole(RoleDTO roleDTO) throws RestException;

    Boolean assignRoleToUser(UserRoleDTO assignRoleDTO) throws RestException;

    Boolean userHasRole(UserRoleDTO userRoleDTO) throws RestException;

    Boolean assignAuthoritiesToRole(AssignAuthoritiesDTO assignAuthoritiesDTO) throws RestException;

    Boolean roleHasAuthority(RoleAuthorityDTO roleAuthorityDTO) throws RestException;

    List<RoleEO> listRoles();

    List<AuthorityEO> listAuthorities();
    List<AuthorityEO> listAuthorities(String[] keys);

    List<UserPublicDTO> listUsers() throws RestException;

    Boolean resetPassword(PasswordDTO passwordDTO) throws RestException;

    Boolean changePassword(PasswordDTO passwordDTO) throws RestException;

    ResponseMessage initiatePasswordReset(PasswordResetDTO passwordResetDTO) throws RestException;

    Boolean validateToken(PasswordResetDTO passwordResetDTO) throws RestException;

    Boolean emailExist(String email);

}
