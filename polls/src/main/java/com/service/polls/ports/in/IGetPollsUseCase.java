package com.service.polls.ports.in;

import com.service.polls.domain.model.Poll;

public interface IGetPollsUseCase {
    Poll execute(Long pollId);
}
