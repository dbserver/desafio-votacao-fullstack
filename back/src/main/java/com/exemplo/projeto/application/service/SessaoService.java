package com.exemplo.projeto.application.service;

import com.exemplo.projeto.domain.model.Pauta;
import com.exemplo.projeto.domain.model.Sessao;
import com.exemplo.projeto.application.port.input.SessaoUseCase;
import com.exemplo.projeto.application.port.output.PautaRepositoryPort;
import com.exemplo.projeto.application.port.output.SessaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoService implements SessaoUseCase {
    private final PautaRepositoryPort pautaRepo;
    private final SessaoRepositoryPort sessaoRepo;

    @Override
    public Sessao abrirSessao(Long idPauta, Long duracaoEmMinutos) {
        Pauta pauta = pautaRepo.findById(idPauta)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));

        if (pauta.getSessao() != null && pauta.getSessao().getInicio() != null) {
            throw new RuntimeException("Sessão já iniciada para esta pauta.");
        }

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = duracaoEmMinutos != null
                ? inicio.plusMinutes(duracaoEmMinutos)
                : inicio.plusMinutes(1);

        Sessao sessao = new Sessao(inicio, fim, idPauta);
        pauta.setSessao(sessao);
        pautaRepo.save(pauta);
        sessaoRepo.save(sessao);

        return sessao;
    }
}
