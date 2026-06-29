package com.sousadev.voteapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssociateDto(

        @NotBlank
        String name,

        @NotBlank
        String document,

        @NotNull
        UUID pautaId
) {
}
