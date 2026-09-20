package com.example.votacao.voto.dto;

import com.example.votacao.voto.OpcaoVoto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CriarVotoRequest(
    @Schema(description = "Identificador do associado", example = "1") @NotNull(message = "Associado é obrigatório") Long associadoId,
    @Schema(description = "Opção de voto", example = "SIM") @NotNull(message = "Opção é obrigatória") OpcaoVoto opcao) {

}
