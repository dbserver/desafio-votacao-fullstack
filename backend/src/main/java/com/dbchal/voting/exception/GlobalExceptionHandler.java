package com.dbchal.voting.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PautaNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> trataPautaNotFound(PautaNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "PAUTA_NOT_FOUND",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(AlreadyExistingSessionException.class)
	public ResponseEntity<ApiErrorResponse> trataAlreadyExistingSession(AlreadyExistingSessionException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "ALREADY_EXISTING_SESSION",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(SessaoNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> trataAlreadyExistingSession(SessaoNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "SESSAO_NOT_FOUND",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}
}
