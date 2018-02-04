package com.kodefactory.tech.lib.core.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class BaseREST {
    public ResponseEntity<Object> buildSuccessResponse(Object response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Boolean notNull(Object object) {
        return Objects.nonNull(object);
    }
}
