package br.com.jardelkuhn.backend.dto;

public class SessionOpenedException extends AgendaRuleException {
    public SessionOpenedException() {
        super("A agenda já foi aberta");
    }
}
