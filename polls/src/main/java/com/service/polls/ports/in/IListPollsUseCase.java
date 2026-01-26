package com.service.polls.ports.in;

import com.service.polls.adapters.out.persistence.entities.PollEntity;
import org.springframework.data.domain.Page;

public interface IListPollsUseCase {
    Page<PollEntity> execute(int page, int size);
}
