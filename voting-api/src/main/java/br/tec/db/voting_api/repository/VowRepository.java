package br.tec.db.voting_api.repository;

import br.tec.db.voting_api.domain.Vow;
import br.tec.db.voting_api.domain.enums.VowType;
import br.tec.db.voting_api.repository.projection.VotingResultProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VowRepository extends JpaRepository<Vow, Long> {

    @Query("""
    SELECT
        COUNT(v.id)
    FROM Vow v
    WHERE v.agenda.id = :agendaId
    """)
    Long countVotes(Long agendaId);

    @Query("""
    SELECT
        SUM(CASE WHEN v.vote = 'YES' THEN 1 ELSE 0 END) AS totalYes,
        SUM(CASE WHEN v.vote = 'NO' THEN 1 ELSE 0 END) AS totalNo
    FROM Vow v
    WHERE v.agenda.id = :agendaId
    """)
    VotingResultProjection countResult(Long agendaId);

    boolean existsByAssociatedIdAndAgendaId(String associatedId, Long agendaId);
}
