package br.tec.db.voting_api.repository;

import br.tec.db.voting_api.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
