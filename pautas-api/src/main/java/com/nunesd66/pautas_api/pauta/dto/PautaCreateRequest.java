package com.nunesd66.pautas_api.pauta.dto;

import com.nunesd66.pautas_api.sessaoVotacao.dto.SessaoVotacaoCreateRequest;

public record PautaCreateRequest(
    String titulo,
    String descricao,
    SessaoVotacaoCreateRequest sessaoVotacao
) {}