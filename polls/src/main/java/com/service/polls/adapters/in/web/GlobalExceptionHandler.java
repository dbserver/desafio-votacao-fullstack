package com.service.polls.adapters.in.web;

import com.service.polls.domain.exceptions.BusinessException;
import com.service.polls.domain.exceptions.ErrorDTO;
import com.service.polls.domain.exceptions.VotingClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleDomain(
            BusinessException ex
    ) {
        return ResponseEntity.status(ex.getCode())
                .body(new ErrorDTO(
                        ex.getCode().name(),
                        ex.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(VotingClosedException.class)
    public ResponseEntity<ErrorDTO> handleDomain(
            VotingClosedException ex
    ) {
        return ResponseEntity.status(ex.getCode())
                .body(new ErrorDTO(
                        ex.getCode().name(),
                        ex.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleJsonParse(
            HttpMessageNotReadableException ex
    ) {

        String message = "Invalid request payload";

        if (ex.getCause() instanceof InvalidFormatException ife) {
            if (ife.getTargetType().isEnum()) {
                message = "Invalid value for enum " + ife.getTargetType().getSimpleName();
            }
        }

        return ResponseEntity.badRequest()
                .body(new ErrorDTO(
                        "INVALID_VALUE",
                        message,
                        Instant.now()
                ));
    }

}
