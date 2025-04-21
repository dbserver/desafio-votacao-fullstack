package com.db.votacaobackend.utils;

import com.db.votacaobackend.agenda.exception.AgendaBadRequestException;
import com.db.votacaobackend.agenda.exception.AgendaNotFoundExcepcion;
import com.db.votacaobackend.section.exception.SectionNotFoundException;
import com.db.votacaobackend.vote.exception.VoteBadRequestException;
import com.db.votacaobackend.vote.exception.VoteConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AgendaBadRequestException.class)
  private ResponseEntity<ErrorMessage> agendaBadRequest(AgendaBadRequestException exception) {
    ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }

  @ExceptionHandler(AgendaNotFoundExcepcion.class)
  private ResponseEntity<ErrorMessage> agendaNotFound(AgendaNotFoundExcepcion exception) {
    ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }


  @ExceptionHandler(SectionNotFoundException.class)
  private ResponseEntity<ErrorMessage> sectionNotFound(SectionNotFoundException exception) {
    ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  @ExceptionHandler(VoteConflictException.class)
  private ResponseEntity<ErrorMessage> voteBadRequest(VoteConflictException exception) {
    ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
  }

  @ExceptionHandler(VoteBadRequestException.class)
  private ResponseEntity<ErrorMessage> voteBadRequest(VoteBadRequestException exception) {
    ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }
}
