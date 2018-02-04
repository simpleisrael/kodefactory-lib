package com.kodefactory.tech.lib.approval.dto;

import lombok.Data;

@Data
public class ApprovalRequestDTO {
    private Long approvalRequest;
    private Long[] approvalRequestList;
    private Integer minApprovalLevel;
    private Integer maxApprovalLevel;
    private Integer page;
    private Integer count;
    private String approvedByName;
}
