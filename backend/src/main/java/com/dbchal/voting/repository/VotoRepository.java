package com.dbchal.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbchal.voting.domain.entity.Voto;
import com.dbchal.voting.dto.voto.ResultadoVotosProjection;

public interface VotoRepository extends JpaRepository<Voto, Long> {

	boolean existsBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

	@Query(value = """
			SELECT COUNT(*) FILTER (WHERE opcao = 'SIM') AS "votosSim",
				   COUNT(*) FILTER (WHERE opcao = 'NAO') AS "votosNao"
			  FROM votos
			 WHERE sessao_id = :sessaoId
			""", nativeQuery = true)
	ResultadoVotosProjection countBySessaoId(@Param("sessaoId") Long sessaoId);

}
