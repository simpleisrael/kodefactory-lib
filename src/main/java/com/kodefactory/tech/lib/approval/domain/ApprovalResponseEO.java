package com.kodefactory.tech.lib.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodefactory.tech.lib.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "approval_response")
public class ApprovalResponseEO extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private ApprovalStatus approvalStatus;

    @Size(max = 10000)
    private String remark;

    private Boolean isRead = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "approval_request_id", referencedColumnName = "id")
    private ApprovalRequestEO approvalRequest;
}
