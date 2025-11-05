package com.db.votacaobackend.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VoteBadRequestException extends RuntimeException {

  public VoteBadRequestException(String message) {
    super(message);
  }

}
