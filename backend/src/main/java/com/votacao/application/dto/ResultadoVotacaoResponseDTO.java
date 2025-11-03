package com.votacao.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVotacaoResponseDTO {

    @Schema(description = "Identificador da pauta", example = "1")
    private Long idPauta;

    @Schema(description = "Título da pauta", example = "Reforma do estatuto da cooperativa")
    private String tituloPauta;

    @Schema(description = "Descrição da pauta", example = "Votação sobre atualização do estatuto interno")
    private String descricaoPauta;

    @Schema(description = "Identificador da sessão de votação", example = "10")
    private Long idSessao;

    @Schema(description = "Data e hora de início da sessão", example = "2025-10-30T10:00:00")
    private LocalDateTime inicioSessao;

    @Schema(description = "Data e hora de término da sessão", example = "2025-10-30T10:05:00")
    private LocalDateTime fimSessao;

    @Schema(description = "Sessão está aberta para votação", example = "false")
    private boolean sessaoAberta;

    @Schema(description = "Quantidade total de votos SIM", example = "125")
    private Long votosSim;

    @Schema(description = "Quantidade total de votos NÃO", example = "89")
    private Long votosNao;

    @Schema(description = "Total geral de votos", example = "214")
    private Long totalVotos;
}