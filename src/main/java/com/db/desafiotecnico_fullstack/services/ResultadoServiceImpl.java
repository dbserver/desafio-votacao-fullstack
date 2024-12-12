package com.db.desafiotecnico_fullstack.services;

import com.db.desafiotecnico_fullstack.models.Pauta;
import com.db.desafiotecnico_fullstack.models.dto.ResultadoDTO;
import com.db.desafiotecnico_fullstack.models.enuns.EscolhaVoto;
import com.db.desafiotecnico_fullstack.repositories.PautaRepository;
import com.db.desafiotecnico_fullstack.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResultadoServiceImpl {
    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;

    @Autowired
    public ResultadoServiceImpl(VotoRepository votoRepository, PautaRepository pautaRepository) {
        this.votoRepository = votoRepository;
        this.pautaRepository = pautaRepository;
    }

    public ResultadoDTO calcularResultado(UUID pautaId) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));

        long votosSim = votoRepository.countByPauta_IdAndVoto(pautaId, EscolhaVoto.SIM);
        long votosNao = votoRepository.countByPauta_IdAndVoto(pautaId, EscolhaVoto.NAO);
        long totalVotos = votosSim + votosNao;

        return new ResultadoDTO(
                pautaId,
                pauta.getTitulo(),
                votosSim,
                votosNao,
                totalVotos
        );
    }
}