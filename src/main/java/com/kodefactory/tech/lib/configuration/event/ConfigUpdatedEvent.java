package com.kodefactory.tech.lib.configuration.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class ConfigUpdatedEvent<T> extends BaseEvent {
    public ConfigUpdatedEvent(T t) {
        super(t);
    }
}
