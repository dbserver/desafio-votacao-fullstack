package com.nunesd66.pautas_api.pauta.dto;

import com.nunesd66.pautas_api.sessaoVotacao.dto.SessaoVotacaoResponse;

public record PautaResponse(
        Long id,
        String titulo,
        String descricao,
        SessaoVotacaoResponse sessaoVotacao
) {}
