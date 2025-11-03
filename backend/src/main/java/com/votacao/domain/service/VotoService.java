package com.votacao.domain.service;

import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.model.Voto;
import com.votacao.infrastructure.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VotoService {

    private final VotoRepository votoRepository;

    public Voto save(Voto voto) {
        return votoRepository.save(voto);
    }

    public Long contarVotosSim(SessaoVotacao sessao) {
        return votoRepository.contarVotosSim(sessao.getId());
    }

    public Long contarVotosNao(SessaoVotacao sessao) {
        return votoRepository.contarVotosNao(sessao.getId());
    }

}
