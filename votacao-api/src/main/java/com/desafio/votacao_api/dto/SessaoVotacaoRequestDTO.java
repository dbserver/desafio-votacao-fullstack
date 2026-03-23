package com.desafio.votacao_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * Request payload to open a voting session for an agenda.
 *
 * @param pautaId agenda identifier
 * @param durationInSeconds session duration in seconds; when null or less than or equal to zero, default duration is applied
 */
public record SessaoVotacaoRequestDTO(
        @NotNull(message = "The agenda id is required.")
        UUID pautaId,

        @Positive(message = "The session duration must be greater than zero.")
        Integer durationInSeconds
) {
}



