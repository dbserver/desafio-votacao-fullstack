package com.votacao.backend.domain.repository;

import com.votacao.backend.domain.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    Optional<Sessao> findByPautaId(UUID pautaId);

    List<Sessao> findByEncerradaEmIsNullAndExpiraEmLessThanEqual(LocalDateTime dataHora);
}
