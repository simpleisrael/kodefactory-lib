package com.kodefactory.tech.lib.analytics.listener;

import com.kodefactory.tech.lib.analytics.domain.ActivityEO;
import com.kodefactory.tech.lib.analytics.dto.AnalyticsEventPayload;
import com.kodefactory.tech.lib.analytics.event.AuthenticatedActivityEvent;
import com.kodefactory.tech.lib.analytics.repository.ActivityRepository;
import com.kodefactory.tech.lib.security.domain.UserEO;
import com.kodefactory.tech.lib.security.jwt.JwtTokenUtil;
import com.kodefactory.tech.lib.security.repository.UserRepository;
import com.kodefactory.tech.lib.security.service.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ActivityListener {

    private AuthenticatedUser authenticatedUser;
    private ActivityRepository activityRepository;
    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;


    public ActivityListener(ActivityRepository activityRepository,
                            UserRepository userRepository,
                            JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @EventListener
    public void handleAuthenticatedActivity(AuthenticatedActivityEvent authenticatedActivityEvent) {
        AnalyticsEventPayload eventPayload = (AnalyticsEventPayload)authenticatedActivityEvent.getPayload();

        UserEO user = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(eventPayload.getToken())).orElse(null);

        if(user != null) {
            ActivityEO activityEO = new ActivityEO();
            activityEO.setEmail(user.getEmail());
            activityEO.setFullName(user.getFullName());
            activityEO.setIpAddress(eventPayload.getIpAddress());
            activityEO.setUrl(eventPayload.getUrl());
            activityEO.setUserId(user.getId());
            activityRepository.save(activityEO);
        } else {
            System.out.println("User is currently not available");
        }

    }
}
