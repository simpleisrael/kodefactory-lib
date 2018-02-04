package com.kodefactory.tech.lib.storage.domain;

import com.kodefactory.tech.lib.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "file")
@Data
@EqualsAndHashCode(callSuper = false)
public class FileEO extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    @Size(min = 255)
    private String url;

    @Column(name = "type")
    private String type;

    @Column(name = "is_temp")
    private Boolean isTemp;

    @Column(name = "reference_id")
    private Long referenceId;
}
