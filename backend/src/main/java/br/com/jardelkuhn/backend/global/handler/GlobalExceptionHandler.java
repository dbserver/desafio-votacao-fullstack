package br.com.jardelkuhn.backend.global.handler;

import br.com.jardelkuhn.backend.global.dto.ServerErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> threat500(Exception e) {
        ServerErrorDTO serverErrorDTO = LoggingHandler.getInstance().handleException(e);

        return ResponseEntity.internalServerError().body(serverErrorDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> threat404(EntityNotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> threat400(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEnum(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String field = ife.getPath().getFirst().getPropertyName();

            String allowedValues = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            errors.put(field, "O campo " + field + " deve ser um dos seguintes: " + allowedValues);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}