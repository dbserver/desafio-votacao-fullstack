package com.exemplo.projeto.application.service;

import com.exemplo.projeto.domain.model.Pauta;
import com.exemplo.projeto.application.port.input.PautaUseCase;
import com.exemplo.projeto.application.port.output.PautaRepositoryPort;
import com.exemplo.projeto.application.port.output.VotoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PautaService implements PautaUseCase {
    private final PautaRepositoryPort pautaRepo;
    private final VotoRepositoryPort votoRepo;

    @Override
    public List<Pauta> buscar() {
        return pautaRepo.findAll();
    }

    @Override
    public Optional<Pauta> buscarPorId(Long id) {
        return pautaRepo.findById(id);
    }

    @Override
    public Pauta criar(Pauta pauta) {
        return pautaRepo.save(pauta);
    }
}
