package com.service.polls.ports.in;

import com.service.polls.application.dto.PollDTO;
import com.service.polls.domain.model.Poll;

public interface ICreatePollUseCase {
    Poll execute(PollDTO pollDTO);
}
