package com.kodefactory.tech.lib.configuration.domain;

import com.kodefactory.tech.lib.audit.Auditable;
import com.kodefactory.tech.lib.configuration.constants.DataType;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="CONFIG_DATA")
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigEO extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="config_key")
    private String configKey;

    @Column(name="data")
    private String data;

    @Column(name="label")
    private String label;

    @Column(name="scope_name")
    private String scopeName;

    @Column(name="scope_id")
    private Long scopeId;

    private Boolean editable;

    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @ElementCollection
    private List<String> options;

    @Transient
    private Object rawData;
}
