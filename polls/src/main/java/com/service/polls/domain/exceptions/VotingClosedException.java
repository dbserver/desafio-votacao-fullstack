package com.service.polls.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VotingClosedException extends BusinessException {
    public VotingClosedException() {
        super(HttpStatus.BAD_REQUEST, "Votação encerrada");
    }
}
