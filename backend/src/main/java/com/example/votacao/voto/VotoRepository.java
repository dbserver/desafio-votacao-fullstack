package com.example.votacao.voto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
  boolean existsBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

  List<Voto> findBySessaoId(Long sessaoId);

}