package com.votacao.backend.infrastructure.exception;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException() {
        super("CPF não encontrado.");
    }
}
