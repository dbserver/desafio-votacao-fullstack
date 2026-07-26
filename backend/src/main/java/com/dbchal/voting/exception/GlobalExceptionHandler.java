package com.dbchal.voting.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dbchal.voting.exception.associado.AssociadoNotFoundException;
import com.dbchal.voting.exception.associado.CpfAlreadyRegisteredException;
import com.dbchal.voting.exception.pauta.PautaNotFoundException;
import com.dbchal.voting.exception.sessao.AlreadyExistingSessaoException;
import com.dbchal.voting.exception.sessao.ExpiredSessaoException;
import com.dbchal.voting.exception.sessao.SessaoNotFoundException;
import com.dbchal.voting.exception.voto.DuplicateVoteException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> trataValidationException(MethodArgumentNotValidException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).distinct()
				.collect(Collectors.joining("; "));

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "VALIDATION_ERROR", message,
				request.getRequestURI());

		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> trataHttpMessageNotReadable(HttpMessageNotReadableException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "INVALID_REQUEST_BODY",
				"O corpo da requisição está inválido ou possui valores não reconhecidos", request.getRequestURI());

		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(CreateEntityException.class)
	public ResponseEntity<ApiErrorResponse> trataCreateEntityException(CreateEntityException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "CREATE_ENTITY_EXCEPTION",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	// Pautas
	@ExceptionHandler(PautaNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> trataPautaNotFound(PautaNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "PAUTA_NOT_FOUND",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	// Sessão Votação
	@ExceptionHandler(AlreadyExistingSessaoException.class)
	public ResponseEntity<ApiErrorResponse> trataAlreadyExistingSessao(AlreadyExistingSessaoException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "ALREADY_EXISTING_SESSION",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(SessaoNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> trataSessaoNotFound(SessaoNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "SESSAO_NOT_FOUND",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(ExpiredSessaoException.class)
	public ResponseEntity<ApiErrorResponse> trataExpiredSessaoException(ExpiredSessaoException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.FORBIDDEN;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "EXPIRED_SESSION",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	// Associados
	@ExceptionHandler(AssociadoNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> trataAssociadoNotFound(AssociadoNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "ASSOCIADO_NOT_FOUND",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(CpfAlreadyRegisteredException.class)
	public ResponseEntity<ApiErrorResponse> trataCpfCadastradoException(CpfAlreadyRegisteredException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "CPF_ALREADY_REGISTERED",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	// Votos
	@ExceptionHandler(DuplicateVoteException.class)
	public ResponseEntity<ApiErrorResponse> trataDuplicateVoteException(DuplicateVoteException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;

		ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), "DUPLICATED_VOTE",
				exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}
}
