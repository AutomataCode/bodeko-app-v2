package com.reydiazz.bodeko.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handlerBusiness(BusinessException exception) {
        ErrorCode error = exception.getError();
        HttpStatus status = error.getHttpStatus();
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code(error.getCode())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult().getFieldErrors().stream().map(
                error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage())).toList();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("VALIDATION_ERROR")
                .message("One or more fields are invalid.")
                .fieldErrors(fieldErrors)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("INVALID_REQUEST")
                .message("The request body contains invalid values for one or more fields.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("METHOD_NOT_ALLOWED")
                .message("The HTTP method '%s' is not supported for this route.".formatted(exception.getMethod()))
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("RESOURCE_NOT_FOUND")
                .message("The requested route does not exist.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        log.warn("Data integrity violation: {}", exception.getMostSpecificCause().getMessage());
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("DATA_INTEGRITY_VIOLATION")
                .message("The operation could not be executed due to database integrity constraints.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unexpected error", exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("BAD_CREDENTIALS")
                .message("Email or password incorrect.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException exception) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("ACCOUNT_DISABLED")
                .message("Access is denied because your account is disabled.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("AUTHENTICATION_REQUIRED")
                .message("Authentication credentials were not found.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("INVALID_PARAMETER_TYPE")
                .message("The data type of a provided parameter is invalid.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .code("ACCESS_DENIED")
                .message("You do not have permission to perform this operation.")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}