package com.votacao.desafiovotacao.infra;

import com.votacao.desafiovotacao.domain.entities.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    // Consult open sessions
    List<Session> findByStatus(Session.StatusSession status);
}
