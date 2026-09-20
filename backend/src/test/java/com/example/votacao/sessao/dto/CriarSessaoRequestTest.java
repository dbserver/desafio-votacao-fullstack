package com.example.votacao.sessao.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CriarSessaoRequestTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @AfterAll
  static void tearDown() {
    factory.close();
  }

  @Test
  @DisplayName("Deve aceitar duração não informada")
  void deveAceitarDuracaoNula() {
    assertThat(validator.validate(new CriarSessaoRequest(null))).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 120 })
  @DisplayName("Deve aceitar duração positiva")
  void deveAceitarDuracaoPositiva(int duracao) {
    assertThat(validator.validate(new CriarSessaoRequest(duracao))).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, -1, -120 })
  @DisplayName("Deve rejeitar duração zero ou negativa")
  void deveRejeitarDuracaoNaoPositiva(int duracao) {
    Set<ConstraintViolation<CriarSessaoRequest>> violacoes = validator.validate(new CriarSessaoRequest(duracao));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage)
        .containsExactly("A duração deve ser maior que zero");
  }
}
