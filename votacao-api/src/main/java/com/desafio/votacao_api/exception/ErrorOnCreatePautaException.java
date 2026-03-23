package com.desafio.votacao_api.exception;

public class ErrorOnCreatePautaException extends RuntimeException {
    public ErrorOnCreatePautaException(String message) {
        super(message);
    }
}
