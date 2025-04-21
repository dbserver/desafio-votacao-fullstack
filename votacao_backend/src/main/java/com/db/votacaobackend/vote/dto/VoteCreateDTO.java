package com.db.votacaobackend.vote.dto;

import com.db.votacaobackend.vote.model.VoteOption;

public record VoteCreateDTO(Long sectionId, VoteOption voteOption, String cpf) {

}
