package com.kodefactory.tech.lib.analytics.repository;

import com.kodefactory.tech.lib.analytics.domain.ActivityEO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEO, Long> {
    List<ActivityEO> findByEmail(String email, Pageable pageable);
    List<ActivityEO> findByUrl(String url, Pageable pageable);
    List<ActivityEO> findByUserId(Long userId, Pageable pageable);
    List<ActivityEO> findByIpAddress(String ipAddress, Pageable pageable);
    List<ActivityEO> findByCreateDateBetween(Date startDate, Date endDate, Pageable pageable);
}
