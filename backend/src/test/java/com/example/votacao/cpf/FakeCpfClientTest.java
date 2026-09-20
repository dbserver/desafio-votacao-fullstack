package com.example.votacao.cpf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.votacao.shared.RecursoNaoEncontradoException;

@ExtendWith(MockitoExtension.class)
class FakeCpfClientTest {

  private static final String CPF = "52998224725";

  @Mock
  private Random random;

  @Test
  @DisplayName("Deve lançar exceção quando o sorteio considerar o CPF inválido")
  void deveRejeitarCpfInvalido() {
    when(random.nextBoolean()).thenReturn(true);

    assertThatThrownBy(() -> new FakeCpfClient(random).verificar(CPF))
        .isInstanceOf(RecursoNaoEncontradoException.class)
        .hasMessage("CPF inválido");
  }

  @Test
  @DisplayName("Deve retornar ABLE_TO_VOTE quando o sorteio aprovar o CPF")
  void deveRetornarApto() {
    when(random.nextBoolean()).thenReturn(false, true);

    assertThat(new FakeCpfClient(random).verificar(CPF)).isEqualTo(StatusCpf.ABLE_TO_VOTE);
  }

  @Test
  @DisplayName("Deve retornar UNABLE_TO_VOTE quando o sorteio recusar o CPF")
  void deveRetornarInapto() {
    when(random.nextBoolean()).thenReturn(false, false);

    assertThat(new FakeCpfClient(random).verificar(CPF)).isEqualTo(StatusCpf.UNABLE_TO_VOTE);
  }
}
