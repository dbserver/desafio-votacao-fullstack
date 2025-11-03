package com.votacao.domain.service;

import com.votacao.domain.model.SessaoVotacao;
import com.votacao.infrastructure.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public SessaoVotacao findById(Long id){
        return sessaoVotacaoRepository.findById(id).orElseThrow( () -> new RuntimeException("Sessão não encontrada"));
    }

    public SessaoVotacao save(SessaoVotacao sessaoVotacao){
        return sessaoVotacaoRepository.save(sessaoVotacao);
    }

}
