package com.votacao.desafiovotacao.domain.exceptions;

public class NoVoteException extends RuntimeException {
    public NoVoteException() {
        super("Requirement for voting not attended.");
    }
}
