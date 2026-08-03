package br.com.jardelkuhn.backend.agenda.handler;

import br.com.jardelkuhn.backend.agenda.exception.AgendaException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AgendaValidationExceptionHandler {
    @ExceptionHandler(AgendaException.class)
    public ResponseEntity<Map<String, String>> handleAgendaException(
            AgendaException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("error", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
