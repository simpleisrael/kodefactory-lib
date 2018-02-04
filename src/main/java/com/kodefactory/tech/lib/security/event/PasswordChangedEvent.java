package com.kodefactory.tech.lib.security.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;

public class PasswordChangedEvent<UserEO> extends BaseEvent<UserEO> {
    public PasswordChangedEvent(UserEO user){
        super(user);
    }
}
