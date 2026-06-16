package com.votacao.backend.api.v1.dto;

import com.votacao.backend.domain.enums.OpcaoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarVotoRequest(@NotBlank(message = "ID do associado é obrigatório.") String associadoId,
                                   @NotNull(message = "Opção de voto é obrigatória.") OpcaoVoto opcao) {
}
