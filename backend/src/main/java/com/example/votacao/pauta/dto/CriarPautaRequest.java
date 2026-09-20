package com.example.votacao.pauta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPautaRequest(
        @Schema(description = "Nome da pauta", example = "Pauta sobre futebol") @NotBlank(message = "Título é obrigatório") @Size(max = 255, message = "Título deve ter no máximo 255 caracteres") String titulo,
        @Schema(description = "Descrição da pauta", example = "O futebol atual é melhor do que o antigo??") @NotBlank(message = "Descrição é obrigatório") String descricao) {
}