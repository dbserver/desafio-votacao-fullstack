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

  private static final String NOME = "Maria";
  private static final String CPF = "52998224725";

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
  @DisplayName("Deve aceitar nome e CPF válidos")
  void deveAceitarAssociadoValido() {
    assertThat(validator.validate(new CriarAssociadoRequest(NOME, CPF))).isEmpty();
  }

  @Test
  @DisplayName("Deve aceitar nome com exatamente 255 caracteres")
  void deveAceitarNomeNoLimite() {
    assertThat(validator.validate(new CriarAssociadoRequest("a".repeat(255), CPF))).isEmpty();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { "   ", "\t", "\n" })
  @DisplayName("Deve rejeitar nome nulo, vazio ou em branco")
  void deveRejeitarNomeEmBranco(String nome) {
    Set<ConstraintViolation<CriarAssociadoRequest>> violacoes = validator.validate(new CriarAssociadoRequest(nome, CPF));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("Nome é obrigatório");
  }

  @Test
  @DisplayName("Deve rejeitar nome com mais de 255 caracteres")
  void deveRejeitarNomeMuitoLongo() {
    Set<ConstraintViolation<CriarAssociadoRequest>> violacoes = validator
        .validate(new CriarAssociadoRequest("a".repeat(256), CPF));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage)
        .containsExactly("Nome deve ter no máximo 255 caracteres");
  }

  @Test
  @DisplayName("Deve aceitar CPF com pontuação")
  void deveAceitarCpfFormatado() {
    assertThat(validator.validate(new CriarAssociadoRequest(NOME, "529.982.247-25"))).isEmpty();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("Deve rejeitar CPF nulo ou vazio")
  void deveRejeitarCpfAusente(String cpf) {
    Set<ConstraintViolation<CriarAssociadoRequest>> violacoes = validator.validate(new CriarAssociadoRequest(NOME, cpf));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).contains("CPF é obrigatório");
  }

  @ParameterizedTest
  @ValueSource(strings = { "12345678900", "11111111111", "5299822472", "abc" })
  @DisplayName("Deve rejeitar CPF com dígitos verificadores ou formato inválidos")
  void deveRejeitarCpfInvalido(String cpf) {
    Set<ConstraintViolation<CriarAssociadoRequest>> violacoes = validator.validate(new CriarAssociadoRequest(NOME, cpf));

    assertThat(violacoes).extracting(ConstraintViolation::getMessage).containsExactly("CPF inválido");
  }
}
