package com.dbserver.votacao.dto;

public record ResultadoVotacaoResponse(
        Long pautaId,
        String pautaTitulo,
        long totalVotos,
        long votosSim,
        long votosNao,
        String resultado
) {}
