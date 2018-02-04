package com.kodefactory.tech.lib.approval.repository;

import com.kodefactory.tech.lib.approval.domain.ApprovalRequestEO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEO, Long> {

    @Query("SELECT a FROM ApprovalRequestEO a WHERE a.maxApprovalLevel BETWEEN :min AND :max ORDER BY a.createDate DESC")
    List<ApprovalRequestEO> findApprovalsInRange(@Param("min") Integer min, @Param("max") Integer max, Pageable pageable);
}
