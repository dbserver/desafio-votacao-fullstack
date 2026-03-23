package com.dbserver.votacao.dto;

import com.dbserver.votacao.enums.VotoOpcao;

public record VotoResponse(
        Long id,
        String associadoId,
        VotoOpcao voto
) {}
