package br.tec.db.voting_api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgendaInputDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String title,

        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
        String description

) { }
