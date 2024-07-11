package com.dbserver.desafiovotacaofullstack.dtos;

import java.util.List;

public record TotalVotesDto(Long totalVotes, Long totalVotesSim, Long totalVotesNao, List<VoteResponseDto> votesResponseDto) {
}
