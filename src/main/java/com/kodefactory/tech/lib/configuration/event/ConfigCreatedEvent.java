package com.kodefactory.tech.lib.configuration.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class ConfigCreatedEvent<T> extends BaseEvent {
    public ConfigCreatedEvent(T t) {
        super(t);
    }
}
