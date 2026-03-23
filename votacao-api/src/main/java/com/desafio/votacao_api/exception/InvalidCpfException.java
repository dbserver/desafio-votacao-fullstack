package com.desafio.votacao_api.exception;

/**
 * Exception thrown when a CPF value is invalid.
 */
public class InvalidCpfException extends RuntimeException {

    /**
     * Creates an exception with the provided validation message.
     *
     * @param message exception message describing the validation failure
     */
    public InvalidCpfException(String message) {
        super(message);
    }
}
