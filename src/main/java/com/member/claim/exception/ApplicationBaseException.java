package com.member.claim.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationBaseException extends RuntimeException {
    protected final HttpStatus status;
    protected ApplicationBaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
