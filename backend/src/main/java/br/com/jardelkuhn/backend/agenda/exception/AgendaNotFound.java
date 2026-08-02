package br.com.jardelkuhn.backend.agenda.exception;

public class AgendaNotFound extends AgendaException {
    public AgendaNotFound(Long id) {
        super("Pauta com id " + id + " não encontrada");
    }
}
