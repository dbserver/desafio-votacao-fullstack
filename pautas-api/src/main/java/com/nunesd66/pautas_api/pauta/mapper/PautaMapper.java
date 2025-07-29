package com.nunesd66.pautas_api.pauta.mapper;

import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.sessaoVotacao.dto.SessaoVotacaoCreateRequest;
import com.nunesd66.pautas_api.sessaoVotacao.dto.SessaoVotacaoResponse;
import com.nunesd66.pautas_api.sessaoVotacao.model.SessaoVotacao;

public interface PautaMapper {
    Pauta toEntity(PautaCreateRequest request);
    SessaoVotacao toEntity(SessaoVotacaoCreateRequest request);
    PautaResponse toResponse(Pauta pauta);
    SessaoVotacaoResponse toResponse(SessaoVotacao sessao);
}
