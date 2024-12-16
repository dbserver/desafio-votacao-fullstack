package com.votacao.desafiovotacao.domain.exceptions;

public class NoSessionToVoteException extends Exception {
    public NoSessionToVoteException() {
        super("No session to vote or it is closed");
    }
}
