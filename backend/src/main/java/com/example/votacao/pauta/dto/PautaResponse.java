package com.example.votacao.pauta.dto;

import java.time.Instant;

import com.example.votacao.pauta.Pauta;

import io.swagger.v3.oas.annotations.media.Schema;

public record PautaResponse(@Schema(description = "Identificador da pauta", example = "1") Long id,
    @Schema(description = "Titulo da pauta", example = "Pauta Futebol") String titulo,
    @Schema(description = "Descrição da pauta", example = "O futebol antigo é melhor que o atual?") String descricao,
    @Schema(description = "Data da criação da pauta", example = "2026-09-19T18:26:46.470352Z") Instant criadoEm) {

  public static PautaResponse from(Pauta pauta) {
    return new PautaResponse(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), pauta.getCriadaEm());
  }
}