package com.service.polls.ports.in;

import com.service.polls.application.dto.VoteDTO;

public interface IVoteUseCase {
    void execute(VoteDTO voteDTO);
}
