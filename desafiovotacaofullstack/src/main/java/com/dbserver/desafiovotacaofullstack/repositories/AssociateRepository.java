package com.dbserver.desafiovotacaofullstack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbserver.desafiovotacaofullstack.domains.Associate;

public interface AssociateRepository extends JpaRepository<Associate, Integer> {
	public boolean existsByCpf(String cpf);
}
