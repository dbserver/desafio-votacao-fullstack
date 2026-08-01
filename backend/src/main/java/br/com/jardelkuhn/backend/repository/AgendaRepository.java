package br.com.jardelkuhn.backend.repository;

import br.com.jardelkuhn.backend.entity.Agenda;

import br.com.jardelkuhn.backend.entity.AgendaVotingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    @Query("""
        select new br.com.jardelkuhn.backend.entity.AgendaVotingProjection(
            a.id,
            a.title,
            a.description,
            a.openedDate,
            a.limitDate,

            /* Contabiliza votos YES */
            sum(
                case 
                    when v.voteOption = br.com.jardelkuhn.backend.entity.VoteOption.YES 
                    then 1 
                    else 0 
                end
            ),

            /* Contabiliza votos NO */
            sum(
                case 
                    when v.voteOption = br.com.jardelkuhn.backend.entity.VoteOption.NO 
                    then 1 
                    else 0 
                end
            ),

            /* Contabiliza votos TOTAL */
            count(v.id)
        )

        from Agenda a
        left join a.votes v
        group by 
            a.id,
            a.title,
            a.description,
            a.openedDate,
            a.limitDate
    """)
    List<AgendaVotingProjection> findAllAgendaVotingProjection();
}