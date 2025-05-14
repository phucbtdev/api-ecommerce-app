package com.ecommerce_app.exception;

import com.ecommerce_app.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        log.error("Exception: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex, WebRequest request) {
        log.error("Exception: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        log.error("Exception: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        log.error("Exception: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        log.error("Exception: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message("Access Denied: You don't have permission to access this resource")
                .details(request.getDescription(false))
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error("Access Denied", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // ===== Global Exception (with Swagger path check) =====

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Exception: ", ex);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        // Bỏ qua xử lý nếu là Swagger request
        if (path.startsWith("/api-docs") || path.startsWith("/swagger-ui")) {
            throw new RuntimeException(ex);
        }

        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.error("Exception: ", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.error("Validation Failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===== Helper =====

    private ResponseEntity<ApiResponse<ErrorDetails>> buildErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        log.error("Exception: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .status(status.value())
                .error(status.getReasonPhrase())
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);
        return new ResponseEntity<>(response, status);
    }
}