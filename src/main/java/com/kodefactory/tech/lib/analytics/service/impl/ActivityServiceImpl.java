package com.kodefactory.tech.lib.analytics.service.impl;

import com.kodefactory.tech.lib.analytics.domain.ActivityEO;
import com.kodefactory.tech.lib.analytics.dto.ActivityRequestDTO;
import com.kodefactory.tech.lib.analytics.repository.ActivityRepository;
import com.kodefactory.tech.lib.analytics.service.ActivityService;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.exception.RestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl extends BaseServiceImpl implements ActivityService {
    private ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<ActivityEO> listActivityByDuration(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO,"startDate", "endDate");
        return activityRepository.findByCreateDateBetween(activityRequestDTO.getStartDate(),
                activityRequestDTO.getEndDate(), new PageRequest(activityRequestDTO.getPage(), 50));
    }

    @Override
    public List<ActivityEO> listActivityByEmail(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "email");
        return activityRepository.findByEmail(activityRequestDTO.getEmail(), new PageRequest(activityRequestDTO.getPage(), 50));
    }

    @Override
    public List<ActivityEO> listActivityByUserId(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "userId");
        return activityRepository.findByUserId(activityRequestDTO.getUserId(), new PageRequest(activityRequestDTO.getPage(), 50));
    }

    @Override
    public List<ActivityEO> listActivityByUrl(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "url");
        return activityRepository.findByUrl(activityRequestDTO.getUrl(), new PageRequest(activityRequestDTO.getPage(), 50));
    }

    @Override
    public List<ActivityEO> listActivityByIpAddress(ActivityRequestDTO activityRequestDTO) throws RestException {
        validateIsRequired(activityRequestDTO, "ipAddress");
        return activityRepository.findByIpAddress(activityRequestDTO.getIpAddress(), new PageRequest(activityRequestDTO.getPage(), 50));
    }
}
