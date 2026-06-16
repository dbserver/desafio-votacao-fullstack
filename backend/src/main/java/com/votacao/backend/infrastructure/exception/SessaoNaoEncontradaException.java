package com.votacao.backend.infrastructure.exception;

import java.util.UUID;

public class SessaoNaoEncontradaException extends RuntimeException {
    public SessaoNaoEncontradaException(UUID id) {
        super("Sessão não encontrada: " + id);
    }
}
