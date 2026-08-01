package br.com.jardelkuhn.backend.controller;

import br.com.jardelkuhn.backend.dto.AgendaRuleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AgendaValidationExceptionHandler {
    @ExceptionHandler(AgendaRuleException.class)
    public ResponseEntity<Map<String, String>> handleBusinessRuleException(
            AgendaRuleException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("error", ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
