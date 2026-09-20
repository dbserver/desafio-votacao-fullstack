package com.dbserver.votacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta com a apuração e o resultado consolidado da votação de uma pauta")
public class ResultadoVotacaoResponse {

    @Schema(
        description = "Identificador único da pauta",
        example = "1"
    )
    private Long pautaId;

    @Schema(
        description = "Título da pauta deliberada",
        example = "Aprovação do Orçamento Anual 2026"
    )
    private String tituloPauta;

    @Schema(
        description = "Quantidade total de votos favoráveis (SIM)",
        example = "1"
    )
    private long votosSim;

    @Schema(
        description = "Quantidade total de votos contrários (NÃO)",
        example = "1"
    )
    private long votosNao;

    @Schema(
        description = "Soma total dos votos computados",
        example = "2"
    )
    private long totalVotos;

    @Schema(
        description = "Status consolidado do resultado da votação",
        example = "SESSÃO EM ANDAMENTO",
        allowableValues = {"APROVADA", "REPROVADA", "EMPATE", "SESSÃO EM ANDAMENTO", "SEM VOTOS"}
    )
    private String resultado;

    @Schema(
        description = "Indica se a janela de tempo da sessão de votação já encerrou",
        example = "false"
    )
    private boolean sessaoEncerrada;
}