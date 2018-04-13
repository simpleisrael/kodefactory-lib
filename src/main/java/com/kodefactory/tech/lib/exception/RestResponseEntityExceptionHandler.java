package com.kodefactory.tech.lib.exception;

import com.kodefactory.tech.lib.exception.error.ApiError;
import io.jsonwebtoken.SignatureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class })
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        apiError.setPath(request.getRequestURI());
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        ex.printStackTrace();
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage(), ex, request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleBadRequest(ConstraintViolationException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex, request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredential(BadCredentialsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex, request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        ex.printStackTrace();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({SignatureException.class})
    protected ResponseEntity<Object> handleSignatureException(SignatureException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex, request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), ex, request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({RestException.class})
    protected ResponseEntity<Object> handleRestException(RestException ex, HttpServletRequest request) {
        ApiError apiError = ex.getApiError();
        apiError.setStatus(BAD_REQUEST);
        apiError = buildApiError(apiError, ex, request);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), ex, request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    private ApiError buildApiError(@NotNull ApiError apiError, Exception ex, HttpServletRequest request){
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return apiError;
    }


}