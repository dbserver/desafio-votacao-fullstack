package com.desafio.votacao_api.repository;

import com.desafio.votacao_api.model.Voto;
import com.desafio.votacao_api.model.enums.VotoValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for persisting and retrieving Voto entities.
 */
@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {

    /**
     * Finds all votes for a specific voting session.
     *
     * @param sessaoVotacaoId the voting session ID
     * @return a list of votes in the session
     */
    @Query("SELECT v FROM Voto v WHERE v.sessaoVotacao.id = :sessaoVotacaoId AND v.isActive = true")
    List<Voto> findAllBySessaoVotacaoId(@Param("sessaoVotacaoId") UUID sessaoVotacaoId);

    /**
     * Counts votes with a specific value in a voting session.
     *
     * @param sessaoVotacaoId the voting session ID
     * @param voteValue the vote value to count (SIM/NAO)
     * @return the count of votes matching the criteria
     */
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.sessaoVotacao.id = :sessaoVotacaoId AND v.vote = :voteValue AND v.isActive = true")
    long countBySessaoVotacaoIdAndVoteValue(@Param("sessaoVotacaoId") UUID sessaoVotacaoId, @Param("voteValue") VotoValue voteValue);

    /**
     * Checks whether a CPF has already cast an active vote in a specific agenda.
     *
     * @param pautaId agenda ID
     * @param cpf CPF number
     * @return true when a vote already exists for this agenda/CPF
     */
    @Query("SELECT COUNT(v) > 0 FROM Voto v WHERE v.sessaoVotacao.pauta.id = :pautaId AND v.cpf.number = :cpf AND v.isActive = true")
    boolean existsActiveVoteByPautaIdAndCpf(@Param("pautaId") UUID pautaId, @Param("cpf") String cpf);
}



