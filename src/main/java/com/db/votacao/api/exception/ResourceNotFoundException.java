package com.db.votacao.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends VotingException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
