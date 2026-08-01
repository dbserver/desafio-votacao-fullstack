package br.com.jardelkuhn.backend.agenda.exception;

public class AlreadyClosedException extends AgendaException {
    public AlreadyClosedException() {
        super("A votação da pauta já foi encerrada");
    }
}
