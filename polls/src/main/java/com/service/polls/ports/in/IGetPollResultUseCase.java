package com.service.polls.ports.in;

import com.service.polls.application.dto.PollResultDTO;

public interface IGetPollResultUseCase {
    public PollResultDTO execute(Long pollId);
}
