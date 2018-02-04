package com.kodefactory.tech.lib.approval.event;

import com.kodefactory.tech.lib.core.event.BaseEvent;
import lombok.Getter;

public class ApprovalEvent<T> extends BaseEvent<T> {
    @Getter
    private String entityType;
    private T payload;

    public ApprovalEvent(T payload, String entityType) {
        super(payload);
        this.entityType = entityType;
    }
}
