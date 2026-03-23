package com.dbserver.votacao.dto;

import com.dbserver.votacao.enums.VotoOpcao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VotoRequest(
        @NotBlank(message = "CPF do associado é obrigatório")
        String associadoId,
        @NotNull(message = "Voto é obrigatório (SIM ou NAO)")
        VotoOpcao voto
) {}
