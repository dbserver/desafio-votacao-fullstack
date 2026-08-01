package br.com.jardelkuhn.backend.dto;

public abstract class AgendaRuleException extends RuntimeException {
    public AgendaRuleException(String message) {
        super(message);
    }
}
