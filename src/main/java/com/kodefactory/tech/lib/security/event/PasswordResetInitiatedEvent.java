package com.kodefactory.tech.lib.security.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class PasswordResetInitiatedEvent<User> extends BaseEvent {
    public PasswordResetInitiatedEvent(User user){
        super(user);
    }
}
