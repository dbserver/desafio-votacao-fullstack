package com.dbserver.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta com os detalhes e status da sessão de votação")
public class SessaoVotacaoResponse {

    @Schema(
        description = "Identificador único da sessão de votação",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Identificador único da pauta associada",
        example = "1"
    )
    private Long pautaId;

    @Schema(
        description = "Título da pauta associada à sessão",
        example = "Aprovação do Orçamento Anual 2026"
    )
    private String tituloPauta;

    @Schema(
        description = "Data e hora de início da sessão de votação",
        example = "2026-09-19T20:00:00"
    )
    private LocalDateTime dataAbertura;

    @Schema(
        description = "Data e hora limite de término da sessão de votação",
        example = "2026-09-19T20:15:00"
    )
    private LocalDateTime dataEncerramento;

    @Schema(
        description = "Indica se a sessão está aberta para receber novos votos no momento",
        example = "true"
    )
    private boolean aberta;
}