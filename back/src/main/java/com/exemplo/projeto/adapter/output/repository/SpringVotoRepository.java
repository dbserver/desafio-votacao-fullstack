package com.exemplo.projeto.adapter.output.repository;

import com.exemplo.projeto.adapter.output.entity.VotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringVotoRepository extends JpaRepository<VotoEntity, Long> {

    boolean existsByAssociadoIdAndIdPauta(Long associadoId, Long idPauta);

    long countByIdPautaAndVoto(Long idPauta, boolean voto);

    @Query("SELECT DISTINCT v.idPauta FROM VotoEntity v")
    List<Long> findDistinctIdPauta();
}
