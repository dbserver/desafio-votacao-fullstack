package com.db.votacaobackend.vote.mapper;

import com.db.votacaobackend.vote.dto.VoteDetailDTO;
import com.db.votacaobackend.vote.model.Vote;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {

  public VoteDetailDTO toDto(Vote vote) {
    return new VoteDetailDTO(
        vote.getId(),
        vote.getVoteOption(),
        vote.getCpf()
    );
  }

}
