package com.kodefactory.tech.lib.analytics.service;

import com.kodefactory.tech.lib.analytics.domain.ActivityEO;
import com.kodefactory.tech.lib.analytics.dto.ActivityRequestDTO;
import com.kodefactory.tech.lib.exception.RestException;

import java.util.List;

public interface ActivityService {
    List<ActivityEO> listActivityByDuration(ActivityRequestDTO activityRequestDTO) throws RestException;
    List<ActivityEO> listActivityByEmail(ActivityRequestDTO activityRequestDTO) throws RestException;
    List<ActivityEO> listActivityByUserId(ActivityRequestDTO activityRequestDTO) throws RestException;
    List<ActivityEO> listActivityByUrl(ActivityRequestDTO activityRequestDTO) throws RestException;
    List<ActivityEO> listActivityByIpAddress(ActivityRequestDTO activityRequestDTO) throws RestException;
}
