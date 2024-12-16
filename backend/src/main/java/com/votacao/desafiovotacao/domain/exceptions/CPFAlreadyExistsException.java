package com.votacao.desafiovotacao.domain.exceptions;

public class CPFAlreadyExistsException extends Exception {
    public CPFAlreadyExistsException() {
        super("CPF already exists");
    }
}
