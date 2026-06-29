package com.sousadev.voteapp.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record PautaDto(

        @NotBlank
        String name,

        String description,

        LocalDateTime expireIn

) {
}