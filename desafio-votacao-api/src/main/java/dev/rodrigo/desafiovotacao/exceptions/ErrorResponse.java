package dev.rodrigo.desafiovotacao.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private int status;
  private String message;
}
