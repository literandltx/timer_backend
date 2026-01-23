package com.example.timer_backend.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Default Global Exception Handler
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(
            final Exception exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getLocalizedMessage(),
                "Unexpected error occurred."
        );

        log.info("Exception was handled: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(
            final EntityNotFoundException exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                exception.getLocalizedMessage(),
                "Resource not found."
        );

        log.info("Entity not found: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(
            final UsernameNotFoundException exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                exception.getLocalizedMessage(),
                "User not found."
        );

        log.info("Username not found: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(
            final AccessDeniedException exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN,
                exception.getLocalizedMessage(),
                "Access is denied."
        );

        log.info("Access denied: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleBadRequest(
            final HttpMessageNotReadableException exception,
            final WebRequest request
    ) {
        final ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                exception.getLocalizedMessage(),
                "Invalid request format."
        );

        log.info("Deserialization error: {}", exception.getMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
