package com.db.desafiotecnico_fullstack.exceptions;

public class CpfValidationException extends RuntimeException {
    public CpfValidationException(String message) {
        super(message);
    }
}