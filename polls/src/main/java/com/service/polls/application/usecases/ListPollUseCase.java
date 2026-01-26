package com.service.polls.application.usecases;

import com.service.polls.adapters.out.persistence.entities.PollEntity;
import com.service.polls.ports.in.IListPollsUseCase;
import com.service.polls.ports.out.IPollRepository;
import org.springframework.data.domain.Page;

public class ListPollUseCase implements IListPollsUseCase {

    private final IPollRepository pollRepository;

    public ListPollUseCase(IPollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public Page<PollEntity> execute(int page, int size) {
        return pollRepository.findAll(page -1, size);
    }
}
