package br.com.jardelkuhn.backend.repository;

import br.com.jardelkuhn.backend.entity.Agenda;
import br.com.jardelkuhn.backend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByAgendaAndAssociateId(Agenda agenda, String associateId);
}
