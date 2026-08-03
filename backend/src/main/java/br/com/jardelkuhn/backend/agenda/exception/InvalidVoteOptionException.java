package br.com.jardelkuhn.backend.agenda.exception;

public class InvalidVoteOptionException extends AgendaException {
    public InvalidVoteOptionException() {
        super("Opção de voto inválido");
    }
}
