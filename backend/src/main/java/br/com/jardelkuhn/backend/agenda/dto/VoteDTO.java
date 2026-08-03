package br.com.jardelkuhn.backend.agenda.dto;

import br.com.jardelkuhn.backend.agenda.entity.VoteOption;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VoteDTO(
        @NotNull(message = "O ID do associado deve estar preenchido")
        @Size(min = 1, max = 60, message = "O ID do associado deve estar preenchido")
        String associateId,

        @NotNull(message = "A opção do voto deve ser informada")
        VoteOption option
) { }
