package br.com.jardelkuhn.backend.agenda.exception;

public class DurationInvalidException extends AgendaException {
    public DurationInvalidException() {
        super("Duração inválida para abertura da sessão de votação");
    }
}
