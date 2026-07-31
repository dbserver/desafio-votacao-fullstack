package br.com.jardelkuhn.backend.dto;

public class AgendaDurationInvalidException extends AgendaSessionRuleException {
    public AgendaDurationInvalidException() {
        super("Duração inválida para abertura da sessão");
    }
}
