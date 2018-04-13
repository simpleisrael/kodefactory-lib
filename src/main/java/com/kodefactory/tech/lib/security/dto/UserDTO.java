package com.kodefactory.tech.lib.security.dto;

import com.kodefactory.tech.lib.security.domain.RoleEO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    protected Long id;
    protected String username;
    protected String password;
    protected String confirmPassword;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String phone;
    protected Boolean enabled;
    protected Boolean terms;
    protected Date lastLoginDate;
    protected List<RoleEO> roles;
    protected Integer minApprovalLevel;
    protected Integer maxApprovalLevel;
    protected Integer approvalLevel;
    protected List<String> authorities;
}
