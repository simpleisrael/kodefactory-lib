package com.kodefactory.tech.lib.exception;

import com.kodefactory.tech.lib.exception.error.ApiError;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RestException extends Exception {
    private ApiError apiError;

    public RestException(ApiError apiError) {
        super(apiError.getMessage());
        this.apiError = apiError;
        this.apiError.setDebugMessage(this.getMessage());
    }
}
