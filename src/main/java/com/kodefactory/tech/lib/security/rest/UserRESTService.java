package com.kodefactory.tech.lib.security.rest;

import com.kodefactory.tech.lib.core.rest.BaseREST;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.security.dto.*;
import com.kodefactory.tech.lib.security.jwt.JwtUser;
import com.kodefactory.tech.lib.security.service.AuthenticatedUser;
import com.kodefactory.tech.lib.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user-service")
public class UserRESTService extends BaseREST {

    private UserService userService;
    private AuthenticatedUser authenticatedUser;

    public UserRESTService(UserService userService, AuthenticatedUser authenticatedUser) {
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;
    }


    @GetMapping("me")
    public JwtUser getMe() {
        return authenticatedUser.getAuthenticatedUser();
    }


    @PostMapping("save-role")
    @PreAuthorize("hasAnyRole('ROLE_WRITE', 'ROLE_READ_WRITE')")
    public ResponseEntity<Object> saveRole(@RequestBody RoleDTO roleDTO) throws RestException {
        return buildSuccessResponse(userService.saveRole(roleDTO));
    }



    @PostMapping("save-user")
    @PreAuthorize("hasAnyRole('USER_WRITE', 'USER_READ_WRITE')")
    public ResponseEntity<Object> saveUser(@RequestBody UserDTO userDTO) throws RestException {
        return buildSuccessResponse(userService.saveUser(userDTO));
    }



    @PostMapping("save-authority")
    @PreAuthorize("hasAnyRole('AUTHORITY_WRITE', 'AUTHORITY_READ_WRITE')")
    public ResponseEntity<Object> saveAuthority(@RequestBody AuthorityDTO authorityDTO) throws RestException {
        return buildSuccessResponse(userService.saveAuthority(authorityDTO));
    }



    @PostMapping("assign-role")
    @PreAuthorize("hasAnyRole('USER_WRITE', 'USER_READ_WRITE', 'ROLE_WRITE', 'ROLE_READ_WRITE')")
    public ResponseEntity<Object> assignRole(@RequestBody UserRoleDTO userRoleDTO) throws RestException {
        return buildSuccessResponse(userService.assignRoleToUser(userRoleDTO));
    }



    @PostMapping("has-role")
    @PreAuthorize("hasRoles('USER_READ', 'ROLE_READ')")
    public ResponseEntity<Object> hasRole(@RequestBody UserRoleDTO userRoleDTO) throws RestException {
        return buildSuccessResponse(userService.userHasRole(userRoleDTO));
    }



    @GetMapping("list-roles")
    @PreAuthorize("hasAnyRole('ROLE_READ', 'ROLE_READ_WRITE')")
    public ResponseEntity<Object> listRoles() throws RestException {
        return buildSuccessResponse(userService.listRoles());
    }



    @GetMapping("list-users")
    @PreAuthorize("hasAnyRole('USER_READ', 'USER_READ_WRITE')")
    public ResponseEntity<Object> listUsers() throws RestException {
        return buildSuccessResponse(userService.listUsers());
    }



    @GetMapping("list-authorities")
    @PreAuthorize("hasAnyRole('AUTHORITY_READ', 'AUTHORITY_WRITE')")
    public ResponseEntity<Object> listAuthorities() throws RestException {
        return buildSuccessResponse(userService.listAuthorities());
    }



    @PostMapping("assign-authorities")
    @PreAuthorize("hasAnyRole('USER_READ', 'AUTHORITY_READ')")
    public ResponseEntity<Object> assignAuthorities(@RequestBody AssignAuthoritiesDTO assignAuthoritiesDTO) throws RestException {
        return buildSuccessResponse(userService.assignAuthoritiesToRole(assignAuthoritiesDTO));
    }



    @PostMapping("role-has-authority")
    @PreAuthorize("hasRoles('USER_READ', 'AUTHORITY_READ')")
    public ResponseEntity<Object> assignAuthorities(@RequestBody RoleAuthorityDTO roleAuthorityDTO) throws RestException {
        return buildSuccessResponse(userService.roleHasAuthority(roleAuthorityDTO));
    }



    @PostMapping("validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody PasswordResetDTO passwordResetDTO) throws RestException {
        return buildSuccessResponse(userService.validateToken(passwordResetDTO));
    }


}
