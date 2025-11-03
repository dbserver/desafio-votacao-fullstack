package com.votacao.application.usecase;

import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.model.Voto;
import com.votacao.domain.service.SessaoVotacaoService;
import com.votacao.domain.service.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrarVotoUseCase {

    private final VotoService votoService;
    private final SessaoVotacaoService sessaoVotacaoService;

    public void execute(Long id, String escolha) {

        SessaoVotacao sessaoVotacao = sessaoVotacaoService.findById(id);

        votoService.save(Voto.builder()
                        .escolha(escolha)
                        .pauta(sessaoVotacao.getPauta())
                        .sessaoVotacao(sessaoVotacao)
                        .voto(true)
                .build());
    }

}
