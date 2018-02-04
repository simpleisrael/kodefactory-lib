package com.kodefactory.tech.lib.approval.repository;

import com.kodefactory.tech.lib.approval.domain.ApprovalResponseEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalResponseRepository extends JpaRepository<ApprovalResponseEO, Long> {
}
