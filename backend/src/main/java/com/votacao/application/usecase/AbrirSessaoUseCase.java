package com.votacao.application.usecase;

import com.votacao.application.dto.SessaoVotacaoRequestDTO;
import com.votacao.domain.model.Pauta;
import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.service.PautaService;
import com.votacao.domain.service.SessaoVotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AbrirSessaoUseCase {

    private final SessaoVotacaoService sessaoVotacaoService;
    private final PautaService pautaService;

    public void execute(SessaoVotacaoRequestDTO sessaoVotacaoRequestDTO) {

        Pauta pauta = pautaService.findById(sessaoVotacaoRequestDTO.getIdPauta());

        sessaoVotacaoService.save(SessaoVotacao.builder()
                .pauta(pauta)
                .inicio(sessaoVotacaoRequestDTO.getInicioSessao())
                .fim(sessaoVotacaoRequestDTO.getFimSessao())
                .aberta(false)
                .tempoDuracao(Duration.between(sessaoVotacaoRequestDTO.getInicioSessao(), sessaoVotacaoRequestDTO.getFimSessao()).toMinutes())
                .build());
    }

}
