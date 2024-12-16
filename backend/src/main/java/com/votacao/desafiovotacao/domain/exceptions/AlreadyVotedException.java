package com.votacao.desafiovotacao.domain.exceptions;

public class AlreadyVotedException extends Exception {
    public AlreadyVotedException() {
        super("Associated has already vote in this session");
    }
}
