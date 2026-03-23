package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.SessaoVotacaoRequest;
import com.dbserver.votacao.dto.SessaoVotacaoResponse;
import com.dbserver.votacao.entity.Pauta;
import com.dbserver.votacao.entity.SessaoVotacao;
import com.dbserver.votacao.exception.BusinessException;
import com.dbserver.votacao.exception.NotFoundException;
import com.dbserver.votacao.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private static final Logger log = LoggerFactory.getLogger(SessaoVotacaoService.class);
    private static final int DURACAO_PADRAO_MINUTOS = 1;

    private final SessaoVotacaoRepository sessaoRepository;
    private final PautaService pautaService;

    public SessaoVotacaoResponse abrirSessao(SessaoVotacaoRequest request) {
        log.info("Abrindo sessão de votação para pauta: {}", request.pautaId());

        Pauta pauta = pautaService.findById(request.pautaId());

        if (sessaoRepository.existsByPautaId(request.pautaId())) {
            throw new BusinessException("Já existe uma sessão de votação para esta pauta");
        }

        int duracao = request.duracaoMinutos() != null && request.duracaoMinutos() > 0
                ? request.duracaoMinutos()
                : DURACAO_PADRAO_MINUTOS;

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusMinutes(duracao))
                .build();

        SessaoVotacao salva = sessaoRepository.save(sessao);
        log.info("Sessão aberta com id: {} (duração: {} min)", salva.getId(), duracao);

        return toResponse(salva);
    }

    public SessaoVotacaoResponse buscarPorPautaId(Long pautaId) {
        log.info("Buscando sessão por pauta id: {}", pautaId);
        return toResponse(findByPautaId(pautaId));
    }

    public SessaoVotacao findByPautaId(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada para pauta: " + pautaId));
    }

    private SessaoVotacaoResponse toResponse(SessaoVotacao sessao) {
        return new SessaoVotacaoResponse(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getPauta().getTitulo(),
                sessao.getDataInicio(),
                sessao.getDataFim(),
                sessao.isAberta()
        );
    }
}
