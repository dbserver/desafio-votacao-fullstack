package com.votacao.desafiovotacao.infra;

import com.votacao.desafiovotacao.domain.entities.Session;
import com.votacao.desafiovotacao.domain.entities.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    // Search all votes from a session
    List<Vote> findAllBySession(Optional<Session> session);

    List<Vote> findAllBySessionId(String sessionId);
}
