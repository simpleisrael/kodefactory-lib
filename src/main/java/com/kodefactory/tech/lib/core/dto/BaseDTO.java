package com.kodefactory.tech.lib.core.dto;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseDTO {
    private Date createDate;
    private Date lastModifyDate;
}
