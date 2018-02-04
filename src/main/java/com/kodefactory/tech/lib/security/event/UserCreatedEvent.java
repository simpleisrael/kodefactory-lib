package com.kodefactory.tech.lib.security.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class UserCreatedEvent<User> extends BaseEvent {

    public UserCreatedEvent(User user){
        super(user);
    }
}
