package com.dbserver.votacao.dto;

import jakarta.validation.constraints.NotBlank;

public record PautaRequest(
        @NotBlank(message = "Título é obrigatório")
        String titulo,
        String descricao
) {}
