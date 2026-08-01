package br.com.jardelkuhn.backend.dto;

public class InvalidAssociateException extends AgendaRuleException {
    public InvalidAssociateException() {
        super("O ID do associado informado é inválido");
    }
}
