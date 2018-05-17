package com.kodefactory.tech.lib.analytics.domain;

import com.kodefactory.tech.lib.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="activity")
public class ActivityEO extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private String ipAddress;
    private String url;
    private Long userId;
}
