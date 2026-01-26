package com.service.polls.domain.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {

    public NotFoundException(String value) {
        super(HttpStatus.NOT_FOUND, String.format("Não encontrado: %s", value));
    }
}
