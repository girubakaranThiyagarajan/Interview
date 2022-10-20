package com.member.claim.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class InvalidClaimException extends ApplicationBaseException {
    private List<String> validationMsgs;

    public InvalidClaimException(String message, List<String> validationMsgs) {
        super(message, HttpStatus.BAD_REQUEST);
        this.validationMsgs = validationMsgs;
    }

}
