package com.example.votacao.sessao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
  boolean existsByPautaId(Long pautaId);

  @EntityGraph(attributePaths = "pauta")
  Optional<Sessao> findByPautaId(Long pautaId);

  @Override
  @EntityGraph(attributePaths = "pauta")
  List<Sessao> findAll();
}
