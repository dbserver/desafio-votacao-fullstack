package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.dto.AgendaSessionRuleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AgendaValidationExceptionHandler {
    @ExceptionHandler(AgendaSessionRuleException.class)
    public ResponseEntity<Map<String, String>> handleBusinessRuleException(
            AgendaSessionRuleException ex) {

        Map<String, String> error = Map.of(
                "message", ex.getMessage()
        );

        // conflito, a requisição provavelmente não deveria ter acontecido
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
