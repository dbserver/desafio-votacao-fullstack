package com.votacao.backend.infrastructure.exception;

public class CpfNaoAutorizadoException extends RuntimeException {
    public CpfNaoAutorizadoException() {
        super("Usuário não autorizado para votar.");
    }
}
