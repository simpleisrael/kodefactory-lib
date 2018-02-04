package com.kodefactory.tech.lib.security.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class UserLoggedInEvent<User> extends BaseEvent {
    public UserLoggedInEvent(User user){
        super(user);
    }
}
