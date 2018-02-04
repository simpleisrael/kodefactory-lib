package com.kodefactory.tech.lib.approval.rest;

import com.kodefactory.tech.lib.approval.dto.ApprovalRequestDTO;
import com.kodefactory.tech.lib.approval.dto.ApprovalResponseDTO;
import com.kodefactory.tech.lib.approval.service.ApprovalService;
import com.kodefactory.tech.lib.core.rest.BaseREST;
import com.kodefactory.tech.lib.exception.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("approval-service")
public class ApprovalRESTService extends BaseREST {

    private ApprovalService approvalService;

    public ApprovalRESTService(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("approve")
    @PreAuthorize("hasRole('APPROVAL') AND hasRole('APPROVAL_APPROVE') OR  hasRole('APPROVAL') AND hasRole('APPROVAL_REJECT')")
    public ResponseEntity<Object> approve(@RequestBody ApprovalResponseDTO approvalResponseDTO) throws RestException {
        return buildSuccessResponse(approvalService.approve(approvalResponseDTO));
    }


    @PostMapping("pending-approval")
    @PreAuthorize("hasRole('APPROVAL') AND hasRole('APPROVAL_READ')")
    public ResponseEntity<Object> listPendingRequest(@RequestBody ApprovalRequestDTO approvalRequestDTO) throws RestException{
        return buildSuccessResponse(approvalService.getApprovalRequestWithinRange(approvalRequestDTO));
    }


    @PostMapping("user-pending-approval")
    @PreAuthorize("hasRole('APPROVAL') AND hasRole('APPROVAL_READ')")
    public ResponseEntity<Object> listUserPendingRequest(@RequestBody ApprovalRequestDTO approvalRequestDTO) throws RestException{
        return buildSuccessResponse(approvalService.getUserPendingApprovalRequestWithinRange(approvalRequestDTO));
    }


    @PostMapping("user-completed-approval")
    @PreAuthorize("hasRole('APPROVAL') AND hasRole('APPROVAL_READ')")
    public ResponseEntity<Object> listUserCompletedRequest(@RequestBody ApprovalRequestDTO approvalRequestDTO) throws RestException{
        return buildSuccessResponse(approvalService.getUserCompletedApprovalRequestWithinRange(approvalRequestDTO));
    }

}
