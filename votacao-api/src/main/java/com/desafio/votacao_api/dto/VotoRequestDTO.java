package com.desafio.votacao_api.dto;

import com.desafio.votacao_api.model.enums.VotoValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

/**
 * Request payload to register a vote in a voting session.
 *
 * @param sessaoVotacaoId voting session identifier
 * @param cpf voter CPF with 11 digits
 * @param vote vote value
 */
public record VotoRequestDTO(
        @NotNull(message = "The voting session id is required.")
        UUID sessaoVotacaoId,

        @NotBlank(message = "The voter CPF is required and must not be blank.")
        @Pattern(regexp = "\\d{11}", message = "The CPF must contain exactly 11 digits.")
        String cpf,

        @NotNull(message = "The vote value is required.")
        VotoValue vote
) {
}

