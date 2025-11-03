package com.votacao.application.usecase;

import com.votacao.application.dto.ResultadoVotacaoResponseDTO;
import com.votacao.domain.model.SessaoVotacao;
import com.votacao.domain.service.SessaoVotacaoService;
import com.votacao.domain.service.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContabilizarVotosUseCase {

    private final SessaoVotacaoService sessaoVotacaoService;
    private final VotoService votoService;

    public ResultadoVotacaoResponseDTO execute(Long idSessao) {
        SessaoVotacao sessao = sessaoVotacaoService.findById(idSessao);

        long votosSim = votoService.contarVotosSim(sessao);
        long votosNao = votoService.contarVotosNao(sessao);
        long total = votosSim + votosNao;

        var pauta = sessao.getPauta();

        return ResultadoVotacaoResponseDTO.builder()
                .idPauta(pauta.getId())
                .tituloPauta(pauta.getTitulo())
                .descricaoPauta(pauta.getDescricao())
                .idSessao(sessao.getId())
                .inicioSessao(sessao.getInicio())
                .fimSessao(sessao.getFim())
                .sessaoAberta(sessao.isAberta())
                .votosSim(votosSim)
                .votosNao(votosNao)
                .totalVotos(total)
                .build();
    }

}
