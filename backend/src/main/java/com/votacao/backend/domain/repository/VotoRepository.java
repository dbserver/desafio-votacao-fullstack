package com.votacao.backend.domain.repository;

import com.votacao.backend.domain.entity.Voto;
import com.votacao.backend.domain.enums.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {

    boolean existsBySessaoIdAndAssociadoId(UUID sessaoId, String associadoId);

    long countBySessaoIdAndOpcao(UUID sessaoId, OpcaoVoto opcao);

    Optional<Voto> findByIdAndSessaoId(UUID id, UUID sessaoId);
}
