package com.db.votacao.api.exception;

import org.springframework.http.HttpStatus;

public class VotingException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public VotingException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}