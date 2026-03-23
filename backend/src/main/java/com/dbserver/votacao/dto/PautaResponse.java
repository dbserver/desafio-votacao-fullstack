package com.dbserver.votacao.dto;

import java.time.LocalDateTime;

public record PautaResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime criadaEm
) {}
