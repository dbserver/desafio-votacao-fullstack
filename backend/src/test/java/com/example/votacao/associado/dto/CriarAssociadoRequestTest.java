package com.example.votacao.associado.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CriarAssociadoRequestTest {

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
  @DisplayName("Deve aceitar nome válido")
  void deveAceitarNomeValido() {
    assertThat(validator.validate(new CriarAssociadoRequest("Maria"))).isEmpty();
  }

  @Test
  @DisplayName("Deve aceitar nome com exatamente 255 caracteres")
  void deveAceitarNomeNoLimite() {
    assertThat(validator.validate(new CriarAssociadoRequest("a".repeat(255)))).isEmpty();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { "   ", "\t", "\n" })
  @DisplayName("Deve rejeitar nome nulo, vazio ou em branco")
  void deveRejeitarNomeEmBranco(String nome) {
    Set<ConstraintViolation<CriarAssociadoRequest>> violacoes = validator.validate(new CriarAssociadoRequest(nome));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("Nome é obrigatório");
  }

  @Test
  @DisplayName("Deve rejeitar nome com mais de 255 caracteres")
  void deveRejeitarNomeMuitoLongo() {
    Set<ConstraintViolation<CriarAssociadoRequest>> violacoes = validator
        .validate(new CriarAssociadoRequest("a".repeat(256)));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage)
        .containsExactly("Nome deve ter no máximo 255 caracteres");
  }
}
