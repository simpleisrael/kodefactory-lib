package com.kodefactory.tech.lib.security.dto;

import com.kodefactory.tech.lib.security.domain.RoleEO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean enabled;
    private Boolean terms;
    private Date lastLoginDate;
    private List<RoleEO> roles;
}
