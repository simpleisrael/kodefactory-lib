package com.kodefactory.tech.lib.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodefactory.tech.lib.approval.domain.ApprovalLevelEO;
import com.kodefactory.tech.lib.approval.domain.ApprovalRequestEO;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import com.kodefactory.tech.lib.core.util.CopyUtil;
import com.kodefactory.tech.lib.security.dto.UserDTO;
import com.kodefactory.tech.lib.security.dto.UserPublicDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"approvalLevel", "approvalRequestCollection"})
@ToString(exclude = {"approvalLevel", "approvalRequestCollection"})
public class UserEO extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 50, unique = true)
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    @JsonIgnore
    private String password;

    @Column(name = "FIRSTNAME", length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "PHONE", length = 50)
    @Size(min = 4, max = 50)
    private String phone;

    @Column(name = "ENABLED")
    private Boolean enabled = true;

    @Column(name="deleted")
    @JsonIgnore
    private Boolean deleted = false;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "reset_token")
    @JsonIgnore
    private String resetToken;

    @Column(name = "activation_token")
    @JsonIgnore
    private String activationToken;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @Column(name = "lastLoginDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApprovalLevelEO approvalLevel = null;

    @OneToMany(mappedBy = "requestCreatedByUser")
    @JsonIgnore
    private List<ApprovalRequestEO> approvalRequestCollection;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private List<RoleEO> roles = new ArrayList<>();

    @JsonIgnore
    public List<AuthorityEO> getAuthorities() {
        return roles.stream()
                    .flatMap(role -> role.getAuthorities().stream())
                    .distinct()
                    .sorted(Comparator.comparing(AuthorityEO::getGroupName))
                    .collect(Collectors.toList());
    }


    @Transient
    public Boolean hasRole(Long roleId){
        return this.roles.stream().anyMatch(role -> Objects.equals(role.getId(), roleId));
    }

    @Transient
    public UserPublicDTO toPublicDto(){
        UserPublicDTO dto = new UserPublicDTO();
        try{
            CopyUtil.copy(this, dto);
            dto.setApprovalLevel(approvalLevel.getLevel());
            dto.setMinApprovalLevel(approvalLevel.getMinLevel());
            dto.setMaxApprovalLevel(approvalLevel.getMaxLevel());
        }catch (Exception ex){}
        return dto;
    }

    @Transient
    public UserDTO toPrivateDto(){
        UserDTO dto = new UserDTO();
        try{
            CopyUtil.copy(this, dto);
        }catch (Exception ex){}
        return dto;
    }

    @Transient
    public String getFullName() {
        return firstname+" "+lastname;
    }


}