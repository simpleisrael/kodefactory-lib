package com.kodefactory.tech.lib.approval.service;

import com.kodefactory.tech.lib.approval.domain.ApprovalRequestEO;
import com.kodefactory.tech.lib.approval.dto.ApprovalRequestDTO;
import com.kodefactory.tech.lib.approval.dto.ApprovalResponseDTO;
import com.kodefactory.tech.lib.exception.RestException;

import java.util.List;

public interface ApprovalService {
    boolean sendForApproval(Object entity, Integer min, Integer max, String title) throws RestException;
    boolean approve(ApprovalResponseDTO approvalResponseDTO) throws RestException;
    boolean reject(ApprovalResponseDTO approvalResponseDTO) throws RestException;
    boolean pend(ApprovalResponseDTO approvalResponseDTO) throws RestException;
    boolean massApprove(Long[] ids) throws RestException;
    List<ApprovalRequestEO> getApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException;
    List<ApprovalRequestEO> getUserApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException;
    List<ApprovalRequestEO> getUserPendingApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException;
    List<ApprovalRequestEO> getUserCompletedApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException;
    Long unreadRequest();
}
