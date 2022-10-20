package com.member.claim.exception;

import org.springframework.http.HttpStatus;

public class ClaimBatchJobException extends ApplicationBaseException {
    public ClaimBatchJobException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
