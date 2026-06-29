package com.sousadev.voteapp.dtos;

import com.sousadev.voteapp.enums.VoteValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VoteDto(

        @NotBlank
        String document,

        @NotNull
        UUID pautaId,

        @NotNull
        VoteValue value
) {
}
