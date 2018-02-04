package com.kodefactory.tech.lib.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {

    private HttpStatus status;
    private int statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;
    private String path;

    private ApiError() {
        this.status = HttpStatus.BAD_REQUEST;
        this.statusCode = this.status.value();
        timestamp = LocalDateTime.now();
    }

    public ApiError(String message) {
        this();
        this.message = message;
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
        this.statusCode = this.status.value();
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, ex);
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, Throwable ex, String path) {
        this(status, message, ex);
        this.path = path;
    }

    public ApiError(Throwable ex) {
        this();
        this.debugMessage = ex.getLocalizedMessage();
    }
}
