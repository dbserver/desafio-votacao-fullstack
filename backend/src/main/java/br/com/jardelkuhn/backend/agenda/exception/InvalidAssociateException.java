package br.com.jardelkuhn.backend.agenda.exception;

public class InvalidAssociateException extends AgendaException {
    public InvalidAssociateException() {
        super("O ID do associado informado é inválido");
    }
}
