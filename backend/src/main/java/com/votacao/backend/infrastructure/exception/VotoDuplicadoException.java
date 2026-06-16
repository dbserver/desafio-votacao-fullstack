package com.votacao.backend.infrastructure.exception;

import java.util.UUID;

public class VotoDuplicadoException extends RuntimeException {
    public VotoDuplicadoException() {
        super("Voto duplicado");
    }

    public VotoDuplicadoException(UUID id) {
        super("Voto duplicado: " + id);
    }
}
