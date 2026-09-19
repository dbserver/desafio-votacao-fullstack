package com.example.votacao.sessao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.votacao.pauta.Pauta;

class SessaoTest {

  private final Pauta pauta = new Pauta("Reforma do estatuto", "Aprovar a nova redação");

  @Test
  @DisplayName("Deve calcular o fim somando a duração ao início")
  void deveCalcularFim() {
    Sessao sessao = new Sessao(pauta, Duration.ofMinutes(5));

    assertThat(sessao.getPauta()).isSameAs(pauta);
    assertThat(sessao.getFim()).isEqualTo(sessao.getInicio().plus(Duration.ofMinutes(5)));
  }

  @Test
  @DisplayName("Deve estar aberta antes do fim")
  void deveEstarAberta() {
    assertThat(new Sessao(pauta, Duration.ofMinutes(1)).isAberta()).isTrue();
  }

  @Test
  @DisplayName("Deve estar encerrada depois do fim")
  void deveEstarEncerrada() {
    assertThat(new Sessao(pauta, Duration.ofMinutes(-1)).isAberta()).isFalse();
  }
}
