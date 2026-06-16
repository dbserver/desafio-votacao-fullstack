package com.votacao.backend.infrastructure.exception;

import java.util.UUID;

public class VotoNaoEncontradoException extends RuntimeException {
    public VotoNaoEncontradoException(UUID id) {
        super("Voto não encontrado: " + id);
    }
}
