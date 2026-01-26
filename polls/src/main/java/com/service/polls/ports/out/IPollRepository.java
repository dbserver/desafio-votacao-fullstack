package com.service.polls.ports.out;

import com.service.polls.adapters.out.persistence.entities.PollEntity;
import com.service.polls.domain.model.Poll;
import org.springframework.data.domain.Page;

public interface IPollRepository {
    Poll save(Poll poll);
    Poll findById(Long id);
    Page<PollEntity> findAll(int page, int size);
}
