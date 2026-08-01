package br.com.jardelkuhn.backend.dto;

public class VotingClosedException extends AgendaRuleException {
    public VotingClosedException() {
        super("A votação da pauta já foi encerrada");
    }
}
