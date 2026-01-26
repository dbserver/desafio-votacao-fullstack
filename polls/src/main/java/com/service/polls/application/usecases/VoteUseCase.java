package com.service.polls.application.usecases;

import com.service.polls.application.dto.VoteDTO;
import com.service.polls.domain.exceptions.VotingClosedException;
import com.service.polls.domain.model.Poll;
import com.service.polls.domain.model.Vote;
import com.service.polls.ports.in.IVoteUseCase;
import com.service.polls.ports.out.IPollRepository;
import com.service.polls.ports.out.IVoteCacheProvider;

import java.time.Instant;

public class VoteUseCase implements IVoteUseCase {

    private final IPollRepository pollRepository;
    private final IVoteCacheProvider cacheProvider;

    public VoteUseCase(IPollRepository pollRepository, IVoteCacheProvider cacheProvider) {
        this.pollRepository = pollRepository;
        this.cacheProvider = cacheProvider;
    }

    @Override
    public void execute(VoteDTO voteDTO) {
        Poll poll = pollRepository.findById(voteDTO.getPollId());

        if(!poll.isOpen(Instant.now())) {
            throw new VotingClosedException();
        }
        var vote = Vote.of(voteDTO);

        cacheProvider.validateVote(vote);
        cacheProvider.increment(vote);
//        publisherPort.publish(vote);
    }
}
