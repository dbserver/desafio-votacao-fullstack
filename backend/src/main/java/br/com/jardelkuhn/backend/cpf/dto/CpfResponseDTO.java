package br.com.jardelkuhn.backend.cpf.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da validação de CPF")
public record CpfResponseDTO(Status status) {
    public enum Status {
        @Schema(description = "CPF válido e associado apto a votar")
        ABLE_TO_VOTE,

        @Schema(description = "CPF válido, porém associado não apto a votar")
        UNABLE_TO_VOTE;
    }
}
