package com.dbserver.desafiovotacaofullstack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.dbserver.desafiovotacaofullstack.dtos.ErrorResponseDto;
import com.dbserver.desafiovotacaofullstack.utils.FormatStackTracerException;

@ControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNullPointerException(ResourceNotFoundException exception, WebRequest request) {
		String endpoint = request.getDescription(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND.value(),FormatStackTracerException.getFormatStackTracer(exception.getStackTrace()), endpoint));
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException exception, WebRequest request) {
		String endpoint = request.getDescription(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value(),"", endpoint));
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ConflitDuplicateKeyException.class)
    public ResponseEntity<ErrorResponseDto> handleConflitDuplicateKeyException(ConflitDuplicateKeyException exception, WebRequest request) {
		String endpoint = request.getDescription(false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(exception.getMessage(), HttpStatus.CONFLICT.value(),"", endpoint));
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception, WebRequest request) {
		String endpoint = request.getDescription(false);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto("Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR.value(),"", endpoint));
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
		String endpoint = request.getDescription(false);
        StringBuilder resultMessage = new StringBuilder("");
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            resultMessage.append(fieldName + ": " + errorMessage + "\n");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(resultMessage.toString(), HttpStatus.BAD_REQUEST.value(),"", endpoint));
    }
}
