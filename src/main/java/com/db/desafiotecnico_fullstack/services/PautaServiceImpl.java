package com.db.desafiotecnico_fullstack.services;

import com.db.desafiotecnico_fullstack.models.Pauta;
import com.db.desafiotecnico_fullstack.models.dto.PautaDTO;
import com.db.desafiotecnico_fullstack.repositories.PautaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class PautaServiceImpl {

    private final PautaRepository pautaRepository;
    private final ScheduledExecutorService scheduler;

    public PautaServiceImpl(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public Pauta criarPauta(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        pauta.setTempoVotacao(pautaDTO.getTempoVotacao() != null ?
                pautaDTO.getTempoVotacao() : 1);
        return pautaRepository.save(pauta);
    }

    public void abrirSessao(UUID id, Integer tempoVotacao) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));

        if (pauta.isAberta()) {
            throw new IllegalStateException("Sessão já está aberta");
        }

        if (tempoVotacao != null) {
            pauta.setTempoVotacao(tempoVotacao);
        }

        pauta.setAberta(true);
        pautaRepository.save(pauta);

        scheduler.schedule(() -> {
            fecharSessao(id);
        }, pauta.getTempoVotacao(), TimeUnit.MINUTES);
    }

    private void fecharSessao(UUID id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));
        pauta.setAberta(false);
        pautaRepository.save(pauta);
    }

    public Pauta buscarPorId(UUID id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));
    }
}