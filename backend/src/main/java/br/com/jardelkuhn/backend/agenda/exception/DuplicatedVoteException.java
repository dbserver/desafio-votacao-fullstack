package br.com.jardelkuhn.backend.agenda.exception;

public class DuplicatedVoteException extends AgendaException {
    public DuplicatedVoteException() {
        super("O associado já realizou o voto nesta pauta");
    }
}
