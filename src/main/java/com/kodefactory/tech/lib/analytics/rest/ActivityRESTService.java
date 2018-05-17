package com.kodefactory.tech.lib.analytics.rest;

import com.kodefactory.micronova.dto.ReportRequestDTO;
import com.kodefactory.tech.lib.analytics.dto.ActivityRequestDTO;
import com.kodefactory.tech.lib.analytics.service.ActivityService;
import com.kodefactory.tech.lib.core.rest.BaseREST;
import com.kodefactory.tech.lib.exception.RestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityRESTService extends BaseREST {
    private ActivityService activityService;

    public ActivityRESTService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("list-recent-activities-by-url")
    @PreAuthorize("hasAnyRole('ANALYTICS') and hasRole('ANALYTICS_READ') and hasRole('ANALYTICS_RECENT_ACTIVITIES')")
    public ResponseEntity<Object> listRecentActivitiesByUrl(@RequestBody ActivityRequestDTO activityRequestDTO) throws RestException {
        return buildSuccessResponse(activityService.listActivityByUrl(activityRequestDTO));
    }

    @PostMapping("list-recent-activities-by-email")
    @PreAuthorize("hasAnyRole('ANALYTICS') and hasRole('ANALYTICS_READ') and hasRole('ANALYTICS_RECENT_ACTIVITIES')")
    public ResponseEntity<Object> listRecentActivitiesByEmail(@RequestBody ActivityRequestDTO activityRequestDTO) throws RestException {
        return buildSuccessResponse(activityService.listActivityByEmail(activityRequestDTO));
    }

    @PostMapping("list-recent-activities-by-ipaddress")
    @PreAuthorize("hasAnyRole('ANALYTICS') and hasRole('ANALYTICS_READ') and hasRole('ANALYTICS_RECENT_ACTIVITIES')")
    public ResponseEntity<Object> listActivityByIpAddress(@RequestBody ActivityRequestDTO activityRequestDTO) throws RestException {
        return buildSuccessResponse(activityService.listActivityByIpAddress(activityRequestDTO));
    }

    @PostMapping("list-recent-activities-by-userid")
    @PreAuthorize("hasAnyRole('ANALYTICS') and hasRole('ANALYTICS_READ') and hasRole('ANALYTICS_RECENT_ACTIVITIES')")
    public ResponseEntity<Object> listActivityByFullName(@RequestBody ActivityRequestDTO activityRequestDTO) throws RestException {
        return buildSuccessResponse(activityService.listActivityByUserId(activityRequestDTO));
    }

}
