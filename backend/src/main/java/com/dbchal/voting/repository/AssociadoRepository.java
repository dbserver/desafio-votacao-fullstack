package com.dbchal.voting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbchal.voting.domain.entity.Associado;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {

	boolean existsByCpf(String cpf);

	Optional<Associado> findByCpf(String cpf);

}
