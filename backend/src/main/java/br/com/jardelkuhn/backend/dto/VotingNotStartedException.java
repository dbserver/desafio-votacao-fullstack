package br.com.jardelkuhn.backend.dto;

public class VotingNotStartedException extends AgendaRuleException {
    public VotingNotStartedException() {
        super("A pauta ainda não está disponível para votação");
    }
}
