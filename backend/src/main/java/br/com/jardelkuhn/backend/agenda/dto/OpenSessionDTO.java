package br.com.jardelkuhn.backend.agenda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OpenSessionDTO(
        @NotNull(message = "O tempo mínimo é 1 minuto")
        @Min(value = 1, message = "O tempo mínimo é 1 minuto")
        Integer duration
) {
}
