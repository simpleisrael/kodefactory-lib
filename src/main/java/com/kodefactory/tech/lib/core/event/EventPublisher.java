package com.kodefactory.tech.lib.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(final BaseEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
