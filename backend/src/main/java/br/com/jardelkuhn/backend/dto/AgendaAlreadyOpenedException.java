package br.com.jardelkuhn.backend.dto;

public class AgendaAlreadyOpenedException extends AgendaSessionRuleException {
    public AgendaAlreadyOpenedException() {
        super("A agenda já foi aberta");
    }
}
