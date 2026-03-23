package com.desafio.votacao_api.exception;

/**
 * Exception thrown when attempting to register a vote in a closed voting session.
 */
public class SessionClosedException extends RuntimeException {

    /**
     * Creates an exception with the provided message.
     *
     * @param message exception message describing why the session is closed
     */
    public SessionClosedException(String message) {
        super(message);
    }
}

