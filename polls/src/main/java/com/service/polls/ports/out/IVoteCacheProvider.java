package com.service.polls.ports.out;

import com.service.polls.application.enums.AnswerEnum;
import com.service.polls.domain.model.Vote;

public interface IVoteCacheProvider {
    void validateVote(Vote vote);
    void increment(Vote vote);
    long countVotes(Long pollId, AnswerEnum answer);
}
