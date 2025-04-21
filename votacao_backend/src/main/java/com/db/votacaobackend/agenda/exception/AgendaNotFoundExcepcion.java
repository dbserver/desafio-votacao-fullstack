package com.db.votacaobackend.agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AgendaNotFoundExcepcion extends RuntimeException {

  public AgendaNotFoundExcepcion(String message) {
    super(message);
  }


}
