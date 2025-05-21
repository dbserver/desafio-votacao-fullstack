package com.exemplo.projeto.application.port.output;

import com.exemplo.projeto.domain.model.Voto;

import java.util.List;

public interface VotoRepositoryPort {
    boolean existsByAssociadoIdAndIdPauta(Long associadoId, Long idPauta);
    void save(Voto voto, Long idPauta);
    long countByIdPautaAndVoto(Long idPauta, boolean voto);
    List<Long> findDistinctIdPauta();
}
