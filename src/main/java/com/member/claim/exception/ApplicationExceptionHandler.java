package com.member.claim.exception;

import com.member.claim.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidClaimException.class})
    public ResponseEntity<ErrorResponse> handleException(InvalidClaimException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        Optional.of(exception.getValidationMsgs()).ifPresent(t -> {
            String errorMsg = t.stream().collect(Collectors.joining(","));
            errorResponse.setErrorMessage(errorMsg);
        });
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ApplicationBaseException.class})
    public ResponseEntity<Map<String, String>> handleException(ApplicationBaseException exception) {
        return ResponseEntity.status(exception.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("reason", exception.getMessage()));
    }
}