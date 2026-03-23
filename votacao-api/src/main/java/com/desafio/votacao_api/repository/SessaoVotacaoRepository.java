package com.desafio.votacao_api.repository;

import com.desafio.votacao_api.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for persisting and retrieving SessaoVotacao entities.
 */
@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, UUID> {
}

