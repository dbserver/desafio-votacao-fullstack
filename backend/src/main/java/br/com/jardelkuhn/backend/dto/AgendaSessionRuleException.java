package br.com.jardelkuhn.backend.dto;

public abstract class AgendaSessionRuleException extends RuntimeException {
    public AgendaSessionRuleException(String message) {
        super(message);
    }
}
