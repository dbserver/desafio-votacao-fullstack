package com.desafio.votacao_api.exception;

/**
 * Exception thrown when a vote is not found.
 */
public class VotoNotFoundException extends RuntimeException {

    /**
     * Creates an exception with the provided message.
     *
     * @param message exception message describing the missing vote
     */
    public VotoNotFoundException(String message) {
        super(message);
    }
}

