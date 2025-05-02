package com.desafioVotacaoBackend.desafioVotacaoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Associado;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
    boolean existsByCpf(String cpf);
}
