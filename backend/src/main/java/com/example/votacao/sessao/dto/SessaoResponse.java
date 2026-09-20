package com.example.votacao.sessao.dto;

import java.time.Instant;

import com.example.votacao.sessao.Sessao;

import io.swagger.v3.oas.annotations.media.Schema;

public record SessaoResponse(
    @Schema(description = "Identificador da sessão", example = "1") Long id,
    @Schema(description = "Identificador da pauta", example = "1") Long pautaId,
    @Schema(description = "Titulo da pauta", example = "Pauta 1") String pautaTitulo,
    @Schema(description = "Início da sessão de votação", example = "2026-09-19T18:26:46Z") Instant inicio,
    @Schema(description = "Fim da sessão de votação", example = "2026-09-19T18:27:46Z") Instant fim) {

  public static SessaoResponse from(Sessao sessao) {
    return new SessaoResponse(
        sessao.getId(),
        sessao.getPauta().getId(),
        sessao.getPauta().getTitulo(),
        sessao.getInicio(),
        sessao.getFim());
  }
}
