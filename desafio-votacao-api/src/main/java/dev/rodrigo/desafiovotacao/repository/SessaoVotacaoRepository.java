package dev.rodrigo.desafiovotacao.repository;

import dev.rodrigo.desafiovotacao.entity.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
  boolean existsByPautaIdAndDataFechamentoAfter(Long pautaId, LocalDateTime data);
}
