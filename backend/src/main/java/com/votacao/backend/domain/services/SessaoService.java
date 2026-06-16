package com.votacao.backend.domain.services;

import com.votacao.backend.domain.entity.Pauta;
import com.votacao.backend.domain.entity.Sessao;
import com.votacao.backend.domain.repository.SessaoRepository;
import com.votacao.backend.infrastructure.exception.SessaoExpiradaException;
import com.votacao.backend.infrastructure.exception.SessaoJaAbertaException;
import com.votacao.backend.infrastructure.exception.SessaoNaoEncontradaException;
import com.votacao.backend.infrastructure.time.AppDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    @Transactional
    public Sessao abrir(UUID pautaId, Integer duracaoMinutos) {
        Pauta pauta = pautaService.buscarPorId(pautaId);

        sessaoRepository.findByPautaId(pautaId).ifPresent(s -> {
            throw new SessaoJaAbertaException("Já existe uma sessão para esta pauta.");
        });

        int duracao = (duracaoMinutos != null && duracaoMinutos > 0) ? duracaoMinutos : 1;

        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setExpiraEm(AppDateTime.now().plusMinutes(duracao));

        log.info("Abrindo sessão para pauta {} com duração de {} minuto(s)", pautaId, duracao);
        return sessaoRepository.save(sessao);
    }

    @Transactional(readOnly = true)
    public Sessao buscarAtiva(UUID sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new SessaoNaoEncontradaException(sessaoId));

        if (!sessao.isAtiva()) {
            throw new SessaoExpiradaException();
        }

        return sessao;
    }

    @Transactional(readOnly = true)
    public Sessao buscarPorId(UUID sessaoId) {
        return sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new SessaoNaoEncontradaException(sessaoId));
    }

    @Transactional(readOnly = true)
    public Sessao buscarPorPautaId(UUID pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new SessaoNaoEncontradaException(pautaId));
    }

    @Transactional
    public Sessao encerrar(Sessao sessao) {
        if (sessao.getEncerradaEm() != null) {
            return sessao;
        }

        sessao.setEncerradaEm(AppDateTime.now());
        log.info("Encerrando sessão {} automaticamente por expiração", sessao.getId());
        return sessaoRepository.save(sessao);
    }
}
