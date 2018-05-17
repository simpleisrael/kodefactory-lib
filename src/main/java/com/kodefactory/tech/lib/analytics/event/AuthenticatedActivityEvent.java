package com.kodefactory.tech.lib.analytics.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;
import lombok.Data;

@Data
public class AuthenticatedActivityEvent<AnalyticsEventPayload> extends BaseEvent<AnalyticsEventPayload> {
    public AuthenticatedActivityEvent(AnalyticsEventPayload analyticsEventPayload) {
        super(analyticsEventPayload);
    }
}