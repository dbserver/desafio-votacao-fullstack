package com.db.desafiotecnico_fullstack.exceptions;

public class SessaoFechadaException extends RuntimeException {
    public SessaoFechadaException(String message) {
        super(message);
    }
}