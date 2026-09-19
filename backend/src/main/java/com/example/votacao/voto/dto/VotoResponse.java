package com.example.votacao.voto.dto;

import java.time.Instant;

import com.example.votacao.voto.OpcaoVoto;
import com.example.votacao.voto.Voto;

import io.swagger.v3.oas.annotations.media.Schema;

public record VotoResponse(
    @Schema(description = "Identificador do voto", example = "1") Long id,
    @Schema(description = "Identificador da sessão", example = "1") Long sessaoId,
    @Schema(description = "Identificador do associado", example = "1") Long associadoId,
    @Schema(description = "Opção votada", example = "SIM") OpcaoVoto opcao,
    @Schema(description = "Data do voto", example = "2026-09-19T18:26:46Z") Instant criadoEm) {

  public static VotoResponse from(Voto voto) {
    return new VotoResponse(
        voto.getId(),
        voto.getSessao().getId(),
        voto.getAssociado().getId(),
        voto.getOpcao(),
        voto.getCriadoEm());
  }
}
