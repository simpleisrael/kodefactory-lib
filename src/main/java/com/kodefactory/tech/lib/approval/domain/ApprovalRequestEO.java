package com.kodefactory.tech.lib.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import com.kodefactory.tech.lib.security.domain.UserEO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"approvalResponseCollection", "approvedByName"})
@ToString(exclude = {"approvalResponseCollection", "approvedByName"})
@Table(name = "approval_request")
public class ApprovalRequestEO extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @NotNull
    @Column(name = "entity_class_name")
    private String entityClassName;

    @NotNull
    @Size(max = 50000)
    @JsonIgnore
    @Lob
    private String payload;

    private Integer maxApprovalLevel = 0;
    private Integer minApprovalLevel = 0;
    private Integer currentApprovalLevel = 0;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "approval_request_user_id")
    private UserEO requestCreatedByUser;

    @Enumerated(EnumType.ORDINAL)
    private ApprovalStatus approvalStatus;

    private Boolean isRead = false;


    @JsonIgnore
    @OneToMany(mappedBy = "approvalRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApprovalResponseEO> approvalResponseCollection = new ArrayList<>();

    @Access(AccessType.PROPERTY)
    public Long unreadResponseCount() {
        return approvalResponseCollection.stream().filter(ApprovalResponseEO::getIsRead).count();
    }

    @Transient
    private Object entity;

    @Transient
    private String approvedByName;

}
