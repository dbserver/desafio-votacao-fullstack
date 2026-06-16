package com.votacao.backend.infrastructure.exception;

public class SessaoJaAbertaException extends RuntimeException {
    public SessaoJaAbertaException(String message) {
        super(message);
    }
}
