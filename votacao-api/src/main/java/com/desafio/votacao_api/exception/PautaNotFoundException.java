package com.desafio.votacao_api.exception;

public class PautaNotFoundException extends RuntimeException {
    public PautaNotFoundException(String message) {
        super(message);
    }
}
