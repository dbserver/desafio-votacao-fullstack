package com.db.votacaobackend.vote.repository;

import com.db.votacaobackend.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

  Vote findByCpfAndSectionId(String cpf, Long sectionId);
}

