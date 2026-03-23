package com.desafio.votacao_api.exception;

public class SessionAlreadyExistsForPautaException extends RuntimeException {
    public SessionAlreadyExistsForPautaException(String message) {
        super(message);
    }
}

