package com.sousadev.voteapp.dtos;

import com.sousadev.voteapp.enums.CpfVoteStatus;

public record CpfValidationResponseDto(
        CpfVoteStatus status
) {
}
