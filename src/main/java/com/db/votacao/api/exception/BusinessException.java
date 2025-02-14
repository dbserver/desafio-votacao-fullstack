package com.db.votacao.api.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends VotingException {
    public BusinessException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}