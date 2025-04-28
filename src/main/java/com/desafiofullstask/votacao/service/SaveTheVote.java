package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Vote;
import com.desafiofullstask.votacao.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SaveTheVote {

    private static final Logger logger = LoggerFactory.getLogger(AssociatedService.class);

    private final VoteRepository voteRepository;

    public SaveTheVote(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Async("voteExecutor")
    public void saveVote(Vote vote) {
        voteRepository.save(vote);
        logger.info("Vote saved successfully");
    }
}
