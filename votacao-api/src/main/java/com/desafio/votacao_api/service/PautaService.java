package com.desafio.votacao_api.service;

import com.desafio.votacao_api.repository.PautaRepository;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public void createPauta() {

    }

}
