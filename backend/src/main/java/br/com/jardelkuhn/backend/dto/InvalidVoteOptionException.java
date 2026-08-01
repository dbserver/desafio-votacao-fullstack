package br.com.jardelkuhn.backend.dto;

public class InvalidVoteOptionException extends AgendaRuleException {
    public InvalidVoteOptionException() {
        super("Opção de voto inválido");
    }
}
