package com.dbserver.votacao.dto.response;

import com.dbserver.votacao.model.enums.OpcaoVoto;
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
@Schema(description = "DTO de resposta com a confirmação e os detalhes do voto registrado")
public class VotoResponse {

    @Schema(
        description = "Identificador único do registro de voto",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Identificador da pauta em que o voto foi computado",
        example = "1"
    )
    private Long pautaId;

    @Schema(
        description = "CPF ou identificador único do associado participante",
        example = "12345678901"
    )
    private String associadoId;

    @Schema(
    description = "Opção de voto registrada pelo associado",
    allowableValues = {"SIM", "NAO"}
)
    private OpcaoVoto voto;

    @Schema(
        description = "Data e hora exatas do registro do voto",
        example = "2026-09-19T20:45:00"
    )
    private LocalDateTime dataVoto;
}