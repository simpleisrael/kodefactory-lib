package com.kodefactory.tech.lib.approval.dto;

import lombok.Data;

@Data
public class ApprovalResponseDTO {
    private String approvalStatus;
    private String remark;
    private Long approvalRequestId;
}
