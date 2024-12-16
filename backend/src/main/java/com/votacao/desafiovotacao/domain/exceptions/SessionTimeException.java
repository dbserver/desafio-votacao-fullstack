package com.votacao.desafiovotacao.domain.exceptions;

public class SessionTimeException extends Exception {
    public SessionTimeException() {
        super("Session time isn't correct");
    }
}
