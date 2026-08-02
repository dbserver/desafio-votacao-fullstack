package br.com.jardelkuhn.backend.cpf.dto;

import jakarta.validation.constraints.NotBlank;

public record CpfRequestDTO(
        @NotBlank(message = "CPF é obrigatório")
        String cpf
) {
}
