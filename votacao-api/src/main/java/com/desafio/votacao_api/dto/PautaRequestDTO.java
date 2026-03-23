package com.desafio.votacao_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload used to create a new agenda (pauta).
 *
 * @param name agenda title
 * @param description optional agenda description
 */
public record PautaRequestDTO(
        @NotBlank(message = "The Pauta title is required and must not be blank.")
        @Size(min = 5, max = 100, message = "The title must contain between 5 and 100 characters.")
        String name,

        @Size(max = 500, message = "The description must not exceed 500 characters.")
        String description
) {
}
