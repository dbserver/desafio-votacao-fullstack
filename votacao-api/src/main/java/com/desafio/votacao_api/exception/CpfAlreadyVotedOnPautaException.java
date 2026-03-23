package com.desafio.votacao_api.exception;

public class CpfAlreadyVotedOnPautaException extends RuntimeException {
    public CpfAlreadyVotedOnPautaException(String message) {
        super(message);
    }
}

