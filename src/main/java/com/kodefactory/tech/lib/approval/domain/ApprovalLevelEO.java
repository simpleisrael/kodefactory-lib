package com.kodefactory.tech.lib.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.beans.Transient;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "approval_level")
public class ApprovalLevelEO extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Integer level = 0;
    Integer minLevel = 0;
    Integer maxLevel = 0;

    @JsonIgnore
    @Transient
    public boolean isInRange(Integer level){
        return (minLevel <= level && level <= maxLevel);
    }

}
