package br.com.jardelkuhn.backend.dto;

public class DuplicatedVoteException extends AgendaRuleException {
    public DuplicatedVoteException() {
        super("O associado já realizou o seu voto nesta pauta");
    }
}
