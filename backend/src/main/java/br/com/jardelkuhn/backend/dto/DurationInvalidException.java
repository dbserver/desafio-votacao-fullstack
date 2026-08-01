package br.com.jardelkuhn.backend.dto;

public class DurationInvalidException extends AgendaRuleException {
    public DurationInvalidException() {
        super("Duração inválida para abertura da sessão");
    }
}
