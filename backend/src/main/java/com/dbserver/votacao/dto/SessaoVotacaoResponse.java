package com.dbserver.votacao.dto;

import java.time.LocalDateTime;

public record SessaoVotacaoResponse(
        Long id,
        Long pautaId,
        String pautaTitulo,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean aberta
) {}
