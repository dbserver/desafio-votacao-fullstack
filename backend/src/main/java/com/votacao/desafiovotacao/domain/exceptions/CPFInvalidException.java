package com.votacao.desafiovotacao.domain.exceptions;

public class CPFInvalidException extends Exception {
    public CPFInvalidException() {
        super("CPF valid needed");
    }
}
