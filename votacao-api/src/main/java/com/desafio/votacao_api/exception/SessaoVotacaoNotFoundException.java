package com.desafio.votacao_api.exception;

/**
 * Exception thrown when a voting session is not found.
 */
public class SessaoVotacaoNotFoundException extends RuntimeException {

    /**
     * Creates an exception with the provided message.
     *
     * @param message exception message describing the missing voting session
     */
    public SessaoVotacaoNotFoundException(String message) {
        super(message);
    }
}

