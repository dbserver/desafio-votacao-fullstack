package com.db.votacaobackend.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VoteConflictException extends RuntimeException {

  public VoteConflictException(String message) {
    super(message);
  }
}
