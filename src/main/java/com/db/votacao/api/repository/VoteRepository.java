package com.db.votacao.api.repository;

import com.db.votacao.api.model.Vote;
import com.db.votacao.api.model.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsBySessionAndAssociateId(VotingSession session, String associateId);

    long countBySessionAndVoteValue(VotingSession session, boolean voteValue);
}