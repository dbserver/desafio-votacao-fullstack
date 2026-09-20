package com.example.votacao.associado.dto;

import com.example.votacao.associado.Associado;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um associado")
public record AssociadoResponse(
    @Schema(description = "Identificador do associado", example = "1") Long id,
    @Schema(description = "Nome do associado", example = "Associado Fulano") String nome,
    @Schema(description = "CPF do associado, somente dígitos", example = "52998224725") String cpf) {

  public static AssociadoResponse from(Associado associado) {
    return new AssociadoResponse(associado.getId(), associado.getNome(), associado.getCpf());
  }
}
