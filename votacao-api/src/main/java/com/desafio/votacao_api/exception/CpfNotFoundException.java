package com.desafio.votacao_api.exception;

public class CpfNotFoundException extends RuntimeException {

    public CpfNotFoundException(String message) {
        super(message);
    }
}

