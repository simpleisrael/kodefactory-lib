package com.kodefactory.tech.lib.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ResponseMessage extends BaseDTO {
    private String message;
}
