package com.kodefactory.tech.lib.core.event;

import lombok.Data;

@Data
public abstract class BaseEvent<T> {
    protected T payload;
    public BaseEvent(T payload){
        this.payload = payload;
    }
}