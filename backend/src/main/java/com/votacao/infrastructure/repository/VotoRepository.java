package com.votacao.infrastructure.repository;

import com.votacao.domain.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    Optional<Voto> findBySessaoVotacaoId(Long sessaoVotacaoId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.sessaoVotacao.id = :id AND v.escolha = 'SIM'")
    long contarVotosSim(@Param("id") Long id);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.sessaoVotacao.id = :id AND v.escolha = 'NAO'")
    long contarVotosNao(@Param("id") Long id);

}
