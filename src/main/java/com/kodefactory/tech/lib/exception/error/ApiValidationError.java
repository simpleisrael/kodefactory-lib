package com.kodefactory.tech.lib.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationError(String object, String field, String message) {
        this.field = field;
        this.object = object;
        this.message = message;
    }

    public ApiValidationError(String object, String field, String message, Object rejectedValue) {
        this(object, field, message);
        this.rejectedValue = rejectedValue;
    }
}
