package com.sarapio.votacao_api.repositories;

import com.sarapio.votacao_api.domain.associate.Associate;
import com.sarapio.votacao_api.domain.session.Session;
import com.sarapio.votacao_api.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    List<Vote> findBySession(Session session);
    Vote findByAssociateAndSession(Associate associate, Session session);
}
