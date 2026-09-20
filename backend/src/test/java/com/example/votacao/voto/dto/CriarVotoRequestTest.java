package com.example.votacao.voto.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.example.votacao.voto.OpcaoVoto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CriarVotoRequestTest {

  private static final Long ASSOCIADO_ID = 1L;

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

  @ParameterizedTest
  @EnumSource(OpcaoVoto.class)
  @DisplayName("Deve aceitar associado e opção válidos")
  void deveAceitarVotoValido(OpcaoVoto opcao) {
    assertThat(validator.validate(new CriarVotoRequest(ASSOCIADO_ID, opcao))).isEmpty();
  }

  @Test
  @DisplayName("Deve rejeitar associado nulo")
  void deveRejeitarAssociadoNulo() {
    Set<ConstraintViolation<CriarVotoRequest>> violacoes = validator
        .validate(new CriarVotoRequest(null, OpcaoVoto.SIM));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("Associado é obrigatório");
  }

  @Test
  @DisplayName("Deve rejeitar opção nula")
  void deveRejeitarOpcaoNula() {
    Set<ConstraintViolation<CriarVotoRequest>> violacoes = validator
        .validate(new CriarVotoRequest(ASSOCIADO_ID, null));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("Opção é obrigatória");
  }
}
