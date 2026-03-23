package br.tec.db.voting_api.dto.input;

import br.tec.db.voting_api.domain.enums.VowType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VowInputDTO(

        @NotNull(message = "O associadoId é obrigatório")
        Long agendaId,

        @NotBlank(message = "O associadoId é obrigatório")
        String associatedId,

        @NotNull(message = "O voto deve ser YES ou NO")
        VowType vote
) { }
