package br.com.jardelkuhn.backend.agenda.exception;

public class AlreadyOpenedException extends AgendaException {
    public AlreadyOpenedException() {
        super("A pauta já foi aberta");
    }
}
