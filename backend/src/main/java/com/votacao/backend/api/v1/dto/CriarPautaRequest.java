package com.votacao.backend.api.v1.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarPautaRequest(
        @NotBlank(message = "Título é obrigatório.") String titulo,
        String descricao
) {
}
