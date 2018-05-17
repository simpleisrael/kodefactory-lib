package com.kodefactory.tech.lib.analytics.service.impl;

import com.kodefactory.tech.lib.analytics.domain.ActivityEO;
import com.kodefactory.tech.lib.analytics.dto.ActivityRequestDTO;
import com.kodefactory.tech.lib.analytics.repository.ActivityRepository;
import com.kodefactory.tech.lib.analytics.service.ActivityService;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.exception.RestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl extends BaseServiceImpl implements ActivityService {
    private ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<ActivityEO> listActivityByEmail(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "email", "startDate", "endDate");
        return null;
    }

    @Override
    public List<ActivityEO> listActivityByUserId(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "userId", "startDate", "endDate");
        return null;
    }

    @Override
    public List<ActivityEO> listActivityByUrl(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "url", "startDate", "endDate");
        return null;
    }

    @Override
    public List<ActivityEO> listActivityByIpAddress(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "ipAddress", "startDate", "endDate");
        return null;
    }
}
