package com.kodefactory.tech.lib.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import com.kodefactory.tech.lib.core.util.CopyUtil;
import com.kodefactory.tech.lib.security.dto.AuthorityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "AUTHORITY")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"roles"})
@ToString(exclude = {"roles"})
public class AuthorityEO extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 50)
    @NotNull
    private String name;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "DESCRIPTION", length = 500)
    @NotNull
    private String description;


    private String groupName;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<RoleEO> roles;

    @Transient
    AuthorityDTO toDTO(){
        AuthorityDTO authorityDTO = new AuthorityDTO();
        try{
            CopyUtil.copy(this, authorityDTO);
        }catch (Exception ex){return authorityDTO;}
        return authorityDTO;
    }
}