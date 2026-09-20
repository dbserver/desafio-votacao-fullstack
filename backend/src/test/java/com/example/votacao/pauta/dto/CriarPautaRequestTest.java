package com.example.votacao.pauta.dto;

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

class CriarPautaRequestTest {

  private static final String TITULO = "Reforma do estatuto";
  private static final String DESCRICAO = "Aprovar a nova redação do estatuto";

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
  @DisplayName("Deve aceitar título e descrição válidos")
  void deveAceitarPautaValida() {
    assertThat(validator.validate(new CriarPautaRequest(TITULO, DESCRICAO))).isEmpty();
  }

  @Test
  @DisplayName("Deve aceitar título com exatamente 255 caracteres")
  void deveAceitarTituloNoLimite() {
    assertThat(validator.validate(new CriarPautaRequest("a".repeat(255), DESCRICAO))).isEmpty();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { "   ", "\t", "\n" })
  @DisplayName("Deve rejeitar título nulo, vazio ou em branco")
  void deveRejeitarTituloEmBranco(String titulo) {
    Set<ConstraintViolation<CriarPautaRequest>> violacoes = validator.validate(new CriarPautaRequest(titulo, DESCRICAO));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("Título é obrigatório");
  }

  @Test
  @DisplayName("Deve rejeitar título com mais de 255 caracteres")
  void deveRejeitarTituloMuitoLongo() {
    Set<ConstraintViolation<CriarPautaRequest>> violacoes = validator
        .validate(new CriarPautaRequest("a".repeat(256), DESCRICAO));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage)
        .containsExactly("Título deve ter no máximo 255 caracteres");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { "   ", "\t", "\n" })
  @DisplayName("Deve rejeitar descrição nula, vazia ou em branco")
  void deveRejeitarDescricaoEmBranco(String descricao) {
    Set<ConstraintViolation<CriarPautaRequest>> violacoes = validator.validate(new CriarPautaRequest(TITULO, descricao));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("Descrição é obrigatório");
  }
}
