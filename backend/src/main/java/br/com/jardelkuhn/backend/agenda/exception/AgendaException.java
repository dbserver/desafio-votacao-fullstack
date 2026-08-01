package br.com.jardelkuhn.backend.agenda.exception;

public abstract class AgendaException extends RuntimeException {
    public AgendaException(String message) {
        super(message);
    }
}
