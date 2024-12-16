package com.votacao.desafiovotacao.domain.exceptions;

public class AgendaNotFoundException extends Exception {
    public AgendaNotFoundException() {
        super("Agenda couldn't be found");
    }
}
