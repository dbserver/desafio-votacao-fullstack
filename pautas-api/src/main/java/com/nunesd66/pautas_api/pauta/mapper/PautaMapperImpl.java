package com.nunesd66.pautas_api.pauta.mapper;

import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.sessaoVotacao.dto.SessaoVotacaoCreateRequest;
import com.nunesd66.pautas_api.sessaoVotacao.dto.SessaoVotacaoResponse;
import com.nunesd66.pautas_api.sessaoVotacao.model.SessaoVotacao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class PautaMapperImpl implements PautaMapper {

    @Override
    public Pauta toEntity(PautaCreateRequest request) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(request.titulo());
        pauta.setDescricao(request.descricao());
        pauta.setSessaoVotacao(toEntity(request.sessaoVotacao()));
        return pauta;
    }

    @Override
    public SessaoVotacao toEntity(SessaoVotacaoCreateRequest request) {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setDuracao(request.duracao());
        return sessao;
    }

    @Override
    public PautaResponse toResponse(Pauta pauta) {
        return new PautaResponse(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                toResponse(pauta.getSessaoVotacao())
        );
    }

    @Override
    public SessaoVotacaoResponse toResponse(SessaoVotacao sessao) {
        return new SessaoVotacaoResponse(
                sessao.getId(),
                sessao.getDataAbertura(),
                sessao.getDataFechamento(),
                sessao.getVotos()
        );
    }
}
