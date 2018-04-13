package com.kodefactory.tech.lib.security.dto;

import com.kodefactory.tech.lib.security.domain.AuthorityEO;
import com.kodefactory.tech.lib.security.domain.RoleEO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserPublicDTO {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean enabled;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss")
    private Date lastLoginDate;
    private List<RoleEO> roles;
    private List<AuthorityEO> authorities;
    private Integer approvalLevel;
    private Integer minApprovalLevel;
    private Integer maxApprovalLevel;
}
