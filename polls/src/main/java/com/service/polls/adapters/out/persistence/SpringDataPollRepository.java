package com.service.polls.adapters.out.persistence;

import com.service.polls.adapters.out.persistence.entities.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPollRepository extends JpaRepository<PollEntity, Long> { }
