package com.service.polls.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UserHasAlreadyVotedException extends BusinessException {
    public UserHasAlreadyVotedException() {
        super(HttpStatus.BAD_REQUEST, "Usuário já votou");
    }
}
