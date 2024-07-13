package com.dbserver.desafiovotacaofullstack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value(),"", endpoint));
    }
	
	
}
