package com.exemplo.projeto.application.port.input;

import com.exemplo.projeto.domain.model.Pauta;

import java.util.List;
import java.util.Optional;

public interface PautaUseCase {
    List<Pauta> buscar();
    Optional<Pauta> buscarPorId(Long id);
    Pauta criar(Pauta pauta);
}
