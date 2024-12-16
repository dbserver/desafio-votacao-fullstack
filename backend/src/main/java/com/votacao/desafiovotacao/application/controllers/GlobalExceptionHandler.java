package com.votacao.desafiovotacao.application.controllers;

import com.votacao.desafiovotacao.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CPFAlreadyExistsException.class)
    public ResponseEntity<?> handleCPFAlreadyExistsException(final CPFAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NameNeededException.class)
    public ResponseEntity<?> handleNameNeededException(final CPFAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CPFInvalidException.class)
    public ResponseEntity<?> handleCPFInvalidException(final CPFAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgendaNotValidException.class)
    public ResponseEntity<?> handleAgendaNotValidException(final AgendaNotValidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgendaNotFoundException.class)
    public ResponseEntity<?> handleAgendaNotFoundException(final AgendaNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SessionTimeException.class)
    public ResponseEntity<?> handleSessionTimeException(final SessionTimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatException(final NumberFormatException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyVotedException.class)
    public ResponseEntity<?> handleAlreadyVotedException(final AlreadyVotedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoSessionToVoteException.class)
    public ResponseEntity<?> handleNoSessionToVoteException(final NoSessionToVoteException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoVoteException.class)
    public ResponseEntity<?> handleNoVoteException(final NoVoteException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }
}


