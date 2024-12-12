package com.db.desafiotecnico_fullstack.repositories;

import com.db.desafiotecnico_fullstack.models.Voto;
import com.db.desafiotecnico_fullstack.models.enuns.EscolhaVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {
    boolean existsByPauta_IdAndAssociadoId(UUID pautaId, UUID associadoId);
    long countByPauta_IdAndVoto(UUID pautaId, EscolhaVoto voto);
}