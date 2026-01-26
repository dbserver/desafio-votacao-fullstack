package com.service.polls.application.usecases;

import com.service.polls.application.dto.PollResultDTO;
import com.service.polls.application.enums.AnswerEnum;
import com.service.polls.ports.in.IGetPollResultUseCase;
import com.service.polls.ports.out.IVoteCacheProvider;

public class GetPollResultUseCase implements IGetPollResultUseCase {

    private final IVoteCacheProvider cacheProvider;

    public GetPollResultUseCase(IVoteCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    @Override
    public PollResultDTO execute(Long pollId) {
        long yes = cacheProvider.countVotes(pollId, AnswerEnum.YES);
        long no = cacheProvider.countVotes(pollId, AnswerEnum.NO);
        return new PollResultDTO(yes, no);
    }
}
