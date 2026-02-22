package dev.rodrigo.desafiovotacao.entity;

import dev.rodrigo.desafiovotacao.exceptions.RegraNegocioException;
import dev.rodrigo.desafiovotacao.utils.CpfValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Cpf {

  @Column(name = "cpf", nullable = false, length = 11)
  private String numero;

  protected Cpf() {}

  public Cpf(String numero) {
    numero = numero.replaceAll("\\D", "");

    if (!CpfValidator.isValid(numero)) {
      throw new RegraNegocioException("CPF inválido");
    }

    this.numero = numero;
  }

}
