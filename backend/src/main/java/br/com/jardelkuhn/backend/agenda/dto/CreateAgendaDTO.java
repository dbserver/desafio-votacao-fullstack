package br.com.jardelkuhn.backend.agenda.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAgendaDTO(
        @NotNull(message = "O título deve estar preenchido")
        @Size(min = 2, max = 60, message = "O título deve ser maior que 2 e não exceder 120 caracteres")
        String title,

        @NotNull(message = "A descrição não pode ser nula")
        @Size(max = 255, message = "A descrição não deve ter mais que 255 caracteres")
        String description
) {
}
