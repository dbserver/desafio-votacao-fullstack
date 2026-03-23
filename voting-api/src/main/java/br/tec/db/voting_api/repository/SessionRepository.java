package br.tec.db.voting_api.repository;

import br.tec.db.voting_api.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByAgendaId(Long agendaId);
}
