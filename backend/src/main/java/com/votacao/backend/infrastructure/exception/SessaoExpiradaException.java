package com.votacao.backend.infrastructure.exception;

import java.util.UUID;

public class SessaoExpiradaException extends RuntimeException {
    public SessaoExpiradaException() {
        super("Sessão encerrada.");
    }
}
