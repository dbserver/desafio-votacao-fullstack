package com.dbchal.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbchal.voting.domain.entity.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {

	boolean existsBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

}
