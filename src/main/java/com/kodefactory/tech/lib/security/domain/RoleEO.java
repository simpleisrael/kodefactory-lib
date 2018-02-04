package com.kodefactory.tech.lib.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "role_")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"authorities"})
@ToString(exclude = {"authorities"})
public class RoleEO extends BaseEntity{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name="deleted")
    private Boolean deleted = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "ROLE_AUTHORITY",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    @OrderBy("groupName ASC")
    private List<AuthorityEO> authorities = new ArrayList<>();

    @Transient
    @JsonIgnore
    public Boolean hasAuthority(Long authorityId){
        return this.authorities.stream().anyMatch(authority -> Objects.equals(authority.getId(), authorityId));
    }
}
