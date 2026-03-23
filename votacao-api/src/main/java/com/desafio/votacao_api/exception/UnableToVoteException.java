package com.desafio.votacao_api.exception;

public class UnableToVoteException extends RuntimeException {

    public UnableToVoteException(String message) {
        super(message);
    }
}

