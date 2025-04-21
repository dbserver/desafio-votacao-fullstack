package com.db.votacaobackend.vote.dto;

import com.db.votacaobackend.vote.model.VoteOption;

public record VoteDetailDTO(Long voteId, VoteOption voteOption, String cpf) {

}
