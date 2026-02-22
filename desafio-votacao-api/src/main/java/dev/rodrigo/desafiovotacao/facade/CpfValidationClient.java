package dev.rodrigo.desafiovotacao.facade;

import dev.rodrigo.desafiovotacao.dto.CpfValidationResponse;
import dev.rodrigo.desafiovotacao.exceptions.CpfNotFoundException;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class CpfValidationClient {

  private final Random random = new Random();

  public CpfValidationResponse validarCpf(String cpf) {

    if (random.nextInt(10) < 2) {
      throw new CpfNotFoundException("CPF inválido");
    }

    boolean ableToVote = random.nextBoolean();

    String status = ableToVote ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";

    return new CpfValidationResponse(status);
  }
}
