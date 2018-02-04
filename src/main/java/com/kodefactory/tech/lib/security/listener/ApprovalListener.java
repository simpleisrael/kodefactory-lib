package com.kodefactory.tech.lib.security.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodefactory.tech.lib.approval.domain.ApprovalRequestEO;
import com.kodefactory.tech.lib.approval.event.ApprovalEvent;
import com.kodefactory.tech.lib.approval.repository.ApprovalRequestRepository;
import com.kodefactory.tech.lib.configuration.domain.ConfigEO;
import com.kodefactory.tech.lib.configuration.repository.ConfigurationRepository;
import com.kodefactory.tech.lib.security.domain.RoleEO;
import com.kodefactory.tech.lib.security.domain.AuthorityEO;
import com.kodefactory.tech.lib.security.repository.AuthorityRepository;
import com.kodefactory.tech.lib.security.repository.RoleRepository;
import com.kodefactory.tech.lib.security.service.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApprovalListener {

    private RoleRepository roleRepository;
    private AuthorityRepository authorityRepository;
    private ConfigurationRepository configurationRepository;
    private AuthenticatedUser authenticatedUser;
    private ApprovalRequestRepository approvalRequestRepository;

    public ApprovalListener(RoleRepository roleRepository,
                            AuthorityRepository authorityRepository,
                            ConfigurationRepository configurationRepository,
                            AuthenticatedUser authenticatedUser,
                            ApprovalRequestRepository approvalRequestRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.configurationRepository = configurationRepository;
        this.authorityRepository = authorityRepository;
        this.approvalRequestRepository = approvalRequestRepository;
    }

    @EventListener(condition = "#approvalRequestEvent.entityType == \"RoleEO\"")
    public void handleRoleApproved(ApprovalEvent approvalRequestEvent){
        System.out.println("Approving Role");
        ApprovalRequestEO requestEO = (ApprovalRequestEO)approvalRequestEvent.getPayload();
        try {
            RoleEO role = new ObjectMapper().readValue(requestEO.getPayload(), RoleEO.class);
            roleRepository.save(role);
            updateApprovalRequest(requestEO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventListener(condition = "#approvalRequestEvent.entityType == \"AuthorityEO\"")
    public void handleAuthorityApproved(ApprovalEvent approvalRequestEvent){
        ApprovalRequestEO requestEO = (ApprovalRequestEO)approvalRequestEvent.getPayload();
        try {
            AuthorityEO authority = new ObjectMapper().readValue(requestEO.getPayload(), AuthorityEO.class);
            authorityRepository.save(authority);
            updateApprovalRequest(requestEO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventListener(condition = "#approvalRequestEvent.entityType == \"ConfigEO\"")
    public void handleConfigApproved(ApprovalEvent approvalRequestEvent){
        ApprovalRequestEO requestEO = (ApprovalRequestEO)approvalRequestEvent.getPayload();
        try {
            ConfigEO config = new ObjectMapper().readValue(requestEO.getPayload(), ConfigEO.class);
            configurationRepository.save(config);
            updateApprovalRequest(requestEO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void updateApprovalRequest(ApprovalRequestEO approvalRequestEO){
//        approvalRequestEO.setApprovedBy(authenticatedUser.getAuthenticatedUserEO());
//        approvalRequestRepository.save(approvalRequestEO);
    }


}
