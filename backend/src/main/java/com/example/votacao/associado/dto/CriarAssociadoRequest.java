package com.example.votacao.associado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarAssociadoRequest(
    @Schema(description = "Nome do associado", example = "Associado Fulano") @NotBlank(message = "Nome é obrigatório") @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres") String nome) {
}