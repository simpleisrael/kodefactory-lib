package com.kodefactory.tech.lib.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsEventPayload {
    private String url;
    private String ipAddress;
    private String email;
    private String fullName;
    private String token;

    public AnalyticsEventPayload(String url, String ipAddress, String token) {
        this.url = url;
        this.ipAddress = ipAddress;
        this.token = token;
    }
}
