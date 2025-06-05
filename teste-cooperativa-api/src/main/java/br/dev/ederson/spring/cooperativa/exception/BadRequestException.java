package br.dev.ederson.spring.cooperativa.exception;

import java.io.Serial;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }

}
