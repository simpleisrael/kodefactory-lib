package com.kodefactory.tech.lib.approval.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodefactory.tech.lib.approval.domain.ApprovalRequestEO;
import com.kodefactory.tech.lib.approval.domain.ApprovalResponseEO;
import com.kodefactory.tech.lib.approval.domain.ApprovalStatus;
import com.kodefactory.tech.lib.approval.dto.ApprovalRequestDTO;
import com.kodefactory.tech.lib.approval.dto.ApprovalResponseDTO;
import com.kodefactory.tech.lib.approval.event.ApprovalEvent;
import com.kodefactory.tech.lib.approval.repository.ApprovalRequestRepository;
import com.kodefactory.tech.lib.approval.repository.ApprovalResponseRepository;
import com.kodefactory.tech.lib.approval.util.ApprovalUtil;
import com.kodefactory.tech.lib.core.event.EventPublisher;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.exception.error.ApiError;
import com.kodefactory.tech.lib.security.domain.UserEO;
import com.kodefactory.tech.lib.security.repository.UserRepository;
import com.kodefactory.tech.lib.security.service.AuthenticatedUser;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ApprovalServiceImpl extends BaseServiceImpl implements ApprovalService {

    @Autowired
    private UserRepository userRepository;

    private ApprovalRequestRepository approvalRequestRepository;
    private AuthenticatedUser authenticatedUser;
    private EventPublisher eventPublisher;

    public ApprovalServiceImpl(ApprovalRequestRepository approvalRequestRepository,
                               ApprovalResponseRepository approvalResponseRepository,
                               AuthenticatedUser authenticatedUser,
                               EventPublisher eventPublisher){
        this.approvalRequestRepository = approvalRequestRepository;
        this.authenticatedUser = authenticatedUser;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean sendForApproval(Object entity, Integer minApprovalLevel, Integer maxApprovalLevel, String title) throws RestException {
        UserEO user = userRepository.findOne(authenticatedUser.getAuthenticatedUser().getId());
        ApprovalRequestEO approvalRequest = new ApprovalRequestEO();
        approvalRequest.setEntityClassName(ApprovalUtil.getEntityName(entity.getClass()));
        approvalRequest.setApprovalStatus(ApprovalStatus.PENDING);
        approvalRequest.setMinApprovalLevel(minApprovalLevel);
        approvalRequest.setMaxApprovalLevel(maxApprovalLevel);
        approvalRequest.setCurrentApprovalLevel(0);
        approvalRequest.setTitle(title);
        approvalRequest.setRequestCreatedByUser(user);
        approvalRequest.setIsRead(false);

        try {
            approvalRequest.setPayload(new ObjectMapper().writeValueAsString(entity));
            System.out.println(approvalRequest.getPayload());
        } catch (JsonProcessingException e) { throw new RestException(new ApiError(e.getMessage())); }

        approvalRequestRepository.save(approvalRequest);
        return true;
    }


    /**
     * Approve a pending approval request
     * @param approvalResponseDTO
     * @return
     * @throws RestException
     */
    @Override
    public boolean approve(ApprovalResponseDTO approvalResponseDTO) throws RestException{
        System.out.println(approvalResponseDTO);
        validateIsRequired(approvalResponseDTO, "approvalRequestId", "approvalStatus", "remark");

        Long approvalRequestId = approvalResponseDTO.getApprovalRequestId();
        ApprovalRequestEO approvalRequest = approvalRequestRepository.findOne(approvalRequestId);
        validateNotNull(approvalRequest, String.format("Approval Request with id %d does not exist", approvalRequestId));

        UserEO user = userRepository.findOne(authenticatedUser.getAuthenticatedUser().getId());

        if(approvalRequest.getApprovalStatus() == (ApprovalStatus.APPROVED)){
            throwException("This Approval Request have already been approved!");
        }


        if(isEqual(user, approvalRequest.getRequestCreatedByUser())) {
//            TODO throwException("You can not approve a request you created");
        }

       if(isNull(user.getApprovalLevel())){
           throwException("You do not have the approval level to approve this request");
       }

        if(!user.getApprovalLevel().isInRange(approvalRequest.getMinApprovalLevel()) ||
                !user.getApprovalLevel().isInRange(approvalRequest.getMaxApprovalLevel())){
            throwException("Your approval level is not in range of this approval request. Hence you cannot approve this request");
        }

        approvalRequest.setCurrentApprovalLevel(user.getApprovalLevel().getLevel());

        ApprovalResponseEO approvalResponse = new ApprovalResponseEO();
        approvalResponse.setApprovalStatus(ApprovalStatus.valueOf(approvalResponseDTO.getApprovalStatus()));
        approvalResponse.setIsRead(false);
        approvalResponse.setRemark(approvalResponseDTO.getRemark());
        approvalResponse.setApprovalRequest(approvalRequest);

        approvalRequest.getApprovalResponseCollection().add(approvalResponse);

        // At this point, the request have been approved
        if(approvalRequest.getMaxApprovalLevel() <= approvalRequest.getCurrentApprovalLevel() &&
                approvalResponse.getApprovalStatus() == ApprovalStatus.APPROVED){
            approvalRequest.setApprovalStatus(approvalResponse.getApprovalStatus());
            approvalRequest.setApprovalStatus(ApprovalStatus.APPROVED);
            approvalRequestRepository.save(approvalRequest);
            System.out.println("Publishing event...");
            eventPublisher.publish(new ApprovalEvent<>(approvalRequest, approvalRequest.getEntityClassName()));
        }

        return true;
    }

    @Override
    public boolean reject(ApprovalResponseDTO approvalResponse) throws RestException {
        return false;
    }

    @Override
    public boolean pend(ApprovalResponseDTO approvalResponse) throws RestException {
        return false;
    }

    @Override
    public boolean massApprove(@NotNull Long[] ids) throws RestException{
        return false;
    }

    @Override
    public List<ApprovalRequestEO> getApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException {
        validateIsRequired(approvalRequestDTO, "minApprovalLevel", "maxApprovalLevel", "page", "count");
        PageRequest pageable = new PageRequest(approvalRequestDTO.getPage()-1, approvalRequestDTO.getCount());
        return approvalRequestRepository.findApprovalsInRange(approvalRequestDTO.getMinApprovalLevel(),
                approvalRequestDTO.getMaxApprovalLevel(), pageable);
    }

    @Override
    public List<ApprovalRequestEO> getUserApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException {
        UserEO user = authenticatedUser.getAuthenticatedUserEO();
        validateNotNull(user.getApprovalLevel(), "You do not have a valid approval level");
        approvalRequestDTO.setMinApprovalLevel(user.getApprovalLevel().getMinLevel());
        approvalRequestDTO.setMaxApprovalLevel(user.getApprovalLevel().getMaxLevel());
        validateIsRequired(approvalRequestDTO, "page", "count");
        PageRequest pageable = new PageRequest(approvalRequestDTO.getPage()-1, approvalRequestDTO.getCount());
        return approvalRequestRepository.findApprovalsInRange(approvalRequestDTO.getMinApprovalLevel(), approvalRequestDTO.getMaxApprovalLevel(), pageable);
    }

    @Override
    public List<ApprovalRequestEO> getUserPendingApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException {
        List<ApprovalRequestEO> allApprovals = getUserApprovalRequestWithinRange(approvalRequestDTO);
        return allApprovals.stream()
                .filter(approvalRequestEO -> approvalRequestEO.getApprovalStatus() == ApprovalStatus.PENDING)
                .peek(approvalRequestEO -> {
                    approvalRequestEO.setPayload(StringEscapeUtils.unescapeJson(approvalRequestEO.getPayload()));
                })
                .collect(toList());
    }

    @Override
    public List<ApprovalRequestEO> getUserCompletedApprovalRequestWithinRange(ApprovalRequestDTO approvalRequestDTO) throws RestException {
        List<ApprovalRequestEO> allApprovals = getUserApprovalRequestWithinRange(approvalRequestDTO);
        return allApprovals.stream()
                .filter(approvalRequestEO -> approvalRequestEO.getApprovalStatus() != ApprovalStatus.PENDING)
                .peek(approvalRequestEO -> {
                    approvalRequestEO.setEntity(toJsonObject(approvalRequestEO.getPayload(), Object.class));
                })
                .collect(toList());
    }


    @Override
    public Long unreadRequest() {
        return approvalRequestRepository.findAll()
                .stream()
                .filter(approvalRequestEO -> !approvalRequestEO.getIsRead())
                .count();
    }

    public static void main(String[] args){
        String data = "\"id\":2,\"name\":\"User Role55\",\"description\":\"Authenticated User Role\",\"deleted\":false,\"authorities\"";
        System.out.println(StringEscapeUtils.unescapeJava(data));
        System.out.println(data.replace("\\", ""));
    }
}
