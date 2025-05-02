package com.desafioVotacaoBackend.desafioVotacaoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndCpf(Long pautaId, String cpf);

}
