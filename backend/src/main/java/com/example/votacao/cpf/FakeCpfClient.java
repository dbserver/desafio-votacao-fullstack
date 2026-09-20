package com.example.votacao.cpf;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.votacao.shared.RecursoNaoEncontradoException;

@Component
public class FakeCpfClient implements CpfClient {

  private final Random random;

  public FakeCpfClient() {
    this(new Random());
  }

  FakeCpfClient(Random random) {
    this.random = random;
  }

  @Override
  public StatusCpf verificar(String cpf) {
    if (random.nextBoolean()) {
      throw new RecursoNaoEncontradoException("CPF inválido");
    }

    return random.nextBoolean() ? StatusCpf.ABLE_TO_VOTE : StatusCpf.UNABLE_TO_VOTE;
  }
}
