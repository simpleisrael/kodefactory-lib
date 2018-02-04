package com.kodefactory.tech.lib.websocket.service;

public interface RealTimeService {

    void publish(String type, Object payload);
}
