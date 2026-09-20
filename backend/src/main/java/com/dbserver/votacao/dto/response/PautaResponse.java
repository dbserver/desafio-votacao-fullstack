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
@Schema(description = "DTO de resposta com os dados cadastrais da pauta")
public class PautaResponse {

    @Schema(
        description = "Identificador único da pauta",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Título descritivo da pauta em deliberação",
        example = "Aprovação do Orçamento Anual 2026"
    )
    private String titulo;

    @Schema(
        description = "Detalhamento explicativo sobre o objetivo da pauta",
        example = "Deliberação e aprovação dos custos operacionais para o exercício do próximo ano."
    )
    private String descricao;

    @Schema(
        description = "Data e hora em que a pauta foi criada no sistema",
        example = "2026-09-19T20:30:00"
    )
    private LocalDateTime dataCriacao;
}