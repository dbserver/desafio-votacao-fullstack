package com.db.votacaobackend.agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AgendaBadRequestException extends RuntimeException {

  public AgendaBadRequestException(String message) {
    super(message);
  }
}
