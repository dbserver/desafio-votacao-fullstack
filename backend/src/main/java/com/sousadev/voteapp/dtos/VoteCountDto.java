package com.sousadev.voteapp.dtos;

import lombok.Builder;

@Builder
public record VoteCountDto(
        Votes votes
) {
    @Builder
    public record Votes(
            int sim,
            int nao
    ) {}
}
