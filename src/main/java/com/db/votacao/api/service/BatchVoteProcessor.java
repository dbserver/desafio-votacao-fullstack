package com.db.votacao.api.service;

import com.db.votacao.api.model.Vote;
import com.db.votacao.api.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchVoteProcessor {
    private final VoteRepository voteRepository;

    public BatchVoteProcessor(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Async
    @Transactional
    public void processBatchVotes(List<Vote> votes) {
        voteRepository.saveAll(votes);
    }}