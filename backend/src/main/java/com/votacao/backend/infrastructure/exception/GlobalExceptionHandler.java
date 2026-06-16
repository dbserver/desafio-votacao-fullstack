package com.votacao.backend.infrastructure.exception;

import com.votacao.backend.api.v1.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.votacao.backend.infrastructure.time.AppDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SessaoExpiradaException.class)
    public ResponseEntity<ErrorResponse> handleSessaoExpiradaException(SessaoExpiradaException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, "Sessão encerrada.", ex);
    }

    @ExceptionHandler(VotoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleVotoDuplicadoException(VotoDuplicadoException ex) {
        return error(HttpStatus.CONFLICT, "Você já votou nesta sessão.", ex);
    }

    @ExceptionHandler(VotoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleVotoNaoEncontradoException(VotoNaoEncontradoException ex) {
        return error(HttpStatus.NOT_FOUND, "Voto não encontrado.", ex);
    }

    @ExceptionHandler(PautaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handlePautaNaoEncontradaException(PautaNaoEncontradaException ex) {
        return error(HttpStatus.NOT_FOUND, "Pauta não encontrada.", ex);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return error(HttpStatus.CONFLICT, "Operação inválida para o estado atual.", ex);
    }

    @ExceptionHandler(SessaoJaAbertaException.class)
    public ResponseEntity<ErrorResponse> handleSessaoJaAbertaException(SessaoJaAbertaException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    @ExceptionHandler(SessaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleSessaoNaoEncontradaException(SessaoNaoEncontradaException ex) {
        return error(HttpStatus.NOT_FOUND, "Sessão não encontrada.", ex);
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleCpfInvalidoException(CpfInvalidoException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(CpfNaoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleCpfNaoAutorizadoException(CpfNaoAutorizadoException ex) {
        return error(HttpStatus.FORBIDDEN, ex.getMessage(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Requisição inválida.";
        return error(HttpStatus.BAD_REQUEST, message, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado.", ex);
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String mensagem, Exception ex) {
        if (status.is5xxServerError()) {
            log.error("Erro {}: {}", status.value(), mensagem, ex);
        } else {
            log.warn("Erro {}: {}", status.value(), mensagem);
        }
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), mensagem, AppDateTime.now()));
    }
}
