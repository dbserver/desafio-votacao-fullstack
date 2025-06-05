package br.dev.ederson.spring.cooperativa.repository;

import br.dev.ederson.spring.cooperativa.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("SELECT s FROM Session s WHERE s.startDate IS NULL AND s.endDate IS NULL")
    List<Session> getAllSessionsNotInitialized();
}
