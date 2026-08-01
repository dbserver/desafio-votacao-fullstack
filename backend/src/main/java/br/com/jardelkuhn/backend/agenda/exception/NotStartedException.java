package br.com.jardelkuhn.backend.agenda.exception;

public class NotStartedException extends AgendaException {
    public NotStartedException() {
        super("A pauta ainda não está disponível para votação");
    }
}
