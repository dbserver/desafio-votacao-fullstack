package com.dbchal.voting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbchal.voting.domain.SessaoVotacao;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

	boolean existsByPautaId(Long pautaId);
	
	Optional<SessaoVotacao> findByPautaId(Long pautaId);
	
}
