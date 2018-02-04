package com.kodefactory.tech.lib.security.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class UserUpdatedEvent<User> extends BaseEvent {
    public UserUpdatedEvent(User user){
        super(user);
    }
}
