package com.service.polls.application.usecases;

import com.service.polls.application.dto.PollDTO;
import com.service.polls.domain.model.Poll;
import com.service.polls.ports.in.ICreatePollUseCase;
import com.service.polls.ports.out.IPollRepository;

public class CreatePollUseCase implements ICreatePollUseCase {

    private final IPollRepository pollRepository;

    public CreatePollUseCase(IPollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public Poll execute(PollDTO pollDTO) {

        Poll poll = Poll.of(pollDTO);

        this.pollRepository.save(poll);

        return poll;
    }
}
