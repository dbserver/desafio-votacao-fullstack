package com.service.polls.application.usecases;

import com.service.polls.domain.model.Poll;
import com.service.polls.ports.in.IGetPollsUseCase;
import com.service.polls.ports.out.IPollRepository;

public class GetPollUseCase implements IGetPollsUseCase {

    private final IPollRepository pollRepository;

    public GetPollUseCase(IPollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public Poll execute(Long pollId) {
        return pollRepository.findById(pollId);
    }
}
