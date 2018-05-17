package com.kodefactory.tech.lib.analytics.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityRequestDTO {
    private Date startDate;
    private Date endDate;
    private Long userId;
    private String fullName;
    private String email;
    private String url;
    private String ipAddress;
    private Integer page = 1;
}
