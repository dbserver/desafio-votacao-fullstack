package com.db.votacao.api.service;

import com.db.votacao.api.exception.ResourceNotFoundException;
import com.db.votacao.api.model.VotingResult;
import com.db.votacao.api.model.VotingSession;
import com.db.votacao.api.repository.VoteRepository;
import com.db.votacao.api.repository.VotingSessionRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VotingResultService {
    private final VoteRepository voteRepository;
    private final VotingSessionRepository sessionRepository;
    private final RedisService redisService;

    public VotingResultService(VoteRepository voteRepository, VotingSessionRepository sessionRepository, RedisService redisService) {
        this.voteRepository = voteRepository;
        this.sessionRepository = sessionRepository;
        this.redisService = redisService;
    }

    @Cacheable(value = "voteResults", key = "#sessionId", unless = "#result.getTotalVotes() == 0")
    public VotingResult getVotingResult(Long sessionId) {
        VotingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        long redisYesVotes = redisService.getVoteCount(sessionId, true);
        long redisNoVotes = redisService.getVoteCount(sessionId, false);

        long dbYesVotes = voteRepository.countBySessionAndVoteValue(session, true);
        long dbNoVotes = voteRepository.countBySessionAndVoteValue(session, false);

        long finalYesVotes = Math.max(redisYesVotes, dbYesVotes);
        long finalNoVotes = Math.max(redisNoVotes, dbNoVotes);

        return new VotingResult(finalYesVotes, finalNoVotes);
    }
}
